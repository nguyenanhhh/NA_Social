package com.kt.na_social.comment;

public class Comment_Item {
    private long id;
    private String userName;
    private String content;
    private String createdAt;

    // Constructor
    public Comment_Item(long id, String userName, String content, String createdAt) {
        this.id = id;
        this.userName = userName;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
