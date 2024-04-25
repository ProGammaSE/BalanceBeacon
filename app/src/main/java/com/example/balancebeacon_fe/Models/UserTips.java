package com.example.balancebeacon_fe.Models;

public class UserTips {
    private int userTipId;
    private int userId;
    private int assessmentId;

    private int areaId;
    private int tipId;
    private String tipDescription;
    private int tipStatus;

    public int getUserTipId() {
        return userTipId;
    }

    public void setUserTipId(int userTipId) {
        this.userTipId = userTipId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getTipId() {
        return tipId;
    }

    public void setTipId(int tipId) {
        this.tipId = tipId;
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
}
