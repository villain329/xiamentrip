package com.example.bysj3.Data;

import org.litepal.crud.LitePalSupport;

public class Article extends LitePalSupport {
    private String title;
    private String user;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String content) {
        this.user = content;
    }




}
