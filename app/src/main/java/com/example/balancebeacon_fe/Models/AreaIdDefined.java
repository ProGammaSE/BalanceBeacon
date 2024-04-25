package com.example.balancebeacon_fe.Models;

public class  AreaIdDefined {
    public static String getAreaName(int areaId) {
        String areaDescription = "";

        if (areaId == 4) {
            areaDescription = "Love. Family. Kids";
        }
        if (areaId == 5) {
            areaDescription = "Hobbies. Fun";
        }
        if (areaId == 6) {
            areaDescription = "Money";
        }
        if (areaId == 7) {
            areaDescription = "Education";
        }
        if (areaId == 8) {
            areaDescription = "Health & Fitness";
        }
        if (areaId == 9) {
            areaDescription = "Time Manage";
        }
        if (areaId == 10) {
            areaDescription = "Social & Friends";
        }
        if (areaId == 11) {
            areaDescription = "Recreation";
        }
        if (areaId == 12) {
            areaDescription = "Sex & Romance";
        }
        if (areaId == 13) {
            areaDescription = "Spirituality";
        }
        if (areaId == 14) {
            areaDescription = "Personal Growth";
        }
        return areaDescription;
    }
}
