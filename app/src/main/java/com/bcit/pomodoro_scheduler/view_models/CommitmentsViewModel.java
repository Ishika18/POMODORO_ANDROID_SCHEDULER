package com.bcit.pomodoro_scheduler.view_models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.repositories.CommitmentRepository;

import java.util.HashMap;
import java.util.List;

public class CommitmentsViewModel extends ViewModel implements CommitmentRepository.OnFirestoreTaskComplete {

    private final MutableLiveData<HashMap<Repeat, List<Commitment>>> commitmentsModelData = new MutableLiveData<>();

    public LiveData<HashMap<Repeat, List<Commitment>>> getCommitmentsModelData() {
        return commitmentsModelData;
    }

    private final CommitmentRepository commitmentRepository = new CommitmentRepository(this);


    public CommitmentsViewModel(String userEmail){
        commitmentRepository.getCommitmentsData(userEmail);
    }
    
    @Override
    public void commitmentsDataAdded(HashMap<Repeat, List<Commitment>> commitmentsModel) {
        commitmentsModelData.setValue(commitmentsModel);
    }

    @Override
    public void onError(Exception e) {
        Log.w("Fail", "Error getting documents.", e);
    }
}
