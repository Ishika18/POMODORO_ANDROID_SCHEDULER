package com.bcit.pomodoro_scheduler.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bcit.pomodoro_scheduler.CalendarActivity;
import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.Timestamp;


import java.time.YearMonth;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Stream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCommitmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCommitmentFragment extends Fragment {

    private static final String USER_EMAIL = "USER_EMAIL";
    private static final String COMMITMENT = "commitment";

    private String userEmail;
    private Commitment commitment;

    public CreateCommitmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userEmail User account email from login
     * @param commitment commitment to update if updating
     * @return A new instance of fragment CreateCommitmentFragment.
     */
    public static CreateCommitmentFragment newInstance(String userEmail, Commitment commitment) {
        CreateCommitmentFragment fragment = new CreateCommitmentFragment();
        Bundle args = new Bundle();
        args.putString(USER_EMAIL, userEmail);
        args.putSerializable(COMMITMENT, commitment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(USER_EMAIL);
            if (getArguments().getSerializable(COMMITMENT) != null) {
                commitment = (Commitment) getArguments().getSerializable(COMMITMENT);
            }
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
        CommitmentsViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(CommitmentsViewModel.class);

        Button startTimeDate = view.findViewById(R.id.button_commitment_startTime_date);
        Button endTimeDate = view.findViewById(R.id.button_commitment_endTime_date);
        Button startTimeTime = view.findViewById(R.id.button_commitment_startTime_time);
        Button endTimeTime = view.findViewById(R.id.button_commitment_endTime_time);

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

        Calendar startCalendar = Calendar.getInstance(TimeZone.getDefault());
        Calendar endCalendar = Calendar.getInstance(TimeZone.getDefault());
        endCalendar.add(Calendar.HOUR, 1);

        if (commitment == null) {
            this.commitment = new Commitment(Timestamp.now().toString(), "", "",
                    new Timestamp(startCalendar.getTime()), new Timestamp(endCalendar.getTime()),
                    Repeat.NEVER, "", "");
        }

        startCalendar.setTime(commitment.getStartTime().toDate());
        endCalendar.setTime(commitment.getEndTime().toDate());

        startTimeDate.setText(getFormattedDate(startCalendar));
        endTimeDate.setText(getFormattedDate(endCalendar));
        startTimeTime.setText(getFormattedTime(startCalendar));
        endTimeTime.setText(getFormattedTime(endCalendar));

        Button createButton = view.findViewById(R.id.button_commitment_confirm);
        EditText name = view.findViewById(R.id.editText_commitment_name);
        EditText location = view.findViewById(R.id.editText_commitment_location);
        EditText url = view.findViewById(R.id.editText_commitment_url);
        EditText notes = view.findViewById(R.id.editText_commitment_notes);

        if (commitment == null) {
            createButton.setText(R.string.create);
        }

        name.setText(commitment.getName());
        location.setText(commitment.getLocation());
        url.setText(commitment.getUrl());
        notes.setText(commitment.getNotes());

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

                    if (endCalendar.before(startCalendar)) {
                        endCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    commitment.setStartTime(new Timestamp(startCalendar.getTime()));
                    commitment.setEndTime(new Timestamp(endCalendar.getTime()));
                    startTimeDate.setText(getFormattedDate(startCalendar));
                    endTimeDate.setText(getFormattedDate(endCalendar));
                });

                datePicker.show(
                        requireActivity().getSupportFragmentManager(), datePicker.toString());
            }
        });

        endTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance(TimeZone.getDefault());

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
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
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

                    endCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour() + 1);
                    endCalendar.set(Calendar.MINUTE, timePicker.getMinute());

                    commitment.setStartTime(new Timestamp(startCalendar.getTime()));
                    commitment.setEndTime(new Timestamp(endCalendar.getTime()));

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
                    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    Snackbar.make(view, R.string.commitment_name_error,
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                commitment.setName(name.getText().toString());
                commitment.setLocation(location.getText().toString());
                commitment.setUrl(url.getText().toString());
                commitment.setNotes(notes.getText().toString());

                viewModel.updateCommitmentData(userEmail, commitment).observe(getViewLifecycleOwner(),
                        updated -> {
                    if (!updated) {
                        Snackbar.make(view, R.string.commitment_create_error,
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