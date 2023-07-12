package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    String reviewId,avatarCus,customerId, customerName,reviewedDate, productId, content, rvResponse;
    int rating;
    private List<String> uriList;

    public Review() {
    }

    public Review(String reviewId, String avatarCus, String customerId, String customerName, String reviewedDate, String productId, String content, String rvResponse, int rating, List<String> uriList) {
        this.reviewId = reviewId;
        this.avatarCus = avatarCus;
        this.customerId = customerId;
        this.customerName = customerName;
        this.reviewedDate = reviewedDate;
        this.productId = productId;
        this.content = content;
        this.rvResponse = rvResponse;
        this.rating = rating;
        this.uriList = uriList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRvResponse() {
        return rvResponse;
    }

    public void setRvResponse(String rvResponse) {
        this.rvResponse = rvResponse;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAvatarCus() {
        return avatarCus;
    }

    public void setAvatarCus(String avatarCus) {
        this.avatarCus = avatarCus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(String reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
