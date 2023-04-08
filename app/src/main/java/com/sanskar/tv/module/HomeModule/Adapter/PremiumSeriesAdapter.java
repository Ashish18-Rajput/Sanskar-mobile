package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import java.util.List;

public class PremiumSeriesAdapter extends RecyclerView.Adapter<PremiumSeriesAdapter.ViewHolder> {

    Context context;
    List<MenuMasterList> promoList;
    PremiumSeriesAdapter2 premiumSeriesAdapter2;

    public PremiumSeriesAdapter(Context context, List<MenuMasterList> promoList) {
        this.context = context;
        this.promoList = promoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(context).inflate(R.layout.premium_series_list,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if (!promoList.get(i).getSeason_details().isEmpty()){
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.home_free_txt.setVisibility(View.VISIBLE);
            viewHolder.view.setVisibility(View.VISIBLE);
            viewHolder.recyclerView_free.setVisibility(View.VISIBLE);
            viewHolder.free_image_view_all.setVisibility(View.VISIBLE);
            viewHolder.home_free_txt.setText(promoList.get(i).getCat_name());
        }else{
            viewHolder.home_free_txt.setVisibility(View.GONE);
            viewHolder.view.setVisibility(View.GONE);
            viewHolder.recyclerView_free.setVisibility(View.GONE);
            viewHolder.free_image_view_all.setVisibility(View.GONE);
            viewHolder.itemView.setVisibility(View.GONE);
        }

        viewHolder.recyclerView_free.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        premiumSeriesAdapter2=new PremiumSeriesAdapter2(context,promoList.get(i).getSeason_details());
        viewHolder.recyclerView_free.setAdapter(premiumSeriesAdapter2);
        premiumSeriesAdapter2.notifyDataSetChanged();

        viewHolder.free_view_all.setOnClickListener(v -> {
            ((HomeActivityy)context).Category="4";
            ((HomeActivityy)context).Category_name=promoList.get(i).getCat_name();
            ((HomeActivityy)context).Video_id=promoList.get(i).getId();
            ((HomeActivityy)context).showPromoPremiumCategory();
        });

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                viewHolder.home_free_txt.setTextColor(Color.parseColor("#FFFFFF"));
                viewHolder.free_view.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                viewHolder.home_free_txt.setTextColor(Color.parseColor("#000000"));
                viewHolder.free_view.setTextColor(Color.parseColor("#000000"));
                break;

        }


    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView home_free_txt,free_view;
        View view;
        RecyclerView recyclerView_free;
        RelativeLayout free_view_all;
        ImageView free_image_view_all;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            home_free_txt=itemView.findViewById(R.id.home_free_txt);
            free_view=itemView.findViewById(R.id.free_view);
            recyclerView_free=itemView.findViewById(R.id.recyclerView_free);
            view=itemView.findViewById(R.id.view1);
            free_image_view_all=itemView.findViewById(R.id.free_image_view_all);
            free_view_all=itemView.findViewById(R.id.free_view_all);
        }
    }
}
