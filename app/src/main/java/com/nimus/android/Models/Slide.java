package com.nimus.android.Models;

public class Slide {

    private String Image;
    private String URL;


    public Slide(String image, String URL) {
        this.Image = image;
        this.URL = URL;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}