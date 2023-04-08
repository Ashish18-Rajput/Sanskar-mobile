package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
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
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.sanskar.tv.Others.Helper.Utils.getDate;


public class HomeViewAllAdapter extends RecyclerView.Adapter<HomeViewAllAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack {

    private Context ctx;
    private String listType;
    private String videoId;
    private SignupResponse signupResponse;

    public HomeViewAllAdapter(Context ctx, String listType) {
        this.ctx = ctx;
        this.listType = listType;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
    }

    @Override
    public HomeViewAllAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeViewAllAdapter.ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.single_item_home,null));
    }

    @Override
    public void onBindViewHolder(final HomeViewAllAdapter.ViewHolder holder, final int position) {

        if (listType.equalsIgnoreCase(Constants.VIDEO_LIST_TYPE)) {
            setData(holder, ((HomeActivityy) ctx).homeVideoList.get(position).getThumbnail_url(),
                    ((HomeActivityy) ctx).homeVideoList.get(position).getVideo_title(),
                    ((HomeActivityy) ctx).homeVideoList.get(position).getVideo_desc(),
                    ((HomeActivityy) ctx).homeVideoList.get(position).getPublished_date(),
                    ((HomeActivityy) ctx).homeVideoList.get(position).getViews());
        } else if (listType.equalsIgnoreCase(Constants.GURU_LIST_TYPE)) {
            setData(holder, ((HomeActivityy) ctx).homeGuruList.get(position).getProfile_image(),
                    ((HomeActivityy) ctx).homeGuruList.get(position).getName(),
                    ((HomeActivityy) ctx).homeGuruList.get(position).getDescription(),
                    ((HomeActivityy) ctx).homeGuruList.get(position).getCreation_time(),
                    "");
        } else if (listType.equalsIgnoreCase(Constants.BHAJAN_LIST_TYPE)) {
            setData(holder, ((HomeActivityy) ctx).homeBhajanList.get(position).getImage(),
                    ((HomeActivityy) ctx).homeBhajanList.get(position).getTitle(),
                    ((HomeActivityy) ctx).homeBhajanList.get(position).getDescription(),
                    ((HomeActivityy) ctx).homeBhajanList.get(position).getCreation_time(),
                    "");
        } else if (listType.equalsIgnoreCase(Constants.NEWS_LIST_TYPE)) {
            setData(holder, ((HomeActivityy) ctx).homeNewsList.get(position).getImage(),
                    ((HomeActivityy) ctx).homeNewsList.get(position).getTitle(),
                    ((HomeActivityy) ctx).homeNewsList.get(position).getDescription(),
                    ((HomeActivityy) ctx).homeNewsList.get(position).getCreation_time(),
                    ((HomeActivityy) ctx).homeNewsList.get(position).getViews_count());
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listType.equalsIgnoreCase(Constants.VIDEO_LIST_TYPE)) {
//                    if (isConnectingToInternet(ctx)) {
//                        videoId = ((HomeActivityy) ctx).homeVideoList.get(position).getId();
//                        NetworkCall networkCall = new NetworkCall(HomeViewAllAdapter.this, ctx);
//                        networkCall.NetworkAPICall(API.RECENT_VIEW, false);
//                    } else {
//                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
//                    }

                    Videos[] videos = new Videos[((HomeActivityy) ctx).homeVideoList.size()];
                    for (int i = 0; i < ((HomeActivityy) ctx).homeVideoList.size(); i++) {
                        videos[i] = ((HomeActivityy) ctx).homeVideoList.get(i);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video_data", videos);
                    bundle.putInt("position", position);

                    if (ctx instanceof HomeActivityy) {

                        if(videos[position].getVideo_url().equals("")){
                            Intent intent=new Intent(ctx, MainActivity.class);
                            intent.putExtra("video_data", videos);
                            intent.putExtra("position", position);
                            ctx.startActivity(intent);
                        }else{
                            Intent liveIntent = new Intent(ctx, LiveStreamJWActivity.class);
                            liveIntent.putExtras(bundle);
                            ((HomeActivityy) ctx).startActivityForResult(liveIntent, 121);
                        }

                    } else if (ctx instanceof LiveStreamJWActivity) {
                        Intent liveIntent = new Intent(ctx, LiveStreamJWActivity.class);
                        liveIntent.putExtras(bundle);
                        ((LiveStreamJWActivity) ctx).startActivityForResult(liveIntent, 121);
                    }
                } else if (listType.equalsIgnoreCase(Constants.GURU_LIST_TYPE)) {
                } else if (listType.equalsIgnoreCase(Constants.BHAJAN_LIST_TYPE)) {
                } else if (listType.equalsIgnoreCase(Constants.NEWS_LIST_TYPE)) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (listType.equalsIgnoreCase(Constants.VIDEO_LIST_TYPE)) {
            count = ((HomeActivityy) ctx).homeVideoList.size();
        } else if (listType.equalsIgnoreCase(Constants.GURU_LIST_TYPE)) {
            count = ((HomeActivityy) ctx).homeGuruList.size();
        } else if (listType.equalsIgnoreCase(Constants.BHAJAN_LIST_TYPE)) {
            count = ((HomeActivityy) ctx).homeBhajanList.size();
        } else if (listType.equalsIgnoreCase(Constants.NEWS_LIST_TYPE)) {
            count = ((HomeActivityy) ctx).homeNewsList.size();
        }
        return count;
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

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.RECENT_VIEW)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("type", "2");
//                    .setMultipartParameter("media_id", video_id);
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


    private void setData(final HomeViewAllAdapter.ViewHolder holder, String imageUrl, String title, String des, String date, String numViews) {
        Ion.with(ctx).load(imageUrl).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null)
                    holder.artistImage.setImageBitmap(result);
                else
                    holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
            }
        });
        holder.channelName.setText(title);
        String desc = Html.fromHtml(des).toString();
        holder.description.setText(desc);
//        if (!date.isEmpty()) {
//            holder.date.setText(getDate(Long.parseLong(date)));
//        }
        if (numViews.equals("0")) {
            holder.viewsNumber.setText("no view");
        } else if (numViews.equals("1")) {
            holder.viewsNumber.setText("1 view");
        } else {
            holder.viewsNumber.setText(numViews + " views");
        }
    }

}

