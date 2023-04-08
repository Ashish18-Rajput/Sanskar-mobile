package com.sanskar.tv.module.HomeModule.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sanskar.tv.module.HomeModule.Fragment.PremiumThumbnailFragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

public class PremiumThumbnailFragAdapter extends FragmentStatePagerAdapter {

    String[] thumbnails1;
    MenuMasterList[] thumbnails;

    public PremiumThumbnailFragAdapter(FragmentManager fm, MenuMasterList[] thumbnails, String[] thumbnails1) {
        super(fm);
        this.thumbnails = thumbnails;
        this.thumbnails1 = thumbnails1;
    }


    @Override
    public Fragment getItem(int i) {

        return PremiumThumbnailFragment.newInstance(thumbnails[i],thumbnails1[i]);
    }

    @Override
    public int getCount() {
        return thumbnails.length;
    }
}
