package com.bcit.pomodoro_scheduler.view_models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;
    private final String userEmail;

    public ViewModelFactory(Context context, String userEmail) {
        this.context = context;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CommitmentsViewModel.class)) {
            return (T) new CommitmentsViewModel(context, userEmail);
        } else if (modelClass.isAssignableFrom(GoalsViewModel.class)) {
            return (T) new GoalsViewModel(context, userEmail);
        } else if (modelClass.isAssignableFrom(SchedulesViewModel.class)) {
            return (T) new SchedulesViewModel(context, userEmail);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
