package com.bcit.pomodoro_scheduler.repositories;


import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
                    Repeat repeat = Repeat.valueOf((String) result.get("repeat"));
                    Objects.requireNonNull(commitmentRepeats.get(repeat)).
                            add(getCommitmentFromDocumentMap(result));
                }
                onFirestoreTaskComplete.commitmentsDataAdded(commitmentRepeats);
            }
        }).addOnFailureListener(onFirestoreTaskComplete::onError);
    }

    private HashMap<Repeat, List<Commitment>> getCommitmentRepeatHashmap() {
        HashMap<Repeat, List<Commitment>> commitmentRepeats = new HashMap<>();
        for (Repeat repeat: Repeat.values()) {
            commitmentRepeats.put(repeat, new ArrayList<>());
        }
        return commitmentRepeats;
    }

    private Commitment getCommitmentFromDocumentMap(HashMap<String, Object> result) {
        return new Commitment (
                (String) result.get("id"),
                (String) result.get("name"),
                (String) result.get("location"),
                (Timestamp) result.get("startTime"),
                (Timestamp) result.get("endTime"),
                Repeat.valueOf((String) result.get("repeat"))
        );
    }

    public interface  OnFirestoreTaskComplete{
        void commitmentsDataAdded(HashMap<Repeat, List<Commitment>> commitmentsModel);
        void onError(Exception e);
    }
}