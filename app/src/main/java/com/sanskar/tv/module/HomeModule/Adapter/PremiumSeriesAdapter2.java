package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.io.Serializable;
import java.util.List;

public class PremiumSeriesAdapter2 extends RecyclerView.Adapter<PremiumSeriesAdapter2.ViewHolder> {

    Context context;
    List<MenuMasterList> seasonDetails;
    List<MenuMasterList> getSeasonDetails;
    String categoryOrder;
    boolean isSecondConstructor = false;

    public PremiumSeriesAdapter2(Context context, List<MenuMasterList> seasonDetails) {
        this.context = context;
        this.seasonDetails = seasonDetails;
        this.categoryOrder=categoryOrder;
    }

    public PremiumSeriesAdapter2(Context context, List<MenuMasterList> seasonDetails, List<MenuMasterList> getSeasonDetails, boolean isSecondConstructor) {
        this.context = context;
        this.seasonDetails = seasonDetails;
        this.getSeasonDetails = getSeasonDetails;
        this.isSecondConstructor = isSecondConstructor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.premium_series_item_2,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (isSecondConstructor){
            Glide.with(context)
                    .load(getSeasonDetails.get(position).getSeason_thumbnail())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into(holder.bhajan_single_category_single_item_image);

            holder.bhajan_single_category_single_item_text.setVisibility(View.GONE);

            holder.bhajan_single_category_single_item_text.setText(getSeasonDetails.get(position).getSeason_title());


            holder.bhajan_single_category_single_item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", getSeasonDetails.get(position));
                    bundle.putSerializable("dataMain", (Serializable) seasonDetails);
                    bundle.putString("type","Premium");
                    Layer3Fragment fragment = new Layer3Fragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Layer3").replace(R.id.container_layout_home, fragment).commit();

                }
            });
        }else{
            Glide.with(context)
                    .load(seasonDetails.get(position).getSeason_thumbnail())
                    .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                    .into(holder.bhajan_single_category_single_item_image);

            holder.bhajan_single_category_single_item_text.setVisibility(View.GONE);

            holder.bhajan_single_category_single_item_text.setText(seasonDetails.get(position).getSeason_title());


            holder.bhajan_single_category_single_item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", seasonDetails.get(position));
                    bundle.putSerializable("dataMain", (Serializable) seasonDetails);
                    bundle.putString("type","Premium");
                    Layer3Fragment fragment = new Layer3Fragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Layer3").replace(R.id.container_layout_home, fragment).commit();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (isSecondConstructor) return  getSeasonDetails.size();
        else return seasonDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bhajan_single_category_single_item_image,lock_video_icon;
        TextView bhajan_single_category_single_item_text,desc_txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bhajan_single_category_single_item_image=itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            bhajan_single_category_single_item_text=itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            lock_video_icon=itemView.findViewById(R.id.lock_video_icon);
            //desc_txt=itemView.findViewById(R.id.desc_txt);
        }
    }
}
