package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

public class PremiumEpisodeAdapter extends RecyclerView.Adapter<PremiumEpisodeAdapter.ViewHolder> {

    Context context;
    List<MenuMasterList> masterLists;
    Fragment fragment;

    public PremiumEpisodeAdapter(Context context, List<MenuMasterList> masterLists,Fragment fragment) {
        this.context = context;
        this.masterLists = masterLists;
        this.fragment=fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_recyclerview_list3,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(masterLists.get(position).getThumbnailUrl())
                .apply(new RequestOptions().error(R.mipmap.landscape_placeholder).placeholder(R.mipmap.landscape_placeholder))
                .into(holder.bhajan_single_category_single_item_image);

        holder.bhajan_single_category_single_item_text.setVisibility(View.GONE);
        holder.bhajan_single_category_single_item_text.setText(masterLists.get(position).getEpisode_title());
        if (masterLists.get(position).isIs_now_playing()){
            holder.now_playing_relative_layout.setVisibility(View.VISIBLE);
        }
        if (!masterLists.get(position).isIs_now_playing()){
            holder.now_playing_relative_layout.setVisibility(View.GONE);
        }

        if (masterLists.get(position).getIs_locked().equalsIgnoreCase("0")){
            holder.lock_video_icon.setVisibility(View.GONE);
        }if (masterLists.get(position).getIs_locked().equalsIgnoreCase("1")){
            holder.lock_video_icon.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return masterLists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView bhajan_single_category_single_item_image,lock_video_icon;
        TextView bhajan_single_category_single_item_text,desc_txt;
        RelativeLayout now_playing_relative_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bhajan_single_category_single_item_image=itemView.findViewById(R.id.bhajan_single_category_single_item_image);
            bhajan_single_category_single_item_text=itemView.findViewById(R.id.bhajan_single_category_single_item_text);
            lock_video_icon=itemView.findViewById(R.id.lock_video_icon);
            now_playing_relative_layout=itemView.findViewById(R.id.now_playing_relative_layout);

            bhajan_single_category_single_item_image.setOnClickListener(this::onClick);
            //  desc_txt=itemView.findViewById(R.id.desc_txt);
        }

        @Override
        public void onClick(View v) {
            if (v == bhajan_single_category_single_item_image){
                for (int i =0 ; i< masterLists.size(); i++)
                    masterLists.get(i).setIs_now_playing(i == getAdapterPosition());

                if (masterLists.get(getAdapterPosition()).getIs_locked().equalsIgnoreCase("0")){
                   // ((Layer3Fragment)fragment).setOrientation();
                    ((Layer3Fragment)fragment).setVideoData(masterLists.get(getAdapterPosition()),"episodes");
                }if (masterLists.get(getAdapterPosition()).getIs_locked().equalsIgnoreCase("1")){
                    /*if (masterLists.get(position).getEpisode_url()!=null || masterLists.get(position).getYt_episode_url()!=null){
                        ((Layer3Fragment)fragment).setVideoData(masterLists.get(position),"episodes");
                    }
                    else{
                        ToastUtil.showDialogBox(context,"Please Purchase it");

                    }*/
                    ToastUtil.showDialogBox1(context,"Please subscribe us to watch this video");
                }

                notifyDataSetChanged();
            }
        }
    }
}
