package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.sanskar.tv.module.HomeModule.Fragment.GuruDetailsFragment;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;
import com.sanskar.tv.R;

import java.util.List;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class HomeGuruAdapter extends RecyclerView.Adapter<HomeGuruAdapter.ViewHolder> {

    List<GuruPojo> guruResponseList;
    Context context;

    public HomeGuruAdapter(List<GuruPojo> guruResponseList, Context context) {
        this.guruResponseList = guruResponseList;
        this.context = context;
    }

    @Override
    public HomeGuruAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeGuruAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_category_single_item_bhajan, null));
    }

    @Override
    public void onBindViewHolder(final HomeGuruAdapter.ViewHolder holder, final int position) {
        Ion.with(context).load(guruResponseList.get(position).getProfile_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    holder.artistImage.setImageBitmap(result);
                } else {
                    holder.artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
                }
            }
        });

        //Picasso.with(context).load(guruResponses.get(position).getProfile_image()).placeholder(R.mipmap.profile_pic_default).into(holder.artistImage);
        holder.channelName.setText(guruResponseList.get(position).getName());
        String des = Html.fromHtml(guruResponseList.get(position).getDescription()).toString();
        holder.desc.setText(des);
        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", guruResponseList.get(position));
                GuruDetailsFragment fragment = new GuruDetailsFragment();
                fragment.setArguments(bundle);
                ((HomeActivityy)context).getSupportFragmentManager().beginTransaction().addToBackStack("Guru_Details").replace(R.id.container_layout_home,fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return guruResponseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName;
        TextView desc;
        RelativeLayout singleItem;

        public ViewHolder(View itemView) {
            super(itemView);
//            artistImage = itemView.findViewById(R.id.artist_image_single_item_guru);
//            channelName = itemView.findViewById(R.id.channel_name_single_item_guru);
//            //description = itemView.findViewById(R.id.description_single_item_guru);
//            singleItem = itemView.findViewById(R.id.single_item_guru);

            artistImage = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            channelName = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            desc = itemView.findViewById(R.id.desc_txt);
            singleItem = itemView.findViewById(R.id.bhajan_single_category_single_item_holder_layout);
        }

    }
}