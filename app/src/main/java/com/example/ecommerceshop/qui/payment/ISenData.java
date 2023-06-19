package com.example.ecommerceshop.qui.payment;

import java.util.List;

public interface ISenData {
    void senDataToAdapter(List<Voucher> vouchers);
    void senDataToPaymentActivity(List<ItemPayment> itemPaymentList);

}
