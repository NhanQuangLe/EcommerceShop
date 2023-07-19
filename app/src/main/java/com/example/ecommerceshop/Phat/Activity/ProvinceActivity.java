package com.example.ecommerceshop.Phat.Activity;


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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.AddressItem;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.AddressAdapter;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.IClickAddressItemListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProvinceActivity extends AppCompatActivity {
    public final String apiProvince = "https://vn-public-apis.fpo.vn/provinces/getAll?limit=-1&cols=name,name_with_type";
    ImageView backbtn;
    ArrayList<AddressItem> addressList;
    AddressAdapter addressAdapter;
    RecyclerView listReview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        addressList = new ArrayList<>();

        addressAdapter = new AddressAdapter(this, addressList, new IClickAddressItemListener() {
            @Override
            public void ClickItem(AddressItem addressItem) {
                AddressItemChoose(addressItem);
            }
        });
        backbtn = findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listReview = findViewById(R.id.listReview);
        listReview.setAdapter(addressAdapter);
        getDataProvince();
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
                                addressItem.setName(singleObject.getString("name"));
                                addressList.add(addressItem);
                                addressAdapter.notifyDataSetChanged();
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
                Toast.makeText(ProvinceActivity.this, "Lỗi khi lấy địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }
    private void AddressItemChoose(AddressItem addressItem){
        Intent i = new Intent(ProvinceActivity.this, RequestToShopActivity.class);
        Log.d("Nhanle", addressItem.getFullName());
        i.putExtra("province", addressItem.getFullName());
        setResult(1, i);
        finish();
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
        addressAdapter.notifyDataSetChanged();
        listReview.smoothScrollToPosition(0);
    }
}