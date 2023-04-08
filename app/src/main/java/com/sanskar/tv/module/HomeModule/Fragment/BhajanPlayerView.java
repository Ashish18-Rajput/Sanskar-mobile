package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import android.widget.RelativeLayout;

import com.sanskar.tv.R;
import com.squareup.picasso.Picasso;


public class BhajanPlayerView extends PagerAdapter {
    Context context;
    Integer[] images=new Integer[]{};
    String [] imgs = new String[]{};
    LayoutInflater layoutInflater;


    public BhajanPlayerView(Context context, Integer[] images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BhajanPlayerView(Context context, String[] imgs) {
        this.context = context;
        this.imgs = imgs;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(images.length>0)
            return images.length;
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.bhajan_player_view, container, false);

        ImageView imageView = itemView.findViewById(R.id.bhajan_img);
        if (images.length > 0)
            Picasso.with(context).load(images[position]).placeholder(R.mipmap.landscape_placeholder).into(imageView);
        else {
            Picasso.with(context).load(Uri.parse(imgs[position])).placeholder(R.mipmap.landscape_placeholder).into(imageView);
            container.addView(itemView);
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}