package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.model.TaskType;
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
                .addOnFailureListener(onFirestoreTaskComplete::onErrorGetScheduleData);
    }

    private Task getTaskDataFromMap(HashMap<String, Object> result) {
        return new Task (
                (String) result.get("id"),
                (String) result.get("name"),
                ((Long) result.get("startTime")).intValue(),
                ((Long) result.get("endTime")).intValue(),
                0,
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

    public void addOrUpdateSchedule(
            String userEmail, HashMap<LocalDate,
            ArrayList<com.bcit.pomodoro_scheduler.model.Task>> schedule
    ) {
        for (Map.Entry<LocalDate, ArrayList<com.bcit.pomodoro_scheduler.model.Task>>
                entry : schedule.entrySet()) {
            taskRef.document(userEmail)
                    .set(createScheduleObject(entry.getKey(), entry.getValue()))
                    .addOnSuccessListener(unused -> onFirestoreTaskComplete.scheduleDataUpdated())
                    .addOnFailureListener(onFirestoreTaskComplete::onErrorUpdateScheduleData);
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

    public interface OnFirestoreTaskComplete{
        void schedulesDataAdded(HashMap<LocalDate, ArrayList<Task>> schedulesModel);
        void scheduleDataUpdated();
        void onErrorUpdateScheduleData(Exception e);
        void onErrorGetScheduleData(Exception e);
    }
}