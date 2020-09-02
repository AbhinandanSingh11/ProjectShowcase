package com.nimus.android.Models;

public class ProjectModel {
    private String category;
    private String cost;
    private String date;
    private String desc;
    private String email;
    private String image;
    private String imageURL;
    private String title;
    private String uid;
    private String url;
    private String user;


    public ProjectModel(){

    }

    public ProjectModel(String category, String cost, String date, String desc, String email, String image, String imageURL, String title, String uid, String url, String user) {
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.desc = desc;
        this.email = email;
        this.image = image;
        this.imageURL = imageURL;
        this.title = title;
        this.uid = uid;
        this.url = url;
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
