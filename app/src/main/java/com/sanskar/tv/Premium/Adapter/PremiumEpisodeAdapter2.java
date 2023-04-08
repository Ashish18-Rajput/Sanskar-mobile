package com.sanskar.tv.Premium.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

public class PremiumEpisodeAdapter2 extends RecyclerView.Adapter<PremiumEpisodeAdapter2.ViewHolder> {

    Context context;
    List<MenuMasterList> masterLists;
    Fragment fragment;

    public PremiumEpisodeAdapter2(Context context, List<MenuMasterList> masterLists) {
        this.context = context;
        this.masterLists = masterLists;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_list4, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(masterLists.get(position).getThumbnailUrl())
                .apply(new RequestOptions().error(R.mipmap.landscape_placeholder).placeholder(R.mipmap.landscape_placeholder))
                .into(holder.bhajan_single_category_single_item_image);

        holder.bhajan_single_category_single_item_text.setVisibility(View.GONE);
        if (masterLists.get(position).getIs_locked().equalsIgnoreCase("0")){
            holder.lock_video_icon.setVisibility(View.GONE);
        }
        if (masterLists.get(position).getIs_locked().equalsIgnoreCase("1")){
            holder.lock_video_icon.setVisibility(View.VISIBLE);
        }
        holder.bhajan_single_category_single_item_text.setText(masterLists.get(position).getEpisode_title());
        holder.bhajan_single_category_single_item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (masterLists.get(position).getIs_locked().equalsIgnoreCase("0")){
                    showSeasonEpifrag(position, 2);
                }
                if (masterLists.get(position).getIs_locked().equalsIgnoreCase("1")){
                    ToastUtil.showDialogBox(context,"Please Purchase it");
                }

            }
        });

    }

    private void showSeasonEpifrag(int position, int type) {

       /* Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("data", masterLists.get(position));
        context.startActivity(intent);*/

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", masterLists.get(position));
        Layer3Fragment fragment = new Layer3Fragment();
        fragment.setArguments(bundle);
        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction().addToBackStack("Layer3").replace(R.id.container_layout_home, fragment).commit();


    }

    @Override
    public int getItemCount() {
        return masterLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bhajan_single_category_single_item_image,lock_video_icon;
        TextView bhajan_single_category_single_item_text, desc_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bhajan_single_category_single_item_image = itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            bhajan_single_category_single_item_text = itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            lock_video_icon=itemView.findViewById(R.id.lock_video_icon);
            //  desc_txt=itemView.findViewById(R.id.desc_txt);
        }
    }
}
