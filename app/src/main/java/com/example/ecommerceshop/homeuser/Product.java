package com.example.ecommerceshop.homeuser;



import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Product {
    private String productId;
    private String productName;
    private String productBrand;
    private String productCategory;
    private String productDescription;
    private String productDiscountNote;
    private int productDiscountPrice;
    private int productPrice;
    private int getProductQuantity;
    private String productSite;
    private String udi;


    private List<String> uriList;

    public Product(String productId, String productName, String productBrand, String productCategory, String productDescription, String productDiscountNote, int productDiscountPrice, int productPrice, int getProductQuantity, String productSite, String udi, List<String> urlList) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productDiscountNote = productDiscountNote;
        this.productDiscountPrice = productDiscountPrice;
        this.productPrice = productPrice;
        this.getProductQuantity = getProductQuantity;
        this.productSite = productSite;
        this.udi = udi;
        this.uriList = urlList;
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

    public int getGetProductQuantity() {
        return getProductQuantity;
    }

    public void setGetProductQuantity(int getProductQuantity) {
        this.getProductQuantity = getProductQuantity;
    }

    public String getProductSite() {
        return productSite;
    }

    public void setProductSite(String productSite) {
        this.productSite = productSite;
    }

    public String getUdi() {
        return udi;
    }

    public void setUdi(String udi) {
        this.udi = udi;
    }

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    public String getDiscountPrice(){
        int res =  (this.productPrice - this.productDiscountPrice);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }
    public String getPrice(){
        int res =  this.productPrice;
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        String str1 = currencyVN.format(res);
        return str1;
    }

    public String getPercentDiscount(){
        NumberFormat numEN = NumberFormat.getPercentInstance();
        String percentageEN = "-" + numEN.format(this.productDiscountPrice/this.productPrice);
        return percentageEN;
    }
}
