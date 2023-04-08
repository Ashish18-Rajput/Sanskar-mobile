package com.sanskar.tv.Premium.Adapter;

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
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

public class PromoPremiumAdapter2 extends RecyclerView.Adapter<PromoPremiumAdapter2.ViewHolder> {

    Context context;
    List<MenuMasterList> masterLists;

    public PromoPremiumAdapter2(Context context, List<MenuMasterList> masterLists) {
        this.context = context;
        this.masterLists = masterLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.premium_series_item_2, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context)
                .load(masterLists.get(position).getSeason_thumbnail())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(holder.bhajan_single_category_single_item_image);

        holder.bhajan_single_category_single_item_text.setVisibility(View.GONE);
        holder.bhajan_single_category_single_item_text.setText(masterLists.get(position).getSeason_title());
        holder.bhajan_single_category_single_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeasonEpifrag(position, "season");
            }
        });


    }

    private void showSeasonEpifrag(int position, String type) {

       /* Intent intent=new Intent(context, VideoActivity.class);
        intent.putExtra("data",masterLists.get(position));
        intent.putExtra("type",type);
        context.startActivity(intent);*/
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", masterLists.get(position));
        bundle.putString("type", type);
        Layer3Fragment fragment = new Layer3Fragment();
        fragment.setArguments(bundle);
        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Layer3").replace(R.id.container_layout_home, fragment).commit();


    }

    @Override
    public int getItemCount() {
        return masterLists.size();
    }
    /*@Override
    public int getItemViewType(int i) {
        if (masterLists.get(0).getSeason_title()!=null) {
            return 0;
        }
        else {
            return 1;
        }

    }*/


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bhajan_single_category_single_item_image;
        TextView bhajan_single_category_single_item_text, desc_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bhajan_single_category_single_item_image = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            bhajan_single_category_single_item_text = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            //  desc_txt=itemView.findViewById(R.id.desc_txt);
        }
    }

    private class PremiumViewHolder extends ViewHolder {
        ImageView bhajan_single_category_single_item_image;
        TextView bhajan_single_category_single_item_text, desc_txt;

        public PremiumViewHolder(View view) {
            super(view);
            bhajan_single_category_single_item_image = view.findViewById(R.id.bhajan_single_category_single_item_image);
            bhajan_single_category_single_item_text = view.findViewById(R.id.bhajan_single_category_single_item_text);
        }
    }
}
