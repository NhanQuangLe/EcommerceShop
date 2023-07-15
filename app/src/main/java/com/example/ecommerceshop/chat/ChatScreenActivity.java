package com.example.ecommerceshop.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.chat.adapters.ChatAdapter;
import com.example.ecommerceshop.chat.models.ChatMessage;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.chat.network.ApiClient;
import com.example.ecommerceshop.chat.network.ApiService;
import com.example.ecommerceshop.databinding.ActivityChatScreenBinding;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatScreenActivity extends AppCompatActivity {
    private ActivityChatScreenBinding binding;
    private UserChat receiverUser;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PreferenceManagement preferenceManagement;
    private FirebaseFirestore database;
    private String conversationId = null;
    private Boolean isReceiverAvailability = false;
    private FirebaseUser mCurrentUser;
    private Boolean isRoleShop;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        setContentView(binding.getRoot());
        setListener();
        LoadReceiverUser();
        Init();
        listenerMessage();
    }
    private void Init()
    {
        isRoleShop = false;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        isRoleShop = preferenceManagement.getBoolean("roleShop");
        if (isRoleShop){
            currentUserId = mCurrentUser.getUid()+"Shop";
        }
        else {
           currentUserId = mCurrentUser.getUid();
        }
        if (!receiverUser.imageShop.equals("")) receiverUser.image = receiverUser.imageShop;
        if (!receiverUser.imageCus.equals("")) receiverUser.image = receiverUser.imageCus;
        if (!receiverUser.idShop.equals("")) receiverUser.id = receiverUser.idShop;
        if (!receiverUser.idCus.equals("")) receiverUser.id = receiverUser.idCus;
        if (!receiverUser.nameShop.equals("")) receiverUser.name = receiverUser.nameShop;
        if (!receiverUser.nameCus.equals("")) receiverUser.name = receiverUser.nameCus;
        binding.textName.setText(receiverUser.name);
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessageList,
                receiverUser.image,
                currentUserId
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void sendNotification(String messageBody)
    {
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body() != null)
                        {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1)
                            {
                                JSONObject error = (JSONObject) results.get(0);
                                showToast(error.getString("error"));
                                return;
                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    showToast("Notification send successfully!");
                }
                else
                {
                    showToast("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                showToast(t.getMessage());
            }
        });
    }
    private void sendMessage()
    {
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, currentUserId);
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if (conversationId != null)
        {
            updateConversion(binding.inputMessage.getText().toString());
        }
        else
        {
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, currentUserId);
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
//        if (!isReceiverAvailability)
//        {
//            try
//            {
//                JSONArray tokens = new JSONArray();
//                tokens.put(receiverUser.token);
//                JSONObject data = new JSONObject();
//                data.put(Constants.KEY_USER_ID, preferenceManagement.getString(Constants.KEY_USER_ID));
//                data.put(Constants.KEY_NAME, preferenceManagement.getString(Constants.KEY_NAME));
//                data.put(Constants.KEY_FCM_TOKEN, preferenceManagement.getString(Constants.KEY_FCM_TOKEN));
//                data.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
//
//                JSONObject body = new JSONObject();
//                body.put(Constants.REMOTE_MSG_DATA, data);
//                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);
//
//                sendNotification(body.toString());
//            }
//            catch (Exception e)
//            {
//                showToast(e.getMessage());
//            }
//        }
        binding.inputMessage.setText(null);
    }

    private void listenAvailabilityOfReceiver()
    {
        database.collection(Constants.KEY_COLLECTION_USER).document(
                receiverUser.id
        ).addSnapshotListener(ChatScreenActivity.this, (value, error) -> {
            if (error != null)
            {
                return;
            }
            if (value != null)
            {
                if (value.getLong(Constants.KEY_AVAILABILITY) != null)
                {
                    int availability = Objects.requireNonNull(value.getLong(
                            Constants.KEY_AVAILABILITY)).intValue();
                    isReceiverAvailability = availability == 1;
                }
                receiverUser.token = value.getString(Constants.KEY_FCM_TOKEN);
                if (receiverUser.image == null)
                {
                    receiverUser.image = value.getString(Constants.KEY_IMAGE);
                    chatAdapter.setReceiverProfileImage(receiverUser.image);
                    chatAdapter.notifyItemRangeChanged(0, chatMessageList.size());
                }
            }
            if (isReceiverAvailability)
            {
                binding.textAvailability.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.textAvailability.setVisibility(View.GONE);
            }
        });
    }
    private void listenerMessage()
    {
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, currentUserId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, currentUserId)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null)
        {
            return;
        }
        if (value != null)
        {
            int count = chatMessageList.size();
            for (DocumentChange documentChange : value.getDocumentChanges())
            {
                if (documentChange.getType() == DocumentChange.Type.ADDED)
                {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessageList.add(chatMessage);
                }
            }
            Collections.sort(chatMessageList,(obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0)
            {
                chatAdapter.notifyDataSetChanged();
            }
            else
            {
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(), chatMessageList.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if (conversationId == null)
        {
            checkForConversion();
        }
    };
    private Bitmap getBitmapFromEncodedString(String encodedImage)
    {
        if (encodedImage != null)
        {
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
        }
        else
        {
            return null;
        }
    }
    private void LoadReceiverUser()
    {
        receiverUser = (UserChat) getIntent().getSerializableExtra(Constants.KEY_USER);
    }
    private void setListener()
    {
        binding.buttonBack.setOnClickListener(view -> onBackPressed());
        binding.layoutSend.setOnClickListener(view -> sendMessage());
    }

    private String getReadableDateTime(Date date)
    {
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion)
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }
    private void updateConversion(String message)
    {
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversationId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );
    }
    private void checkForConversion()
    {
        if (chatMessageList.size() != 0)
        {
            checkForConversionRemotely(
                    currentUserId,
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    currentUserId
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId)
    {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0)
        {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }
}