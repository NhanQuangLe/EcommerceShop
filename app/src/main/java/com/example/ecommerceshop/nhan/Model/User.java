package com.example.ecommerceshop.nhan.Model;

public class User {
    private String UserID;
    private String UserType;

    public User(String userID, String userType) {
        UserID = userID;
        UserType = userType;
    }

    public User() {

    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}
