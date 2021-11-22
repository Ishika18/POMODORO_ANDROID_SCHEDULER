package com.bcit.pomodoro_scheduler.goals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class FirebaseDB {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void deleteGoal(String userEmail, String goalID) {

    }

    public void addOrUpdateTask(String userEmail, Goal goal) {
        HashMap<String, Object> task = createTaskObject(goal);

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

    public HashMap<String, Object> createTaskObject(Goal goal) {
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

    public FirebaseFirestore getDb() {
        return this.db;
    }
}
