package com.bcit.pomodoro_scheduler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.adapters.MonthlyCalendarViewAdapter;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;

import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthFragment extends Fragment {


    private static final String YEAR_MONTH = "yearMonth";

    private Year year;
    private Month month;

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_fragmentMonth_monthDays);
        setUpRecyclerView(getYearMonths(year), recyclerView);
    }

    private void setUpRecyclerView(YearMonth[] yearMonths, RecyclerView recyclerView) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.scrollToPosition(this.month.getValue());
        recyclerView.setLayoutManager(layoutManager);

        // define an adapter
        MonthlyCalendarViewAdapter adapter = new MonthlyCalendarViewAdapter(yearMonths);
        recyclerView.setAdapter(adapter);
    }

    private YearMonth[] getYearMonths(Year year) {
        YearMonth[] yearMonths = new YearMonth[12];
        for (int i = 1; i < 13; i++) {
            yearMonths[i - 1] = year.atMonth(i);
        }
        return yearMonths;
    }
}