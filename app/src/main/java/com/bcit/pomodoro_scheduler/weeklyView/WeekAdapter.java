package com.bcit.pomodoro_scheduler.weeklyView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    private WeekViewDateItem[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayStr;
        private final TextView dayNum;

        public ViewHolder(View view) {
            super(view);

            dayStr = view.findViewById(R.id.textView_itemWeek_letterDay); //error here should be expected, this is a template
            dayNum = view.findViewById(R.id.textView_itemWeek_numberDay); //error here should be expected, this is a template
        }

        public TextView getDayStr() {
            return dayStr;
        }

        public TextView getDayNum() {
            return dayNum;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public WeekAdapter(WeekViewDateItem[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_week, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getDayNum().setText(Integer.toString(localDataSet[position].getDayNum()));
        viewHolder.getDayStr().setText(localDataSet[position].getDayStr());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}