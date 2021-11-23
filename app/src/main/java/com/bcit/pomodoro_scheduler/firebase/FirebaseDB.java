package com.bcit.pomodoro_scheduler.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class FirebaseDB {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void deleteGoal(String userEmail, String goalID) {
        Map<String, Object> deleteGoal = new HashMap<>();
        deleteGoal.put(goalID, FieldValue.delete());
        db.collection("tasks").document(userEmail)
                .update(deleteGoal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DELETE", "Goal deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DELETE_GOAL_ERROR", e.getMessage());
            }
        });
    }

    public void addOrUpdateGoal(String userEmail, Goal goal) {
        HashMap<String, Object> task = createGoalObject(goal);

        db.collection("tasks").document(userEmail)
                .set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Worked", "data added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", e.getLocalizedMessage());
                    }
                });
    }

    public HashMap<String, Object> createGoalObject(Goal goal) {
        HashMap<String, Object> task = new HashMap<>();
        HashMap<String, Object> taskDescription = new HashMap<>();
        taskDescription.put("id", goal.getId());
        taskDescription.put("name", goal.getName());
        taskDescription.put("location", goal.getLocation());
        taskDescription.put("totalTimeInMinutes", goal.getTotalTimeInMinutes());
        taskDescription.put("deadline", goal.getDeadline());
        taskDescription.put("priority", goal.getPriority().name());
        taskDescription.put("url", goal.getUrl());
        taskDescription.put("notes", goal.getNotes());
        task.put(goal.getId(), taskDescription);
        return task;
    }

    public void getUsersTasks(String userEmail) {
        db.collection("users").document(userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("USER_TASKS", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("ERROR", "No such user");
                        }
                    }
                });
    }

    public void addOrUpdateCommitment(String userEmail, Commitment commitment) {
        HashMap<String, Object> commitmentObject = createCommitmentObject(commitment);

        db.collection("commitments").document(userEmail)
                .set(commitmentObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Worked", "commitment added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", e.getLocalizedMessage());
                    }
                });
    }

    public HashMap<String, Object> createCommitmentObject(Commitment commitment) {
        HashMap<String, Object> commitmentObject = new HashMap<>();
        HashMap<String, Object> commitmentDescription = new HashMap<>();
        commitmentDescription.put("id", commitment.getId());
        commitmentDescription.put("name", commitment.getName());
        commitmentDescription.put("location", commitment.getLocation());
        commitmentDescription.put("startTime", commitment.getEndTime());
        commitmentDescription.put("endTime", commitment.getEndTime());
        commitmentDescription.put("repeat", commitment.getRepeat().name());
        commitmentObject.put(commitment.getId(), commitmentDescription);
        return commitmentObject;
    }

    public void deleteCommitment(String userEmail, String commitmentID) {
        Map<String, Object> deleteCommitment = new HashMap<>();
        deleteCommitment.put(commitmentID, FieldValue.delete());
        db.collection("commitments").document(userEmail)
                .update(deleteCommitment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("DELETE", "Commitment deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("DELETE_COMMITMENT_ERROR", e.getMessage());
            }
        });
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }
}
