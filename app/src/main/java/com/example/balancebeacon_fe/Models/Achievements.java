package com.example.balancebeacon_fe.Models;

public class Achievements {
    private int userId = 0;
    private int assessmentCount = 0;
    private int goalCount = 0;
    private int goalCompletionCount = 0;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAssessmentCount() {
        return assessmentCount;
    }

    public void setAssessmentCount(int assessmentCount) {
        this.assessmentCount = assessmentCount;
    }

    public int getGoalCount() {
        return goalCount;
    }

    public void setGoalCount(int goalCount) {
        this.goalCount = goalCount;
    }

    public int getGoalCompletionCount() {
        return goalCompletionCount;
    }

    public void setGoalCompletionCount(int goalCompletionCount) {
        this.goalCompletionCount = goalCompletionCount;
    }
}
