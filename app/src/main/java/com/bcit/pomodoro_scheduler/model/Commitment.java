package com.bcit.pomodoro_scheduler.model;

import java.io.Serializable;
import com.google.firebase.Timestamp;
import java.util.Objects;

public class Commitment extends ScheduleEvent implements Serializable {

    private Timestamp startTime;
    private Timestamp endTime;
    private Repeat repeat;

    public Commitment(String id, String name, String location, Timestamp startTime, Timestamp endTime,
                      Repeat repeat, String url, String notes) {
        super(id, name, location, url, notes);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Commitment that = (Commitment) o;
        return getStartTime().equals(that.getStartTime())
                && getEndTime().equals(that.getEndTime())
                && getRepeat() == that.getRepeat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStartTime(), getEndTime(), getRepeat());
    }
}
