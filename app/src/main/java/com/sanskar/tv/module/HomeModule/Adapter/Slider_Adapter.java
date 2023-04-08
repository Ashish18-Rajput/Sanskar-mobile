package com.sanskar.tv.module.HomeModule.Adapter;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import com.sanskar.tv.R;

import java.util.List;

/**
 * Created by admin1 on 6/11/17.
 */

public class Slider_Adapter extends PagerAdapter {

    Context ctx;
    List<String> products;
    ViewGroup container;

    public Slider_Adapter(List<String> products, Context context ) {
        this.ctx = context;
        this.products = products;

    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        this.container = container;
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sliding_image_layout, container, false);

        ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(ctx).load(products.get(position))
                .into(mImageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}
