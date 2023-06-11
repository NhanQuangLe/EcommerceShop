package com.example.ecommerceshop.nhan.Model;

public class Cart{
    String productID, shopID;
    int productQuantity;

    public Cart() {
    }

    public Cart(String productID, String shopID, int productQuantity) {
        this.productID = productID;
        this.shopID = shopID;
        this.productQuantity = productQuantity;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
