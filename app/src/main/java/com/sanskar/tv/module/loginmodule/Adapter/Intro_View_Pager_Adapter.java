package com.sanskar.tv.module.loginmodule.Adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanskar.tv.R;


public class Intro_View_Pager_Adapter extends PagerAdapter{
    Context context;
    String images[];
    LayoutInflater layoutInflater;


    public Intro_View_Pager_Adapter(Context context, String images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.sliding_text_layout, container, false);

        TextView imageView = itemView.findViewById(R.id.imageView);
        imageView.setText(images[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }



}