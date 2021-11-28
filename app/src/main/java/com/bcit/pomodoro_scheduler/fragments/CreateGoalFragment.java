package com.bcit.pomodoro_scheduler.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bcit.pomodoro_scheduler.CalendarActivity;
import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGoalFragment extends Fragment {

    private static final String USER_EMAIL = "userEmail";
    private static final String GOAL = "goal";

    private static final int HOUR_DAY_LIMIT = 10;
    private static final int HOUR_TO_MINUTES = 60;
    private static final int DAY_TO_MINUTES = 1440;

    private final Map<String, Integer> taskTimeOptions;

    private String userEmail;
    private Goal goal;

    public CreateGoalFragment() {
        this.taskTimeOptions = new LinkedHashMap<>();
        setUpTaskTimeOptions();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userEmail account email from login
     * @param goal the goal to update if updating
     * @return A new instance of fragment CreateGoalFragment.
     */
    public static CreateGoalFragment newInstance(String userEmail, Goal goal) {
        CreateGoalFragment fragment = new CreateGoalFragment();
        Bundle args = new Bundle();
        args.putString(USER_EMAIL, userEmail);
        args.putSerializable(GOAL, goal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(USER_EMAIL);
            if (getArguments().getSerializable(GOAL) != null) {
                goal = (Goal) getArguments().getSerializable(GOAL);
            }
        }
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

        GoalsViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(GoalsViewModel.class);

        Button deadlineDate = view.findViewById(R.id.button_goal_deadline_date);
        Button deadlineTime = view.findViewById(R.id.button_goal_deadline_time);

        Button taskTime = view.findViewById(R.id.button_goal_task_time_time);
        String[] taskTimes = taskTimeOptions.keySet().toArray(new String[0]);

        taskTime.setOnClickListener(new View.OnClickListener() {
            private int selection;

            @Override
            public void onClick(View view) {
                selection = 0;
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(getResources().getString(R.string.task_time_picker_title))
                        .setNeutralButton(getResources().getString(R.string.cancel), null)
                        .setPositiveButton(getResources().getString(R.string.select),
                                ((dialogInterface, i) -> {
                                    goal.setTotalTimeInMinutes(
                                            taskTimeOptions.get(taskTimes[selection]));
                                    taskTime.setText(taskTimes[selection]);
                                }))
                        .setSingleChoiceItems(taskTimes, selection,
                                ((dialogInterface, i) -> {
                                    setSelection(i);
                                })).show();
            }
            private void setSelection(int i) {
                selection = i;
            }
        });

        Button priority = view.findViewById(R.id.button_goal_priority);
        String[] priorities = Stream.of(Priority.values())
                .map(value -> value.name().charAt(0) + value.name().substring(1).toLowerCase())
                .toArray(String[]::new);

        priority.setOnClickListener(new View.OnClickListener() {
            private int selection;

            @Override
            public void onClick(View view) {
                selection = 0;
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(R.string.priority_picker_title)
                        .setNeutralButton(getResources().getString(R.string.cancel), null)
                        .setPositiveButton(getResources().getString(R.string.ok),
                                ((dialogInterface, i) -> {
                                    goal.setPriority(Priority.fromValue(selection));
                                    priority.setText(goal.getPriority().name());
                                }))
                        .setSingleChoiceItems(priorities, selection, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setSelection(i);
                            }
                        }).show();
            }

            private void setSelection(int i) {
                selection = i;
            }
        });

        Calendar deadlineCalendar = Calendar.getInstance(TimeZone.getDefault());
        deadlineCalendar.add(Calendar.DAY_OF_YEAR, 1);

        if (goal == null) {
            goal = new Goal(Timestamp.now().toString(), "", "", 30,
                    new Timestamp(deadlineCalendar.getTime()), Priority.LOW, "", "");
        }

        deadlineCalendar.setTime(goal.getDeadline().toDate());

        deadlineDate.setText(getFormattedDate(deadlineCalendar));
        deadlineTime.setText(getFormattedTime(deadlineCalendar));

        Button createButton = view.findViewById(R.id.button_goal_confirm);
        EditText name = view.findViewById(R.id.editText_goal_name);
        EditText location = view.findViewById(R.id.editText_goal_location);
        EditText url = view.findViewById(R.id.editText_goal_url);
        EditText notes = view.findViewById(R.id.editText_goal_notes);

        if (goal == null) {
            createButton.setText(R.string.create);
        }

        name.setText(goal.getName());
        location.setText(goal.getLocation());
        url.setText(goal.getUrl());
        notes.setText(goal.getNotes());
        priority.setText(goal.getPriority().name());
        taskTime.setText(getKeyByTaskTimeValue(goal.getTotalTimeInMinutes()));

        deadlineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText(getResources().getString(R.string.deadline_picker_title))
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    c.setTimeInMillis(selection);

                    deadlineCalendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH));

                    goal.setDeadline(new Timestamp(deadlineCalendar.getTime()));
                    deadlineDate.setText(getFormattedDate(deadlineCalendar));
                });
                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });

        deadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Select Deadline Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(selection -> {
                    deadlineCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    deadlineCalendar.set(Calendar.MINUTE, timePicker.getMinute());

                    goal.setDeadline(new Timestamp(deadlineCalendar.getTime()));
                    deadlineTime.setText(getFormattedTime(deadlineCalendar));
                });
                timePicker.show(
                        requireActivity().getSupportFragmentManager(), timePicker.toString());
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    Snackbar.make(view, R.string.goal_name_error, Snackbar.LENGTH_LONG).show();
                    return;
                }

                goal.setName(name.getText().toString());
                goal.setLocation(location.getText().toString());
                goal.setUrl(url.getText().toString());
                goal.setNotes(notes.getText().toString());

                viewModel.updateGoalData(userEmail, goal).observe(getViewLifecycleOwner(),
                        updated -> {
                            if (!updated) {
                                Snackbar.make(view, R.string.goal_create_error,
                                        Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            CalendarActivity calendarActivity = (CalendarActivity) getActivity();
                            assert calendarActivity != null;
                            calendarActivity.goToMonthlyView(YearMonth.now());
                        });
            }
        });
    }

    private void setUpTaskTimeOptions() {
        taskTimeOptions.put("30 Mins", 30);
        taskTimeOptions.put("1 Hour", HOUR_TO_MINUTES);
        for (int i = 2; i <= HOUR_DAY_LIMIT; i++) {
            taskTimeOptions.put(i + " Hours", i * HOUR_TO_MINUTES);
        }
        taskTimeOptions.put(1 + " Day", DAY_TO_MINUTES);
        for (int i = 2; i <= HOUR_DAY_LIMIT; i++) {
            taskTimeOptions.put(i + " Days", i * DAY_TO_MINUTES);
        }
    }

    private String getFormattedDate(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH) +
                "-" +
                (calendar.get(Calendar.MONTH) + 1) +
                "-" +
                calendar.get(Calendar.YEAR);
    }

    private String getFormattedTime(Calendar calendar) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private String getKeyByTaskTimeValue(int value) {
        for (Map.Entry<String, Integer> entry : taskTimeOptions.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}