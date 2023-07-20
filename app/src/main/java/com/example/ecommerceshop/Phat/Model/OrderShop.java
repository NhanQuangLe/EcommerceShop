package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;
import java.util.List;

public class OrderShop implements Serializable {
    String customerId;
    String orderId;
    String orderStatus;
    String orderedDate;
    String shopId;

    public String getVoucherUsedId() {
        return voucherUsedId;
    }

    public void setVoucherUsedId(String voucherUsedId) {
        this.voucherUsedId = voucherUsedId;
    }

    String voucherUsedId;
    long shipPrice, discountPrice, totalPrice;

    receiveAddress receiveAddress;
    List<OrderItem> items;
    public OrderShop() {
    }

    public OrderShop(String customerId, String orderId, String orderStatus, String orderedDate, String shopId, String voucherUsedId, long shipPrice, long discountPrice, long totalPrice, com.example.ecommerceshop.Phat.Model.receiveAddress receiveAddress, List<OrderItem> items) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.shopId = shopId;
        this.voucherUsedId = voucherUsedId;
        this.shipPrice = shipPrice;
        this.discountPrice = discountPrice;
        this.totalPrice = totalPrice;
        this.receiveAddress = receiveAddress;
        this.items = items;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
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
