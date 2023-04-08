package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import com.sanskar.tv.module.HomeModule.Fragment.VideosChildFragment;


public class VideosParentViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    int num;
    String searchContent;
    //    ArrayList<Videos[]> videoResponses;

    private Fragment mCurrentFragment;


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public VideosParentViewPagerAdapter(FragmentManager fragmentManager/*, ArrayList<Videos[]> videoResponses*/, Context context, int num,String searchContent) {
        super(fragmentManager);
        this.context = context;
        this.num = num;
        if (searchContent!=null)
        this.searchContent =  searchContent;
        else{
            this.searchContent="";
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new VideosChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return num;
    }
}