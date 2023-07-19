package com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceshop.databinding.FragmentHistoryOrdersBinding;
import com.example.ecommerceshop.nhan.Model.Address;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.Order;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.OrderDetailActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.review.ReviewActivity;
import com.example.ecommerceshop.qui.cart.CartActivity;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryOrdersFragment extends Fragment {
    public static final int HISTORY_ORDER = 1601;
    public HistoryOrdersFragment() {
    }
    FirebaseAuth firebaseAuth;
    FragmentHistoryOrdersBinding fragmentHistoryOrdersBinding;
    HistoryOrdersAdapter mHistoryAdapter;
    public static RecyclerView mHistoryAdapterView;
    ArrayList<Order> listOrders;
    View mViewFragment;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                }
            });

    public static HistoryOrdersFragment newInstance() {
        HistoryOrdersFragment fragment = new HistoryOrdersFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentHistoryOrdersBinding = FragmentHistoryOrdersBinding.inflate(inflater, container, false);
        mViewFragment = fragmentHistoryOrdersBinding.getRoot();
        listOrders = new ArrayList<>();
        mHistoryAdapterView = fragmentHistoryOrdersBinding.rvHistoryOrder;
        firebaseAuth = FirebaseAuth.getInstance();
        mHistoryAdapter = new HistoryOrdersAdapter(getContext(), listOrders, new IClickHistoryOrderListener() {
            @Override
            public void GoToOrderDetail(Order order) {
                ToOrderDetail(order);
            }
            @Override
            public void GotoRebuy(Order order){
                ToRebuy(order);
            }

            @Override
            public void GoToReview(Order order) {
                ToReview(order);
            }
        });
        mHistoryAdapterView.setAdapter(mHistoryAdapter);
        LoadData();
        return mViewFragment;
    }

    public void LoadData() {
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");

        dbReference.child(firebaseAuth.getUid())
                .child("Customer")
                .child("Orders").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listOrders.clear();
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            if(ds.child("orderStatus").getValue(String.class).equals("Completed"))
                            {
                                Order ho = new Order();
                                ho.setOrderId(ds.child("orderId").getValue(String.class));
                                ho.setCustomerId(ds.child("customerId").getValue(String.class));
                                dbReference.child(ds.child("shopId").getValue(String.class))
                                        .child("Shop")
                                        .child("ShopInfos").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                ho.setShopAvt(snapshot.child("shopAvt").getValue(String.class));
                                                ho.setShopName(snapshot.child("shopName").getValue(String.class));
                                                ho.setShopId(ds.child("shopId").getValue(String.class));
                                                ho.setShipPrice(ds.child("shipPrice").getValue(int.class));
                                                ho.setDiscountPrice(ds.child("discountPrice").getValue(int.class));
                                                ho.setOrderStatus(ds.child("orderStatus").getValue(String.class));
                                                ho.setTotalPrice(ds.child("totalPrice").getValue(int.class));
                                                ho.setOrderedDate(ds.child("orderDate").getValue(String.class));
                                                ho.setReceiveAddress(ds.child("receiveAddress").getValue(Address.class));
                                                ho.setShipPrice(ds.child("shipPrice").getValue(int.class));
                                                ArrayList<Product> products = new ArrayList<>();
                                                for(DataSnapshot product : ds.child("items").getChildren())
                                                {
                                                    Product pd = new Product();
                                                    pd.setProductID(product.child("pid").getValue(String.class));
                                                    pd.setShopID(ds.child("shopId").getValue(String.class));
                                                    pd.setProductAvatar(product.child("pAvatar").getValue(String.class));
                                                    pd.setProductBrand(product.child("pBrand").getValue(String.class));
                                                    pd.setProductName(product.child("pName").getValue(String.class));
                                                    pd.setProductCategory(product.child("pCategory").getValue(String.class));
                                                    pd.setProductDiscountPrice(product.child("pPrice").getValue(int.class));
                                                    pd.setPurchaseQuantity(product.child("pQuantity").getValue(int.class));
                                                    products.add(pd);
                                                }
                                                ho.setItems(products);
                                                listOrders.add(ho);
                                                mHistoryAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                CustomToast.makeText(getContext(),error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                                            }
                                        });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        CustomToast.makeText(getContext(),error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });
    }
    public void ToOrderDetail(Order order){
        try{
            Intent intent = new Intent(getContext(), OrderDetailActivity.class);
            intent.putExtra("HistoryOrder", order);
            mActivityLauncher.launch(intent);
        }
        catch (Exception e)
        {
            Log.d("Nhanle", e + "");
        }
    }
    public void ToRebuy(Order order){
        try{
            Intent intent = new Intent(getContext(), CartActivity.class);
            intent.putExtra("HistoryOrder", order);
            intent.putExtra("Key", HISTORY_ORDER);
            mActivityLauncher.launch(intent);
        }
        catch (Exception e)
        {
            Log.d("Nhanle", e + "");
        }
    }
    public void ToReview(Order order){
        try{
            Intent intent = new Intent(getContext(), ReviewActivity.class);
            intent.putExtra("HistoryOrder", order);
            intent.putExtra("i", listOrders.indexOf(order));
            intent.putExtra("isHistoryOrder",true);
            mActivityLauncher.launch(intent);
        }
        catch (Exception e)
        {
            Log.d("Nhanle", e + "");
        }

    }
}