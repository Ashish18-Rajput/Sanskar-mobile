package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.WallPaperModelMain;

import java.util.List;

public class WallPaperAdapter1 extends RecyclerView.Adapter<WallPaperAdapter1.ViewHolder> {

    Context context;
    HomeActivityy homeActivityy;
    List<WallPaperModelMain> wallPaperModelMainList;


    public WallPaperAdapter1(Context context, HomeActivityy homeActivityy, List<WallPaperModelMain> wallPaperModelMainList) {
        this.context = context;
        this.homeActivityy = homeActivityy;
        this.wallPaperModelMainList = wallPaperModelMainList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.home_free_txt.setText(wallPaperModelMainList.get(position).getCategory_name());
        holder.free_view_all.setOnClickListener(view-> homeActivityy.showViewAllFragmentForWallpaper(wallPaperModelMainList.get(position).getWallPaperModelList()));
        holder.recyclerView_free.setAdapter(new WallPaperAdapter2(context,homeActivityy,wallPaperModelMainList.get(position).getWallPaperModelList()));
    }

    @Override
    public int getItemCount() {
        return wallPaperModelMainList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView home_free_txt;
        RecyclerView recyclerView_free;
        RelativeLayout free_view_all;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView_free = itemView.findViewById(R.id.recyclerView_free);
            recyclerView_free.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
            home_free_txt = itemView.findViewById(R.id.home_free_txt);
            free_view_all = itemView.findViewById(R.id.free_view_all);
        }
    }
}
