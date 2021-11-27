package com.bcit.pomodoro_scheduler.model;

public enum TaskType {
    COMMITMENT("commitment"),
    BREAK("break"),
    GOAL("goal");

    private final String commitment;

    TaskType(String commitment) {
        this.commitment = commitment;
    }

    public String getCommitment() {
        return this.commitment;
    }
}
