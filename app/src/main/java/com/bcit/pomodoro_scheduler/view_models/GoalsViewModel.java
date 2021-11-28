package com.bcit.pomodoro_scheduler.view_models;

import android.content.Context;
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

    private final Context context;
    private final String userEmail;

    private final MutableLiveData<List<Goal>> goalsModelData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> goalDataUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> goalDataDeleted = new MutableLiveData<>();

    public LiveData<List<Goal>> getGoalsModelData() {
        return goalsModelData;
    }

    public LiveData<Boolean> updateGoalData(String userEmail, Goal goal) {
        goalRepository.addOrUpdateGoal(userEmail, goal);
        return goalDataUpdated;
    }

    public LiveData<Boolean> deleteGoalData(String userEmail, String goalId) {
        goalRepository.deleteGoal(userEmail, goalId);
        return goalDataDeleted;
    }

    private final GoalRepository goalRepository = new GoalRepository(this);

    public GoalsViewModel(Context context, String userEmail){
        this.context = context;
        this.userEmail = userEmail;
        goalRepository.getGoalsData(userEmail);
    }
    
    @Override
    public void goalsDataAdded(List<Goal> goalsModels) {
        goalsModelData.setValue(goalsModels);
    }

    @Override
    public void goalDataUpdated() {
        goalDataUpdated.setValue(Boolean.TRUE);
    }

    @Override
    public void goalDataDeleted() {
        goalDataDeleted.setValue(Boolean.TRUE);
    }

    @Override
    public void onErrorGoalDataDeleted(Exception e) {
        goalDataDeleted.setValue(Boolean.FALSE);
    }

    @Override
    public void onErrorGoalDataUpdated(Exception e) {
        goalDataUpdated.setValue(Boolean.FALSE);
    }

    @Override
    public void onErrorGetGoalsData(Exception e) {
        Log.w("Fail", "Error getting documents.", e);
    }

}
