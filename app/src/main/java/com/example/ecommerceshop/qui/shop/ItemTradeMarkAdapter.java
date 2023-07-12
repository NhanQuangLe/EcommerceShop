package com.example.ecommerceshop.qui.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.AdapterItemTrademarkBinding;

import java.util.List;

public class ItemTradeMarkAdapter extends ArrayAdapter<String> {
    private List<String> mList;
    private int resource;
    public void setData(List<String> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public ItemTradeMarkAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.resource = resource;
        mList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            v = layoutInflater.inflate(resource,null);
        }
        String tradeMark = mList.get(position);
        if (tradeMark!=null){
            TextView tv = (TextView) v.findViewById(R.id.trademark);
            tv.setText(tradeMark);
        }
        return  v;
    }
}
