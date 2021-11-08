package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.bcit.pomodoro_scheduler.fragments.WeekFragment;

/**
 * This is where the year view will go.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goToWeeklyView(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView_main, WeekFragment.newInstance());
        ft.commit();
    }
}