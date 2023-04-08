package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.R;

import java.util.ArrayList;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class BhajanCategorySingleItemAdapter extends RecyclerView.Adapter<BhajanCategorySingleItemAdapter.ViewHolder> {

    Bhajan[] bhajans;
    ArrayList<Bhajan> bhajanArrayList;
    Context context;
    //private String categoryName;
    private Progress mprogress;
    private String directPlay;

    public BhajanCategorySingleItemAdapter(Bhajan[] bhajans, int position, Context context) {
//        this.bhajanArrayList = bhajanArrayList;
        this.bhajans = bhajans;
        this.context = context;
        //this.categoryName = categoryName;
        //mprogress = new Progress(context);
        //mprogress.setCancelable(false);
//        this.directPlay = Integer.parseInt(directPlay);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_bhajan_adapter,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (bhajans[position].getDirect_play().equalsIgnoreCase("0")) {
            Ion.with(context)
                    .load(bhajans[position].getArtist_image())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
//                    Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                    holder.categoryImage.setImageBitmap(result);

                }
            });
            holder.categoryName.setText(bhajans[position].getArtist_name());
            holder.categoryName.setMaxLines(2);
            holder.desc.setVisibility(View.GONE);
        } else {
            Ion.with(context)
                    .load(bhajans[position].getImage())
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            // Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                            holder.categoryImage.setImageBitmap(result);
                        }
                    });
            holder.categoryName.setText(bhajans[position].getTitle());
            holder.desc.setText(bhajans[position].getArtist_name());

            String desc = Html.fromHtml(bhajans[position].getDescription()).toString();
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(desc);
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if(bhajans[position].getDirect_play().equalsIgnoreCase("1")) {
                    bundle.putInt("position", position);
                    bundle.putSerializable("bhajans",bhajans);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }

                else{
                    bundle.putSerializable("bhajanViewAll",bhajans[position]);
                    BhajanViewAllFragment fragment = new BhajanViewAllFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("BHAJANS").replace(R.id.container_layout_home,fragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
//        return bhajanArrayList.size();
        return bhajans.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView categoryName;
        RelativeLayout layout;
        ImageView mp3VideoIcon;
        private TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.bhajan_single_category_single_item_image);

            categoryName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            desc = itemView.findViewById(R.id.desc_txt);
            layout = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_layout);
            mp3VideoIcon = itemView.findViewById(R.id.mp3_video_icon);

            mp3VideoIcon.setImageResource(R.mipmap.mp3);
            mp3VideoIcon.setVisibility(View.VISIBLE);
        }
    }

}
