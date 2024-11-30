package com.kt.na_social.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String uid;
    private String email;
    private String username;
    private String idToken;
    private String password;

    @SerializedName("apiToken")
    private String token;

    @SerializedName("avatar")
    private String profileAvatar;

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
