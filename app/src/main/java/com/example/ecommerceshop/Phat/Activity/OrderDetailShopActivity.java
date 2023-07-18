package com.example.ecommerceshop.Phat.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Adapter.AdapterListItemOrderDetail;
import com.example.ecommerceshop.Phat.Model.Notification;
import com.example.ecommerceshop.Phat.Model.OrderItem;
import com.example.ecommerceshop.Phat.Model.OrderShop;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderDetailShopActivity extends AppCompatActivity {
    TextView orderId, orderedDate,ReceiverName, phonenum_order,orderStatus,orderQuantity,addressBuyer,paymentMethod,deliveryMethod,orderDiscount,orderTotalPrice,deliveryPrice;
    RecyclerView r4;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String orderid, customerid, ordStatus;
    ArrayList<OrderItem> orderItems;
    AdapterListItemOrderDetail adapterListItemOrderDetail;
    ImageView backbtn, editbtn;
    int numOrderItem,size;
    String voucherIdOrder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_detail_shop);
        initUI();
         orderid = getIntent().getStringExtra("orderid");
         customerid = getIntent().getStringExtra("cusid");
        LoadOrderDetail();
        loadOrderItems();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailShopActivity.this);
                if(ordStatus.equals("UnProcessed")){
                    builder.setTitle("Edit Order Status")
                            .setItems(Constants.orderStatus, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String selectOpt = Constants.orderStatus[i];
                                    editOrdStatus(selectOpt);
                                }
                            }).show();
                } else if (ordStatus.equals("Processing")){
                    builder.setTitle("Edit Order Status")
                            .setItems(Constants.orderStatus1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String selectOpt = Constants.orderStatus1[i];
                                    editOrdStatus(selectOpt);
                                }
                            }).show();
                }

            }
        });
    }

    private void editOrdStatus(String selectOpt) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderStatus", selectOpt);
        final String[] voucherId = new String[1];
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(customerid).child("Customer").child("Orders").child(orderid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String vId = snapshot.child("voucherUserId").getValue(String.class);
                if (vId!=null) {
                    voucherId[0] = vId;
                    voucherIdOrder = vId;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderDetailShopActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        reference.child(customerid).child("Customer").child("Orders")
                .child(orderid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(selectOpt.equals("Completed")){
                    saveNotification(selectOpt);

                }
                if(selectOpt.equals("Processing")){
                    numOrderItem = 0;
                    size = orderItems.size();
                    processingOrder(selectOpt);


                }
                if(selectOpt.equals("Cancelled")){
                    HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
                    hashMap1.clear();
                    hashMap1.put("used", false);
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users");
                    reference1.child(customerid).child("Customer").child("Vouchers").child(voucherId[0])
                            .updateChildren(hashMap1, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    saveNotification(selectOpt);
                                }
                            });
                }
                Toast.makeText(OrderDetailShopActivity.this, "Order is now "+selectOpt, Toast.LENGTH_SHORT).show();

            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderDetailShopActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNotification(String selectOpt) {
        String timestamp = ""+System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();

        // Lấy giờ, phút, ngày, tháng, năm hiện tại
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Vì Calendar.MONTH bắt đầu từ 0
        int year = calendar.get(Calendar.YEAR);
        String date = hour+":"+minute+" "+day+"/"+month+"/"+year;
        Notification notification = new Notification(orderItems.get(0).getpAvatar(), orderid, selectOpt, customerid, date);
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Users");
        reference.child(customerid).child("Customer").child("Notifications").child(timestamp).setValue(notification);
    }
    private void processingOrder(String selectOpt){
        numOrderItem++;
        if (numOrderItem>size){
            deleteVoucher(selectOpt);
            return;
        }

        OrderItem orderItem = orderItems.get(numOrderItem-1);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Products")
                .child(orderItem.getPid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int psold = 0;
                        psold=snapshot.child("psoldQuantity").getValue(Integer.class);
                        int pQuantity = 0;
                        pQuantity =snapshot.child("productQuantity").getValue(Integer.class);
                        HashMap<String, Object> hashMap1 = new HashMap<>();
                        hashMap1.clear();
                        hashMap1.put("psoldQuantity", psold+orderItem.getpQuantity());
                        hashMap1.put("productQuantity", pQuantity-orderItem.getpQuantity());
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users");
                        reference1.child(firebaseAuth.getUid()).child("Shop").child("Products")
                                .child(orderItem.getPid()).updateChildren(hashMap1, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                       processingOrder(selectOpt);
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderDetailShopActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void deleteVoucher(String selectOpt){
        if(voucherIdOrder!=null){
            Toast.makeText(OrderDetailShopActivity.this, ""+voucherIdOrder, Toast.LENGTH_SHORT).show();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(customerid).child("Customer").child("Vouchers").child(voucherIdOrder).removeValue(new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    reference.child(firebaseAuth.getUid()).child("Shop")
                            .child("Vouchers").child(voucherIdOrder).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int pQuantity = 0;
                                    pQuantity =snapshot.child("quantity").getValue(Integer.class);
                                    HashMap<String, Object> hashMap1 = new HashMap<>();
                                    hashMap1.clear();

                                    hashMap1.put("quantity", pQuantity-1);
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users");
                                    reference1.child(firebaseAuth.getUid()).child("Shop").child("Vouchers")
                                            .child(voucherIdOrder).updateChildren(hashMap1, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    saveNotification(selectOpt);
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(OrderDetailShopActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });


        }
        else   {
            saveNotification(selectOpt);
        }
    }

    private void LoadOrderDetail() {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(customerid).child("Customer").child("Orders").child(orderid)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                OrderShop orderShop = snapshot.getValue(OrderShop.class);
                                orderId.setText(orderShop.getOrderId());
                                ReceiverName.setText(orderShop.getReceiveAddress().getFullName());
                                orderedDate.setText(orderShop.getOrderedDate());
                                phonenum_order.setText(orderShop.getReceiveAddress().getPhoneNumber());
                                orderStatus.setText(orderShop.getOrderStatus());
                                ordStatus= orderShop.getOrderStatus();
                                if(orderShop.getOrderStatus().equals("UnProcessed")){
                                    orderStatus.setTextColor(Color.RED);
                                }
                                if(orderShop.getOrderStatus().equals("Processing")){
                                    orderStatus.setTextColor(Color.BLUE);
                                }
                                if(orderShop.getOrderStatus().equals("Completed")){
                                    orderStatus.setTextColor(getColor(R.color.green1));
                                    editbtn.setVisibility(View.GONE);
                                }
                                if(orderShop.getOrderStatus().equals("Cancelled")){
                                    orderStatus.setTextColor(Color.GRAY);
                                    editbtn.setVisibility(View.GONE);
                                }
                                addressBuyer.setText(orderShop.getReceiveAddress().getAddress());
                                orderDiscount.setText(String.valueOf(orderShop.getDiscountPrice()));
                                deliveryPrice.setText(String.valueOf(orderShop.getShipPrice()));
                                orderTotalPrice.setText(String.valueOf(orderShop.getTotalPrice()));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    private void loadOrderItems(){
        orderItems=new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(customerid).child("Customer").child("Orders").child(orderid).child("items")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderItems.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            OrderItem orderItem = ds.getValue(OrderItem.class);
                            orderItems.add(orderItem);
                        }
                        adapterListItemOrderDetail=new AdapterListItemOrderDetail(OrderDetailShopActivity.this, orderItems);
                        r4.setAdapter(adapterListItemOrderDetail);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initUI(){
        orderId=findViewById(R.id.orderId);
        ReceiverName=findViewById(R.id.ReceiverName);
        orderedDate=findViewById(R.id.orderedDate);
        phonenum_order=findViewById(R.id.phonenum_order);
        orderStatus=findViewById(R.id.orderStatus);
        orderQuantity=findViewById(R.id.orderQuantity);
        addressBuyer=findViewById(R.id.addressBuyer);
        paymentMethod=findViewById(R.id.paymentMethod);
        deliveryMethod=findViewById(R.id.deliveryMethod);
        orderDiscount=findViewById(R.id.orderDiscount);
        backbtn=findViewById(R.id.backbtn);
        editbtn=findViewById(R.id.editbtn);
        orderTotalPrice=findViewById(R.id.orderTotalPrice);
        deliveryPrice=findViewById(R.id.deliveryPrice);
        r4=findViewById(R.id.r4);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(OrderDetailShopActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
    }
}
