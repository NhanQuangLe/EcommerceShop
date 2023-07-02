package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;

import android.widget.RatingBar;

import com.example.ecommerceshop.nhan.Model.Product;

public interface IClickProductInReviewListener {
    void RemoveReview(Product product);
    void RatingBarChange(RatingBar ratingBar, float v, boolean b, Product product);
}
