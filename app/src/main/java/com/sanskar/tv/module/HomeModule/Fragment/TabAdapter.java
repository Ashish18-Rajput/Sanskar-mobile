package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case 0:
                TransparentFragment transparentFragment = new TransparentFragment();
                return transparentFragment;
           /* case 1:
                SongListFragment songListFragment = new SongListFragment();
                return songListFragment;*/

            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
