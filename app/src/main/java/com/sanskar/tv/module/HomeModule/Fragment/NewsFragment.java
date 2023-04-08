package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.NewsAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.MainNewsResponse;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.checkedsnackbar;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.snackbar;

/**
 * Created by appsquadz on 2/14/2018.
 */

public class NewsFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    Context context;

    public AppTextView edit, done;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    LinearLayoutManager layoutManager;
    private NetworkCall networkCall;
    RelativeLayout toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    private String lastNewsId = "";
    private RelativeLayout noDataFound;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private ImageView bannerImg;
    private AppTextView guruTxt;
    private AppTextView guruTxtHeader;
    MainNewsResponse mainNewsResponse;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_2, null);
        context = getActivity();
        initView(view);
        edit.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        if (checkedsnackbar.equalsIgnoreCase("snackbar")) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }

       /*if (HomeActivityy.playerlayout.getVisibility()==View.VISIBLE){
            HomeActivityy.playerlayout.setVisibility(View.GONE);
            Constants.SHOW_LAYOUT_AUDIO = "true";
        }*/
        return view;
    }

    private void initView(View view) {
        edit = ((HomeActivityy) context).editTV;
        done = ((HomeActivityy) context).cancle_noti;
        ((HomeActivityy) context).tv_iv.setVisibility(View.VISIBLE);
        ((HomeActivityy) context).tv_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareapp();
            }
        });
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        /*swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        swipeRefreshLayout.setVisibility(View.GONE);*/
        noDataFound = view.findViewById(R.id.no_data_found_view_all);
        bannerImg = view.findViewById(R.id.banner_img);
        guruTxt = view.findViewById(R.id.guru_txt);
        guruTxtHeader = view.findViewById(R.id.guru_txt_header);
        guruTxt.setVisibility(View.GONE);
        guruTxtHeader.setVisibility(View.GONE);
        bannerImg.setImageResource(R.mipmap.articles);

        /*LinearSnapHelper linearSnapHelper=new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(recyclerView);*/

        SnapHelper snapHelper=new PagerSnapHelper();
        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        snapHelper.attachToRecyclerView(recyclerView);


        adapter = new NewsAdapter(((HomeActivityy) context).newsList, context);
    }

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

        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
        /*if(((HomeActivityy)context).newsList.isEmpty()) {
           networkHit(true);
        }*/
        if (SharedPreference.getInstance(context).getNews() != null) {
            InitLandingPageAdapter();
        } else {
            networkHit(true);
        }
        networkHit(false);
       /* swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (((HomeActivityy) context).newsList.size() > 0)
                    ((HomeActivityy) context).newsList.clear();
                adapter.notifyDataSetChanged();
                lastNewsId = "";
                networkHit(false);

            }
        });*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading /*&& !(lastCommentId.equalsIgnoreCase(firstCommentId))*/) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            lastNewsId = ((HomeActivityy) context).newsList.get(((HomeActivityy) context).newsList.size() - 1).getId();
                            networkHit(false);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).invalidateOptionsMenu();
        toolbar.setVisibility(View.GONE);
//        ((HomeActivityy) context).handleToolBar();

        ((HomeActivityy) context).handleToolbar();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GET_NEWS)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("last_news_id", lastNewsId);
        }
        return ion;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
//        swipeRefreshLayout.setRefreshing(false);
        if (result.optBoolean("status")) {

            mainNewsResponse = new Gson().fromJson(result.toString(), MainNewsResponse.class);
            SharedPreference.getInstance(context).setNews(mainNewsResponse);
            JSONArray jsonArray = result.optJSONArray("data");

            /*if (((HomeActivityy)context).newsList.size()>0){
                ((HomeActivityy)context).newsList.clear();
            }*/
            /*if(jsonArray.length()>0){
                noDataFound.setVisibility(View.GONE);
            for(int i =0;i<jsonArray.length();i++){
                News news = new Gson().fromJson(jsonArray.opt(i).toString(),News.class);
                ((HomeActivityy)context).newsList.add(news);
            }
                loading = true;
                adapter.notifyDataSetChanged();
            }*/

            InitLandingPageAdapter();
        } else {
            if (((HomeActivityy) context).newsList.size() < 1) {
                noDataFound.setVisibility(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void InitLandingPageAdapter() {
        MainNewsResponse menuMaster = SharedPreference.getInstance(context).getNews();
        if (((HomeActivityy) context).newsList.size() > 0) {
            ((HomeActivityy) context).newsList.clear();
        }

        ((HomeActivityy) context).newsList.addAll(menuMaster.getData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
     //   swipeRefreshLayout.setRefreshing(false);
        //      ToastUtil.showDialogBox(context,jsonstring);
    }

    private void networkHit(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(NewsFragment.this, context);
            networkCall.NetworkAPICall(API.GET_NEWS, progress);
        } else {
       //     swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    /*@Override
    public void textchanged(String Query) {
        adapter.filter(Query);
    }*/
}
