package com.kt.na_social.api.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedResponse {
    private int id;
    private String caption;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private String image;
    private String author;
    private int privacy;
    private String authorAvatar;
    private List<String> userReacted = new ArrayList<>();


}
