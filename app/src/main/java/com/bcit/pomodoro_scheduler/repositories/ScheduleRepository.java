package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.model.TaskType;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private Task getTaskDataFromMap(HashMap<String, Object> result) {
        return new Task (
                (String) result.get("id"),
                (String) result.get("name"),
                ((Long) result.get("startTime")).intValue(),
                ((Long) result.get("endTime")).intValue(),
                TaskType.valueOf((String) result.get("type"))
        );
    }

    private void onSuccess(DocumentSnapshot documentSnapshot) {
        HashMap<LocalDate, ArrayList<Task>> schedules = new HashMap<>();

        Map<String, Object> map = documentSnapshot.getData();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                LocalDate key = LocalDate.parse(entry.getKey());
                schedules.put(key, new ArrayList<>());

                for (HashMap<String, Object> taskObj: (ArrayList<HashMap<String, Object>>) entry.getValue()) {
                    Task task = getTaskDataFromMap(taskObj);
                    Objects.requireNonNull(schedules.get(key)).add(task);
                }
            }

            onFirestoreTaskComplete.schedulesDataAdded(schedules);
        }
    }

    public interface OnFirestoreTaskComplete{
        void schedulesDataAdded(HashMap<LocalDate, ArrayList<Task>> schedulesModel);
        void onError(Exception e);
    }
}