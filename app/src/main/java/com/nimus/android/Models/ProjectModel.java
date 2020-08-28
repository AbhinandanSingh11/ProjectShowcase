package com.nimus.android.Models;

public class ProjectModel {
    private String title;
    private String price;
    private String displayImageURL;
    private String publishDate;
    private String desc;
    private String size;
    private String author;
    private String coAuthor;
    private String projectURL;
    private String instagramPostUrl;


    public ProjectModel(){

    }

    public ProjectModel(String title, String price, String displayImageURL, String publishDate, String desc, String size, String author, String coAuthor, String projectURL, String instagramPostUrl) {
        this.title = title;
        this.price = price;
        this.displayImageURL = displayImageURL;
        this.publishDate = publishDate;
        this.desc = desc;
        this.size = size;
        this.author = author;
        this.coAuthor = coAuthor;
        this.projectURL = projectURL;
        this.instagramPostUrl = instagramPostUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisplayImageURL() {
        return displayImageURL;
    }

    public void setDisplayImageURL(String displayImageURL) {
        this.displayImageURL = displayImageURL;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoAuthor() {
        return coAuthor;
    }

    public void setCoAuthor(String coAuthor) {
        this.coAuthor = coAuthor;
    }

    public String getProjectURL() {
        return projectURL;
    }

    public void setProjectURL(String projectURL) {
        this.projectURL = projectURL;
    }

    public String getInstagramPostUrl() {
        return instagramPostUrl;
    }

    public void setInstagramPostUrl(String instagramPostUrl) {
        this.instagramPostUrl = instagramPostUrl;
    }
}
