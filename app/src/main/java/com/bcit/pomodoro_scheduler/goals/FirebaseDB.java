package com.bcit.pomodoro_scheduler.goals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class FirebaseDB {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        taskDescription.put("title", goal.getTitle());
        taskDescription.put("location", goal.getLocation());
        taskDescription.put("taskTimeInMinutes", goal.getTaskTimeInMinutes());
        taskDescription.put("deadline", goal.getDeadline());
        taskDescription.put("priority", goal.getPriority());
        taskDescription.put("url", goal.getUrl());
        taskDescription.put("notes", goal.getNotes());
        task.put(goal.getId(), taskDescription);
        return task;
    }

    public void getUsersTasks(String userEmail) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Sucess", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Fail", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public FirebaseFirestore getDb() {
        return this.db;
    }
}
