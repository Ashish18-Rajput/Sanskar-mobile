package com.sanskar.tv.FreshEpg.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;


public class ChannelNewAdapter extends RecyclerView.Adapter<ChannelNewAdapter.ViewHolder> {

    Context context;
    List<MenuMasterList> menuMasterLists;
    int pos = -1;
    int channel_position;
    private int lastPosition = -1;

    public ChannelNewAdapter(Context context, List<MenuMasterList> menuMasterLists, int channel_position) {
        this.context = context;
        this.menuMasterLists = menuMasterLists;
        this.channel_position = channel_position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item_round, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(menuMasterLists.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.thumbnail_placeholder).error(R.mipmap.thumbnail_placeholder))
                .into(holder.profile_image);


        if (pos == position) {
            holder.container_rl.setBackgroundResource(R.drawable.rounded_circular_image_bg_1);
        } else {
            holder.container_rl.setBackgroundResource(R.drawable.rounded_circular_image_bg);
        }

        if (channel_position != -1) {
            if (channel_position == position)
                holder.container_rl.setBackgroundResource(R.drawable.rounded_circular_image_bg_1);
        }

        holder.container_rl.setOnClickListener(view -> {

            ((LiveStreamJWActivity) context).linearLayoutManager.scrollToPositionWithOffset(position, 25);
            ((LiveStreamJWActivity) context).isToday = true;

            pos = position;
            channel_position = -1;
            notifyDataSetChanged();

            ((LiveStreamJWActivity) context).LoadChannel(menuMasterLists.get(position).getLikes(), menuMasterLists.get(position).getIs_likes(), menuMasterLists.get(position).getChannelUrl(), menuMasterLists.get(position).getId(), menuMasterLists.get(position).getImage(), position,menuMasterLists.get(position).getName());
        });

        //setAnimation(holder.container_rl,position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return menuMasterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image;
        RelativeLayout container_rl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            container_rl = itemView.findViewById(R.id.container_rl);
        }
    }
}
