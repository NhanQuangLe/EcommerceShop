package com.example.ecommerceshop.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.chat.adapters.UsersAdapter;
import com.example.ecommerceshop.chat.interfaces.UserListener;
import com.example.ecommerceshop.chat.models.UserChat;
import com.example.ecommerceshop.databinding.ActivityUsersBinding;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {
    private ActivityUsersBinding binding;
    private PreferenceManagement preferenceManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManagement = new PreferenceManagement(getApplicationContext());
        setListener();
        getUsers();

    }
    private void setListener() {
        binding.buttonBack.setOnClickListener(view -> onBackPressed());
    }

    private void getUsers()
    {
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USER)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManagement.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult()!= null)
                    {
                        List<UserChat> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult())
                        {
                            if (currentUserId.equals(queryDocumentSnapshot.getId()))
                            {
                                continue;
                            }
                            UserChat user = new UserChat();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        if (users.size()>0)
                        {
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            binding.userRecyclerView.setAdapter(usersAdapter);
                            binding.userRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            showErrorMessage();
                        }
                    }
                    else
                    {
                        showErrorMessage();
                    }
                });
    }
    private void showErrorMessage()
    {
        binding.textErrorMessage.setText(String.format("%s", "No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading)
    {
        if (isLoading)
        {
            binding.progressbar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.progressbar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(UserChat user) {
        Intent intent = new Intent(getApplicationContext(), ChatScreenActivity.class);
        intent.putExtra(Constants.KEY_USER ,user);
        startActivity(intent);
        finish();
    }
}