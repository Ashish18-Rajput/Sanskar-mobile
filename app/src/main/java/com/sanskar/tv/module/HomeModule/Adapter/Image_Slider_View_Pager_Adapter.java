package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import android.widget.LinearLayout;

import com.sanskar.tv.R;
import com.squareup.picasso.Picasso;


public class Image_Slider_View_Pager_Adapter extends PagerAdapter{
    Context context;
    Integer[] images=new Integer[]{};
    String [] imgs = new String[]{};
    LayoutInflater layoutInflater;


    public Image_Slider_View_Pager_Adapter(Context context, Integer[] images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Image_Slider_View_Pager_Adapter(Context context, String[] imgs) {
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
        View itemView = layoutInflater.inflate(R.layout.sliding_image_layout, container, false);

        final ImageView imageView = itemView.findViewById(R.id.imageView);
        if (images.length > 0)
            Picasso.with(context).load(images[position]).placeholder(R.mipmap.landscape_placeholder).into(imageView);
        else {
            Picasso.with(context).load(Uri.parse(imgs[position])).placeholder(R.mipmap.landscape_placeholder).into(imageView);
            container.addView(itemView);
//            Ion.with(context).load(imgs[position]).asBitmap().setCallback(new FutureCallback<Bitmap>() {
//                @Override
//                public void onCompleted(Exception e, Bitmap result) {
//                    imageView.setImageBitmap(result);
//                }
//            });
        }
//

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