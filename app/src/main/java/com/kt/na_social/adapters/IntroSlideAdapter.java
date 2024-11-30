package com.kt.na_social.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kt.na_social.R;
import com.kt.na_social.model.IntroItem;

import java.util.ArrayList;
import java.util.List;

public class IntroSlideAdapter extends RecyclerView.Adapter<IntroSlideAdapter.IntroSlideViewHolder> {
    private List<IntroItem> mItems = new ArrayList<>();
    private Context mContext;

    public void setItems(List<IntroItem> items) {
        this.mItems = items;
        this.notifyDataSetChanged();
    }

    public IntroSlideAdapter(List<IntroItem> items, Context context) {
        this.mItems = items;
        this.mContext = context;
    }

    @NonNull
    @Override
    public IntroSlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intro_slide_item, parent, false);
        return new IntroSlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroSlideViewHolder holder, int position) {
        holder.onBind(mItems.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class IntroSlideViewHolder extends RecyclerView.ViewHolder {

        ImageView mImv_cover;
        TextView mTxtTitle;
        TextView mTxtDesc;

        public IntroSlideViewHolder(@NonNull View itemView) {
            super(itemView);
            mImv_cover = itemView.findViewById(R.id.imv_intro_cover);
            mTxtDesc = itemView.findViewById(R.id.txt_intro_description);
            mTxtTitle = itemView.findViewById(R.id.txt_intro_title);
        }

        public void onBind(IntroItem introItem, Context context) {
            Glide.with(context).load(introItem.getImage()).fitCenter().into(mImv_cover);
            mTxtTitle.setText(introItem.getTitle());
            mTxtDesc.setText(introItem.getDescription());
        }
    }
}
