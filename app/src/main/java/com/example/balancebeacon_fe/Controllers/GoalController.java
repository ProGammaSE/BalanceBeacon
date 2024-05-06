package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Goals;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GoalController {

    @GET("/goals/get/all/{userId}")
    Call<List<Goals>> getAllGoals (@Path("userId") int userId);

}
