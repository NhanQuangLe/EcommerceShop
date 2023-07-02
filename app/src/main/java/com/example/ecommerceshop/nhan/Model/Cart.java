package com.example.ecommerceshop.nhan.Model;

public class Cart{
    String cartId, productId, shopId;
    int productQuantity;

    public Cart() {
    }

    public Cart(String cartId, String productId, String shopId, int productQuantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.shopId = shopId;
        this.productQuantity = productQuantity;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}
