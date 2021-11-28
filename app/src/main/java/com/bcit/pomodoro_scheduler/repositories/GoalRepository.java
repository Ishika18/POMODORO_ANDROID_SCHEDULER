package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
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
        }).addOnFailureListener(onFirestoreTaskComplete::onErrorGetGoalsData);
    }

    public void deleteGoal(String userEmail, String goalID) {
        Map<String, Object> deleteGoal = new HashMap<>();
        deleteGoal.put(goalID, FieldValue.delete());
        taskRef.document(userEmail).update(deleteGoal)
                .addOnSuccessListener(unused -> onFirestoreTaskComplete.goalDataDeleted())
                .addOnFailureListener(onFirestoreTaskComplete::onErrorGoalDataDeleted);
    }

    public void addOrUpdateGoal(String userEmail, Goal goal) {
        HashMap<String, Object> task = createGoalObject(goal);

        taskRef.document(userEmail)
                .update(task)
                .addOnSuccessListener(unused -> onFirestoreTaskComplete.goalDataUpdated())
                .addOnFailureListener(onFirestoreTaskComplete::onErrorGoalDataUpdated);
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

    public interface OnFirestoreTaskComplete{
        void goalsDataAdded(List<Goal> goalsModels);
        void goalDataUpdated();
        void goalDataDeleted();
        void onErrorGoalDataDeleted(Exception e);
        void onErrorGetGoalsData(Exception e);
        void onErrorGoalDataUpdated(Exception e);
    }
}