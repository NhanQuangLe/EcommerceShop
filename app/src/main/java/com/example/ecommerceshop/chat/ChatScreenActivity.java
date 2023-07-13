package com.example.ecommerceshop.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.chat.adapters.ChatAdapter;
import com.example.ecommerceshop.chat.models.ChatMessage;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.databinding.ActivityChatScreenBinding;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ChatScreenActivity extends AppCompatActivity {
    private ActivityChatScreenBinding binding;
    private UserChat receiverUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManagement preferenceManagement;
    private FirebaseFirestore database;
    private String conversationId = null;
    private Boolean isReceiverAvailability = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
    }
}