package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.AssessAreaRequest;
import com.example.balancebeacon_fe.Models.GeneralResponse;
import com.example.balancebeacon_fe.Models.UserAssessResponse;
import com.example.balancebeacon_fe.Models.UserResponse;
import com.example.balancebeacon_fe.Models.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AssessAreaController {
    @POST("/assess/area/request")
    Call<GeneralResponse> addUserAreas(@Body AssessAreaRequest assessAreaRequest);

    @GET("/assess/user/areas/all/{userId}")
    Call<UserAssessResponse> getAllUserAreas(@Path("userId") int userId);
}
