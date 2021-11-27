package com.bcit.pomodoro_scheduler.model;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private static final Map<Integer, Priority> names = Stream.of(Priority.values())
            .collect(Collectors.toMap(Priority::getPriority, Function.identity()));

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    private int getPriority() {
        return this.priority;
    }

    public static Priority fromValue(int value) {
        Priority priority = names.get(value);
        if (priority != null) {
            return priority;
        }
        throw new IllegalArgumentException("No enum found from value");
    }
}
