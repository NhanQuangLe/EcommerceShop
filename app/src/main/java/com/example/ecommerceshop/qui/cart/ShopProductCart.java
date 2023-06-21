package com.example.ecommerceshop.qui.cart;

import java.util.List;

public class ShopProductCart {
    private boolean isChecked;



    private String shopId;
    private String shopName;
    private List<ProductCart> productCarts;

    public ShopProductCart(String shopId, String shopName, List<ProductCart> productCarts) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.productCarts = productCarts;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ProductCart> getProductCarts() {
        return productCarts;
    }

    public void setProductCarts(List<ProductCart> productCarts) {
        this.productCarts = productCarts;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
