package com.bcit.pomodoro_scheduler.model;

import androidx.annotation.NonNull;

public enum Repeat {
    EVERY_DAY("Everyday"),
    EVERY_WEEK("Every Week"),
    EVERY_TWO_WEEKS("Every Two Weeks"),
    EVERY_MONTH("Every Month"),
    EVERY_YEAR("Every Year"),
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
