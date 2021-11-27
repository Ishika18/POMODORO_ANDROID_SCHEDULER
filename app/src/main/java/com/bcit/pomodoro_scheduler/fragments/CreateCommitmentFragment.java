package com.bcit.pomodoro_scheduler.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCommitmentFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateCommitmentFragment.
     */
    public static CreateCommitmentFragment newInstance() {
        return new CreateCommitmentFragment();
    }

    public CreateCommitmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_commitment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar startCalendar = Calendar.getInstance(TimeZone.getDefault());
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.add(Calendar.HOUR, 1);

        if (endCalendar.get(Calendar.HOUR_OF_DAY) == 0) {
            endCalendar.add(Calendar.DATE, 1);
        }

        Button startTimeDate = view.findViewById(R.id.button_commitment_startTime_date);
        Button endTimeDate = view.findViewById(R.id.button_commitment_endTime_date);
        Button startTimeTime = view.findViewById(R.id.button_commitment_startTime_time);
        Button endTimeTime = view.findViewById(R.id.button_commitment_endTime_time);


        Commitment commitment = new Commitment(Timestamp.now().toString(), "", "",
                new Timestamp(startCalendar.getTime()), new Timestamp(endCalendar.getTime()),
                Repeat.NEVER, "", "");

        Button repeat = view.findViewById(R.id.button_commitment_repeat);
        String[] repeats = Stream.of(Repeat.values()).map(Repeat::toString).toArray(String[]::new);

        repeat.setOnClickListener(new View.OnClickListener() {
            private int selection;

            @Override
            public void onClick(View view) {
                selection = 0;
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle(R.string.repeat_picker_title)
                        .setNeutralButton(getResources().getString(R.string.cancel),
                                (dialogInterface, i) -> {
                                })
                        .setPositiveButton(getResources().getString(R.string.ok),
                                ((dialogInterface, i) -> {
                                    commitment.setRepeat(Repeat.fromValue(repeats[selection]));
                                    repeat.setText(repeats[selection].toUpperCase());
                                }))
                        .setSingleChoiceItems(repeats, selection, new DialogInterface.OnClickListener() {
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

        startTimeDate.setText(getFormattedDate(startCalendar));
        endTimeDate.setText(getFormattedDate(endCalendar));
        startTimeTime.setText(getFormattedTime(startCalendar));
        endTimeTime.setText(getFormattedTime(endCalendar));

        startTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("Select Start Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    startCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    endCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    endCalendar.add(Calendar.HOUR_OF_DAY, 1);

                    if (startCalendar.get(Calendar.HOUR_OF_DAY) >= 23) {
                        endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    String date = getFormattedDate(calendar);

                    commitment.setStartTime(new Timestamp(startCalendar.getTime()));
                    commitment.setEndTime(new Timestamp(endCalendar.getTime()));
                    startTimeDate.setText(date);
                    endTimeDate.setText(date);
                });

                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });

        endTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                c.setTime(startCalendar.getTime());
                c.add(Calendar.DATE, -1);
                CalendarConstraints constraint = new CalendarConstraints.Builder()
                        .setStart(c.getTimeInMillis())
                        .setValidator(DateValidatorPointForward.from(c.getTimeInMillis()))
                        .build();

                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                        .datePicker()
                        .setCalendarConstraints(constraint)
                        .setTitleText("Select End Date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                    calendar.setTimeInMillis(selection);

                    endCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));

                    commitment.setEndTime(new Timestamp(endCalendar.getTime()));
                    endTimeDate.setText(getFormattedDate(calendar));
                });

                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });

        startTimeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Select Start Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(selection -> {
                    startCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    startCalendar.set(Calendar.MINUTE, timePicker.getMinute());

                    if (timePicker.getHour() >= 23
                            && startCalendar.get(Calendar.DATE) == endCalendar.get(Calendar.DATE)) {
                        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    } else {
                        endCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour() + 1);
                    }
                    endCalendar.set(Calendar.MINUTE, timePicker.getMinute());

                    startTimeTime.setText(getFormattedTime(startCalendar));
                    endTimeTime.setText(getFormattedTime(endCalendar));
                    endTimeDate.setText(getFormattedDate(endCalendar));
                });

                timePicker.show(
                        requireActivity().getSupportFragmentManager(), timePicker.toString());
            }
        });

        endTimeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setTitleText("Select End Time")
                        .build();

                timePicker.addOnPositiveButtonClickListener(selection -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endCalendar.getTime());
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                    if (calendar.before(startCalendar)) {
                        Snackbar.make(view, R.string.invalid_time_selection,
                                Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    endCalendar.setTime(calendar.getTime());
                    commitment.setEndTime(new Timestamp(endCalendar.getTime()));
                    endTimeTime.setText(getFormattedTime(endCalendar));
                });

                timePicker.show(
                        requireActivity().getSupportFragmentManager(), timePicker.toString());
            }
        });
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