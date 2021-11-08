package com.bcit.pomodoro_scheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;

import java.time.YearMonth;
import java.util.Locale;


public class MonthlyCalendarViewAdapter extends RecyclerView.Adapter<MonthlyCalendarViewAdapter.ViewHolder> {

    private final YearMonth[] yearMonths;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] dateCells;
        private final TextView monthName;

        public ViewHolder(View view) {
            super(view);
            int numDateCells = 42;

            this.monthName = view.findViewById(R.id.textView_monthName);
            this.dateCells = new TextView[numDateCells];
            for (int i = 0; i < numDateCells; i ++) {
                String tag = "tag_textView_dateCell_" + (i + 1);
                this.dateCells[i] = view.findViewWithTag(tag);
            }
        }

        public TextView[] getDateCells() {
            return this.dateCells;
        }

        public TextView getMonthName() {
            return this.monthName;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param monthDays String[] containing days of months to populate
     */
    public MonthlyCalendarViewAdapter(YearMonth[] monthDays) {
        this.yearMonths = monthDays;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_month, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        TextView[] dateCells = viewHolder.getDateCells();
        TextView monthName = viewHolder.getMonthName();
        int startOfMonth = yearMonths[position].atDay(1).getDayOfWeek().getValue() % 7;
        int endOfMonth = yearMonths[position].lengthOfMonth() + startOfMonth;

        monthName.setText(yearMonths[position].getMonth().toString().toLowerCase());

        for (int i = startOfMonth; i < endOfMonth; i++) {
            int date = i - startOfMonth + 1;
            String text = Integer. toString(date);;
            dateCells[i].setText(text);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return yearMonths.length;
    }
}