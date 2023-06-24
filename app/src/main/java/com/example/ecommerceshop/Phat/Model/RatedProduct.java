package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;
import java.util.List;

public class RatedProduct implements Serializable {
    private String productId,productName, productBrand;
    private int productPrice, reviewNum;
    private List<String> uriList;

    public RatedProduct(String productId, String productName, String productBrand, int productPrice, int reviewNum, List<String> uriList) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productPrice = productPrice;
        this.reviewNum = reviewNum;
        this.uriList = uriList;
    }

    public RatedProduct() {
    }

    public int getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(int reviewNum) {
        this.reviewNum = reviewNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
}
