package com.kt.na_social.api.requests;

public class UploadImageRequest {
    private String uid;
    private boolean isCoverImage;

    public UploadImageRequest() {
    }

    public UploadImageRequest(String uid, boolean isCoverImage) {
        this.uid = uid;
        this.isCoverImage = isCoverImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isCoverImage() {
        return isCoverImage;
    }

    public void setCoverImage(boolean coverImage) {
        isCoverImage = coverImage;
    }
}
