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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {

    private LocalDate[] localDataSet;
    private WeekFragment weekFragment;
    private LocalDate adapterPosition;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayStr;
        private final TextView dayNum;
        private LocalDate day;

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

        public void setDay(LocalDate day) {
            this.day = day;
        }

        public LocalDate getDay() {
            return day;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public WeekAdapter(LocalDate[] dataSet, WeekFragment weekFragment, LocalDate adapterPosition) {
        this.localDataSet = dataSet;
        this.weekFragment = weekFragment;
        this.adapterPosition = adapterPosition;
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

    private ArrayList<ViewHolder> holder_list = new ArrayList<>();

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        holder_list.add(viewHolder);
        viewHolder.setDay(localDataSet[position]);

        TextView dayStr = viewHolder.getDayStr();
        dayStr.setText(localDataSet[position].getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.US));

        TextView dayNum = viewHolder.getDayNum();
        dayNum.setText(Integer.toString(localDataSet[position].getDayOfMonth()));

        if (localDataSet[position] == adapterPosition) {
            dayNum.setBackgroundResource(R.drawable.item_week_circle_background_small);
        } else {
            dayNum.setBackgroundResource(0);
        }

        dayNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNum.setBackgroundResource(R.drawable.item_week_circle_background_small);
                if (adapterPosition != localDataSet[viewHolder.getAdapterPosition()]) {
                    updateBackgrounds();
                    adapterPosition = localDataSet[viewHolder.getAdapterPosition()];
                }
                weekFragment.swapDayFragment(localDataSet[viewHolder.getAdapterPosition()]);
            }
        });
    }

    public void updateBackgrounds() {
        for (ViewHolder viewHolder: holder_list) {
            if (viewHolder.getDay() != null && viewHolder.getDay() == adapterPosition) {
                TextView dayNumOld = viewHolder.getDayNum();
                dayNumOld.setBackgroundResource(0);
            }
        }
    }

    public void notifyAdapterOfChange(int position) {
        this.notifyAdapterOfChange(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}