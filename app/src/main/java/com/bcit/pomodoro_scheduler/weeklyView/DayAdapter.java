package com.bcit.pomodoro_scheduler.weeklyView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.model.TaskType;

import java.time.LocalTime;
import java.util.ArrayList;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private ArrayList<Task> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView startTime;
        private final TextView endTime;
        private final TextView task;
        private final View line;

        public ViewHolder(View view) {
            super(view);
            startTime = view.findViewById(R.id.textView_itemDay_start);
            endTime = view.findViewById(R.id.textView_itemDay_end);
            task = view.findViewById(R.id.textView_itemDay_taskName);
            line = view.findViewById(R.id.view_itemDay_line);
        }
        public TextView getEndTime() {
            return endTime;
        }

        public TextView getStartTime() {
            return startTime;
        }

        public TextView getTask() {
            return task;
        }

        public View getLine() {
            return line;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public DayAdapter(ArrayList<Task> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_day, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Task task = localDataSet.get(position);
        LocalTime startTime = LocalTime.of(task.getStartTime() / 60, task.getStartTime() % 60);
        LocalTime endTime = LocalTime.of(task.getEndTime() / 60, task.getEndTime() % 60);
        viewHolder.getStartTime().setText(startTime.toString());
        viewHolder.getEndTime().setText(endTime.toString());
        viewHolder.getTask().setText(task.getName());

        if (task.getType() == TaskType.COMMITMENT) {
            viewHolder.getLine().setBackgroundColor(Color.parseColor("#FFCF44"));
        }
        if (task.getType() == TaskType.GOAL) {
            viewHolder.getLine().setBackgroundColor(Color.parseColor("#B15DFF"));
        }
        if (task.getType() == TaskType.BREAK) {
            viewHolder.getLine().setBackgroundColor(Color.parseColor("#1EB980"));
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}