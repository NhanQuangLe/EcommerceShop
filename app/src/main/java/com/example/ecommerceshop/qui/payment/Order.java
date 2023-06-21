package com.example.ecommerceshop.qui.payment;

import com.example.ecommerceshop.nhan.Model.Address;

import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private long discountPrice;
    private String orderStatus;
    private String orderedDate;
    private long shipPrice;
    private String shopId;
    private long totalPrice;
    private List<ItemOrder> Items;
    private Address receiveAddress;

    public Order() {
    }

    public Order(String orderId, String customerId, long discountPrice, String orderStatus, String orderedDate, long shipPrice, String shopId, long totalPrice, List<ItemOrder> items, Address receiveAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.discountPrice = discountPrice;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.shipPrice = shipPrice;
        this.shopId = shopId;
        this.totalPrice = totalPrice;
        this.Items = items;
        this.receiveAddress = receiveAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public List<ItemOrder> getItems() {
        return Items;
    }

    public void setItems(List<ItemOrder> items) {
        Items = items;
    }

    public Address getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(Address receiveAddress) {
        this.receiveAddress = receiveAddress;
    }
}
