package com.bcit.pomodoro_scheduler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.bcit.pomodoro_scheduler.model.Repeat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGoalFragment extends Fragment {

    private static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm",
            Locale.getDefault());
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
            Locale.getDefault());

    public CreateGoalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateGoalFragment.
     */
    public static CreateGoalFragment newInstance() {
        return new CreateGoalFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner priority = view.findViewById(R.id.spinner_goal_priority);
        List<String> priorities = Stream.of(Priority.values())
                .map(Priority::name)
                .collect(Collectors.toList());
        priority.setAdapter(new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, priorities));

        Spinner repeat = view.findViewById(R.id.spinner_goal_repeat);
        repeat.setAdapter(new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, Repeat.values()));


        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Button deadlineDate = view.findViewById(R.id.button_goal_deadline_date);
        Button deadlineTime = view.findViewById(R.id.button_goal_deadline_time);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        String time = timeFormatter.format(new Date());

        deadlineDate.setText(date);
        deadlineTime.setText(time);
    }
}