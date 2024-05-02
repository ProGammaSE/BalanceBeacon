package com.example.balancebeacon_fe.Models;

public class Feedback {
    private int feedbackId;
    private int userId;
    private float feedbackRate;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getFeedbackRate() {
        return feedbackRate;
    }

    public void setFeedbackRate(float feedbackRate) {
        this.feedbackRate = feedbackRate;
    }
}
