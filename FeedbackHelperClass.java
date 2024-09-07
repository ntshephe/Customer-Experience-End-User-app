package com.example.cxmuserapp.helpersClasses;

public class FeedbackHelperClass {
    private String userId;
    private String userName;
    private String feedbackText;
    private String date;

    public FeedbackHelperClass() {
        // Default constructor required for Firebase
    }

    public FeedbackHelperClass(String userId, String userName, String feedbackText, String date) {
        this.userId = userId;
        this.userName = userName;
        this.feedbackText = feedbackText;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}