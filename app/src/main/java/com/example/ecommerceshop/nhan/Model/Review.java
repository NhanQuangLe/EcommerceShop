package com.example.ecommerceshop.nhan.Model;

public class Review {
    String Content, ReviewDate, UserID;
    int StarRate;

    public Review() {
    }

    public Review(String content, String reviewDate, String userID, int starRate) {
        Content = content;
        ReviewDate = reviewDate;
        UserID = userID;
        StarRate = starRate;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getReviewDate() {
        return ReviewDate;
    }

    public void setReviewDate(String reviewDate) {
        ReviewDate = reviewDate;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public int getStarRate() {
        return StarRate;
    }

    public void setStarRate(int starRate) {
        StarRate = starRate;
    }
}
