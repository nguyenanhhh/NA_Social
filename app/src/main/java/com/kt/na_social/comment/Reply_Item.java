package com.kt.na_social.comment;

public class Reply_Item {
        private long id;
        private String userName;
        private String replyText;
        private String createdAt;

        // Constructor
        public Reply_Item(long id, String userName, String replyText, String createdAt) {
            this.id = id;
            this.userName = userName;
            this.replyText = replyText;
            this.createdAt = createdAt;
        }

        // Getters
        public long getId() {
            return id;
        }

        public String getUserName() {
            return userName;
        }

        public String getReplyText() {
            return replyText;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}