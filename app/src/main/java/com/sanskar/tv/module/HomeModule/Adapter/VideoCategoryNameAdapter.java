package com.sanskar.tv.module.HomeModule.Adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Fragment.VideosParentFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Category;

import java.util.List;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class VideoCategoryNameAdapter extends RecyclerView.Adapter<VideoCategoryNameAdapter.ViewHolder> {

    List<Category> videoFragmentVideoesCategoryList;
   /* Context context;*/
    Activity activity;
    Fragment fragment;
    /*public static RecyclerView recyclerView;
    public static Bhajan[] bhajanlist;
*/
   /* public VideoCategoryNameAdapter(ArrayList<BhajanResponse> bhajanResponseArrayList, Context context) {
        this.bhajanResponseArrayList = bhajanResponseArrayList;
        this.context = context;
    }*/

    public VideoCategoryNameAdapter(List<Category> videoFragmentVideoesCategoryList, Activity activity, Fragment fragment) {
        this.videoFragmentVideoesCategoryList = videoFragmentVideoesCategoryList;
        this.activity = activity;
        this.fragment = fragment;
    }

    /*  public VideoCategoryNameAdapter(List<Category> videoFragmentVideoesCategoryList, Context context) {
            this.videoFragmentVideoesCategoryList = videoFragmentVideoesCategoryList;
            this.context = context;
        }
    */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.single_video_category,null));
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.video_cat_name.setText(videoFragmentVideoesCategoryList.get(position).getCategory_name());

        if(videoFragmentVideoesCategoryList.get(position).isClicked()){
            holder.video_cat_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.chat_curved_color));
            holder.video_cat_name.setTextColor(activity.getResources().getColor(R.color.white));
        }else{
            holder.video_cat_name.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.chat_curved));
            holder.video_cat_name.setTextColor(activity.getResources().getColor(R.color.video_text));
        }
        holder.video_cat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((VideosParentFragment)fragment).LoadQuestion(videoFragmentVideoesCategoryList.get(position).getId(), position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoFragmentVideoesCategoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView video_cat_name;
       // RelativeLayout single_video_cat;
        public ViewHolder(View itemView) {
            super(itemView);
            /*recyclerView = itemView.findViewById(R.id.bhajan_single_category_recycler_view);*/
            video_cat_name = itemView.findViewById(R.id.video_cat_name);
         //   single_video_cat = itemView.findViewById(R.id.single_video_cat);
           /* layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);

            categoryName.setVisibility(View.VISIBLE);
*/
            //ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        }
    }
}
