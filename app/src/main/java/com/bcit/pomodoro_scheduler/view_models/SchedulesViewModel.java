package com.bcit.pomodoro_scheduler.view_models;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.repositories.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class SchedulesViewModel extends ViewModel implements ScheduleRepository.OnFirestoreTaskComplete {

    private final String userEmail;

    private final MutableLiveData<HashMap<LocalDate, ArrayList<Task>>>
            schedulesModelData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> scheduleDataUpdated = new MutableLiveData<>();

    public LiveData<HashMap<LocalDate, ArrayList<Task>>> getSchedulesModelData() {
        return schedulesModelData;
    }

    public LiveData<Boolean> updateScheduleData(String userEmail, HashMap<LocalDate,
            ArrayList<com.bcit.pomodoro_scheduler.model.Task>> schedule) {
        scheduleRepository.addOrUpdateSchedule(userEmail, schedule);
        return scheduleDataUpdated;
    }

    private final ScheduleRepository scheduleRepository = new ScheduleRepository(this);


    public SchedulesViewModel(String userEmail) {
        this.userEmail = userEmail;
        scheduleRepository.createDocForNewUser(this.userEmail);
        scheduleRepository.getSchedulesData(userEmail);
    }
    
    @Override
    public void schedulesDataAdded(HashMap<LocalDate, ArrayList<Task>> schedulesModel) {
        schedulesModelData.setValue(schedulesModel);
    }

    @Override
    public void scheduleDataUpdated() {
        scheduleDataUpdated.setValue(Boolean.TRUE);
    }

    @Override
    public void onErrorUpdateScheduleData(Exception e) {
        scheduleDataUpdated.setValue(Boolean.FALSE);
    }

    @Override
    public void onErrorGetScheduleData(Exception e) {
        Log.w("Fail", "Error getting documents.", e);
    }
}
