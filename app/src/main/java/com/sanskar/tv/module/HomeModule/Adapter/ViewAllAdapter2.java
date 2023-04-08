package com.sanskar.tv.module.HomeModule.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.youtube.MainActivity.coolNumberFormat;

/**
 * Created by appsquadz on 2/6/18.
 */

public class ViewAllAdapter2 extends RecyclerView.Adapter<ViewAllAdapter2.ViewHolder>
        implements NetworkCall.MyNetworkCallBack, Serializable {

    public Videos[] videos;
    Context context;
    Activity activity;
    private NetworkCall networkCall;

    List<Videos> videosList;
    String video_id;
    private SignupResponse signupResponse;
    private final static int FADE_DURATION = 1000;

    public ViewAllAdapter2(List<Videos> videosList, Context context) {
        this.videosList = videosList;
        this.context = context;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_item_home2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {

            videos=new Videos[videosList.size()];
            for (int i=0;i<videosList.size();i++){
                videos[i]=videosList.get(i);
            }
            Glide.with(context)
                    .load(videosList.get(position).getThumbnail_url())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into(holder.artistImage);
            holder.channelName.setText(videosList.get(position).getVideo_title());
            String desc = Html.fromHtml(videosList.get(position).getVideo_desc()).toString();
            holder.description.setText(desc);
            holder.date.setText(getDate(Long.parseLong(videosList.get(position).getPublished_date())));
            /*if (videosList.get(position).getViews().equals("0")) {
                holder.viewsNumber.setText("no view");
            } else if (videosList.get(position).getViews().equals("1")) {
                holder.viewsNumber.setText("1 view");
            } else {
                holder.viewsNumber.setText(videosList.get(position).getViews() + " views");
            }*/
            int view1=0;
            if (videosList.get(position).getViews()!=null){
                view1= Integer.parseInt(videosList.get(position).getViews());
            }

            String vieW="";
            if (view1<=1){
                vieW=coolNumberFormat(view1)+" view";
            }
            if (view1>1){
                vieW=coolNumberFormat(view1)+" views";
            }

            holder.viewsNumber.setText(vieW);
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectingToInternet(context)) {
                        video_id = videosList.get(position).getId();
                        networkCall = new NetworkCall(ViewAllAdapter2.this, context);
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
//                   Fragment fragment = new JwPlayerFragment();
//                    fragment.setArguments(bundle);
//                    ((HomeActivityy) context)
//                            .getSupportFragmentManager()
//                            .beginTransaction()
//                            .addToBackStack("BHAJANS")
//                            .replace(R.id.container_layout_home, fragment)
//                            .commit();
                        if (videos[position].getYoutube_url().equals("")) {

                            /*Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            context.sendBroadcast(intent);*/
                            ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");

                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("type",1);
                            intent.putExtra("position", position);
                            intent.putExtra("ads_counts",++Networkconstants.adsCount);
                            intent.putExtra("menutypeid", Networkconstants.Video_id);
                            context.startActivity(intent);
                            //((HomeActivityy) context).finish();
                        }

                   /* Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((HomeActivityy) context).startActivityForResult(liveIntent, 121);*/
                    } else if (context instanceof LiveStreamJWActivity) {
                        Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                        liveIntent.putExtras(bundle);
                        liveIntent.putExtra("from", "guru");
                        ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);

                    } else if (context instanceof MainActivity) {
                        if (videos[position].getYoutube_url().equals("")) {
                          /* Intent intent = new Intent(context, HomeActivityy.class);
                           intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            context.startActivity(intent);*/
                            ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");


                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            intent.putExtra("type",1);
                            intent.putExtra("ads_counts",++Networkconstants.adsCount);
                            intent.putExtra("menutypeid", Networkconstants.Video_id);

                            context.startActivity(intent);
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
       // setFadeAnimation(holder.itemView);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public int getItemCount() {
        return videosList.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.RECENT_VIEW)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",signupResponse.getId())
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
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage,bt_play_video;
        TextView channelName, description, date, viewsNumber;
        RelativeLayout relativeLayout;
        ConstraintLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);

            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            relativeLayout=itemView.findViewById(R.id.relativeLayout);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            //layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }

}