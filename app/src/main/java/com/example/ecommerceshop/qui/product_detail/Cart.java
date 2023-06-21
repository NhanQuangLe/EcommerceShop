package com.example.ecommerceshop.qui.product_detail;

public class Cart {
    private String cartId;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    private String productId;
    private int productQuantity;
    private String shopId;

    public Cart(String cartId, String productId, int productQuantity, String shopId) {
        this.cartId = cartId;
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.shopId = shopId;
    }

    public Cart() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
