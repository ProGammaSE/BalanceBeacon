package com.example.balancebeacon_fe.Models;

public class AssessmentPayload {
    private int areaId;
    private String areaDescription;
    private int areaCurrent;
    private int areaFuture;
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

    public int getAreaCurrent() {
        return areaCurrent;
    }

    public void setAreaCurrent(int areaCurrent) {
        this.areaCurrent = areaCurrent;
    }

    public int getAreaFuture() {
        return areaFuture;
    }

    public void setAreaFuture(int areaFuture) {
        this.areaFuture = areaFuture;
    }

    public boolean isAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(boolean areaStatus) {
        this.areaStatus = areaStatus;
    }
}
