package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Pojo.SankirtanVideos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

/**
 * Created by kapil on 1/5/18.
 */

public class SankirtanViewAllAdapter extends RecyclerView.Adapter<SankirtanViewAllAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack{

    public SankirtanVideos[] videos;
    Context context;
    private NetworkCall networkCall;
    String video_id;

    public SankirtanViewAllAdapter(SankirtanVideos[] videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public SankirtanViewAllAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SankirtanViewAllAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_home,null));
    }

    @Override
    public void onBindViewHolder(final SankirtanViewAllAdapter.ViewHolder holder, final int position) {
        SankirtanVideos video = videos[position];
        Ion.with(context).load(videos[position].getThumbnail_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                holder.artistImage.setImageBitmap(result);
            }
        });
        holder.channelName.setText(videos[position].getAuthor_name());
        holder.description.setText(videos[position].getVideo_title());
        holder.date.setText(getDate(Long.parseLong(videos[position].getCreation_time())));
        holder.viewsNumber.setText(videos[position].getViews()+" views");
        holder.layout.setTag(videos[position].getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (isConnectingToInternet(context)) {
//                    video_id = videos[position].getId();
//                    networkCall = new NetworkCall(SankirtanViewAllAdapter.this, context);
//                    networkCall.NetworkAPICall(API.RECENT_VIEW, false);
//                } else {
//                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("video_data",videos[position]);
                bundle.putInt("position",position);

//                Fragment fragment = new WatchVideoFragment();
//                fragment.setArguments(bundle);
//                ((HomeActivityy)context)
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack("Watch_Video")
//                        .replace(R.id.container_layout_home, fragment)
//                        .commit();
                Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                liveIntent.putExtras(bundle);
                context.startActivity(liveIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videos.length;
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.RECENT_VIEW)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        Log.d("RECENT_VIEW Response",jsonstring.toString());
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName, description, date, viewsNumber;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }

}

