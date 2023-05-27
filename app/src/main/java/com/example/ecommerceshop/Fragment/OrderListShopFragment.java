package com.example.ecommerceshop.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerceshop.R;
public class OrderListShopFragment extends Fragment {
    View mview;
    ImageView imageView4;
    EditText searchView;
    TextView orderstatustv;
    RecyclerView orderList;
    HorizontalScrollView statusList;
    boolean isfilter=false;
    AppCompatButton cancelled,completed,processing,unprocessed,allorders;
    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_order_list_shop, container, false);
        initUI();
        LoadAllOrders();
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isfilter){
                    statusList.setVisibility(View.VISIBLE);
                    imageView4.setImageResource(R.drawable.ic_un_filter);
                    isfilter=true;
                }
                else {
                    statusList.setVisibility(View.GONE);
                    imageView4.setImageResource(R.drawable.ic_filter);
                    isfilter=false;
                }
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        allorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAllOrders();
                orderstatustv.setText("All orders");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        unprocessed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("UnProcessed");
                orderstatustv.setText("UnProcessed");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Processing");
                orderstatustv.setText("Processing");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Completed");
                orderstatustv.setText("Completed");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item);
            }
        });
        cancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadFilterOrderList("Cancelled");
                orderstatustv.setText("Cancelled");
                allorders.setBackgroundResource(R.drawable.bg_filter_list_item);
                unprocessed.setBackgroundResource(R.drawable.bg_filter_list_item);
                processing.setBackgroundResource(R.drawable.bg_filter_list_item);
                completed.setBackgroundResource(R.drawable.bg_filter_list_item);
                cancelled.setBackgroundResource(R.drawable.bg_filter_list_item_checked);
            }
        });
        return mview;
    }

    private void LoadFilterOrderList(String status) {
    }

    private void LoadAllOrders() {
    }

    private void initUI(){
        imageView4 = mview.findViewById(R.id.imageView4);
        searchView = mview.findViewById(R.id.searchView);
        orderstatustv = mview.findViewById(R.id.orderstatustv);
        orderList = mview.findViewById(R.id.orderList);
        statusList = mview.findViewById(R.id.statusList);
        cancelled = mview.findViewById(R.id.cancelled);
        completed = mview.findViewById(R.id.completed);
        processing = mview.findViewById(R.id.processing);
        unprocessed = mview.findViewById(R.id.unprocessed);
        allorders = mview.findViewById(R.id.allorders);
    }
}