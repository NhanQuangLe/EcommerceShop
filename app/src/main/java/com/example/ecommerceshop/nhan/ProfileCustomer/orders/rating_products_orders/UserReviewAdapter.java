package com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.HistoryProductsInOrderAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.history_orders.IClickHistoryOrderListener;

import java.util.ArrayList;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {

    private Context context;
    private ArrayList<Review> orders;
    private HistoryProductsInOrderAdapter historyProductsInOrderAdapter;
    private IClickHistoryOrderListener mClickHistoryOrderListener;
    public UserReviewAdapter(Context context, ArrayList<Review> orders, IClickHistoryOrderListener a) {
        this.context = context;
        this.orders = orders;
        this.mClickHistoryOrderListener = a;
    }
    @NonNull
    @Override
    public UserReviewAdapter.UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_order_item, parent,false);
        return new UserReviewAdapter.UserReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewAdapter.UserReviewViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class UserReviewViewHolder extends RecyclerView.ViewHolder{

        public UserReviewViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
