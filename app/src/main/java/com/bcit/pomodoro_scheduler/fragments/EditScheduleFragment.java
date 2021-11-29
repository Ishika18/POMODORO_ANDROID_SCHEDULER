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
import android.widget.TextView;

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
    private boolean isEditingGoals = true;
    private String userEmail;
    private TextView currentRecycler;

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

    public void setIsEditingGoals(Boolean value) {
        this.isEditingGoals = value;
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

        RecyclerView rv = view.findViewById(
                R.id.recyclerView__FragmentEditSchedule_scheduleEvents);
        setUpRecyclerView(rv);
        setUpRecyclerViewScrollButtons(view, rv);
        this.currentRecycler = view.findViewById(R.id.button__FragmentEditSchedule_currentRecycler);
        currentRecycler.setText(isEditingGoals ? "Goals" : "Commitments");
    }

    private void setUpRecyclerView(RecyclerView rv) {
        if (isEditingGoals) {
            GoalAdapter adapter = new GoalAdapter(requireActivity(), userEmail);
            rv.setAdapter(adapter);
        } else {
            CommitmentAdapter adapter = new CommitmentAdapter(requireActivity(), userEmail);
            rv.setAdapter(adapter);
        }
        rv.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext(),
                RecyclerView.VERTICAL, false));
    }

    private void setUpRecyclerViewScrollButtons(View view, RecyclerView rv) {
        ImageButton nextRecycler = view.findViewById(R.id.button__FragmentEditSchedule_next);
        ImageButton prevRecycler = view.findViewById(R.id.button__FragmentEditSchedule_prev);

        nextRecycler.setOnClickListener(view1 -> {
            isEditingGoals = !isEditingGoals;
            setUpRecyclerView(rv);
            currentRecycler.setText(isEditingGoals ? "Goals" : "Commitments");
        });

        prevRecycler.setOnClickListener(view2 -> {
            isEditingGoals = !isEditingGoals;
            setUpRecyclerView(rv);
            currentRecycler.setText(isEditingGoals ? "Goals" : "Commitments");
        });
    }

}