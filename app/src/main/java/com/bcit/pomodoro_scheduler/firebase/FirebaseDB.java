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
