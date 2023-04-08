package com.sanskar.tv.slidingtabhelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence tabTitles[];
    int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence tabTitles[], int numOfTabs) {
        super(fm);
        this.tabTitles = tabTitles;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            /*BhajanCat allSongs = new BhajanCat();
            return allSongs;*/
        } else {
         //   PlayLists playLists = new PlayLists();
           // return playLists;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
