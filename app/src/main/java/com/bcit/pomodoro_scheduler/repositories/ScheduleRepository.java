package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.bcit.pomodoro_scheduler.model.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScheduleRepository {

    private final OnFirestoreTaskComplete onFirestoreTaskComplete;


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference taskRef = firebaseFirestore
            .collection("schedules");


    public ScheduleRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getSchedulesData(String userEmail){
        taskRef.document(userEmail).get()
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(onFirestoreTaskComplete::onError);
    }

    private Goal getScheduleDataFromMap(HashMap<String, Object> result) {
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

    private void onSuccess(DocumentSnapshot documentSnapshot) {
        HashMap<LocalDate, ArrayList<Task>> schedules = new HashMap<>();

        Map<String, Object> map = documentSnapshot.getData();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                HashMap<String, Object> result = (HashMap<String, Object>) entry.getValue();
//                    schedules.add(getScheduleDataFromMap(result));
            }

            onFirestoreTaskComplete.schedulesDataAdded(schedules);
        }
    }

    public interface OnFirestoreTaskComplete{
        void schedulesDataAdded(HashMap<LocalDate, ArrayList<Task>> schedulesModel);
        void onError(Exception e);
    }
}