package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.R;

import java.util.List;

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class HomeBhajanAdapter extends RecyclerView.Adapter<HomeBhajanAdapter.ViewHolder> {

    List<Bhajan> bhajanList;
    Context context;
    Bhajan[] bhajans;

    public static  Bhajan[] bhajanforImagefromHome;
    public HomeBhajanAdapter(List<Bhajan> bhajanList, Context context) {
        this.bhajanList = bhajanList;
        this.context = context;
    }

    @Override
    public HomeBhajanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeBhajanAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_single_item_bhajan, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Ion.with(context).load(bhajanList.get(position).getArtist_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
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
        holder.channelName.setText(bhajanList.get(position).getArtist_name());
        String des = Html.fromHtml(bhajanList.get(position).getDescription()).toString();
        holder.desc.setText(des);
        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data", guruResponses.get(position));
//                GuruDetailsFragment fragment = new GuruDetailsFragment();
//                fragment.setArguments(bundle);
//                ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Guru_Details").replace(R.id.container_layout_home, fragment).commit();
            }
        });

        if (bhajanList.get(position).getDirect_play() !=null&& bhajanList.get(position).getDirect_play().equalsIgnoreCase("0")) {
            Ion
                    .with(context)
                    .load(bhajanList.get(position).getArtist_image())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                            holder.artistImage.setImageBitmap(result);

                        }
                    });
            holder.channelName.setText(bhajanList.get(position).getArtist_name());
        } else {
            Ion
                    .with(context)
                    .load(bhajanList.get(position).getImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            // Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                            holder.artistImage.setImageBitmap(result);
                        }
                    });
            holder.channelName.setText(bhajanList.get(position).getTitle());
        }
        String desc = Html.fromHtml(bhajanList.get(position).getDescription()).toString();
        holder.desc.setText(desc);
        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout1.setVisibility(View.GONE);

                if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                    HomeActivityy.playerlayout2.setVisibility(View.GONE);
             bhajans = new Bhajan[bhajanList.size()];
                for (int i = 0; i < bhajanList.size(); i++) {
                    bhajans[i] = bhajanList.get(i);
                }

                Bundle bundle = new Bundle();
                try{

                    if (downloadmediaplayer != null) {
                        if (downloadmediaplayer.isPlaying()){
                            downloadmediaplayer.pause();}}
                    else{

                    }

                    if(playlistmediaplayer!=null){
                        if (playlistmediaplayer.isPlaying()){
                            playlistmediaplayer.pause();}}
                    else{

                }

                if(bhajanList.get(position).getDirect_play().equalsIgnoreCase("1")) {
                    if (!PlayListFrag.serviceBound) {
                        if (!DownloadsFragment.serviceBound) {
                            if (!BhajanViewAllFragment.serviceBound) {
                                if (!HomeFragment.serviceBound) {
                                    if (!BhajansCategoryFragment.serviceBound) {
                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                        intentservice.putExtra("bhajanarray", bhajans);
                                        intentservice.putExtra("status", "bhajan");
                                        PreferencesHelper.getInstance().setValue("index", position);

                                        context.startService(intentservice);
                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = position;
                                        bhajanforImagefromHome=bhajans;
                                        bundle.putInt("position", position);
                                        bundle.putSerializable("bhajans", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();
                                    } else {
                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajans);
                                        bundle.putSerializable("bhajanlist", bhajans);
                                        Constants.LIST_INDEX = position;

                                        bhajanforImagefromHome=bhajans;

                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);
                                    }

                                } else {
                                    Constants.AUDIO_ACTIVE_LIST = "home";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", bhajans);
                                    bundle.putSerializable("bhajanlist", bhajans);
                                    Constants.LIST_INDEX = position;
                                    bhajanforImagefromHome=bhajans;
                                    PreferencesHelper.getInstance().setValue("index", position);
                                    intent.putExtra("position", position);
                                    context.sendBroadcast(intent);

                                }
                            } else {
                                Constants.AUDIO_ACTIVE_LIST = "home";
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajans);
                                bundle.putSerializable("bhajanlist", bhajans);
                                Constants.LIST_INDEX = position;
                                bhajanforImagefromHome=bhajans;
                                PreferencesHelper.getInstance().setValue("index", position);
                                intent.putExtra("position", position);
                                context.sendBroadcast(intent);
                            }
                        } else {
                            Constants.AUDIO_ACTIVE_LIST = "home";
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist", bhajans);
                            bundle.putSerializable("bhajanlist", bhajans);
                            Constants.LIST_INDEX = position;
                            bhajanforImagefromHome=bhajans;
                            PreferencesHelper.getInstance().setValue("index", position);
                            intent.putExtra("position", position);
                            context.sendBroadcast(intent);
                        }
                    }else{
                        Constants.AUDIO_ACTIVE_LIST = "home";
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("bhajanlist", bhajans);
                        bundle.putSerializable("bhajanlist", bhajans);
                        Constants.LIST_INDEX = position;
                        bhajanforImagefromHome=bhajans;
                        PreferencesHelper.getInstance().setValue("index", position);
                        intent.putExtra("position", position);
                        context.sendBroadcast(intent);
                    }
                }
                else{
                    bundle.putSerializable("bhajanViewAll",bhajanList.get(position));
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bhajanList.size();
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

}
