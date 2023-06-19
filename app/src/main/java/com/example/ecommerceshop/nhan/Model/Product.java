package com.example.ecommerceshop.nhan.Model;

import java.util.ArrayList;

public class Product {
    String productAvatar, productBrand, productCategory, productName, productID, productSite, shopID;
    int purchaseQuantity;
    int productDiscountPrice;

    float productRating;
    ArrayList<Review> productReviews;

    public float getProductRating() {
        return productRating;
    }
    public void setProductRating(float productRating) {
        this.productRating = productRating;
    }

    public String getProductSite() {
        return productSite;
    }

    public void setProductSite(String productSite) {
        this.productSite = productSite;
    }


    public Product(){}

    public Product(String productAvatar, String productBrand, String productCategory, String productName, String productID, String productSite, String shopID, int purchaseQuantity, int productDiscountPrice, float productRating, ArrayList<Review> productReviews) {
        this.productAvatar = productAvatar;
        this.productBrand = productBrand;
        this.productCategory = productCategory;
        this.productName = productName;
        this.productID = productID;
        this.productSite = productSite;
        this.shopID = shopID;
        this.purchaseQuantity = purchaseQuantity;
        this.productDiscountPrice = productDiscountPrice;
        this.productRating = productRating;
        this.productReviews = productReviews;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public ArrayList<Review> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(ArrayList<Review> productReviews) {
        this.productReviews = productReviews;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public void setProductAvatar(String productAvatar) {
        this.productAvatar = productAvatar;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(int productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(int purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }
}
