package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class OrderShop implements Serializable {
    String customerId, discountPrice, orderId, orderStatus, orderedDate, receiveAddress, receiveName, receivePhone,
    shipPrice, shopId, totalPrice;

    public OrderShop() {
    }

    public OrderShop(String customerId, String discountPrice, String orderId, String orderStatus, String orderedDate, String receiveAddress,
                     String receiveName, String receivePhone, String shipPrice, String shopId, String totalPrice) {
        this.customerId = customerId;
        this.discountPrice = discountPrice;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.receiveAddress = receiveAddress;
        this.receiveName = receiveName;
        this.receivePhone = receivePhone;
        this.shipPrice = shipPrice;
        this.shopId = shopId;
        this.totalPrice = totalPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
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

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(String shipPrice) {
        this.shipPrice = shipPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
