package com.bcit.pomodoro_scheduler.weeklyView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.fragments.WeekFragment;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    private LocalDate[] localDataSet;
    private WeekFragment weekFragment;

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
    public WeekAdapter(LocalDate[] dataSet, WeekFragment weekFragment) {
        localDataSet = dataSet;
        this.weekFragment = weekFragment;
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
        viewHolder.getDayNum().setText(Integer.toString(localDataSet[position].getDayOfMonth()));
        viewHolder.getDayStr().setText(localDataSet[position].getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.US));

        viewHolder.getDayNum().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekFragment.swapDayFragment(localDataSet[position]);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}