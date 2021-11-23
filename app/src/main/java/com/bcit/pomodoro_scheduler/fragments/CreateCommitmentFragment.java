package com.bcit.pomodoro_scheduler.fragments;

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
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
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

        Spinner repeat = view.findViewById(R.id.spinner_commitment_repeat);
        repeat.setAdapter(new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_dropdown_item, Repeat.values()));


        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",
                Locale.getDefault());

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Button startTimeDate = view.findViewById(R.id.button_commitment_startTime_date);
        Button endTimeDate = view.findViewById(R.id.button_commitment_endTime_date);
        Button startTimeTime = view.findViewById(R.id.button_commitment_startTime_time);
        Button endTimeTime = view.findViewById(R.id.button_commitment_endTime_time);

        String date = calendar.get(Calendar.DAY_OF_MONTH)+ "-" +
                (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);

        String time = timeFormatter.format(calendar.getTime());

        startTimeDate.setText(date);
        endTimeDate.setText(date);
        startTimeTime.setText(time);
        endTimeTime.setText(time);

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

                    String date = calendar.get(Calendar.DAY_OF_MONTH) +
                            "-" +
                            (calendar.get(Calendar.MONTH) + 1) +
                            "-" +
                            calendar.get(Calendar.YEAR);

                    startTimeDate.setText(date);
                });

                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });

        endTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Date startDate = null;
                try {
                    startDate = dateFormat.parse(startTimeDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert startDate != null;
                c.setTime(startDate);
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
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);

                    String date = calendar.get(Calendar.DAY_OF_MONTH) +
                            "-" +
                            (calendar.get(Calendar.MONTH) + 1) +
                            "-" +
                            calendar.get(Calendar.YEAR);

                    endTimeDate.setText(date);
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
                    String time = String.format(Locale.getDefault(), "%02d:%02d",
                            timePicker.getHour(), timePicker.getMinute());
                    startTimeTime.setText(time);
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
                    String time = String.format(Locale.getDefault(), "%02d:%02d",
                            timePicker.getHour(), timePicker.getMinute());
                    try {
                        Date startDate = dateFormat.parse(startTimeDate.getText().toString());
                        Date endDate = dateFormat.parse(endTimeDate.getText().toString());

                        assert endDate != null;
                        if (endDate.equals(startDate)) {
                            Date startTime = timeFormatter.parse(startTimeTime.getText().toString());
                            Date endTime = timeFormatter.parse(time);
                            assert endTime != null;
                            if (endTime.before(startTime)) {
                                Log.d("Time Picker", "Invalid time selection");
                                return;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    endTimeTime.setText(time);
                });

                timePicker.show(
                        requireActivity().getSupportFragmentManager(), timePicker.toString());
            }
        });
    }
}