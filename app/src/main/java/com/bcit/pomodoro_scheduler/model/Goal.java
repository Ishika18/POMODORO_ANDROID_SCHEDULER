package com.bcit.pomodoro_scheduler.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Objects;

public class Goal extends ScheduleEvent implements Serializable {

    private int totalTimeInMinutes;
    private Timestamp deadline;
    private Priority priority;

    public Goal(String id, String name, String location, int totalTimeInMinutes, Timestamp deadline,
                Priority priority, String url, String notes) {
        super(id, name, location, url, notes);
        this.totalTimeInMinutes = totalTimeInMinutes;
        this.deadline = deadline;
        this.priority = priority;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Goal goal = (Goal) o;
        return getTotalTimeInMinutes() == goal.getTotalTimeInMinutes()
                && getDeadline().equals(goal.getDeadline())
                && getPriority() == goal.getPriority();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTotalTimeInMinutes(), getDeadline(), getPriority());
    }
}
