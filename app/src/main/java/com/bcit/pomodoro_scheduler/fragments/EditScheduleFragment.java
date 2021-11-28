package com.bcit.pomodoro_scheduler.fragments;

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

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.adapters.CommitmentAdapter;
import com.bcit.pomodoro_scheduler.adapters.GoalAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditScheduleFragment extends Fragment {

    private static final String USER_EMAIL = "USER_EMAIL";
    private static boolean isEditingGoals;
    private String userEmail;
    private String[] mockData;

    public EditScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewScheduleFragment.
     */
    public static EditScheduleFragment newInstance(String userEmail) {
        EditScheduleFragment fragment = new EditScheduleFragment();
        Bundle args = new Bundle();
        args.putString(USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(USER_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            RecyclerView rv = view.findViewById(R.id.recyclerView_fragmentWeek_days);
            mockData = new String[]{"Benis"};
            setUpRecyclerView(rv);
            setUpRecyclerViewScrollButtons(view, rv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpRecyclerView(RecyclerView rv) {
        if (isEditingGoals) {
            GoalAdapter adapter = new GoalAdapter(mockData);
            rv.setAdapter(adapter);
        } else {
            CommitmentAdapter adapter = new CommitmentAdapter(mockData);
            rv.setAdapter(adapter);
        }
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.HORIZONTAL, false));
    }

    private void setUpRecyclerViewScrollButtons(View view, RecyclerView rv) {
        ImageButton nextRecycler = view.findViewById(R.id.button__FragmentEditSchedule_next);
        ImageButton prevRecycler = view.findViewById(R.id.button__FragmentEditSchedule_prev);

        nextRecycler.setOnClickListener(view1 -> {
            isEditingGoals = !isEditingGoals;
            setUpRecyclerView(rv);
        });

        prevRecycler.setOnClickListener(view2 -> {
            isEditingGoals = !isEditingGoals;
            setUpRecyclerView(rv);
        });
    }

}