package com.example.ecommerceshop.nhan.ProfileCustomer.favourite_products;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceshop.R;
import com.example.ecommerceshop.nhan.Model.Cart;
import com.example.ecommerceshop.nhan.Model.Product;
import com.example.ecommerceshop.nhan.Model.Review;
import com.example.ecommerceshop.nhan.Model.Shop;
import com.example.ecommerceshop.nhan.ProfileCustomer.addresses.edit_new_address.EditAddressActivity;
import com.example.ecommerceshop.nhan.ProfileCustomer.favourite_shops.FavouriteShopsActivity;
import com.example.ecommerceshop.toast.CustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.type.DateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FavouriteProductsActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView rv_FavouriteProductsView, rv_ProductTypesView;
    ArrayList<Product> listFavouriteProduct;
    ArrayList<Product> AllFavouriteProduct;
    ArrayList<String> listProductType;
    FavouriteProductsAdapter favouriteProductsAdapter;
    ProductTypeAdapter productTypeAdapter;
    ArrayList<String> listProductTypeChoosen;
    ImageView ic_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_product);
        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();

        rv_FavouriteProductsView = findViewById(R.id.rv_FavouriteProducts);
        rv_ProductTypesView = findViewById(R.id.rv_ProductTypes);

        listFavouriteProduct = new ArrayList<>();
        AllFavouriteProduct = new ArrayList<>();
        listProductType = new ArrayList<>();
        listProductTypeChoosen = new ArrayList<>();

        favouriteProductsAdapter = new FavouriteProductsAdapter(FavouriteProductsActivity.this, listFavouriteProduct, new IClickFavouriteProductListener() {
            @Override
            public void addProductToCart(Product product) {
                AddProductToCart(product);
            }

            @Override
            public void deleteProduct(Product product) {
                DeleteFavouriteProduct(product);
            }
        });
        favouriteProductsAdapter.setFirebaseAuth(firebaseAuth);
        rv_FavouriteProductsView.setAdapter(favouriteProductsAdapter);

        GetProductFavouriteData();
        GetProductTypeData();
    }

    private void GetProductFavouriteData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseAuth.getUid())
                .child("Customer")
                .child("FavouriteProducts")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        databaseReference.child(snapshot.child("shopId").getValue(String.class) + "")
                                .child("Shop")
                                .child("Products")
                                .child(snapshot.child("productId").getValue(String.class) + "")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot s) {
                                        Product product = new Product();
                                        product.setProductAvatar(s.child("uriList").child("0").getValue(String.class));
                                        product.setProductID(s.child("productId").getValue(String.class));
                                        product.setProductName(s.child("productName").getValue(String.class));
                                        product.setProductBrand(s.child("productBrand").getValue(String.class));
                                        product.setProductSite(s.child("productSite").getValue(String.class));
                                        product.setProductCategory(s.child("productCategory").getValue(String.class));
                                        product.setProductDiscountPrice(s.child("productDiscountPrice").getValue(int.class));
                                        product.setShopID(s.child("uid").getValue(String.class));
                                        if(s.child("productReviews").getValue() == null)
                                            product.setProductRating(0);
                                        else
                                        {
                                            ArrayList<Review> reviewList = new ArrayList<>();
                                            for(DataSnapshot review : s.child("productReviews").getChildren())
                                            {
                                                Review rv = review.getValue(Review.class);
                                                reviewList.add(rv);
                                            }
                                            product.setProductRating(GetAverageRatingProduct(reviewList));
                                        }
                                        for(int i = 0; i < listFavouriteProduct.size(); i++)
                                            if(listFavouriteProduct.get(i).getProductID().equals(s.child("productId").getValue(String.class)))
                                            {
                                                listFavouriteProduct.set(i, product);
                                                AllFavouriteProduct.set(i, product);
                                                favouriteProductsAdapter.notifyDataSetChanged();
                                                return;
                                            }
                                        listFavouriteProduct.add(product);
                                        AllFavouriteProduct.add(product);
                                        favouriteProductsAdapter.notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        CustomToast.makeText(getApplicationContext(),error.getMessage()+  "",CustomToast.SHORT,CustomToast.ERROR).show();

                                    }
                                });
                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        for(int i = 0; i < listFavouriteProduct.size(); i++)
                            if(listFavouriteProduct.get(i).getProductID().equals(snapshot.child("productId").getValue(String.class)))
                            {
                                listFavouriteProduct.remove(i);
                                AllFavouriteProduct.remove(i);
                                favouriteProductsAdapter.notifyDataSetChanged();
                                return;
                            }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void GetProductTypeData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProductType");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                    listProductType.add(ds.getValue(String.class));
                productTypeAdapter = new ProductTypeAdapter(FavouriteProductsActivity.this, listProductType, new IClickProductType() {
                    @Override
                    public void IClickFilter(String productType, boolean isChoose) {
                        if(isChoose){
                            listProductTypeChoosen.remove(productType);
                            GetProductFavouriteDataBy(listProductTypeChoosen);
                        }
                        else{
                            listProductTypeChoosen.add(productType);
                            GetProductFavouriteDataBy(listProductTypeChoosen);
                        }
                    }
                });
                rv_ProductTypesView.setAdapter(productTypeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                CustomToast.makeText(getApplicationContext(),error.getMessage()+  "",CustomToast.SHORT,CustomToast.ERROR).show();

            }
        });
    }
    public void AddProductToCart(Product favouriteProduct) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn thêm sản phẩm này vào giỏ hàng không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = database.getReference().child("Users").child(firebaseAuth.getUid()).child("Customer").child("Cart");

                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean isContain = false;
                                for(DataSnapshot pd : snapshot.getChildren()){
                                    if(pd.child("productId").getValue(String.class).equals(favouriteProduct.getProductID()))
                                    {
                                        isContain = true;
                                        CustomToast.makeText(getApplicationContext(),"Sản phẩm đã có trong giỏ hàng",CustomToast.SHORT,CustomToast.ERROR).show();

                                    }
                                }
                                if(!isContain){
                                    String cartId = Long.toString(new Date().getTime());
                                    Cart cart = new Cart(cartId, favouriteProduct.getProductID(), favouriteProduct.getShopID(), 1);
                                    dbRef.child(cartId).setValue(cart, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            CustomToast.makeText(getApplicationContext(),"Đã thêm sản phẩm vào giỏ hàng",CustomToast.SHORT,CustomToast.SUCCESS).show();

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                CustomToast.makeText(getApplicationContext(),"Can not get value",CustomToast.SHORT,CustomToast.ERROR).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
        ///

    }
    public void DeleteFavouriteProduct(Product favouriteProduct) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = database.getReference();
                        dbRef.child("Users")
                                .child(firebaseAuth.getUid())
                                .child("Customer")
                                .child("FavouriteProducts")
                                .child(favouriteProduct.getProductID())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        CustomToast.makeText(getApplicationContext(),"Đã xóa sản phẩm",CustomToast.SHORT,CustomToast.ERROR).show();

                                    }
                                });
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
    private float GetAverageRatingProduct(ArrayList<Review> rvList){
        int length = rvList.size();
        if(length == 0)
            return 0;
        float rate = 0;
        for(int i = 0; i < rvList.size();i++)
        {
            rate += rvList.get(i).getRating();
        }
        return rate / rvList.size();
    }
    private void GetProductFavouriteDataBy(ArrayList<String> productTypeChoosens){
        listFavouriteProduct.clear();
        if(listProductType.size() == productTypeChoosens.size() || productTypeChoosens.size() == 0)
        {
            for(int i = 0; i < AllFavouriteProduct.size(); i++){
                listFavouriteProduct.add(AllFavouriteProduct.get(i));
            }
        }
        else
            for(int i = 0; i < AllFavouriteProduct.size(); i++){
                for(int j = 0 ; j < productTypeChoosens.size(); j++)
                {
                    if(productTypeChoosens.get(j).equals(AllFavouriteProduct.get(i).getProductCategory())){
                        listFavouriteProduct.add(AllFavouriteProduct.get(i));
                        break;
                    }
                }
            }
        favouriteProductsAdapter.notifyDataSetChanged();
    }
}
