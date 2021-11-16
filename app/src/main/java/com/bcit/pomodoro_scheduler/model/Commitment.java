package com.bcit.pomodoro_scheduler.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Commitment extends ScheduleEvent implements Serializable {

    private Timestamp startTime;
    private Timestamp endTime;
    private Repeat repeat;

    public Commitment(String name, String location, Timestamp startTime, Timestamp endTime,
                      Repeat repeat) {
        super(name, location);
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeat = repeat;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Repeat getRepeat() {
        return repeat;
    }

    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }
}
