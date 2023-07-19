package com.example.ecommerceshop.Phat.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.Phat.Adapter.AdapterNotification;
import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.Notification;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.Phat.Utils.Constants;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    private View mview;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    ArrayList<Notification> notifications;
    AdapterNotification adapterNotification;

    public NotificationFragment(){};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mview = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView=mview.findViewById(R.id.requestList);
        firebaseAuth=FirebaseAuth.getInstance();
        loadData();


        return mview;
    }

    private void loadData() {
        notifications=new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Customer").child("Notifications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notifications.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Notification notification = ds.getValue(Notification.class);
                            notifications.add(notification);
                        }
                        adapterNotification = new AdapterNotification(getContext(), notifications);
                        recyclerView.setAdapter(adapterNotification);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        CustomToast.makeText(getContext(),""+ error.getMessage(),CustomToast.SHORT,CustomToast.ERROR).show();

                    }
                });
    }

}
