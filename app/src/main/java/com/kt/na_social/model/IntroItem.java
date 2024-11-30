package com.kt.na_social.model;

public class IntroItem {
    private String ID;
    private String title;
    private String description;
    private int image;

    public IntroItem() {
    }

    public IntroItem(String ID, String title, String description, int image) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
