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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 2/6/18.
 */

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack, Serializable {

    public Videos[] videos;
    Context context;
    Activity activity;
    private NetworkCall networkCall;
    String video_id;
    private SignupResponse signupResponse;

    public ViewAllAdapter(Videos[] videos, Context context) {
        this.videos = videos;
        this.context = context;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_home1,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
          /*  Ion.with(context).load(videos[position].getThumbnail_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null)
                        holder.artistImage.setImageBitmap(result);
                    else
                        holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            });
*/
            Glide.with(context)
                    .load(videos[position].getThumbnail_url())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into(holder.artistImage);
            holder.channelName.setVisibility(View.GONE);
            holder.channelName.setText(videos[position].getVideo_title());
            String desc = Html.fromHtml(videos[position].getVideo_desc()).toString();
            holder.description.setVisibility(View.GONE);
            holder.description.setText(desc);
            holder.date.setText(getDate(Long.parseLong(videos[position].getPublished_date())));
            if (videos[position].getViews().equals("0")) {
                holder.viewsNumber.setText("no view");
            } else if (videos[position].getViews().equals("1")) {
                holder.viewsNumber.setText("1 view");
            } else {
                holder.viewsNumber.setText(videos[position].getViews() + " views");
            }
           // holder.layout.setTag(videos[position].getId());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnectingToInternet(context)) {
                        video_id = videos[position].getId();
                        networkCall = new NetworkCall(ViewAllAdapter.this, context);
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

                          /*  Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            context.sendBroadcast(intent);*/
                            ToastUtil.showDialogBox1(context,"Please Subscribe to premium");
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("type",1);
                            intent.putExtra("position", position);
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
                         /*  Intent intent = new Intent(context, HomeActivityy.class);
                           intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            context.startActivity(intent);*/
                            ToastUtil.showDialogBox1(context,"Please Subscribe to premium");
                        } else {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (context instanceof HomeActivityy) {
            if (((HomeActivityy) context)
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.container_layout_home) instanceof HomeFragment) {
                if (videos.length > 2) {
                    return 2;
                } else {
                    return videos.length;
                }
            } else {
                return videos.length;
            }
        }
        return videos.length;
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