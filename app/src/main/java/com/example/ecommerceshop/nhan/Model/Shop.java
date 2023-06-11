package com.example.ecommerceshop.nhan.Model;

public class Shop{
    private String ShopID, ShopAvatar, ShopName, ShopEmail, ShopAddress;
    private long NumberFollowers;
    private double Rating;
    public Shop(){}

    public Shop(String shopID, String shopAvatar, String shopName, String shopEmail, String shopAddress, long numberFollowers, double rating) {
        ShopID = shopID;
        ShopAvatar = shopAvatar;
        ShopName = shopName;
        ShopEmail = shopEmail;
        ShopAddress = shopAddress;
        NumberFollowers = numberFollowers;
        Rating = rating;
    }

    public String getShopID() {
        return ShopID;
    }

    public void setShopID(String shopID) {
        ShopID = shopID;
    }

    public String getShopAvatar() {
        return ShopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        ShopAvatar = shopAvatar;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getShopEmail() {
        return ShopEmail;
    }

    public void setShopEmail(String shopEmail) {
        ShopEmail = shopEmail;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public long getNumberFollowers() {
        return NumberFollowers;
    }

    public void setNumberFollowers(long numberFollowers) {
        NumberFollowers = numberFollowers;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }
}
