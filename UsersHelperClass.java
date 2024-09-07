package com.example.cxmuserapp;

public class UsersHelperClass {

    String userId; // Added field for Firebase Authentication UID
    String name;
    String email;
    String mobile;
    String pass;
    String userType;
    String imageUrl;

    public UsersHelperClass() {
    }

    public UsersHelperClass(String userId, String name, String email, String mobile, String pass, String userType, String imageUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.pass = pass;
        this.userType = userType;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

