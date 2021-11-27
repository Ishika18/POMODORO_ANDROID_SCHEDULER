package com.bcit.pomodoro_scheduler.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Priority;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGoalFragment extends Fragment {

    private static final int HOUR_DAY_LIMIT = 10;
    private static final int HOUR_TO_MINUTES = 60;
    private static final int DAY_TO_MINUTES = 1440;

    private final Map<String, Integer> taskTimeOptions;

    public CreateGoalFragment() {
        this.taskTimeOptions = new LinkedHashMap<>();
        setUpTaskTimeOptions();
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

        Calendar deadlineCalendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Button deadlineDate = view.findViewById(R.id.button_goal_deadline_date);
        Button deadlineTime = view.findViewById(R.id.button_goal_deadline_time);

        deadlineCalendar.add(Calendar.DAY_OF_YEAR, 1);

        deadlineDate.setText(getFormattedDate(deadlineCalendar));
        deadlineTime.setText(getFormattedTime(deadlineCalendar));

        Goal goal = new Goal(Timestamp.now().toString(), "", "", 30,
                new Timestamp(deadlineCalendar.getTime()), Priority.LOW, "", "");

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

                    deadlineCalendar.set(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1,
                            c.get(Calendar.YEAR));

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
}