package com.example.ecommerceshop.chat.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ecommerceshop.chat.interfaces.ConversionListener;
import com.example.ecommerceshop.chat.models.ChatMessage;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.databinding.ItemConstainerRecentConversionBinding;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder>{

    private final List<ChatMessage> chatMessageList;
    private final ConversionListener conversionListener;


    public RecentConversationsAdapter(List<ChatMessage> chatMessageList, ConversionListener conversionListener) {
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
                UserChat user = new UserChat();
                user.id = chatMessage.conversionId;
                user.image = chatMessage.conversionImage;
                user.name = chatMessage.conversionName;
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

