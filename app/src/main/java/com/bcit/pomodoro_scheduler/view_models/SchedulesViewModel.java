package com.bcit.pomodoro_scheduler.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.repositories.ScheduleRepository;

import java.util.List;

public class SchedulesViewModel extends ViewModel implements ScheduleRepository.OnFirestoreTaskComplete {

    private final MutableLiveData<List<Goal>> goalsModelData = new MutableLiveData<>();

    public LiveData<List<Goal>> getSchedulesModelData() {
        return goalsModelData;
    }

    private final ScheduleRepository scheduleRepository = new ScheduleRepository(this);


    public SchedulesViewModel(String userEmail){
        scheduleRepository.getSchedulesData(userEmail);
    }
    
    @Override
    public void goalsDataAdded(List<Goal> goalsModels) {
        goalsModelData.setValue(goalsModels);
    }

    @Override
    public void onError(Exception e) {
        Log.w("Fail", "Error getting documents.", e);
    }
}
