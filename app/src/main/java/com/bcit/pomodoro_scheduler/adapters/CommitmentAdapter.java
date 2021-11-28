package com.bcit.pomodoro_scheduler.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.CalendarActivity;
import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.fragments.CreateCommitmentFragment;
import com.bcit.pomodoro_scheduler.model.Commitment;
import com.bcit.pomodoro_scheduler.view_models.CommitmentsViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {

    private final FragmentActivity activity;
    private final String userEmail;
    private final List<Commitment> commitments;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView commitmentTitle;
        private final TextView commitmentTime;
        private final MaterialCardView card;
        private final ImageButton editButton;

        public ViewHolder(View view) {
            super(view);

            commitmentTitle = view.findViewById(R.id.textview_itemEvent_cardMain);
            commitmentTime = view.findViewById(R.id.textview_itemEvent_cardSub);
            card = view.findViewById(R.id.card_itemEvent_eventInfo);
            editButton = view.findViewById(R.id.button__itemEvent_editButton);
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
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param activity The FragmentActivity this GoalAdapter needs for MVVM context
     */
    public CommitmentAdapter(FragmentActivity activity, String userEmail) {
        this.activity = activity;
        this.userEmail = userEmail;
        this.commitments = new ArrayList<>();
        CommitmentsViewModel viewModel = new ViewModelProvider(activity)
                .get(CommitmentsViewModel.class);

        viewModel.getCommitmentsModelData().observe(activity, commitmentHashMap -> {
            for (List<Commitment> listValue : commitmentHashMap.values()) {
                if (listValue != null) {
                    this.commitments.addAll(listValue);
                }
            }
        });
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
                        CreateCommitmentFragment.newInstance(userEmail,
                                commitments.get(viewHolder.getAdapterPosition())));
                ft.commit();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return commitments.size();
    }
}