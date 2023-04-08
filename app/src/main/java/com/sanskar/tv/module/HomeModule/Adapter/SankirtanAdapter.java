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
import com.sanskar.tv.module.HomeModule.Pojo.SankirtanBean;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;

import java.util.List;

/**
 * Created by kapil on 1/5/18.
 */

public class SankirtanAdapter extends RecyclerView.Adapter<SankirtanAdapter.ViewHolder> {

    private Context context;
    private List<SankirtanBean> list;
    Videos[] videos;
    RecyclerView container;

    public SankirtanAdapter(List<SankirtanBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SankirtanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_rv_single_item, null);
        return new SankirtanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SankirtanAdapter.ViewHolder holder, int position) {
        if (list.get(position).getVideos().length > 0) {

            holder.parentLayout.setVisibility(View.VISIBLE);
            holder.catName.setVisibility(View.VISIBLE);
            holder.viewAll.setVisibility(View.VISIBLE);

            holder.viewAllAdapter = new HomeVideoHorizontalAdapter(list.get(position).getVideos(), context);
            container.setAdapter(holder.viewAllAdapter);
            holder.viewAllAdapter.notifyDataSetChanged();

            holder.catName.setText(list.get(position).getCategory());
            holder.viewAll.setText(context.getResources().getString(R.string.view_alll));
            holder.viewAll.setTag(list.get(position).getId() + "," + list.get(position).getCategory());
            holder.viewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new ViewAllVideosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.SANKIRTAN_VIEW_ALL, holder.viewAll.getTag().toString());
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("View All")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }
            });

//            holder.viewAllAdapter = new HomeVideoHorizontalAdapter(list.get(position).getVideos(), context);
//            holder.container.setAdapter(holder.viewAllAdapter);
        }
        else{

            holder.parentLayout.setVisibility(View.GONE);
            holder.catName.setVisibility(View.GONE);
            holder.viewAll.setVisibility(View.GONE);
//            list.remove(position);
//            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView catName, viewAll;
        RecyclerView.LayoutManager layoutManager;
        private HomeVideoHorizontalAdapter viewAllAdapter;
        private HomeVideoAdapter homeVideoAdapter;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            catName = itemView.findViewById(R.id.single_item_home_cat_name);
            viewAll = itemView.findViewById(R.id.single_item_home_view_all);
            container = itemView.findViewById(R.id.single_item_home_container_rv);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
            container.setLayoutManager(layoutManager);

            ViewCompat.setNestedScrollingEnabled(container, false);

            parentLayout.setVisibility(View.GONE);
            catName.setVisibility(View.GONE);
            viewAll.setVisibility(View.GONE);
        }
    }
}
