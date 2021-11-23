package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bcit.pomodoro_scheduler.fragments.CreateCommitmentFragment;
import com.bcit.pomodoro_scheduler.fragments.MonthFragment;
import com.bcit.pomodoro_scheduler.fragments.WeekFragment;
import com.bcit.pomodoro_scheduler.model.Schedule;
import com.bcit.pomodoro_scheduler.model.Task;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {
    private static final String MONTH_FRAGMENT_TAG = "MONTH_FRAGMENT";
    private static final String WEEK_FRAGMENT_TAG = "WEEK_FRAGMENT";
    private static final String CREATE_COMMITMENT_FRAGMENT_TAG = "CREATE_COMMITMENT_FRAGMENT";
    private String userEmail;
    private HashMap<LocalDate, ArrayList<Task>> schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        this.userEmail = intent.getStringExtra("googleAccount");

        // print user email, this will be passed on to the next fragment
        Log.d("EMAIL", this.userEmail);
        setActionBarFunction();
        goToMonthlyView(YearMonth.now());
        Schedule s = new Schedule();
        schedule = s.getSchedule();
    }

    public void setActionBarFunction() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthFragment monthFragment = (MonthFragment) getSupportFragmentManager()
                        .findFragmentByTag(MONTH_FRAGMENT_TAG);
                WeekFragment weekFragment = (WeekFragment) getSupportFragmentManager()
                        .findFragmentByTag(WEEK_FRAGMENT_TAG);
                CreateCommitmentFragment createCommitmentFragment =
                        (CreateCommitmentFragment) getSupportFragmentManager()
                                .findFragmentByTag(CREATE_COMMITMENT_FRAGMENT_TAG);
                if (monthFragment != null && monthFragment.isVisible()) {
                    finish();
                } else if (weekFragment != null && weekFragment.isVisible()) {
                    // just go to the current date
                    goToMonthlyView(YearMonth.now());
                } else if (createCommitmentFragment != null
                        && createCommitmentFragment.isVisible()) {
                    goToMonthlyView(YearMonth.now());
                }
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_1: {
                        // go to new task
                    }

                    case R.id.option_2: {
                        goToCreateCommitmentView();
                    }
                }

                return true;
            }
        });
    }

    public void goToMonthlyView(YearMonth year){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(
                R.id.fragmentContainerView_main,
                MonthFragment.newInstance(year),
                MONTH_FRAGMENT_TAG
        );
        ft.commit();
    }

    public void goToWeeklyView(LocalDate date){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(
                R.id.fragmentContainerView_main,
                WeekFragment.newInstance(date, this.userEmail),
                WEEK_FRAGMENT_TAG
        );
        ft.commit();
    }

    public void goToCreateCommitmentView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(
                R.id.fragmentContainerView_main,
                CreateCommitmentFragment.newInstance(),
                CREATE_COMMITMENT_FRAGMENT_TAG
        );
        ft.commit();
    }

    public HashMap<LocalDate, ArrayList<Task>> getSchedule() {
        return schedule;
    }
}