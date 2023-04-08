package com.sanskar.tv.module.HomeModule.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.EventLogger;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.LiveTvAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.MainChannelData;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.TrackSelectionHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class LiveFragment extends Fragment implements NetworkCall.MyNetworkCallBack {
    public ArrayList<MenuMasterList> ChannelList = new ArrayList<>();
    public List<MenuMasterList> homeChannelList1 = new ArrayList<>();
    public RecyclerView recyclerView_Live;
    public static PlayerView playerView;
    public static ProgressBar progressBar;
    public static ImageView full_Screen;
    public static ImageView option_menu;
    Handler mainHandler;
    public static SimpleExoPlayer simpleExoPlayer,simpleExoPlayerAds, simpleExoPlayerAds1;
    public static DefaultTrackSelector trackSelector;
    TrackSelector selector,selector1;
    DefaultTrackSelector.Parameters trackParameters;
    public static TrackSelectionHelper trackSelectionHelper;
    static DefaultHttpDataSourceFactory factory;
    DataSource.Factory dataSourceFactory;
    private TrackGroupArray lastSeenTrackGroupArray;
    EventLogger eventLogger;
    BandwidthMeter bandwidthMeter;
    LoadControl loadControl;
    static HlsMediaSource hlsMediaSource;
    static RelativeLayout root_container;
    ImageView imageView, exo_nextVideo;
    static Activity activity;
    static HomeActivityy homeActivityy;
    public static String Is_premium = "";
    public static MenuMasterList[] bhajanforImagefromHome;

    public static Context context;

    static long countAds = 5;
    public static boolean is_pause = true;
    public static boolean is_pause_1 = true;
    Fragment fragment;

    static NetworkCall networkCall;
    static MenuMasterList[] bhajanNew;

    MainChannelData mainChannelData;
    static AdvertisementAds[] liveTvAds;
    public static SimpleExoPlayerView playerViewHome;
    public static TextView quality;
    public static String advertisement_id = "", time_slot_id = "";
    public static MediaSource mediaSource1;
    public static PlayerView playerViewAdsHome;
    public static int adsType = -1;
    public static int adsType_1 = -1;
    public static int adsType_2 = -1;
    public static boolean isAdsInBufferState = false;
    public static int timePlay = 0;
    public static String channel_url = "", advertisement_status = "";
    public static TextView skip_ads;

    public static long gap_Duration_live_channel = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        homeActivityy = (HomeActivityy) activity;
        fragment = getParentFragment();
    }

    public static void setVideos(Uri uri, String Id,MenuMasterList radioSatsang) {
        if (homeActivityy.homeBhajanListNew.size() > 0) {
            homeActivityy.homeBhajanListNew.clear();
        }
        homeActivityy.homeBhajanListNew.add(radioSatsang);
        bhajanNew = new MenuMasterList[1];
        bhajanNew[0] = radioSatsang;
        if (!Id.equalsIgnoreCase("30")) {

            homeActivityy.plays = 1;
            progressBar.setVisibility(View.VISIBLE);

            //
            // playAds(liveTvAds[],)

            if (Is_premium.equalsIgnoreCase("0")) {
                if (SharedPreference.getInstance(context).getLiveTvAds() != null && SharedPreference.getInstance(context).getLiveTvAds().length!=0) {
                    liveTvAds = SharedPreference.getInstance(context).getLiveTvAds();
                    if (Networkconstants.adsCount > liveTvAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    playVideo(uri);
                    //playAds(liveTvAds[Networkconstants.adsCount], uri);
                } else {
                    playVideo(uri);
                }
            } else {
                playVideo(uri);
            }
        }
        else {
            Bundle bundle = new Bundle();
            if (!PlayListFrag.serviceBound) {
                if (!DownloadsFragment.serviceBound) {
                    if (!BhajanViewAllFragment.serviceBound) {
                        if (!HomeFragment1.serviceBound) {
                            if (!BhajansCategoryFragment.serviceBound) {
                                Constants.AUDIO_ACTIVE_LIST = "home";
                                Intent intentservice = new Intent(context, AudioPlayerService.class);
                                intentservice.putExtra("bhajanarray1", bhajanNew);
                                intentservice.putExtra("status", "radio");
                                intentservice.putExtra("redirect", Constants.AUDIO_ACTIVE_LIST);
                                PreferencesHelper.getInstance().setValue("index", 0);

                                context.startService(intentservice);
                                context.bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                                Constants.IS_PLAYING_ON_CATEGORY = "false";
                                // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                                Constants.LIST_INDEX = 0;
                                bhajanforImagefromHome = bhajanNew;
                                bundle.putInt("position", 0);
                                ((HomeActivityy) context).back1 = "";
                                bundle.putString("status", "radio");
                                Networkconstants.bhajan_name = "radio";
                                bundle.putSerializable("bhajans1", bhajanNew);
                                Fragment fragment = new BhajanPlayFragment();
                                fragment.setArguments(bundle);
                                ((HomeActivityy) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack("BHAJANS")
                                        .replace(R.id.container_layout_home, fragment)
                                        .commit();
                            } else {
                                Constants.AUDIO_ACTIVE_LIST = "home";
                                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                                intent.putExtra("bhajanlist1", bhajanNew);
                                bundle.putSerializable("bhajanlist1", bhajanNew);
                                Constants.LIST_INDEX = 0;

                                bhajanforImagefromHome = bhajanNew;

                                PreferencesHelper.getInstance().setValue("index", 0);
                                intent.putExtra("position", 0);
                                context.sendBroadcast(intent);

                                bundle.putInt("position", 0);
                                bundle.putSerializable("bhajans1", bhajanNew);
                                Fragment fragment = new BhajanPlayFragment();
                                fragment.setArguments(bundle);
                                ((HomeActivityy) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack("BHAJANS")
                                        .replace(R.id.container_layout_home, fragment)
                                        .commit();
                            }

                        } else {
                            Constants.AUDIO_ACTIVE_LIST = "home";
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("bhajanlist1", bhajanNew);
                            bundle.putSerializable("bhajanlist1", bhajanNew);
                            Constants.LIST_INDEX = 0;
                            bhajanforImagefromHome = bhajanNew;
                            PreferencesHelper.getInstance().setValue("index", 0);
                            intent.putExtra("position", 0);
                            context.sendBroadcast(intent);

                            bundle.putInt("position", 0);
                            bundle.putSerializable("bhajans1", bhajanNew);
                            Fragment fragment = new BhajanPlayFragment();
                            fragment.setArguments(bundle);
                            ((HomeActivityy) context)
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack("BHAJANS")
                                    .replace(R.id.container_layout_home, fragment)
                                    .commit();
                        }
                    } else {
                        Constants.AUDIO_ACTIVE_LIST = "home";
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("bhajanlist1", bhajanNew);
                        bundle.putSerializable("bhajanlist1", bhajanNew);
                        Constants.LIST_INDEX = 0;
                        bhajanforImagefromHome = bhajanNew;
                        PreferencesHelper.getInstance().setValue("index", 0);
                        intent.putExtra("position", 0);
                        context.sendBroadcast(intent);

                        bundle.putInt("position", 0);
                        bundle.putSerializable("bhajans1", bhajanNew);
                        Fragment fragment = new BhajanPlayFragment();
                        fragment.setArguments(bundle);
                        ((HomeActivityy) context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("BHAJANS")
                                .replace(R.id.container_layout_home, fragment)
                                .commit();
                    }
                } else {
                    Constants.AUDIO_ACTIVE_LIST = "home";
                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("bhajanlist1", bhajanNew);
                    bundle.putSerializable("bhajanlist1", bhajanNew);
                    Constants.LIST_INDEX = 0;
                    bhajanforImagefromHome = bhajanNew;
                    PreferencesHelper.getInstance().setValue("index", 0);
                    intent.putExtra("position", 0);
                    context.sendBroadcast(intent);

                    bundle.putInt("position", 0);
                    bundle.putSerializable("bhajans1", bhajanNew);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
                            .commit();
                }
            } else {
                Constants.AUDIO_ACTIVE_LIST = "home";
                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                intent.putExtra("bhajanlist1", bhajanNew);
                bundle.putSerializable("bhajanlist1", bhajanNew);
                Constants.LIST_INDEX = 0;
                bhajanforImagefromHome = bhajanNew;
                PreferencesHelper.getInstance().setValue("index", 0);
                intent.putExtra("position", 0);
                context.sendBroadcast(intent);

                bundle.putInt("position", 0);
                bundle.putSerializable("bhajans1", bhajanNew);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle);
                ((HomeActivityy) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        }
        //playVideo(uri);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live, container, false);
        //getBundleData();


        recyclerView_Live = view.findViewById(R.id.recyclerView_Live);

        imageView = view.findViewById(R.id.btn_close);
        networkCall = new NetworkCall(LiveFragment.this, context);
        root_container = view.findViewById(R.id.root_container);
        if (SharedPreference.getInstance(context).getChannelData()!=null){
            InitAdapter();
        }else {
            fetchData(true);
        }
        gap_Duration_live_channel=homeActivityy.gap_Duration_live_channel;
        playerView = view.findViewById(R.id.player_view_exo);
        playerViewAdsHome=view.findViewById(R.id.player_view_exo_ads);
        full_Screen = playerView.findViewById(R.id.bt_full);
        skip_ads =playerViewAdsHome.findViewById(R.id.skip_ads);
        option_menu = playerView.findViewById(R.id.option_menu);
        progressBar = view.findViewById(R.id.progress_bar);
        exo_nextVideo = view.findViewById(R.id.exo_nextVideo);
        exo_nextVideo.setVisibility(View.GONE);
        mainHandler = new Handler();
        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();
        quality = playerView.findViewById(R.id.quality);

        trackParameters = new DefaultTrackSelector.ParametersBuilder().build();

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        trackSelector.setParameters(trackParameters);
        selector= new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );
        selector1= new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );
        trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);

        dataSourceFactory = new DefaultDataSourceFactory(context, defaultBandwidthMeter, buildHttpDataSourceFactory(defaultBandwidthMeter));
        lastSeenTrackGroupArray = null;
        eventLogger = new EventLogger(trackSelector);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getContext(), trackSelector, loadControl
        );

        simpleExoPlayerAds = ExoPlayerFactory.newSimpleInstance(
                getContext(), selector, loadControl
        );
        simpleExoPlayerAds1 = ExoPlayerFactory.newSimpleInstance(
                getContext(), selector1, loadControl
        );
        factory = new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );

        imageView.setOnClickListener(v -> {
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer.release();
            if (simpleExoPlayerAds!=null)
            simpleExoPlayerAds.setPlayWhenReady(false);
            simpleExoPlayerAds.release();
            if (simpleExoPlayerAds1!=null)
            simpleExoPlayerAds1.setPlayWhenReady(false);
            simpleExoPlayerAds1.release();
            root_container.setVisibility(View.GONE);
        });

        return view;

    }
    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("exoplayer_video", bandwidthMeter);
    }

    private void fetchData(Boolean load) {
        networkCall = new NetworkCall(this, context);
        networkCall.NetworkAPICall(API.CHANNEL_LIST, load);
    }

    public void getNotificationFrag() {

        // ((HomeActivityy) activity).showNotificationFrag();

        NotificationFragment frag = new NotificationFragment();
        //NotificationFragmentNew frag = new NotificationFragmentNew();

        ((HomeActivityy) activity)
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Notification_fragment")
                .replace(R.id.container_layout_home, frag)
                .commit();

    }
    public static void updateAPPAdvertisementCounter(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.UPDATE_APP_ADVERTISEMENT_COUNTER, b);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    public static void playads1(AdvertisementAds liveTvAd) {
        adsType = 3;
        adsType_1 = 3;
        ++Networkconstants.adsCount;
        simpleExoPlayer.setPlayWhenReady(false);
        playerView.setVisibility(View.GONE);
        if (liveTvAd.getSkip().equalsIgnoreCase("0")){
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")){
            skip_ads.setVisibility(View.VISIBLE);
        }
        playerViewAdsHome.setVisibility(View.VISIBLE);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        advertisement_id = liveTvAd.getId();
        time_slot_id = liveTvAd.getTime_slot_id();


        Uri uri1 = Uri.parse(liveTvAd.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        mediaSource1 = new ExtractorMediaSource(uri1
                , factory, extractorsFactory, null, null);

        playerViewAdsHome.setPlayer(simpleExoPlayerAds1);
        //keep screen on
        playerViewAdsHome.setKeepScreenOn(true);

        simpleExoPlayerAds1.prepare(mediaSource1);
        countAds = 5;
        skip_ads.setText("Skip ads in 5");


        simpleExoPlayerAds1.setPlayWhenReady(true);

        simpleExoPlayerAds1.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    progressBar.setVisibility(View.GONE);
                }
                if (playbackState == ExoPlayer.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    if (adsType_1 == 3) {
                        if (liveTvAd.getSkip().equalsIgnoreCase("0")){
                            skip_ads.setVisibility(View.GONE);
                        }

                        if (liveTvAd.getSkip().equalsIgnoreCase("1")){
                            skip_ads.setVisibility(View.VISIBLE);
                        }
                        updatetimer(5000);
                        adsType_1++;
                    }
                    if (adsType_2 == 1) {
                        isAdsInBufferState = false;
                        updatetimer(countAds * 1000);
                    }
                }
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    adsType_2 = 1;
                    isAdsInBufferState = true;

                    // updatetimer(countAds*1000);
                }
                if (playbackState == ExoPlayer.STATE_ENDED) {

                    progressBar.setVisibility(View.GONE);
                    playerViewAdsHome.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    // simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayerAds1.setPlayWhenReady(false);
                    // simpleExoPlayerAds.release();
                    simpleExoPlayer.setPlayWhenReady(true);

                    countAds = 5;
                    if (adsType == 3) {
                        Log.d("shantanu", "playads1");
                        adsType++;
                        prepareNextAds();
                        advertisement_status = "2";
                        updateAPPAdvertisementCounter(false);
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }
    public static void playAds(AdvertisementAds liveTvAd, Uri uri) {
        adsType = 1;
        adsType_1 = 1;
        ++Networkconstants.adsCount;
        root_container.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        playerViewAdsHome.setVisibility(View.VISIBLE);

        if (liveTvAd.getSkip().equalsIgnoreCase("0")){
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")){
            skip_ads.setVisibility(View.VISIBLE);
        }
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        advertisement_id = liveTvAd.getId();
        time_slot_id = liveTvAd.getTime_slot_id();

        Uri uri1 = Uri.parse(liveTvAd.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        mediaSource1 = new ExtractorMediaSource(uri1
                , factory, extractorsFactory, null, null);

        playerViewAdsHome.setPlayer(simpleExoPlayerAds);
        //keep screen on
        playerViewAdsHome.setKeepScreenOn(true);

        countAds = 5;
        skip_ads.setText("Skip Ads in 5");

        simpleExoPlayerAds.prepare(mediaSource1);

        simpleExoPlayerAds.setPlayWhenReady(true);
        simpleExoPlayerAds.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady) {
                    progressBar.setVisibility(View.GONE);
                }
                if (playbackState == ExoPlayer.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                    if (adsType_1 == 1) {
                        if (liveTvAd.getSkip().equalsIgnoreCase("0")){
                            skip_ads.setVisibility(View.GONE);
                        }

                        if (liveTvAd.getSkip().equalsIgnoreCase("1")){
                            skip_ads.setVisibility(View.VISIBLE);
                        }
                        updatetimer(5000);
                        adsType_1++;
                    }
                    if (adsType_2 == 2) {
                        isAdsInBufferState = false;
                        updatetimer(countAds * 1000);
                    }
                }
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                    adsType_2 = 2;
                    isAdsInBufferState = true;
                    // countDownTimer.cancel();
                }
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    progressBar.setVisibility(View.VISIBLE);
                    playerViewAdsHome.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    // simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayerAds.setPlayWhenReady(false);
                    // simpleExoPlayerAds.release();
                    if (adsType == 1) {
                        Log.d("shantanu", "playads");

                        advertisement_status = "2";
                        if (timePlay == 0) {
                            homeActivityy.pause_1 = 0;
                            timePlay = 1;
                        }
                        updateAPPAdvertisementCounter(false);
                        adsType++;
                        Log.d("shantanu url", String.valueOf(uri));
                        playVideo(uri);
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

        skip_ads.setOnClickListener(v -> {
            if (countAds == 0) {
                progressBar.setVisibility(View.VISIBLE);
                playerViewAdsHome.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
                //simpleExoPlayer.setPlayWhenReady(true);
                if (simpleExoPlayerAds != null) {
                    simpleExoPlayerAds.setPlayWhenReady(false);
                }
                if (simpleExoPlayerAds1 != null) {
                    simpleExoPlayerAds1.setPlayWhenReady(false);
                }
                advertisement_status = "1";
                updateAPPAdvertisementCounter(false);
                if (timePlay == 0) {
                    homeActivityy.pause_1 = 0;
                    timePlay = 1;
                }
                playVideo(uri);
            }
        });

    }

    public static void updatetimer(long millisecond) {
      CountDownTimer  countDownTimer = new CountDownTimer(millisecond, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isAdsInBufferState) {

                    if (countAds != 0) {
                        skip_ads.setText("Skip Ad in " + countAds);
                    } else {
                        skip_ads.setText("Skip Ad =>");
                    }
                    countAds = millisUntilFinished / 1000;
                }
            }

            @Override
            public void onFinish() {
                if (!isAdsInBufferState) {
                    countAds = 0;
                    skip_ads.setText("Skip Ad =>");
                }
            }
        };
        countDownTimer.start();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skip_ads.setText("Skip ads in " + (countAds - 1));

                if (countAds != 0) {
                    countAds = countAds - 1;
                    updatetimer();
                }
                if (countAds == 0) {
                    skip_ads.setText("Skip");
                }
            }
        }, 999);*/


    }

    public static void playNextAds() {

        if (Networkconstants.adsCount >= liveTvAds.length - 1) {
            Networkconstants.adsCount = 0;
        }

        Log.d("shantanu gap_duration", String.valueOf(gap_Duration_live_channel));
        if (homeActivityy.pause_1 != 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (homeActivityy.pause_1 == 0) {
                        Log.d("shantanu", "isPLaying");
                        //adsType++;
                        progressBar.setVisibility(View.GONE);

                        if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                            Networkconstants.adsCount = 0;
                        }
                        playads1(liveTvAds[Networkconstants.adsCount]);
                        // playNextAds();
                    }
                }
            }, gap_Duration_live_channel);
        }

    }

    public static void playNextAds1() {

        if (Networkconstants.adsCount >= liveTvAds.length - 1) {
            Networkconstants.adsCount = 0;
        }

        Log.d("shantanu gap_duration", String.valueOf(gap_Duration_live_channel));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (homeActivityy.pause_1 == 1) {
                    Log.d("shantanu", "isPLaying");
                    //adsType++;
                    progressBar.setVisibility(View.GONE);

                    homeActivityy.pause_1 = 0;

                    if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    playads1(liveTvAds[Networkconstants.adsCount]);
                    // playNextAds();
                }
            }
        }, gap_Duration_live_channel);

    }
    public static void prepareNextAds() {
        if (homeActivityy.pause_1 == 0) {
            Log.d("shantanu", "IN NExtAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds();
        }

        if (homeActivityy.pause_1 == 1) {
            Log.d("shantanu", "IN NExtAds " + String.valueOf(is_pause));
            Log.d("shantanu", "IN NExtAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds1();
        }

    }

    public static void playVideo(Uri uri) {

        if (SharedPreference.getInstance(context).getLiveTvAds() != null) {
            prepareNextAds();
        }
        progressBar.setVisibility(View.GONE);
        root_container.setVisibility(View.VISIBLE);

        hlsMediaSource = new HlsMediaSource.Factory(factory).createMediaSource(uri);

        playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView.setKeepScreenOn(true);

        simpleExoPlayer.prepare(hlsMediaSource);

        simpleExoPlayer.setPlayWhenReady(true);
        if (simpleExoPlayer.getPlayWhenReady()) {

        }

        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        full_Screen.setOnClickListener(v -> homeActivityy.showHomeFragment());
        quality.setOnClickListener(v -> {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                trackSelectionHelper.showSelectionDialog(context, "Quality",
                        trackSelector.getCurrentMappedTrackInfo(), 0);
            }

           /* TrackSelectionDialog trackSelectionDialog =
                    TrackSelectionDialog.createForTrackSelector(
                            trackSelector,
                            *//* onDismissListener= *//* dismissedDialog -> isShowingTrackSelectionDialog = false);
            trackSelectionDialog.show(activity.getSupportFragmentManager(), *//* tag= *//* null);*/
        });
        boolean[] checkedItems = {false, true, false, false, false, false};
        option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                alertDialog.setTitle("Playback Speed");
                String[] items = {"0.5x", "1x", "1.5x", "2x", "2.5x", "3x"};

                alertDialog.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    switch (which) {
                        case 0:
                            if (isChecked)
                                playbackSpeed(0.5f);
                            checkedItems[0] = true;
                            checkedItems[1] = false;
                            checkedItems[2] = false;
                            checkedItems[3] = false;
                            checkedItems[4] = false;
                            checkedItems[5] = false;
                            dialog.dismiss();
                            break;
                        case 1:
                            if (isChecked)
                                playbackSpeed(1f);
                            checkedItems[0] = false;
                            checkedItems[1] = true;
                            checkedItems[2] = false;
                            checkedItems[3] = false;
                            checkedItems[4] = false;
                            checkedItems[5] = false;
                            dialog.dismiss();
                            break;
                        case 2:
                            if (isChecked)
                                playbackSpeed(1.5f);
                            checkedItems[0] = false;
                            checkedItems[1] = false;
                            checkedItems[2] = true;
                            checkedItems[3] = false;
                            checkedItems[4] = false;
                            checkedItems[5] = false;
                            dialog.dismiss();
                            break;
                        case 3:
                            if (isChecked)
                                playbackSpeed(2f);
                            checkedItems[0] = false;
                            checkedItems[1] = false;
                            checkedItems[2] = false;
                            checkedItems[3] = true;
                            checkedItems[4] = false;
                            checkedItems[5] = false;
                            dialog.dismiss();
                            break;
                        case 4:
                            if (isChecked)
                                playbackSpeed(2.5f);
                            checkedItems[0] = false;
                            checkedItems[1] = false;
                            checkedItems[2] = false;
                            checkedItems[3] = false;
                            checkedItems[4] = true;
                            checkedItems[5] = false;
                            dialog.dismiss();
                            break;
                        case 5:
                            if (isChecked)
                                playbackSpeed(3f);
                            checkedItems[0] = false;
                            checkedItems[1] = false;
                            checkedItems[2] = false;
                            checkedItems[3] = false;
                            checkedItems[4] = false;
                            checkedItems[5] = true;
                            dialog.dismiss();
                            break;
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();

            }
        });
    }


    public static void playbackSpeed(float speed) {
        PlaybackParams params = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params = new PlaybackParams();
            params.setSpeed(speed);
        }
        simpleExoPlayer.setPlaybackParams(params);
    }



    @Override
    public void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equalsIgnoreCase(API.CHANNEL_LIST)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(apitype)
                    .setMultipartParameter("user_id", homeActivityy.signupResponse.getId())
                    .setMultipartParameter("current_version", ""+ Utils.getVersionCode(activity))
                    .setMultipartParameter("device_type", "1");
        }else if (apitype.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
            Log.d("shantanu", advertisement_id + " " + time_slot_id);
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", homeActivityy.signupResponse.getId())
                    .setMultipartParameter("advertisement_id", advertisement_id)
                    .setMultipartParameter("time_slot_id", time_slot_id)
                    .setMultipartParameter("advertisement_status", String.valueOf(2));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (jsonstring.optBoolean("status")) {
            if (apitype.equalsIgnoreCase(API.CHANNEL_LIST)) {
                mainChannelData = new Gson().fromJson(jsonstring.toString(), MainChannelData.class);
                SharedPreference.getInstance(context).setChannelData(mainChannelData);
                InitAdapter();
            }else if (apitype.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                Log.d("shantanu", "updated");
                //Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void InitAdapter() {
        MainChannelData mainChannelData=SharedPreference.getInstance(context).getChannelData();
        ChannelList.addAll(mainChannelData.getData());
        recyclerView_Live.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView_Live.setAdapter(new LiveTvAdapter(getContext(), ChannelList, fragment));
        recyclerView_Live.scheduleLayoutAnimation();
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}