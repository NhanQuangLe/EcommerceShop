package com.example.ecommerceshop.qui.payment;

import com.example.ecommerceshop.qui.cart.ProductCart;

import java.util.List;

public class ItemPayment {
    private String shopId;
    private String shopName;
    private List<ProductCart> listProductCart;
    private long tienKhuyenMai;



    public ItemPayment(String shopId, String shopName, List<ProductCart> listProductCart) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.listProductCart = listProductCart;
    }

    public ItemPayment() {
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

    public List<ProductCart> getListProductCart() {
        return listProductCart;
    }

    public void setListProductCart(List<ProductCart> listProductCart) {
        this.listProductCart = listProductCart;
    }
    public long getTienKhuyenMai() {
        return tienKhuyenMai;
    }

    public void setTienKhuyenMai(long tienKhuyenMai) {
        this.tienKhuyenMai = tienKhuyenMai;
    }
}
