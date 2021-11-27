package com.bcit.pomodoro_scheduler.model;

import java.util.Objects;

public abstract class ScheduleEvent  {
    // Currently decided with creation timestamp as Event ID
    private String id;
    private String name;
    private String location;
    private String url;
    private String notes;

    public ScheduleEvent(String id, String name, String location, String url, String notes) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.url = url;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleEvent that = (ScheduleEvent) o;
        return getId().equals(that.getId()) && getName().equals(that.getName())
                && Objects.equals(getLocation(), that.getLocation())
                && Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getNotes(), that.getNotes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLocation(), getUrl(), getNotes());
    }
}
