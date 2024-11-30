package com.kt.na_social.model;

import com.kt.na_social.api.response.media.MediaResponse;
import com.kt.na_social.enums.FeedPrivacy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Feed {
    private long id;
    private String caption;
    private List<MediaResponse> media = new ArrayList<>();
    private FeedPrivacy privacy;
    private List<Comment> comments = new ArrayList<>();
    private boolean isShowing = true;
    private List<UserReact> userReacted = new ArrayList<>();
    private User author;
    private String createdAt;
    private String updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<MediaResponse> getMedia() {
        return media;
    }

    public void setMedia(List<MediaResponse> media) {
        this.media = media;
    }

    public FeedPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FeedPrivacy privacy) {
        this.privacy = privacy;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public List<UserReact> getUserReacted() {
        return userReacted;
    }

    public void setUserReacted(List<UserReact> userReacted) {
        this.userReacted = userReacted;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
