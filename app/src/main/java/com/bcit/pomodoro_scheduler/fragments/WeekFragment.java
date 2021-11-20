package com.bcit.pomodoro_scheduler.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.weeklyView.WeekAdapter;
import com.bcit.pomodoro_scheduler.weeklyView.WeekViewDateItem;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekFragment newInstance(String param1, String param2) {
        WeekFragment fragment = new WeekFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
            setUpRecyclerView(mockWeekItems(Year.of(2021).atMonth(11)), rv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView(LocalDate[] data, RecyclerView rv) {
        WeekAdapter adapter = new WeekAdapter(data);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.HORIZONTAL, false));
    }

    private LocalDate[] mockWeekItems(YearMonth month) {
        LocalDate[] items = new LocalDate[month.lengthOfMonth()];
        for (int i = 0; i < month.lengthOfMonth(); i++) items[i] = month.atDay(i + 1);
        return items;
    }

    public void swapDayFragment() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerView_fragmentWeek_Day, DayFragment.newInstance());
        ft.commit();
    }
}