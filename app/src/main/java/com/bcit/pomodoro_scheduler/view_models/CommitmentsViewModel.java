package com.bcit.pomodoro_scheduler.view_models;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.repositories.CommitmentRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class CommitmentsViewModel extends ViewModel implements CommitmentRepository.OnFirestoreTaskComplete {

    private final MutableLiveData<HashMap<Repeat, List<Commitment>>> commitmentsModelData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> commitmentUpdated = new MutableLiveData<>();
    private final MutableLiveData<Boolean> commitmentDeleted = new MutableLiveData<>();

    public LiveData<HashMap<Repeat, List<Commitment>>> getCommitmentsModelData() {
        return commitmentsModelData;
    }

    public LiveData<Boolean> updateCommitmentData(String userEmail, Commitment commitment) {
        commitmentRepository.addOrUpdateCommitment(userEmail, commitment);
        return commitmentUpdated;
    }

    public LiveData<Boolean> deleteCommitmentData(String userEmail, String id) {
        commitmentRepository.deleteCommitment(userEmail, id);
        return commitmentDeleted;
    }

    private final Context context;
    private final String userEmail;

    private final CommitmentRepository commitmentRepository = new CommitmentRepository(
            this
    );


    public CommitmentsViewModel(Context context, String userEmail) {
        this.context = context;
        this.userEmail = userEmail;
        commitmentRepository.getCommitmentsData(this.userEmail);
    }

    @Override
    public void onCommitmentDeleted() {
        commitmentDeleted.setValue(Boolean.TRUE);
    }

    @Override
    public void onCommitmentUpdated() {
        commitmentUpdated.setValue(Boolean.TRUE);
    }

    @Override
    public void commitmentsDataAdded(HashMap<Repeat, List<Commitment>> commitmentsModel) {
        commitmentsModelData.setValue(commitmentsModel);
    }

    @Override
    public void onErrorGetCommitmentData(Exception e) {
        Log.w("GET_COMMITMENT_DATA", "Error getting documents.", e);
    }

    @Override
    public void onErrorUpdateCommitmentData(Exception e) {
        commitmentUpdated.setValue(Boolean.FALSE);
        Log.w("UPDATE_COMMITMENT_DATA", "Error getting documents.", e);
    }

    @Override
    public void onErrorDeleteCommitmentData(Exception e) {
        commitmentDeleted.setValue(Boolean.FALSE);
        Log.w("DELETE_COMMITMENT_DATA", "Error getting documents.", e);
    }
}
