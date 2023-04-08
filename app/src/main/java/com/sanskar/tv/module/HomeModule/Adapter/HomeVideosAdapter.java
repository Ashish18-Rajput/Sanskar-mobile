package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.ViewAllVideosFragment;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponse;
import com.sanskar.tv.R;
import java.util.ArrayList;

/**
 * Created by appsquadz on 3/30/2018.
 */

public class HomeVideosAdapter extends RecyclerView.Adapter<HomeVideosAdapter.ViewHolder>{

    Context context;
    ArrayList<VideoResponse> list;

    public HomeVideosAdapter(Context context, ArrayList<VideoResponse> list) {
        this.context = context;
        this.list = list;
   }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_rv_single_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (list.get(position).getVideos().length > 0) {

            holder.parentLayout.setVisibility(View.VISIBLE);
            holder.catName.setVisibility(View.VISIBLE);
            holder.viewAll.setVisibility(View.VISIBLE);

            holder.catName.setText(list.get(position).getCategory());
            holder.viewAll.setText(context.getResources().getString(R.string.view_all));
            holder.viewAll.setTag(list.get(position).getId() + "," + list.get(position).getCategory());
            holder.viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new ViewAllVideosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("category", holder.viewAll.getTag().toString());
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("View All")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }
            });

            holder.viewAllAdapter = new HomeVideoHorizontalAdapter(list.get(position).getVideos(),context);
            holder.container.setAdapter(holder.viewAllAdapter);
        } else{
            holder.parentLayout.setVisibility(View.GONE);
            holder.catName.setVisibility(View.GONE);
            holder.viewAll.setVisibility(View.GONE);
            //list.remove(position)
            //list.remove(position);
//            this.notifyDataSetChanged();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView catName, viewAll;
        RecyclerView container;
        RecyclerView.LayoutManager layoutManager;
        HomeVideoHorizontalAdapter viewAllAdapter;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            catName = itemView.findViewById(R.id.single_item_home_cat_name);
            viewAll = itemView.findViewById(R.id.single_item_home_view_all);
            container = itemView.findViewById(R.id.single_item_home_container_rv);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);;
            container.setLayoutManager(layoutManager);

            ViewCompat.setNestedScrollingEnabled(container, false);

            parentLayout.setVisibility(View.GONE);
            catName.setVisibility(View.GONE);
            viewAll.setVisibility(View.GONE);
        }
    }
}
