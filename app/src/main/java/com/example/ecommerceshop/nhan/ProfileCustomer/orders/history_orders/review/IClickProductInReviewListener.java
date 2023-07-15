package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review;

import android.widget.RatingBar;

import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;

public interface IClickProductInReviewListener {
    void RemoveReview(Review review);
    void RatingBarChange(RatingBar ratingBar, double v, boolean b, Review review);
    void AddImage(ImageReviewAdapter imageReviewAdapter, Review review);
}
