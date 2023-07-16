package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;

public interface IClickHistoryOrderListener {
    void GoToOrderDetail(Order order);
    void GotoRebuy(Order order);
    void GoToReview(Order order);
}
