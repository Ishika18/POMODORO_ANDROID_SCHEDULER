package com.bcit.pomodoro_scheduler.goals;

import com.google.firebase.Timestamp;

public class Goal {
    private final String id;
    private final String title;
    private final String location;
    private final int taskTimeInMinutes;
    private final Timestamp deadline;
    private final String priority;
    private final String url;
    private final String notes;

    public Goal (
            String id, String title, String location,
            int taskTimeInMinutes, Timestamp deadline, String priority,
            String url, String notes
    ) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.taskTimeInMinutes = taskTimeInMinutes;
        this.deadline = deadline;
        this.priority = priority;
        this.url = url;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public int getTaskTimeInMinutes() {
        return taskTimeInMinutes;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getUrl() {
        return url;
    }

    public String getNotes() {
        return notes;
    }
}

