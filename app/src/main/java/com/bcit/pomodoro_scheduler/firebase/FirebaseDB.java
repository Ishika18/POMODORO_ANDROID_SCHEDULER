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

import java.time.LocalDate;
import java.util.ArrayList;
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


    public void addOrUpdateSchedule(
            String userEmail, HashMap<LocalDate,
            ArrayList<com.bcit.pomodoro_scheduler.model.Task>> schedule
    ) {
        for (Map.Entry<LocalDate, ArrayList<com.bcit.pomodoro_scheduler.model.Task>>
                entry : schedule.entrySet()) {
            db.collection("schedules").document(userEmail)
                    .set(createScheduleObject(entry.getKey(), entry.getValue()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Worked", "schedule added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", e.getLocalizedMessage());
                        }
                    });
        }
    }

    public HashMap<String, Object> createScheduleObject(
            LocalDate key, ArrayList<com.bcit.pomodoro_scheduler.model.Task> tasks
    ) {
        HashMap<String, Object> scheduleObject = new HashMap<>();
        scheduleObject.put(key.toString(), createTasksListObject(tasks));
        return scheduleObject;
    }

    public ArrayList<Object> createTasksListObject (
            ArrayList<com.bcit.pomodoro_scheduler.model.Task> tasks
    ) {
        ArrayList<Object> taskObjects = new ArrayList<>();
        for (com.bcit.pomodoro_scheduler.model.Task task: tasks) {
            taskObjects.add(createTaskObject(task));
        }
        return taskObjects;
    }

    public HashMap<String, Object> createTaskObject (
            com.bcit.pomodoro_scheduler.model.Task task
    ) {
        HashMap<String, Object> taskObject = new HashMap<>();
        taskObject.put("id", task.getID());
        taskObject.put("name", task.getName());
        taskObject.put("startTime", task.getEndTime());
        taskObject.put("endTime", task.getEndTime());
        taskObject.put("type", task.getType().name());
        return taskObject;
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }
}
