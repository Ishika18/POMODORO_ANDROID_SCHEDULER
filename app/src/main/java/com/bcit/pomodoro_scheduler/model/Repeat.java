package com.bcit.pomodoro_scheduler.model;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final Map<String, Repeat> names = Stream.of(Repeat.values())
            .collect(Collectors.toMap(Repeat::toString, Function.identity()));
    private final String value;

    Repeat(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

    public static Repeat fromValue(String value) {
        Repeat repeat = names.get(value);
        if (repeat != null) {
            return repeat;
        }
        throw new IllegalArgumentException("Repeat enum not found from value.");
    }
}
