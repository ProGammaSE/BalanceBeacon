package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.UpdateUserTips;
import com.example.balancebeacon_fe.Models.UserTips;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserTipsController {

    @GET("/user/tips/{userId}/{assessmentId}/{status}")
    Call<List<UserTips>> getAllUserTips(@Path("userId") int userId, @Path("assessmentId") int assessmentId, @Path("status") int status);

    @POST("/user/tips/update")
    Call <GeneralResponse> updateUserTips(@Body UpdateUserTips updateUserTips);
}
