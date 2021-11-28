package com.bcit.pomodoro_scheduler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcit.pomodoro_scheduler.CalendarActivity;
import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.adapters.DayAdapter;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.model.Scheduler;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;
import com.bcit.pomodoro_scheduler.view_models.SchedulesViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DAY = "day";
    private LocalDate date;
    private List<Goal> goals;
    private HashMap<Repeat, List<Commitment>> commitmentHashMap;
    private HashMap<LocalDate, ArrayList<Task>> scheduleHashMap;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(LocalDate date) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putSerializable(DAY, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.date = (LocalDate) getArguments().getSerializable(DAY);
            this.date = this.date == null ? LocalDate.now() : this.date;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoalsViewModel goalsViewModel = new ViewModelProvider(requireActivity())
                .get(GoalsViewModel.class);
        goalsViewModel.getGoalsModelData().observe(getViewLifecycleOwner(), goals -> {
            this.goals = goals;
        });

        CommitmentsViewModel commitmentsViewModel = new ViewModelProvider(requireActivity())
                .get(CommitmentsViewModel.class);
        commitmentsViewModel.getCommitmentsModelData()
                .observe(getViewLifecycleOwner(), commitmentsMap -> {
                    this.commitmentHashMap = commitmentsMap;
                });

        SchedulesViewModel schedulesViewModel = new ViewModelProvider(requireActivity())
                .get(SchedulesViewModel.class);
        LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
        schedulesViewModel.getSchedulesModelData()
                .observe(lifecycleOwner, scheduleMap -> {
                    this.scheduleHashMap = scheduleMap;

                    if (scheduleHashMap.isEmpty()) {
                        Scheduler scheduler = new Scheduler(commitmentHashMap, goals);
                        this.scheduleHashMap = scheduler.getSchedule();
                        schedulesViewModel.updateScheduleData(scheduleMap);
                    };

                    ArrayList<Task> tasks = scheduleHashMap.get(date) == null?
                            new ArrayList<>(): scheduleHashMap.get(date);
                    RecyclerView rv = view.findViewById(R.id.recyclerView_fragmentDay_dailySchedule);
                    setUpRecyclerView(tasks, rv);
                });
    }

    private void setUpRecyclerView(ArrayList<Task> tasks, RecyclerView rv) {
        DayAdapter adapter = new DayAdapter(tasks);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.VERTICAL, false));
    }
}