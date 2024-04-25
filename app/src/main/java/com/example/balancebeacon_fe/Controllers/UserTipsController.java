package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.UserTips;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserTipsController {

    @GET("/user/tips/{userId}/{assessmentId}/{status}")
    Call<List<UserTips>> getAllUserTips(@Path("userId") int userId, @Path("assessmentId") int assessmentId, @Path("status") int status);
}
