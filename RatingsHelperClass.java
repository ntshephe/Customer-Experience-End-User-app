package com.example.cxmuserapp.helpersClasses;

public class RatingsHelperClass {
    private String userId;
    private String userName;
    private float rating;
    String ratingComment;

    String imageUrl;

    public RatingsHelperClass(String userId, String userName, float rating,String ratingComment, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.ratingComment =ratingComment;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
