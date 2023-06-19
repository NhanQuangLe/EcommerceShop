package com.example.ecommerceshop.nhan.ProfileCustomer.orders.HistoryOrders;


import com.example.ecommerceshop.nhan.Model.Product;

import java.util.ArrayList;

public class HistoryOrder{
    String CreateDate, OrderID, ReceiveAddress, Status, ShopAvatar, ShopName, ShopID;
    int TotalPrice, TransportFee;
    Product Product;
    public HistoryOrder(){}

    public HistoryOrder(String createDate, String orderID, String receiveAddress, String status, String shopAvatar, String shopName, String shopID, int totalPrice, int transportFee, com.example.ecommerceshop.nhan.Model.Product product) {
        CreateDate = createDate;
        OrderID = orderID;
        ReceiveAddress = receiveAddress;
        Status = status;
        ShopAvatar = shopAvatar;
        ShopName = shopName;
        ShopID = shopID;
        TotalPrice = totalPrice;
        TransportFee = transportFee;
        Product = product;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getReceiveAddress() {
        return ReceiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        ReceiveAddress = receiveAddress;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getShopAvatar() {
        return ShopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        ShopAvatar = shopAvatar;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public int getTransportFee() {
        return TransportFee;
    }

    public void setTransportFee(int transportFee) {
        TransportFee = transportFee;
    }

    public com.example.ecommerceshop.nhan.Model.Product getProduct() {
        return Product;
    }

    public void setProduct(com.example.ecommerceshop.nhan.Model.Product product) {
        Product = product;
    }
}
