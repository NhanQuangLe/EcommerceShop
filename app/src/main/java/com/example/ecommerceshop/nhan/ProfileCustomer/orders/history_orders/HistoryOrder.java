package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;


import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.Product;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoryOrder implements Serializable {
    String orderId, shopId, customerId, orderStatus, orderedDate;
    int discountPrice, shipPrice, totalPrice;
    String shopAvt, shopName;
    Address receiveAddress;
    ArrayList<Product> items;
    public HistoryOrder(){}

    public HistoryOrder(String orderId, String shopId, String customerId, String orderStatus, String orderedDate, int discountPrice, int shipPrice, int totalPrice, String shopAvt, String shopName, Address receiveAddress, ArrayList<Product> items) {
        this.orderId = orderId;
        this.shopId = shopId;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.discountPrice = discountPrice;
        this.shipPrice = shipPrice;
        this.totalPrice = totalPrice;
        this.shopAvt = shopAvt;
        this.shopName = shopName;
        this.receiveAddress = receiveAddress;
        this.items = items;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getShipPrice() {
        return shipPrice;
    }

    public void setShipPrice(int shipPrice) {
        this.shipPrice = shipPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShopAvt() {
        return shopAvt;
    }

    public void setShopAvt(String shopAvt) {
        this.shopAvt = shopAvt;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Address getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(Address receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }
}
