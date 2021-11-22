package com.bcit.pomodoro_scheduler.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.bcit.pomodoro_scheduler.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CreateCommitmentFragment extends Fragment {

    private static final String DAY = "DAY";

    private Date day;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param day a Java Date.
     * @return A new instance of fragment CreateCommitmentFragment.
     */
    public static CreateCommitmentFragment newInstance(Date day) {
        CreateCommitmentFragment fragment = new CreateCommitmentFragment();
        Bundle args = new Bundle();
        args.putSerializable(DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateCommitmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = (Date) getArguments().getSerializable(DAY);
        }
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
        Button startTimeDate = view.findViewById(R.id.button_commitment_startTime_date);
        Button endTimeDate = view.findViewById(R.id.button_commitment_endTime_date);
        Button startTimeTime = view.findViewById(R.id.button_commitment_startTime_time);
        Button endTimeTime = view.findViewById(R.id.button_commitment_endTime_time);

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
                            calendar.get(Calendar.MONTH) +
                            "-" +
                            calendar.get(Calendar.YEAR);

                    startTimeDate.setText(date);
                });

                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });
    }
}