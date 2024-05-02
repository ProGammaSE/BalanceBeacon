package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Achievements;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AchievementController {
    @GET("/achievements/get/all/{userId}")
    Call<Achievements> getAchievements(@Path("userId") int userId);
}
