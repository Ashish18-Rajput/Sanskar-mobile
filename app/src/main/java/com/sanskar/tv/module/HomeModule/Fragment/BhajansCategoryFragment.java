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
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomTouchListener;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanCategoryListAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.MainBajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.onItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.checkedsnackbar;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.snackbar;

/**
 * Created by appsquadz on 2/21/2018.
 */

public class BhajansCategoryFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    private static Context mContext;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.sanskar.tv.audioplayer.PlayNewAudio";

    public AppTextView edit,done;
    public static final String BROADCAST_TITLE_SONG="song title";
    public static final String BROADCAST_DESCRIPTION_SONG="song title";
    public static final String BROADCAST_THUMB_SONG="song title";
    public static final String BROADCAST_ISPLAYING_SONG="song playpause";
    public static final String SONG_TITLE = "songTitle";
    public static final String DISPLAY_NAME = "displayName";
    public static final String SONG_ID = "songID";
    public static final String SONG_PATH = "songPath";
    public static final String ALBUM_NAME = "albumName";
    public static final String ARTIST_NAME = "artistName";
    public static final String SONG_DURATION = "songDuration";
    public static final String SONG_POS = "songPosInList";
    public static final String SONG_PROGRESS = "songProgress";

    RecyclerView recyclerView;
    BhajanCategoryListAdapter adapter;
   RecyclerView.LayoutManager layoutManager;

   MainBajanResponse mainBajanResponse;

  // LinearLayoutManager layoutManager;
   int scrollCount=0;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    private NetworkCall networkCall;
    private int mPage = 1;
    public RelativeLayout toolbar;
    //public SearchView searchView;
    private ImageView backImg;
    private ImageView closeIV;
    private String searchContent;
    private Bhajan[] bhajanList;
    private ImageView bannerImg;
    private AppTextView guruTxt;
    private AppTextView guruTxtHeader;
    private CardView card_view;
    String thumbsong,titlesong,descsong,playerstate;

    public static boolean serviceBound = false;
    public static AudioPlayerService player;
    RelativeLayout parentlayout;
    Intent intentservice;
    HomeActivityy activityy;

    private List<ImageView> viewList;
    private String[] imgString;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    int currentPage = 0;
    public Handler handler;
    public Runnable update;
    public List<Banners> bannersList;
    private Banners[] banners;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;
    AppViewPager imageSwitcherViewPager;
    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;
   RelativeLayout intro_images_layout ;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all, null);
        Constants.IS_PLAYING_ON_CATEGORY="true";



        context = getActivity();
        initView(view);

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_NO:
                ((HomeActivityy) context).titleaudio.setTextColor(Color.parseColor("#000000"));
                ((HomeActivityy) context).descriptionaudio.setTextColor(Color.parseColor("#000000"));

                break;
        }

       /* if (((HomeActivityy) context).playerlayout.getVisibility()==View.VISIBLE){
            ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
        }*/

        //((HomeActivityy) context).playerlayout.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        if (checkedsnackbar.equalsIgnoreCase("snackbar")) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
        setSongData();
        setPlayPause();
        getVideoList(false);

        return view;
    }


    /*public void clickplaypause(){
        ((HomeActivityy) context).playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.IS_PLAYING.equals("true")){
                    ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
                    AudioPlayerService.mediaPlayer.start();
                }else if (Constants.IS_PLAYING.equals("false")){
                    ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                    AudioPlayerService.mediaPlayer.pause();
                }
            }
        });

    }*/


    public void getVideoList(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, false);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    public void setPlayPause(){
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause,intentFilter);
    }

    public void setSongData(){
        IntentFilter filter = new IntentFilter(BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Constants.IS_PLAYING_ON_CATEGORY="false";
        try {
            if (handler != null) {
                handler.removeCallbacksAndMessages(update);
                timer.cancel();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy)context).searchView.onActionViewCollapsed();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(playNewAudio);
        getActivity().unregisterReceiver(playpause);
        /*if (serviceBound){
            getActivity().unbindService(serviceConnection);
            player.stopSelf();
        }*/
    }

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

    private void initView(View view) {

        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        parentlayout = view.findViewById(R.id.parentlayout);
        card_view    = view.findViewById(R.id.card_view);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);
        imageSwitcherViewPager = view.findViewById(R.id.image_switcher_view_pager_videos);
        imageSwitcherViewPager.setPagingEnabled(true);
        update = null;
        timer = null;
        bannersList = new ArrayList<>();
        viewList = new ArrayList<>();
        edit = ((HomeActivityy) context).editTV;
        done = ((HomeActivityy) context).cancle_noti;
       /* intro_images_layout=view.findViewById(R.id.intro_images_layout);
        intro_images_layout.setVisibility(View.VISIBLE);*/
        /*toolbar = view.findViewById(R.id.toobar_view_all_videos);
        toolbar.setVisibility(View.GONE);*/
     //   bannerImg = view.findViewById(R.id.banner_img);
        guruTxt = view.findViewById(R.id.guru_txt);
        guruTxtHeader = view.findViewById(R.id.guru_txt_header);
        guruTxt.setVisibility(View.GONE);
        guruTxtHeader.setVisibility(View.GONE);
        /*bannerImg.setImageResource(R.mipmap.bhajan);
        bannerImg.setVisibility(View.VISIBLE);*/
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        adapter = new BhajanCategoryListAdapter(((HomeActivityy) context).bhajanResponseArrayList, getActivity());
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
      //MovableRecycler(recyclerView,layoutManager);

        Constants.DONT_SHOW="false";


        //searchView = toolbar.findViewById(R.id.search_view);
        //backImg = toolbar.findViewById(R.id.back);
        // closeIV = toolbar.findViewById(R.id.close_right);
//        searchView.setVisibility(View.GONE);
        //toolbar.setVisibility(View.GONE);

        if (BhajanCategoryListAdapter.recyclerView!=null) {
            BhajanCategoryListAdapter.recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    playAudio(index);
                }
            }));
        }

       /* ((HomeActivityy) context).playerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                bundle1.putSerializable("bhajans",((HomeActivityy) context).bhajanResponseArrayList.get(Constants.LIST_INDEX).getBhajan());
                //PreferencesHelper.getInstance().setValue("index",Constants.AUDIO_INDEX);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);
                ((HomeActivityy) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });*/

    }

    public void playAudio(int audioindex){
        List<Bhajan> list=Arrays.asList(bhajanList);
        Bundle bundle=new Bundle();
        if (!serviceBound){
            intentservice=new Intent(getActivity(),AudioPlayerService.class);
            intentservice.putExtra("bhajanlist",bhajanList);
            bundle.putSerializable("bhajanlist",bhajanList);
            intentservice.putExtra("position",audioindex);
            getActivity().startService(intentservice);
            getActivity().bindService(intentservice,serviceConnection,Context.BIND_AUTO_CREATE);
            // getActivity().bindService(intentservice,)
        }else{
            Intent intent=new Intent(Broadcast_PLAY_NEW_AUDIO);
            intent.putExtra("bhajanlist",bhajanList);
            bundle.putSerializable("bhajanlist",bhajanList);
            intent.putExtra("position",audioindex);
            getActivity().sendBroadcast(intent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       card_view.setVisibility(View.GONE);
       /* if (activityy.bannersList.size() > 0){
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

                indicatorLayout.addView(addSliderDotsView());
            }
            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            setViewPagerData();
        }*/
        //toolbar.setVisibility(View.GONE);
       /* if (((HomeActivityy) context).bhajanResponseArrayList.isEmpty()) {
            if (isConnectingToInternet(context)) {
                networkCall = new NetworkCall(BhajansCategoryFragment.this, context);
                networkCall.NetworkAPICall(API.GET_BHAJANS, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
            }


        }*/
        if (SharedPreference.getInstance(context).getBhajan()!=null){
            InitLandingPageAdapter();
        }
        else{
            if (isConnectingToInternet(context)) {
                networkCall = new NetworkCall(BhajansCategoryFragment.this, context);
                networkCall.NetworkAPICall(API.GET_BHAJANS, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnectingToInternet(context)) {
                    ((HomeActivityy) context).bhajanResponseArrayList.clear();
                    //   networkCall = new com.sanskar.totalbhakti.Others.NetworkNew.NetworkCall(BhajansCategoryFragment.this, context);
                    networkCall = new NetworkCall(BhajansCategoryFragment.this, context);
                    networkCall.NetworkAPICall(API.GET_BHAJANS, false);
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
            }
        });

        if (Constants.SHOW_LAYOUT_AUDIO.equals("true")){
            Constants.SHOW_LAYOUT_AUDIO = "false";
            showSongData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Constants.IS_PLAYING_ON_CATEGORY="true";
        ((HomeActivityy) context).invalidateOptionsMenu();
        //toolbar.setVisibility(View.GONE);
        ((HomeActivityy) context).handleToolbar();
        Constants.DONT_SHOW="false";
        //setSongData();

        if (Constants.CALL_RESUME.equals("true")) {
            Constants.CALL_RESUME="false";
            showSongData();
        }


    }

    public void getCategoryList() {
      //  indicatorLayout.removeAllViews();
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);
            Log.e("TAG", "getCategoryList: " + ((HomeActivityy) context).searchContent);
        } else {
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

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.GET_BHAJANS)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId());
        } else if (API_NAME.equals(API.BHAJAN_SEARCH)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("search_content", ((HomeActivityy) context).searchContent);
        }
        else if(API_NAME.equals(API.GET_SEARCH_VIDEOS)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
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
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if (jsonstring.optBoolean("status")) {


            JSONArray jsonArray = jsonstring.optJSONArray("data");
            if (apitype.equals(API.GET_BHAJANS)) {
                /*if (jsonArray.length() > 0) {
                    if (((HomeActivityy) context).bhajanResponseArrayList.size() > 0)
                        ((HomeActivityy) context).bhajanResponseArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        BhajanResponse bhajanResponse = new Gson().fromJson(jsonArray.opt(i).toString(), BhajanResponse.class);
                        ((HomeActivityy) context).bhajanResponseArrayList.add(bhajanResponse);
                    }
                    adapter.notifyDataSetChanged();
                }*/
                mainBajanResponse=new Gson().fromJson(jsonstring.toString(), MainBajanResponse.class);
                SharedPreference.getInstance(context).setBhajan(mainBajanResponse);
                InitLandingPageAdapter();

            }
            else if (apitype.equals(API.GET_SEARCH_VIDEOS)) {
                if (jsonstring.optBoolean("status")) {
                    VideoResponseNew videoResponseNew = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), VideoResponseNew.class);

               /*     if (bannersList.size() > 0) {
                        bannersList.clear();
                    }
                    banners = videoResponseNew.getBanners();
                    for (int i = 0; i < banners.length; i++) {
                        Banners banner = banners[i];
                        bannersList.add(banner);
                    }*/

//
//                    if(banners!=null && banners.length> 0){
//                        banners.
//                    }

                    /*if (bannersList.size() > 0) {
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

                    }*/
                }
            }
            else if (apitype.equals(API.BHAJAN_SEARCH)) {
                if (jsonArray.length() > 0) {
                    bhajanList = new Bhajan[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Bhajan bhajan = new Gson().fromJson(jsonArray.opt(i).toString(), Bhajan.class);
                        bhajanList[i] = bhajan;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bhajanList", bhajanList);
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
        }
    }

    public void InitLandingPageAdapter(){
        MainBajanResponse menuMaster=SharedPreference.getInstance(context).getBhajan();
        if (((HomeActivityy) context).bhajanResponseArrayList.size() > 0)
            ((HomeActivityy) context).bhajanResponseArrayList.clear();

        ((HomeActivityy) context).bhajanResponseArrayList.addAll(menuMaster.getData());
        adapter.notifyDataSetChanged();

    }
    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        //ToastUtil.showDialogBox(context, jsonstring.toString());
    }

    public void getSearchBhajan() {
        //searchContent = query;
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(BhajansCategoryFragment.this, context);
            networkCall.NetworkAPICall(API.BHAJAN_SEARCH, false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceBound){
            getActivity().unbindService(serviceConnection);
            player.stopSelf();
        }

    }*/
   /* private LinearLayout addSliderDotsView() {
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
    }*/

    private void MovableRecycler(RecyclerView rl, LinearLayoutManager llmanager) {



        llmanager=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false){
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                Log.d("checking","2");
                super.smoothScrollToPosition(recyclerView, state, position);
                Log.d("checking","3");
                try {
                    LinearSmoothScroller smoothScroller=new LinearSmoothScroller(Objects.requireNonNull(context)){

                        private static final float SPEED = 3500f;// Change this value (default=25f)

                        @Override
                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                            return  SPEED / displayMetrics.densityDpi;
                        }
                    };
                    smoothScroller.setTargetPosition(position);
                    Log.d("checking","4");
                    startSmoothScroll(smoothScroller);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        scrollCount = 0;
        final Handler handler = new Handler();
        Log.d("checking","5");
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rl.smoothScrollToPosition((scrollCount++));
                  /*  if (scrollCount == onTopHomeLiveChannelAdapter.getItemCount() -2 ) {
                        activity.homeChannelList.addAll(activity.homeChannelList);
                        onTopHomeLiveChannelAdapter.notifyDataSetChanged();
                    }*/
                handler.postDelayed(this, 2000);
                Log.d("checking","6");
            }
        };
        handler.postDelayed(runnable, 2000);

        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rl.setLayoutManager(llmanager);
        rl.setHasFixedSize(true);
        rl.setItemViewCacheSize(1000);
        rl.setDrawingCacheEnabled(true);
        rl.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //    recyclerView.setAdapter(holder.bhajanCategorySingleItemAdapter);

        Log.d("checking","7");

    }
    public void getNotificationFrag() {

        // ((HomeActivityy) activity).showNotificationFrag();

        NotificationFragment frag = new NotificationFragment();

        ((HomeActivityy) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Notification_fragment")
                .replace(R.id.container_layout_home, frag)
                .commit();

    }

}
