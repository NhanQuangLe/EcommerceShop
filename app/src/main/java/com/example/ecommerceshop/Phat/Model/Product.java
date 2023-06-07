package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String productId, productName, productDescription, productCategory, productBrand, productSite, productDiscountNote, uid;
    private int productQuantity, productPrice, productDiscountPrice;

    private List<String> uriList;

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public Product() {}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Product(String productId, String productName, String productDescription, String productCategory,
                   String productBrand, String productSite, String productDiscountNote , int productQuantity,
                   int productPrice, int productDiscountPrice , List<String> uriList, String uid) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.productBrand = productBrand;
        this.productSite = productSite;
        this.productDiscountNote = productDiscountNote;
        this.uid = uid;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.uriList = uriList;
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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSite() {
        return productSite;
    }

    public void setProductSite(String productSite) {
        this.productSite = productSite;
    }

    public String getProductDiscountNote() {
        return productDiscountNote;
    }

    public void setProductDiscountNote(String productDiscountNote) {
        this.productDiscountNote = productDiscountNote;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(int productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }
}
