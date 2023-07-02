package com.example.ecommerceshop.Phat.Model;

import java.io.Serializable;

public class TopProduct implements Serializable {
    String id, name, avatar;
    long price;
    int quan;

    public TopProduct() {
    }

    public TopProduct(String id, String name, String avatar, long price, int quan) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.price = price;
        this.quan = quan;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuan() {
        return quan;
    }

    public void setQuan(int quan) {
        this.quan = quan;
    }
}
