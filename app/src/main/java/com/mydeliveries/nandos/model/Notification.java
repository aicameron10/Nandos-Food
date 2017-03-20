package com.mydeliveries.nandos.model;

/**
 * Created by Andrew on 04/09/2015.
 */

public class Notification {
    private String body;
    private String title;
    private String image;
    public Notification() {
    }
    public Notification(String body, String title, String image){
        this.image = image;
        this.title = title;
        this.body = body;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}