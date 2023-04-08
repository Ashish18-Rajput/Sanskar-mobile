package com.sanskar.tv.module.HomeModule.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

/**
 * Created by appsquadz on 4/5/2018.
 */

public class LiveStreamOnTopLiveChannelAdapter extends RecyclerView.Adapter<LiveStreamOnTopLiveChannelAdapter.ViewHolder>{

   // Context context;
 //   ArrayList<Channel> channels;
    List<MenuMasterList> channels;
    Activity activity;
    Context context;
    Fragment fragment;

  /*  public OnTopHomeLiveChannelAdapter(Context context, ArrayList<Channel> channels) {
        this.context = context;
        this.channels = channels;
    }*/

    public LiveStreamOnTopLiveChannelAdapter(List<MenuMasterList> channels, Context context, Activity activity) {
        this.channels = channels;
        this.activity = activity;
        this.fragment = fragment;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_on_top_live_channel,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Ion.with(context)
                .load(channels.get(position).getImage())

                .asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    holder.channel_image.setImageBitmap(result);
                } else {
                    holder.channel_image.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            }
        });

      //  Glide.with(getActivity()).load(image).into(videoImage);


       /* Glide.with(activity)
                .load(channels.get(position).getImage())
                .override(100, 100)
                .fitCenter()
                .crop()
                .into(holder.channel_image);*/
       /* Picasso.with(activity)
                .load(channels.get(position).getImage()) //Load the image
                .error(R.mipmap.thumbnail_placeholder) //Image resource for error
                .resize(50, 50)
                .centerCrop()// Post processing - Resizing the image
                .into(holder.channel_image); // View where image is loaded.*/
        holder.home_on_top_live_channel.setTag(channels.get(position).getId());

        holder.home_on_top_live_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ((HomeFragment)fragment).Loadplayer(channels.get(position).getLikes(),channels.get(position).getIs_likes(),channels.get(position).getChannel_url(),channels.get(position).getId(),channels.get(position).getImage());
                ((LiveStreamJWActivity)activity).LoadChannel(channels.get(position).getLikes(),channels.get(position).getIs_likes(),channels.get(position).getChannelUrl(),channels.get(position).getId(),channels.get(position).getImage(), position,channels.get(position).getName());

            //    ((LiveStreamJWActivity)activity).LoadChannel(channels.get(position).getChannel_url(),channels.get(position).getId(),channels.get(position).getImage());

               /* Intent intent = new Intent(context , LiveStreamJWActivity.class);
                intent.putExtra("livevideourl",channels.get(position).getChannel_url());
                intent.putExtra("channel_id",channels.get(position).getId());
                intent.putExtra("from","livetv");
                context.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView channel_image;
        RelativeLayout home_on_top_live_channel;


    public ViewHolder(View itemView) {
        super(itemView);
        channel_image = itemView.findViewById(R.id.channel_image);
        home_on_top_live_channel = itemView.findViewById(R.id.home_on_top_live_channel);
    }
}
}


