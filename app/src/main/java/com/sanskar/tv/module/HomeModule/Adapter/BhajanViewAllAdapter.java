package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.bhajanCategory.BhajanCat;
import com.sanskar.tv.module.HomeModule.Pojo.bhajanCategory.BhajanCatData;

import java.util.List;

/**
 * Created by appsquadz on 2/22/2018.
 */

public class BhajanViewAllAdapter extends RecyclerView.Adapter<BhajanViewAllAdapter.ViewHolder> {

    Bhajan[] bhajans;
    List<BhajanResponse> bhajanResponseArrayList;
    Context context;
    private boolean isArtistBhajan;
    BhajanViewAllFragment bhajanViewAllFragment;
    BhajanCat[] BhajanCatList;
    List<BhajanCatData> bhajanCatDataList;
    int Type;

    public BhajanViewAllAdapter(Bhajan[] bhajans, Context context, boolean isArtistBhajan) {
        this.bhajans = bhajans;
        this.context = context;
        this.isArtistBhajan = isArtistBhajan;
    }

    public BhajanViewAllAdapter(BhajanCat[] BhajanCatList,Context context, boolean isArtistBhajan) {
        this.context = context;
        this.BhajanCatList = BhajanCatList;
        this.isArtistBhajan = isArtistBhajan;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_all_single_item_bhajan1,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*if(Type==1){*/
        if (bhajans[position].getImage() != null && !TextUtils.isEmpty(bhajans[position].getImage())) {
            Ion.with(context).load(bhajans[position].getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    holder.artistImage.setImageBitmap(result);
                }
            });
        } else {
            holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
        }
        if (isArtistBhajan) {
            holder.channelName.setText(bhajans[position].getTitle().trim());
        } else {
            holder.channelName.setText(bhajans[position].getTitle().trim());
        }
        String desc = Html.fromHtml(bhajans[position].getDescription().trim()).toString();
        holder.description.setText(desc);

        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (bhajans[position].getDirect_play().equalsIgnoreCase("1")) {
                    /*bundle.putInt("position", position);
                    bundle.putSerializable("bhajans", bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();*/
                    if (!PlayListFrag.serviceBound) {
                        if (!DownloadsFragment.serviceBound) {
                            if (!BhajanViewAllFragment.serviceBound) {
                                if (!HomeFragment.serviceBound) {
                                    if (!BhajansCategoryFragment.serviceBound) {
                                        Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                        intentservice.putExtra("bhajanarray", bhajans);
                                        intentservice.putExtra("status", "bhajan");
                                        PreferencesHelper.getInstance().setValue("index", position);

                                        context.startService(intentservice);
                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                        Constants.LIST_INDEX = position;

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
                                        Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", bhajans);
                                        bundle.putSerializable("bhajanlist", bhajans);
                                        Constants.LIST_INDEX = position;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);
                                    }

                                } else {
                                    //Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", bhajans);
                                    bundle.putSerializable("bhajanlist", bhajans);
                                    Constants.LIST_INDEX = position;
                                    PreferencesHelper.getInstance().setValue("index", position);
                                    intent.putExtra("position", position);
                                    context.sendBroadcast(intent);

                                }
                            } else {
                                //Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", bhajans);
                                bundle.putSerializable("bhajanlist", bhajans);
                                Constants.LIST_INDEX = position;
                                PreferencesHelper.getInstance().setValue("index", position);
                                intent.putExtra("position", position);
                                context.sendBroadcast(intent);
                            }
                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist", bhajans);
                            bundle.putSerializable("bhajanlist", bhajans);
                            Constants.LIST_INDEX = position;
                            PreferencesHelper.getInstance().setValue("index", position);
                            intent.putExtra("position", position);
                            context.sendBroadcast(intent);
                        }
                    } else {
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("bhajanlist", bhajans);
                        bundle.putSerializable("bhajanlist", bhajans);
                        Constants.LIST_INDEX = position;
                        PreferencesHelper.getInstance().setValue("index", position);
                        intent.putExtra("position", position);
                        context.sendBroadcast(intent);
                    }


                } else {
                    bundle.putSerializable("bhajanViewAll", bhajans[position]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home, fragment).commit();
                }
            }

        });
    }
        /*else{


            if (BhajanCatList[position].getImage() != null && !TextUtils.isEmpty(BhajanCatList[position].getImage())) {
                Ion.with(context).load(BhajanCatList[position].getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        holder.artistImage.setImageBitmap(result);
                    }
                });
            } else {
                holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
            }
            if (isArtistBhajan) {
                holder.channelName.setText(BhajanCatList[position].getTitle().trim());
            } else {
                holder.channelName.setText(BhajanCatList[position].getTitle().trim());
            }
            String desc = Html.fromHtml(bhajans[position].getDescription().trim()).toString();
            holder.description.setText(desc);

            holder.singleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    if (BhajanCatList[position].getDirectPlay().equalsIgnoreCase("1")) {
                    *//*bundle.putInt("position", position);
                    bundle.putSerializable("bhajans", bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();*//*
                        if (!PlayListFrag.serviceBound) {
                            if (!DownloadsFragment.serviceBound) {
                                if (!BhajanViewAllFragment.serviceBound) {
                                    if (!HomeFragment.serviceBound) {
                                        if (!BhajansCategoryFragment.serviceBound) {
                                            Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                            Intent intentservice = new Intent(context, AudioPlayerService.class);
                                            intentservice.putExtra("bhajanarray", BhajanCatList);
                                            intentservice.putExtra("status", "bhajan");
                                            PreferencesHelper.getInstance().setValue("index", position);

                                            context.startService(intentservice);
                                            context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                            Constants.IS_PLAYING_ON_CATEGORY = "false";
                                            // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                            Constants.LIST_INDEX = position;

                                            bundle.putInt("position", position);
                                            bundle.putSerializable("bhajans", BhajanCatList);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();
                                        } else {
                                            Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist", BhajanCatList);
                                            bundle.putSerializable("bhajanlist", BhajanCatList);
                                            Constants.LIST_INDEX = position;
                                            PreferencesHelper.getInstance().setValue("index", position);
                                            intent.putExtra("position", position);
                                            context.sendBroadcast(intent);
                                        }

                                    } else {
                                        //Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist", BhajanCatList);
                                        bundle.putSerializable("bhajanlist", BhajanCatList);
                                        Constants.LIST_INDEX = position;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);

                                    }
                                } else {
                                    //Constants.AUDIO_ACTIVE_LIST = "bhajan";
                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                    intent.putExtra("bhajanlist", BhajanCatList);
                                    bundle.putSerializable("bhajanlist", BhajanCatList);
                                    Constants.LIST_INDEX = position;
                                    PreferencesHelper.getInstance().setValue("index", position);
                                    intent.putExtra("position", position);
                                    context.sendBroadcast(intent);
                                }
                            } else {
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist", BhajanCatList);
                                bundle.putSerializable("bhajanlist", BhajanCatList);
                                Constants.LIST_INDEX = position;
                                PreferencesHelper.getInstance().setValue("index", position);
                                intent.putExtra("position", position);
                                context.sendBroadcast(intent);
                            }
                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist", BhajanCatList);
                            bundle.putSerializable("bhajanlist", BhajanCatList);
                            Constants.LIST_INDEX = position;
                            PreferencesHelper.getInstance().setValue("index", position);
                            intent.putExtra("position", position);
                            context.sendBroadcast(intent);
                        }


                    } else {
                        bundle.putSerializable("bhajanViewAll", BhajanCatList[position]);
                        BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home, fragment).commit();
                    }
                }

            });








        }*/



    @Override
    public int getItemCount() {

        return bhajans.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView dotImg;
        ImageView artistImage;
        TextView channelName, description;
        CardView singleItem;


        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_view_all_bhajan);
            channelName = itemView.findViewById(R.id.channel_name_view_all_bhajan);
            description = itemView.findViewById(R.id.description_view_all_bhajan);
            singleItem = itemView.findViewById(R.id.single_item_view_all_bhajan);
            dotImg = itemView.findViewById(R.id.imageView2);

            dotImg.setVisibility(View.GONE);
        }
    }
}
