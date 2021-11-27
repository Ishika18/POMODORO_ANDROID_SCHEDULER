package com.bcit.pomodoro_scheduler.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bcit.pomodoro_scheduler.CalendarActivity;
import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.adapters.MonthlyCalendarViewAdapter;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {


    private static final String YEAR_MONTH = "yearMonth";

    public Year year;
    public Month month;


    public MonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param yearMonth YearMonth object
     * @return A new instance of fragment MonthFragment.
     */
    public static MonthFragment newInstance(YearMonth yearMonth) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putSerializable(YEAR_MONTH, yearMonth);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            YearMonth yearMonth = (YearMonth) getArguments().getSerializable(YEAR_MONTH);
            this.year = Year.of(yearMonth.getYear());
            this.month = yearMonth.getMonth();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setYearViewText(view);

        setUpYearScrollButtons(view);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_fragmentMonth_monthDays);
        setUpRecyclerView(getYearMonths(year), recyclerView);
    }

    private void setUpYearScrollButtons(View view) {
        ImageButton goToPrevMonth = view.findViewById(R.id.btn_fragmentMonth_prevMonth);
        ImageButton goToNextMonth = view.findViewById(R.id.btn_fragmentMonth_nextMonth);

        goToPrevMonth.setOnClickListener(view1 -> {
            year = Year.of(year.getValue() - 1);
            ((CalendarActivity)getActivity())
                    .goToMonthlyView(YearMonth.of(year.getValue(), 1));
        });

        goToNextMonth.setOnClickListener(view2 -> {
            year = Year.of(year.getValue() + 1);
            ((CalendarActivity)getActivity())
                    .goToMonthlyView(YearMonth.of(year.getValue(), 1));
        });
    }

    private void setYearViewText(View view) {
        TextView yearTextView = view.findViewById(R.id.textView_fragmentMonth_year);
        yearTextView.setText(this.year.toString());
    }

    private void setUpRecyclerView(YearMonth[] yearMonths, RecyclerView recyclerView) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(this.month.getValue());
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        MonthlyCalendarViewAdapter adapter = new MonthlyCalendarViewAdapter(this, yearMonths);
        recyclerView.setAdapter(adapter);
    }

    private YearMonth[] getYearMonths(Year year) {
        YearMonth[] yearMonths = new YearMonth[12];
        for (int i = 1; i < 13; i++) {
            yearMonths[i - 1] = year.atMonth(i);
        }
        return yearMonths;
    }

    public void goToWeekView(LocalDate date) {
        ((CalendarActivity) getActivity()).goToWeeklyView(date);
    }
}