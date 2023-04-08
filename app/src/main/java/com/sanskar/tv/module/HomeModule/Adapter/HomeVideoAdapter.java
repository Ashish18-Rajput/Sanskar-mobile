package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack, Serializable {

    List<Videos> videoList;
    Context context;
    String video_id;
    private SignupResponse signupResponse;
    private NetworkCall networkCall;

    public HomeVideoAdapter(List<Videos> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
    }

    @Override
    public HomeVideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeVideoAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_single_item_bhajan, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Glide.with(context).load(videoList.get(0).getThumbnail_url()).into(HomeFragment.playerView);
        final Videos[] videos = new Videos[videoList.size()];
        for (int i = 0; i < videoList.size(); i++) {
            videos[i] = videoList.get(i);
        }
//        HomeFragment.playerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videoList.get(0).getYoutube_url().equals("")){
//                    Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
//                    intent.putExtra("video_data", videos);
//                    intent.putExtra("position", 0);
//                    context.sendBroadcast(intent);
//                }else{
//                  /*  Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
//                    intent.putExtra("video_data", videos);
//                    intent.putExtra("position", 0);
//                    context.sendBroadcast(intent);*/
//                    Intent intent=new Intent(context,MainActivity.class);
//                    intent.putExtra("video_data", videos);
//                    intent.putExtra("position", 0);
//                    context.startActivity(intent);
//                }
//
//            }
//        });

        Ion.with(context).load(videoList.get(position).getThumbnail_url())
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    holder.artistImage.setImageBitmap(result);
                } else {
                    holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            }
        });

        //Picasso.with(context).load(guruResponses.get(position).getProfile_image()).placeholder(R.mipmap.profile_pic_default).into(holder.artistImage);
        holder.channelName.setText(videoList.get(position).getVideo_title());
        String des = Html.fromHtml(videoList.get(position).getVideo_desc()).toString();
        holder.desc.setText(des);

        holder.singleItem.setTag(videoList.get(position).getId());
        video_id = videoList.get(position).getId();
        /*holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Videos[] videos = new Videos[videoList.size()];
                for (int i = 0; i < videoList.size(); i++) {
                    videos[i] = videoList.get(i);
                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", guruResponses.get(position));
//                GuruDetailsFragment fragment = new GuruDetailsFragment();
//                fragment.setArguments(bundle);
//                ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Guru_Details").replace(R.id.container_layout_home, fragment).commit();
                if (isConnectingToInternet(context)) {
                    video_id = videoList.get(position).getId();
                    networkCall = new NetworkCall(HomeVideoAdapter.this, context);
                    networkCall.NetworkAPICall(API.RECENT_VIEW, false);
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("video_data", videos);
                bundle.putInt("position", position);
//                Fragment fragment = new WatchVideoFragment();
//                fragment.setArguments(bundle);
//                ((HomeActivityy)context)
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack("Watch_Video")
//                        .replace(R.id.container_layout_home, fragment)
//                        .commit();
                if (context instanceof HomeActivityy) {
                    if(AudioPlayerService.mediaPlayer != null){
                        try {
                            Constants.IS_PAUSEDFROMHOME = "true";
                            AudioPlayerService.mediaPlayer.stop();

                            HomeActivityy.playerlayout.setVisibility(View.GONE);
                            Constants.CALL_RESUME = "false";
                        }catch (IllegalStateException e){
                            e.printStackTrace();
                        }
                    }

                    Intent intent=new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                    intent.putExtra("video_data",videos);
                    intent.putExtra("position",position);
                    context.sendBroadcast(intent);
                    *//*Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((HomeActivityy) context).startActivityForResult(liveIntent, 121);*//*
                } else if (context instanceof LiveStreamJWActivity) {
                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
                }

            }
        });*/
    }

    @Override
    public int getItemCount() {
        if (context instanceof HomeActivityy) {
            if (((HomeActivityy) context)
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.container_layout_home) instanceof HomeFragment) {
                /*if (videoList.size() > 6) {
                    return 6;
                } else {
                  */  return videoList.size();
               /* }*/
            } else {
                return videoList.size();
            }
        }
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName;
        TextView desc;
        RelativeLayout singleItem;

        public ViewHolder(View itemView) {
            super(itemView);
//            artistImage = itemView.findViewById(R.id.artist_image_single_item_guru);
//            channelName = itemView.findViewById(R.id.channel_name_single_item_guru);
//            //description = itemView.findViewById(R.id.description_single_item_guru);
//            singleItem = itemView.findViewById(R.id.single_item_guru);

            artistImage = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            channelName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            desc = itemView.findViewById(R.id.desc_txt);
            singleItem = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_layout);
        }

    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.RECENT_VIEW)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId())
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
    public void ErrorCallBack(String jsonstring, String apitype) {}

}
