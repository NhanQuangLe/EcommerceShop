package com.example.ecommerceshop.Model;

import android.net.Uri;

public class Photo {
    private Uri uri;
    private int typeImg;

    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Photo(Uri uri, int typeImg) {
        this.uri = uri;
        this.typeImg = typeImg;
    }

    public int getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(int typeImg) {
        this.typeImg = typeImg;
    }
}
