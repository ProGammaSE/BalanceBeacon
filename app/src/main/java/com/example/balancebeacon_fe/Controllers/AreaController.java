package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Areas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AreaController {
    @GET("/area/all")
    Call<List<Areas>> getAllAreas();
}
