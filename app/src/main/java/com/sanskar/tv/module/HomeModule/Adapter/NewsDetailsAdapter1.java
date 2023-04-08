package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsDetailsAdapter1 extends RecyclerView.Adapter<NewsDetailsAdapter1.NewsDetailsHolder> {
    Context context;
    List<MenuMasterList> newsArrayList;


    int reading_mode;

    public NewsDetailsAdapter1(Context context, List<MenuMasterList> newsArrayList,int reading_mode) {
        this.newsArrayList = newsArrayList;
        this.context = context;
        this.reading_mode=reading_mode;
    }


    @NonNull
    @Override
    public NewsDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_item_bhajan_news_details, null);
        return new NewsDetailsAdapter1.NewsDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDetailsHolder newsDetailsHolder, int i) {
        newsDetailsHolder.news_heading_tv.setText(newsArrayList.get(i).getTitle());

        if (reading_mode==1){
            newsDetailsHolder.news_image.setVisibility(View.VISIBLE);
            newsDetailsHolder.news_heading_tv.setTextSize(18);
            newsDetailsHolder.news_desc_tv.setTextSize(16);
           // newsDetailsHolder.ll_news.setPadding(0,710,0,0);
        }
        if (reading_mode==2){
            newsDetailsHolder.news_image.setVisibility(View.GONE);
            newsDetailsHolder.news_heading_tv.setTextSize(20);
            newsDetailsHolder.news_desc_tv.setTextSize(19);
            //newsDetailsHolder.ll_news.setPadding(0,910,0,0);
        }

        newsDetailsHolder.news_heading_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bhajan_url = SharedPreference.getInstance(context).getString(Constants.WEB_VIEW_NEWS);
                //String bhajan_url="http://app.sanskargroup.in/sanskar_staging/index.php/web/Home/view_news_by_id?id=";
                // String playStoreLink = "https://play.google.com/store/apps/details?id=" + context.getPackageName();

                Intent i2 = new Intent(Intent.ACTION_SEND);


                i2.setType("text/plain");
                i2.putExtra(Intent.EXTRA_SUBJECT, "Sanskar");
                /* i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);*/
                i2.putExtra(Intent.EXTRA_TEXT, "Read News " + bhajan_url + newsArrayList.get(i).getId());
                //  i.putExtra(Intent.EXTRA_STREAM,imageUri);
                // startActivity(Intent.createChooser(i,"Share via"));

                if (i2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(i2);
                }
            }
        });

        newsDetailsHolder.date_tv.setText(Utils.getDate(Long.parseLong(newsArrayList.get(i).getCreationTime())));
        String desc = String.valueOf(Html.fromHtml(newsArrayList.get(i).getDescription()));
        newsDetailsHolder.news_desc_tv.setText(desc);
        Picasso.with(context).load(newsArrayList.get(i).getImage()).placeholder(R.mipmap.landscape_placeholder).into(newsDetailsHolder.news_image);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class NewsDetailsHolder extends RecyclerView.ViewHolder {
        TextView news_heading_tv, date_tv, news_desc_tv;
        ImageView news_image;
        LinearLayout ll_news;

        public NewsDetailsHolder(@NonNull View itemView) {
            super(itemView);
            news_heading_tv = itemView.findViewById(R.id.news_heading_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            news_desc_tv = itemView.findViewById(R.id.news_desc_tv);
            news_image = itemView.findViewById(R.id.news_image);
            ll_news=itemView.findViewById(R.id.ll_news);
        }
    }
}
