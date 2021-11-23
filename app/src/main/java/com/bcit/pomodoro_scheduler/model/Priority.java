package com.bcit.pomodoro_scheduler.model;

public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int priority;

    private Priority(int priority) {
        this.priority = priority;
    }

    private int getPriority() {
        return this.priority;
    }
}
