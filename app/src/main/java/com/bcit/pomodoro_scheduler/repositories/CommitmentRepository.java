package com.bcit.pomodoro_scheduler.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class CommitmentRepository {

    private final OnFirestoreTaskComplete onFirestoreTaskComplete;


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = firebaseFirestore
            .collection("commitments");

    public CommitmentRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getCommitmentsData(String userEmail){
        taskRef.document(userEmail).get().addOnSuccessListener(documentSnapshot -> {
            HashMap<Repeat, List<Commitment>> commitmentRepeats = getCommitmentRepeatHashmap();


            Map<String, Object> map = documentSnapshot.getData();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    HashMap<String, Object> result = (HashMap<String, Object>) entry.getValue();
                    Repeat repeat = Repeat.valueOf(
                                        ((String) Objects.requireNonNull(result.get("repeat")))
                                    );

                    if (repeat != Repeat.DAILY) {
                        Objects.requireNonNull(commitmentRepeats.get(repeat)).
                                add(getCommitmentFromDocumentMap(result));
                    } else {
                        Stream.of(Repeat.values())
                                .forEach(
                                        c -> {
                                            if (c != Repeat.DAILY) {
                                                Objects.requireNonNull(commitmentRepeats
                                                        .get(c))
                                                        .add(getCommitmentFromDocumentMap(result));
                                            }
                                        }
                                );
                    }
                }
                onFirestoreTaskComplete.commitmentsDataAdded(commitmentRepeats);
            }
        }).addOnFailureListener(onFirestoreTaskComplete::onErrorGetCommitmentData);
    }

    public void addOrUpdateCommitment(String userEmail, Commitment commitment) {
        HashMap<String, Object> commitmentObject = createCommitmentObject(commitment);

        taskRef.document(userEmail)
                .update(commitmentObject)
                .addOnSuccessListener(unused -> {
                    onFirestoreTaskComplete.onCommitmentUpdated();
                })
                .addOnFailureListener(onFirestoreTaskComplete::onErrorUpdateCommitmentData);
    }

    public void deleteCommitment(String userEmail, String commitmentID) {
        Map<String, Object> deleteCommitment = new HashMap<>();
        deleteCommitment.put(commitmentID, FieldValue.delete());
        taskRef.document(userEmail)
                .update(deleteCommitment)
                .addOnSuccessListener(unused -> onFirestoreTaskComplete.onCommitmentDeleted())
                .addOnFailureListener(onFirestoreTaskComplete::onErrorDeleteCommitmentData);
    }

    public HashMap<String, Object> createCommitmentObject(Commitment commitment) {
        HashMap<String, Object> commitmentObject = new HashMap<>();
        HashMap<String, Object> commitmentDescription = new HashMap<>();
        commitmentDescription.put("id", commitment.getId());
        commitmentDescription.put("name", commitment.getName());
        commitmentDescription.put("location", commitment.getLocation());
        commitmentDescription.put("startTime", commitment.getStartTime());
        commitmentDescription.put("endTime", commitment.getEndTime());
        commitmentDescription.put("repeat", commitment.getRepeat().name());
        commitmentDescription.put("url", commitment.getUrl());
        commitmentDescription.put("notes", commitment.getNotes());
        commitmentObject.put(commitment.getId(), commitmentDescription);
        return commitmentObject;
    }

    private HashMap<Repeat, List<Commitment>> getCommitmentRepeatHashmap() {
        HashMap<Repeat, List<Commitment>> commitmentRepeats = new HashMap<>();
        for (Repeat repeat: Repeat.values()) {
            if (repeat != Repeat.DAILY) {
                commitmentRepeats.put(repeat, new ArrayList<>());
            }
        }
        return commitmentRepeats;
    }

    private Commitment getCommitmentFromDocumentMap(HashMap<String, Object> result) {
        return new Commitment(
                (String) result.get("id"),
                (String) result.get("name"),
                (String) result.get("location"),
                (Timestamp) result.get("startTime"),
                (Timestamp) result.get("endTime"),
                Repeat.valueOf((String) result.get("repeat")),
                (String) result.get("url"),
                (String) result.get("notes")
        );
    }

    public void createDocForNewUser(String userEmail) {
        HashMap<String, Object> data = new HashMap<>();
        taskRef.document(userEmail).set(data, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d("COMMITMENT_DOC", "new doc created or merged with prev");
                }).addOnFailureListener(e -> {
                    Log.d("COMMITMENT_DOC_FAIL", "error while creating/merging user doc.");
        }       );
    }

    public interface OnFirestoreTaskComplete {
        void commitmentsDataAdded(HashMap<Repeat, List<Commitment>> commitmentsModel);
        void onCommitmentDeleted();
        void onCommitmentUpdated();
        void onErrorGetCommitmentData(Exception e);
        void onErrorUpdateCommitmentData(Exception e);
        void onErrorDeleteCommitmentData(Exception e);
    }
}