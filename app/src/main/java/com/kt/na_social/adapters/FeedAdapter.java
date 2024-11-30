package com.kt.na_social.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kt.na_social.R;
import com.kt.na_social.model.Feed;
import com.kt.na_social.ultis.ApiUtils;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {
    private List<Feed> feedList;

    private final Context context;
    private final IFeedAction iFeedAction;

    public List<Feed> getFeedList() {
        return feedList;
    }

    public FeedAdapter(List<Feed> feedList, Context context, IFeedAction iFeedAction) {
        this.feedList = feedList;
        this.context = context;
        this.iFeedAction = iFeedAction;
    }

    public void updateData(List<Feed> feedList) {
        this.feedList = feedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new FeedViewHolder(root);
    }


    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        holder.onBind(feedList.get(position), context, iFeedAction);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void addFeeds(List<Feed> body) {
        this.feedList.addAll(body);
        notifyDataSetChanged();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView txtCaption;
        TextView txtAuthor;
        TextView txtTime;
        ImageView imvThumb;
        ImageView imvAuthor;
        TextView txtLikeCount;
        TextView txtCommentCount;
        ImageButton btnLikeAction;
        ImageView btnOpenCommentAction;
        ImageButton btnShareFeed;
        ImageView btnBookMarkFeed;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAuthor = itemView.findViewById(R.id.txt_author);
            txtTime = itemView.findViewById(R.id.txt_time);
            imvThumb = itemView.findViewById(R.id.feed_imv);
            imvAuthor = itemView.findViewById(R.id.author_avt);
            txtLikeCount = itemView.findViewById(R.id.txt_like_count);
            txtCommentCount = itemView.findViewById(R.id.txt_comment_count);
            btnLikeAction = itemView.findViewById(R.id.btn_like_action);
            btnOpenCommentAction = itemView.findViewById(R.id.btn_open_comment);
            btnShareFeed = itemView.findViewById(R.id.btn_share_action);
            btnBookMarkFeed = itemView.findViewById(R.id.btn_book_mark_action);
            txtCaption = itemView.findViewById(R.id.txt_description);
        }

        public void onBind(Feed feed, Context context, IFeedAction iFeedAction) {
            Glide.with(context).load(ApiUtils.mediaUri(feed.getMedia().get(0).getId()))
                    .into(imvThumb);
            Glide.with(context).load(feed.getAuthor().getProfileAvatar()).circleCrop().into(imvAuthor);
            txtAuthor.setText(feed.getAuthor().getUsername());
            txtTime.setText(feed.getCreatedAt());
            txtLikeCount.setText(String.valueOf(feed.getUserReacted().size()));
            txtCommentCount.setText(String.valueOf(feed.getComments().size()));
            txtCaption.setText(feed.getCaption());
            btnShareFeed.setOnClickListener((e) -> iFeedAction.shareFeed(feed.getId()));
            btnBookMarkFeed.setOnClickListener((e) -> iFeedAction.bookMarkFeed(feed.getId()));
            btnOpenCommentAction.setOnClickListener((e) -> iFeedAction.openFeedComment(feed.getId()));
            btnLikeAction.setOnClickListener((e) -> iFeedAction.likeFeed(feed.getId()));
        }
    }

    public interface IFeedAction {
        void likeFeed(long feedId);

        void openFeedComment(long feedId);

        void shareFeed(long feedId);

        void bookMarkFeed(long feedId);
    }
}
