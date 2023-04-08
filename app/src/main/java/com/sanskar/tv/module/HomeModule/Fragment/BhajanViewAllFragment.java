package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanViewAllCategoryWiseAdapterRecentChange;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.module.HomeModule.Pojo.bhajanCategory.BhajanCat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 2/22/2018.
 */

public class BhajanViewAllFragment extends Fragment implements NetworkCall.MyNetworkCallBack{

    RecyclerView recyclerView;
    RelativeLayout toolbar;
    TextView title,bhajan_single_category_name;
    ImageView back,imgBanner;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    private NetworkCall networkCall;
    String category;
    String category_no;
   // BhajanViewAllCategoryWiseAdapter adapter;
    BhajanViewAllCategoryWiseAdapterRecentChange adapter;
    private int mPage = 1;
    ArrayList<Bhajan> bhajanArrayList;
    RecyclerView.LayoutManager layoutManager;
    BhajanResponse bhajanResponse = new BhajanResponse();
    //  BhajanCatData bhajanCatData = new BhajanCatData();
    // private BhajanCat[] bhajans;
    String searchContent="";
    private Bundle bundle;
    private Bhajan bhajan;
    private RelativeLayout rlBhajanCategory;
    private CardView card_view;
    CoordinatorLayout coordinatorLayout;
    public static boolean serviceBound = false;
    public BottomSheetBehavior behavior;
    public static AudioPlayerService player;
    HomeActivityy activityy;

    NestedScrollView rl_nested;

    int currentPage = 0;
    LinearLayoutManager manager;
    AppViewPager imageSwitcherViewPager;
    RelativeLayout intro_images_layout;

    private int pageSize;
    private boolean isLoading;
    private boolean loading = false;
    Boolean is_post_exit = true;
    private String lastBhajanId = "";
    private String frag = "";
    int mPageSize = 10;
    int apiType=-1;


    Boolean isScrolling=true;
    private int currentitems,totalitems,scrolloutitem;

    private Bhajan[] bhajans=new Bhajan[]{};
    private BhajanCat[] bhajanCats=new BhajanCat[]{};
    // List<BhajanCat> BhajanCat;
    List<Bhajan> bhajanList;
    Bhajan[] bhajanss = new Bhajan[]{};


    private List<ImageView> viewList;
    private String[] imgString;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    public Handler handler;
    public Runnable update;
    public List<Banners> bannersList;
    private Banners[] banners;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;

    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null)
            serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AudioPlayerService.LocalBinder binder=(AudioPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all,null);
        context = getActivity();
        bhajanArrayList = new ArrayList<>();
        getBundleData();
        initView(view);
        setSongData();
        setPlayPause();
        getVideoList(true);
        return view;
    }
    public void getVideoList(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void setPlayPause(){
        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause,intentFilter);
    }

    public void setSongData(){
        IntentFilter filter = new IntentFilter(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }

    private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("check","check1" );
            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }
            ((HomeActivityy) context).playerlayout.setVisibility(View.VISIBLE);
        }
    };

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.d("check","check1");
            ((HomeActivityy) context).titleaudio.setText(intent.getStringExtra("title"));
            ((HomeActivityy) context).descriptionaudio.setText(Html.fromHtml(intent.getStringExtra("description")).toString());

            Ion.with(getContext()).load(intent.getStringExtra("thumb")).asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            ((HomeActivityy) context).thumbaudio.setImageBitmap(result);
                        }
                    });

            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }

            ((HomeActivityy) context).playerlayout.setVisibility(View.VISIBLE);


        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(playNewAudio);
        getActivity().unregisterReceiver(playpause);
    }

    private void getBundleData() {
        if(getArguments()!=null)
        {
            bundle = getArguments();

            /*if(bundle.containsKey("data")){
                bhajanResponse = (BhajanResponse) bundle.getSerializable("data");
                category = bhajanResponse.getCategory_name();
                bhajans = bhajanResponse.getBhajan();
//                for(BhajanCat bhajan : bhajans){
//                    bhajanArrayList.add(bhajan);
//                }
            } else if(bundle.containsKey("bhajanViewAll")){
                bhajan = (BhajanCat) bundle.getSerializable("bhajanViewAll");
                category = bhajan.getArtist_id();
//                for(BhajanCat bhajan : bhajans){
//                    bhajanArrayList.add(bhajan);
//                }
            }*/

            if (bundle.containsKey("data")) {
                apiType=1;
                bhajanResponse = (BhajanResponse) bundle.getSerializable("data");
                category = bhajanResponse.getCategory_name();
                if(category.equalsIgnoreCase("Trending Bhajan"))
                {
                    category_no="1";
                }
                else if(category.equalsIgnoreCase("Trending Artist"))
                {
                    category_no="2";
                }
                else if(category.equalsIgnoreCase("All Bhajan")){
                    category_no="4";
                }
                else if(category.equalsIgnoreCase("Listen by Category")){
                    category_no="5";
                }
                else{
                    category_no="3";
                }
                bhajans = bhajanResponse.getBhajan();
//                for(BhajanCat bhajan : bhajans){
//                    bhajanArrayList.add(bhajan);
//                }
            }
            else if (bundle.containsKey("bhajanViewAllByGod")){

                apiType=3;
                bhajan = (Bhajan) bundle.getSerializable("bhajanViewAllByGod");
                category = bhajan.getArtist_id();
            }
            else if (bundle.containsKey("bhajanViewAll")) {
                apiType=2;
                bhajan = (Bhajan) bundle.getSerializable("bhajanViewAll");
                category = bhajan.getArtist_id();
//                for(BhajanCat bhajan : bhajans){
//                    bhajanArrayList.add(bhajan);
//                }
            }
        }
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        card_view    = view.findViewById(R.id.card_view);
        toolbar   = view.findViewById(R.id.toobar_view_all_videos);
        title     = toolbar.findViewById(R.id.toolbar_title);
        back      = toolbar.findViewById(R.id.back);
        imgBanner = view.findViewById(R.id.banner_img);
        rlBhajanCategory = view.findViewById(R.id.rl_bhajan_showmore);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);
        imageSwitcherViewPager = view.findViewById(R.id.image_switcher_view_pager_videos);
        imageSwitcherViewPager.setPagingEnabled(true);
        bhajan_single_category_name = view.findViewById(R.id.bhajan_single_category_name);
        update = null;
        timer = null;
        rl_nested=view.findViewById(R.id.rl_nested);
        intro_images_layout=view.findViewById(R.id.intro_images_layout);
        intro_images_layout.setVisibility(View.GONE);
        bannersList = new ArrayList<>();
        viewList = new ArrayList<>();
        if(bhajanResponse.getCategory_name()!=null){
        if(bhajanResponse.getCategory_name().equalsIgnoreCase("Listen by Category")||bhajanResponse.getCategory_name().equalsIgnoreCase("Trending Artist")){
           GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        }}
        else {
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        }
        toolbar.setVisibility(View.GONE);
        card_view.setVisibility(View.GONE);
        ViewCompat.setNestedScrollingEnabled(recyclerView,false);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });


    }

    public void  getBottomSheetBehaviour(){
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivityy)context).getSupportActionBar().hide();
        if(bundle.containsKey("data")) {
            title.setText(getResources().getString(R.string.bhajan));
        } else {
            title.setText(bhajan.getArtist_name());
        }

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_NO:
                ((HomeActivityy) context).titleaudio.setTextColor(Color.parseColor("#000000"));
                ((HomeActivityy) context).descriptionaudio.setTextColor(Color.parseColor("#000000"));

                break;
        }

        /* imgBanner.setVisibility(View.GONE);*/
        rlBhajanCategory.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        if(bhajans!=null){
            //swipeRefreshLayout.setEnabled(true);
          /*  bhajan_single_category_name.setText(bhajanResponse.getCategory_name());
            adapter = new BhajanViewAllCategoryWiseAdapter(bhajanArrayList, context, true);
            recyclerView.setAdapter(adapter);*/
            bhajan_single_category_name.setText(bhajanResponse.getCategory_name());
                networkHit2();
            //   getbhajan(true);

        }
        else{
            networkHit();
        }
      /*  swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // networkHit();
                yoyo();
            }

        });
*/

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivityy)context).onBackPressed();
            }
        });




        rl_nested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        if (is_post_exit) {
                            if(apiType==1) {
                                //  lastBhajanId = returnLastItemId(bhajanss.length);
                                // networkHit2();
                                getbhajan(true);
                            }
                            else if (apiType==2){
                                getbhajanlist(true);
                            }
                            else if(apiType==3){
                                getbhajanlistbyGod(true);
                            }
                            else {}

                            // (true);
                        } else  {
                            Toast.makeText(context, "BhajanCatData not found!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    if ((!isLoading && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 1)) {
                        isLoading = true;
                      // lastBhajanId = returnLastItemId(bhajanss.length);
                        networkHit();
                    }
                }
            }
        });
*/

        yoyo();



  /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


       @Override
       public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
           super.onScrolled(recyclerView, dx, dy);
           currentitems=manager.getChildCount();
           totalitems=manager.getItemCount();
           scrolloutitem=manager.findFirstVisibleItemPosition();

           if (loading) {
               if ((currentitems + scrolloutitem) >=  totalitems
                       && scrolloutitem >= 0
                       && totalitems >= pageSize) {
                   loading = false;
                   ++mPage;

          *//*         *//**//*if(dy>0)*//**//*
           {
           if(isScrolling &&(currentitems+scrolloutitem==totalitems))
           {
               isScrolling=false;
//               getbhajanlist(true);*//*
               networkHit();

           }}
       }
   });*/
    }

 /*   private void fetchdata() {
    }


    private String returnLastItemId(int sizeOfList) {
        int index = sizeOfList / mPageSize;
        return bhajanss[index].getId();
    }*/

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = bhajanss.length;
    }


    private void yoyo(){
        initialState();
        is_post_exit = true;
        if(apiType==2) {
            networkHit();
        }
        else if(apiType==1){
            //getbhajan(true);
            // networkHit2();
        }
        else if(apiType==3){
            networkHitforGod();
        }
        /*search_content = "";
        activityy.searchContent = "";*/
        swipeRefreshLayout.setRefreshing(false);
    }

    public void getbhajanlist(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.BHAJAN_GET_ARTIST, false);


        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }



    public void getbhajanlistbyGod(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.BHAJAN_GET_GOD, false);


        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    public void getbhajan(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_BHAJANS_CATEGORY_WISE, false);
            bhajan_single_category_name.setText(bhajanResponse.getCategory_name());

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void networkHit2() {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv
                    .Others.NetworkNew.NetworkCall(BhajanViewAllFragment.this, context);
            if (bundle.containsKey("data")) {
                networkCall.NetworkAPICall(API.GET_BHAJANS_CATEGORY_WISE, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }





    private void setBhajanData() {

    }

    private void networkHit() {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv
                    .Others.NetworkNew.NetworkCall(BhajanViewAllFragment.this, context);
            if (bundle.containsKey("bhajanViewAll")) {
                networkCall.NetworkAPICall(API.BHAJAN_GET_ARTIST, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    private void networkHitforGod() {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv
                    .Others.NetworkNew.NetworkCall(BhajanViewAllFragment.this, context);
            if (bundle.containsKey("bhajanViewAllByGod")) {
                networkCall.NetworkAPICall(API.BHAJAN_GET_GOD, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.BHAJAN_GET_ARTIST)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("artist_name", bhajan.getArtist_name() )
                    .setBodyParameter("artist_id", category)
                    .setBodyParameter("limit", String.valueOf(10))
                    .setBodyParameter("page_no", String.valueOf(mPage));

        } else if (API_NAME.equals(API.BHAJAN_GET_GOD)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("god_name", bhajan.getGod_name())
                    .setBodyParameter("artist_id", category)
                    .setBodyParameter("limit", String.valueOf(10))
                    .setBodyParameter("page_no", String.valueOf(mPage));

        }
        else if (API_NAME.equals(API.GET_BHAJANS)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("limit", String.valueOf(40))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        }

        else if (API_NAME.equals(API.GET_BHAJANS_CATEGORY_WISE)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("category",category_no)
                    .setBodyParameter("limit", String.valueOf(15))
                    .setBodyParameter("page_no", String.valueOf(mPage));;
        }
        else if (API_NAME.equals(API.BHAJAN_SEARCH)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("search_content", ((HomeActivityy) context).searchContent);
        }
        else if(API_NAME.equals(API.GET_SEARCH_VIDEOS)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("search_content", "")
                    .setMultipartParameter("video_category", "")
                    .setMultipartParameter("last_video_id", "")
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String API_NAME) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if(jsonstring.optBoolean("status")){

            if(API_NAME.equals(API.GET_BHAJANS_CATEGORY_WISE)){
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                Log.d("jsonArray","jsonArray:"+jsonArray);
                Log.d("response","jsonArray:"+jsonstring.toString());
                if (jsonArray.length() > 0) {



                    JSONArray jsonArray1=jsonArray.optJSONObject(0).optJSONArray("bhajan");
                    if(is_post_exit) {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Bhajan bhajan = new Gson().fromJson(jsonArray1.get(i).toString(), Bhajan.class);
                            //  BhajanCat bhajanCat = new Gson().fromJson(jsonArray.get(i).toString(),BhajanCat.class);
                          //  Log.d("responsgfvhe", "bhajan:" + new Gson().toJson(bhajan));
                            bhajanArrayList.add(bhajan);

                            // bhajanCats[i] = bhajanCat;
                        }
                        Log.d("responhvse", "jsonArray:" + bhajanArrayList.size());
                      //  adapter = new BhajanViewAllCategoryWiseAdapter(bhajanArrayList, context, false);
                        adapter = new BhajanViewAllCategoryWiseAdapterRecentChange(bhajanArrayList, context, false);
                        recyclerView.setAdapter(adapter);
                    }
                    else
                    {
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            Bhajan bhajan = new Gson().fromJson(jsonArray1.get(i).toString(), Bhajan.class);
                            //  BhajanCat bhajanCat = new Gson().fromJson(jsonArray.get(i).toString(),BhajanCat.class);
                            Log.d("responses", "bhajan:" + new Gson().toJson(bhajan));
                            bhajanArrayList.add(bhajan);
                            // bhajanCats[i] = bhajanCat;
                        }
                        Log.d("responsess", "jsonArray:" + new Gson().toJson(bhajanArrayList));
                        //adapter = new BhajanViewAllCategoryWiseAdapter(bhajanArrayList, context, false);
                        // recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }




            }    else if (API_NAME.equals(API.BHAJAN_SEARCH)) {
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                if (jsonArray.length() > 0) {
                    bhajans = new Bhajan[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Bhajan bhajan = new Gson().fromJson(jsonArray.opt(i).toString(), Bhajan.class);
                        bhajans[i] = bhajan;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bhajanList", bhajans);
                    SearchBhajanFrag frag = new SearchBhajanFrag();
                    frag.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJAN_SEARCH")
                            .replace(R.id.container_layout_home, frag)
                            .commit();
                } else {
                    searchContent = "";
                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                }
            }
            else if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
                if (jsonstring.optBoolean("status")) {
                    VideoResponseNew videoResponseNew = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), VideoResponseNew.class);

                    if (bannersList.size() > 0) {
                        bannersList.clear();
                    }
                    banners = videoResponseNew.getBanners();
                    for (int i = 0; i < banners.length; i++) {
                        Banners banner = banners[i];
                        bannersList.add(banner);
                    }

//
//                    if(banners!=null && banners.length> 0){
//                        banners.
//                    }

                    if (bannersList.size() > 0) {
                        if(indicatorLayout.getChildCount()>0){
                            //  indicatorLayout.removeAllViews();
                        }

                        imgString = new String[bannersList.size()];
                        for (int i = 0; i < bannersList.size(); i++) {
                            imgString[i] = bannersList.get(i).getImage();
                            indicatorLayout.addView(addSliderDotsView());
                        }
                        imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
                        imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

                        setViewPagerData();

                    }
                }
            }
            else if (API_NAME.equals(API.BHAJAN_GET_ARTIST)){
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                Log.d("ARTIST_response","jsonArray:"+jsonstring.toString());

                if (jsonArray.length() > 0) {
                    //  JSONArray jsonArray1=jsonArray.getJSONObject(0).getJSONArray("bhajan");


                    Bhajan [] newListBhajans = new Bhajan[jsonArray.length()];
                    Log.d("ARTIST_response2","jsonArray2:"+newListBhajans);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        // JSONArray jsonArray1=jsonArray.getJSONObject(0).getJSONArray("bhajan");

                        Bhajan bhajan = new Gson().fromJson(jsonArray.get(i).toString(), Bhajan.class);
                        //bhajanArrayList.add(bhajan);
                        Log.d("ARTIST_response2","jsonArray3:"+bhajan);

                        // newListBhajans[i] = bhajan;

                        bhajanArrayList.add(bhajan);

                    }
/*
                    Bhajan[] tempBhajans= new Bhajan[bhajans.length + jsonArray.length()];

                    for (int i = 0; i < bhajans.length; i++){
                        tempBhajans[i] = bhajans[i];
                    }

                    int j=0;
                    for (int i = bhajans.length; i < tempBhajans.length; i++) {


                        tempBhajans[i]=newListBhajans[j];

                        j++;

                    }


                    bhajans = tempBhajans;*/


                    adapter = new BhajanViewAllCategoryWiseAdapterRecentChange(bhajanArrayList, context, false);
                    recyclerView.setAdapter(adapter);
                    // adapter = new BhajanViewAllCategoryWiseAdapter(bhajanArrayList, context, true);
                    //  recyclerView.setAdapter(adapter);
                    //    mPage++;
                }







            }

            else if (API_NAME.equals(API.BHAJAN_GET_GOD)){
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                Log.d("God_ARTIST_response","jsonArray:"+jsonstring.toString());

                if (jsonArray.length() > 0) {
                    //  JSONArray jsonArray1=jsonArray.getJSONObject(0).getJSONArray("bhajan");


                    Bhajan [] newListBhajans = new Bhajan[jsonArray.length()];
                    Log.d("ARTIST_response2","jsonArray2:"+newListBhajans);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        // JSONArray jsonArray1=jsonArray.getJSONObject(0).getJSONArray("bhajan");

                        Bhajan bhajan = new Gson().fromJson(jsonArray.get(i).toString(), Bhajan.class);
                        //bhajanArrayList.add(bhajan);
                        Log.d("ARTIST_response2","jsonArray3:"+bhajan);

                        // newListBhajans[i] = bhajan;

                        bhajanArrayList.add(bhajan);

                    }
/*
                    Bhajan[] tempBhajans= new Bhajan[bhajans.length + jsonArray.length()];

                    for (int i = 0; i < bhajans.length; i++){
                        tempBhajans[i] = bhajans[i];
                    }

                    int j=0;
                    for (int i = bhajans.length; i < tempBhajans.length; i++) {


                        tempBhajans[i]=newListBhajans[j];

                        j++;

                    }


                    bhajans = tempBhajans;*/


                    adapter = new BhajanViewAllCategoryWiseAdapterRecentChange(bhajanArrayList, context, false);
                    recyclerView.setAdapter(adapter);
                    // adapter = new BhajanViewAllCategoryWiseAdapter(bhajanArrayList, context, true);
                    //  recyclerView.setAdapter(adapter);
                    //    mPage++;
                }







            }
        }/* else {
            if (!API.BHAJAN_GET_ARTIST.equals(API_NAME))
                ToastUtil.showDialogBox(context, jsonstring.optString("message"));
        }*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy)context).searchView.onActionViewCollapsed();
    }
    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
     //   ToastUtil.showDialogBox(context,jsonstring);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();

        if (Constants.CALL_RESUME.equals("true")) {
            Constants.CALL_RESUME="false";
            showSongData();
        }
//getbhajan(true);
       //b networkHit2();

    }
    public void getSearchBhajan() {
        //searchContent = query;
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(BhajanViewAllFragment.this, context);
            networkCall.NetworkAPICall(API.BHAJAN_SEARCH, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    private void showSongData() {
        if (!Constants.IS_PAUSEDFROMHOME.equals("true")) {

            ((HomeActivityy) context).titleaudio.setText(Constants.TITLE_SONG);
            ((HomeActivityy) context).descriptionaudio.setText(Html.fromHtml(Constants.DESCRIPTION_SONG).toString());
            Ion.with(getContext()).load(Constants.THUMB_SONG).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    ((HomeActivityy) context).thumbaudio.setImageBitmap(result);
                }
            });

            if (Constants.ISPLAYING_SONG.equals("true")) {
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            } else {
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }

            ((HomeActivityy) context).playerlayout.setVisibility(View.VISIBLE);
        }

    }

    private LinearLayout addSliderDotsView() {
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.layout_indicator, null);
        indicatorImg = linearLayout.findViewById(R.id.indicator_img);
        viewList.add(indicatorImg);
        return linearLayout;
    }

    private void setImgUnselected() {
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setImageResource(R.drawable.intro_indicator_unselected);
        }
    }

    private void setViewPagerData() {

        imageSwitcherViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setImgUnselected();
                viewList.get(position).setImageResource(R.drawable.intro_indicator_selected);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == imgString.length) {
                    currentPage = 0;
                }
                imageSwitcherViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }



    private void setBannerImg() {
        if (activityy.bannersList.size() > 0) {
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

                indicatorLayout.addView(addSliderDotsView());
            }

            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            setViewPagerData();
        }
    }


}