package com.example.balancebeacon_fe.Models;

public class Goals {
    private int goalId;
    private int userTipId;
    private String areaDescription;
    private String tipDescription;
    private int tipStatus;
    private String goalDays;

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserTipId() {
        return userTipId;
    }

    public void setUserTipId(int userTipId) {
        this.userTipId = userTipId;
    }

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public String getTipDescription() {
        return tipDescription;
    }

    public void setTipDescription(String tipDescription) {
        this.tipDescription = tipDescription;
    }

    public int getTipStatus() {
        return tipStatus;
    }

    public void setTipStatus(int tipStatus) {
        this.tipStatus = tipStatus;
    }

    public String getGoalDays() {
        return goalDays;
    }

    public void setGoalDays(String goalDays) {
        this.goalDays = goalDays;
    }
}
