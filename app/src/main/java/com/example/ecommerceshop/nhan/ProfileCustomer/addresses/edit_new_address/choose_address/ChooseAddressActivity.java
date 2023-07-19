package com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.AddressItem;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.choose_location_gg_map.GoogleMapLocationActivity;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChooseAddressActivity extends AppCompatActivity {
    public final String apiProvince = "https://vn-public-apis.fpo.vn/provinces/getAll?limit=-1&cols=name,name_with_type";
    public final String apiDistrict = "https://vn-public-apis.fpo.vn/districts/getByProvince?limit=-1&provinceCode=";
    public final String apiWard = "https://vn-public-apis.fpo.vn/wards/getByDistrict?&limit=-1&districtCode=";
    public static final int SUCCESS_CREATE_ADDRESS = 1;
    public static final int SUCCESS_CREATE_ADDRESS_BY_CURRENT_LOCATION = 2;
    public int Status = 0;
    // 0: Chọn tỉnh
    // 1: Chọn huyện
    // 2: Chọn xã
    public String mainAddress;
    RecyclerView rv_ListAddress;
    TextView btn_ResetAddress, tv_TypeChoose, tv_CurrentProvince, tv_CurrentDistrict;
    LinearLayout ll_CurrentLocation, ll_ChooseDistrictRoundbox, ll_ChooseWardRoundbox, btn_open_google_map;
    ArrayList<AddressItem> addressList;
    ArrayList<AddressItem> addressSearchResult;
    AddressAdapter addressAdapter;
    EditText et_SearchBox;
    ImageView ic_back;
    private FusedLocationProviderClient fusedLocationClient;

    private ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (result.getResultCode() == GoogleMapLocationActivity.CHOOSE_ADDRESS_MAP) {
                        Intent it = new Intent(ChooseAddressActivity.this, EditAddressActivity.class);
                        Address ad = (Address) it.getSerializableExtra("choosenAddress");
                        mainAddress = ad.getAddressLine(0);
                        it.putExtra("Address", mainAddress);
                        setResult(SUCCESS_CREATE_ADDRESS, intent);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_shipping_address);

        InitUI();

        addressList = new ArrayList<>();
        mainAddress = "";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getDataProvince();

        addressAdapter = new AddressAdapter(this, addressList, new IClickAddressItemListener() {
            @Override
            public void ClickItem(AddressItem addressItem) {
                AddressItemChoose(addressItem, Status);
            }
        });
        rv_ListAddress.setAdapter(addressAdapter);


    }

    void InitUI() {
        ic_back = findViewById(R.id.ic_back);
        rv_ListAddress = findViewById(R.id.rv_ListAddress);
        btn_ResetAddress = findViewById(R.id.btn_ResetAddress);
        tv_TypeChoose = findViewById(R.id.tv_TypeChoose);
        ll_CurrentLocation = findViewById(R.id.ll_CurrentLocation);
        tv_CurrentProvince = findViewById(R.id.tv_CurrentProvince);
        tv_CurrentDistrict = findViewById(R.id.tv_CurrentDistrict);
        ll_ChooseDistrictRoundbox = findViewById(R.id.ll_ChooseDistrictRoundBox);
        ll_ChooseWardRoundbox = findViewById(R.id.ll_ChooseWardRoundBox);
        et_SearchBox = findViewById(R.id.et_SearchBox);
        btn_open_google_map = findViewById(R.id.btn_open_google_map);
        btn_open_google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        et_SearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_ResetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataProvince();
                mainAddress = "";
                Status = 0;
                tv_TypeChoose.setText("Tỉnh/ Thành phố");
                ll_CurrentLocation.setVisibility(View.GONE);
                ll_ChooseDistrictRoundbox.setVisibility(View.VISIBLE);
                ll_ChooseWardRoundbox.setVisibility(View.GONE);
            }
        });
    }
    private void getDataProvince(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiProvince,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("api", response.toString());
                        try{
                            JSONObject object = new JSONObject(response);

                            JSONObject object1 = object.getJSONObject("data");
                            JSONArray array = object1.getJSONArray("data");
                            for(int i = 0; i < array.length(); i++){
                                JSONObject singleObject = array.getJSONObject(i);
                                AddressItem addressItem = new AddressItem();
                                addressItem.setCode(singleObject.getString("code"));
                                addressItem.setFullName(singleObject.getString("name"));
                                addressList.add(addressItem);
                            }
                            SortAndSetHeader();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.d("api", "on respond" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomToast.makeText(getApplicationContext(),"Lỗi khi lấy địa chỉ",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
        queue.add(stringRequest);

    }
    private void getDataBy(String api){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("api", response.toString());
                        try{
                            JSONObject object = new JSONObject(response);

                            JSONObject object1 = object.getJSONObject("data");
                            JSONArray array = object1.getJSONArray("data");
                            addressList.clear();
                            for(int i = 0; i < array.length(); i++){
                                JSONObject singleObject = array.getJSONObject(i);
                                AddressItem addressItem = new AddressItem();
                                addressItem.setCode(singleObject.getString("code"));
                                addressItem.setFullName(singleObject.getString("name_with_type"));
                                addressList.add(addressItem);
                            }
                            SortAndSetHeader();
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.d("api", "on respond" + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomToast.makeText(getApplicationContext(),"Lỗi khi lấy địa chỉ",CustomToast.SHORT,CustomToast.ERROR).show();
            }
        });
        queue.add(stringRequest);

    }
    private void AddressItemChoose(AddressItem addressItem, int status){
        switch (status)
        {
            case 0:
                Status = 1;
                ll_CurrentLocation.setVisibility(View.VISIBLE);
                String apiDisTemp = apiDistrict + addressItem.getCode();
                getDataBy(apiDisTemp);
                mainAddress += addressItem.getFullName();
                tv_CurrentProvince.setText(addressItem.getFullName());
                tv_TypeChoose.setText("Quận/ Huyện");
                et_SearchBox.setText("");
                break;
            case 1:
                Status = 2;
                String apiWardTemp = apiWard + addressItem.getCode();
                getDataBy(apiWardTemp);
                mainAddress = addressItem.getFullName() + ", " + mainAddress;
                tv_CurrentDistrict.setText(addressItem.getFullName());
                tv_TypeChoose.setText("Phường/ Xã");
                ll_ChooseDistrictRoundbox.setVisibility(View.GONE);
                ll_ChooseWardRoundbox.setVisibility(View.VISIBLE);
                et_SearchBox.setText("");
                break;
            case 2:
                Status = 0;
                mainAddress = addressItem.getFullName() + ", " + mainAddress;
                Intent intent = new Intent(ChooseAddressActivity.this, EditAddressActivity.class);
                intent.putExtra("Address", mainAddress);
                setResult(SUCCESS_CREATE_ADDRESS, intent);
                mainAddress = "";
                finish();
                break;
        }
    }
    void SortAndSetHeader(){
        addressList.sort((o1, o2) -> o1.getFullName().compareTo(o2.getFullName()));
        String previousHead = "";
        for(int i = 0; i < addressList.size(); i++)
        {
            String head = addressList.get(i).getFullName().substring(0,1).toUpperCase();
            if(!previousHead.equals(head))
            {
                addressList.get(i).setHeadName(head);
                previousHead = head;
            }
        }
        ListFull = new ArrayList<>(addressList);
        addressAdapter.notifyDataSetChanged();
        rv_ListAddress.smoothScrollToPosition(0);
    }
    ArrayList<AddressItem> ListFull;
    void getLastLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Geocoder geocoder = new Geocoder(ChooseAddressActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Intent intent = new Intent(ChooseAddressActivity.this, EditAddressActivity.class);
                                    intent.putExtra("choosenAddress", addresses.get(0));
                                    setResult(SUCCESS_CREATE_ADDRESS_BY_CURRENT_LOCATION, intent);
                                    finish();

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else{
                                CustomToast.makeText(getApplicationContext(),"Lỗi khi tìm địa chỉ của bạn, hãy thử lại sau",CustomToast.SHORT,CustomToast.ERROR).show();

                            }
                        }
                    });
        }
        else{
            askPermission();
        }
    }
    void askPermission(){
        ActivityCompat.requestPermissions(ChooseAddressActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                CustomToast.makeText(getApplicationContext(),"Please turn on your Location App permissions",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        }
    }

}
