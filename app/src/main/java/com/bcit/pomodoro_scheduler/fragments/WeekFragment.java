package com.bcit.pomodoro_scheduler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.TypedArrayUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.weeklyView.WeekAdapter;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String START_DATE = "day";
    private LocalDate startDate;

    private static final String EMAIL = "day";
    private String userEmail;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(LocalDate startDate, String userEmail) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putSerializable(START_DATE, startDate);
        args.putString(EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    public static WeekFragment newInstance() {
        WeekFragment fragment = new WeekFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startDate = (LocalDate) getArguments().getSerializable(START_DATE);
            userEmail = (String) getArguments().get(EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RecyclerView rv = view.findViewById(R.id.recyclerView_fragmentWeek_days);
            setUpRecyclerView(getDaysInMonth(startDate), rv, startDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView(LocalDate[] data, RecyclerView rv, LocalDate currentDay) {
        WeekAdapter adapter = new WeekAdapter(data, this, currentDay);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.HORIZONTAL, false));
    }

    private LocalDate[] getDaysInMonth(LocalDate day) {
        YearMonth month = YearMonth.of(day.getYear(), day.getMonth());
        LocalDate[] items = new LocalDate[month.lengthOfMonth()];
        for (int i = 0; i < month.lengthOfMonth(); i++) items[i] = month.atDay(i + 1);
        return items;
    }


    public void swapDayFragment(LocalDate date) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView_fragmentWeek_Day, DayFragment.newInstance(date));
        ft.commit();
    }
}