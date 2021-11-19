package com.bcit.pomodoro_scheduler.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Goal extends ScheduleEvent implements Serializable {

    private String notes;
    private int totalTimeInMinutes;
    private Timestamp deadline;
    private Priority priority;

    public Goal(String name, String location, String notes, int totalTimeInMinutes,
                Timestamp deadline, Priority priority) {
        super(name, location);
        this.notes = notes;
        this.totalTimeInMinutes = totalTimeInMinutes;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getTotalTimeInMinutes() {
        return totalTimeInMinutes;
    }

    public void setTotalTimeInMinutes(int totalTimeInMinutes) {
        this.totalTimeInMinutes = totalTimeInMinutes;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
