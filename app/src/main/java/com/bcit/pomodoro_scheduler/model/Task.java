package com.bcit.pomodoro_scheduler.model;

public class Task implements Comparable<Task> {
    private String ID;
    private String name;
    private int startTime;
    private int endTime;
    private TaskType type;

    public Task(String ID, String name, int startTime, int endTime, TaskType type) {
        this.ID = ID;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public int compareTo(Task o) {
        Integer thisStarTime = this.startTime;
        Integer otherStartTime = o.getStartTime();
        return thisStarTime.compareTo(otherStartTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                '}';
    }
}
