package com.bcit.pomodoro_scheduler.weeklyView;

public class WeekViewDateItem {
    private String dayStr;
    private int dayNum;

    public WeekViewDateItem(String dayStr, int dayNum) {
        this.dayStr = dayStr;
        this.dayNum = dayNum;
    }

    public String getDayStr() {
        return dayStr;
    }

    public int getDayNum() {
        return dayNum;
    }
}
