package com.bcit.pomodoro_scheduler.adapters;

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
import com.bcit.pomodoro_scheduler.fragments.CreateCommitmentFragment;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.bcit.pomodoro_scheduler.view_models.GoalsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;


public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {

    private final FragmentActivity activity;
    private final String userEmail;
    private List<Commitment> commitments = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView commitmentTitle;
        private final TextView commitmentTime;
        private final MaterialCardView card;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);

            commitmentTitle = view.findViewById(R.id.textview_itemEvent_cardMain);
            commitmentTime = view.findViewById(R.id.textview_itemEvent_cardSub);
            card = view.findViewById(R.id.card_itemEvent_eventInfo);
            editButton = view.findViewById(R.id.button__itemEvent_editButton);
            deleteButton = view.findViewById(R.id.button__itemEvent_deleteButton);
        }

        public TextView getCommitmentTitle() {
            return commitmentTitle;
        }

        public TextView getCommitmentTime() {
            return commitmentTime;
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
    public CommitmentAdapter(FragmentActivity activity, String userEmail) {
        this.activity = activity;
        this.userEmail = userEmail;
        updateCommitmentsData();
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
        viewHolder.getCommitmentTitle().setText(commitments.get(position).getName());
        StringBuilder sb = new StringBuilder();
        sb.append(commitments.get(position).getRepeat().name()).append(" ");
        sb.append(Instant.ofEpochMilli(commitments.get(position).getStartTime().getSeconds() * 1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        sb.append(" - ");
        sb.append(Instant.ofEpochMilli(commitments.get(position).getEndTime().getSeconds() * 1000)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        viewHolder.getCommitmentTime().setText(sb.toString());
        viewHolder.getCard().setStrokeColor(ContextCompat.getColor(viewHolder.itemView.getContext(),
                R.color.rally_orange));

        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentContainerView_main,
                        CreateCommitmentFragment.newInstance(
                                commitments.get(viewHolder.getAdapterPosition())));
                ft.commit();
            }
        });

        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            private void onChanged(Boolean deleted) {
                if (deleted) {
                    commitments.clear();
                    updateCommitmentsData();
                    notifyDataSetChanged();
                } else {
                    Snackbar.make(viewHolder.itemView, R.string.goal_delete_error, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(v.getContext())
                        .setTitle("Delete " + commitments.get(viewHolder.getAdapterPosition()).getName() + "?")
                        .setNeutralButton("Cancel",
                                (dialogInterface, i) -> {
                                })
                        .setPositiveButton("Confirm",
                                ((dialogInterface, i) -> {
                                    String commitmentID = commitments
                                            .get(viewHolder.getAdapterPosition()).getId();

                                    CommitmentsViewModel commitmentsViewModel = new ViewModelProvider(activity)
                                            .get(CommitmentsViewModel.class);
                                    commitmentsViewModel.deleteCommitmentData(commitmentID)
                                            .observe(activity, this::onChanged);
                                })).show();
            }
        });
    }

    public void updateCommitmentsData() {
        HashMap<String, Commitment> commitmentsMap = new HashMap<>();
        CommitmentsViewModel viewModel = new ViewModelProvider(activity)
                .get(CommitmentsViewModel.class);

        viewModel.getCommitmentsModelData().observe(activity, commitmentHashMap -> {
            for (List<Commitment> listValue : commitmentHashMap.values()) {
                if (listValue != null) {
                    for (Commitment commitment : listValue) {
                        commitmentsMap.put(commitment.getId(), commitment);
                    }
                }
            }
            this.commitments.addAll(commitmentsMap.values());
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commitments.size();
    }
}