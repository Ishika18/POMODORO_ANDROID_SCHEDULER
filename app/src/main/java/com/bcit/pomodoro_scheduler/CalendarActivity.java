package com.bcit.pomodoro_scheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bcit.pomodoro_scheduler.fragments.CreateCommitmentFragment;
import com.bcit.pomodoro_scheduler.fragments.CreateGoalFragment;
import com.bcit.pomodoro_scheduler.fragments.EditScheduleFragment;
import com.bcit.pomodoro_scheduler.fragments.MonthFragment;
import com.bcit.pomodoro_scheduler.fragments.WeekFragment;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;
import com.bcit.pomodoro_scheduler.view_models.SchedulesViewModel;
import com.bcit.pomodoro_scheduler.view_models.ViewModelFactory;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    public static final String MONTH_FRAGMENT_TAG = "MONTH_FRAGMENT";
    public static final String WEEK_FRAGMENT_TAG = "WEEK_FRAGMENT";
    public static final String CREATE_COMMITMENT_FRAGMENT_TAG = "CREATE_COMMITMENT_FRAGMENT";
    public static final String CREATE_GOAL_FRAGMENT_TAG = "CREATE_GOAL_FRAGMENT";
    public static final String EDIT_SCHEDULE_TAG = "EDIT_SCHEDULE_FRAGMENT";

    private String userEmail;
    private List<Goal> goals;
    private HashMap<Repeat, List<Commitment>> commitmentHashMap;
    private HashMap<LocalDate, ArrayList<Task>> scheduleHashMap;

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
        getDataFromFirebaseViewModels();
    }

    private void getDataFromFirebaseViewModels() {
        ViewModelFactory factory = new ViewModelFactory(userEmail);

        GoalsViewModel goalsViewModel = new ViewModelProvider(this, factory).get(GoalsViewModel.class);
        goalsViewModel.getGoalsModelData().observe(this, goals -> {
            this.goals = goals;
        });

        CommitmentsViewModel commitmentsViewModel = new ViewModelProvider(this, factory)
                .get(CommitmentsViewModel.class);
        commitmentsViewModel.getCommitmentsModelData()
                .observe(this, commitmentsMap -> {
                    this.commitmentHashMap = commitmentsMap;
                });

        SchedulesViewModel schedulesViewModel = new ViewModelProvider(this, factory)
                .get(SchedulesViewModel.class);
        schedulesViewModel.getSchedulesModelData()
                .observe(this, scheduleMap -> {
                    this.scheduleHashMap = scheduleMap;
                });
    }

    public void setActionBarFunction() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MonthFragment monthFragment = (MonthFragment) getSupportFragmentManager()
                        .findFragmentByTag(MONTH_FRAGMENT_TAG);
                if (monthFragment != null && monthFragment.isVisible()) {
                    finish();
                } else {
                    goToMonthlyView(YearMonth.now());
                }
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_appbar_edit) {
                    goToEditScheduleFragment(Boolean.TRUE);
                } else if (item.getItemId() == R.id.menu_appbar_add_task) {
                    goToCreateGoalView();
                } else if (item.getItemId() == R.id.menu_appbar_add_commitment) {
                    goToCreateCommitmentView();
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
                CreateCommitmentFragment.newInstance(null),
                CREATE_COMMITMENT_FRAGMENT_TAG
        );
        ft.commit();
    }

    public void goToCreateGoalView() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(
                R.id.fragmentContainerView_main,
                CreateGoalFragment.newInstance(null),
                CREATE_GOAL_FRAGMENT_TAG
        );
        ft.commit();
    }

    public void goToEditScheduleFragment(Boolean isEditingGoals) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        EditScheduleFragment editScheduleFragment = EditScheduleFragment.newInstance(userEmail);
        editScheduleFragment.setIsEditingGoals(isEditingGoals);
        ft.replace(
                R.id.fragmentContainerView_main,
                editScheduleFragment,
                EDIT_SCHEDULE_TAG
        );
        ft.commit();
    }
}