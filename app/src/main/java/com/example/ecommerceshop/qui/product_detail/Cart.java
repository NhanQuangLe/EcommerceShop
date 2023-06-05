package com.example.ecommerceshop.qui.product_detail;

public class Cart {
    private String productId;
    private int productQuantity;
    private String ShopId;

    public Cart(String productId, int productQuantity, String shopId) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        ShopId = shopId;
    }

    public Cart() {
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
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
}
