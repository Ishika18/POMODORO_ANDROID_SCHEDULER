package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoalRepository {

    private final OnFirestoreTaskComplete onFirestoreTaskComplete;


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = firebaseFirestore.collection("goals");

    public GoalRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getGoalsData(String userEmail){
        taskRef.document(userEmail).get().addOnSuccessListener(documentSnapshot -> {
            List<Goal> goals = new ArrayList<>();

            Map<String, Object> map = documentSnapshot.getData();
            if (map != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    HashMap<String, Object> result = (HashMap<String, Object>) entry.getValue();
                    goals.add(getGoalFromDocumentMap(result));
                }
                onFirestoreTaskComplete.goalsDataAdded(goals);
            }
        }).addOnFailureListener(onFirestoreTaskComplete::onError);
    }

    private Goal getGoalFromDocumentMap(HashMap<String, Object> result) {
        return new Goal (
                (String) result.get("id"),
                (String) result.get("name"),
                (String) result.get("location"),
                ((Long) result.get("totalTimeInMinutes")).intValue(),
                (Timestamp) result.get("deadline"),
                Priority.valueOf((String) result.get("priority")),
                (String) result.get("notes"),
                (String) result.get("url")
        );
    }

    public interface  OnFirestoreTaskComplete{
        void goalsDataAdded(List<Goal> goalsModels);
        void onError(Exception e);
    }
}