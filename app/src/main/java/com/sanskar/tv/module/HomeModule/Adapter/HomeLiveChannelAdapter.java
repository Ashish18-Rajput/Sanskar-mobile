package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.R;

import java.util.ArrayList;

/**
 * Created by appsquadz on 4/5/2018.
 */

public class HomeLiveChannelAdapter extends RecyclerView.Adapter<HomeLiveChannelAdapter.ViewHolder>{

    Context context;
    ArrayList<Channel> channels;

    public HomeLiveChannelAdapter(Context context, ArrayList<Channel> channels) {
        this.context = context;
        this.channels = channels;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_home_live_channel,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Ion.with(context).load(channels.get(position).getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    holder.channelImage.setImageBitmap(result);
                } else {
                    holder.channelImage.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            }
        });
        holder.channelName.setText(channels.get(position).getName());
        holder.liveChannelLayout.setTag(channels.get(position).getId());

        holder.liveChannelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context , LiveStreamJWActivity.class);
                intent.putExtra("livevideourl",channels.get(position).getChannel_url());
                intent.putExtra("channel_id",channels.get(position).getId());
                intent.putExtra("from","livetv");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

    ImageView channelImage;
    TextView channelName;
    LinearLayout liveChannelLayout;

    public ViewHolder(View itemView) {
        super(itemView);
        channelImage = itemView.findViewById(R.id.single_item_home_live_channel_civ);
        liveChannelLayout = itemView.findViewById(R.id.home_live_channel);
        channelName = itemView.findViewById(R.id.single_item_home_live_channel_tv);
    }
}
}


