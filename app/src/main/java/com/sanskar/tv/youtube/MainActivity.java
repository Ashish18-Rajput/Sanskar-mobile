package com.sanskar.tv.youtube;

import static com.sanskar.tv.Others.Helper.Utils.clearEditText;
import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListVideoAdapter.videoplaylisttype;
import static com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity.isConnectingToInternet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.jwPlayer.KeepScreenOnHandler;
import com.sanskar.tv.model.VideoPlaylistModel;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter1;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter2;
import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.sqlite.DatabaseManager;
import com.sanskar.tv.utility.YoutubeExtractor.ExtractorException;
import com.sanskar.tv.utility.YoutubeExtractor.YoutubeStreamExtractor;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTMedia;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTSubtitles;
import com.sanskar.tv.utility.YoutubeExtractor.model.YoutubeMeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        NetworkCall.MyNetworkCallBack, View.OnClickListener {
    //YouTubePlayerView youTubePlayerView;

    private YouTubePlayer mPlayer = null;
    public Videos video;
    public static Videos guru_video;
    public static VideoPlaylistModel videoPlaylistModel;
    File file;
    public static int currentPosition, cu;
    RelativeLayout rl_add_to_playlist;
    ImageView iv_add_to_playlist;
    DatabaseManager databaseManager;
    TextView tv_add_to_playlist;
    int count;
    public int ads_counts = -1;
    Boolean is_post_exit = true;
    String url1;
    List<Videos> videosList = new ArrayList<>();
    List<MenuMasterList> videosList1 = new ArrayList<>();
    private Videos[] videos;
    private static Videos[] videosArray;
    private static VideoPlaylistModel[] videoPlaylistModels;

    MenuMasterList menuMasterList;
    MenuMasterList[] masterLists, masterLists1;

    private Videos[] vd = new Videos[]{};
    private MenuMasterList[] vd1 = new MenuMasterList[]{};
    private int mPage = 1;
    private int pos;
    private int arrayIndex = 0;
    private NetworkCall networkCall;
    KeepScreenOnHandler keepScreenOnHandler;
    String url;
    private RelativeLayout noDataFound;
    TextView name, description, date;
    ImageView back;
    private int pageSize;
    int ads_type = -1;
    RecyclerView relatedVideosList;
    ViewAllAdapter adapter;
    ViewAllAdapter1 adapter1;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout share;
    TextView viewNumber, likeNumber, skip_ads;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText comment;
    TextView postComment;
    int type;
    int countAds = 5;
    NestedScrollView parent_related;
    private ArrayList<Videos> videoResponses;
    String advertisement_id = "", time_slot_id = "", advertisement_status = "";
    private LinearLayout nonFullScreenLayout;
    private ImageView like;
    private boolean loading = false;
    ProgressBar commentProgress;
    private TextView commentNumbers, downloadwatch_video;
    private Context context;
    private CircleImageView profileImg;
    public SignupResponse signupResponse;
    private RelativeLayout container;
    private boolean isLike = false;
    private boolean isComment = false;
    private RelativeLayout layout, toolbar;
    private long enqueue;
    private DownloadManager dm;
    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    ArrayList<Long> list = new ArrayList<>();
    private ImageView download;
    private long downloadID;
    ImageView profile_iv;
    ImageView tv_iv;
    public PlayerView playerView, playerViewAds;
    public ProgressBar progressBar;
    public ImageView full_Screen, option_menu;
    public SimpleExoPlayer simpleExoPlayer, simpleExoPlayerAds;
    DefaultHttpDataSourceFactory factory;
    TrackSelector trackSelector;
    BandwidthMeter bandwidthMeter;
    LoadControl loadControl;
    boolean flag = false;
    private HomeActivityy activityy;
    String livevideourl;
    String from;
    PlaylistItem item;
    CardView cardView;
    CircularProgressBar circularProgressBar;
    MediaSource mediaSource;
    ImageView exo_previousVideo, exo_nextVideo;
    String BASE_URL = "https://www.youtube.com";
    private Button btn_paynow, btn_donate, btn_back;
    private RelativeLayout donationLayout, paymentlayout;
    String youtubeLink;
    private EditText donationEdittext;
    private LinearLayout phonepeLayout, googleLayout, paytmLayout, debitcardLayout;
    TextView donationAmount;
    String pause_at = "";

    int openActivityCounter = 0;
    String status = "", total_play_time = "";
    String id = "";
    List<Videos> videosArrayList = new ArrayList<>();
    List<MenuMasterList> videosMenuArrayList = new ArrayList<>();
    ViewAllAdapter2 viewAllAdapter;
    TextView quality;

    AdvertisementAds[] videosAds;
    String MenuTypeId = "";
    HomeActivityy homeActivityy;
    private String Is_premium = "";

    YouTubePlayerView youTubePlayerView;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                //   Toast.makeText(HomeActivityy.this, "Download Completed", Toast.LENGTH_SHORT).show();
                download.setImageResource(R.drawable.download_complete);
                downloadwatch_video.setText("Downloaded");

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        databaseManager = new DatabaseManager(MainActivity.this);
        initView();
        donationPay();

        context = getApplicationContext();
        if (SharedPreference.getInstance(this).getString(Constants.IS_PREMIUM) != null) {
            Is_premium = SharedPreference.getInstance(this).getString(Constants.IS_PREMIUM);
        }

        loadControl = new DefaultLoadControl();

        bandwidthMeter = new DefaultBandwidthMeter();

        trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                MainActivity.this, trackSelector, loadControl
        );

        simpleExoPlayerAds = ExoPlayerFactory.newSimpleInstance(
                MainActivity.this, trackSelector, loadControl
        );

        factory = new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );

        swipeRefreshLayout.setOnRefreshListener(() -> getRelatedVideos());

        if (masterLists != null) {
            masterLists = null;
        }

        if (videosArray != null) {
            videosArray = null;
        }

        getBundleData();
        setVideoData();

        if (SharedPreference.getInstance(context).getVideosAds() != null) {
            videosAds = SharedPreference.getInstance(context).getVideosAds();
        }

        if (Is_premium.equalsIgnoreCase("0")) {
            if (SharedPreference.getInstance(context).getVideosAds() != null && SharedPreference.getInstance(context).getVideosAds().length != 0) {
                Log.d("shantanu", " MainActivity" + ads_counts);
                playerView.setVisibility(View.GONE);
                playerViewAds.setVisibility(View.VISIBLE);
                if (ads_counts >= videosAds.length) {
                    HomeActivityy.ads_count = 0;
                    Networkconstants.adsCount = 0;
                    ads_counts = 0;
                }
                videosAds = SharedPreference.getInstance(context).getVideosAds();
                playAds(videosAds[ads_counts]);
            } else {
                //playerView.setVisibility(View.VISIBLE);
                playerViewAds.setVisibility(View.GONE);
                if (videoplaylisttype == 1) {
                } else {
                    // youtubeLink = BASE_URL + "/watch?v=" + video.getYoutube_url();
                    if (videosArray != null) {
                        youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                        Log.d("VideoExopLayer", youtubeLink);

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                            }

                            @Override
                            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                                super.onStateChange(youTubePlayer, state);
                                if (state == PlayerConstants.PlayerState.ENDED){
                                    setUpNextVideos();
                                }
                            }
                        });
                    }
                    if (masterLists != null) {
                        youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                        Log.d("VideoExopLayer", youtubeLink);


                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                            }

                            @Override
                            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                                super.onStateChange(youTubePlayer, state);
                                if (state == PlayerConstants.PlayerState.ENDED){
                                    setUpNextVideos();
                                }
                            }
                        });
                    }
                }
            }
        } else {
            playerViewAds.setVisibility(View.GONE);
            if (videoplaylisttype == 1) {
            } else {
                if (videosArray != null) {
                    youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                    Log.d("VideoExopLayer", youtubeLink);


                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                        }
                        @Override
                        public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                            super.onStateChange(youTubePlayer, state);
                            if (state == PlayerConstants.PlayerState.ENDED){
                                setUpNextVideos();
                            }
                        }
                    });

                }
                if (masterLists != null) {
                    youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                    Log.d("VideoExopLayer", youtubeLink);


                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                        }

                        @Override
                        public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                            super.onStateChange(youTubePlayer, state);
                            if (state == PlayerConstants.PlayerState.ENDED){
                                setUpNextVideos();
                            }
                        }
                    });
                }
            }
        }

        full_Screen.setImageDrawable(getResources()
                .getDrawable(R.drawable.ic_fullscreen_exit));

        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);*/

        flag = true;

        fetchData(false);
        getRelatedVideos();

        parent_related.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        if (is_post_exit == true) {

                            //getrelatedvideo(true);
                            //getRelatedVideos()
                            getRelatedVideos();
                        } else if (is_post_exit == false) {
                            Toast.makeText(context, "BhajanCatData not found!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        viewAllAdapter = new ViewAllAdapter2(videosArrayList, this);
        relatedVideosList.setAdapter(viewAllAdapter);

    }

    private void fetchData(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, this);
            networkCall.NetworkAPICall(API.VIEW_VIDEO, false);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void CONTINUE_WATCHING(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.CONTINUE_WATCHING, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void UPDATE_USER_PLAY_TIME(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.COUNTRY_WISE_USER_PLAY_HISTORY, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
            swipeRefreshLayout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) youTubePlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide your objects here.
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) youTubePlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
        }
    }

    public void playAds(AdvertisementAds video_Ads) {

        ads_type = 1;

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (video_Ads.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (video_Ads.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }
        advertisement_id = video_Ads.getId();
        time_slot_id = video_Ads.getTime_slot_id();
        Uri uri = Uri.parse(video_Ads.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

        mediaSource = new ExtractorMediaSource(uri
                , factory, extractorsFactory, null, null);

        playerViewAds.setPlayer(simpleExoPlayerAds);
        //keep screen on
        playerViewAds.setKeepScreenOn(true);

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
                    if (video_Ads.getSkip().equalsIgnoreCase("0")) {
                        skip_ads.setVisibility(View.GONE);
                    }

                    if (video_Ads.getSkip().equalsIgnoreCase("1")) {
                        skip_ads.setVisibility(View.VISIBLE);
                    }
                    if (ads_type == 1) {
                        updatetimer();
                        ads_type = 3;
                    }

                }
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    progressBar.setVisibility(View.GONE);
                    playerViewAds.setVisibility(View.GONE);
                    //playerView.setVisibility(View.VISIBLE);
                    // simpleExoPlayer.setPlayWhenReady(true);
                    simpleExoPlayerAds.setPlayWhenReady(false);
                    // simpleExoPlayerAds.release();
                    advertisement_status = "2";
                    updateAPPAdvertisementCounter(false);

                    if (videoplaylisttype == 1) {
                        // playVideo(Uri.parse(videoPlaylistModel.get_url()));
                        // Log.d("VideoPlayListModel", videoPlaylistModel.get_url());
                    } else {
                        // youtubeLink = BASE_URL + "/watch?v=" + video.getYoutube_url();
                        if (videosArray != null) {
                            youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                            Log.d("VideoExopLayer", youtubeLink);
                            /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {

                                @Override
                                public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                    //   progress.dismiss();
                                    if (adativeStream.isEmpty()) {
                                        // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                        // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (muxedStream.isEmpty()) {
                                        //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                        //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String downloadUrl = muxedStream.get(0).getUrl();
                                    Uri url = Uri.parse(downloadUrl);
                                    playVideo(url);
                                }

                                @Override
                                public void onExtractionGoesWrong(final ExtractorException e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).useDefaultLogin().Extract(youtubeLink);*/

                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                    youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                                }
                            });

                        }
                        if (masterLists != null) {
                            youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                            Log.d("VideoExopLayer", youtubeLink);

                            /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                                @Override
                                public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                    //   progress.dismiss();
                                    if (adativeStream.isEmpty()) {
                                        // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                        // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (muxedStream.isEmpty()) {
                                        //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                        //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String downloadUrl = muxedStream.get(0).getUrl();
                                    Uri url = Uri.parse(downloadUrl);
                                    playVideo(url);
                                }

                                @Override
                                public void onExtractionGoesWrong(final ExtractorException e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }).useDefaultLogin().Extract(youtubeLink);*/

                            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                @Override
                                public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                    youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                                }
                            });
                        }

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
                //progressBar.setVisibility(View.VISIBLE);
                playerViewAds.setVisibility(View.GONE);
                //playerView.setVisibility(View.VISIBLE);
                //simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayerAds.setPlayWhenReady(false);
                advertisement_status = "1";
                updateAPPAdvertisementCounter(false);
                // simpleExoPlayerAds.release();
                if (videoplaylisttype == 1) {
                    // playVideo(Uri.parse(videoPlaylistModel.get_url()));
                    // Log.d("VideoPlayListModel", videoPlaylistModel.get_url());
                } else {
                    // youtubeLink = BASE_URL + "/watch?v=" + video.getYoutube_url();
                    if (videosArray != null) {
                        youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                        Log.d("VideoExopLayer", youtubeLink);
                        /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                            @Override
                            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                //   progress.dismiss();
                                if (adativeStream.isEmpty()) {
                                    // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                    // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (muxedStream.isEmpty()) {
                                    //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                    //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String downloadUrl = muxedStream.get(0).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                            }

                            @Override
                            public void onExtractionGoesWrong(final ExtractorException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).useDefaultLogin().Extract(youtubeLink);*/

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                            }
                        });

                    }
                    if (masterLists != null) {
                        youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                        Log.d("VideoExopLayer", youtubeLink);

                        /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                            @Override
                            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                    // progress.dismiss();
                                if (adativeStream.isEmpty()) {
                                    // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                    // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (muxedStream.isEmpty()) {
                                    // Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                    // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String downloadUrl = muxedStream.get(0).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                            }

                            @Override
                            public void onExtractionGoesWrong(final ExtractorException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).useDefaultLogin().Extract(youtubeLink);
           */
                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                            }
                        });
                    }

                }
            }
        });
    }

    private void updateAPPAdvertisementCounter(boolean b) {
        if (Utils.isConnectingToInternet(this)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.UPDATE_APP_ADVERTISEMENT_COUNTER, b);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void playAds1(AdvertisementAds video_Ads, int count) {

        ads_type = 2;

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


        Uri uri = Uri.parse(video_Ads.getMedia());
        // Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");
        if (video_Ads.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (video_Ads.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }
        mediaSource = new ExtractorMediaSource(uri
                , factory, extractorsFactory, null, null);

        playerViewAds.setPlayer(simpleExoPlayerAds);
        //keep screen on
        playerViewAds.setKeepScreenOn(true);

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
                    if (video_Ads.getSkip().equalsIgnoreCase("0")) {
                        skip_ads.setVisibility(View.GONE);
                    }

                    if (video_Ads.getSkip().equalsIgnoreCase("1")) {
                        skip_ads.setVisibility(View.VISIBLE);
                    }
                    if (ads_type == 2) {
                        updatetimer();
                        ads_type++;
                    }

                }
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    // progressBar.setVisibility(View.VISIBLE);
                    playerViewAds.setVisibility(View.GONE);
                    //playerView.setVisibility(View.VISIBLE);
                    simpleExoPlayerAds.setPlayWhenReady(false);
                    // simpleExoPlayerAds.release();

                    if (videosArray != null) {
                        id = videosArray[count].getId();
                        name.setText(videosArray[count].getAuthor_name());
                        date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));

                        if (videosArray[count].getIs_like().equals("0")) {
                            like.setImageResource(R.drawable.like_default);
                            like.setTag(false);
                        }
                        String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                        description.setText(desc);
                        if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                            commentNumbers.setText("No comment");
                            commentNumbers.setEnabled(false);
                        } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                            commentNumbers.setText("View comment");
                            commentNumbers.setEnabled(true);
                        } else {
                            commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                            commentNumbers.setEnabled(true);
                        }

                        if (videosArray[count].getLikes().equals("0")) {
                            likeNumber.setText("0 like");
                        } else if (videosArray[count].getLikes().equals("1")) {
                            likeNumber.setText(videosArray[count].getLikes() + " like");
                        } else {
                            likeNumber.setText(videosArray[count].getLikes() + " likes");
                        }

                        if (videosArray[count].getViews().equals("0")) {
                            viewNumber.setText("no view");
                        } else if (videosArray[count].getViews().equals("1")) {
                            viewNumber.setText(videosArray[count].getViews() + " view");
                        } else {
                            viewNumber.setText(videosArray[count].getViews() + " views");
                        }

                        if (videosArray[count].getIs_like().equals("1")) {
                            like.setImageResource(R.drawable.liked);
                            like.setTag(true);
                        }

                        youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();
                        // videosList.add(videosArray[count]);
                        videosList.add(videosArray[count]);
                        /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                            @Override
                            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                //   progress.dismiss();
                                if (adativeStream.isEmpty()) {
                                    // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                    // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (muxedStream.isEmpty()) {
                                    //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                    //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String downloadUrl = muxedStream.get(0).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                            }

                            @Override
                            public void onExtractionGoesWrong(final ExtractorException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).useDefaultLogin().Extract(youtubeLink);*/

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                            }
                        });

                    }
                    if (masterLists != null) {
                        id = masterLists[count].getId();
                        name.setText(masterLists[count].getAuthorName());
                        date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));

                        if (masterLists[count].getIs_like().equals("0")) {
                            like.setImageResource(R.drawable.like_default);
                            like.setTag(false);
                        }

                        String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                        description.setText(desc);
                        if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                            commentNumbers.setText("No comment");
                            commentNumbers.setEnabled(false);
                        } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                            commentNumbers.setText("View comment");
                            commentNumbers.setEnabled(true);
                        } else {
                            commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                            commentNumbers.setEnabled(true);
                        }

                        if (masterLists[count].getLikes().equals("0")) {
                            likeNumber.setText("0 like");
                        } else if (masterLists[count].getLikes().equals("1")) {
                            likeNumber.setText(masterLists[count].getLikes() + " like");
                        } else {
                            likeNumber.setText(masterLists[count].getLikes() + " likes");
                        }

                        if (masterLists[count].getViews().equals("0")) {
                            viewNumber.setText("no view");
                        } else if (masterLists[count].getViews().equals("1")) {
                            viewNumber.setText(masterLists[count].getViews() + " view");
                        } else {
                            viewNumber.setText(masterLists[count].getViews() + " views");
                        }

                        if (masterLists[count].getIs_like().equals("1")) {
                            like.setImageResource(R.drawable.liked);
                            like.setTag(true);
                        }

                        youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();
                        // videosList.add(videosArray[count]);
                        videosList1.add(masterLists[count]);
                        /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                            @Override
                            public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                //   progress.dismiss();
                                if (adativeStream.isEmpty()) {
                                    // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                    // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (muxedStream.isEmpty()) {
                                    //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                    //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String downloadUrl = muxedStream.get(0).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                            }

                            @Override
                            public void onExtractionGoesWrong(final ExtractorException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).useDefaultLogin().Extract(youtubeLink);*/

                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(masterLists[count].getYoutubeUrl(), 0);
                            }
                        });

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
                // progressBar.setVisibility(View.VISIBLE);
                playerViewAds.setVisibility(View.GONE);
                // playerView.setVisibility(View.VISIBLE);
                //simpleExoPlayer.setPlayWhenReady(true);
                simpleExoPlayerAds.setPlayWhenReady(false);
                // simpleExoPlayerAds.release();
                if (videosArray != null) {
                    id = videosArray[count].getId();
                    name.setText(videosArray[count].getAuthor_name());
                    date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));

                    if (videosArray[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);
                        like.setTag(false);
                    }

                    String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                    description.setText(desc);
                    if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (videosArray[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (videosArray[count].getLikes().equals("1")) {
                        likeNumber.setText(videosArray[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(videosArray[count].getLikes() + " likes");
                    }

                    if (videosArray[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (videosArray[count].getViews().equals("1")) {
                        viewNumber.setText(videosArray[count].getViews() + " view");
                    } else {
                        viewNumber.setText(videosArray[count].getViews() + " views");
                    }

                    if (videosArray[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }

                    youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();
                    // videosList.add(videosArray[count]);
                    videosList.add(videosArray[count]);
                    /*new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                            //   progress.dismiss();
                            if (adativeStream.isEmpty()) {
                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);*/

                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(videosArray[count].getYoutube_url(), 0);
                        }
                    });

                }
                if (masterLists != null) {
                    id = masterLists[count].getId();
                    name.setText(masterLists[count].getAuthorName());
                    date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));

                    if (masterLists[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);
                        like.setTag(false);
                    }

                    String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                    description.setText(desc);
                    if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (masterLists[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (masterLists[count].getLikes().equals("1")) {
                        likeNumber.setText(masterLists[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(masterLists[count].getLikes() + " likes");
                    }

                    if (masterLists[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (masterLists[count].getViews().equals("1")) {
                        viewNumber.setText(masterLists[count].getViews() + " view");
                    } else {
                        viewNumber.setText(masterLists[count].getViews() + " views");
                    }

                    if (masterLists[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }

                    youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();
                    // videosList.add(videosArray[count]);
                    videosList1.add(masterLists[count]);
                  /*  new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                // progress.dismiss();
                            if (adativeStream.isEmpty()) {
                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);
*/
                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(masterLists[count].getYoutubeUrl(), 0);
                        }
                    });
                }
            }
        });
    }

    public void updatetimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skip_ads.setText("Skip ads in " + (countAds - 1));

                if (countAds != 0) {
                    countAds = countAds - 1;
                    updatetimer();
                }
                if (countAds == 0) {
                    skip_ads.setText("Skip =>");
                }
            }
        }, 999);

    }

    public void playVideo(Uri uri) {


        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        mediaSource = new ExtractorMediaSource(uri
                , factory, extractorsFactory, null, null);

        playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView.setKeepScreenOn(true);

        simpleExoPlayer.prepare(mediaSource);
        if (masterLists != null) {
            if (menuMasterList.getPause_at() != null)
                simpleExoPlayer.seekTo(Long.parseLong(menuMasterList.getPause_at()));
        }

        simpleExoPlayer.setPlayWhenReady(true);

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
                if (playWhenReady) {
                    progressBar.setVisibility(View.GONE);
                }
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_READY) {
                    progressBar.setVisibility(View.GONE);
                   /* simpleExoPlayer.setPlayWhenReady(false);
                    playerView.setVisibility(View.GONE);
                    playerViewAds.setVisibility(View.VISIBLE);
                    playAds();*/
                } else if (playbackState == Player.STATE_ENDED) {
                    status = "2";
                    if (Is_premium.equalsIgnoreCase("0")) {

                        if (SharedPreference.getInstance(context).getVideosAds() != null) {
                            Log.d("shantanu", " MainActivity" + ads_counts);
                            playerView.setVisibility(View.GONE);
                            playerViewAds.setVisibility(View.VISIBLE);
                            if (ads_counts >= videosAds.length) {
                                HomeActivityy.ads_count = 0;
                                Networkconstants.adsCount = 0;
                                ads_counts = 0;
                            }
                            videosAds = SharedPreference.getInstance(context).getVideosAds();
                            if (videosArray != null) {
                                if (pos != -1) {
                                    if (pos > videosArray.length - 1) {
                                        pos = 0;
                                    } else {
                                        pos = pos + 1;
                                    }
                                }
                            }
                            if (masterLists != null) {
                                if (pos != -1) {
                                    if (pos > masterLists.length - 1) {
                                        pos = 0;
                                    } else {
                                        pos = pos + 1;
                                    }
                                }
                            }
                            countAds = 5;
                            playAds1(videosAds[ads_counts], pos);
                            setVideoData1(pos);
                        } else {
                            // playerView.setVisibility(View.VISIBLE);
                            playerViewAds.setVisibility(View.GONE);
                            if (videoplaylisttype == 1) {
                                // playVideo(Uri.parse(videoPlaylistModel.get_url()));
                                // Log.d("VideoPlayListModel", videoPlaylistModel.get_url());
                            } else {
                                // youtubeLink = BASE_URL + "/watch?v=" + video.getYoutube_url();
                                if (videosArray != null) {
                                    if (pos != -1) {
                                        if (pos > videosArray.length - 1) {
                                            pos = 0;
                                        } else {
                                            pos = pos + 1;
                                        }
                                    }
                                    Log.d("shantanu", "onPlayerStateChanged: pos: " + pos);
                                    setVideoData1(pos);

                                    youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                                    Log.d("VideoExopLayer", youtubeLink);
                                   /* new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {

                                        @Override
                                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                            //   progress.dismiss();
                                            if (adativeStream.isEmpty()) {
                                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (muxedStream.isEmpty()) {
                                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            String downloadUrl = muxedStream.get(0).getUrl();
                                            Uri url = Uri.parse(downloadUrl);
                                            playVideo(url);
                                        }

                                        @Override
                                        public void onExtractionGoesWrong(final ExtractorException e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }).useDefaultLogin().Extract(youtubeLink);
*/
                                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                            youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                                        }
                                    });
                                }
                                if (masterLists != null) {
                                    if (pos != -1) {
                                        if (pos > masterLists.length - 1) {
                                            pos = 0;
                                        } else {
                                            pos = pos + 1;
                                        }
                                    }
                                    setVideoData1(pos);
                                    Log.d("shantanu", "onPlayerStateChanged: pos: " + pos);
                                    youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                                    Log.d("VideoExopLayer", youtubeLink);

                                   /* new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                                        @Override
                                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                                            //   progress.dismiss();
                                            if (adativeStream.isEmpty()) {
                                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            if (muxedStream.isEmpty()) {
                                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            String downloadUrl = muxedStream.get(0).getUrl();
                                            Uri url = Uri.parse(downloadUrl);
                                            playVideo(url);
                                        }

                                        @Override
                                        public void onExtractionGoesWrong(final ExtractorException e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }).useDefaultLogin().Extract(youtubeLink);
                                */
                                    youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                        @Override
                                        public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                            youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        // playerView.setVisibility(View.VISIBLE);
                        playerViewAds.setVisibility(View.GONE);
                        if (videoplaylisttype == 1) {
                            // playVideo(Uri.parse(videoPlaylistModel.get_url()));
                            // Log.d("VideoPlayListModel", videoPlaylistModel.get_url());
                        } else {
                            if (videosArray != null) {
                                if (pos != -1) {
                                    if (pos > videosArray.length - 1) {
                                        pos = 0;
                                    } else {
                                        pos = pos + 1;
                                    }
                                }
                                Log.d("shantanu", "onPlayerStateChanged: pos: " + pos);
                                setVideoData1(pos);

                                youtubeLink = BASE_URL + "/watch?v=" + videosArray[pos].getYoutube_url();
                                Log.d("VideoExopLayer", youtubeLink);
                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                        youTubePlayer.loadVideo(videosArray[pos].getYoutube_url(), 0);
                                    }
                                });
                            }
                            if (masterLists != null) {
                                if (pos != -1) {
                                    if (pos > masterLists.length - 1) {
                                        pos = 0;
                                    } else {
                                        pos = pos + 1;
                                    }
                                }
                                setVideoData1(pos);
                                Log.d("shantanu", "onPlayerStateChanged: pos: " + pos);
                                youtubeLink = BASE_URL + "/watch?v=" + menuMasterList.getYoutubeUrl();
                                Log.d("VideoExopLayer", youtubeLink);

                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                                        youTubePlayer.loadVideo(menuMasterList.getYoutubeUrl(), 0);
                                    }
                                });

                            }
                        }
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

        yoyo();
        full_Screen.setOnClickListener(view -> {
            if (flag) {
                full_Screen.setImageDrawable(getResources().getDrawable(R.drawable.ic_full));

                //set portrait orientation

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);


                flag = false;
            } else {
                full_Screen.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_fullscreen_exit));

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


                flag = true;
            }
        });

        boolean[] checkedItems = {false, true, false, false, false, false};
        option_menu.setOnClickListener(v -> {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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

    public void playbackSpeed(float speed) {
        PlaybackParams params = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params = new PlaybackParams();
            params.setSpeed(speed);
        }
        simpleExoPlayer.setPlaybackParams(params);
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = vd.length;
    }


    private void yoyo() {
        initialState();
        is_post_exit = true;
        getRelatedVideos();
        /*search_content = "";
        activityy.searchContent = "";*/
        swipeRefreshLayout.setRefreshing(false);
    }


    private void initView() {

        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.initialize(new YouTubePlayerListener() {
            @Override
            public void onReady(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                youTubePlayer.play();
            }

            @Override
            public void onStateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {
                if (playerState == PlayerConstants.PlayerState.ENDED) {
                    status = "1";
                }
                if (playerState == PlayerConstants.PlayerState.PAUSED) {
                    status = "0";
                }
            }

            @Override
            public void onPlaybackQualityChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onError(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {
                pause_at = String.valueOf(v);
            }

            @Override
            public void onVideoDuration(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer, @NonNull String s) {

            }

            @Override
            public void onApiChange(@NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {

            }
        }, true, new IFramePlayerOptions.Builder().controls(1).ivLoadPolicy(1).build());

        youTubePlayerView.getPlayerUiController().showYouTubeButton(false);
        youTubePlayerView.getPlayerUiController().showMenuButton(false);
        youTubePlayerView.getPlayerUiController().showVideoTitle(false);

        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        , WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                swipeRefreshLayout.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) youTubePlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                playerView.setLayoutParams(params);
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) youTubePlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = (int) (300 * getApplicationContext().getResources().getDisplayMetrics().density);
                playerView.setLayoutParams(params);
            }
        });
//
        exo_nextVideo = findViewById(R.id.exo_nextVideo);
        exo_nextVideo.setOnClickListener(v -> {
            if (masterLists != null) {

                if (count == masterLists.length) {
                    // nextVideo(count);   //----by sumit
                    //count++;
                    arrayIndex = count - 1;
                    nextVideo(arrayIndex - 1);
                    Log.d("count==", String.valueOf(count));
                } else {
                    nextVideo(count);
                    count++;
                    arrayIndex = count - 2;
                    Log.d("count==", String.valueOf(count));
                }
            }
            if (videosArray != null) {

                if (count == videosArray.length) {
                    //  nextVideo(count);------------by sumit
                    //count++;
                    arrayIndex = count - 1;
                    nextVideo(arrayIndex);

                    Log.d("count==", String.valueOf(count));
                } else {
                    nextVideo(count);
                    count++;
                    arrayIndex = count - 2;
                    Log.d("count==", String.valueOf(count));
                }
            }

            exo_previousVideo.setVisibility(View.VISIBLE);

        });
        donationAmount = findViewById(R.id.donationAmount);

        donationEdittext = findViewById(R.id.donation_edittext);

        phonepeLayout = findViewById(R.id.phonepeLayout);
        phonepeLayout.setOnClickListener(this);
        googleLayout = findViewById(R.id.googleLayout);
        googleLayout.setOnClickListener(this);
        paytmLayout = findViewById(R.id.paytmlayout);
        paytmLayout.setOnClickListener(this);
        debitcardLayout = findViewById(R.id.debitcardLayout);
        debitcardLayout.setOnClickListener(this);

        btn_paynow = findViewById(R.id.btn_paynow);
        btn_donate = findViewById(R.id.btn_donate);
        btn_back = findViewById(R.id.btn_back);

        donationLayout = findViewById(R.id.donation_layout);
        paymentlayout = findViewById(R.id.payment_layout);

        exo_previousVideo = findViewById(R.id.exo_previousVideo);
        exo_previousVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayIndex == 0) {
                    exo_previousVideo.setVisibility(View.GONE);
                    previousVideo(arrayIndex);
                    count--;
                    //  arrayIndex--;
                    Log.d("array==", String.valueOf(arrayIndex));
                } else {
                    exo_previousVideo.setVisibility(View.VISIBLE);
                    previousVideo(arrayIndex);
                    count--;
                    arrayIndex--;
                }
            }
        });
        playerView = findViewById(R.id.player_view_exo);
        skip_ads = findViewById(R.id.skip_ads);

        playerViewAds = findViewById(R.id.player_view_exo_ads);
        progressBar = findViewById(R.id.progress_bar);
        full_Screen = findViewById(R.id.bt_full);
        option_menu = findViewById(R.id.option_menu);
        quality = findViewById(R.id.quality);
        quality.setVisibility(View.GONE);

        cardView = findViewById(R.id.cardview);
        parent_related = findViewById(R.id.parent_related);
        toolbar = findViewById(R.id.toolbar);
        name = findViewById(R.id.name_watch_video);
        description = findViewById(R.id.description_watch_video);
        date = findViewById(R.id.date_watch_video);

        iv_add_to_playlist = findViewById(R.id.iv_add_to_playlist);
        tv_add_to_playlist = findViewById(R.id.tv_add_to_playlist);
        back = findViewById(R.id.back);
        share = findViewById(R.id.share_watch_video);
        viewNumber = findViewById(R.id.views_number_watch_video);
        like = findViewById(R.id.like_video_watch_video);
        like.setTag(false);
        likeNumber = findViewById(R.id.like_number_watch_video);
        nonFullScreenLayout = findViewById(R.id.watch_video_non_full_screen);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_watch_video);
        //title = findViewById(R.id.toolbar_title);
        noDataFound = findViewById(R.id.no_data_found_watvh_video);
        downloadwatch_video = findViewById(R.id.downloadwatch_video);
        comment = findViewById(R.id.watch_video_comment_et);
        postComment = findViewById(R.id.watch_video_comment_post_tv);
        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    postComment.setTextColor(getResources().getColor(R.color.colorAccent));
                else
                    postComment.setTextColor(getResources().getColor(R.color.text_color_2));
            }
        });

        circularProgressBar = findViewById(R.id.circularProgressBar);

        commentProgress = findViewById(R.id.watch_video_comment_post_pb);
        commentNumbers = findViewById(R.id.fragment_watch_video_tv_view_all_comments);
        profileImg = findViewById(R.id.fragment_watch_comment_image);

        container = findViewById(R.id.container);
        layout = findViewById(R.id.layout);
        download = findViewById(R.id.download);
        profile_iv = findViewById(R.id.profile_iv);
        tv_iv = findViewById(R.id.tv_iv);

        share.setOnClickListener(this);
        back.setOnClickListener(this);
        like.setOnClickListener(this);
        postComment.setOnClickListener(this);
        commentNumbers.setOnClickListener(this);
        download.setOnClickListener(this);
        profile_iv.setOnClickListener(this);
        tv_iv.setOnClickListener(this);
        iv_add_to_playlist.setOnClickListener(this);


        relatedVideosList = findViewById(R.id.related_video_recycler_view_watch_video);
        relatedVideosList.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this);
        relatedVideosList.setLayoutManager(layoutManager);


        circularProgressBar.setProgressMax(200f);
        circularProgressBar.setProgressBarColor(getResources().getColor(R.color.download_image));
        circularProgressBar.setProgressBarWidth(7f);
        circularProgressBar.setBackgroundProgressBarWidth(3f);

        videoResponses = new ArrayList<>();

        if (signupResponse.getProfile_picture().toString().isEmpty()
                || signupResponse.getProfile_picture().toString() == null) {
            profileImg.setImageResource(R.mipmap.profile);
        } else {
            Ion.with(this).load(signupResponse.getProfile_picture().toString())
                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            profileImg.setImageBitmap(result);
                        }
                    });
        }

       /* file = new File(getExternalFilesDir("bhakti"), video.getVideo_title()+".jwp");
        if (file.exists()) {
            download.setImageResource(R.drawable.download_complete);
            downloadwatch_video.setText("Downloaded");
        }else {
            download.setImageResource(R.drawable.download);
            downloadwatch_video.setText("Download");
        }*/
        iv_add_to_playlist.setClickable(true);

    }


    private void setUpNextVideos(){
        if (masterLists != null) {

            if (count == masterLists.length) {
                // nextVideo(count);   //----by sumit
                //count++;
                arrayIndex = count - 1;
                nextVideo(arrayIndex - 1);
                Log.d("count==", String.valueOf(count));
            } else {
                nextVideo(count);
                count++;
                arrayIndex = count - 2;
                Log.d("count==", String.valueOf(count));
            }
        }
        if (videosArray != null) {

            if (count == videosArray.length) {
                //  nextVideo(count);------------by sumit
                //count++;
                arrayIndex = count - 1;
                nextVideo(arrayIndex);

                Log.d("count==", String.valueOf(count));
            } else {
                nextVideo(count);
                count++;
                arrayIndex = count - 2;
                Log.d("count==", String.valueOf(count));
            }
        }
    }

    private void donationPay() {
        btn_donate.setOnClickListener(v -> {
            String donationamont = donationEdittext.getText().toString();
            if (donationamont.isEmpty()) {
                donationEdittext.setError("Enter Amount..");
            } else {
                donationLayout.setVisibility(View.GONE);
                paymentlayout.setVisibility(View.VISIBLE);
                donationAmount.setText("₹" + donationamont);
            }
        });

        btn_back.setOnClickListener(v -> {
            donationLayout.setVisibility(View.VISIBLE);
            paymentlayout.setVisibility(View.GONE);
            donationEdittext.setText("");
        });

        btn_paynow.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Donation SuccessFull", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(MainActivity.this,MainActivity2.class));
        });

    }

    private void previousVideo(int count) {
        long playTime = simpleExoPlayer.getCurrentPosition() / 1000;
        total_play_time = String.valueOf(playTime);
        pause_at = String.valueOf(simpleExoPlayer.getCurrentPosition());

        if (status.equalsIgnoreCase("2")) {
            status = "2";
        } else if (status.equalsIgnoreCase("1")) {
            status = "1";
        } else {
            status = "0";
        }

        if (simpleExoPlayer.getContentPosition() < simpleExoPlayer.getDuration()) {
            if (openActivityCounter == 0) {
                openActivityCounter++;
                status = "1";
            }
        }

        if (Is_premium.equalsIgnoreCase("0")) {
            if (SharedPreference.getInstance(context).getVideosAds() != null && SharedPreference.getInstance(context).getVideosAds().length != 0) {
                countAds = 5;
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.setPlayWhenReady(false);
                }
                playerView.setVisibility(View.GONE);
                playerViewAds.setVisibility(View.VISIBLE);

                ++ads_counts;
                if (ads_counts >= videosAds.length) {
                    HomeActivityy.ads_count = 0;
                    Networkconstants.adsCount = 0;
                    ads_counts = 0;
                }

                Log.d("shantanu", " MainActivity" + ads_counts);
                playAds1(videosAds[ads_counts], count);
            } else {
                if (videosArray != null) {
                    id = videosArray[count].getId();
                    name.setText(videosArray[count].getAuthor_name());
                    date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));
                    if (videosArray[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);

                    }

                    String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                    description.setText(desc);

                    if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (videosArray[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (videosArray[count].getLikes().equals("1")) {
                        likeNumber.setText(videosArray[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(videosArray[count].getLikes() + " likes");
                    }

                    if (videosArray[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (videosArray[count].getViews().equals("1")) {
                        viewNumber.setText(videosArray[count].getViews() + " view");
                    } else {
                        viewNumber.setText(videosArray[count].getViews() + " views");
                    }

                    if (videosArray[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }

                    youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();

                    new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                            //   progress.dismiss();
                            if (adativeStream.isEmpty()) {
                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);
                }
                if (masterLists != null) {
                    id = masterLists[count].getId();
                    name.setText(masterLists[count].getAuthorName());
                    date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));


                    if (masterLists[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);

                    }

                    String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                    description.setText(desc);
                    if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (masterLists[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (masterLists[count].getLikes().equals("1")) {
                        likeNumber.setText(masterLists[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(masterLists[count].getLikes() + " likes");
                    }

                    if (masterLists[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (masterLists[count].getViews().equals("1")) {
                        viewNumber.setText(masterLists[count].getViews() + " view");
                    } else {
                        viewNumber.setText(masterLists[count].getViews() + " views");
                    }

                    if (masterLists[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }
                    youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();
                    @SuppressLint("StaticFieldLeak")
                    YouTubeExtractor extractor = new YouTubeExtractor(this) {
                        @Override
                        protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                            if (ytFiles != null) {
                                int itag = 18;
                                String downloadUrl = ytFiles.get(itag).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                                Log.d("VideoExopLayer", downloadUrl);
                                Log.d("VideoExopLayer", String.valueOf(ytFiles.size()));
                            }
                        }
                    };
                    extractor.extract(youtubeLink, true, true);

                    new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                            //   progress.dismiss();
                            if (adativeStream.isEmpty()) {
                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);
                }
                CONTINUE_WATCHING(false);
                UPDATE_USER_PLAY_TIME(false);
            }
        } else {
            if (videosArray != null) {
                id = videosArray[count].getId();
                name.setText(videosArray[count].getAuthor_name());
                date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));
                if (videosArray[count].getIs_like().equals("0")) {
                    like.setImageResource(R.drawable.like_default);

                }

                String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                description.setText(desc);

                if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                    commentNumbers.setText("No comment");
                    commentNumbers.setEnabled(false);
                } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText("View comment");
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                    commentNumbers.setEnabled(true);
                }

                if (videosArray[count].getLikes().equals("0")) {
                    likeNumber.setText("0 like");
                } else if (videosArray[count].getLikes().equals("1")) {
                    likeNumber.setText(videosArray[count].getLikes() + " like");
                } else {
                    likeNumber.setText(videosArray[count].getLikes() + " likes");
                }

                if (videosArray[count].getViews().equals("0")) {
                    viewNumber.setText("no view");
                } else if (videosArray[count].getViews().equals("1")) {
                    viewNumber.setText(videosArray[count].getViews() + " view");
                } else {
                    viewNumber.setText(videosArray[count].getViews() + " views");
                }

                if (videosArray[count].getIs_like().equals("1")) {
                    like.setImageResource(R.drawable.liked);
                    like.setTag(true);
                }

                youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();

                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        //   progress.dismiss();
                        if (adativeStream.isEmpty()) {
                            // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                            // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (muxedStream.isEmpty()) {
                            //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                            //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String downloadUrl = muxedStream.get(0).getUrl();
                        Uri url = Uri.parse(downloadUrl);
                        playVideo(url);
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).useDefaultLogin().Extract(youtubeLink);
            }
            if (masterLists != null) {
                id = masterLists[count].getId();
                name.setText(masterLists[count].getAuthorName());
                date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));


                if (masterLists[count].getIs_like().equals("0")) {
                    like.setImageResource(R.drawable.like_default);

                }

                String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                description.setText(desc);
                if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                    commentNumbers.setText("No comment");
                    commentNumbers.setEnabled(false);
                } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText("View comment");
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                    commentNumbers.setEnabled(true);
                }

                if (masterLists[count].getLikes().equals("0")) {
                    likeNumber.setText("0 like");
                } else if (masterLists[count].getLikes().equals("1")) {
                    likeNumber.setText(masterLists[count].getLikes() + " like");
                } else {
                    likeNumber.setText(masterLists[count].getLikes() + " likes");
                }

                if (masterLists[count].getViews().equals("0")) {
                    viewNumber.setText("no view");
                } else if (masterLists[count].getViews().equals("1")) {
                    viewNumber.setText(masterLists[count].getViews() + " view");
                } else {
                    viewNumber.setText(masterLists[count].getViews() + " views");
                }

                if (masterLists[count].getIs_like().equals("1")) {
                    like.setImageResource(R.drawable.liked);
                    like.setTag(true);
                }
                youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();


                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        //   progress.dismiss();
                        if (adativeStream.isEmpty()) {
                            // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                            // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (muxedStream.isEmpty()) {
                            //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                            //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String downloadUrl = muxedStream.get(0).getUrl();
                        Uri url = Uri.parse(downloadUrl);
                        playVideo(url);
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).useDefaultLogin().Extract(youtubeLink);
            }

            CONTINUE_WATCHING(false);
            UPDATE_USER_PLAY_TIME(false);
        }


    }

    private void nextVideo(int count) {
        long playTime = simpleExoPlayer.getCurrentPosition() / 1000;
        total_play_time = String.valueOf(playTime);
        pause_at = String.valueOf(simpleExoPlayer.getCurrentPosition());

        if (status.equalsIgnoreCase("2")) {
            status = "2";
        } else if (status.equalsIgnoreCase("1")) {
            status = "1";
        } else {
            status = "0";
        }

        if (simpleExoPlayer.getContentPosition() < simpleExoPlayer.getDuration()) {
            if (openActivityCounter == 0) {
                openActivityCounter++;
                status = "1";
            }
        }
        if (Is_premium.equalsIgnoreCase("0")) {
            if (SharedPreference.getInstance(context).getVideosAds() != null && SharedPreference.getInstance(context).getVideosAds().length != 0) {
                countAds = 5;
                if (simpleExoPlayer != null) {
                    simpleExoPlayer.setPlayWhenReady(false);
                }
                playerView.setVisibility(View.GONE);
                playerViewAds.setVisibility(View.VISIBLE);
                // arrayIndex = count;
                ++ads_counts;
                if (ads_counts >= videosAds.length) {
                    HomeActivityy.ads_count = 0;
                    Networkconstants.adsCount = 0;
                    ads_counts = 0;
                }
                Log.d("shantanu", " MainActivity" + ads_counts);
                playAds1(videosAds[ads_counts], count);
            } else {
                if (videosArray != null) {
                    id = videosArray[count].getId();
                    name.setText(videosArray[count].getAuthor_name());
                    date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));

                    if (videosArray[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);
                        like.setTag(false);
                    }
                    String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                    description.setText(desc);
                    if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (videosArray[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (videosArray[count].getLikes().equals("1")) {
                        likeNumber.setText(videosArray[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(videosArray[count].getLikes() + " likes");
                    }

                    if (videosArray[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (videosArray[count].getViews().equals("1")) {
                        viewNumber.setText(videosArray[count].getViews() + " view");
                    } else {
                        viewNumber.setText(videosArray[count].getViews() + " views");
                    }

                    if (videosArray[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }

                    youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();
                    // videosList.add(videosArray[count]);
                    videosList.add(videosArray[count]);
                    new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                            if (adativeStream.isEmpty()) {
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);

                }
                if (masterLists != null) {
                    id = masterLists[count].getId();
                    name.setText(masterLists[count].getAuthorName());
                    date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));

                    if (masterLists[count].getIs_like().equals("0")) {
                        like.setImageResource(R.drawable.like_default);
                        like.setTag(false);
                    }

                    String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                    description.setText(desc);
                    if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                        commentNumbers.setText("No comment");
                        commentNumbers.setEnabled(false);
                    } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                        commentNumbers.setText("View comment");
                        commentNumbers.setEnabled(true);
                    } else {
                        commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                        commentNumbers.setEnabled(true);
                    }

                    if (masterLists[count].getLikes().equals("0")) {
                        likeNumber.setText("0 like");
                    } else if (masterLists[count].getLikes().equals("1")) {
                        likeNumber.setText(masterLists[count].getLikes() + " like");
                    } else {
                        likeNumber.setText(masterLists[count].getLikes() + " likes");
                    }

                    if (masterLists[count].getViews().equals("0")) {
                        viewNumber.setText("no view");
                    } else if (masterLists[count].getViews().equals("1")) {
                        viewNumber.setText(masterLists[count].getViews() + " view");
                    } else {
                        viewNumber.setText(masterLists[count].getViews() + " views");
                    }

                    if (masterLists[count].getIs_like().equals("1")) {
                        like.setImageResource(R.drawable.liked);
                        like.setTag(true);
                    }

                    youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();
                    // videosList.add(videosArray[count]);
                    videosList1.add(masterLists[count]);
                    new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                        @Override
                        public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                            //   progress.dismiss();
                            if (adativeStream.isEmpty()) {
                                // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                                // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (muxedStream.isEmpty()) {
                                //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                                //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String downloadUrl = muxedStream.get(0).getUrl();
                            Uri url = Uri.parse(downloadUrl);
                            playVideo(url);
                        }

                        @Override
                        public void onExtractionGoesWrong(final ExtractorException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);

                }


                CONTINUE_WATCHING(false);
                UPDATE_USER_PLAY_TIME(false);

            }
        } else {
            if (videosArray != null) {
                id = videosArray[count].getId();
                name.setText(videosArray[count].getAuthor_name());
                date.setText(getDate(Long.parseLong(videosArray[count].getCreation_time())));

                if (videosArray[count].getIs_like().equals("0")) {
                    like.setImageResource(R.drawable.like_default);
                    like.setTag(false);
                }
                String desc = Html.fromHtml(videosArray[count].getVideo_desc()).toString();

                description.setText(desc);
                if (videosArray[count].getComments().equalsIgnoreCase("0")) {
                    commentNumbers.setText("No comment");
                    commentNumbers.setEnabled(false);
                } else if (videosArray[count].getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText("View comment");
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, videosArray[count].getComments()));
                    commentNumbers.setEnabled(true);
                }

                if (videosArray[count].getLikes().equals("0")) {
                    likeNumber.setText("0 like");
                } else if (videosArray[count].getLikes().equals("1")) {
                    likeNumber.setText(videosArray[count].getLikes() + " like");
                } else {
                    likeNumber.setText(videosArray[count].getLikes() + " likes");
                }

                if (videosArray[count].getViews().equals("0")) {
                    viewNumber.setText("no view");
                } else if (videosArray[count].getViews().equals("1")) {
                    viewNumber.setText(videosArray[count].getViews() + " view");
                } else {
                    viewNumber.setText(videosArray[count].getViews() + " views");
                }

                if (videosArray[count].getIs_like().equals("1")) {
                    like.setImageResource(R.drawable.liked);
                    like.setTag(true);
                }

                youtubeLink = BASE_URL + "/watch?v=" + videosArray[count].getYoutube_url();
                // videosList.add(videosArray[count]);
                videosList.add(videosArray[count]);
                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        //   progress.dismiss();
                        if (adativeStream.isEmpty()) {
                            // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                            // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (muxedStream.isEmpty()) {
                            //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                            //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String downloadUrl = muxedStream.get(0).getUrl();
                        Uri url = Uri.parse(downloadUrl);
                        playVideo(url);
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).useDefaultLogin().Extract(youtubeLink);

            }
            if (masterLists != null) {
                id = masterLists[count].getId();
                name.setText(masterLists[count].getAuthorName());
                date.setText(getDate(Long.parseLong(masterLists[count].getCreationTime())));

                if (masterLists[count].getIs_like().equals("0")) {
                    like.setImageResource(R.drawable.like_default);
                    like.setTag(false);
                }

                String desc = Html.fromHtml(masterLists[count].getVideoDesc()).toString();

                description.setText(desc);
                if (masterLists[count].getComments().equalsIgnoreCase("0")) {
                    commentNumbers.setText("No comment");
                    commentNumbers.setEnabled(false);
                } else if (masterLists[count].getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText("View comment");
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, masterLists[count].getComments()));
                    commentNumbers.setEnabled(true);
                }

                if (masterLists[count].getLikes().equals("0")) {
                    likeNumber.setText("0 like");
                } else if (masterLists[count].getLikes().equals("1")) {
                    likeNumber.setText(masterLists[count].getLikes() + " like");
                } else {
                    likeNumber.setText(masterLists[count].getLikes() + " likes");
                }

                if (masterLists[count].getViews().equals("0")) {
                    viewNumber.setText("no view");
                } else if (masterLists[count].getViews().equals("1")) {
                    viewNumber.setText(masterLists[count].getViews() + " view");
                } else {
                    viewNumber.setText(masterLists[count].getViews() + " views");
                }

                if (masterLists[count].getIs_like().equals("1")) {
                    like.setImageResource(R.drawable.liked);
                    like.setTag(true);
                }

                youtubeLink = BASE_URL + "/watch?v=" + masterLists[count].getYoutubeUrl();
                // videosList.add(videosArray[count]);
                videosList1.add(masterLists[count]);
                new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner() {
                    @Override
                    public void onExtractionDone(List<YTMedia> adativeStream, final List<YTMedia> muxedStream, List<YTSubtitles> subtitles, YoutubeMeta meta) {
                        //   progress.dismiss();
                        if (adativeStream.isEmpty()) {
                            // Log.d(TAG, "onExtractionDone: "+"adativeStream.isEmpty");
                            // Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (muxedStream.isEmpty()) {
                            //Log.d(TAG, "onExtractionDone: "+"muxedStream.isEmpty");
                            //Toast.makeText(YoutubeExoPlayerActivity.this, getResources().getString(R.string.unable_to_play_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String downloadUrl = muxedStream.get(0).getUrl();
                        Uri url = Uri.parse(downloadUrl);
                        playVideo(url);
                    }

                    @Override
                    public void onExtractionGoesWrong(final ExtractorException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }).useDefaultLogin().Extract(youtubeLink);

            }


            CONTINUE_WATCHING(false);
            UPDATE_USER_PLAY_TIME(false);

        }

    }


    private void getBundleData() {

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();


            if (videoplaylisttype == 1) {


                if (bundle.containsKey("playlist")) {
                    videoPlaylistModels = (VideoPlaylistModel[]) bundle.getSerializable("playlist");
                    pos = bundle.getInt("position");
                    videoPlaylistModel = videoPlaylistModels[pos];
                }
            } else {
                if (bundle.containsKey("video_data1")) {
                    //  videosArray = (Videos[]) bundle.getSerializable("video_data");
                    masterLists = (MenuMasterList[]) bundle.getSerializable("video_data1");
                    pos = bundle.getInt("position");
                    type = bundle.getInt("type");
                    ads_counts = bundle.getInt("ads_counts");
                    count = pos + 1;
                    menuMasterList = masterLists[pos];
                    MenuTypeId = bundle.getString("menutypeid");
                    Log.d("shantanu", "getBundleData: " + MenuTypeId);
                    FirebaseAnalytics.getInstance(this).setUserProperty("Video", menuMasterList.getVideoDesc());
                    // video = videosArray[pos];
                } else if (bundle.containsKey("video_data_guru")) {
                    videosArray = (Videos[]) bundle.getSerializable("video_data_guru");

                    //  guru_video = (Videos) bundle.getSerializable("video_data_guru");

                    ads_counts = bundle.getInt("ads_counts");
                    pos = bundle.getInt("position");
                    type = 1;
                    // guru_video = (Videos) bundle.getSerializable("video_data_guru");
                    // video=guru_video;
                    count = pos + 1;

                    video = videosArray[pos];
                    FirebaseAnalytics.getInstance(this).setUserProperty("Video", video.getVideo_desc());


                } else if (bundle.containsKey("video_data")) {
                    videosArray = (Videos[]) bundle.getSerializable("video_data");
                    //masterLists= (MenuMasterList[]) bundle.getSerializable("video_data1");
                    pos = bundle.getInt("position");
                    type = bundle.getInt("type");
                    ads_counts = bundle.getInt("ads_counts");
                    if (bundle.containsKey("menutypeid")) {
                        MenuTypeId = bundle.getString("menutypeid");
                    }
                    count = pos + 1;
                    // menuMasterList=masterLists[pos];
                    video = videosArray[pos];
                    FirebaseAnalytics.getInstance(this).setUserProperty("Video", video.getVideo_desc());
                }
                if (type == 1) {
                    file = new File(getExternalFilesDir("bhakti"), video.getVideo_title() + ".jwp");
                    if (file.exists()) {
                        download.setImageResource(R.drawable.download_complete);
                        downloadwatch_video.setText("Downloaded");
                    } else {
                        download.setImageResource(R.drawable.download);
                        downloadwatch_video.setText("Download");
                    }
                }
                if (type == 2) {
                    file = new File(getExternalFilesDir("bhakti"), menuMasterList.getVideoTitle() + ".jwp");
                    if (file.exists()) {
                        download.setImageResource(R.drawable.download_complete);
                        downloadwatch_video.setText("Downloaded");
                    } else {
                        download.setImageResource(R.drawable.download);
                        downloadwatch_video.setText("Download");
                    }
                }

            }
        }


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    private void setVideoData() {
        if (masterLists != null) {
            //title.setText(video.getVideo_title());
            setRelatedVideoData();
        }
        if (videosArray != null) {
            setRelatedVideoData1();
        }

    }

    private void setVideoData1(int pos) {
        if (masterLists != null) {
            //title.setText(video.getVideo_title());
            menuMasterList = masterLists[pos];
            setRelatedVideoData();
        }
        if (videosArray != null) {
            video = videosArray[pos];
            setRelatedVideoData1();
        }

    }

    private void setRelatedVideoData1() {
        // name.setText(video.getAuthor_name());
        id = video.getId();
        name.setText(video.getAuthor_name());
        /*date.setText(getDate(Long.parseLong(video.getCreation_time())));

        String desc = Html.fromHtml(video.getVideo_desc()).toString();

        description.setText(desc);*/
        date.setText(getDate(Long.parseLong(video.getCreation_time())));

        String desc = Html.fromHtml(video.getVideo_desc()).toString();

        description.setText(desc);
        if (video.getComments().equalsIgnoreCase("0")) {
            commentNumbers.setText("No comment");
            commentNumbers.setEnabled(false);
        } else if (video.getComments().equalsIgnoreCase("1")) {
            commentNumbers.setText("View comment");
            commentNumbers.setEnabled(true);
        } else {
            commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
            commentNumbers.setEnabled(true);
        }

        if (video.getLikes().equals("0")) {
            likeNumber.setText("0 like");
        } else if (video.getLikes().equals("1")) {
            likeNumber.setText(video.getLikes() + " like");
        } else {
            likeNumber.setText(video.getLikes() + " likes");
        }

        int view1 = 0;
        if (video.getViews() != null) {
            view1 = Integer.parseInt(video.getViews());
        }

        String vieW = "";
        if (view1 <= 1) {
            vieW = coolNumberFormat(view1) + " view";
        }
        if (view1 > 1) {
            vieW = coolNumberFormat(view1) + " views";
        }

        viewNumber.setText(vieW);

       /* if (video.getViews().equals("0")) {
            viewNumber.setText("no view");
        } else if (video.getViews().equals("1")) {
            viewNumber.setText(video.getViews() + " view");
        } else {
            viewNumber.setText(video.getViews() + " views");
        }*/

        if (video.getIs_like().equals("1")) {
            like.setImageResource(R.drawable.liked);
            like.setTag(true);
        }
    }

    private void getRelatedVideos() {
        if (isConnectingToInternet(this)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, this);
            networkCall.NetworkAPICall(API.RELATED_VIDEOS, false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void setRelatedVideoData() {
        // name.setText(video.getAuthor_name());
        name.setText(menuMasterList.getAuthorName());
        id = menuMasterList.getId();
        /*date.setText(getDate(Long.parseLong(video.getCreation_time())));

        String desc = Html.fromHtml(video.getVideo_desc()).toString();

        description.setText(desc);*/
        date.setText(getDate(Long.parseLong(menuMasterList.getCreationTime())));

        String desc = Html.fromHtml(menuMasterList.getVideoDesc()).toString();

        description.setText(desc);
        if (menuMasterList.getComments().equalsIgnoreCase("0")) {
            commentNumbers.setText("No comment");
            commentNumbers.setEnabled(false);
        } else if (menuMasterList.getComments().equalsIgnoreCase("1")) {
            commentNumbers.setText("View comment");
            commentNumbers.setEnabled(true);
        } else {
            commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
            commentNumbers.setEnabled(true);
        }

        int view1 = 0;
        if (menuMasterList.getViews() != null) {
            view1 = Integer.parseInt(menuMasterList.getViews());
        }


        String vieW = "";
        if (view1 <= 1) {
            vieW = coolNumberFormat(view1) + " view";
        }
        if (view1 > 1) {
            vieW = coolNumberFormat(view1) + " views";
        }

        if (menuMasterList.getLikes().equals("0")) {
            likeNumber.setText("0 like");
        } else if (menuMasterList.getLikes().equals("1")) {
            likeNumber.setText(menuMasterList.getLikes() + " like");
        } else {
            likeNumber.setText(menuMasterList.getLikes() + " likes");
        }


        viewNumber.setText(vieW);


        if (menuMasterList.getIs_like().equals("1")) {
            like.setImageResource(R.drawable.liked);
            like.setTag(true);
        }
    }

    public static String coolNumberFormat(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        DecimalFormat format = new DecimalFormat("0.#");
        String value = format.format(count / Math.pow(1000, exp));
        return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.RELATED_VIDEOS)) {
            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", menuMasterList.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("menu_master_id", MenuTypeId)
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("menu_master_id", MenuTypeId)
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            }

        }
        if (API_NAME.equals(API.VIEW_VIDEO)) {
            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", menuMasterList.getId());
            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId());
            }
        } else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        //    .setMultipartParameter("video_id", masterLists[count].getId());
                        .setMultipartParameter("video_id", menuMasterList.getId());
            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        // .setMultipartParameter("video_id", videosArray[count].getId());
                        .setMultipartParameter("video_id", video.getId());
            }


        } else if (API_NAME.equals(API.POST_COMMENT)) {

            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", menuMasterList.getId())
                        .setMultipartParameter("comment", comment.getText().toString());
            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("comment", comment.getText().toString());
            }

        } else if (API_NAME.equalsIgnoreCase(API.CONTINUE_WATCHING)) {
            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("type", String.valueOf(1))
                        .setMultipartParameter("media_id", menuMasterList.getId())
                        .setMultipartParameter("pause_at", pause_at)
                        .setMultipartParameter("status", status);
            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("type", String.valueOf(1))
                        .setMultipartParameter("media_id", video.getId())
                        .setMultipartParameter("pause_at", pause_at)
                        .setMultipartParameter("status", status);
            }

        } else if (API_NAME.equalsIgnoreCase(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
            Log.d("shantanu", advertisement_id + " " + time_slot_id);
            ion = (Builders.Any.B) Ion.with(MainActivity.this).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid", signupResponse.getId())
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("advertisement_id", advertisement_id)
                    .setMultipartParameter("time_slot_id", time_slot_id)
                    .setMultipartParameter("advertisement_status", advertisement_status);
        } else if (API_NAME.equalsIgnoreCase(API.COUNTRY_WISE_USER_PLAY_HISTORY)) {
            Log.d("shantanu", status + "  " + pause_at);
            if (masterLists != null) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("media_type", String.valueOf(1))
                        .setMultipartParameter("media_id", menuMasterList.getId())
                        .setMultipartParameter("device_type", "1")
                        .setMultipartParameter("total_play", pause_at)
                        .setMultipartParameter("video_status", status);

            }
            if (videosArray != null) {
                ion = (Builders.Any.B) Ion.with(this)
                        .load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT) != null ? SharedPreference.getInstance(this).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", signupResponse.getId())
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("media_type", String.valueOf(1))
                        .setMultipartParameter("media_id", video.getId())
                        .setMultipartParameter("device_type", "1")
                        .setMultipartParameter("total_play", pause_at)
                        .setMultipartParameter("video_status", status);
            }
        }
        return ion;

    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if (result.optBoolean("status")) {
            if (apitype.equals(API.RELATED_VIDEOS)) {
                nonFullScreenLayout.setVisibility(View.VISIBLE);
                JSONArray jsonArray = result.optJSONArray("data");
                Log.e("watch_video", jsonArray.toString());

               /* if (videoResponses.size() > 0)
                    videoResponses.clear();*/

                if (jsonArray.length() > 0) {
                    relatedVideosList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                        videosArrayList.add(video);
                    }

                    viewAllAdapter.notifyDataSetChanged();

                } else {
                    relatedVideosList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }

            } else if (apitype.equals(API.LIKE_VIDEO)) {

                LikeDislike(true);
            } else if (apitype.equals(API.UNLIKE_VIDEO)) {

                LikeDislike(false);
            } else if (apitype.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                //Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
            } else if (apitype.equals(API.VIEW_VIDEO)) {

                //ToastUtil.showShortToast(MainActivity.this,"Views Updated");
            } else if (apitype.equals(API.CONTINUE_WATCHING)) {
//                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                //ToastUtil.showShortToast(MainActivity.this,"Views Updated");
            } else if (apitype.equals(API.COUNTRY_WISE_USER_PLAY_HISTORY)) {
                //   Toast.makeText(context, result.optString("message"), Toast.LENGTH_SHORT).show();
                //ToastUtil.showShortToast(MainActivity.this,"Views Updated");
            } else if (apitype.equals(API.POST_COMMENT)) {
                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);
                video.setComments(String.valueOf(Integer.parseInt(video.getComments()) + 1));
                if (video.getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText("View comment");
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
                    commentNumbers.setEnabled(true);
                }
                isComment = true;
                ToastUtil.showShortToast(this, result.optString("message"));
            }
        } else {
            // ToastUtil.showShortToast(this,"Status is false");
            if (apitype.equals(API.HOME_PAGE_VIDEOS)) {
                nonFullScreenLayout.setVisibility(View.GONE);
            } else if (apitype.equals(API.POST_COMMENT)) {
                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);

                ToastUtil.showShortToast(this, result.optString("message"));
            }
        }
    }

    private void LikeDislike(boolean b) {
        like.setTag(b);
        int likenum = 0;
        if (likeNumber.getText().toString().equalsIgnoreCase("0 like")) {
            likenum = 0;
        } else {
            likenum = Integer.parseInt(likeNumber.getText().toString().split(" ")[0]);
        }
        if (b) {
            if (likenum == 0) {
                likeNumber.setText(likenum + 1 + " like");
            } else {
                likeNumber.setText(likenum + 1 + " likes");
            }
            like.setImageResource(R.drawable.liked);
            SharedPreference.getInstance(getApplicationContext()).putString("Like", "true");
//            video.setLikes(String.valueOf(likenum + 1));
//            video.setIs_like("1");
        } else {
            if (likenum == 1) {
                likeNumber.setText("0 like");
            } else if (likenum == 2) {
                likeNumber.setText(likenum - 1 + " like");
            } else {
                likeNumber.setText(likenum - 1 + " likes");
            }
            like.setImageResource(R.drawable.like_default);
            SharedPreference.getInstance(getApplicationContext()).putString("Like", "false");
//            video.setLikes(String.valueOf(likenum - 1));
//            video.setIs_like("0");
        }
        isLike = true;
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        //  ToastUtil.showDialogBox(this, jsonstring);//---by sumit
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        String videos_url = SharedPreference.getInstance(this).getString(Constants.WEB_VIEW_VIDEO);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");

        //share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + video.getYoutube_url());
        if (videosArray != null) {
            share.putExtra(Intent.EXTRA_TEXT, video.getAuthor_name());
            share.putExtra(Intent.EXTRA_SUBJECT, video.getVideo_title());
            share.putExtra(Intent.EXTRA_TEXT, "Watch Videos " + videos_url.trim() + id.trim());
        }
        if (masterLists != null) {
            share.putExtra(Intent.EXTRA_TEXT, menuMasterList.getAuthorName());
            share.putExtra(Intent.EXTRA_SUBJECT, menuMasterList.getVideoTitle());
            share.putExtra(Intent.EXTRA_TEXT, "Watch Videos " + videos_url.trim() + id.trim());
        }
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add_to_playlist:
                addDataToPlayList();
                break;

            case R.id.share_watch_video:
                shareTextUrl();
                //  shareAppUrl();
                break;

            case R.id.like_video_watch_video:

                Log.d("curr", String.valueOf(currentPosition));
                Log.d("curre", String.valueOf(cu));
                if (isConnectingToInternet(this)) {
                    if (!Boolean.parseBoolean(like.getTag().toString())) {
                        networkCall.NetworkAPICall(API.LIKE_VIDEO, false);
                    } else {
                        networkCall.NetworkAPICall(API.UNLIKE_VIDEO, false);
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;

            case R.id.back:
                onBackPressed();
                break;

            case R.id.watch_video_comment_post_tv:
                postComment.setVisibility(View.GONE);
                commentProgress.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(this);
                networkCall.NetworkAPICall(API.POST_COMMENT, false);
                break;

            case R.id.fragment_watch_video_tv_view_all_comments:

                /*Bundle bundle = new Bundle();
                bundle.putString(Constants.VIDEO_ID, video.getId());
                bundle.putSerializable("video", video);
                Fragment fragment = new ViewCommentsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("VIEW_COMMENT")
                        .commit();*/

                break;
            case R.id.phonepeLayout:
                phonepeLayout.setBackgroundResource(R.drawable.paymentoption_background1);
                googleLayout.setBackgroundResource(R.drawable.paymentoption_background);
                paytmLayout.setBackgroundResource(R.drawable.paymentoption_background);
                debitcardLayout.setBackgroundResource(R.drawable.paymentoption_background);
                break;
            case R.id.googleLayout:
                googleLayout.setBackgroundResource(R.drawable.paymentoption_background1);
                phonepeLayout.setBackgroundResource(R.drawable.paymentoption_background);
                paytmLayout.setBackgroundResource(R.drawable.paymentoption_background);
                debitcardLayout.setBackgroundResource(R.drawable.paymentoption_background);
                break;
            case R.id.paytmlayout:
                paytmLayout.setBackgroundResource(R.drawable.paymentoption_background1);
                phonepeLayout.setBackgroundResource(R.drawable.paymentoption_background);
                googleLayout.setBackgroundResource(R.drawable.paymentoption_background);
                debitcardLayout.setBackgroundResource(R.drawable.paymentoption_background);
                break;
            case R.id.debitcardLayout:
                debitcardLayout.setBackgroundResource(R.drawable.paymentoption_background1);
                phonepeLayout.setBackgroundResource(R.drawable.paymentoption_background);
                googleLayout.setBackgroundResource(R.drawable.paymentoption_background);
                paytmLayout.setBackgroundResource(R.drawable.paymentoption_background);
                break;
            case R.id.download:

                //   if (!file.exists()) {
                beginDownload();
                //    }else{
                //        download.setClickable(true);
                Toast.makeText(this, "File Already Exists", Toast.LENGTH_SHORT).cancel();
                //    }
                break;

           /* case R.id.tv_iv:
                Intent intent=new Intent(LiveStreamJWActivity.this, (LiveTvFragment.class));
                startActivity(intent);
                break;*/
        }
    }

    public void addDataToPlayList() {


        iv_add_to_playlist.setClickable(false);
        boolean isInsertedvideo = false;
        if (videosArray != null) {
            isInsertedvideo = databaseManager.addvideoplaylist(new VideoPlaylistModel(video.getAuthor_name(), video.getYoutube_url(),
                    video.getThumbnail_url()));
        }
        if (masterLists != null) {
            isInsertedvideo = databaseManager.addvideoplaylist(new VideoPlaylistModel(menuMasterList.getAuthorName(), menuMasterList.getYoutubeUrl(),
                    menuMasterList.getThumbnailUrl()));
        }


/*
        name.setText(video.getAuthor_name());
        date.setText(getDate(Long.parseLong(video.getCreation_time())));

        String desc = Html.fromHtml(video.getVideo_desc()).toString();

        description.setText(desc);*/
       /* boolean isInsertedvideo= databaseManager.addvideoplaylist(new VideoPlaylistModel("sumit","_2IfzwJ1Od8",
                "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/thumbnails/7737319min-1571641420maxresdefault_%282%29.jpg"));
*/

        if (isInsertedvideo) {
            tv_add_to_playlist.setText("Success");
            iv_add_to_playlist.setImageResource(R.mipmap.added_to_playlist);


        } else {
            iv_add_to_playlist.setImageResource(R.mipmap.added_to_playlist);

            tv_add_to_playlist.setText("Already Added");
        }
        //    Toast.makeText(getActivity(),"Successfully Inserted",Toast.LENGTH_SHORT).show();
    }

    private void beginDownload() {

        if (!video.getVideo_url().equals("")) {
            Constants.DOWNLOAD_ACTIVE = "true";
            Toast.makeText(this, "Downloading In Progress...", Toast.LENGTH_SHORT).show();
            File file = new File(getExternalFilesDir("bhakti"), video.getVideo_title() + ".jwp");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video.getVideo_url()))
                    .setTitle(video.getVideo_title())
                    .setDescription("Downloading")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationUri(Uri.fromFile(file))
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadID = downloadManager.enqueue(request);
        } else {
            Toast.makeText(this, "No Video link found for download", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayerAds.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
        long playTime = simpleExoPlayer.getCurrentPosition() / 1000;
        total_play_time = String.valueOf(playTime);
        //pause_at = String.valueOf(simpleExoPlayer.getCurrentPosition());


        if (status.equalsIgnoreCase("2")) {
            status = "2";
        } else if (status.equalsIgnoreCase("1")) {
            status = "1";
        } else {
            status = "0";
        }

        if (simpleExoPlayer.getContentPosition() < simpleExoPlayer.getDuration()) {
            if (openActivityCounter == 0) {
                openActivityCounter++;
                status = "1";
            }
        }

        CONTINUE_WATCHING(false);
        UPDATE_USER_PLAY_TIME(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();

        /*pause_at= String.valueOf(simpleExoPlayer.getCurrentPosition());
        if (status.equalsIgnoreCase("1")){
            status="1";
        }else{
            status="0";
        }
        CONTINUE_WATCHING(false);*/


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.getPlaybackState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isVpn(this)) {
            ToastUtil.showDialogBox3(this, "Please disable VPN");
        }
    }
}
