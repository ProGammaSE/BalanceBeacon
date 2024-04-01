package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Login;
import com.example.balancebeacon_fe.Models.UserResponse;
import com.example.balancebeacon_fe.Models.Users;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserController {
    @POST("/user/login")
    Call<UserResponse> userLogin(@Body Login login);

    @POST("/user/register")
    Call<UserResponse> registerUser(@Body Users user);
}
