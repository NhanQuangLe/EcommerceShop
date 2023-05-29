package com.example.ecommerceshop.Phat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;

import java.util.List;

public class AdapterListCategoryShop extends RecyclerView.Adapter<AdapterListCategoryShop.CategoryHolder> {
    Context context;
    List<String> list;

    public AdapterListCategoryShop(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_list_category, parent, false);
        return new AdapterListCategoryShop.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.title.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder{
        TextView title;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.titlecate);
        }
    }
}
