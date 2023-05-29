package com.example.ecommerceshop.nhan.Model;

public class Shop extends User{
    private String UserID;
    public Shop(){}

    @Override
    public String getUserID() {
        return UserID;
    }

    @Override
    public void setUserID(String userID) {
        UserID = userID;
    }
}
