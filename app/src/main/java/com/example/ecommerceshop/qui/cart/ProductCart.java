package com.example.ecommerceshop.qui.cart;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductCart implements Parcelable {

    public String productId;

    private String productCardId;
    private String productName;
    private int productQuantity;
    private int productPrice;
    private int productDiscountPrice;
    private String uri;
    private String shopId;
    private String shopName;
    private String brand;
    private String productCategory;

    public int getProductAvailable() {
        return productAvailable;
    }

    public void setProductAvailable(int productAvailable) {
        this.productAvailable = productAvailable;
    }

    private int productAvailable;


    public boolean isCheckAll() {
        return isCheckAll;
    }

    public void setCheckAll(boolean checkAll) {
        isCheckAll = checkAll;
    }

    private boolean isCheckAll;

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    private boolean isChecked = false;


    public ProductCart() {
    }

    public ProductCart( String productCardId,String productId, int productAvailable,  String productName, int productQuantity, int productPrice, int productDiscountPrice, String uri, String shopId, String shopName, String brand, String productCategory) {
        this.productId = productId;
        this.productCardId = productCardId;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.uri = uri;
        this.shopId = shopId;
        this.shopName = shopName;
        this.brand = brand;
        this.productCategory  = productCategory;
        this.isCheckAll = false;
        this.productAvailable = productAvailable;
    }

    protected ProductCart(Parcel in) {
        productId = in.readString();
        productCardId = in.readString();
        productName = in.readString();
        productQuantity = in.readInt();
        productPrice = in.readInt();
        productDiscountPrice = in.readInt();
        uri = in.readString();
        shopId = in.readString();
        shopName = in.readString();
        brand = in.readString();
        productCategory = in.readString();
        isChecked = in.readByte() != 0;
        productAvailable = in.readInt();
    }


    public static final Creator<ProductCart> CREATOR = new Creator<ProductCart>() {
        @Override
        public ProductCart createFromParcel(Parcel in) {
            return new ProductCart(in);
        }

        @Override
        public ProductCart[] newArray(int size) {
            return new ProductCart[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductCardId() {
        return productCardId;
    }

    public void setProductCardId(String productCardId) {
        this.productCardId = productCardId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(int productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getProductPriceStr(){
        int res =  this.productPrice;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }
    public String getProductAfterDiscountStr(){
        int res =  this.productPrice - this.productDiscountPrice;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(productCardId);
        parcel.writeString(productName);
        parcel.writeInt(productQuantity);
        parcel.writeInt(productPrice);
        parcel.writeInt(productDiscountPrice);
        parcel.writeString(uri);
        parcel.writeString(shopId);
        parcel.writeString(shopName);
        parcel.writeString(brand);
        parcel.writeString(productCategory);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
        parcel.writeInt(productAvailable);
    }
}
