package com.bcit.pomodoro_scheduler.weeklyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GoalScheduler {
    private ArrayList<GoalDataForScheduler> high_priority;
    private ArrayList<GoalDataForScheduler> med_priority;
    private ArrayList<GoalDataForScheduler> low_priority;

    public GoalScheduler(ArrayList<GoalDataForScheduler> goals) {
        high_priority = new ArrayList<>();
        med_priority = new ArrayList<>();
        low_priority = new ArrayList<>();
        setGoals(goals);
    }

    private void setGoals(ArrayList<GoalDataForScheduler> goals) {
        Collections.sort(goals, Collections.reverseOrder());
        separatePriorities(goals);
    }

    public void separatePriorities(ArrayList<GoalDataForScheduler> goals) {
        for (GoalDataForScheduler goal : goals) {
            if (goal.getTask_priority() == Priority.HIGH) {
                high_priority.add(goal);
            }
            if (goal.getTask_priority() == Priority.MEDIUM) {
                med_priority.add(goal);
            }
            if (goal.getTask_priority() == Priority.LOW) {
                low_priority.add(goal);
            }
        }
    }

    private ArrayList<GoalDataForDisplay> getGoalSchedule() {
        ArrayList<GoalDataForDisplay> schedule = new ArrayList<>();
        addPriorityToSchedule(schedule, high_priority);
        addPriorityToSchedule(schedule, med_priority);
        addPriorityToSchedule(schedule, low_priority);
        return schedule;
    }

    private void addPriorityToSchedule(ArrayList<GoalDataForDisplay> schedule, ArrayList<GoalDataForScheduler> priority) {
        addHighPriorityGoalsInInterleavedGroups(schedule, priority, 3);
        addHighPriorityGoalsInInterleavedGroups(schedule, priority, 2);
        addHighPriorityGoalsInInterleavedGroups(schedule, priority, 1);
    }

    private void addHighPriorityGoalsInInterleavedGroups(ArrayList<GoalDataForDisplay> schedule,
                                                         ArrayList<GoalDataForScheduler> priority,
                                                         int groupSize) {
        while (priority.size() >= groupSize) {
            for (int i = 0; i < groupSize && i < priority.size(); i++) {
                GoalDataForScheduler goalData = priority.get(i);
                if (goalData.getMinutes() > 0) {
                    GoalDataForDisplay goal = new GoalDataForDisplay(goalData.getID(), goalData.getName(), goalData.takeWorkBlock());
                    schedule.add(goal);
                    if (goalData.getMinutes() <= 0) priority.remove(i);
                } else {
                    priority.remove(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        int h, l, m;
        h = rand.nextInt(20) + 5;
        l = rand.nextInt(20) + 5;
        m = rand.nextInt(20) + 5;
        ArrayList<GoalDataForScheduler> goals = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            goals.add(new GoalDataForScheduler("id", "low " + i, Priority.LOW, i + 1, 150, 50));
        }
        for (int i = 0; i < 10; i++) {
            goals.add(new GoalDataForScheduler("id", "medium " + i, Priority.MEDIUM, i + 1, 150, 50));
        }
        for (int i = 0; i < 10; i++) {
            goals.add(new GoalDataForScheduler("id", "high " + i, Priority.HIGH, i + 1, 150, 50));
        }
        GoalScheduler scheduler = new GoalScheduler(goals);
        ArrayList<GoalDataForDisplay> schedule =  scheduler.getGoalSchedule();
        for(GoalDataForDisplay goal: schedule) {
            System.out.println(goal);
        }
    }
}

enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final Integer priority;

    private Priority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return this.priority;
    }
}

class GoalDataForDisplay {
    private String ID;
    private String name;
    private int minutes;

    public GoalDataForDisplay(String ID, String name, int minutes) {
        this.ID = ID;
        this.name = name;
        this.minutes = minutes;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return "GoalDataForDisplay{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", minutes=" + minutes +
                '}';
    }
}

class GoalDataForScheduler implements Comparable<GoalDataForScheduler> {
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

    @Override
    public int compareTo(GoalDataForScheduler o) {
        GoalDataForScheduler other = (GoalDataForScheduler) o;

        Integer this_priority = task_priority.getPriority();
        Integer other_priority = o.getTask_priority().getPriority();
        if (this_priority.compareTo(other_priority) != 0)
            return this_priority.compareTo(other_priority);

        Integer this_weighted_minutes = (minutes / days_left);
        Integer other_weighted_minutes = (o.getMinutes() / o.getDays_left());
        return this_weighted_minutes.compareTo(other_weighted_minutes);
    }
}























