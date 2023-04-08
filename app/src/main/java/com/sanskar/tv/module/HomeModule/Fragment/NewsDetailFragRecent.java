package com.sanskar.tv.module.HomeModule.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.NewsDetailsAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.NewsDetailsAdapter1;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Adapter.MyAdapter2.newspos1;
import static com.sanskar.tv.module.HomeModule.Adapter.NewsAdapter.newspos;

public class NewsDetailFragRecent extends Fragment implements DiscreteScrollView.OnItemChangedListener, NetworkCall.MyNetworkCallBack {
    News news;
    MenuMasterList menuMasterList;
    int type;
    List<MenuMasterList> menuMasterLists=new ArrayList<>();
    private DiscreteScrollView itemPicker;
    private Context context;
    private TextView newsDateTV;
    private NetworkCall networkCall;
    private TextView title;
    private TextView newsDesc;
    private RelativeLayout toolbar;
    public static int position = 0;
    String pos;
    private String lastNewsId="";
    private ImageView newsImage;
    private ImageView back;
   /* private FragmentNewsDetailBinding binding;*/

    NewsDetailsAdapter newsDetailsAdapter;
    NewsDetailsAdapter1 newsDetailsAdapter1;
    Switch reading_mode;

    public NewsDetailFragRecent() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
/*        binding = FragmentNewsDetailBinding.inflate(getLayoutInflater(),
                container, false);
        return binding.getRoot();*/
        View view = inflater.inflate(R.layout.fragment_news_detail_recent, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivityy) getActivity()).handleToolbar();
         getBundleData();
        initViews(view);
        toolbar.setVisibility(View.GONE);
      // setData(news);

        if (type==1){
            lastNewsId = ((HomeActivityy)context).newsList.get(((HomeActivityy)context).newsList.size()-1).getId();

            newsDetailsAdapter= new NewsDetailsAdapter(context,((HomeActivityy)context).newsList,1);
            itemPicker.setAdapter(newsDetailsAdapter);
            if(((HomeActivityy)context).newsList.isEmpty()) {
                networkHit(true);
            }
            itemPicker.scrollToPosition(newspos);
        }
        else {
           // lastNewsId = (menuMasterLists.get(menuMasterLists.size()-1)).getId();

            newsDetailsAdapter1= new NewsDetailsAdapter1(context,menuMasterLists,1);
            itemPicker.setAdapter(newsDetailsAdapter1);
            itemPicker.scrollToPosition(newspos1);
        }

        reading_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (type==1){
                        lastNewsId = ((HomeActivityy)context).newsList.get(((HomeActivityy)context).newsList.size()-1).getId();

                        newsDetailsAdapter= new NewsDetailsAdapter(context,((HomeActivityy)context).newsList,2);
                        itemPicker.setAdapter(newsDetailsAdapter);
                        if(((HomeActivityy)context).newsList.isEmpty()) {
                            networkHit(true);
                        }
                        itemPicker.scrollToPosition(newspos);
                        newsDetailsAdapter.notifyDataSetChanged();
                    }
                    else {
                        // lastNewsId = (menuMasterLists.get(menuMasterLists.size()-1)).getId();

                        newsDetailsAdapter1= new NewsDetailsAdapter1(context,menuMasterLists,2);
                        itemPicker.setAdapter(newsDetailsAdapter1);
                        itemPicker.scrollToPosition(newspos1);
                        newsDetailsAdapter1.notifyDataSetChanged();
                    }
                }else{
                    if (type==1){
                        lastNewsId = ((HomeActivityy)context).newsList.get(((HomeActivityy)context).newsList.size()-1).getId();

                        newsDetailsAdapter= new NewsDetailsAdapter(context,((HomeActivityy)context).newsList,1);
                        itemPicker.setAdapter(newsDetailsAdapter);
                        if(((HomeActivityy)context).newsList.isEmpty()) {
                            networkHit(true);
                        }
                        itemPicker.scrollToPosition(newspos);
                        newsDetailsAdapter.notifyDataSetChanged();
                    }
                    else {
                        // lastNewsId = (menuMasterLists.get(menuMasterLists.size()-1)).getId();

                        newsDetailsAdapter1= new NewsDetailsAdapter1(context,menuMasterLists,1);
                        itemPicker.setAdapter(newsDetailsAdapter1);
                        itemPicker.scrollToPosition(newspos1);
                        newsDetailsAdapter1.notifyDataSetChanged();
                    }


                }
            }
        });

        fetchData(false);


    }

    private void fetchData(boolean progress) {
        if (isConnectingToInternet(context)){
            networkCall=new NetworkCall(this,context);
            networkCall.NetworkAPICall(API.NEWS_RELATED_VIEWS, progress);
        }
        else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }

    private void networkHit(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(NewsDetailFragRecent.this, context);
            networkCall.NetworkAPICall(API.GET_NEWS, progress);
        } else {
/*
            swipeRefreshLayout.setRefreshing(false);
*/

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();
    }

    private News getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("MasterlistNews")){
            menuMasterList= (MenuMasterList) bundle.getSerializable("MasterlistNews");
            menuMasterLists= (List<MenuMasterList>) bundle.getSerializable("MasterlistNews1");
            type=bundle.getInt("type");
        }

        if (bundle != null && bundle.containsKey(Constants.NEWS_DATA)) {
            news = (News) bundle.getSerializable(Constants.NEWS_DATA);
            type=bundle.getInt("type");
            pos=bundle.getString("position");
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
        back.setOnClickListener(onClickListener);
        itemPicker=view.findViewById(R.id.view_pager_play_bhajan1);

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);

        reading_mode=view.findViewById(R.id.reading_mode);


        // itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.1f)
                //  .setMinScale(0f)
                 /*.setMaxScale(1.05f)
                 .setMinScale(0.8f)
                 .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                 .setPivotY(Pivot.Y.BOTTOM) */// CENTER is a default one
                .build());


    }

  /*  private void setData(News news) {
        binding.setNews(news);
        newsDateTV.setText(Utils.getDate(Long.parseLong(news.getCreation_time())));
        Picasso.with(context).load(news.getImage()).placeholder(R.mipmap.landscape_placeholder).into(newsImage);
        String desc = Html.fromHtml(news.getDescription()).toString();
        newsDesc.setText(desc);
    }*/

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
        Intent i = new Intent(Intent.ACTION_SEND);
        //  i.setType("text/plain");

        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Sanskar");
        i.putExtra(Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);
        //  i.putExtra(Intent.EXTRA_STREAM,imageUri);
        // startActivity(Intent.createChooser(i,"Share via"));

        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            this.startActivity(i);
        }

    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {

    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion =null;
        if(apitype.equals(API.GET_NEWS)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                    .setMultipartParameter("last_news_id",lastNewsId);
        }
        if(apitype.equals(API.NEWS_RELATED_VIEWS)){
            if (type==1){
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                        .setMultipartParameter("news_id",news.getId());
            }
            else {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                        .setMultipartParameter("news_id",menuMasterList.getId());
            }

        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        if(result.optBoolean("status")){
            if (apitype.equalsIgnoreCase(API.GET_NEWS)){
                JSONArray jsonArray = result.optJSONArray("data");
                if(jsonArray.length()>0){
                    for(int i =0;i<jsonArray.length();i++){
                        News news = new Gson().fromJson(jsonArray.opt(i).toString(),News.class);
                        ((HomeActivityy)context).newsList.add(news);
                    }

                    if (type==1){
                        newsDetailsAdapter.notifyDataSetChanged();
                    }

                }
            } else {
                if(((HomeActivityy)context).newsList.size() < 1) {

                    if (type==1){
                        newsDetailsAdapter.notifyDataSetChanged();
                    }
                }
            }
            if (apitype.equalsIgnoreCase(API.NEWS_RELATED_VIEWS)){
                networkHit(false);
            }
            }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
      //  ToastUtil.showDialogBox(context,jsonstring); //------by sumit

    }

}
