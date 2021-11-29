package com.bcit.pomodoro_scheduler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bcit.pomodoro_scheduler.R;
import com.bcit.pomodoro_scheduler.model.Task;
import com.bcit.pomodoro_scheduler.model.TaskType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;


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

        int startTime = localDataSet.get(position).getStartTime();
        LocalTime startLocalTime = LocalTime.of(startTime / 60, startTime % 60);

        int endTime = localDataSet.get(position).getEndTime();
        LocalTime endLocalTime = LocalTime.of(endTime / 60, endTime % 60);


        viewHolder.getStartTime().setText(startLocalTime.toString());
        viewHolder.getEndTime().setText(endLocalTime.toString());
        viewHolder.getTask().setText(localDataSet.get(position).getName());

        TaskType taskType = localDataSet.get(position).getType();
        View line = viewHolder.getLine();
        if (taskType == TaskType.BREAK){
            line.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.rally_blue));
        }
        if (taskType == TaskType.COMMITMENT){
            line.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.rally_orange));
        }
        if (taskType == TaskType.GOAL){
            line.setBackgroundColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.rally_green));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}