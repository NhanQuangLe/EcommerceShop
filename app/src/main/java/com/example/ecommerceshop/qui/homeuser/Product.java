package com.example.ecommerceshop.qui.homeuser;



import com.example.ecommerceshop.qui.product_detail.Review;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Product implements Serializable {
    private String productId;
    private String productName;
    private String productBrand;
    private String productCategory;
    private String productDescription;
    private String productDiscountNote;
    private int productDiscountPrice;
    private int productPrice;
    private int productQuantity;
    private String productSite;
    private String uid;


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private String shopName;

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    private String shopProvince;
    private int psoldQuantity;
    private boolean sold;

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    private List<String> uriList;
    private List<Review> productReviews;

    public List<Review> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<Review> productReviews) {
        this.productReviews = productReviews;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getPsoldQuantity() {
        return psoldQuantity;
    }

    public void setPsoldQuantity(int psoldQuantity) {
        this.psoldQuantity = psoldQuantity;
    }


    public Product(String productId, String productName, String productBrand, String productCategory, String productDescription, String productDiscountNote, int productDiscountPrice, int productPrice, int productQuantity, String productSite, String uid, int psoldQuantity, List<String> uriList, List<Review> productReviews) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productDiscountNote = productDiscountNote;
        this.productDiscountPrice = productDiscountPrice;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSite = productSite;
        this.uid = uid;
        this.psoldQuantity = psoldQuantity;
        this.uriList = uriList;
        this.productReviews = productReviews;
    }


    public Product() {

    }
    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDiscountNote() {
        return productDiscountNote;
    }

    public void setProductDiscountNote(String productDiscountNote) {
        this.productDiscountNote = productDiscountNote;
    }

    public int getProductDiscountPrice() {
        return productDiscountPrice;
    }

    public void setProductDiscountPrice(int productDiscountPrice) {
        this.productDiscountPrice = productDiscountPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }



    public String getProductSite() {
        return productSite;
    }

    public void setProductSite(String productSite) {
        this.productSite = productSite;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public String getPriceAfterDiscountStr(){
        int res =  this.productPrice - this.productDiscountPrice;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);

        return str1;
    }
    public String getPriceStr(){
        int res =  this.productPrice;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }
    public int getPrice(){
        return  this.productPrice - this.productDiscountPrice;
    }

    public String getPercentDiscountStr(){
        NumberFormat numEN = NumberFormat.getPercentInstance();
        String percentageEN = "-" + numEN.format((1.0*this.productDiscountPrice)/this.productPrice);

        return percentageEN;
    }
}
