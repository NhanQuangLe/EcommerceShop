package com.example.ecommerceshop.Phat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerceshop.Phat.Model.RequestShop;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationToShopInAdminActivity extends AppCompatActivity {

    ImageView backbtn;
    CircleImageView avatarShop;
    TextView shopName, shopDescription, shopEmail, shopPhone, shopAddress,regisDate,noti;
    AppCompatButton btnRefuse;
    Button btnAllow;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    String id;
    RequestShop requestShop= new RequestShop();
    boolean isdelete=false ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_to_shop_in_admin);
        id=getIntent().getStringExtra("id");
        initUI();

        if(!isdelete){
            LoadRequestDetail();
        }
        
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnAllow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccountChat(requestShop.getShopEmail());
            }
        });
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationToShopInAdminActivity.this);
                builder.setTitle("Refuse...").setMessage("Are you sure you want to refuse this request ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                isdelete=true;
                                deleteRequest(id);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .show();
            }
        });
    }

    private void checkExist() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()) {
                    String uid = "" + ds.getRef().getKey();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(uid).child("Shop")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String sid = snapshot.child("shopId").getValue(String.class);
                                        if(sid.equals(id)){
                                            noti.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(id).child("Shop").child("ShopInfos").setValue(requestShop).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("shopId", id);
                hashMap.put("active", true);

                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
                databaseReference1.child(id).child("Shop").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegistrationToShopInAdminActivity.this, "Allow successfully!", Toast.LENGTH_SHORT).show();
                        isdelete=true;
                        deleteRequest(id);
                    }
                });
            }
        });
    }
    private void CreateAccountChat(String email) {
        String userIdChat = FirebaseAuth.getInstance().getCurrentUser().getUid()+"Shop";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> userChat = new HashMap<>();
        userChat.put(Constants.KEY_EMAIL, email);
        db.collection(Constants.KEY_COLLECTION_USER).document(userIdChat).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                        } else {
                            db.collection(Constants.KEY_COLLECTION_USER).document(userIdChat)
                                    .set(userChat)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            PreferenceManagement preferenceManagement = new PreferenceManagement(getApplicationContext());
                                            preferenceManagement.putString(Constants.KEY_USER_ID, userIdChat);
                                            uploadData();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            showToast(e.getMessage());
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void deleteRequest(String requestId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
        reference.child(requestId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationToShopInAdminActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void LoadRequestDetail() {
        checkExist();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                requestShop=snapshot.getValue(RequestShop.class);
                if(requestShop!=null){
                    Glide.with(getApplicationContext()).load(Uri.parse(requestShop.getShopAvt())).into(avatarShop);
                    shopName.setText(requestShop.getShopName());
                    shopEmail.setText(requestShop.getShopEmail());
                    shopPhone.setText(requestShop.getShopPhone());
                    shopDescription.setText(requestShop.getShopDescription());
                    shopAddress.setText(requestShop.getShopAddress());
                    regisDate.setText(requestShop.getTimestamp());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegistrationToShopInAdminActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {
        backbtn=findViewById(R.id.backbtn);
        avatarShop=findViewById(R.id.avatarShop);
        shopName=findViewById(R.id.shopName);
        shopEmail=findViewById(R.id.shopEmail);
        shopDescription=findViewById(R.id.shopDescription);
        shopPhone=findViewById(R.id.shopPhone);
        shopAddress=findViewById(R.id.shopAddress);
        regisDate=findViewById(R.id.regisDate);
        btnRefuse=findViewById(R.id.btnRefuse);
        noti=findViewById(R.id.noti);
        btnAllow=findViewById(R.id.btnAllow);
        progressDialog=new ProgressDialog(RegistrationToShopInAdminActivity.this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth= FirebaseAuth.getInstance();
    }
}