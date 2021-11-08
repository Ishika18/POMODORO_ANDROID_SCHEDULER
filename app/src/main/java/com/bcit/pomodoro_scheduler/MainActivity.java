package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.bcit.pomodoro_scheduler.fragments.MonthFragment;

import java.time.Year;

/**
 * This is where the year view will go.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TO TEST MONTHLY VIEW
        // goToMonthlyView(Year.of(2021));
    }

    public void goToMonthlyView(Year year){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView_main, MonthFragment.newInstance(year));
        ft.commit();
    }
}