package com.example.werk.model;

import android.graphics.Bitmap;

public class ProfilePicture {

    private String imageName;
    private Bitmap image;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
