package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment1;
import com.sanskar.tv.module.HomeModule.Fragment.HomeViewAllFrag;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMaster;

import java.util.List;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    List<MenuMaster> categoriesList;
    HomeFragment1 fragment;
    HomeActivityy activityy;

    private final static int FADE_DURATION = 1000;
    private int lastPosition = -1;

    public MyAdapter(Context context, List<MenuMaster> categoriesList, Fragment fragment) {
        this.context = context;
        activityy=(HomeActivityy)context;
        this.fragment= (HomeFragment1) fragment;
        this.categoriesList = categoriesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recyclerview_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!categoriesList.get(position).getList().isEmpty()){
            holder.home_free_txt.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.free_view.setVisibility(View.VISIBLE);
            holder.recyclerView_free.setVisibility(View.VISIBLE);
            holder.free_image_view_all.setVisibility(View.VISIBLE);
            holder.itemView.setVisibility(View.VISIBLE);
            holder.home_free_txt.setText(categoriesList.get(position).getMenuTitle());
        }
        else{
            holder.home_free_txt.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
            holder.recyclerView_free.setVisibility(View.GONE);
            holder.free_view.setVisibility(View.GONE);
            holder.free_image_view_all.setVisibility(View.GONE);

            holder.itemView.setVisibility(View.GONE);
        }
        holder.recyclerView_free.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        holder.recyclerView_free.setAdapter(new MyAdapter2(context,categoriesList.get(position).getList(),categoriesList.get(position).getMenuTypeId(),categoriesList.get(position).getId(),fragment));


        if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("13")){
            holder.free_view_all.setVisibility(View.GONE);
        }

        holder.free_view_all.setOnClickListener(v -> {

            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("1")){
                activityy.showLiveFrag();
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("6")){
                activityy.Category="1";
                activityy.Category_name=categoriesList.get(position).getMenuTitle();
                activityy.Video_id=categoriesList.get(position).getId();
                activityy.showPromoPremiumFrag();
            }
            if ( categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("14")){
                activityy.Category="1";
                activityy.Category_name=categoriesList.get(position).getMenuTitle();
                activityy.Video_id=categoriesList.get(position).getId();
                activityy.Premium_Cat_Id=categoriesList.get(position).getPremium_cat_id();
                activityy.Premium_Auth_Id="";
                activityy.showPromoPremiumFrag();
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("15")){
                activityy.Category="1";
                activityy.Category_name=categoriesList.get(position).getMenuTitle();
                activityy.Video_id=categoriesList.get(position).getId();
                activityy.Premium_Auth_Id=categoriesList.get(position).getPremium_auth_id();
                activityy.Premium_Cat_Id="";
                activityy.showPromoPremiumFrag();
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("9")){
                activityy.Category="2";
                activityy.Category_name=categoriesList.get(position).getMenuTitle();
                activityy.Video_id=categoriesList.get(position).getId();
                activityy.showPromoPremiumFrag();
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("10")){
                activityy.Category="3";
                activityy.Category_name=categoriesList.get(position).getMenuTitle();
                activityy.Video_id=categoriesList.get(position).getId();
                activityy.showPromoPremiumFrag();
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("5")){

                  /*  if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.GURU_LIST_TYPE);
                    } else {*/
                activityy.showGuruFrag();


            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("4")){
                if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                    showHomeViewAllFrag(Constants.NEWS_LIST_TYPE);
                } else {
                    activityy.showNewsFrag();
                }
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("3")){
                if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                    showHomeViewAllFrag(Constants.VIDEO_LIST_TYPE);
                } else {
                    activityy.Category="2";
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    Log.d("Video_id",categoriesList.get(position).getId());
                    ((HomeActivityy) activityy).showLVideoListFrag();
                }
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("11")){
                if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                    showHomeViewAllFrag(Constants.VIDEO_LIST_TYPE);
                } else {
                    activityy.Category="1";
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    Log.d("Video_id",categoriesList.get(position).getId());
                    ((HomeActivityy) activityy).showLVideoListFrag();
                }
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("2")){
                if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                    showHomeViewAllFrag(Constants.BHAJAN_LIST_TYPE);
                } else {
                    activityy.Category="2";
                    activityy.Bhajan_id=categoriesList.get(position).getId();
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.back1="Back";
                    Log.d("Video_id_bhajan",categoriesList.get(position).getId());
                    activityy.showLBhajanListFrag();
                }
            }
            if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("12")){
                if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                    showHomeViewAllFrag(Constants.BHAJAN_LIST_TYPE);
                } else {
                    activityy.Category="1";
                    activityy.Bhajan_id=categoriesList.get(position).getId();
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.back1="Back";
                    Log.d("Video_id_bhajan",categoriesList.get(position).getId());
                    activityy.showLBhajanListFrag();
                }
            }
        });
        holder.free_image_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("1")){

                    activityy.showLiveFrag();
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("6")){
                    activityy.Category="1";
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.showPromoPremiumFrag();
                }
                if ( categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("14")){
                    activityy.Category="1";
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.Premium_Cat_Id=categoriesList.get(position).getPremium_cat_id();
                    activityy.Premium_Auth_Id="";
                    activityy.showPromoPremiumFrag();
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("15")){
                    activityy.Category="1";
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.Premium_Auth_Id=categoriesList.get(position).getPremium_auth_id();
                    activityy.Premium_Cat_Id="";
                    activityy.showPromoPremiumFrag();
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("9")){
                    activityy.Category="2";
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.showPromoPremiumFrag();
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("10")){
                    activityy.Category="3";
                    activityy.Category_name=categoriesList.get(position).getMenuTitle();
                    activityy.Video_id=categoriesList.get(position).getId();
                    activityy.showPromoPremiumFrag();
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("5")){

                      /*  if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                            showHomeViewAllFrag(Constants.GURU_LIST_TYPE);
                        } else {*/
                    activityy.showGuruFrag();

                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("4")){
                    if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.NEWS_LIST_TYPE);
                    } else {
                        activityy.showNewsFrag();
                    }
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("3")){
                    if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.VIDEO_LIST_TYPE);
                    } else {
                        activityy.Category="2";
                        activityy.Video_id=categoriesList.get(position).getId();
                        activityy.Category_name=categoriesList.get(position).getMenuTitle();
                        Log.d("Video_id",categoriesList.get(position).getId());
                        ((HomeActivityy) activityy).showLVideoListFrag();
                    }
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("11")){
                    if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.VIDEO_LIST_TYPE);
                    } else {
                        activityy.Category="1";
                        activityy.Video_id=categoriesList.get(position).getId();
                        activityy.Category_name=categoriesList.get(position).getMenuTitle();
                        Log.d("Video_id",categoriesList.get(position).getId());
                        ((HomeActivityy) activityy).showLVideoListFrag();
                    }
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("2")){
                    if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.BHAJAN_LIST_TYPE);
                    } else {
                        activityy.Category="2";
                        activityy.Bhajan_id=categoriesList.get(position).getId();
                        activityy.Category_name=categoriesList.get(position).getMenuTitle();
                        activityy.back1="Back";
                        Log.d("Video_id_bhajan",categoriesList.get(position).getId());
                        activityy.showLBhajanListFrag();
                    }
                }
                if (categoriesList.get(position).getMenuTypeId().equalsIgnoreCase("12")){
                    if (!TextUtils.isEmpty(activityy.searchContent) && activityy.searchContent != null) {
                        showHomeViewAllFrag(Constants.BHAJAN_LIST_TYPE);
                    } else {
                        activityy.Category="1";
                        activityy.Bhajan_id=categoriesList.get(position).getId();
                        activityy.Category_name=categoriesList.get(position).getMenuTitle();
                        activityy.back1="Back";
                        Log.d("Video_id_bhajan",categoriesList.get(position).getId());
                        activityy.showLBhajanListFrag();
                    }
                }
            }
        });

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                holder.home_free_txt.setTextColor(Color.parseColor("#FFFFFF"));
                holder.free_view.setTextColor(Color.parseColor("#FFFFFF"));

                break;
            case Configuration.UI_MODE_NIGHT_NO:
                holder.home_free_txt.setTextColor(Color.parseColor("#000000"));
                holder.free_view.setTextColor(Color.parseColor("#000000"));
                break;
        }
        //setFadeAnimation(holder.itemView);

        //   setAnimation(holder.itemView, position);

    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView home_free_txt,free_view;
        View view;
        RecyclerView recyclerView_free;
        ImageView free_image_view_all;
        RelativeLayout free_view_all;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_free_txt=itemView.findViewById(R.id.home_free_txt);
            recyclerView_free=itemView.findViewById(R.id.recyclerView_free);
            view=itemView.findViewById(R.id.view1);
            free_view=itemView.findViewById(R.id.free_view);
            free_view_all=itemView.findViewById(R.id.free_view_all);
            free_image_view_all=itemView.findViewById(R.id.free_image_view_all);
        }
    }
    private void showHomeViewAllFrag(String listType) {
        Bundle bundle = new Bundle();
        bundle.putString("HOME_LIST_TYPE", listType);
        HomeViewAllFrag frag = new HomeViewAllFrag();
        frag.setArguments(bundle);
        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout_home, frag).commit();
    }
}
