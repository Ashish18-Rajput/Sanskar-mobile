package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFragRecent;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.getDate;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    ArrayList<News> newsArrayList;
    Context context;
    List<News> alphabeticalcharlist;
    public static  int newspos;

    public NewsAdapter(ArrayList<News> newsArrayList, Context context) {
        this.newsArrayList = newsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_news,null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.channelName.setText(newsArrayList.get(position).getTitle());
        newsArrayList.get(position).getDescription().length();
        String desc = String.valueOf(Html.fromHtml(newsArrayList.get(position).getDescription()));

        holder.description.setText(desc);
        holder.description.setTrimLines(10);

        holder.date.setText(getDate(Long.parseLong(newsArrayList.get(position).getPublished_date())));
        if (newsArrayList.get(position).getViews_count().equals("0")) {
            holder.viewsNumber.setText("no view");
        } else if (newsArrayList.get(position).getViews_count().equals("1")) {
            holder.viewsNumber.setText("1 view");
        } else {
            holder.viewsNumber.setText(String.format(newsArrayList.get(position).getViews_count()) + " views");
        }

       /* Ion.with(context).load(newsArrayList.get(position).getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                holder.artistImage.setImageBitmap(result);
            }
        });*/

        Glide.with(context)
                .load(newsArrayList.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(holder.artistImage);


        // Picasso.with(context).load(newsArrayList.get(position).getImage())
        // .placeholder(R.mipmap.profile_pic_default)
        // .into(holder.artistImage);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.NEWS_DATA, newsArrayList.get(position));
                bundle.putInt("type",1);
                newspos=position;
                Log.d("newspos", String.valueOf(position));
                NewsDetailFragRecent fragment = new NewsDetailFragRecent();
                fragment.setArguments(bundle);
                ((HomeActivityy)context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(Constants.READ_NEWS)
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }
  /*  public void filter(String charText) {
        newsArrayList.clear();
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            newsArrayList.addAll(alphabeticalcharlist);
        } else {
            for (News wp : alphabeticalcharlist) {
                if (wp.getId().toLowerCase(Locale.getDefault()).contains(charText)) {
                    newsArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView artistImage;
        TextView channelName, date, viewsNumber;
        ReadMoreTextView description;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }
}
