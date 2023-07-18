package com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.choose_location_gg_map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.choose_address.ChooseAddressActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoogleMapLocationActivity extends AppCompatActivity {
    SupportMapFragment google_map;
    final static int REQUEST_CODE = 100;
    public final static int CHOOSE_ADDRESS_MAP = 1000;
    public final static int CHOOSE_ADDRESS_MAP_BY_STRING_ADDRESS = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btnBackward, btn_ConfirmLocation;
    public static Address choosenAddress;
    String stringAddress;
    Double latitude, longitude;
    Boolean check = false, isChoose = false;
    com.example.ecommerceshop.nhan.Model.Address currentAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_location);
        InitUI();
        Intent intent = getIntent();
        google_map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        if(intent.getBooleanExtra("check", false) == true){
            check = true;
            stringAddress = intent.getStringExtra("stringAddress");
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);
            getLastLocation(latitude, longitude);
        }
        else{
            currentAddress = (com.example.ecommerceshop.nhan.Model.Address) intent.getSerializableExtra("location");
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();
        }
    }

    private void InitUI() {
        btnBackward = findViewById(R.id.btnBackward);
        btn_ConfirmLocation = findViewById(R.id.btn_ConfirmLocation);
        btnBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_ConfirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoogleMapLocationActivity.this, EditAddressActivity.class);
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(check){
                        intent.putExtra("isChoose", isChoose);
                        if(isChoose){
                            if(checkTrueLocation()) {
                                intent.putExtra("location", choosenAddress);
                            }
                            else{
                                Toast.makeText(GoogleMapLocationActivity.this, "Vui lòng chọn địa chỉ nằm trong vùng "
                                        + currentAddress.GetAddressString(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        else{
                            setResult(CHOOSE_ADDRESS_MAP_BY_STRING_ADDRESS, intent);
                            finish();
                        }
                    }
                    else
                        if(checkTrueLocation()) {
                            intent.putExtra("location", choosenAddress);
                            setResult(CHOOSE_ADDRESS_MAP, intent);
                            finish();
                        }
                        else{
                            Toast.makeText(GoogleMapLocationActivity.this, "Vui lòng chọn địa chỉ nằm trong vùng "
                                    + currentAddress.GetAddressString(), Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
    }
    void getLastLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LatLng end = new LatLng(currentAddress.getLatitude(), currentAddress.getLongitude());

            MarkerOptions markerEnd = new MarkerOptions().position(end).title("Đây là địa chỉ của bạn");
            google_map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    googleMap.addMarker(markerEnd);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(end, 20)) ;
                    googleMap.setMaxZoomPreference(20);
                    googleMap.setMinZoomPreference(16);
                    googleMap.isMyLocationEnabled();
                    googleMap.getUiSettings().setTiltGesturesEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(@NonNull LatLng latLng) {
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                googleMap.clear();
                                ArrayList<Address> addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Đây là địa chỉ của bạn");
                                choosenAddress = addresses.get(0);
                                isChoose = true;
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                googleMap.addMarker(markerOptions);
                            } catch (Exception e){

                                Toast.makeText(getApplicationContext(), "Không xác định được vị trí", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
        else{
            askPermission();
        }
    }
    private void getLastLocation(double latitude, double longitude){
        LatLng end = new LatLng(latitude, longitude);

        MarkerOptions markerEnd = new MarkerOptions().position(end).title("Đây là địa chỉ của bạn");
        google_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.addMarker(markerEnd);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(end, 20)) ;
                googleMap.setMaxZoomPreference(20);
                googleMap.setMinZoomPreference(16);
                googleMap.isMyLocationEnabled();
                googleMap.getUiSettings().setTiltGesturesEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            googleMap.clear();
                            ArrayList<Address> addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Đây là địa chỉ của bạn");
                            choosenAddress = addresses.get(0);
                            isChoose = true;
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.addMarker(markerOptions);
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Không xác định được vị trí", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    void askPermission(){
        ActivityCompat.requestPermissions(GoogleMapLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(GoogleMapLocationActivity.this, "Please turn on your Location App permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkTrueLocation(){
        if(check){
            Log.d("ad", choosenAddress.getAddressLine(0) + "");
            Log.d("ad", stringAddress.split(", ")[0] + "");
            Log.d("ad", stringAddress.split(", ")[1] +  "");
            Log.d("ad", stringAddress.split(", ")[2] + "");
            if(!choosenAddress.getAddressLine(0).contains(GetDistrictRemoveHeader(stringAddress.split(", ")[1])))
                return false;
            if(!choosenAddress.getAddressLine(0).contains(GetWardRemoveHeader(stringAddress.split(", ")[0])))
                return false;
        }
        else{
            if(!choosenAddress.getAddressLine(0).contains(currentAddress.GetDistrictRemoveHeader()))
                return false;
            if(!choosenAddress.getAddressLine(0).contains(currentAddress.GetWardRemoveHeader()))
                return false;
        }
        return true;
    }
    public String GetWardRemoveHeader(String k){
        String result = "";
        String[] a = k.split(" ");
        int i = 1;
        if(a[0].equals("Thị")) i = 2;
        while (i < a.length){
            result += a[i];
            if(i != a.length - 1)
                result += " ";
            i++;
        }
        return result;
    }
    public String GetDistrictRemoveHeader(String k){
        String result = "";
        String[] a = k.split(" ");
        int i = 1;
        if(a[0].equals("Thành")) i = 2;
        while (i < a.length){
            result += a[i];
            if(i != a.length - 1)
                result += " ";
            i++;
        }
        return result;
    }
}
