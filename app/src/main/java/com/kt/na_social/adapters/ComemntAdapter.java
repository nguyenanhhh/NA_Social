package com.kt.na_social.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kt.na_social.R;
import com.kt.na_social.comment.Comment_Item;
import com.kt.na_social.comment.Reply_Item;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_REPLY = 1;

    private final Context context;
    private final List<Object> commentList;

    // Constructor
    public CommentAdapter(Context context, List<Object> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_COMMENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_reply, parent, false);
            return new ReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = commentList.get(position);

        if (holder instanceof CommentViewHolder) {
            // Bind dữ liệu cho bình luận
            Comment_Item comment = (Comment_Item) item;
            ((CommentViewHolder) holder).onBind(comment, context);
        } else if (holder instanceof ReplyViewHolder) {
            // Bind dữ liệu cho phần phản hồi (reply)
            Reply_Item reply = (Reply_Item) item;
            ((ReplyViewHolder) holder).onBind(reply, context);
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (commentList.get(position) instanceof Comment_Item) {
            return TYPE_COMMENT;
        } else {
            return TYPE_REPLY;
        }
    }

    // ViewHolder cho Comment
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTextView, commentTextView, timeTextView;
        ImageView imvAvatar;
        ImageButton btnLike, btnReply;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.tv_user_name);
            commentTextView = itemView.findViewById(R.id.tv_comment_text);
            timeTextView = itemView.findViewById(R.id.tv_time);
            imvAvatar = itemView.findViewById(R.id.imv_avatar);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnReply = itemView.findViewById(R.id.btn_reply);
        }

        public void onBind(Comment_Item comment, Context context) {
            userNameTextView.setText(comment.getUserName());
            commentTextView.setText(comment.getContent());
            timeTextView.setText(comment.getCreatedAt());

            btnLike.setOnClickListener(v -> {
            });

            btnReply.setOnClickListener(v -> {
            });

            Glide.with(context)
                    .load(R.drawable.ic_default_avatar) 
                    .circleCrop()
                    .into(imvAvatar);
        }
    }

    // ViewHolder cho Reply
    public static class ReplyViewHolder extends RecyclerView.ViewHolder {

        TextView replyUserNameTextView, replyTextView, replyTimeTextView;
        ImageView imvReplyAvatar;

        public ReplyViewHolder(View itemView) {
            super(itemView);
            replyUserNameTextView = itemView.findViewById(R.id.tv_reply_user_name);
            replyTextView = itemView.findViewById(R.id.tv_reply_text);
            replyTimeTextView = itemView.findViewById(R.id.tv_reply_time);
            imvReplyAvatar = itemView.findViewById(R.id.imv_reply_avatar);
        }

        public void onBind(Reply_Item reply, Context context) {
            replyUserNameTextView.setText(reply.getUserName());
            replyTextView.setText(reply.getReplyText());
            replyTimeTextView.setText(reply.getCreatedAt());

            Glide.with(context)
                    .load(R.drawable.ic_default_avatar)
                    .circleCrop()
                    .into(imvReplyAvatar);
        }
    }
}
