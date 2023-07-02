package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class OrderShop implements Serializable {
    String customerId, orderId, orderStatus, orderedDate, shopId;
    long shipPrice, discountPrice, totalPrice;
    receiveAddress receiveAddress;
    public OrderShop() {
    }

    public OrderShop(String customerId, long discountPrice, String orderId, String orderStatus, String orderedDate, long shipPrice, String shopId, long totalPrice, com.example.ecommerceshop.Phat.Model.receiveAddress receiveAddress) {
        this.customerId = customerId;
        this.discountPrice = discountPrice;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.shipPrice = shipPrice;
        this.shopId = shopId;
        this.totalPrice = totalPrice;
        this.receiveAddress = receiveAddress;
    }

    public com.example.ecommerceshop.Phat.Model.receiveAddress getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(com.example.ecommerceshop.Phat.Model.receiveAddress receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public long getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(long shipPrice) {
        this.shipPrice = shipPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
