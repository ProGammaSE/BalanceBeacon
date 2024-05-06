package com.example.balancebeacon_fe.Models;

public class UpdateUserTips {

    int userId;

    int userTipId = 0;
    String areaDescription;
    String tipDescription;
    int tipStatus = 0;
    String goalDays = "";

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
