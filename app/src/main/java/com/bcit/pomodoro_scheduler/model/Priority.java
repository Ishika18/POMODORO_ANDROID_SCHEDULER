package com.bcit.pomodoro_scheduler.model;

public enum Priority {
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
