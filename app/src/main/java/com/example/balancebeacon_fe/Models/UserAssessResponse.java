package com.example.balancebeacon_fe.Models;

import java.util.List;

public class UserAssessResponse {
    private int responseCode;
    private String responseDescription;
    private int userId = 0;
    private int assessmentId = 0;
    private List<AssessmentPayload> assessmentPayloads;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(int assessmentId) {
        this.assessmentId = assessmentId;
    }

    public List<AssessmentPayload> getAssessmentPayloads() {
        return assessmentPayloads;
    }

    public void setAssessmentPayloads(List<AssessmentPayload> assessmentPayloads) {
        this.assessmentPayloads = assessmentPayloads;
    }
}
