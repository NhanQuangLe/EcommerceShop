package com.example.ecommerceshop.nhan.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Review {
    String avatarCus, content, customerId, customerName, productId, reviewId, reviewDate, shopId, rvResponse;
    float rating;
    ArrayList<String> uriList;

    public Review() {
    }

    public Review(String avatarCus, String content, String customerId, String customerName, String productId, String reviewId, String reviewDate, String shopId, String rvResponse, float rating, ArrayList<String> uriList) {
        this.avatarCus = avatarCus;
        this.content = content;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productId = productId;
        this.reviewId = reviewId;
        this.reviewDate = reviewDate;
        this.shopId = shopId;
        this.rvResponse = rvResponse;
        this.rating = rating;
        this.uriList = uriList;
    }

    public void setAvatarCus(String avatarCus) {
        this.avatarCus = avatarCus;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setRvResponse(String rvResponse) {
        this.rvResponse = rvResponse;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setUriList(ArrayList<String> uriList) {
        this.uriList = uriList;
    }

    public String getAvatarCus() {
        return avatarCus;
    }

    public String getContent() {
        return content;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductId() {
        return productId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getShopId() {
        return shopId;
    }

    public String getRvResponse() {
        return rvResponse;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<String> getUriList() {
        return uriList;
    }
}
