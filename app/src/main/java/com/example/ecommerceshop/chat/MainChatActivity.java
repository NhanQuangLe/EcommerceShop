package com.example.ecommerceshop.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.chat.adapters.RecentConversationsAdapter;
import com.example.ecommerceshop.chat.interfaces.ConversionListener;
import com.example.ecommerceshop.chat.models.ChatMessage;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.databinding.ActivityMainBinding;
import com.example.ecommerceshop.databinding.ActivityMainChatBinding;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainChatActivity extends AppCompatActivity implements ConversionListener {
    private FirebaseUser mCurrentUser;
    private ActivityMainChatBinding binding;
    private List<ChatMessage> conversationsList;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;
    private PreferenceManagement preferenceManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        init();
        loadUserDetails();
        getToken();
        listenConversations();

    }

    private void init(){
        conversationsList = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversationsList, this);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }
    private void loadUserDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users/"+mCurrentUser.getUid()+"/Customer/CustomerInfos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String image = snapshot.child("avatar").getValue(String.class);
                binding.textName.setText(name);
                Glide.with(getApplicationContext()).load(image).into(binding.imageProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getToken()
    {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token)
    {
        preferenceManagement.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USER).document(preferenceManagement.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Unable to update token !"));
    }
    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void listenConversations()
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManagement.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManagement.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
        {
            return;
        }
        if (value != null)
        {
            for (DocumentChange documentChange : value.getDocumentChanges())
            {
                if (documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (preferenceManagement.getString(Constants.KEY_USER_ID).equals(senderId))
                    {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    }
                    else
                    {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversationsList.add(chatMessage);
                }
                else if (documentChange.getType() == DocumentChange.Type.MODIFIED)
                {
                    for (int i = 0; i < conversationsList.size(); i++)
                    {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversationsList.get(i).senderId.equals(senderId) && conversationsList.get(i).receiverId.equals(receiverId))
                        {
                            conversationsList.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversationsList.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversationsList, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };
    @Override
    public void onConversionClicked(UserChat user) {
        Intent intent = new Intent(getApplicationContext(), ChatScreenActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);

    }
}