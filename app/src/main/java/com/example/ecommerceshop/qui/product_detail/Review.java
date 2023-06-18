package com.example.ecommerceshop.qui.product_detail;

import java.io.Serializable;

public class Review implements Serializable {
    private String Content;
    private String ReviewDate;
    private  double StarRate;
    private String UserID;

    public Review(String content, String reviewDate, double starRate, String userID) {
        Content = content;
        ReviewDate = reviewDate;
        StarRate = starRate;
        UserID = userID;
    }

    public Review() {
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

    public double getStarRate() {
        return StarRate;
    }

    public void setStarRate(double starRate) {
        StarRate = starRate;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
