package com.bcit.pomodoro_scheduler.adapters;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.fragments.CreateGoalFragment;
import com.bcit.pomodoro_scheduler.model.Goal;
import com.bcit.pomodoro_scheduler.model.Repeat;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {

    private FragmentActivity activity;
    private final String userEmail;
    private List<Goal> goals;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView goalTitle;
        private final TextView goalHours;
        private final MaterialCardView card;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);

            goalTitle = view.findViewById(R.id.textview_itemEvent_cardMain);
            goalHours = view.findViewById(R.id.textview_itemEvent_cardSub);
            card = view.findViewById(R.id.card_itemEvent_eventInfo);
            editButton = view.findViewById(R.id.button__itemEvent_editButton);
            deleteButton = view.findViewById(R.id.button__itemEvent_deleteButton);
        }

        public TextView getGoalTitle() {
            return goalTitle;
        }

        public TextView getGoalHours() {
            return goalHours;
        }

        public MaterialCardView getCard() {
            return card;
        }

        public ImageButton getEditButton() {
            return editButton;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param activity The FragmentActivity this GoalAdapter needs for MVVM context
     */
    public GoalAdapter(FragmentActivity activity, String userEmail) {
        this.activity = activity;
        this.userEmail = userEmail;
        updateGoalsData();
        Log.d("GOAL_ADAPTER", goals.toString() + "Size: " + goals.size());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_event, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (goals.get(position).getName().length() < 20) {
            viewHolder.getGoalTitle()
                    .setText(goals.get(position).getName());
        } else {
            String title = goals.get(position).getName()
                    .substring(0, 18) + "...";
            viewHolder.getGoalTitle().setText(title);
        }

        String goalHours;
        if (goals.get(position).getTotalTimeInMinutes() < 60) {
            goalHours = goals.get(position).getTotalTimeInMinutes() + " Mins";
        } else {
            goalHours = goals.get(position).getTotalTimeInMinutes() / 60 + " Hours";
        }

        viewHolder.getGoalHours().setText(goalHours);
        viewHolder.getCard().setStrokeColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.rally_green));
        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView_main,
                        CreateGoalFragment.newInstance(
                                goals.get(viewHolder.getAdapterPosition())));
                ft.commit();
            }
        });

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            private void onChanged(Boolean deleted) {
                if (deleted) {
                    updateGoalsData();
                    notifyDataSetChanged();
                } else {
                    Snackbar.make(viewHolder.itemView, R.string.goal_delete_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Delete " + goals.get(viewHolder.getAdapterPosition()).getName() + "?")
                        .setNeutralButton("Cancel",
                                (dialogInterface, i) -> {
                                })
                        .setPositiveButton("Confirm",
                                ((dialogInterface, i) -> {
                                    String goalID = goals
                                            .get(viewHolder.getAdapterPosition()).getId();

                                    GoalsViewModel goalsViewModel = new ViewModelProvider(activity)
                                            .get(GoalsViewModel.class);
                                    goalsViewModel.deleteGoalData(goalID)
                                            .observe(activity, this::onChanged);

                                })).show();
            }
        });
    }

    public void updateGoalsData() {
        GoalsViewModel goalsViewModel = new ViewModelProvider(activity).get(GoalsViewModel.class);
        goalsViewModel.getGoalsModelData().observe(activity, goalsData -> {
            this.goals = goalsData;
        });
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }
}