package com.example.ecommerceshop.Phat.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceshop.Phat.Adapter.AdapterListCategoryShop;
import com.example.ecommerceshop.Phat.Adapter.AdapterProductShop;
import com.example.ecommerceshop.Phat.Model.Product;
import com.example.ecommerceshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageShopFragment extends Fragment {
    private View mview;
    private ImageView filterbtn;
    private TextView filtertv;
    private RecyclerView listproduct , statusList;
    private AdapterProductShop adapterProductShop;
    private AdapterListCategoryShop shop;
    private List<String> listCategory, producttypeList;
    private ArrayList<Product> products;
    private FirebaseAuth firebaseAuth;
    private EditText searchView;
    String[] categorys;
    public HomePageShopFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview=inflater.inflate(R.layout.fragment_home_page_shop, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        initUI();
        LoadData();

        loadAllProduct();
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Category")
                        .setItems(categorys, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String selected = categorys[i];
                                filtertv.setText(selected);
                                if(selected.equals("All items")) loadAllProduct();
                                else{
                                    LoadFilterProduct(selected);
                                }
                            }
                        }).show();
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    adapterProductShop.getFilter().filter(charSequence);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return mview;
    }
    private void LoadData() {
        producttypeList = new ArrayList<>();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference();
        databaseReference.child("ProductType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                producttypeList.clear();
                producttypeList.add("All items");
                for (DataSnapshot ds: snapshot.getChildren()) {
                    String str = ds.getValue(String.class);
                    producttypeList.add(str);
                }
                categorys = producttypeList.toArray(new String[producttypeList.size()]);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void LoadFilterProduct(String selected) {
        products = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    String category = ""+ds.child("productCategory").getValue();
                    if(category.equals(selected)){
                        Product product = ds.getValue(Product.class);
                        products.add(0,product);
                    }
                }
                adapterProductShop = new AdapterProductShop(getContext(), products);
                listproduct.setAdapter(adapterProductShop);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadListcategory() {
        listCategory=new ArrayList<>();
        listCategory= Arrays.asList(categorys);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        shop = new AdapterListCategoryShop(getContext(), listCategory);
        statusList.setLayoutManager(linearLayoutManager);
        statusList.setAdapter(shop);
    }
    private void loadAllProduct() {
        products = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid()).child("Shop").child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Product product = ds.getValue(Product.class);
                    products.add(0,product);
                }

                adapterProductShop = new AdapterProductShop(getContext(), products);
                listproduct.setAdapter(adapterProductShop);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initUI(){
        listproduct=mview.findViewById(R.id.listproduct);
        filterbtn=mview.findViewById(R.id.imageView4);
        statusList=mview.findViewById(R.id.statusList);
        filtertv=mview.findViewById(R.id.filtertv);
        searchView=mview.findViewById(R.id.searchView);
    }
}