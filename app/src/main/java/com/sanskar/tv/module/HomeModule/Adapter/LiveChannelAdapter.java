package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;

import java.util.ArrayList;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class LiveChannelAdapter extends RecyclerView.Adapter<LiveChannelAdapter.ViewHolder> {

    ArrayList<Channel> channels;
    Context context;

    public LiveChannelAdapter(ArrayList<Channel> channels, Context context) {
        this.channels = channels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_home_channel,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.channelName.setText(channels.get(position).getName());
     //   String desc = Html.fromHtml(channels.get(position).getShortDesc()).toString();
    //   holder.description.setText(desc);
        holder.date.setText(getDate(Long.parseLong(channels.get(position).getCreation_time())));
      /*  if (newsArrayList.get(position).getViews_count().equals("0")) {
            holder.viewsNumber.setText("no view");
        } else if (newsArrayList.get(position).getViews_count().equals("1")) {
            holder.viewsNumber.setText("1 view");
        } else {
            holder.viewsNumber.setText(String.format(newsArrayList.get(position).getViews_count()) + " views");
        }*/
        Ion.with(context).load(channels.get(position).getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                holder.artistImage.setImageBitmap(result);
            }
        });
//        Picasso.with(context).load(newsArrayList.get(position).getImage()).placeholder(R.mipmap.profile_pic_default).into(holder.artistImage);
        holder.layout.setTag(channels.get(position).getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.NEWS_DATA, channels.get(position));
                NewsDetailFrag fragment = new NewsDetailFrag();
                fragment.setArguments(bundle);
                ((HomeActivityy)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        //.addToBackStack(Constants.READ_NEWS)
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView artistImage;
        TextView channelName, description, date, viewsNumber;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            description.setVisibility(itemView.GONE);
            date = itemView.findViewById(R.id.date_single_item);
            date.setVisibility(itemView.GONE);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            viewsNumber.setVisibility(itemView.GONE);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }
}
