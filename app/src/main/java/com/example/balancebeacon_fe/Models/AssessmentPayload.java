package com.example.balancebeacon_fe.Models;

public class AssessmentPayload {
    private int areaId;
    private String areaDescription;
    private int areaPercentage;
    private boolean areaStatus;

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaDescription() {
        return areaDescription;
    }

    public void setAreaDescription(String areaDescription) {
        this.areaDescription = areaDescription;
    }

    public int getAreaPercentage() {
        return areaPercentage;
    }

    public void setAreaPercentage(int areaPercentage) {
        this.areaPercentage = areaPercentage;
    }

    public boolean isAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(boolean areaStatus) {
        this.areaStatus = areaStatus;
    }
}
