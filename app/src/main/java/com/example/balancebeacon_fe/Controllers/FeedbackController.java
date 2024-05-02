package com.example.balancebeacon_fe.Controllers;

import com.example.balancebeacon_fe.Models.Feedback;
import com.example.balancebeacon_fe.Models.GeneralResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FeedbackController {
    @POST("/feedback/add")
    Call<GeneralResponse> addFeedback(@Body Feedback feedback);
}
