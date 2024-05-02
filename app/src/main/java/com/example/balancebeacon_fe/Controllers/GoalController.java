package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Goals;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GoalController {

    @GET("/goals/get/all")
    Call<List<Goals>> getAllGoals ();

}
