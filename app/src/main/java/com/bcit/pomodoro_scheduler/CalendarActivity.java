package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bcit.pomodoro_scheduler.fragments.MonthFragment;

import java.time.YearMonth;

public class CalendarActivity extends AppCompatActivity {
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        this.userEmail = intent.getStringExtra("googleAccount");

        // print user email, this will be passed on to the next fragment
        Log.d("EMAIL", this.userEmail);
        goToMonthlyView(YearMonth.now());
    }

    public void goToMonthlyView(YearMonth year){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView_main, MonthFragment.newInstance(year));
        ft.commit();
    }
}