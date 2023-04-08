package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.PlayerView;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Fragment.LiveFragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LiveTvAdapter extends RecyclerView.Adapter<LiveTvAdapter.ViewHolder> {

    Context context;
    public List<MenuMasterList> menuMasterLists;
    public PlayerView playerView;
    Fragment fragment;
    private final static int FADE_DURATION = 1000;


    public LiveTvAdapter(Context context, List<MenuMasterList> menuMasterLists, Fragment fragment) {
        this.context = context;
        this.fragment=fragment;
        this.menuMasterLists = menuMasterLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_item_live,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Glide.with(context)
                .load(menuMasterLists.get(i).getImage())
                .into(holder.artist_image_single_item);

        holder.date_single_item.setText(new SimpleDateFormat("dd MMM, yyyy").format(new Date(
                Long.parseLong(menuMasterLists.get(i).getCreationTime()) *1000)));

        holder.channel_name_single_item.setText(menuMasterLists.get(i).getName());
        holder.views_number_single_item.setText(menuMasterLists.get(i).getLikes()+" likes");

        holder.single_item_layout_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUrl = menuMasterLists.get(i).getChannelUrl()+"?start="+previousDate()+"&end=";
                Uri uri=Uri.parse(newUrl);
                if (menuMasterLists.get(i).getChannelUrl()!=null && !TextUtils.isEmpty(menuMasterLists.get(i).getChannelUrl()))
                LiveFragment.setVideos(uri,menuMasterLists.get(i).getId(),menuMasterLists.get(i));
                else ToastUtil.showDialogBox1(context,"Please Subscribe to our Premium");
              //  LiveFragment.playVideo(uri);
              //  Log.d("home1",homeChannelList.get(i).getLikes()+homeChannelList.get(i).getIs_likes()+homeChannelList.get(i).getChannel_url()+homeChannelList.get(i).getId()+homeChannelList.get(i).getImage());
              //  ((HomeFragment)fragment).Loadplayer(homeChannelList.get(i).getLikes(),homeChannelList.get(i).getIs_likes(),homeChannelList.get(i).getChannel_url(),homeChannelList.get(i).getId(),homeChannelList.get(i).getImage());

            }
        });

        setFadeAnimation(holder.itemView);

    }
    public  String previousDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY,-12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.ENGLISH);
        Log.e("shantanu", "old date: "+sdf.format(c.getTime()));

        String oldDate = sdf.format(c.getTime());
        int length = oldDate.length();
        String newDate = oldDate.substring(0, length - 2) + ':' + oldDate.substring(length - 2);
        Log.e("shantanu", "new date: "+newDate);
        return newDate;
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return menuMasterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView artist_image_single_item;
        TextView channel_name_single_item,date_single_item,views_number_single_item;
        ConstraintLayout single_item_layout_video;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artist_image_single_item=itemView.findViewById(R.id.artist_image_single_item);
            channel_name_single_item=itemView.findViewById(R.id.channel_name_single_item);
            date_single_item=itemView.findViewById(R.id.date_single_item);
            views_number_single_item=itemView.findViewById(R.id.views_number_single_item);
            single_item_layout_video=itemView.findViewById(R.id.single_item_layout_video);
        }
    }
}
