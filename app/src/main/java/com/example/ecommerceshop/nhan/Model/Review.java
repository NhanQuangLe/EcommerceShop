package com.example.ecommerceshop.nhan.Model;

import android.net.Uri;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Review {
    String avatarCus, content, customerId, customerName, productId, reviewId, reviewDate, shopId, rvResponse;
    Double rating;
    ArrayList<String> uriList;
    String productAvatar, productName;

    public Review() {
    }

    public Review(String avatarCus, String content, String customerId, String customerName, String productId, String reviewId, String reviewDate, String shopId, String rvResponse, Double rating, ArrayList<String> uriList, String productAvatar, String productName) {
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
        this.productAvatar = productAvatar;
        this.productName = productName;
    }

    public String getAvatarCus() {
        return avatarCus;
    }

    public void setAvatarCus(String avatarCus) {
        this.avatarCus = avatarCus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getRvResponse() {
        return rvResponse;
    }

    public void setRvResponse(String rvResponse) {
        this.rvResponse = rvResponse;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public ArrayList<String> getUriList() {
        return uriList;
    }

    public void setUriList(ArrayList<String> uriList) {
        this.uriList = uriList;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public void setProductAvatar(String productAvatar) {
        this.productAvatar = productAvatar;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
