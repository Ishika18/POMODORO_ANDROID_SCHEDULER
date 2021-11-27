package com.bcit.pomodoro_scheduler.model;

public class GoalDataForScheduler implements Comparable<GoalDataForScheduler> {
    private String ID;
    private String name;

    private Priority task_priority;
    private int days_left;

    private int minutes;
    private int block_size;

    public GoalDataForScheduler(String ID, String name, Priority task_priority, int days_left, int minutes, int block_size) {
        this.ID = ID;
        this.name = name;
        this.task_priority = task_priority;
        this.days_left = days_left;
        this.minutes = minutes;
        this.block_size = block_size;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Priority getTask_priority() {
        return task_priority;
    }

    public int getDays_left() {
        return days_left;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getBlockSize() {
        return block_size;
    }

    public int takeWorkBlock() {
        if (minutes - block_size < 0) {
            int return_val = minutes;
            minutes = 0;
            return return_val;
        } else {
            minutes -= block_size;
            return block_size;
        }
    }

    public int takeMinutes(int minutesToTake) {
        if (minutes - minutesToTake < 0) {
            int return_val = minutes;
            minutes = 0;
            return return_val;
        } else {
            minutes -= minutesToTake;
            return minutesToTake;
        }
    }

    @Override
    public int compareTo(GoalDataForScheduler o) {
        Integer this_priority = task_priority.getPriority();
        Integer other_priority = o.getTask_priority().getPriority();
        if (this_priority.compareTo(other_priority) != 0)
            return this_priority.compareTo(other_priority);

        Integer this_weighted_minutes = (minutes / days_left);
        Integer other_weighted_minutes = (o.getMinutes() / o.getDays_left());
        return this_weighted_minutes.compareTo(other_weighted_minutes);
    }
}
