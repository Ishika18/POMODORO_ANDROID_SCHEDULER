package com.bcit.pomodoro_scheduler.model;

import androidx.annotation.NonNull;

public enum Repeat {
    SUN("Sunday"),
    MON("Monday"),
    TUE("Tuesday"),
    WED("Wednesday"),
    THR("Thursday"),
    FRI("Friday"),
    SAT("Saturday"),
    DAILY("Daily"),
    NEVER("Never");

    private final String value;

    private Repeat(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}
