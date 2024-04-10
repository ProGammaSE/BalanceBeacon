package com.example.balancebeacon_fe.Models;

import java.util.ArrayList;

public class AssessAreaRequest {
    int userId;
    ArrayList<Areas> userChosenAreas;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Areas> getUserChosenAreas() {
        return userChosenAreas;
    }

    public void setUserChosenAreas(ArrayList<Areas> userChosenAreas) {
        this.userChosenAreas = userChosenAreas;
    }
}
