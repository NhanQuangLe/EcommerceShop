package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

public interface IClickHistoryOrderListener {
    void GoToOrderDetail(HistoryOrder historyOrder);
    void GotoRebuy(HistoryOrder historyOrder);
    void GoToReview(HistoryOrder historyOrder);
}
