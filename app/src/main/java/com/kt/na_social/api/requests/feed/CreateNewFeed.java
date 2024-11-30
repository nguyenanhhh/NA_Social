package com.kt.na_social.api.requests.feed;

import com.kt.na_social.enums.FeedPrivacy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateNewFeed {
    private String caption;
    private List<Long> mediaIds = new ArrayList<>();
    private FeedPrivacy privacy;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<Long> getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(List<Long> mediaIds) {
        this.mediaIds = mediaIds;
    }

    public FeedPrivacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(FeedPrivacy privacy) {
        this.privacy = privacy;
    }
}
