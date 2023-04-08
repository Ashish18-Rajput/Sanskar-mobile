package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/*import com.brightcove.player.model.Video;*/
import com.bumptech.glide.Glide;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomTouchListener;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.cast.ExpandedControlsActivity;
import com.sanskar.tv.jwPlayer.KeepScreenOnHandler;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.HomeBhajanAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.HomeGuruAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.HomeLiveChannelAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.HomeNewsAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.HomeVideoAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.HomeVideosAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Adapter.OnTopHomeLiveChannelAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.Slider_Adapter;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.onItemClickListener;
import com.sanskar.tv.utility.Utils;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class SearchHomeFrag extends Fragment implements NetworkCall.MyNetworkCallBack, View.OnClickListener, VideoPlayerEvents.OnFullscreenListener{
    private static final String TAG = "HomeFragment";
    private Context context;
    public String video_url;
    public  String Id;
    private NetworkCall networkCall;

    boolean likechannel = false;
    Channel channel;
    Channel channels;
    boolean isLiked = false;
    public String Likes_no;
    public  static String VIEWlike;
    public  static String Is_like_no;
    public static String totallike;

    RelativeLayout parent_bhajan,parent_guru,parent_video;
   /* BrightcoveExoPlayerVideoView Home_channel_play;*/
    RelativeLayout headerrel;
    ProgressBar progressvideo;
   // Video video_channel_wise;
    RelativeLayout mini_container;
    ImageView videoImage;
    public ImageView playIconIV;
    Boolean RotationFull = false;
    RelativeLayout home_fragment;
    RelativeLayout category_list;
    public  RelativeLayout floatingbutton;
    ImageView floatingActionButton;
    String video_id;
    RelativeLayout noDataFound,rl;
    private ScrollView parentScroll;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView channelRecyclerView, videoRecyclerView;
    private int mPage = 1;
    private RecyclerView guruRV;
    private RecyclerView bhajanRV;
    private RecyclerView newsRV;
    private RecyclerView livetvRV;
    private RecyclerView channel_recycler;
    private HomeVideoAdapter videoAdapter;
    private HomeGuruAdapter gurusAdapter;
    private HomeBhajanAdapter bhajanAdapter;
    private HomeNewsAdapter newsAdapter;
    private HomeLiveChannelAdapter liveChannelAdapter;
    private OnTopHomeLiveChannelAdapter onTopHomeLiveChannelAdapter;
    private LinearLayoutManager guruLM;
    private LinearLayoutManager bhajanLM;
    private LinearLayoutManager newsLM;
    private LinearLayoutManager livetvLM;
    private LinearLayoutManager ontoplivetvLM;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    private HomeActivityy activity;

    HomeLiveChannelAdapter homeLiveChannelAdapter;
    HomeVideosAdapter homeVideosAdapter;
    LinearLayout more_option;
    RelativeLayout toolbar;
    ImageView back;
    TextView title;
    ImageView  share_iv ;

    private RelativeLayout liveStreamLayout;
    private ImageView gifIV;
    public static ImageView playerView;
    private KeepScreenOnHandler keepScreenOnHandler;
    Slider_Adapter slider_adapter;
    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;
    HomeActivityy activityy;
    private String[] imgString;
    public List<Banners> bannersList;
    private Banners[] banners=new Banners[]{};
    AppViewPager imageSwitcherViewPager;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;
    private List<ImageView> viewList;
    public Handler handler;
    public Runnable update;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    int currentPage = 0;
    int count=0;
    public static String like_or_not;
    public static String sanskar_likes;

    private RelativeLayout parentViewVideo;
    private RelativeLayout parentViewGuru;
    private RelativeLayout parentViewBhajan;
    private RelativeLayout parentViewNews;
    private RelativeLayout parentViewlivetv;
    private RelativeLayout jwGifParent;
    private String  homevideourl;
    private TextView videoViewAll;
    private TextView guruViewAll;
    private TextView bhajanViewAll;
    private RelativeLayout newsViewAll;
    private TextView liveTvViewAll;
    private Boolean status=false;
    ArrayList <Bhajan>bhajansearchlist=new ArrayList<>();
    ArrayList <GuruPojo>gurusearchlist=new ArrayList<>();
    ArrayList <Channel>channelsearchlist=new ArrayList<>();
    ArrayList <Videos>videosearchlist=new ArrayList<>();


    public static boolean serviceBound = false;
    public static AudioPlayerService player;


    private HomeFragment.PlaybackLocation mLocation;
    private HomeFragment.PlaybackState mPlaybackState;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mQueueMenuItem;
    private MediaInfo mSelectedMedia;
    Videos videofile;

    RelativeLayout like_channel,Comment_channel;
    ImageView like_channel_button,Comment;
    TextView comment_number_watch_channel,like_number_watch_channel;



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


    public SearchHomeFrag() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_home, null);

        context=getActivity();

        activityy = ((HomeActivityy) context);
        initView(view);
        getBundleData();
        setSongData();
        setPlayPause();
        more_option.setVisibility(View.GONE);
        //Home_channel_play.setVisibility(View.GONE);
        videoImage.setVisibility(View.GONE);
        progressvideo.setVisibility(View.GONE);
        playIconIV.setVisibility(View.GONE);
return  view;
    }
    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("videosearchlist")) {
                videosearchlist = (ArrayList<Videos>) bundle.getSerializable("videosearchlist");
            }
            if (bundle.containsKey("bhajansearchlist")) {
                bhajansearchlist = (ArrayList<Bhajan>) bundle.getSerializable("bhajansearchlist");
            }
            if (bundle.containsKey("gurusearchlist")) {
                gurusearchlist = (ArrayList<GuruPojo>) bundle.getSerializable("gurusearchlist");
            }

            if (bundle.containsKey("channelsearchlist")) {
                channelsearchlist = (ArrayList<Channel>) bundle.getSerializable("channelsearchlist");
            }

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
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: onStopIsCAlled");
    }
    private void initView(View view) {
        if (Constants.SHOW_LAYOUT_AUDIO.equals("true")){
            Constants.SHOW_LAYOUT_AUDIO = "false";
            showSongData();
        }

        bannersList = new ArrayList<>();
        viewList = new ArrayList<>();

        noDataFound = view.findViewById(R.id.no_data_found_home);
        //rl = (RelativeLayout)view.findViewById(R.id.l2);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);
//        channelRecyclerView = view.findViewById(R.id.fragment_home_rv_live_channel);
        imageSwitcherViewPager = view.findViewById(R.id.image_switcher_view_pager_videos);
        imageSwitcherViewPager.setPagingEnabled(true);
        videoRecyclerView = view.findViewById(R.id.fragment_home_rv_videos_by_category);
        guruRV = view.findViewById(R.id.fragment_home_rv_guru);
        bhajanRV = view.findViewById(R.id.fragment_home_rv_bhajan);
        newsRV = view.findViewById(R.id.fragment_home_rv_news);
        livetvRV = view.findViewById(R.id.fragment_home_rv_livetv);
        more_option = view.findViewById(R.id.more_option);
        parent_bhajan = view.findViewById(R.id.parent_bhajan);
        parent_guru = view.findViewById(R.id.parent_guru);
        parent_video = view.findViewById(R.id.parent_videos);
        share_iv=view.findViewById(R.id.share_iv);
        mini_container=view.findViewById(R.id.mini_container);


        like_channel = view.findViewById(R.id.like_channel);
        Comment_channel = view.findViewById(R.id.Comment_channel);
        //   like_channel_button= view.findViewById(R.id.like);
        Comment = view.findViewById(R.id.Comment);
        comment_number_watch_channel = view.findViewById(R.id.comment_number_watch_channel);
        like_number_watch_channel = view.findViewById(R.id.like_number_watch_channel);

        // gifIV = view.findViewById(R.id.gif_iv);
        playerView = view.findViewById(R.id.playerView);
        /*((HomeActivityy) context).*/

      //  floatingActionButton=activityy.findViewById(R.id.floatingActionButton);
        videoImage=view.findViewById(R.id.videoImage);
        playIconIV=view.findViewById(R.id.playIconIV);

        //playerView.addOnFullscreenListener(this);
        headerrel = view.findViewById(R.id.headerrel);
       // Home_channel_play=view.findViewById(R.id.Home_channel_play);
        progressvideo = view.findViewById(R.id.progressvideo);
        home_fragment=view.findViewById(R.id.home_fragment);
        category_list=view.findViewById(R.id.category_list);
        //   channel_recycler=view.findViewById(R.id.channel_recycler);



        jwGifParent = view.findViewById(R.id.intro_images_layout);
        parentViewGuru = view.findViewById(R.id.parent_guru);
        parentViewVideo = view.findViewById(R.id.parent_videos);
        parentViewBhajan = view.findViewById(R.id.parent_bhajan);
        parentViewNews = view.findViewById(R.id.parent_news);
        parentViewlivetv = view.findViewById(R.id.parent_liveTv);


        videoViewAll = view.findViewById(R.id.video_view_all);
        guruViewAll = view.findViewById(R.id.guru_view_all);
        bhajanViewAll = view.findViewById(R.id.bhajan_view_all);
        newsViewAll = view.findViewById(R.id.news_view_all);
        liveTvViewAll = view.findViewById(R.id.liveTv_view_all);
        //  like_channel.setTag(false);

        videoViewAll.setOnClickListener(this);
        guruViewAll.setOnClickListener(this);
        bhajanViewAll.setOnClickListener(this);
        newsViewAll.setOnClickListener(this);
        liveTvViewAll.setOnClickListener(this);
      //  floatingActionButton.setOnClickListener(this);
        like_channel.setOnClickListener(this);
        share_iv.setOnClickListener(this);

/*
        if (activityy.bannersList.size() > 0){
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

               // indicatorLayout.addView(addSliderDotsView());
            }
            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            //setViewPagerData();
        }
*/
        update = null;
        timer = null;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: onViewCreated");
        // rl.setVisibility(View.GONE);
     //   activity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    /* if( (activity.homeChannelList.isEmpty() || activity.homeVideoList.isEmpty() || activity.homeBhajanList.isEmpty() ||
                activity.homeGuruList.isEmpty())){
         //   homeVisibilityVisible();
           // getHomeData();
           // getCategoryList();
            getCategoryList();

        } else {
          //getCategoryList();
        }

*/

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });


//        if (activity.isHit) {
//            if (isConnectingToInternet(context)) {
//                networkCall = new com.sanskar.sanskartv.Others.NetworkNew.NetworkCall(HomeFragment.this, context);
//                networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS, false);
//            } else {
//                swipeRefreshLayout.setRefreshing(false);
//                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//            }
//        }

//        layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
//        videoRecyclerView.setLayoutManager(layoutManager);
//        homeVideosAdapter = new HomeVideosAdapter(context, activity.homeVideosList);
//        videoRecyclerView.setAdapter(homeVideosAdapter);

        layoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        channelRecyclerView.setLayoutManager(layoutManager1);
        // homeLiveChannelAdapter = new HomeLiveChannelAdapter(context, activity.homeChannelList);
//        channelRecyclerView.setAdapter(homeLiveChannelAdapter);
        if(videosearchlist.isEmpty()){
            parentViewVideo.setVisibility(View.GONE);
        }
        else {
            parentViewVideo.setVisibility(View.VISIBLE);
        }
        videoAdapter = new HomeVideoAdapter(videosearchlist, context);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoRecyclerView.setAdapter(videoAdapter);

        bhajanAdapter = new HomeBhajanAdapter(bhajansearchlist, context);
        bhajanLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        bhajanRV.setLayoutManager(bhajanLM);
        bhajanRV.setAdapter(bhajanAdapter);

        gurusAdapter = new HomeGuruAdapter(gurusearchlist, context);
        guruLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        guruRV.setLayoutManager(guruLM);
        guruRV.setAdapter(gurusAdapter);
        /*newsAdapter = new HomeNewsAdapter(activity.homeNewsList, context);
        newsLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        newsRV.setLayoutManager(newsLM);
        newsRV.setAdapter(newsAdapter);*/

     /*   liveChannelAdapter = new HomeLiveChannelAdapter(context, activity.homeChannelList);
        livetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        livetvRV.setLayoutManager(livetvLM);
        livetvRV.setAdapter(liveChannelAdapter);
*/

        like_channel_button= view.findViewById(R.id.like);
        like_channel_button.setTag(false);
        //  setchanneldata();

        onTopHomeLiveChannelAdapter = new OnTopHomeLiveChannelAdapter(channelsearchlist,getActivity(),SearchHomeFrag.this);
        ontoplivetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        livetvRV.setLayoutManager(ontoplivetvLM);
        livetvRV.setAdapter(onTopHomeLiveChannelAdapter);


        ViewCompat.setNestedScrollingEnabled(videoRecyclerView, false);
        ViewCompat.setNestedScrollingEnabled(guruRV, false);
        ViewCompat.setNestedScrollingEnabled(bhajanRV, false);
        ViewCompat.setNestedScrollingEnabled(newsRV, false);
        ViewCompat.setNestedScrollingEnabled(livetvRV, false);
//        ViewCompat.setNestedScrollingEnabled(channelRecyclerView, false);

        /*if (activity.homeChannelList.isEmpty() || activity.homeVideoList.isEmpty() || activity.homeBhajanList.isEmpty() ||
                activity.homeGuruList.isEmpty() || activity.homeNewsList.isEmpty() ||
                TextUtils.isEmpty(activity.promotionUrl) || TextUtils.isEmpty(activity.promotionType) || activity.isLike) {
            homeVisibilityGone();
            //getHomeData();
            getCategoryList();
        } else {
            playPromotion();
            //notifyAdapters();
        }
*/

        videoRecyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                togglePlayback(view,position);
                //playVideo(view,position);

               /* Videos[] videos = new Videos[activity.homeVideoList.size()];
                for (int i = 0; i < activity.homeVideoList.size(); i++) {
                    videos[i] = activity.homeVideoList.get(i);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("video_data", videos);
                bundle.putInt("position", position);
                mSelectedMedia = (MediaInfo) Arrays.asList(videos);

                if (context instanceof HomeActivityy) {
                    if(AudioPlayerService.mediaPlayer != null){
                        try {
                            Constants.IS_PAUSEDFROMHOME = "true";
                            AudioPlayerService.mediaPlayer.stop();

                            HomeActivityy.playerlayout.setVisibility(View.GONE);
                            Constants.CALL_RESUME = "false";
                        }catch (IllegalStateException e){
                            e.printStackTrace();
                        }
                    }

                    Intent intent=new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                    intent.putExtra("video_data",videos);
                    intent.putExtra("position",position);
                    activity.sendBroadcast(intent);

                } else if (context instanceof LiveStreamJWActivity) {
                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
                }

                if (isConnectingToInternet(context)) {
                    video_id = activity.homeVideoList.get(position).getId();
                    //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }*/
            }
        }));
    }
    private void playVideo(View view,int position){
        Videos[] videos = new Videos[videosearchlist.size()];
        for (int i = 0; i < videosearchlist.size(); i++) {
            videos[i] = videosearchlist.get(i);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("video_data", videos);
        bundle.putInt("position", position);
        //mSelectedMedia = (MediaInfo) Arrays.asList(videos);
        videofile = videos[position];

        if (context instanceof HomeActivityy) {
            if(AudioPlayerService.mediaPlayer != null){
                try {
                    Constants.IS_PAUSEDFROMHOME = "true";
                    AudioPlayerService.mediaPlayer.stop();

                    HomeActivityy.playerlayout.setVisibility(View.GONE);
                    Constants.CALL_RESUME = "false";
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
            if (videos[position].getYoutube_url().equals("")) {
                Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                context.sendBroadcast(intent);
            }else{
               /* Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                activity.sendBroadcast(intent);*/

                Intent intent=new Intent(context, MainActivity.class);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }

        } else if (context instanceof LiveStreamJWActivity) {
            Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
            liveIntent.putExtras(bundle);
            ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
        }

        if (isConnectingToInternet(context)) {
            video_id = activity.homeVideoList.get(position).getId();
            //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }
    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.registerCallback(new RemoteMediaClient.Callback() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(getActivity(), ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.unregisterCallback(this);
            }
        });
       /* remoteMediaClient.load(mSelectedMedia,
                new MediaLoadOptions.Builder()
                        .setAutoplay(autoPlay)
                        .setPlayPosition(position).build());*/
    }

    private void togglePlayback(View view, int position) {

        Videos[] videos = new Videos[videosearchlist.size()];
        for (int i = 0; i <videosearchlist.size(); i++) {
            videos[i] = videosearchlist.get(i);
        }
        //mSelectedMedia = (MediaInfo) Arrays.asList(videos);
        videofile = videos[position];
        if (mCastSession!=null && mCastSession.isConnected()){
            Utils.showQueuePopup(getActivity(), view, videofile);
        }else{
            playVideo(view,position);
        }
        //break;

        /*case PLAYING:
         *//*mPlaybackState = PlaybackState.PAUSED;
                mVideoView.pause();*//*
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        playVideo(view,position);
                        *//*mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*//*
                        break;
                    case REMOTE:
                        if (mCastSession != null && mCastSession.isConnected()) {
                            Utils.showQueuePopup(getActivity(), view, mSelectedMedia);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
        // }
               break;*/
        updatePlayButton(mPlaybackState);
    }

    private void updatePlaybackLocation(HomeFragment.PlaybackLocation location) {
        mLocation = location;
       /* if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                setCoverArtStatus(null);
                startControllersTimer();
            } else {
                stopControllersTimer();
                setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            stopControllersTimer();
            setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            updateControllersVisibility(false);
        }*/
    }
    private void updatePlayButton(HomeFragment.PlaybackState state) {
       /* Log.d("TAG", "Controls: PlayBackState: " + state);
        boolean isConnected = (mCastSession != null)
                && (mCastSession.isConnected() || mCastSession.isConnecting());

        switch (state) {
            case PLAYING:

                break;
            case IDLE:

                break;
            case PAUSED:

                break;
            case BUFFERING:

                break;
            default:
                break;
        }*/
    }

    public void Loadplayer(String viewlike, String is_likes, String channel_url, String id, String image)  {
        // public void Loadplayer( String channel_url, String id, String image) {
        video_url=channel_url;
        Id=id;
        // Likes_no=viewlike;
        VIEWlike=viewlike;
        Is_like_no=is_likes;
        totallike=viewlike;
        String Channel_image=image;
        //  setChannelData();
        setchanneldata(channel_url);
      //  Home_channel_play.clear();
        // Home_channel_play.stop(mediaController1);
       /* BrightcoveMediaController mediaController2= new BrightcoveMediaController(Home_channel_play, R.layout.custom_controller_brightcove);
        Home_channel_play.setMediaController(mediaController2);*/
      //  Home_channel_play.setVisibility(View.GONE);
        progressvideo.setVisibility(View.GONE);
        playIconIV.setVisibility(View.VISIBLE);

        more_option.setVisibility(View.VISIBLE);

        videoImage.setVisibility(View.VISIBLE);

        Glide.with(getActivity()).load(Channel_image).into(videoImage);
        playIconIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Home_channel_play.clear();

                Home_channel_play.setVisibility(View.VISIBLE);*/
                playIconIV.setVisibility(View.GONE);
                progressvideo.setVisibility(View.VISIBLE);
               // video_channel_wise = Video.createVideo(channel_url);
                //  AudioPlayerService.mediaPlayer.pause();
                //   ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);

                livestreaming_code();
            }
        });

       /* Glide.with(getActivity())
                .load(image)
                .apply(new RequestOptions().placeholder(R.drawable.full_width_placeholder).error(R.drawable.full_width_placeholder))
                .into(videoImage);*/

      /*  Bitmap thumb = ThumbnailUtils.createVideoThumbnail(s,
                MediaStore.Images.Thumbnails.MINI_KIND);*/

    }


    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        SignupResponse signupResponse;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        Builders.Any.B ion = null;
        if (API_NAME.equals(API.HOME_PAGE_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getContext()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", activity.signupResponse.getId());
        } else if (API_NAME.equals(API.HOME_SEARCH_DATA)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getContext()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", activity.signupResponse.getId())
                    .setMultipartParameter("keyword", ((HomeActivityy) context).searchContent);
        }else if (API_NAME.equals(API.RECENT_VIEW)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getContext()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }
        else if(API_NAME.equals(API.LIKE_CHANNEL)|| API_NAME.equals(API.UNLIKE_CHANNEL)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getContext()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("channel_id", Id);
        }
        else if(API_NAME.equals(API.GET_SEARCH_VIDEOS)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getContext()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("search_content", "")
                    .setMultipartParameter("video_category", "")
                    .setMultipartParameter("last_video_id", "")
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        }


//        else if(API_NAME.equals(API.RECENT_VIEW)){
//            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
//                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
//                    .setMultipartParameter("type", "2")
//                    .setMultipartParameter("media_id", video_id);
//        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
       // clearHomeList();
        // homeVisibilityGone();
        swipeRefreshLayout.setRefreshing(false);
        if (result.optBoolean("status")) {
            activity.isLike = false;

           /* if (API_NAME.equals(API.HOME_PAGE_VIDEOS) || API_NAME.equals(API.HOME_SEARCH_DATA)) {
                setHomeData(result);
                notifyAdapters();
            }*/


            if (API_NAME.equals(API.HOME_PAGE_VIDEOS)) {
                setHomeData(result);
           //     notifyAdapters();
            }
            else if( API_NAME.equals(API.HOME_SEARCH_DATA)){
                setSearchHomeData(result)    ;
            //    notifyAdapters();
            }
            else if (API_NAME.equals(API.RECENT_VIEW)) {

            }
            else if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
                if (result.optBoolean("status")) {
                    VideoResponseNew videoResponseNew = new Gson().fromJson(result.optJSONObject("data").toString(), VideoResponseNew.class);
                    if (bannersList.size() > 0) {
                        bannersList.clear();
                    }

                    // banners = null;
                    banners = videoResponseNew.getBanners();
                    for (int i = 0; i < banners.length; i++) {
                        Banners banner = banners[i];
                        bannersList.add(banner);
                    }

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
                        /*getHomeData();*/
                    }
                }
            } else if (API_NAME.equals(API.LIKE_CHANNEL)) {

                LikeDislike(true);
                //clearHomeList2();

                //    channels = PreferencesHelper.getInstance().getObjectValue(Constants.HOME_CHANNEL_DATA, Channel.class);

               /* getHomeData();*/
          //      notifyAdapters();
                // onTopHomeLiveChannelAdapter.notifyDataSetChanged();

            }
            else if (API_NAME.equals(API.UNLIKE_CHANNEL)) {
                LikeDislike(false);
                // clearHomeList2();
             /*   getHomeData();*/
          //      notifyAdapters();
                //  onTopHomeLiveChannelAdapter.notifyDataSetChanged();

            }


        }
    }

    private void clearHomeList2() {
        activity.homeVideoList.clear();
        activity.homeGuruList.clear();
        activity.homeBhajanList.clear();
        activity.homeNewsList.clear();
        activity.homeChannelList.clear();
    }

    private void clearheadergone() {
        parent_bhajan.setVisibility(View.GONE);
        parent_guru.setVisibility(View.GONE);
        parent_video.setVisibility(View.GONE);

    }
    private void clearheadervisible() {
        parent_bhajan.setVisibility(View.GONE);
        parent_guru.setVisibility(View.GONE);

    }


     /*   private void LikeDislike(boolean b) {
            // like.setTag(b);
            like_channel.setTag(b);

           // int likenum = Integer.parseInt(bhajan.getLikes());
            if (b) {
                like.setImageResource(R.mipmap.audio_liked);
                bhajan.setIs_like(String.valueOf(Is_like_no + 1));
                bhajan.setIs_like("1");
            } else {
                like.setImageResource(R.mipmap.audio_like);
                bhajan.setLikes(String.valueOf(likenum));
                bhajan.setIs_like("0");
            }
             //((HomeActivityy)context).bhajanResponseArrayList.set(position, bhajans[position]);
            }

        }*/


    /*   private void setChannelRelatedData()  {

               if (!TextUtils.isEmpty(Is_like_no) && Is_like_no != null) {
                   if (Is_like_no.equals("1")) {
                       like_channel_button.setImageResource(R.mipmap.audio_liked);
                       isLiked = true;
                       like_channel_button.setTag(true);
                   } else {
                       like_channel_button.setImageResource(R.mipmap.audio_like);
                       isLiked = false;
                       like_channel_button.setTag(false);
                   }
               }



                   if (Likes_no.equals("0")) {
                       if (isLiked) {
                           like_number_watch_channel.setText("1 Like");
                       } else {
                           like_number_watch_channel.setText("0 Likes");
                       }

                   } else if (Likes_no.equals("1")) {
                       if (isLiked) {
                           like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
                       } else {
                           like_number_watch_channel.setText("1 Like");
                       }

                   } else {
                       if (isLiked) {
                           like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
                       } else {
                           like_number_watch_channel.setText((Likes_no + " Likes").toString());
                       }
                   }












       }*/
    private void LikeDislike(boolean b)  {
        channels= SharedPreference.getInstance(context).getchannel();
        String s=channels.getLikes();
       /* if (!TextUtils.isEmpty(Is_like_no) && Is_like_no != null) {
            if (Is_like_no.equals("1")) {
                like_channel_button.setImageResource(R.mipmap.audio_liked);
                isLiked = true;
                like_channel_button.setTag(true);
            } else {
                like_channel_button.setImageResource(R.mipmap.audio_like);
                isLiked = false;
                like_channel_button.setTag(false);
            }
        }



        if (Likes_no.equals("0")) {
            if (isLiked) {
                like_number_watch_channel.setText("1 Like");
            } else {
                like_number_watch_channel.setText("0 Likes");
            }

        } else if (Likes_no.equals("1")) {
            if (isLiked) {
                like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
            } else {
                like_number_watch_channel.setText("1 Like");
            }

        } else {
            if (isLiked) {
                like_number_watch_channel.setText(((Integer.parseInt(Likes_no) + 1) + " Likes").toString());
            } else {
                like_number_watch_channel.setText((Likes_no + " Likes").toString());
            }
        }
*/






        like_channel_button.setTag(b);
        int likenum = 0;
        if (like_number_watch_channel.getText().toString().equalsIgnoreCase("0 like")) {
            likenum = 0;
        } else {
            likenum = Integer.parseInt(like_number_watch_channel.getText().toString().split(" ")[0]);
        }
        if (b) {
            VIEWlike=String.valueOf(Integer.parseInt(VIEWlike)+1);

            if (likenum == 0) {
                like_number_watch_channel.setText(likenum + 1 + " like");

                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            } else {
                like_number_watch_channel.setText(likenum + 1 + " likes");
                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            }
            like_channel_button.setImageResource(R.mipmap.audio_liked);
            Is_like_no="1";
//            video.setLikes(String.valueOf(likenum + 1));
//            video.setIs_like("1");

        } else {
            VIEWlike=String.valueOf(Integer.parseInt(VIEWlike)-1);

            if (likenum == 1) {
                like_number_watch_channel.setText("0 like");
                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            } else if (likenum == 2) {
                like_number_watch_channel.setText(likenum - 1 + " like");
                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            } else {
                like_number_watch_channel.setText(likenum - 1 + " likes");
                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            }
            like_channel_button.setImageResource(R.mipmap.white_like);
//            video.setLikes(String.valueOf(likenum - 1));
//            video.setIs_like("0");
            Is_like_no="0";

        }
        //  isLike = true;




    }

 /*   private void setChannelRelatedData() {
        if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("0 like");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(VIEWlike + " like");
        } else {
            like_number_watch_channel.setText(VIEWlike + " likes");
        }

      *//*  if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("no view");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(VIEWlike + " view");
        } else {
            like_number_watch_channel.setText(VIEWlike + " views");
        }*//*

        if (Is_like_no.equals("1")) {
            like_channel_button.setImageResource(R.mipmap.audio_liked);
            like_channel_button.setTag(true);
        }
        else if(
            Is_like_no.equals("0")) {
                like_channel_button.setImageResource(R.mipmap.white_like);
                like_channel_button.setTag(false);
        }
    }*/

    private void setChannelRelatedData() {
        if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("0 like");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(totallike + " like");
        } else {
            like_number_watch_channel.setText(totallike + " likes");
        }

      /*  if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("no view");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(VIEWlike + " view");
        } else {
            like_number_watch_channel.setText(VIEWlike + " views");
        }*/

        if (Is_like_no.equals("1")) {
            like_channel_button.setImageResource(R.mipmap.audio_liked);
            like_channel_button.setTag(true);
        }
        else if(
                Is_like_no.equals("0")) {
            like_channel_button.setImageResource(R.mipmap.white_like);
            like_channel_button.setTag(false);
        }
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

    private LinearLayout addSliderDotsView() {
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.layout_indicator, null);
        indicatorImg = linearLayout.findViewById(R.id.indicator_img);
        viewList.add(indicatorImg);
        return linearLayout;
    }


    @Override
    public void onResume() {
        /*if (playerView.isActivated()) {
            playerView.stop();

        }*/

        super.onResume();
        Log.d(TAG, "onResume: onResumeIsCAlled");


     /*     if(count==0)
        {
            getCategoryList();
            count++;
        }*/
     /*   if(bannersList!=null&& bannersList.size()==0){
            getCategoryList();
        }*/
    /*    if( (activity.homeChannelList.isEmpty() || activity.homeVideoList.isEmpty() || activity.homeBhajanList.isEmpty() ||
                activity.homeGuruList.isEmpty())){
            homeVisibilityVisible();
           // getHomeData();
           // getCategoryList();
            getCategoryList();

        } else {
          //getCategoryList();
        }
*/

  //      clearHomeList();
        more_option.setVisibility(View.GONE);
        clearheadergone();
       // networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS,true);

        if (Constants.CALL_RESUME.equals("true")) {
            Constants.CALL_RESUME="false";
            showSongData();
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
    public void onPause() {

        super.onPause();
       /* Log.d(TAG, "onPause: onPauseIsCalled");
        if (handler != null) {
            handler.removeCallbacksAndMessages(update);
            try {
                timer.cancel();
            }
            catch (Exception e)
            {e.printStackTrace();}
        }
      */
    }

    @Override
    public void onDestroyView() {
        /*if (playerView.isActivated()) {
            playerView.stop();
        }*/
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: onDestroyViewIsCalled");
        getActivity().unregisterReceiver(playNewAudio);
        getActivity().unregisterReceiver(playpause);
    }

    @Override
    public void ErrorCallBack(String message, String API_NAME) {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showDialogBox(context, message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id. like_channel:
                if (isConnectingToInternet(context)) {
                    if (!Boolean.parseBoolean(like_channel_button.getTag().toString())) {
                        networkCall.NetworkAPICall(API.LIKE_CHANNEL, false);
                    } else {
                        networkCall.NetworkAPICall(API.UNLIKE_CHANNEL, false);
                    }
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;

            case R.id.floatingActionButton:
                //  Loadplayer()
                //  Loadplayer(activityy.homeChannelList.get(0).getChannel_url());
                if( video_url==null){
                    Toast.makeText(context, "Please Select any Live Channel", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(context, LiveStreamJWActivity.class);
                    intent.putExtra("livevideourl", video_url);
                    intent.putExtra("channel_id", Id);
                    intent.putExtra("from", "livetv");
                    intent.putExtra("VIEWlike",VIEWlike);
                    intent.putExtra("Is_like_no", Is_like_no);

                    context.startActivity(intent);
                }
                // Toast.makeText(context, "click applicable", Toast.LENGTH_SHORT).show();
                break;


            case R.id.video_view_all:
                if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    showHomeViewAllFrag(Constants.VIDEO_LIST_TYPE);
                } else {
                    Fragment fragment = new VideosParentFragment();
                    ((HomeActivityy) activity).showVideoFrag();
                }
                break;
            case R.id.share_iv:{
                shareapp();
                break;
            }


            case R.id.guru_view_all:
              /*  if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "guru");
                    bundle.putString("HOME_LIST_TYPE", Constants.GURU_LIST_TYPE);
                    Fragment frag = new GurusListFragment();
                    frag.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, frag).commit();
                } else {
                    activity.showGuruFrag();
                }*/
                if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    showHomeViewAllFrag(Constants.GURU_LIST_TYPE);
                } else {
                    activity.showGuruFrag();
                }

                break;

            case R.id.bhajan_view_all:
                if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    showHomeViewAllFrag(Constants.BHAJAN_LIST_TYPE);
                } else {
                    activity.showBhajanFrag();
                }
                break;

            case R.id.news_view_all:
                if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    showHomeViewAllFrag(Constants.NEWS_LIST_TYPE);
                } else {
                    activity.showNewsFrag();
                }
                break;
            case R.id.liveTv_view_all:
                if (!TextUtils.isEmpty(activity.searchContent) && activity.searchContent != null) {
                    showHomeViewAllFrag(Constants.NEWS_LIST_TYPE);
                } else {
                    activity.showLiveTvFrag();
                }
                break;
        }
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



    private void playPromotionVideo() {

        String baseUrl = API.BASE_URL_VIDEO + "/vipul/_definst_/amazons3/mp4:bhaktiappproduction/videos/";

        String url = baseUrl + activity.promotionUrl + "/playlist.m3u8";
//        }

        Log.e("URL", url);

        PlaylistItem item = new PlaylistItem(activity.homevideourl);
        //"https://s3.ap-south-1.amazonaws.com/dams-apps-production/12BD5F37-6AFF-4C45-9F98-C6925CEC8DB3-1458-000003B4C7C6745B.mp4");//("http://techslides.com/demos/sample-videos/small.mp4");
        //keepScreenOnHandler = new KeepScreenOnHandler(playerView, activity.getWindow());

        //playerView.load(item);
        //playerView.play();
    }

    private void playPromotionGif() {
        Glide.with(context)
                .load(activity.promotionUrl)
                .into(gifIV);
    }

    public void getSearchHomeVideos() {
        status=false;
        if(isConnectingToInternet(context)){
            networkCall.NetworkAPICall(API.HOME_SEARCH_DATA,true);
        }
        else{
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

  /*  public void getHomeData() {
        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }*/

    private void homeVisibilityGone() {
        jwGifParent.setVisibility(View.GONE);
        parentViewGuru.setVisibility(View.GONE);
        parentViewBhajan.setVisibility(View.GONE);
        parentViewVideo.setVisibility(View.GONE);
        parentViewNews.setVisibility(View.GONE);
        parentViewlivetv.setVisibility(View.GONE);
    }
    private void homeVisibilityVisible() {
        jwGifParent.setVisibility(View.VISIBLE);
        parentViewGuru.setVisibility(View.VISIBLE);
        parentViewBhajan.setVisibility(View.VISIBLE);
        parentViewVideo.setVisibility(View.VISIBLE);
        parentViewNews.setVisibility(View.VISIBLE);
        parentViewlivetv.setVisibility(View.GONE);
    }

    private void setHomeData(JSONObject result) {


        JSONArray jsonArrayChannel = result.optJSONArray("channel");
        JSONObject promotionJsonObj = result.optJSONObject("promotion");
        JSONArray jsonArrayHomeVideos = result.optJSONArray("video");
        JSONArray jsonArrayCategoryVideos = jsonArrayHomeVideos.optJSONObject(0).optJSONArray("videos");
        JSONArray jsonArrayHomeGuru = result.optJSONArray("guru");
        JSONArray jsonArrayHomeBhajan = result.optJSONArray("bhajan");
        JSONArray jsonArrayCategoryBhajan = jsonArrayHomeBhajan.optJSONObject(0).optJSONArray("bhajan");
        JSONArray jsonArrayHomeNews = result.optJSONArray("news");
        activity.homevideourl = result.optString("home_video");

        if (TextUtils.isEmpty(activity.searchContent) || activity.searchContent == null) {
            activity.promotionType = promotionJsonObj.optString("extension_type");
            activity.promotionUrl = promotionJsonObj.optString("video_url");
        } else {
            activity.promotionType = "";
//            activity.promotionUrl = promotionJsonObj.optString("home_video");?
        }

        // playPromotion();

        if (jsonArrayChannel != null) {
            for (int i = 0; i < jsonArrayChannel.length(); i++) {
                channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
                activity.homeChannelList.add(channel);
                activity.channellist.add(channel);

            }
            Channel.copyInstance(channel);
            SharedPreference.getInstance(context).setChannel(channel);

            if(activity.homeChannelList.size()==0){
                parentViewlivetv.setVisibility(View.GONE);

            }else{
                parentViewlivetv.setVisibility(View.GONE);
                status=true;
            }
        }

        for (int i = 0; i < jsonArrayCategoryVideos.length(); i++) {
            Videos videos = new Gson().fromJson(jsonArrayCategoryVideos.opt(i).toString(), Videos.class);
            activity.homeVideoList.add(videos);
        }
        if(activity.homeVideoList.size()==0){
            parentViewVideo.setVisibility(View.GONE);

        }else{
            parentViewVideo.setVisibility(View.VISIBLE);
            status=true;
        }
        for (int i = 0; i < jsonArrayHomeGuru.length(); i++) {
            GuruPojo guruResponse = new Gson().fromJson(jsonArrayHomeGuru.opt(i).toString(), GuruPojo.class);
            activity.homeGuruList.add(guruResponse);

        }
        if(activity.homeGuruList.size()==0){
            parentViewGuru.setVisibility(View.GONE);
        }else{
            parentViewGuru.setVisibility(View.VISIBLE);
            status=true;
        }
        for (int i = 0; i < jsonArrayCategoryBhajan.length(); i++) {
            Bhajan bhajan = new Gson().fromJson(jsonArrayCategoryBhajan.opt(i).toString(), Bhajan.class);
            activity.homeBhajanList.add(bhajan);
        }
        if(activity.homeBhajanList.size()==0){
            parentViewBhajan.setVisibility(View.GONE);

        }else{
            parentViewBhajan.setVisibility(View.VISIBLE);
            status=true;
        }
        activity.homeNewsList.clear();
        for (int i = 0; i < jsonArrayHomeNews.length(); i++) {
            News news = new Gson().fromJson(jsonArrayHomeNews.opt(i).toString(), News.class);
            activity.homeNewsList.add(news);
            parentViewNews.setVisibility(View.VISIBLE);
        }
        if(activity.homeNewsList.size()==0){
            parentViewNews.setVisibility(View.GONE);

        }else{
            parentViewNews.setVisibility(View.VISIBLE);
            status=true;
        }
        if(status){
            noDataFound.setVisibility(View.GONE);

        }else{
            noDataFound.setVisibility(View.VISIBLE);
        }
    }
    private void setSearchHomeData(JSONObject result) {


        JSONArray jsonArrayChannel = result.optJSONArray("channel");
        JSONObject promotionJsonObj = result.optJSONObject("promotion");
        JSONArray jsonArrayHomeVideos = result.optJSONArray("video");
        JSONArray jsonArrayCategoryVideos = jsonArrayHomeVideos.optJSONObject(0).optJSONArray("videos");
        JSONArray jsonArrayHomeGuru = result.optJSONArray("guru");
        JSONArray jsonArrayHomeBhajan = result.optJSONArray("bhajan");
        JSONArray jsonArrayCategoryBhajan = jsonArrayHomeBhajan.optJSONObject(0).optJSONArray("bhajan");
        JSONArray jsonArrayHomeNews = result.optJSONArray("news");
        activity.homevideourl = result.optString("home_video");

        if (TextUtils.isEmpty(activity.searchContent) || activity.searchContent == null) {
            activity.promotionType = promotionJsonObj.optString("extension_type");
            activity.promotionUrl = promotionJsonObj.optString("video_url");
        } else {
            activity.promotionType = "";
//            activity.promotionUrl = promotionJsonObj.optString("home_video");?
        }

        // playPromotion();

        if (jsonArrayChannel != null) {
            for (int i = 0; i < jsonArrayChannel.length(); i++) {
                channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
                activity.homeChannelList.add(channel);
                activity.channellist.add(channel);

            }
            Channel.copyInstance(channel);
            SharedPreference.getInstance(context).setChannel(channel);

            if(activity.homeChannelList.size()==0){
                parentViewlivetv.setVisibility(View.GONE);

            }else{
                parentViewlivetv.setVisibility(View.GONE);
                status=true;
            }
        }

        for (int i = 0; i < jsonArrayCategoryVideos.length(); i++) {
            Videos videos = new Gson().fromJson(jsonArrayCategoryVideos.opt(i).toString(), Videos.class);
            activity.homeVideoList.add(videos);
        }
        if(activity.homeVideoList.size()==0){
            parentViewVideo.setVisibility(View.GONE);

        }else{
            parentViewVideo.setVisibility(View.VISIBLE);
            status=true;
        }
        for (int i = 0; i < jsonArrayHomeGuru.length(); i++) {
            GuruPojo guruResponse = new Gson().fromJson(jsonArrayHomeGuru.opt(i).toString(), GuruPojo.class);
            activity.homeGuruList.add(guruResponse);

        }
        if(activity.homeGuruList.size()==0){
            parentViewGuru.setVisibility(View.GONE);
        }else{
            parentViewGuru.setVisibility(View.VISIBLE);
            status=true;
        }
        for (int i = 0; i < jsonArrayCategoryBhajan.length(); i++) {
            Bhajan bhajan = new Gson().fromJson(jsonArrayCategoryBhajan.opt(i).toString(), Bhajan.class);
            activity.homeBhajanList.add(bhajan);
        }
        if(activity.homeBhajanList.size()==0){
            parentViewBhajan.setVisibility(View.GONE);

        }else{
            parentViewBhajan.setVisibility(View.VISIBLE);
            status=true;
        }
        activity.homeNewsList.clear();
        for (int i = 0; i < jsonArrayHomeNews.length(); i++) {
            News news = new Gson().fromJson(jsonArrayHomeNews.opt(i).toString(), News.class);
            activity.homeNewsList.add(news);
            parentViewNews.setVisibility(View.VISIBLE);
        }
        if(activity.homeNewsList.size()==0){
            parentViewNews.setVisibility(View.GONE);

        }else{
            parentViewNews.setVisibility(View.VISIBLE);
            status=true;
        }
        if(status){
            noDataFound.setVisibility(View.GONE);

        }else{
            noDataFound.setVisibility(View.VISIBLE);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("channelList",  activity.channellist);
        bundle.putSerializable("bhajanlist", (Serializable) activity.homeBhajanList);
        bundle.putSerializable("GuruList", (Serializable) activity.homeGuruList);
        bundle.putSerializable("videoList", (Serializable) activity.homeVideoList);
        SearchHomeFrag frag = new SearchHomeFrag();
        frag.setArguments(bundle);
        ((HomeActivityy) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("HOME_SEARCH")
                .replace(R.id.container_layout_home, frag)
                .commit();
    }



    private void showHomeViewAllFrag(String listType) {
        Bundle bundle = new Bundle();
        bundle.putString("HOME_LIST_TYPE", listType);
        HomeViewAllFrag frag = new HomeViewAllFrag();
        frag.setArguments(bundle);
        ((HomeActivityy) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout_home, frag).commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFullscreen(boolean state) {
        ActionBar actionBar = ((HomeActivityy) activity).getSupportActionBar();
        if (actionBar != null) {
            if (state) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }


    private void livestreaming_code() {
      /*  BrightcoveMediaController mediaController1 = new BrightcoveMediaController(Home_channel_play, R.layout.custom_controller_brightcove);
        Home_channel_play.setMediaController(mediaController1);

        Home_channel_play.clear();
        Home_channel_play.add(video_channel_wise);
        Home_channel_play.start();
        Log.d("rune","runnin");*/

      /*  Home_channel_play.getEventEmitter().on(EventType.BUFFERING_STARTED, new EventListener() {
            @Override
            public void processEvent(Event event) {

                progressvideo.setVisibility(View.VISIBLE);
            }
        });
        Home_channel_play.getEventEmitter().on(EventType.BUFFERING_COMPLETED, new EventListener() {
            @Override
            public void processEvent(Event event) {

                progressvideo.setVisibility(View.GONE);
            }
        });*/
        /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , RelativeLayout.LayoutParams.MATCH_PARENT);
      //  params.weight = 5;
        // headerrel.setLayoutParams(params);
        headerrel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 600));
        Home_channel_play.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));*/
        Log.d("rune","running2a");

       /* Home_channel_play.getEventEmitter().on(EventType.DID_ENTER_FULL_SCREEN, new EventListener() {
            @Override
            public void processEvent(Event event) {
                RotationFull = true;
                //   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                Log.d("run","running1");
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                Log.d("run","running2");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT
                );

                FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);



              *//*  RelativeLayout.LayoutParams params =  (RelativeLayout.LayoutParams)headerrel.getLayoutParams();
                params.width=params.MATCH_PARENT;
                params.height=params.MATCH_PARENT;*//*




             *//*   FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);*//*
                // params.weight = 5;
                // headerrel.setLayoutParams(params);
                headerrel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
                Log.d("run","running3");
                //    Home_channel_play.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

                Home_channel_play.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                Log.d("run","running4");
                // params.weight = 1;
                //   headerrel.setLayoutParams(params);
                //  playerView.setLayoutParams(params1);
                //toolbar.setVisibility(View.GONE);
                Log.d("run","running5");
                param2.setMargins(0, 0, 0, 0);
                home_fragment.setLayoutParams(param2);
                Log.d("run","running6");
                home_fragment.requestLayout();
                Log.d("run","running7");
                category_list.setVisibility(View.GONE);
                activityy.bottomNavigationView.setVisibility(View.GONE);
                //activityy.floatingbutton.setVisibility(View.GONE);
                activityy.toolbar.setVisibility(View.GONE);
                livetvRV.setVisibility(View.GONE);
                guruRV.setVisibility(View.GONE);
                bhajanRV.setVisibility(View.GONE);
                videoImage.setVisibility(View.GONE);
                parent_bhajan.setVisibility(View.GONE);
                parent_guru.setVisibility(View.GONE);
                parent_video.setVisibility(View.GONE);
                videoRecyclerView.setVisibility(View.GONE);


                // swipeRefreshLayout.setVisibility(View.GONE);

            }
        });
        Home_channel_play.getEventEmitter().on(EventType.DID_EXIT_FULL_SCREEN, new EventListener() {
            @Override
            public void processEvent(Event event) {
                // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //      if (Rotation) {
                //          setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                RotationFull = false;
                //      }
                //      else {
                RotationFull = false;

                //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT
                );
                FrameLayout.LayoutParams param2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);
                //  params.weight = 5;
                // headerrel.setLayoutParams(params);

                headerrel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 500));
                Home_channel_play.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                //   param2.setMargins(0, 0, 0, 0);
                home_fragment.setLayoutParams(param2);
                home_fragment.requestLayout();
                // swipeRefreshLayout.setVisibility(View.VISIBLE);
                //   linearbottom.setVisibility(View.VISIBLE);
                // linearmid.setVisibility(View.VISIBLE);
                //   }
                category_list.setVisibility(View.VISIBLE);
                livetvRV.setVisibility(View.VISIBLE);
                guruRV.setVisibility(View.VISIBLE);
                activityy.bottomNavigationView.setVisibility(View.VISIBLE);
                //activityy.floatingbutton.setVisibility(View.VISIBLE);
                activityy.toolbar.setVisibility(View.VISIBLE);
                bhajanRV.setVisibility(View.VISIBLE);

                //videoImage.setVisibility(View.VISIBLE);
                parent_bhajan.setVisibility(View.VISIBLE);
                parent_guru.setVisibility(View.VISIBLE);
                parent_video.setVisibility(View.VISIBLE);
                videoRecyclerView.setVisibility(View.VISIBLE);
            }
        });*/
    }
    private void setchanneldata(String channel_url) {
        if ( activity.homeChannelList != null) {
            setChannelRelatedData();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){


    }
}
