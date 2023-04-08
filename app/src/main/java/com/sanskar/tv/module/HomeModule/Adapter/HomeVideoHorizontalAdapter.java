package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class HomeVideoHorizontalAdapter extends RecyclerView.Adapter<HomeVideoHorizontalAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack, Serializable {


    public Videos[] videos;
    Context context;
    private NetworkCall networkCall;
    String video_id;
    private SignupResponse signupResponse;

    public HomeVideoHorizontalAdapter(Videos[] videos, Context context) {
        this.videos = videos;
        this.context = context;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
    }

    @Override
    public HomeVideoHorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeVideoHorizontalAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_single_item_bhajan,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Ion.with(context).load(videos[position].getThumbnail_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
//            @Override
//            public void onCompleted(Exception e, Bitmap result) {
//                if (result != null)
//                    holder.artistImage.setImageBitmap(result);
//                else
//                    holder.artistImage.setImageResource(R.mipmap.dummy_big);
//            }
//        });
//        holder.channelName.setText(videos[position].getVideo_title());
//        holder.description.setText(videos[position].getAuthor_name());
//        holder.date.setText(getDate(Long.parseLong(videos[position].getCreation_time())));
//        holder.viewsNumber.setText(videos[position].getViews()+" views");
//        holder.layout.setTag(videos[position].getId());
//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isConnectingToInternet(context)) {
//                    video_id = videos[position].getId();
//                    networkCall = new NetworkCall(HomeVideoHorizontalAdapter.this, context);
//                    networkCall.NetworkAPICall(API.RECENT_VIEW, false);
//                } else {
//                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("video_data", videos);
//                bundle.putInt("position", position);
////                Fragment fragment = new WatchVideoFragment();
////                fragment.setArguments(bundle);
////                ((HomeActivityy)context)
////                        .getSupportFragmentManager()
////                        .beginTransaction()
////                        .addToBackStack("Watch_Video")
////                        .replace(R.id.container_layout_home, fragment)
////                        .commit();
//                if (context instanceof HomeActivityy) {
//                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
//                    liveIntent.putExtras(bundle);
//                    ((HomeActivityy) context).startActivityForResult(liveIntent, 121);
//                } else if (context instanceof LiveStreamJWActivity) {
//                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
//                    liveIntent.putExtras(bundle);
//                    ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
//                }
//            }
//        });

            Ion.with(context)
                    .load(videos[position].getThumbnail_url())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                            holder.categoryImage.setImageBitmap(result);

                        }
                    });
        holder.categoryName.setText(videos[position].getVideo_title());
        String desc = Html.fromHtml(videos[position].getVideo_desc()).toString();
        holder.desc.setText(desc);

        holder.layout.setTag(videos[position].getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnectingToInternet(context)) {

                    video_id = videos[position].getId();
                    networkCall = new NetworkCall(HomeVideoHorizontalAdapter.this, context);
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
                    Intent intent=new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                    intent.putExtra("video_data",videos);
                    intent.putExtra("position",position);
                    context.sendBroadcast(intent);
                } else if (context instanceof LiveStreamJWActivity) {
                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
                }
            }
        });

//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                if(bhajans[position].getDirect_play().equalsIgnoreCase("1")) {
//                    bundle.putInt("position", position);
//                    bundle.putSerializable("bhajans",bhajans);
//                    Fragment fragment = new BhajanPlayFragment();
//                    fragment.setArguments(bundle);
//                    ((HomeActivityy) context)
//                            .getSupportFragmentManager()
//                            .beginTransaction()
//                            .addToBackStack("BHAJANS")
//                            .replace(R.id.container_layout_home, fragment)
//                            .commit();
//                }
//                else{
//                    bundle.putSerializable("bhajanViewAll",bhajans[position]);
//                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
//                    fragment.setArguments(bundle);
//                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (context instanceof HomeActivityy) {
            if (((HomeActivityy) context)
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.container_layout_home) instanceof HomeFragment) {
                if (videos.length > 10) {
                    return 10;
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

//        ImageView artistImage;
//        TextView channelName, description, date, viewsNumber;
//        ConstraintLayout layout;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            artistImage = itemView.findViewById(R.id.artist_image_single_item);
//            channelName = itemView.findViewById(R.id.channel_name_single_item);
//            description = itemView.findViewById(R.id.description_single_item);
//            date = itemView.findViewById(R.id.date_single_item);
//            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
//            layout = itemView.findViewById(R.id.single_item_layout_video);
//        }
//    }

        ImageView categoryImage;
        TextView categoryName;
        private TextView desc;
        RelativeLayout layout;
        ImageView mp3VideoIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            categoryName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            desc = itemView.findViewById(R.id.desc_txt);
            layout = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_layout);
            mp3VideoIcon = itemView.findViewById(R.id.mp3_video_icon);

            mp3VideoIcon.setImageResource(R.mipmap.video);
        }
    }

}
