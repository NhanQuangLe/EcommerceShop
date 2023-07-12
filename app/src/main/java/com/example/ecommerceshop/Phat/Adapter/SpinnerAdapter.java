package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ecommerceshop.Phat.Model.SpinnerItem;
import com.example.ecommerceshop.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<SpinnerItem> mList;

    public SpinnerAdapter(Context mContext, List<SpinnerItem> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList!=null? mList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return mList!=null? mList.get(i) :null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner,viewGroup,false);
        TextView tvSpinnerName = rootView.findViewById(R.id.spinner_name);
        tvSpinnerName.setText(mList.get(i).getItemName());

        return rootView;
    }
}