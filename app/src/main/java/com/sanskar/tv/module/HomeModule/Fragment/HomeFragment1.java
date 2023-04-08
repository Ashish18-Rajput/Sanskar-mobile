package com.sanskar.tv.module.HomeModule.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.jwPlayer.LiveStreamJWActivity.COME_FROM_LIVETV;
import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.frag_type;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.checkedsnackbar;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.snackbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
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
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.cast.ExpandedControlsActivity;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.EventLogger;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.MyAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.MainMenuMaster;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.TrackSelectionHelper;
import com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomeFragment1 extends Fragment implements NetworkCall.MyNetworkCallBack/*, ExoPlayer.EventListener*/ {

    public boolean is_pause = true;
    public boolean is_pause_1 = true;
    RecyclerView recyclerView;
    List<MenuMasterList> menuMasterListList = new ArrayList<>();
    Context context;
    private int mPage = 1;
    private Boolean status = false;
    private HomeActivityy activity;
    private static final String TAG = "HomeFragment";
    private NetworkCall networkCall;
    public static PlayerView playerViewAds;
    public static SimpleExoPlayerView playerView1;
    public static ProgressBar progressBar;
    public static ImageView full_Screen;
    public static ImageView option_menu;
    public static SimpleExoPlayer simpleExoPlayer, simpleExoPlayerAds, simpleExoPlayerAds1;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    static DefaultHttpDataSourceFactory factory;

    TrackSelector selector, selector1;
    DefaultTrackSelector trackSelector;
    DefaultTrackSelector.Parameters trackParameters;
    TrackSelectionHelper trackSelectionHelper;
    private Handler mainHandler;
    boolean isShowingTrackSelectionDialog = false;
    private TrackGroupArray lastSeenTrackGroupArray;
    BandwidthMeter bandwidthMeter;
    DefaultLoadControl loadControl;
    String channel_name;
    static HlsMediaSource hlsMediaSource;
    ImageView imageView, exo_nextVideo, videoImage, playIconIV;
    TextView like_number_watch_channel, comment_number_watch_channel;
    public static AudioPlayerService player;
    public static boolean serviceBound = false;
    HomeActivityy activityy;
    MyAdapter MyAdapter;
    String channel_url = "", advertisement_status = "";
    EventLogger eventLogger;

    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mQueueMenuItem;
    private MediaInfo mSelectedMedia;
    private String isAddSkipAble = "";

    private PlaybackLocation mLocation;
    private PlaybackState mPlaybackState;
    public static String postid, commentid;
    ImageView liveTvbutton, liveTvbutton1;
    String video_id;
    public static MenuMasterList[] bhajanforImagefromHome;
    MenuMasterList[] bhajanNew;
    String video_url, Id, VIEWlike, Is_like_no;
    int channel_position = -1;
    public static int Channel_position;

    String web_view_bhajan, web_view_news, web_view_video;
    MainMenuMaster mainMenuMaster;
    List<AdvertisementAds> advertisementAds_liveTv = new ArrayList<>();
    List<AdvertisementAds> advertisementAds_video = new ArrayList<>();
    List<AdvertisementAds> advertisementAds_bhajan = new ArrayList<>();
    DefaultTimeBar exo_progress;
    private int Positon_of_channel;
    SwipeRefreshLayout swipe_refresh_home1;

    RelativeLayout like_channel, Comment_channel;
    ImageView like_channel_button, Comment;
    public static String totallike;

    public long playerViewPastTime = 0;
    public int adsType = -1;
    public int adsType_1 = -1;
    public int adsType_2 = -1;
    public boolean isAdsInBufferState = false;
    public int timePlay = 0;
    TextView quality;

    AdvertisementAds[] liveTvAds;

    public static String default_channel = "";
    public String finalUrl = "";
    boolean flag = false;
    MainMenuMaster mainMenuMaster_list;
    DataSource.Factory dataSourceFactory;

    String is_premium_active = "";
    long gap_Duration_live_channel = 600000;
    int live_user_api_gap_duration = 300000;
    int live_user = 1;
    private String Is_premium = "";
    TextView skip_ads, live_dot;

    String redirect;
    long countAds = 5;
    CountDownTimer countDownTimer;
    long milliseconds = 0;
    String advertisement_id = "", time_slot_id = "";
    List<Bhajan> bhajans1;
    public static Bhajan[] bhajans;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public String Current_channel_Name = "";
    public String Previous_channel_Name = "";
    int Current_channel_users = 0;
    MediaSource mediaSource;
    public Boolean isfirsttime = true;
    private boolean fromGuest = false;

    public ArrayList<BhajanResponse> bhajanResponseArrayList;


    long firstChannelPlayTime = 0;
    long afterChangeTime = 0;
    long differenceBetweenTime = 0;
    boolean isItFirstTimeToPlay = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;

        }
    };

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home1, container, false);

        networkCall = new NetworkCall(this, context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference("live_users_channel");

        databaseReference = null;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mCastContext = CastContext.getSharedInstance(getActivity());
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        setupCastListener();
        activityy = ((HomeActivityy) context);
        HomeActivityy.notify_ll.setVisibility(View.GONE);
        // getBundleData();
        initViews(view);

        if (activityy.getIntent().getExtras() != null && activityy.getIntent().hasExtra(Constants.FROM_GUEST)) {
            if (activityy.getIntent().getStringExtra(Constants.FROM_GUEST).equalsIgnoreCase("1")) {
                fromGuest = true;
            }
        }

        fetchData1();

        if (frag_type != null) {
            if (frag_type.equalsIgnoreCase(Constants.Notification)) {
                getNotificationFrag(1);
            }
        }
        if (SharedPreference.getInstance(context).getString(Constants.IS_PREMIUM) != null) {
            Is_premium = SharedPreference.getInstance(context).getString(Constants.IS_PREMIUM);
            LiveFragment.Is_premium = Is_premium;
        }

        mainHandler = new Handler();
        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();

        trackParameters = new DefaultTrackSelector.ParametersBuilder().build();

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);
        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        trackSelector.setParameters(trackParameters);

        selector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );
        selector1 = new DefaultTrackSelector(
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


        if (checkedsnackbar.equalsIgnoreCase("snackbar")) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }

        activity.getSupportActionBar().show();

        setSongData();
        setPlayPause();


        if (Is_premium.equalsIgnoreCase("0")) {
            //  getAdvertisementAds(false);
            SHOW_ADVERTISEMENT();
        }


        swipe_refresh_home1.setOnRefreshListener(() -> {
            if (!isItFirstTimeToPlay) {
                afterChangeTime = System.currentTimeMillis();
                differenceBetweenTime = (afterChangeTime - firstChannelPlayTime) / 1000;
                isItFirstTimeToPlay = true;
                UPDATE_USER_PLAY_TIME(false);
            }
            fetchData1();

        });


        Log.d("shantanu in OnCreate", String.valueOf(SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS_GAP_DURATION)));
        Log.d("shantanu in OnCreate", String.valueOf(SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS)));

        if (SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS) == 1) {
            //fetchLiveUsers(false);
        }

        return view;
    }

    public void UPDATE_USER_PLAY_TIME(boolean b) {
        if (GoLiveActivity.isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.COUNTRY_WISE_USER_PLAY_HISTORY, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("exoplayer_video", bandwidthMeter);
    }

    private void playads1(AdvertisementAds liveTvAd) {
        adsType = 3;
        adsType_1 = 3;
        ++Networkconstants.adsCount;
        simpleExoPlayer.setPlayWhenReady(false);
        playerView1.setVisibility(View.GONE);
        playIconIV.setVisibility(View.GONE);
        playerViewAds.setVisibility(View.VISIBLE);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        advertisement_id = liveTvAd.getId();
        time_slot_id = liveTvAd.getTime_slot_id();
        Log.d("skip", liveTvAd.getSkip());
        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }

        Uri uri1 = Uri.parse(liveTvAd.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        mediaSource = new ExtractorMediaSource(uri1
                , factory, extractorsFactory, null, null);

        playerViewAds.setPlayer(simpleExoPlayerAds1);
        //keep screen on
        playerViewAds.setKeepScreenOn(true);

        simpleExoPlayerAds1.prepare(mediaSource);
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
                        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
                            skip_ads.setVisibility(View.GONE);
                        }

                        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
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
                    playerViewAds.setVisibility(View.GONE);
                    playerView1.setVisibility(View.VISIBLE);
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

    private void getAdvertisementAds(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(HomeFragment1.this, context);
            networkCall.NetworkAPICall(API.GET_ADVERTISEMENT_ADS, b);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void updateAPPAdvertisementCounter(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(HomeFragment1.this, context);
            networkCall.NetworkAPICall(API.UPDATE_APP_ADVERTISEMENT_COUNTER, b);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            //swipeRefreshLayout.setVisibility(View.GONE);
            ((HomeActivityy) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.toolbar.setVisibility(View.GONE);
            activity.bottomNavigationView.setVisibility(View.GONE);
            activity.floatingbutton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView1.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView1.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) playerViewAds.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerViewAds.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) videoImage.getLayoutParams();
            params2.width = params.MATCH_PARENT;
            params2.height = params.MATCH_PARENT;
            videoImage.setLayoutParams(params1);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide your objects here.
            //   swipeRefreshLayout.setVisibility(View.VISIBLE);
            ((HomeActivityy) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.toolbar.setVisibility(View.VISIBLE);
            activity.bottomNavigationView.setVisibility(View.VISIBLE);
            activity.floatingbutton.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView1.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerView1.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) playerViewAds.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerViewAds.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) videoImage.getLayoutParams();
            params2.width = params.MATCH_PARENT;
            params2.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
            videoImage.setLayoutParams(params1);
        }
    }

    private void SHOW_ADVERTISEMENT() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_ADVERTISEMENT, false);
        } else {
            ToastUtil.showDialogBox(context, "No Internet Connection");
        }
    }

    private void GET_API_GET_APP_VERSION() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.API_GET_APP_VERSION, false);
        } else {
            ToastUtil.showDialogBox(context, "No Internet Connection");
        }
    }

    public static int getVersionCode(Activity activity) {
        int version = 0;
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static void getVersionUpdateDialog(final Activity ctx) {
        androidx.appcompat.app.AlertDialog.Builder alertBuild = new androidx.appcompat.app.AlertDialog.Builder(ctx);
        alertBuild
                .setTitle("Sanskar")
                .setMessage("Available in a new version , you are using an old version")
                .setCancelable(false)
                .setPositiveButton("Update", (dialog, whichButton)
                        -> ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.sanskar.tv"))))
                .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    dialog.dismiss();
                    ctx.finishAffinity();
                });
        androidx.appcompat.app.AlertDialog dialog = alertBuild.create();
        dialog.show();
    }

    private void fetchData1() {
        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.MENU_MASTER, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = ((HomeActivityy) context);
        Log.d(TAG, "onAttach: onAttachIsCalled()");
    }

    public void fetchData() {
        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.MENU_MASTER, false);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void setupCastListener() {

        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                if (null != mSelectedMedia) {

                    if (mPlaybackState == PlaybackState.PLAYING) {
                        loadRemoteMedia(0, true);
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }
        };

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

    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;

    }

    private void updatePlayButton(PlaybackState state) {

    }

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();

    }

    public void setPlayPause() {
        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause, intentFilter);
    }

    public void setSongData() {
        IntentFilter filter = new IntentFilter(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }

    private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("isplaying").equals("true")) {
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            } else if (intent.getStringExtra("isplaying").equals("false")) {
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
                    .setCallback((e, result) -> ((HomeActivityy) context).thumbaudio.setImageBitmap(result));

            if (intent.getStringExtra("isplaying").equals("true")) {
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            } else if (intent.getStringExtra("isplaying").equals("false")) {
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


    private void initViews(View view) {

        recyclerView = view.findViewById(R.id.recyclerView_homefrag);
        recyclerView.setNestedScrollingEnabled(false);

        playIconIV = view.findViewById(R.id.playIconIV);
        videoImage = view.findViewById(R.id.videoImage);
        playerView1 = view.findViewById(R.id.player_view_exo);

        playerViewAds = view.findViewById(R.id.player_view_exo_ads);

        full_Screen = playerView1.findViewById(R.id.bt_full);
        full_Screen.setVisibility(View.VISIBLE);

        full_Screen.setOnClickListener(v -> {
            if (flag) {
                ((HomeActivityy) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN);
                full_Screen.setImageDrawable(getResources().getDrawable(R.drawable.ic_full));

                //set portrait orientation

                ((HomeActivityy) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

                activity.toolbar.setVisibility(View.VISIBLE);
                activity.bottomNavigationView.setVisibility(View.VISIBLE);
                activity.floatingbutton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView1.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView1.setLayoutParams(params);

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) playerViewAds.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerViewAds.setLayoutParams(params1);

                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) videoImage.getLayoutParams();
                params2.width = params.MATCH_PARENT;
                params2.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
                videoImage.setLayoutParams(params1);
                flag = false;
            } else {
                ((HomeActivityy) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        , WindowManager.LayoutParams.FLAG_FULLSCREEN);
                full_Screen.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_fullscreen_exit));

                ((HomeActivityy) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                activity.toolbar.setVisibility(View.GONE);
                activity.toolbar.setVisibility(View.GONE);
                activity.bottomNavigationView.setVisibility(View.GONE);
                activity.floatingbutton.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView1.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView1.setLayoutParams(params);

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) playerViewAds.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerViewAds.setLayoutParams(params1);

                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) videoImage.getLayoutParams();
                params2.width = params.MATCH_PARENT;
                params2.height = params.MATCH_PARENT;
                videoImage.setLayoutParams(params1);
                flag = true;
            }
        });

        option_menu = playerView1.findViewById(R.id.option_menu);
        progressBar = view.findViewById(R.id.progress_bar);
        quality = playerView1.findViewById(R.id.quality);

        exo_nextVideo = view.findViewById(R.id.exo_nextVideo);
        exo_progress = view.findViewById(R.id.exo_progress);

        exo_nextVideo.setVisibility(View.GONE);

        skip_ads = view.findViewById(R.id.skip_ads);

        swipe_refresh_home1 = view.findViewById(R.id.swipe_refresh_home1);
        swipe_refresh_home1.setRefreshing(true);
        like_channel = view.findViewById(R.id.like_channel);
        live_dot = view.findViewById(R.id.live_dot);

        Comment_channel = view.findViewById(R.id.Comment_channel);
        comment_number_watch_channel = view.findViewById(R.id.comment_number_watch_channel);

        like_number_watch_channel = view.findViewById(R.id.like_number_watch_channel);
        liveTvbutton = activityy.findViewById(R.id.floatingActionButton);
        liveTvbutton1 = activityy.findViewById(R.id.floatingActionButton1);
        like_channel_button = view.findViewById(R.id.like);
        like_channel_button.setTag(false);

        like_channel.setOnClickListener(view1 -> {
            if (isConnectingToInternet(context)) {
                if (!Boolean.parseBoolean(like_channel_button.getTag().toString())) {
                    networkCall.NetworkAPICall(API.LIKE_CHANNEL, false);
                } else {
                    networkCall.NetworkAPICall(API.UNLIKE_CHANNEL, false);
                }
            } else {
                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        });

        liveTvbutton.setOnClickListener(v -> {
            if (video_url == null) {
                Toast.makeText(context, "Please Select any Live Channel", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(context, LiveStreamJWActivity.class);
                intent.putExtra("livevideourl", video_url);
                intent.putExtra("channel_id", Id);
                intent.putExtra("channel_position", String.valueOf(channel_position));
                intent.putExtra("channel_name", channel_name);
                intent.putExtra("from", "livetv");
                intent.putExtra("VIEWlike", VIEWlike);
                intent.putExtra("Is_like_no", Is_like_no);
                context.startActivity(intent);
            }
        });

        liveTvbutton1.setOnClickListener(v -> {
            if (video_url == null) {
                Toast.makeText(context, "Please Select any Live Channel", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(context, LiveStreamJWActivity.class);
                intent.putExtra("livevideourl", video_url);
                intent.putExtra("channel_id", Id);
                intent.putExtra("channel_position", String.valueOf(channel_position));
                intent.putExtra("channel_name", channel_name);
                intent.putExtra("from", "livetv");
                intent.putExtra("VIEWlike", VIEWlike);
                intent.putExtra("Is_like_no", Is_like_no);
                context.startActivity(intent);

            }
        });
    }

    public void playVideo(List<MenuMasterList> masterLists, int position, String MenuTypeId, int ads_counts) {

        MenuMasterList[] videos1 = new MenuMasterList[masterLists.size()];
        for (int i = 0; i < masterLists.size(); i++) {
            videos1[i] = masterLists.get(i);
        }

        Log.d("shantanu", " Home" + ads_counts);

        Bundle bundle = new Bundle();
        bundle.putSerializable("video_data", videos1);
        bundle.putInt("position", position);
        bundle.putInt("ads_counts", ads_counts);

        if (context instanceof HomeActivityy) {
            if (AudioPlayerService.mediaPlayer != null) {
                try {
                    Constants.IS_PAUSEDFROMHOME = "true";
                    AudioPlayerService.mediaPlayer.stop();

                    HomeActivityy.playerlayout.setVisibility(View.GONE);
                    Constants.CALL_RESUME = "false";
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            if (videos1[position].getYoutubeUrl().equals("")) {
                ToastUtil.showDialogBox(context, "Sorry! Can't Play this Video");
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("video_data1", videos1);
                intent.putExtra("type", 2);
                intent.putExtra("position", position);
                intent.putExtra("menutypeid", MenuTypeId);
                intent.putExtra("ads_counts", ads_counts);
                context.startActivity(intent);
                //((HomeActivityy) context).finish();
            }
        } else if (context instanceof LiveStreamJWActivity) {
            Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
            liveIntent.putExtras(bundle);
            //((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
        }

        if (isConnectingToInternet(context)) {
            video_id = masterLists.get(position).getId();
            //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }

    public String currentDate() {
        Calendar c = Calendar.getInstance();
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.ENGLISH);
        Log.e("shantanu", "old date: " + sdf.format(c.getTime()));

        String oldDate = sdf.format(c.getTime());
        int length = oldDate.length();
        String newDate = oldDate.substring(0, length - 2) + ':' + oldDate.substring(length - 2);
        Log.e("shantanu", "new date: " + newDate);
        return oldDate;
    }

    public String previousDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, -12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.ENGLISH);
        Log.e("shantanu", "old date: " + sdf.format(c.getTime()));

        String oldDate = sdf.format(c.getTime());
        int length = oldDate.length();
        String newDate = oldDate.substring(0, length - 2) + ':' + oldDate.substring(length - 2);
        Log.e("shantanu", "new date: " + newDate);
        return newDate;
    }

    public void setPlayer(String channel_url, String channel_name, String banner, String likes, String id, String is_like_no, int position, MenuMasterList radioSatsang) {

        like_channel.setVisibility(View.VISIBLE);
        video_url = String.valueOf(channel_url);
        activity.video_url = video_url;
        if (!isItFirstTimeToPlay) {
            afterChangeTime = System.currentTimeMillis();
            differenceBetweenTime = (afterChangeTime - firstChannelPlayTime) / 1000;
            isItFirstTimeToPlay = true;
            UPDATE_USER_PLAY_TIME(false);
        }
        Channel_position = position;
        channel_position = position;
        activity.channel_position = position;
        this.channel_name = channel_name;

        String newUrl = "";
        if (id.equalsIgnoreCase("27")) {
            newUrl = channel_url;
        } else {
            newUrl = channel_url + "?start=" + previousDate() + "&end=";
        }

        Log.e("shantanu", "newUrl: " + newUrl);

        //Current_channel_Name = channel_name;

        Positon_of_channel = position;
        Comment_channel.setVisibility(View.GONE);
        VIEWlike = likes;
        Id = id;
        totallike = likes;

        Is_like_no = is_like_no;

        activity.video_id = Id;
        activity.channel_name = channel_name;
        activity.view_like = VIEWlike;
        activity.is_like_no = Is_like_no;
        like_number_watch_channel.setText(likes + " Likes");

        setChannelRelatedData();
        activity.pause_1 = 1;

        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
        }

        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
        }

        videoImage.setVisibility(View.VISIBLE);
        playerView1.setVisibility(View.GONE);
        playerViewAds.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
        playIconIV.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(banner)
                .into(videoImage);

        if (activityy.homeBhajanListNew.size() > 0) {
            activityy.homeBhajanListNew.clear();
        }

        activityy.homeBhajanListNew.add(radioSatsang);
        bhajanNew = new MenuMasterList[1];
        bhajanNew[0] = radioSatsang;
        String finalNewUrl = newUrl;
        playIconIV.setOnClickListener(v -> {

            if (((HomeActivityy) getActivity()).playerlayout.getVisibility() == View.VISIBLE) {
                ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
                AudioPlayerService.mediaPlayer.pause();
            }
            if (((HomeActivityy) getActivity()).playerlayout1.getVisibility() == View.VISIBLE) {
                ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);

            }
            if (((HomeActivityy) getActivity()).playerlayout2.getVisibility() == View.VISIBLE) {
                ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);

            }

            if (!Id.equalsIgnoreCase("30")) {

                this.channel_url = channel_url;
                activityy.plays = 1;
                Comment_channel.setVisibility(View.VISIBLE);
                isfirsttime = true;
                Current_channel_Name = Id;
                //  updateLiveUsers(false);
                like_channel.setVisibility(View.VISIBLE);
                videoImage.setVisibility(View.GONE);
                playerView1.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if (Is_premium.equalsIgnoreCase("0")) {
                    if (SharedPreference.getInstance(context).getLiveTvAds() != null) {
                        liveTvAds = SharedPreference.getInstance(context).getLiveTvAds();
                        if (Networkconstants.adsCount > liveTvAds.length - 1) {
                            Networkconstants.adsCount = 0;
                        }
                        Log.d("shantanu", finalNewUrl);
                        // playads(liveTvAds[Networkconstants.adsCount], Uri.parse(channel_url));
                        if (TextUtils.isEmpty(channel_url)) {
                            ToastUtil.showDialogBox1(context, "Please Subscribe to our Premium");
                        } else
                            playVideo1(Uri.parse(finalNewUrl), channel_name);
                    } else {
                        if (TextUtils.isEmpty(channel_url)) {
                            ToastUtil.showDialogBox1(context, "Please Subscribe to our Premium");
                        } else
                            playVideo1(Uri.parse(finalNewUrl), channel_name);
                    }
                } else {
                    playVideo1(Uri.parse(finalNewUrl), channel_name);
                }
            } else {
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
        });

    }


    public void prepareNextAds() {
        if (activity.pause_1 == 0) {
            Log.d("shantanu", "IN NExtAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds();
        }

        if (activity.pause_1 == 1) {
            Log.d("shantanu", "IN NExtAds " + String.valueOf(is_pause));
            Log.d("shantanu", "IN NExtAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds1();
        }

    }


    public void playVideo1(Uri uri, String channel_name) {
        Log.d("shantanu", String.valueOf(uri));

        if (isItFirstTimeToPlay) {
            firstChannelPlayTime = System.currentTimeMillis();
            isItFirstTimeToPlay = false;
        }

        // Uri uri1 = Uri.parse("https://s3.evidya.in/file_library/videos/vod/669748764772175700_video_VOD.m3u8?Expires=2130323556&Signature=BeiaSaGWdE1d1n8ctRjSSXrPEK6NGg9WKqOdcgMlQYoz4lKtaxWbXAODR5xinpEE6OB8etcM-SbkRpGCPuBRKm12Zy~0a3R22xEVaGv9jrLPuiAnwNNDc6SJp~DUpzThm3-GUhShslRqZiLK~wB46tgNMtvkSdUYq1YC6QuAHaD-Zh6GvWMIiFvGrGuBpXcmj~kscyP5PWrLf0bHO94bCJOWB6J~8yp~woSBluCMcLkDlBUoH1mA9OfJWY4ILfLlRpraEvvsAANH-dEnPkjjvb-AS8tZUj1tta93e0g9wGk~p67fzoMTkhEpXa5l6qNwp01k79t6d6biJvKKX7cmgg__&Key-Pair-Id=APKAIKXNLMQGC5ARQTMA");
        if (liveTvAds != null && liveTvAds.length > 0) {
            prepareNextAds();
        }
        if (channel_name.equalsIgnoreCase("Total Bhakti")) {
            quality.setVisibility(View.GONE);
        } else {
            quality.setVisibility(View.VISIBLE);
        }

        playerViewAds.setVisibility(View.GONE);
        playerView1.setVisibility(View.VISIBLE);

        playIconIV.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

        simpleExoPlayer.addListener(eventLogger);


        simpleExoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                if (trackGroups != lastSeenTrackGroupArray) {
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                    if (mappedTrackInfo != null) {
                        if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                            Toast.makeText(context, "Error Unsupported Video", Toast.LENGTH_SHORT).show();
                        }
                        if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                            Toast.makeText(context, "Error Unsupported Video", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lastSeenTrackGroupArray = trackGroups;
                }
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);

                }
                if (playWhenReady) {
                    Log.d("shantanu", "when PLaywhenReady is " + String.valueOf(is_pause));
                    is_pause = false;
                    is_pause_1 = false;
                } else {

                    Log.d("shantanu", "when PLaywhenReady is " + String.valueOf(is_pause));
                    is_pause = true;
                    //activity.pause_1 = 1;
                    is_pause_1 = true;
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
                live_dot.setVisibility(View.VISIBLE);
            }


        });
        live_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo1(uri, channel_name);
                live_dot.setVisibility(View.GONE);
            }
        });

        playerView1.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView1.setKeepScreenOn(true);

        final LoopingMediaSource loopingSource = new LoopingMediaSource(hlsMediaSource);

        simpleExoPlayer.prepare(loopingSource);
        playerView1.requestFocus();
        simpleExoPlayer.setPlayWhenReady(true);
        if (simpleExoPlayer.getPlayWhenReady()) {

        }
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    trackSelectionHelper.showSelectionDialog(context, "Quality",
                            trackSelector.getCurrentMappedTrackInfo(), 0);
                }
            }
        });
        boolean[] checkedItems = {false, true, false, false, false, false};
        option_menu.setOnClickListener(v -> {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Playback Speed");
            String[] items = {"0.5x", "1x", "1.5x", "2x", "2.5x", "3x"};

            alertDialog.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
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
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();

        });

    }

    public void playNextAds() {

        if (Networkconstants.adsCount >= liveTvAds.length - 1) {
            Networkconstants.adsCount = 0;
        }

        Log.d("shantanu gap_duration", String.valueOf(gap_Duration_live_channel));
        if (activity.pause_1 != 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (activity.pause_1 == 0) {
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

    public void playNextAds1() {

        if (Networkconstants.adsCount >= liveTvAds.length - 1) {
            Networkconstants.adsCount = 0;
        }

        Log.d("shantanu gap_duration", String.valueOf(gap_Duration_live_channel));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity.pause_1 == 1) {
                    Log.d("shantanu", "isPLaying");
                    //adsType++;
                    progressBar.setVisibility(View.GONE);

                    activity.pause_1 = 0;

                    if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    playads1(liveTvAds[Networkconstants.adsCount]);
                    // playNextAds();
                }
            }
        }, gap_Duration_live_channel);
    }


    public void getNotificationFrag(int type) {
        // ((HomeActivityy) activity).showNotificationFrag();
        NotificationFragment frag = new NotificationFragment();
        if (type == 1) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TYPE, "" + type);
            frag.setArguments(bundle);
        }
        //NotificationFragmentNew frag = new NotificationFragmentNew();
        ((HomeActivityy) context)
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Notification_fragment")
                .replace(R.id.container_layout_home, frag)
                .commit();
    }

    public static void playbackSpeed(float speed) {
        PlaybackParams params = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params = new PlaybackParams();
            params.setSpeed(speed);
        }
        simpleExoPlayer.setPlaybackParams(params);
    }

    public void getSearchHomeVideos() {
        status = false;
        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.HOME_SEARCH_DATA, true);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
        Builders.Any.B ion = null;
        Log.e("shantanu", SharedPreference.getInstance(activity).getString(Constants.JWT) + " " + Utils.JWT + "user_id " + ((HomeActivityy) context).signupResponse.getId());
//        if (!SharedPreference.getInstance(context).getString(Constants.GUEST_ID).equalsIgnoreCase("163")) {
        if (!fromGuest) {
            if (API_NAME.equalsIgnoreCase(API.MENU_MASTER)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", activity.signupResponse.getId())
                        .setMultipartParameter("current_version", "" + Utils.getVersionCode(activity))
                        .setMultipartParameter("device_type", "1");
            } else if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", activity.signupResponse.getId());
            } else if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT_ADS)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", activity.signupResponse.getId());
            } else if (API_NAME.equals(API.HOME_SEARCH_DATA)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", activity.signupResponse.getId())
                        .setMultipartParameter("keyword", ((HomeActivityy) context).searchContent);
            }
       /*else if (API_NAME.equals(API.RECENT_VIEW)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }*/
            else if (API_NAME.equals(API.LIKE_CHANNEL) || API_NAME.equals(API.UNLIKE_CHANNEL)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("channel_id", Id);
            } else if (API_NAME.equals(API.UPDATE_LIVE_USERS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("channel_id", Id)
                        .setMultipartParameter("last_channel_id", Previous_channel_Name);
            } else if (API_NAME.equals(API.GET_LIVE_USERS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("channel_id", Id);
            } else if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("search_content", "")
                        .setMultipartParameter("video_category", "")
                        .setMultipartParameter("last_video_id", "")
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            } else if (API_NAME.equals(API.API_GET_APP_VERSION)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME);
            } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                Log.d("shantanu", advertisement_id + " " + time_slot_id);
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("advertisement_id", advertisement_id)
                        .setMultipartParameter("time_slot_id", time_slot_id)
                        .setMultipartParameter("advertisement_status", String.valueOf(2));
            } else if (API_NAME.equalsIgnoreCase(API.COUNTRY_WISE_USER_PLAY_HISTORY)) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("media_type", "3")
                        .setMultipartParameter("media_id", Id)
                        .setMultipartParameter("device_type", "1")
                        .setMultipartParameter("total_play", String.valueOf(differenceBetweenTime))
                        .setMultipartParameter("video_status", "0");
            }
        } else {
            if (API_NAME.equalsIgnoreCase(API.MENU_MASTER)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("current_version", "" + Utils.getVersionCode(activity))
                        .setMultipartParameter("device_type", "1");
            } else if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", "163");
            } else if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT_ADS)) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("user_id", "163");
            } else if (API_NAME.equals(API.HOME_SEARCH_DATA)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("keyword", ((HomeActivityy) context).searchContent);
            }
       /*else if (API_NAME.equals(API.RECENT_VIEW)){
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }*/
            else if (API_NAME.equals(API.LIKE_CHANNEL) || API_NAME.equals(API.UNLIKE_CHANNEL)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("channel_id", Id);
            } else if (API_NAME.equals(API.UPDATE_LIVE_USERS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("channel_id", Id)
                        .setMultipartParameter("last_channel_id", Previous_channel_Name);
            } else if (API_NAME.equals(API.GET_LIVE_USERS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("channel_id", Id);
            } else if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("search_content", "")
                        .setMultipartParameter("video_category", "")
                        .setMultipartParameter("last_video_id", "")
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            } else if (API_NAME.equals(API.API_GET_APP_VERSION)) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME);
            } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                Log.d("shantanu", advertisement_id + " " + time_slot_id);
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("advertisement_id", advertisement_id)
                        .setMultipartParameter("time_slot_id", time_slot_id)
                        .setMultipartParameter("advertisement_status", String.valueOf(2));
            } else if (API_NAME.equalsIgnoreCase(API.COUNTRY_WISE_USER_PLAY_HISTORY)) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("media_type", "3")
                        .setMultipartParameter("media_id", Id)
                        .setMultipartParameter("device_type", "1")
                        .setMultipartParameter("total_play", String.valueOf(differenceBetweenTime))
                        .setMultipartParameter("video_status", "0");
            }
        }
        return ion;
    }


    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {

        if (result.optBoolean("status")) {
            activity.isLike = false;

            if (API_NAME.equals(API.MENU_MASTER)) {
                web_view_bhajan = result.optString("web_view_bhajan");
                web_view_news = result.optString("web_view_news");
                web_view_video = result.optString("web_view_video");

                int is_premium = result.optInt("is_premium");

                is_premium_active = result.optString("is_premium_active");
                live_user = result.optInt("live_user");
                live_user_api_gap_duration = result.optInt("live_user_api_gap_duration");

                if (result.optString("qr_scanner").equalsIgnoreCase("1")) {
                    activity.qrCode.setVisibility(View.VISIBLE);
                } else {
                    activity.qrCode.setVisibility(View.GONE);
                }

                String tv_guide_android = result.optString("tv_guide_android");
                String total_play = result.optString("total_play");

                SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_BHAJAN, web_view_bhajan);
                SharedPreference.getInstance(context).putString(Constants.TV_GUIDE_ANDROID, tv_guide_android);
                SharedPreference.getInstance(context).putInt(Constants.LIVE_USERS, live_user);
                SharedPreference.getInstance(context).putInt(Constants.LIVE_USERS_GAP_DURATION, live_user_api_gap_duration);
                SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_NEWS, web_view_news);
                SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_VIDEO, web_view_video);

                SharedPreference.getInstance(context).putString(Constants.TOTAL_PLAY, total_play);

                SharedPreference.getInstance(context).putString(Constants.IS_PREMIUM, String.valueOf(is_premium));
                Log.e("1234", "Is premium" + String.valueOf(is_premium));

                SharedPreference.getInstance(context).putString(Constants.IS_PREMIUM_ACTIVE, is_premium_active);

                mainMenuMaster = new Gson().fromJson(result.toString(), MainMenuMaster.class);
                SharedPreference.getInstance(context).setMenuMaster(mainMenuMaster);


                Log.d("Shantanu_Chaudhary", is_premium_active);
                if (!is_premium_active.equalsIgnoreCase("")) {

                    Log.d("Shantanu_Chaudhary", is_premium_active);
                    if (is_premium_active.equalsIgnoreCase("1")) {
                        activityy.changeBottomBand(true);
                    }
                    if (is_premium_active.equalsIgnoreCase("2")) {
                        activityy.changeBottomBand(false);
                    }
                }

                videoImage.setVisibility(View.VISIBLE);
                playerView1.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                playIconIV.setVisibility(View.VISIBLE);

                InitLandingPageAdapter();
            } else if (API_NAME.equals(API.LIKE_CHANNEL)) {
                LikeDislike(true);
                fetchData();
            } else if (API_NAME.equals(API.UPDATE_LIVE_USERS)) {
                // ToastUtil.showShortToast(context,result.optString("message"));
            } else if (API_NAME.equals(API.GET_LIVE_USERS)) {
                comment_number_watch_channel.setText(result.optString("data"));
                live_user = result.optInt("live_user");
                live_user_api_gap_duration = result.optInt("live_user_api_gap_duration");
                SharedPreference.getInstance(context).putInt(Constants.LIVE_USERS, live_user);
                SharedPreference.getInstance(context).putInt(Constants.LIVE_USERS_GAP_DURATION, live_user_api_gap_duration);
                if (live_user == 1) {
                    comment_number_watch_channel.setText(result.optString("data"));
                }

            } else if (API_NAME.equals(API.GET_ADVERTISEMENT)) {


                showDialogBox(result.optJSONObject("data").optString("thumbnail"));

            } else if (API_NAME.equals(API.COUNTRY_WISE_USER_PLAY_HISTORY)) {

                //   Toast.makeText(context, result.optString("message"), Toast.LENGTH_SHORT).show();

            } else if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT_ADS)) {
                JSONArray live_tv = result.optJSONArray("live_tv");
                JSONArray video = result.optJSONArray("video");
                JSONArray bhajan = result.optJSONArray("bhajan");

                gap_Duration_live_channel = result.optJSONObject("gap_duration").optInt("LIVE_CHANNEL");

                SharedPreference.getInstance(context).putLong(Constants.GAP_DURATION_LIVE_CHANNEl, gap_Duration_live_channel);

                activityy.gap_Duration_live_channel = gap_Duration_live_channel;

                AdvertisementAds[] liveTvAds = new AdvertisementAds[live_tv.length()];
                AdvertisementAds[] videoAds = new AdvertisementAds[video.length()];
                AdvertisementAds[] bhajanAds = new AdvertisementAds[bhajan.length()];

                if (advertisementAds_liveTv.size() > 0) {
                    advertisementAds_liveTv.clear();
                }
                if (advertisementAds_video.size() > 0) {
                    advertisementAds_video.clear();
                }
                if (advertisementAds_bhajan.size() > 0) {
                    advertisementAds_bhajan.clear();
                }
                if (live_tv != null) {
                    for (int i = 0; i < live_tv.length(); i++) {
                        AdvertisementAds advertisementAds = new Gson().fromJson(live_tv.opt(i).toString(), AdvertisementAds.class);
                        advertisementAds_liveTv.add(advertisementAds);
                        liveTvAds[i] = advertisementAds;
                    }
                    SharedPreference.getInstance(context).setLiveTvAds(liveTvAds);

                } else {
                    SharedPreference.getInstance(context).setLiveTvAds(null);
                }

                if (video != null) {
                    for (int i = 0; i < video.length(); i++) {
                        AdvertisementAds advertisementAds = new Gson().fromJson(video.opt(i).toString(), AdvertisementAds.class);
                        advertisementAds_video.add(advertisementAds);
                        videoAds[i] = advertisementAds;
                    }
                    SharedPreference.getInstance(context).setVideoAds(videoAds);
                } else {
                    SharedPreference.getInstance(context).setVideoAds(null);
                }

                if (bhajan != null) {
                    for (int i = 0; i < bhajan.length(); i++) {
                        AdvertisementAds advertisementAds = new Gson().fromJson(bhajan.opt(i).toString(), AdvertisementAds.class);
                        advertisementAds_bhajan.add(advertisementAds);
                        bhajanAds[i] = advertisementAds;
                    }
                    SharedPreference.getInstance(context).setPictureAds(bhajanAds);
                } else {
                    SharedPreference.getInstance(context).setPictureAds(null);
                }

            } else if (API_NAME.equals(API.UNLIKE_CHANNEL)) {
                LikeDislike(false);
                fetchData();
            } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                Log.d("shantanu", "updated");
                //Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
            } else if (API_NAME.equals(API.API_GET_APP_VERSION)) {
                if (result.optJSONObject("data").optString("android") != null) {
                    int versionCode = Integer.parseInt(result.optJSONObject("data").optString("android"));

                    if (versionCode != 0) {
                        if (versionCode == getVersionCode(activity)) {
                            //go_next();
                        } else if (versionCode < getVersionCode(activity)) {
                            //go_next();
                        } else {
                            getVersionUpdateDialog(activity);
                        }
                    }
                }
            }
        } else {
            if (result.optString("error").equalsIgnoreCase("100100")) {
                Utils.clearUserData(getContext());
                Intent intent = new Intent(getContext(), LoginSignupActivity.class);
                ((HomeActivityy) getContext()).finish();
                startActivity(intent);
            }
            if (API_NAME.equalsIgnoreCase(API.GET_ADVERTISEMENT_ADS)) {
                SharedPreference.getInstance(context).setLiveTvAds(null);
                SharedPreference.getInstance(context).setVideoAds(null);
                SharedPreference.getInstance(context).setPictureAds(null);
            }
        }
    }

    private void showDialogBox(String menuMasterListList) {
        final Dialog dialog = new Dialog(activity, R.style.BottomSheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.premium_banner);

        ImageView imageView, imageView1;

        imageView = dialog.findViewById(R.id.banner_img);

        Glide.with(context)
                .load(menuMasterListList)
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(imageView);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentActivity.class);
            context.startActivity(intent);
            dialog.dismiss();
        });

        imageView1 = dialog.findViewById(R.id.delete_banner_img);

        imageView1.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }


    private void LikeDislike(boolean b) {

        like_channel_button.setTag(b);
        int likenum = 0;
        if (like_number_watch_channel.getText().toString().equalsIgnoreCase("0 like")) {
            likenum = 0;
        } else {
            likenum = Integer.parseInt(like_number_watch_channel.getText().toString().split(" ")[0]);
        }
        if (b) {
            VIEWlike = String.valueOf(Integer.parseInt(VIEWlike) + 1);
            if (likenum == 0) {
                like_number_watch_channel.setText(likenum + 1 + " like");

                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            } else {
                like_number_watch_channel.setText(likenum + 1 + " likes");
                totallike = like_number_watch_channel.getText().toString().split(" ")[0];

            }

            like_channel_button.setImageResource(R.mipmap.audio_liked);
            Is_like_no = "1";

            activity.homeChannelList1.get(Positon_of_channel).setIs_likes("1");
            activity.homeChannelList1.get(Channel_position).setLikes(String.valueOf(likenum + 1));
            activity.homeChannelList1.get(Channel_position).setIs_likes("1");
        } else {
            VIEWlike = String.valueOf(Integer.parseInt(VIEWlike) - 1);
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

            activity.homeChannelList1.get(Positon_of_channel).setIs_likes("0");
            Is_like_no = "0";

            activity.homeChannelList1.get(Channel_position).setLikes(String.valueOf(likenum - 1));
            activity.homeChannelList1.get(Channel_position).setIs_likes("0");
        }

    }


    private void InitLandingPageAdapter() {
        activityy.plays = 0;
        like_channel.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);

        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
            //simpleExoPlayer.release();
        }

        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);

        }

        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
        }

        playerViewAds.setVisibility(View.GONE);

        String url = "";

        mainMenuMaster_list = SharedPreference.getInstance(context).getMenuMaster();

        for (int i = 0; i < mainMenuMaster_list.getData().size(); i++) {
            if (mainMenuMaster_list.getData().get(i).getMenuTypeId().equalsIgnoreCase("1")) {
                Glide.with(context)
                        .load(mainMenuMaster_list.getData().get(i).getList().get(i).getImage())
                        .into(videoImage);
                like_number_watch_channel.setText(mainMenuMaster_list.getData().get(i).getList().get(i).getLikes() + " Likes");

                Log.d("Shantanu", mainMenuMaster_list.getData().get(i).getList().get(i).getIs_likes());
                if (mainMenuMaster_list.getData().get(i).getList().get(i).getIs_likes().equalsIgnoreCase("1")) {
                    like_channel_button.setImageResource(R.mipmap.audio_liked);
                    like_channel_button.setTag(true);
                } else {
                    like_channel_button.setImageResource(R.mipmap.white_like);
                    like_channel_button.setTag(false);
                }

                VIEWlike = mainMenuMaster_list.getData().get(i).getList().get(i).getLikes();
                Id = mainMenuMaster_list.getData().get(i).getList().get(i).getId();
                totallike = mainMenuMaster_list.getData().get(i).getList().get(i).getLikes();
                // Current_channel_Name = mainMenuMaster_list.getData().get(i).getList().get(i).getName();
                Current_channel_Name = Id;
                Is_like_no = mainMenuMaster_list.getData().get(i).getList().get(i).getIs_likes();

                Channel_position = i;
                Log.d("check_like", VIEWlike);
                Log.d("check_id", Id);
                Log.d("check_like1", totallike);
                Log.d("check_is_like", Is_like_no);

                url = mainMenuMaster_list.getData().get(i).getList().get(i).getChannelUrl();
                channel_name = mainMenuMaster_list.getData().get(i).getList().get(i).getName();

            }
        }

        swipe_refresh_home1.setRefreshing(false);
        Log.d("shantanu", url);
        finalUrl = url;
        playIconIV.setOnClickListener(v -> {
            Comment_channel.setVisibility(View.VISIBLE);
            activityy.plays = 1;
            Previous_channel_Name = "";
            // updateLiveUsers(false);

            if (finalUrl != null) {
                //  default_channel="1";
                if (((HomeActivityy) getActivity()).playerlayout.getVisibility() == View.VISIBLE) {
                    ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
                    AudioPlayerService.mediaPlayer.pause();
                }
                if (((HomeActivityy) getActivity()).playerlayout1.getVisibility() == View.VISIBLE) {
                    ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);

                }
                if (((HomeActivityy) getActivity()).playerlayout2.getVisibility() == View.VISIBLE) {
                    ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);

                }
                like_channel.setVisibility(View.VISIBLE);
                Comment_channel.setVisibility(View.VISIBLE);
                videoImage.setVisibility(View.GONE);
                playerView1.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String newUrl = finalUrl + "?start=" + previousDate() + "&end=";

                Uri uri = Uri.parse(newUrl);
                channel_url = finalUrl;
                Log.d("shantanu", String.valueOf(uri));
                if (Is_premium.equalsIgnoreCase("0")) {
                    if (SharedPreference.getInstance(context).getLiveTvAds() != null) {
                        liveTvAds = SharedPreference.getInstance(context).getLiveTvAds();
                        if (Networkconstants.adsCount > liveTvAds.length - 1) {
                            Networkconstants.adsCount = 0;
                        }
                        //playads(liveTvAds[Networkconstants.adsCount], uri);
                        playVideo1(uri, channel_name);
                    } else {
                        playVideo1(uri, channel_name);
                    }
                } else {
                    playVideo1(uri, channel_name);
                }

            } else {
                Toast.makeText(context, "url is empty", Toast.LENGTH_SHORT).show();
            }

        });

        MyAdapter adapter = new MyAdapter(context, mainMenuMaster_list.getData(), HomeFragment1.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();
        adapter.notifyDataSetChanged();

    }

    private void playads(AdvertisementAds liveTvAd, Uri uri) {
        adsType = 1;
        adsType_1 = 1;
        ++Networkconstants.adsCount;
        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }

        playerView1.setVisibility(View.GONE);
        playIconIV.setVisibility(View.GONE);
        playerViewAds.setVisibility(View.VISIBLE);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        advertisement_id = liveTvAd.getId();
        time_slot_id = liveTvAd.getTime_slot_id();

        Uri uri1 = Uri.parse(liveTvAd.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        mediaSource = new ExtractorMediaSource(uri1
                , factory, extractorsFactory, null, null);

        playerViewAds.setPlayer(simpleExoPlayerAds);
        //keep screen on
        playerViewAds.setKeepScreenOn(true);

        countAds = 5;
        skip_ads.setText("Skip Ads in 5");

        simpleExoPlayerAds.prepare(mediaSource);

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
                        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
                            skip_ads.setVisibility(View.GONE);
                        }

                        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
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
                    playerViewAds.setVisibility(View.GONE);
                    playerView1.setVisibility(View.VISIBLE);
                    // simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayerAds.setPlayWhenReady(false);
                    // simpleExoPlayerAds.release();
                    if (adsType == 1) {
                        Log.d("shantanu", "playads");
                        Log.d("shantanu", channel_url);
                        advertisement_status = "2";
                        if (timePlay == 0) {
                            activity.pause_1 = 0;
                            timePlay = 1;
                        }
                        updateAPPAdvertisementCounter(false);
                        adsType++;
                        playVideo1(Uri.parse(channel_url), channel_name);
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
                playerViewAds.setVisibility(View.GONE);
                playerView1.setVisibility(View.VISIBLE);
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
                    activity.pause_1 = 0;
                    timePlay = 1;
                }
                playVideo1(Uri.parse(channel_url), channel_name);
            }
        });

    }

    public void updatetimer(long millisecond) {
        countDownTimer = new CountDownTimer(millisecond, 1000) {
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


    }


    @Override
    public void onResume() {

        super.onResume();

        HomeActivityy.notify_ll.setVisibility(View.GONE);

        // fetchData1();
        simpleExoPlayer.setPlayWhenReady(false);
        Log.d(TAG, "onResume: onResumeIsCAlled");
        ((HomeActivityy) context).invalidateOptionsMenu();
        ((HomeActivityy) context).handleToolbar();

        if (activity.pause == 1) {
            //  Current_channel_users=getLiveUsers();
            Log.d("shantanu", "onResume: onResumeIsCAlled");
            activity.plays = 1;
            activity.pause = 0;
            Id = Previous_channel_Name;
            Previous_channel_Name = "";
            //   updateLiveUsers(false);
        }

        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
        }

        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
        }

        activity.pause_1 = 0;
        //adsType=-1;

        Log.d("shantanu in OnResume", String.valueOf(SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS_GAP_DURATION)));
        Log.d("shantanu in OnResume", String.valueOf(SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS)));

        if (SharedPreference.getInstance(context).getInt(Constants.LIVE_USERS) == 1) {
            //   fetchLiveUsers(false);
        }

        if (COME_FROM_LIVETV == true) {


        } else {

        }
        if (Constants.CALL_RESUME.equals("true")) {
            Constants.CALL_RESUME = "false";
            showSongData();
        }

        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);

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
        Log.d(TAG, "onPause: onPauseIsCalled");

        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);

        activity.pause_1 = 1;

        if (activityy.plays == 1) {
            activity.pause = 1;

            Previous_channel_Name = Id;
            //Id = "";
            //   updateLiveUsers(false);
        }

        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
        }

        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
        }


        if (!isItFirstTimeToPlay) {
            afterChangeTime = System.currentTimeMillis();
            differenceBetweenTime = (afterChangeTime - firstChannelPlayTime) / 1000;
            isItFirstTimeToPlay = true;
            UPDATE_USER_PLAY_TIME(false);
        }


        simpleExoPlayer.setPlayWhenReady(false);

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.d(TAG, "onDestroyView: onDestroyViewIsCalled");
        getActivity().unregisterReceiver(playNewAudio);
        getActivity().unregisterReceiver(playpause);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void ErrorCallBack(String message, String apitype) {
        swipe_refresh_home1.setRefreshing(false);
        //   ToastUtil.showDialogBox(context, message);//----by sumit
    }


    private void setChannelRelatedData() {
        if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("0 like");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(totallike + " like");
        } else {
            like_number_watch_channel.setText(totallike + " likes");
        }

        if (activity.homeChannelList1.get(Positon_of_channel).getIs_likes().equals("1")) {
            like_channel_button.setImageResource(R.mipmap.audio_liked);
            like_channel_button.setTag(true);
        } else if (
                Is_like_no.equals("0")) {
            like_channel_button.setImageResource(R.mipmap.white_like);
            like_channel_button.setTag(false);
        }
    }


}