package com.bcit.pomodoro_scheduler.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.repositories.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SchedulesViewModel extends ViewModel implements ScheduleRepository.OnFirestoreTaskComplete {

    private final MutableLiveData<HashMap<LocalDate, ArrayList<Task>>>
            schedulesModelData = new MutableLiveData<>();

    public LiveData<HashMap<LocalDate, ArrayList<Task>>> getSchedulesModelData() {
        return schedulesModelData;
    }

    private final ScheduleRepository scheduleRepository = new ScheduleRepository(this);


    public SchedulesViewModel(String userEmail){
        scheduleRepository.getSchedulesData(userEmail);
    }
    
    @Override
    public void schedulesDataAdded(HashMap<LocalDate, ArrayList<Task>> schedulesModel) {
        schedulesModelData.setValue(schedulesModel);
    }

    @Override
    public void onError(Exception e) {
        Log.w("Fail", "Error getting documents.", e);
    }
}
