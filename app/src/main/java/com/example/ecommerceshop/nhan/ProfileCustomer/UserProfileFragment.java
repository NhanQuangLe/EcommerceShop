package com.example.ecommerceshop.nhan.ProfileCustomer;


import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecommerceshop.MainShopActivity;
import com.example.ecommerceshop.Phat.Activity.RegistrationToShopInAdminActivity;
import com.example.ecommerceshop.Phat.Activity.RequestToShopActivity;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.databinding.FragmentUserProfileBinding;
import com.example.ecommerceshop.nhan.Model.Customer;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.UserAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.edit_user_info.EditUserInfoActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products.FavouriteProductsActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops.FavouriteShopsActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.UserOrdersActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.orders.rating_products_orders.UserReviewsActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.vouchers.VoucherCustomerActivity;
import com.example.ecommerceshop.tinh.Activity.HelpActivity;
import com.example.ecommerceshop.tinh.Activity.LoginActivity;
import com.example.ecommerceshop.utilities.Constants;
import com.example.ecommerceshop.utilities.PreferenceManagement;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {
    public static final int ORDER_UNPROCESSED = 0;
    public static final int ORDER_DELIVERY = 1;
    public static final int ORDER_HISTORY = 2;
    public static final int ORDER_CANCEL = 3;
    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                }
            });
    private View mView;
    FragmentUserProfileBinding mFragmentUserProfileBinding;
    private FirebaseAuth firebaseAuth;
    private Customer currentCustomer;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    public UserProfileFragment() {
        // Required empty public constructor
    }
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mFragmentUserProfileBinding = FragmentUserProfileBinding.inflate(inflater,container,false);
        mView = mFragmentUserProfileBinding.getRoot();
        firebaseAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(getContext(), gso);
        currentCustomer = new Customer();
        LoadData();
        setEventInteract();
        return mView;
    }
    private void LoadData() {
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Shop").exists()){
                    mFragmentUserProfileBinding.tvBeginSale.setText("Cửa hàng của tôi");
                    mFragmentUserProfileBinding.tvRegisFree.setText("");
                }
              }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Customer").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentCustomer.setName(snapshot.child("CustomerInfos/name").getValue(String.class));
                        currentCustomer.setAvatar(snapshot.child("CustomerInfos/avatar").getValue(String.class));
                        currentCustomer.setEmail(snapshot.child("CustomerInfos/email").getValue(String.class));
                        currentCustomer.setPhoneNumber(snapshot.child("CustomerInfos/phoneNumber").getValue(String.class));
                        currentCustomer.setDateOfBirth(snapshot.child("CustomerInfos/dateOfBirth").getValue(String.class));
                        currentCustomer.setGender(snapshot.child("CustomerInfos/gender").getValue(String.class));

                        mFragmentUserProfileBinding.tvCustomerName.setText(currentCustomer.getName());
                        mFragmentUserProfileBinding.tvCustomerEmail.setText(currentCustomer.getEmail());
                        mFragmentUserProfileBinding.tvCustomerNumberFollowers.setText(Long.toString(snapshot.child("Followers").getChildrenCount()));
                        Picasso.get().load(currentCustomer.getAvatar()).into(mFragmentUserProfileBinding.ivCustomerAvatar);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setEventInteract(){
        mFragmentUserProfileBinding.clHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mFragmentUserProfileBinding.clHistoryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserOrdersActivity.class);
                intent.putExtra("OrderClickType", ORDER_HISTORY);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llUserAddresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserAddressActivity.class);
                mActivityLauncher.launch(intent);
            }
        });

        mFragmentUserProfileBinding.llShopFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavouriteShopsActivity.class);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llFavouriteProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavouriteProductsActivity.class);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditUserInfoActivity.class);
                intent.putExtra("currentUser", currentCustomer);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llConvertToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(firebaseAuth.getUid()).child("Shop").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            PreferenceManagement preferenceManagement = new PreferenceManagement(getContext());
                            preferenceManagement.putBoolean("roleShop", true);
                            Intent intent = new Intent(getContext(), MainShopActivity.class);
                            mActivityLauncher.launch(intent);
                            getActivity().finish();
                        }
                        else{
                            Intent intent = new Intent(getContext(), RequestToShopActivity.class);
                            intent.putExtra("id", firebaseAuth.getUid());
                            mActivityLauncher.launch(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mFragmentUserProfileBinding.llVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VoucherCustomerActivity.class);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llUserPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelpActivity.class);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                gsc.signOut();
                checkUser();
            }
        });
        mFragmentUserProfileBinding.llUnProcessed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserOrdersActivity.class);
                intent.putExtra("OrderClickType", ORDER_UNPROCESSED);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llDeliveryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserOrdersActivity.class);
                intent.putExtra("OrderClickType", ORDER_DELIVERY);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserOrdersActivity.class);
                intent.putExtra("OrderClickType", ORDER_CANCEL);
                mActivityLauncher.launch(intent);
            }
        });
        mFragmentUserProfileBinding.llRatingProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UserReviewsActivity.class);
                intent.putExtra("OrderClickType", ORDER_CANCEL);
                mActivityLauncher.launch(intent);
            }
        });
    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(getContext(), LoginActivity.class));
            (new PreferenceManagement(getContext())).putBoolean(Constants.KEY_USER_ADMIN,false);
            getActivity().finish();
        }
    }
}