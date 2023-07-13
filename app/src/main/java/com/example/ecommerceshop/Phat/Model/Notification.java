package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class Notification implements Serializable {
    String avt, orderId, orderStatus, customerId, dateNotifi;


    public Notification() {
    }

    public Notification(String avt, String orderId, String orderStatus, String customerId, String dateNotifi) {
        this.avt = avt;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.dateNotifi = dateNotifi;

    }



    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDateNotifi() {
        return dateNotifi;
    }

    public void setDateNotifi(String dateNotifi) {
        this.dateNotifi = dateNotifi;
    }
}
