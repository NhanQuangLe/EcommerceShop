package com.example.ecommerceshop.chat.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ecommerceshop.chat.interfaces.ConversionListener;
import com.example.ecommerceshop.chat.models.ChatMessage;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.databinding.ItemConstainerRecentConversionBinding;
import com.example.ecommerceshop.utilities.PreferenceManagement;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>{

    private final List<ChatMessage> chatMessageList;
    private final ConversionListener conversionListener;
    private Context mContext;

    public RecentConversationsAdapter(Context context,List<ChatMessage> chatMessageList, ConversionListener conversionListener) {
        this.mContext = context;
        this.chatMessageList = chatMessageList;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemConstainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ConversionViewHolder extends RecyclerView.ViewHolder{
        ItemConstainerRecentConversionBinding binding;
        ConversionViewHolder(ItemConstainerRecentConversionBinding itemConstainerRecentConversionBinding)
        {
            super(itemConstainerRecentConversionBinding.getRoot());
            binding = itemConstainerRecentConversionBinding;
        }
        void setData(ChatMessage chatMessage)
        {
            Glide.with(binding.imageProfile).load(chatMessage.conversionImage).into(binding.imageProfile);
            binding.textName.setText(chatMessage.conversionName);
            binding.textRecentMessage.setText(chatMessage.message);
            binding.getRoot().setOnClickListener(view -> {
                PreferenceManagement preferenceManagement = new PreferenceManagement(mContext);
                Boolean isRoleShop =false;
                isRoleShop  = preferenceManagement.getBoolean("roleShop");
                UserChat user = new UserChat();
               if (isRoleShop){
                   user.idCus = chatMessage.conversionId;
                   user.imageCus = chatMessage.conversionImage;
                   user.nameCus = chatMessage.conversionName;
                   user.idShop = "";
                   user.imageShop = "";
                   user.nameShop = "";
               }
               else {

                   user.idShop = chatMessage.conversionId;
                   user.imageShop = chatMessage.conversionImage;
                   user.nameShop = chatMessage.conversionName;
                   user.idCus = "";
                   user.imageCus = "";
                   user.nameCus = "";
               }
                conversionListener.onConversionClicked(user);
            });
        }
        private Bitmap getConversionImage(String encodedImage)
        {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);
        }
    }

}

