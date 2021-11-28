package com.bcit.pomodoro_scheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;
import com.google.android.material.card.MaterialCardView;


public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.ViewHolder> {

    private String[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView goalTitle;
        private final TextView goalHours;
        private final MaterialCardView card;

        public ViewHolder(View view) {
            super(view);

            goalTitle = view.findViewById(R.id.textview_itemEvent_cardMain);
            goalHours = view.findViewById(R.id.textview_itemEvent_cardSub);
            card = view.findViewById(R.id.card_itemEvent_eventInfo);

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
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public CommitmentAdapter(String[] dataSet) {
        localDataSet = dataSet;
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
        viewHolder.getGoalTitle().setText(localDataSet[position] + "COMMITMENT");
        viewHolder.getGoalHours().setText(localDataSet[position]);
        viewHolder.getCard().setStrokeColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.rally_blue));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}