package com.sanskar.tv.module.HomeModule.Adapter;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kapil on 27/4/18.
 */

public class ParticularGuruVideoAdapter extends RecyclerView.Adapter<ParticularGuruVideoAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack {

    private List<Videos> list;
    Context context;
    private NetworkCall networkCall;
    String video_id;

    Videos[] videos = new Videos[]{};

    public ParticularGuruVideoAdapter(List<Videos> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_related_guru, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Videos particularGuruVideosBean = list.get(position);
        Ion.with(context).load(particularGuruVideosBean.getThumbnail_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                holder.artistImage.setImageBitmap(result);
            }
        });
        holder.channelName.setText(particularGuruVideosBean.getAuthor_name());
        String desc = Html.fromHtml(particularGuruVideosBean.getVideo_title()).toString();
/*
        holder.bt_play_video.setVisibility(View.VISIBLE);
*/
        holder.description.setText(desc);
        holder.date.setText(getDate(Long.parseLong(particularGuruVideosBean.getCreation_time())));
        holder.viewsNumber.setText(particularGuruVideosBean.getViews() + " views");
        holder.layout.setTag(particularGuruVideosBean.getId());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (particularGuruVideosBean.getYoutube_url()!=null && !TextUtils.isEmpty(particularGuruVideosBean.getYoutube_url())){
                    Bundle bundle = new Bundle();
                    Videos[] videos = new Videos[list.size()];


                    for (int i = 0; i < list.size(); i++) {
                        videos[i] = list.get(i);
                    }
                    bundle.putSerializable("video_data_guru", videos);
                    bundle.putSerializable("position", position);

                    Intent liveIntent = new Intent(context, MainActivity.class);
                    liveIntent.putExtras(bundle);
                    liveIntent.putExtra("from", "guru");
                    liveIntent.putExtra("ads_counts", Networkconstants.adsCount);
                    context.startActivity(liveIntent);
                }else{
                    ToastUtil.showDialogBox1(context,"Please Subscribe to our Premium");
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GURU_RELATED_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        Log.d("RECENT_VIEW Response", jsonstring.toString());
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage, bt_play_video;
        TextView channelName, description, date, viewsNumber;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            // bt_play_video = itemView.findViewById(R.id.bt_play_video);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }

}

