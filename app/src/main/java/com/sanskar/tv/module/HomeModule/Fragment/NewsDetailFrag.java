package com.sanskar.tv.module.HomeModule.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.R;
import com.squareup.picasso.Picasso;

public class NewsDetailFrag extends Fragment {

    private Context context;
    private TextView newsDateTV;
    private TextView title,news_heading_tv;
    private TextView newsDesc;
    private RelativeLayout toolbar;
    private ImageView newsImage;
    private ImageView back;

    public NewsDetailFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_news_detail,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivityy) getActivity()).handleToolbar();
        News news = getBundleData();
        initViews(view);
        toolbar.setVisibility(View.GONE);
        setData(news);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();
    }

    private News getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.NEWS_DATA)) {
            News news = (News) bundle.getSerializable(Constants.NEWS_DATA);
            return news;
        } else {
            return null;
        }
    }

    private void initViews(View view) {
        context = getActivity();
        ((HomeActivityy) context).tv_iv.setVisibility(View.VISIBLE);
        ((HomeActivityy) context).tv_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareapp();
            }
        });
        newsDateTV = view.findViewById(R.id.date_tv);
        newsDesc = view.findViewById(R.id.news_desc_tv);
        newsImage = view.findViewById(R.id.news_image);
        toolbar = view.findViewById(R.id.toolbar_guru);
        title = toolbar.findViewById(R.id.toolbar_title);
        back = toolbar.findViewById(R.id.back);
        news_heading_tv = toolbar.findViewById(R.id.news_heading_tv);
        back.setOnClickListener(onClickListener);
    }

    private void setData(News news) {
        //binding.setNews(news);
        news_heading_tv.setText(news.getTitle());
        newsDateTV.setText(Utils.getDate(Long.parseLong(news.getCreation_time())));
        Picasso.with(context).load(news.getImage()).placeholder(R.mipmap.landscape_placeholder).into(newsImage);
        String desc = Html.fromHtml(news.getDescription()).toString();
        newsDesc.setText(desc);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.back) {
                ((HomeActivityy) context).onBackPressed();
            }
        }
    };
    private void shareapp() {
/*
            String playStoreLink = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
            Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/mipmap/" + "ic_launcher_round");
            Intent i = new Intent(android.content.Intent.ACTION_SEND);
            i.setType("text/plain");
            //i.setType("image/*");

            i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar App");
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);
            i.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(i, "Share via"));*/

        String playStoreLink = "https://play.google.com/store/apps/details?id=" +getActivity().getPackageName();
        //  Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/mipmap/" + "ic_launcher_round");
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        //  i.setType("text/plain");

        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);
        //  i.putExtra(Intent.EXTRA_STREAM,imageUri);
        // startActivity(Intent.createChooser(i,"Share via"));

        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            this.startActivity(i);
        }

    }
}
