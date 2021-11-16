package com.bcit.pomodoro_scheduler.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Goal extends ScheduleEvent implements Serializable {

    private int totalTime;
    private Timestamp deadline;
    private Repeat repeat;
    private Priority priority;

    public Goal(String name, String location, int totalTime, Timestamp deadline, Repeat repeat,
                Priority priority) {
        super(name, location);
        this.totalTime = totalTime;
        this.deadline = deadline;
        this.repeat = repeat;
        this.priority = priority;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}
