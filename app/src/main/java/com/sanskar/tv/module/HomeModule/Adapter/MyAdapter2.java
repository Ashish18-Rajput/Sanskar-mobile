package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;

import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.GuruDetailsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment1;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFragRecent;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/*import io.requery.query.HavingAndOr;*/

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    Context context;
    List<MenuMasterList> categories1List;
    String MenuTypeId, MenuTypeId2;
    HomeFragment1 fragment;
    MenuMasterList[] bhajans;

    List<Bhajan> bhajanList;

    HomeActivityy activityy;
    public static MenuMasterList[] bhajanforImagefromHome;
    public static int newspos1;
    int pos;

    public MyAdapter2(Context context, List<MenuMasterList> categories1List, String MenuTypeId, String MenuTypeId2, Fragment fragment) {
        this.context = context;
        this.fragment = (HomeFragment1) fragment;
        activityy = (HomeActivityy) context;
        this.categories1List = categories1List;
        this.MenuTypeId = MenuTypeId;
        this.MenuTypeId2 = MenuTypeId2;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_list2, parent, false);
            return new ChannelHolder(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.premium_series_item_2, parent, false);
            return new PremiumViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_list3, parent, false);
            return new ViewHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ChannelHolder headerHolder = (ChannelHolder) holder;

            headerHolder.textView.setVisibility(View.GONE);
            if (position == pos) {
                headerHolder.textView.setVisibility(View.VISIBLE);
            }
            Glide.with(context)
                    .load(categories1List.get(position).getImage())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(headerHolder.imageView);

            activityy.homeChannelList1.addAll(categories1List);

            headerHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;

                   // fragment.updatePrevious();
                    fragment.setPlayer(categories1List.get(position).getChannelUrl(), categories1List.get(position).getName(), categories1List.get(position).getImage(), categories1List.get(position).getLikes()
                            , categories1List.get(position).getId(), categories1List.get(position).getIs_likes(), position, categories1List.get(position));
                    notifyDataSetChanged();

                }
            });


        }
        if (getItemViewType(position) == 2) {

            PremiumViewHolder headerHolder = (PremiumViewHolder) holder;
            if (MenuTypeId.equalsIgnoreCase("6")
                    || MenuTypeId.equalsIgnoreCase("14")
                    || MenuTypeId.equalsIgnoreCase("15")) {

                Glide.with(context)
                        .load(categories1List.get(position).getSeason_thumbnail())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(headerHolder.imageView1);
                headerHolder.textView.setVisibility(View.GONE);
                headerHolder.textView.setText(categories1List.get(position).getSeason_title());
                headerHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSeasonEpifrag(position, "season");
                    }
                });
            }
            if (MenuTypeId.equalsIgnoreCase("9")) {

                Glide.with(context)
                        .load(categories1List.get(position).getSeason_thumbnail())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(headerHolder.imageView1);
                headerHolder.textView.setVisibility(View.GONE);
                headerHolder.textView.setText(categories1List.get(position).getSeason_title());
                headerHolder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSeasonEpifrag(position, "season");
                    }
                });
            }

        } else {
            if (MenuTypeId.equalsIgnoreCase("10")) {

                Glide.with(context)
                        .load(categories1List.get(position).getThumbnailUrl())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.textView.setVisibility(View.GONE);
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getEpisode_title());
                holder.imageView1.setOnClickListener(v -> showSeasonEpifrag(position, "episodes"));
            }
            if (MenuTypeId.equalsIgnoreCase("5")) {

                Glide.with(context)
                        .load(categories1List.get(position).getBannerImage())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
               /* int view1= Integer.parseInt(categories1List.get(position).get());
                int view2= Integer.parseInt(categories1List.get(position).getYoutube_views());
                int finalView=view1+view2;
                String views="";
                if (finalView<=1){
                    views=String.valueOf(finalView)+" view";
                }
                if (finalView>1){
                    views=String.valueOf(finalView)+" views";
                }
                holder.bhajan_single_category_single_item_text_views.setText(views);*/
                holder.textView.setText(categories1List.get(position).getName());
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data1", categories1List.get(position));
                        bundle.putInt("type", 2);
                        GuruDetailsFragment fragment = new GuruDetailsFragment();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Guru_Details").replace(R.id.container_layout_home, fragment).commit();

                    }
                });
            }

            if (MenuTypeId.equalsIgnoreCase("4")) {

                Glide.with(context)
                        .load(categories1List.get(position).getImage())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                //  holder.textView.setVisibility(View.GONE);
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                int view1 = Integer.parseInt(categories1List.get(position).getViewsCount());
                //int view2= Integer.parseInt(categories1List.get(position).getYoutube_views());
                //int finalView=view1+view2;

                String vieW = "";
                if (view1 <= 1) {
                    vieW = coolNumberFormat(view1) + " view";
                }
                if (view1 > 1) {
                    vieW = coolNumberFormat(view1) + " views";
                }
                holder.bhajan_single_category_single_item_text_views.setText(vieW);
                holder.textView.setText(categories1List.get(position).getTitle());
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("MasterlistNews", categories1List.get(position));
                        bundle.putSerializable("MasterlistNews1", (Serializable) categories1List);
                        bundle.putInt("type", 2);
                        newspos1 = position;
                        Log.d("newspos", String.valueOf(position));
                        NewsDetailFragRecent fragment = new NewsDetailFragRecent();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack(Constants.READ_NEWS)
                                .replace(R.id.container_layout_home, fragment)
                                .commit();
                    }
                });
            }
            if (MenuTypeId.equalsIgnoreCase("3")) {

                Glide.with(context)
                        .load(categories1List.get(position).getThumbnailUrl())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                int view1 = 0;
                if (categories1List.get(position).getViews() != null) {
                    view1 = Integer.parseInt(categories1List.get(position).getViews());
                }

                String vieW = "";
                if (view1 <= 1) {
                    vieW = coolNumberFormat(view1) + " view";
                }
                if (view1 > 1) {
                    vieW = coolNumberFormat(view1) + " views";
                }

                holder.bhajan_single_category_single_item_text_views.setText(vieW);

                holder.textView.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getVideoTitle());
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(categories1List.get(position).getYoutubeUrl()))
                        fragment.playVideo(categories1List, position, MenuTypeId2, activityy.ads_count++);
                        else ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");
                    }
                });
            }
            if (MenuTypeId.equalsIgnoreCase("13")) {

                Glide.with(context)
                        .load(categories1List.get(position).getThumbnailUrl())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                holder.textView.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getVideoTitle());
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (categories1List.get(position).getType().equalsIgnoreCase("1")) {
                            if (!TextUtils.isEmpty(categories1List.get(position).getYoutubeUrl()))
                                fragment.playVideo(categories1List, position, MenuTypeId2, activityy.ads_count++);
                            else ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");
                            //fragment.playVideo(categories1List, position, MenuTypeId2, activityy.ads_count++);
                        }
                        if (categories1List.get(position).getType().equalsIgnoreCase("2")) {
                            showSeasonEpifrag(position, "season");
                        }

                    }
                });
            }
            if (MenuTypeId.equalsIgnoreCase("11")) {

                Glide.with(context)
                        .load(categories1List.get(position).getThumbnailUrl())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);

                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                int view1 = 0;
                if (categories1List.get(position).getViews() != null) {
                    view1 = Integer.parseInt(categories1List.get(position).getViews());
                }

                String vieW = "";
                if (view1 <= 1) {
                    vieW = coolNumberFormat(view1) + " view";
                }
                if (view1 > 1) {
                    vieW = coolNumberFormat(view1) + " views";
                }

                holder.bhajan_single_category_single_item_text_views.setText(vieW);
                holder.textView.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getVideoTitle());
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(categories1List.get(position).getYoutubeUrl()))
                            fragment.playVideo(categories1List, position, MenuTypeId2, activityy.ads_count++);
                        else ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");
                      //  fragment.playVideo(categories1List, position, MenuTypeId2, activityy.ads_count++);
                    }
                });
            }
            if (MenuTypeId.equalsIgnoreCase("2")) {

                Glide.with(context)
                        .load(categories1List.get(position).getImage())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.textView.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getTitle());
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);
                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityy.homeBhajanListNew.size() > 0) {
                            activityy.homeBhajanListNew.clear();
                        }
                        activityy.homeBhajanListNew.addAll(categories1List);
                        if (activityy.homeBhajanListNew != null) {
                            bhajans = new MenuMasterList[activityy.homeBhajanListNew.size()];
                            for (int i = 0; i < activityy.homeBhajanListNew.size(); i++) {
                                bhajans[i] = activityy.homeBhajanListNew.get(i);
                            }
                        }

                        if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
                            HomeActivityy.playerlayout1.setVisibility(View.GONE);

                        if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                            HomeActivityy.playerlayout2.setVisibility(View.GONE);


                        Bundle bundle = new Bundle();
                        try {

                            if (downloadmediaplayer != null) {
                                if (downloadmediaplayer.isPlaying()) {
                                    downloadmediaplayer.pause();
                                }
                            }

                            if (playlistmediaplayer != null) {
                                if (playlistmediaplayer.isPlaying()) {
                                    playlistmediaplayer.pause();
                                }
                            }
                            if (bhajans.length >= 1) {
                                if (categories1List.get(position).getDirectPlay().equalsIgnoreCase("1")) {
                                    if (!PlayListFrag.serviceBound) {
                                        if (!DownloadsFragment.serviceBound) {
                                            if (!BhajanViewAllFragment.serviceBound) {
                                                if (!HomeFragment1.serviceBound) {
                                                    if (!BhajansCategoryFragment.serviceBound) {
                                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                                        intentservice.putExtra("bhajanarray1", bhajans);
                                                        intentservice.putExtra("status", "bhajan");
                                                        intentservice.putExtra("redirect", Constants.AUDIO_ACTIVE_LIST);
                                                        PreferencesHelper.getInstance().setValue("index", position);

                                                        context.startService(intentservice);
                                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                                        Constants.LIST_INDEX = position;
                                                        bhajanforImagefromHome = bhajans;
                                                        bundle.putInt("position", position);
                                                        ((HomeActivityy) context).back1 = "";
                                                        bundle.putString("status", "bhajan");
                                                        Networkconstants.bhajan_name="bhajan";
                                                        bundle.putSerializable("bhajans1", bhajans);
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
                                                        intent.putExtra("bhajanlist1", bhajans);
                                                        bundle.putSerializable("bhajanlist1", bhajans);
                                                        Constants.LIST_INDEX = position;

                                                        bhajanforImagefromHome = bhajans;

                                                        PreferencesHelper.getInstance().setValue("index", position);
                                                        intent.putExtra("position", position);
                                                        context.sendBroadcast(intent);

                                                        bundle.putInt("position", position);
                                                        bundle.putSerializable("bhajans1", bhajans);
                                                        Fragment fragment = new BhajanPlayFragment();
                                                        fragment.setArguments(bundle);
                                                        ((HomeActivityy) context)
                                                                .getSupportFragmentManager()
                                                                .beginTransaction()
                                                                .addToBackStack("BHAJANS")
                                                                .replace(R.id.container_layout_home, fragment)
                                                                .commit();
                                                    }

                                                } else {
                                                    Constants.AUDIO_ACTIVE_LIST = "home";
                                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                                    intent.putExtra("bhajanlist1", bhajans);
                                                    bundle.putSerializable("bhajanlist1", bhajans);
                                                    Constants.LIST_INDEX = position;
                                                    bhajanforImagefromHome = bhajans;
                                                    PreferencesHelper.getInstance().setValue("index", position);
                                                    intent.putExtra("position", position);
                                                    context.sendBroadcast(intent);

                                                    bundle.putInt("position", position);
                                                    bundle.putSerializable("bhajans1", bhajans);
                                                    Fragment fragment = new BhajanPlayFragment();
                                                    fragment.setArguments(bundle);
                                                    ((HomeActivityy) context)
                                                            .getSupportFragmentManager()
                                                            .beginTransaction()
                                                            .addToBackStack("BHAJANS")
                                                            .replace(R.id.container_layout_home, fragment)
                                                            .commit();

                                                }
                                            } else {
                                                Constants.AUDIO_ACTIVE_LIST = "home";
                                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                                intent.putExtra("bhajanlist1", bhajans);
                                                bundle.putSerializable("bhajanlist1", bhajans);
                                                Constants.LIST_INDEX = position;
                                                bhajanforImagefromHome = bhajans;
                                                PreferencesHelper.getInstance().setValue("index", position);
                                                intent.putExtra("position", position);
                                                context.sendBroadcast(intent);

                                                bundle.putInt("position", position);
                                                bundle.putSerializable("bhajans1", bhajans);
                                                Fragment fragment = new BhajanPlayFragment();
                                                fragment.setArguments(bundle);
                                                ((HomeActivityy) context)
                                                        .getSupportFragmentManager()
                                                        .beginTransaction()
                                                        .addToBackStack("BHAJANS")
                                                        .replace(R.id.container_layout_home, fragment)
                                                        .commit();
                                            }
                                        } else {
                                            Constants.AUDIO_ACTIVE_LIST = "home";
                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist1", bhajans);
                                            bundle.putSerializable("bhajanlist1", bhajans);
                                            Constants.LIST_INDEX = position;
                                            bhajanforImagefromHome = bhajans;
                                            PreferencesHelper.getInstance().setValue("index", position);
                                            intent.putExtra("position", position);
                                            context.sendBroadcast(intent);

                                            bundle.putInt("position", position);
                                            bundle.putSerializable("bhajans1", bhajans);
                                            Fragment fragment = new BhajanPlayFragment();
                                            fragment.setArguments(bundle);
                                            ((HomeActivityy) context)
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .addToBackStack("BHAJANS")
                                                    .replace(R.id.container_layout_home, fragment)
                                                    .commit();
                                        }
                                    } else {
                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist1", bhajans);
                                        bundle.putSerializable("bhajanlist1", bhajans);
                                        Constants.LIST_INDEX = position;
                                        bhajanforImagefromHome = bhajans;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);

                                        bundle.putInt("position", position);
                                        bundle.putSerializable("bhajans1", bhajans);
                                        Fragment fragment = new BhajanPlayFragment();
                                        fragment.setArguments(bundle);
                                        ((HomeActivityy) context)
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .addToBackStack("BHAJANS")
                                                .replace(R.id.container_layout_home, fragment)
                                                .commit();
                                    }
                                } else {
                                    bundle.putSerializable("bhajanViewAll1", categories1List.get(position));
                                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                                    fragment.setArguments(bundle);
                                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home, fragment).commit();
                                }
                            } else {
                                Toast.makeText(context, "Please Wait!!!...Still Loading!!!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            if (MenuTypeId.equalsIgnoreCase("12")) {

                Glide.with(context)
                        .load(categories1List.get(position).getImage())
                        .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imageView1);
                holder.textView.setVisibility(View.GONE);
                holder.textView.setText(categories1List.get(position).getTitle());
                holder.bhajan_single_category_single_item_text_views.setVisibility(View.GONE);

                holder.imageView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activityy.homeBhajanListNew.size() > 0) {
                            activityy.homeBhajanListNew.clear();
                        }
                        activityy.homeBhajanListNew.addAll(categories1List);
                        if (activityy.homeBhajanListNew != null) {
                            bhajans = new MenuMasterList[activityy.homeBhajanListNew.size()];
                            for (int i = 0; i < activityy.homeBhajanListNew.size(); i++) {
                                bhajans[i] = activityy.homeBhajanListNew.get(i);
                            }
                        }
                        if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
                            HomeActivityy.playerlayout1.setVisibility(View.GONE);

                        if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                            HomeActivityy.playerlayout2.setVisibility(View.GONE);


                        Bundle bundle = new Bundle();
                        try {

                            if (downloadmediaplayer != null) {
                                if (downloadmediaplayer.isPlaying()) {
                                    downloadmediaplayer.pause();
                                }
                            }

                            if (playlistmediaplayer != null) {
                                if (playlistmediaplayer.isPlaying()) {
                                    playlistmediaplayer.pause();
                                }
                            }

                            if (bhajans.length >= 1) {
                                if (categories1List.get(position).getDirectPlay().equalsIgnoreCase("1")) {
                                    if (!PlayListFrag.serviceBound) {
                                        if (!DownloadsFragment.serviceBound) {
                                            if (!BhajanViewAllFragment.serviceBound) {
                                                if (!HomeFragment1.serviceBound) {
                                                    if (!BhajansCategoryFragment.serviceBound) {
                                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                                        Intent intentservice = new Intent(context, AudioPlayerService.class);
                                                        intentservice.putExtra("bhajanarray1", bhajans);
                                                        intentservice.putExtra("status", "bhajan");
                                                        intentservice.putExtra("redirect", Constants.AUDIO_ACTIVE_LIST);
                                                        PreferencesHelper.getInstance().setValue("index", position);

                                                        context.startService(intentservice);
                                                        context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                                        Constants.IS_PLAYING_ON_CATEGORY = "false";
                                                        // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                                        Constants.LIST_INDEX = position;
                                                        bhajanforImagefromHome = bhajans;
                                                        bundle.putInt("position", position);
                                                        ((HomeActivityy) context).back1 = "";
                                                        bundle.putString("status", "bhajan");
                                                        Networkconstants.bhajan_name="bhajan";
                                                        bundle.putSerializable("bhajans1", bhajans);
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
                                                        intent.putExtra("bhajanlist1", bhajans);
                                                        bundle.putSerializable("bhajanlist1", bhajans);
                                                        Constants.LIST_INDEX = position;

                                                        bhajanforImagefromHome = bhajans;

                                                        PreferencesHelper.getInstance().setValue("index", position);
                                                        intent.putExtra("position", position);
                                                        context.sendBroadcast(intent);
                                                    }

                                                } else {
                                                    Constants.AUDIO_ACTIVE_LIST = "home";
                                                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                                    intent.putExtra("bhajanlist1", bhajans);
                                                    bundle.putSerializable("bhajanlist1", bhajans);
                                                    Constants.LIST_INDEX = position;
                                                    bhajanforImagefromHome = bhajans;
                                                    PreferencesHelper.getInstance().setValue("index", position);
                                                    intent.putExtra("position", position);
                                                    context.sendBroadcast(intent);

                                                }
                                            } else {
                                                Constants.AUDIO_ACTIVE_LIST = "home";
                                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                                intent.putExtra("bhajanlist1", bhajans);
                                                bundle.putSerializable("bhajanlist1", bhajans);
                                                Constants.LIST_INDEX = position;
                                                bhajanforImagefromHome = bhajans;
                                                PreferencesHelper.getInstance().setValue("index", position);
                                                intent.putExtra("position", position);
                                                context.sendBroadcast(intent);
                                            }
                                        } else {
                                            Constants.AUDIO_ACTIVE_LIST = "home";
                                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                            intent.putExtra("bhajanlist1", bhajans);
                                            bundle.putSerializable("bhajanlist1", bhajans);
                                            Constants.LIST_INDEX = position;
                                            bhajanforImagefromHome = bhajans;
                                            PreferencesHelper.getInstance().setValue("index", position);
                                            intent.putExtra("position", position);
                                            context.sendBroadcast(intent);
                                        }
                                    } else {
                                        Constants.AUDIO_ACTIVE_LIST = "home";
                                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                        intent.putExtra("bhajanlist1", bhajans);
                                        bundle.putSerializable("bhajanlist1", bhajans);
                                        Constants.LIST_INDEX = position;
                                        bhajanforImagefromHome = bhajans;
                                        PreferencesHelper.getInstance().setValue("index", position);
                                        intent.putExtra("position", position);
                                        context.sendBroadcast(intent);
                                    }
                                } else {
                                    bundle.putSerializable("bhajanViewAll1", categories1List.get(position));
                                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                                    fragment.setArguments(bundle);
                                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home, fragment).commit();
                                }
                            } else {
                                Toast.makeText(context, "Please Wait!!!...Still Loading!!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void showSeasonEpifrag(int position, String type) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", categories1List.get(position));
        bundle.putSerializable("dataMain", (Serializable) categories1List);
        bundle.putString("type", type);
        Layer3Fragment fragment = new Layer3Fragment();
        fragment.setArguments(bundle);
        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Layer3").replace(R.id.container_layout_home, fragment).commit();

      /*  Intent intent=new Intent(context, VideoActivity.class);
        intent.putExtra("data",categories1List.get(position));
        intent.putExtra("type",type);
        context.startActivity(intent);*/

    }

    @Override
    public int getItemCount() {
        return categories1List.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (MenuTypeId.equalsIgnoreCase("1")) {
            return 0;
        }
        if (MenuTypeId.equalsIgnoreCase("6")
                || MenuTypeId.equalsIgnoreCase("9")
                || MenuTypeId.equalsIgnoreCase("14")
                || MenuTypeId.equalsIgnoreCase("15")) {
            return 2;
        }
        if (MenuTypeId.equalsIgnoreCase("9")) {
            return 2;
        }
        if (MenuTypeId.equalsIgnoreCase("10")) {
            return 1;
        } else {
            return 1;
        }

    }

    public static String coolNumberFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;
        TextView textView, bhajan_single_category_single_item_text_views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            textView = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            bhajan_single_category_single_item_text_views = itemView.findViewById(R.id.bhajan_single_category_single_item_text_views);
        }
    }

    private class PremiumViewHolder extends ViewHolder {
        ImageView imageView1;
        TextView textView;

        public PremiumViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            textView = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
        }
    }

    private class ChannelHolder extends ViewHolder {
        ImageView imageView;
        TextView textView;

        public ChannelHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.channel_image);
            textView = view.findViewById(R.id.live_text);
        }
    }
}
