package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Coaches;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CoachController {
    @GET("/coach/get/all")
    Call<List<Coaches>> getAllCoaches();
}
