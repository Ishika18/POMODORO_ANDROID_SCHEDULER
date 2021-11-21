package com.bcit.pomodoro_scheduler.repositories;

import com.bcit.pomodoro_scheduler.model.Goal;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class GoalRepository {

    private final OnFirestoreTaskComplete onFirestoreTaskComplete;


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = firebaseFirestore.collection("users");

    public GoalRepository(OnFirestoreTaskComplete onFirestoreTaskComplete) {
        this.onFirestoreTaskComplete = onFirestoreTaskComplete;
    }

    public void getGoalsData(){
        userRef.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onFirestoreTaskComplete.goalsDataAdded(task.getResult().toObjects(Goal.class));
            }else{
                onFirestoreTaskComplete.onError(task.getException());
            }
        });
    }

    public interface  OnFirestoreTaskComplete{
        void goalsDataAdded(List<Goal> goalsModels);
        void onError(Exception e);
    }
}