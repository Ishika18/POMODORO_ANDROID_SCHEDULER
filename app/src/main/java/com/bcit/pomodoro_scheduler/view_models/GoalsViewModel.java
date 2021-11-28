package com.bcit.pomodoro_scheduler.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.repositories.GoalRepository;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoalsViewModel extends ViewModel implements GoalRepository.OnFirestoreTaskComplete {

    private final MutableLiveData<List<Goal>> goalsModelData = new MutableLiveData<>();

    public LiveData<List<Goal>> getGoalsModelData() {
        return goalsModelData;
    }

    private final GoalRepository goalRepository = new GoalRepository(this);


    public GoalsViewModel(String userEmail){
        goalRepository.getGoalsData(userEmail);
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
