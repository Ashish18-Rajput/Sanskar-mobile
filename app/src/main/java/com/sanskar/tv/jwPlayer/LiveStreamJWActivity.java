package com.sanskar.tv.jwPlayer;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.jwPlayer.ChatAdapter.deleted_node_id;
import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.FROMDEEPLINK;
import static com.sanskar.tv.module.HomeModule.Activity.HomeActivityy.channellist;
import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.EPG.Day;
import com.sanskar.tv.EPG.EPG;
import com.sanskar.tv.EPG.EPGChannel;
import com.sanskar.tv.EPG.EPGClickListener;
import com.sanskar.tv.EPG.EPGData;
import com.sanskar.tv.EPG.EPGDataImpl;
import com.sanskar.tv.EPG.EPGEvent;
import com.sanskar.tv.EPG.Event;
import com.sanskar.tv.EPG.TvGuiderMaster;
import com.sanskar.tv.FreshEpg.Adapter.ChannelNewAdapter;
import com.sanskar.tv.FreshEpg.Adapter.EpgEventNewAdapter;
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
import com.sanskar.tv.module.HomeModule.Adapter.LiveStreamOnTopLiveChannelAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Fragment.LiveFragment;
import com.sanskar.tv.module.HomeModule.Fragment.ViewCommentsFragment;
import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.module.HomeModule.Pojo.MainChannelData;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.TrackSelectionHelper;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class LiveStreamJWActivity extends AppCompatActivity implements
        VideoPlayerEvents.OnFullscreenListener, NetworkCall.MyNetworkCallBack, View.OnClickListener {
    public static final int TOTAL_ITEM_TO_LOAD = 10;
    public SignupResponse signupResponse;
    public static Boolean COME_FROM_LIVETV = false;
    public ArrayList<MenuMasterList> homeChannelList;
    public List<Channel> ChannelLists = new ArrayList<>();
    List<String> keyList = new ArrayList<>();
    public static String VIEWlike;
    public static String Is_like_no;
    public static String video_url;
    public int channel_position = -1;
    HomeActivityy homeActivityy;
    ImageView videoImage, playIconIV;
    RelativeLayout like_channel, Comment_channel;
    ImageView like_channel_button, Comment;
    TextView comment_number_watch_channel, like_number_watch_channel;
    LinearLayout ll_all_option;
    LinearLayout ll_option;
    static String messageCount;
    Long ccount;
    KeepScreenOnHandler keepScreenOnHandler;
    Channel channel;
    public static Boolean deletechat = true;
    public static String checkdeletechat = "0";
    public static Boolean checkfordeletechat = true;
    TextView view_user_watch_channel, events_name, share_s;
    String url;
    Boolean RotationFull = false;
    TextView name, description, date;
    RecyclerView fragment_home_rv_livetv;
    ImageView back;
    ChildEventListener childEventListener;
    static ChildEventListener showchildEventListner;
    Videos video;
    RecyclerView relatedVideosList;
    ViewAllAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout share;
    TextView viewNumber, likeNumber;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText comment;
    static RecyclerView rc_chat;
    String channelDatabase, channelmain, channel_id, channelUser, Particularchannel;
    RelativeLayout ll_chat;
    Activity activity;
    TextView postComment;
    ImageView share_iv, chat;
    ProgressBar commentProgress, progressvideo;
    ArrayList<Long> list = new ArrayList<>();
    List<Message> messageArrayList = new ArrayList<>();
    ImageView profile_iv;
    ImageView tv_iv;
    String livevideourl, channel_name = "";
    String fromWhere;
    RelativeLayout date_rl;

    EPG epg;
    String id;
    String from;
    PlaylistItem item;
    String url1;
    String url2;

    CardView cardView;
    private List<String> availableChannelsTitle = new ArrayList<>();
    public static List<String> availableChannelsId = new ArrayList<>();
    public static HashMap<String, List<Event>> availableChannelEvents = new HashMap<>();
    private List<String> availableChannelsLogo = new ArrayList<>();
    private List<String> availableChannelsUrl = new ArrayList<>();
    private List<Integer> availableChannelsEventLength = new ArrayList<>();
    private List<Integer> availableChannelsEventStartTime = new ArrayList<>();
    private List<String> availableChannelsEventsTitle = new ArrayList<>();
    private List<String> availableChannelsEventsStartingTime = new ArrayList<>();
    private List<String> availableChannelsEventsEndingTime = new ArrayList<>();
    DatabaseReference mDatabaseReference, mRootReference, mDatabaseReference_variable;
    String push_id = "", push_id2 = "";
    String chat_user_ref;
    Spinner days_spinner;
    EditText et_chat;
    ImageView iv_chat;
    private HomeActivityy activitty;
    private RelativeLayout noDataFound;
    private RelativeLayout headerrel, channel_image_layout;
    private ImageView channel_image1;
    private int mPage = 1;
    private ArrayList<Videos> videoResponses;
    private NetworkCall networkCall;
    private LinearLayout nonFullScreenLayout;
    private ImageView like;
    private TextView commentNumbers;
    private Videos[] videos;
    private Videos[] videosArray;
    private Videos[] vd = new Videos[]{};
    private int pos;
    private Context context;
    private CircleImageView profileImg;
    private RelativeLayout container;
    private boolean isLike = false;
    private boolean isComment = false;
    public boolean checkForShowUser = true;
    public boolean IsSHOWuser = true;
    private RelativeLayout layout, toolbar, fragment_watch_video_comment_layout;
    private long enqueue;
    private DownloadManager dm;
    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    private ImageView download;
    private long downloadID;
    private HomeActivityy activityy;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mLinearLayoutManager;
    private ChatAdapter chatAdapter;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    String releasedDate = "";
    private LiveStreamOnTopLiveChannelAdapter liveStreamOnTopLiveChannelAdapter;
    private int mCurrentPage = 5;
    Query messageQuery, showmessagequery;
    public static PlayerView playerView1;
    public static ProgressBar progressBar;
    public static ImageView full_Screen;
    public static ImageView option_menu;
    public static SimpleExoPlayer simpleExoPlayer;
    static DefaultHttpDataSourceFactory factory;
    DefaultTrackSelector trackSelector;
    TrackSelector selector, selector1;
    BandwidthMeter bandwidthMeter;
    LoadControl loadControl;
    static HlsMediaSource hlsMediaSource;
    ImageView imageView, exo_nextVideo, user;
    MainChannelData mainChannelData;
    static String current_user_ref;
    static String channelID;
    static String channel_database;
    Handler mainHandler;
    public static SimpleExoPlayer simpleExoPlayerAds, simpleExoPlayerAds1;
    DefaultTrackSelector.Parameters trackParameters;
    public static TrackSelectionHelper trackSelectionHelper;
    DataSource.Factory dataSourceFactory;
    private TrackGroupArray lastSeenTrackGroupArray;
    EventLogger eventLogger;
    public static String Is_premium = "";
    public static MenuMasterList[] bhajanforImagefromHome;
    static long countAds = 5;
    public static boolean is_pause = true;
    public static boolean is_pause_1 = true;
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
    public TextView epg_event_name, days_txtView, text_channel_list;
    private ImageView back_imageView, forward_imageView;
    public RecyclerView event_recyclerView, channel_recyclerView;
    public ChannelNewAdapter channelNewAdapter;
    private EpgEventNewAdapter epgEventNewAdapter;
    List<List<Day>> daysArrayList;
    List<Day> currentDayData;
    public LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    String Channel_image;
    String newUrl;
    int when_Forward_backward = -1;
    public boolean isToday = true;


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(LiveStreamJWActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public static void sendLink(Activity activity, String subject, String msg, String msgHtml) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHtml);
        if (sharingIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(sharingIntent);
        }
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_jw);
        //  context=getBaseContext();
        context = this;
        activity = this;
        homeActivityy = new HomeActivityy();
        COME_FROM_LIVETV = true;
        if (AudioPlayerService.mediaPlayer != null) {
            if (AudioPlayerService.mediaPlayer.isPlaying()) {
                AudioPlayerService.mediaPlayer.pause();
            } else {
            }
        }

        if (downloadmediaplayer != null) {
            if (downloadmediaplayer.isPlaying()) {
                downloadmediaplayer.pause();
            }
        } else {

        }

        if (playlistmediaplayer != null) {
            if (playlistmediaplayer.isPlaying()) {
                playlistmediaplayer.pause();
            }
        }


        epg_event_name = findViewById(R.id.epg_event_name);
        days_txtView = findViewById(R.id.days_txtView);
        text_channel_list = findViewById(R.id.text_channel_list);
        back_imageView = findViewById(R.id.back_imageView);
        forward_imageView = findViewById(R.id.forward_imageView);
        event_recyclerView = findViewById(R.id.event_recyclerView);
        channel_recyclerView = findViewById(R.id.channel_recyclerView);
        date_rl = findViewById(R.id.date_rl);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        event_recyclerView.setLayoutManager(linearLayoutManager1);
        channel_recyclerView.setLayoutManager(linearLayoutManager);

        event_recyclerView.setHasFixedSize(true);
        channel_recyclerView.setHasFixedSize(true);

        event_recyclerView.setNestedScrollingEnabled(false);
        channel_recyclerView.setNestedScrollingEnabled(false);
        forward_imageView.setOnClickListener(v -> {
            if (when_Forward_backward != -1 && when_Forward_backward < 7) {

                if (when_Forward_backward != 6) {
                    isToday = ++when_Forward_backward == 6;
                    initLandingPageAdapter(when_Forward_backward, channel_position);
                }


                if (when_Forward_backward == 6) {
                    days_txtView.setText("Today");
                } else if (when_Forward_backward == 5) {
                    days_txtView.setText("Yesterday");
                } else {
                    days_txtView.setText(daysArrayList.get(when_Forward_backward).get(0).getReleaseDate());
                }
            }
        });
        back_imageView.setOnClickListener(v -> {
            if (when_Forward_backward != -1 && when_Forward_backward < 7) {
                if (when_Forward_backward != 0) {
                    --when_Forward_backward;
                    isToday = false;
                    initLandingPageAdapter(when_Forward_backward, channel_position);
                }

                if (when_Forward_backward == 6) {
                    days_txtView.setText("Today");
                } else if (when_Forward_backward == 5) {
                    days_txtView.setText("Yesterday");
                } else {
                    days_txtView.setText(daysArrayList.get(when_Forward_backward).get(0).getReleaseDate());
                }
            }
        });


        homeChannelList = new ArrayList<>();
        ChannelLists = channellist;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        //activitty = ((HomeActivityy) context);
        //mAuth = FirebaseAuth.getInstance();
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        et_chat = findViewById(R.id.et_chat);
        iv_chat = findViewById(R.id.iv_chat);
        rc_chat = findViewById(R.id.rc_chat);
        fragment_home_rv_livetv = findViewById(R.id.fragment_home_rv_livetv);
        videoImage = findViewById(R.id.videoImage);
        playIconIV = findViewById(R.id.playIconIV);
        like_channel_button = findViewById(R.id.like);
        view_user_watch_channel = findViewById(R.id.view_user_watch_channel);
        channel_image1 = findViewById(R.id.channel_image);
        channel_image_layout = findViewById(R.id.channel_image_layout);
        epg = findViewById(R.id.epg);
        days_spinner = findViewById(R.id.days_spinner);


        like_channel_button.setTag(false);
        like_channel = findViewById(R.id.like_channel);
        Comment_channel = findViewById(R.id.Comment_channel);
        //like_channel_button= view.findViewById(R.id.like);
        Comment = findViewById(R.id.Comment);
        comment_number_watch_channel = findViewById(R.id.comment_number_watch_channel);
        like_number_watch_channel = findViewById(R.id.like_number_watch_channel);

        like_channel.setOnClickListener(view -> {
            if (isConnectingToInternet(activity)) {
                if (!Boolean.parseBoolean(like_channel_button.getTag().toString())) {
                    networkCall.NetworkAPICall(API.LIKE_CHANNEL, false);
                } else {
                    networkCall.NetworkAPICall(API.UNLIKE_CHANNEL, false);
                }
            } else {
                ToastUtil.showDialogBox(activity, Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        });

        playerView1 = findViewById(R.id.player_view_exo);
        full_Screen = playerView1.findViewById(R.id.bt_full);
        playerViewAdsHome = findViewById(R.id.player_view_exo_ads);
        full_Screen.setVisibility(View.GONE);
        option_menu = playerView1.findViewById(R.id.option_menu);
        skip_ads = playerViewAdsHome.findViewById(R.id.skip_ads);
        progressBar = findViewById(R.id.progress_bar);
        exo_nextVideo = findViewById(R.id.exo_nextVideo);
        user = findViewById(R.id.user);
        exo_nextVideo.setVisibility(View.GONE);
        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();
        quality = playerView1.findViewById(R.id.quality);
        if (SharedPreference.getInstance(this).getString(Constants.GAP_DURATION_LIVE_CHANNEl) != null) {
            gap_Duration_live_channel = SharedPreference.getInstance(this).getLong(Constants.GAP_DURATION_LIVE_CHANNEl);
        }

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

        dataSourceFactory = new DefaultDataSourceFactory(this, defaultBandwidthMeter, buildHttpDataSourceFactory(defaultBandwidthMeter));
        lastSeenTrackGroupArray = null;
        eventLogger = new EventLogger(trackSelector);
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                this, trackSelector, loadControl
        );

        simpleExoPlayerAds = ExoPlayerFactory.newSimpleInstance(
                this, selector, loadControl
        );
        simpleExoPlayerAds1 = ExoPlayerFactory.newSimpleInstance(
                this, selector1, loadControl
        );
        factory = new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );

        liveStreamOnTopLiveChannelAdapter = new LiveStreamOnTopLiveChannelAdapter(homeChannelList, this, LiveStreamJWActivity.this);

        LinearLayoutManager ontoplivetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        fragment_home_rv_livetv.setLayoutManager(ontoplivetvLM);
        fragment_home_rv_livetv.setAdapter(liveStreamOnTopLiveChannelAdapter);

        Intent intent = getIntent();
        Uri data = intent.getData();
        livevideourl = getIntent().getStringExtra("livevideourl");

        channel_name = getIntent().getStringExtra("channel_name");
        String pp = getIntent().getStringExtra("channel_position");
        channel_position = Integer.parseInt(pp);


        if (SharedPreference.getInstance(context).getChannelData() != null) {
            InitAdapter();
        } else {
            getHomeData1(true);
        }


        Log.d("livevideourl", livevideourl);
        id = getIntent().getStringExtra("channel_id");
        Log.d("livestream", "asdfg");

        VIEWlike = getIntent().getStringExtra("VIEWlike");
        Is_like_no = getIntent().getStringExtra("Is_like_no");
        //  }
        setChannelRelatedData();

        getChannelChat(id);

        if (getIntent().getStringExtra("from") != null) {
            from = getIntent().getStringExtra("from");
        } else {
            from = "";
        }

        getBundleData();
        initView();


        switch (LiveStreamJWActivity.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_NO:
                days_txtView.setTextColor(Color.parseColor("#000000"));
                epg_event_name.setTextColor(Color.parseColor("#000000"));
                text_channel_list.setTextColor(Color.parseColor("#000000"));
                like_number_watch_channel.setTextColor(Color.parseColor("#FFFFFF"));
                comment_number_watch_channel.setTextColor(Color.parseColor("#FFFFFF"));
                view_user_watch_channel.setTextColor(Color.parseColor("#FFFFFF"));
                share_s.setTextColor(Color.parseColor("#FFFFFF"));
                break;

        }


            if (!TextUtils.isEmpty(SharedPreference.getInstance(this).getString(Constants.TV_GUIDE_ANDROID)) && SharedPreference.getInstance(this).getString(Constants.TV_GUIDE_ANDROID).length() > 0) {
            fetchEPGData();
            if (SharedPreference.getInstance(this).getString(Constants.TV_GUIDE_ANDROID).equalsIgnoreCase("1")) {
                rc_chat.setVisibility(View.GONE);
                fragment_home_rv_livetv.setVisibility(View.GONE);
                ll_chat.setVisibility(View.GONE);
                days_spinner.setVisibility(View.GONE);
                events_name.setVisibility(View.GONE);
                epg.setVisibility(View.GONE);
            } else {
                rc_chat.setVisibility(View.VISIBLE);
                fragment_home_rv_livetv.setVisibility(View.VISIBLE);
                ll_chat.setVisibility(View.VISIBLE);
                days_spinner.setVisibility(View.GONE);
                events_name.setVisibility(View.GONE);
                epg.setVisibility(View.GONE);
            }

        } else {
            rc_chat.setVisibility(View.VISIBLE);
            fragment_home_rv_livetv.setVisibility(View.VISIBLE);
            ll_chat.setVisibility(View.VISIBLE);
            days_spinner.setVisibility(View.GONE);
            events_name.setVisibility(View.GONE);
            epg.setVisibility(View.GONE);

        }

        events_name.setText(channel_name);

        Download_Uri = Uri.parse(API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/");
        String baseUrl = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/";
        if (video != null) {
            if (video.getVideo_url().contains("//")) {
                url = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/SampleVideo_1280x720_1mb.mp4/playlist.m3u8";
            } else {
                url = baseUrl + video.getVideo_url() + "/playlist.m3u8";
            }
            url1 = video.getVideo_url();


            url2 = video.getVideo_url();
            String[] urltosppend = url2.split("com");

            url2 = "http://52.204.183.54:1935/vods3/" + "_definst_/mp4:amazons3/bhaktiappproduction" +
                    urltosppend[1] + "/playlist.m3u8";
        }


        if (from.equals("guru")) {
            item = new PlaylistItem(url1);
        } else if (from.equals("livetv")) {
            item = new PlaylistItem(livevideourl);
        }
        if (id.equalsIgnoreCase("27")){
            newUrl = livevideourl;
        }else {
            newUrl = livevideourl + "?start=" + previousDate() + "&end=";
        }
        Uri uri = Uri.parse(newUrl);


        /*if (LiveFragment.Is_premium.equalsIgnoreCase("0")) {
            if (SharedPreference.getInstance(this).getLiveTvAds() != null && SharedPreference.getInstance(this).getLiveTvAds().length != 0) {
                liveTvAds = SharedPreference.getInstance(this).getLiveTvAds();
                if (Networkconstants.adsCount > liveTvAds.length - 1) {
                    Networkconstants.adsCount = 0;
                }
                playVideo1(uri);
            } else {
                playVideo1(uri);
            }
        } else {
            playVideo1(uri);
        }*/


        epg.setEPGClickListener(new EPGClickListener() {
            @Override
            public void onChannelClicked(int channelPosition, EPGChannel epgChannel) {
                //Toast.makeText(LiveStreamJWActivity.this, epgChannel.getName() + " clicked", Toast.LENGTH_SHORT).show();
                String newUrl = epgChannel.getUrl() + "?start=" + previousDate() + "&end=";
                Log.e("chaudhary", newUrl);
                Uri uri = Uri.parse(newUrl);
                playVideo1(uri);
                events_name.setText(epgChannel.getName());
                //initLandingPageAdapter(daysArrayList.size() - 1);
                days_spinner.setSelection(0);
            }

            @Override
            public void onEventClicked(int channelPosition, int programPosition, EPGEvent epgEvent) {
                //Toast.makeText(LiveStreamJWActivity.this, epgEvent.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                String newUrl = epgEvent.getUrl() + "?start=" + epgEvent.getReleasedDate() + "T" + epgEvent.getStarts() + "+05:30" + "&end=" + epgEvent.getReleasedDate() + "T" + epgEvent.getEnds() + "+05:30";
                Log.e("chaudhary", newUrl);
                Uri uri = Uri.parse(newUrl);
                playVideo1(uri);
                events_name.setText(epgEvent.getTitle());
            }

            @Override
            public void onResetButtonClicked() {
                epg.recalculateAndRedraw(true);
            }
        });
    }


    private void fetchEPGData() {
        networkCall = new NetworkCall(this, this);
        networkCall.NetworkAPICall(API.TV_GUIDE, true);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("exoplayer_video", bandwidthMeter);
    }

    public void playAds(AdvertisementAds liveTvAd, Uri uri) {
        adsType = 1;
        adsType_1 = 1;
        ++Networkconstants.adsCount;
        playerView1.setVisibility(View.GONE);
        playerViewAdsHome.setVisibility(View.VISIBLE);

        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        advertisement_id = liveTvAd.getId();
        time_slot_id = liveTvAd.getTime_slot_id();

        Uri uri1 = Uri.parse(liveTvAd.getMedia());
        //Uri uri=Uri.parse("https://i.imgur.com/7bMqysJ.mp4");

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
                    playerViewAdsHome.setVisibility(View.GONE);
                    playerView1.setVisibility(View.VISIBLE);
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
                        playVideo1(uri);
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
                    homeActivityy.pause_1 = 0;
                    timePlay = 1;
                }
                playVideo1(uri);
            }
        });
    }

    public void updateAPPAdvertisementCounter(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall = new NetworkCall(LiveStreamJWActivity.this, this);
            networkCall.NetworkAPICall(API.UPDATE_APP_ADVERTISEMENT_COUNTER, b);
        } else {
            ToastUtil.showShortToast(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
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

    public void playads1(AdvertisementAds liveTvAd) {
        adsType = 3;
        adsType_1 = 3;
        ++Networkconstants.adsCount;
        simpleExoPlayer.setPlayWhenReady(false);
        playerView1.setVisibility(View.GONE);

        playerViewAdsHome.setVisibility(View.VISIBLE);
        if (liveTvAd.getSkip().equalsIgnoreCase("0")) {
            skip_ads.setVisibility(View.GONE);
        }

        if (liveTvAd.getSkip().equalsIgnoreCase("1")) {
            skip_ads.setVisibility(View.VISIBLE);
        }
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
                    playerViewAdsHome.setVisibility(View.GONE);
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

    public void updatetimer(long millisecond) {
        CountDownTimer countDownTimer = new CountDownTimer(millisecond, 1000) {
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

    public void playNextAds() {
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

    public void playNextAds1() {

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

    public void prepareNextAds() {
        if (homeActivityy.pause_1 == 0) {
            Log.d("shantanu", "IN NextAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds();
        }

        if (homeActivityy.pause_1 == 1) {
            Log.d("shantanu", "IN NextAds " + String.valueOf(is_pause));
            Log.d("shantanu", "IN NextAds Pause_1=0 ");
            if (Networkconstants.adsCount >= liveTvAds.length - 1) {
                Networkconstants.adsCount = 0;
            }
            playNextAds1();
        }

    }

    private void getHomeData1(boolean b) {
        networkCall = new NetworkCall(this, LiveStreamJWActivity.this);
        networkCall.NetworkAPICall(API.CHANNEL_LIST, b);
    }

    public void playVideo1(Uri uri) {

        if (SharedPreference.getInstance(this).getLiveTvAds() != null) {
            prepareNextAds();
        }

        progressBar.setVisibility(View.GONE);


        hlsMediaSource = new HlsMediaSource.Factory(factory).createMediaSource(uri);

        playerView1.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView1.setKeepScreenOn(true);

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
        quality.setOnClickListener(v -> {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                trackSelectionHelper.showSelectionDialog(LiveStreamJWActivity.this, "Quality",
                        trackSelector.getCurrentMappedTrackInfo(), 0);
            }
        });
       /* full_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityy.showHomeFragment();
            }
        });*/
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


    public static void playbackSpeed(float speed) {
        PlaybackParams params = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            params = new PlaybackParams();
            params.setSpeed(speed);
        }
        simpleExoPlayer.setPlaybackParams(params);
    }

    private void setChannelRelatedData() {

//        Log.d("livestream",VIEWlike);

        if (VIEWlike.equals("0")) {
            like_number_watch_channel.setText("0 like");
        } else if (VIEWlike.equals("1")) {
            like_number_watch_channel.setText(VIEWlike + " like");
        } else {
            like_number_watch_channel.setText(VIEWlike + " likes");
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
        } else if (
                Is_like_no.equals("0")) {
            like_channel_button.setImageResource(R.mipmap.white_like);
            like_channel_button.setTag(false);
        }
    }


    private void getChannelChat(String id) {
        if (mRootReference != null) {
            mRootReference = null;
        }
        mRootReference = FirebaseDatabase.getInstance().getReference();

        if (id.equals("19")) {
            channelDatabase = "sanskarTV";
            channelmain = "sanskarliveChannels/sanskarTV";
            channelUser = "sanskarliveUsers/sanskarTV";
            //  channelvariable = "sanskarliveChannels/sanskarTV/count";
            channel_id = id;
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //ShowUsers(channelUser);
        } else if (id.equals("20")) {
            channelDatabase = "satsangTV";
            channelmain = "sanskarliveChannels/satsangTV";
            channelUser = "sanskarliveUsers/satsangTV";
            channel_id = id;
            //  ShowUsers(channelUser);

            MessageDatabase(channelDatabase, channelmain, channel_id);
            // ShowUsers(channelUser);

        } else if (id.equals("21")) {
            channelDatabase = "shubhTV";
            channelmain = "sanskarliveChannels/shubhTV";
            channelUser = "sanskarliveUsers/shubhTV";
            channel_id = id;
            //  ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //   ShowUsers(channelUser);


        } else if (id.equals("22")) {
            channelDatabase = "shubhCinemaTV";
            channelmain = "sanskarliveChannels/shubhCinemaTV";
            channelUser = "sanskarliveUsers/shubhCinemaTV";
            channel_id = id;
            //  ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //  ShowUsers(channelUser);


        } else if (id.equals("23")) {
            channelDatabase = "sanskarWebTV";
            channelmain = "sanskarliveChannels/sanskarWebTV";
            channelUser = "sanskarliveUsers/sanskarWebTV";
            channel_id = id;
            //  ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            // ShowUsers(channelUser);


        } else if (id.equals("24")) {
            channelDatabase = "satsangWebTV";
            channelmain = "sanskarliveChannels/satsangWebTV";
            channelUser = "sanskarliveUsers/satsangWebTV";
            channel_id = id;
            //  ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //  ShowUsers(channelUser);


        } else if (id.equals("25")) {
            channelDatabase = "sanskarUK";
            channelmain = "sanskarliveChannels/sanskarUK";
            channelUser = "sanskarliveUsers/satsangWebTV";
            channel_id = id;
            //    ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            // ShowUsers(channelUser);


        } else if (id.equals("26")) {
            channelDatabase = "sanskarUSA";
            channelmain = "sanskarliveChannels/sanskarUSA";
            channelUser = "sanskarliveUsers/sanskarUSA";
            channel_id = id;

            //   ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //  ShowUsers(channelUser);

        } else if (id.equals("29")) {
            channelDatabase = "SanskarTvRadio";
            channelmain = "sanskarliveChannels/SanskarTvRadio";
            channelUser = "sanskarliveUsers/SanskarTvRadio";
            channel_id = id;

            //      ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //  ShowUsers(channelUser);

        } else if (id.equals("27")) {
            channelDatabase = "SanskarTotalBhakti";
            channelmain = "sanskarliveChannels/SanskarTotalBhakti";
            channelUser = "sanskarliveUsers/SanskarTotalBhakti";
            channel_id = id;

            //      ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //  ShowUsers(channelUser);

        } else {
            channelDatabase = "default";
            channelmain = "sanskarliveChannels/default";
            channelmain = "sanskarliveUsers/default";
            channel_id = id;
            //     ShowUsers(channelUser);
            MessageDatabase(channelDatabase, channelmain, channel_id);
            //ShowUsers(channelUser);


        }
    }

    private void UserOnline(String channelDatabase, String channelmain, String channel_id) {

        Map messageMap = new HashMap();

        messageMap.put("count", ccount);


        Log.d("UserOnline", "checkinguser1");
        Log.d("ccc", String.valueOf(ccount));


        mDatabaseReference.updateChildren(messageMap, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                } else {
                    // Log.d("sss",String.valueOf(ccount));
                    getLiveView(ccount);

                    Toast.makeText(LiveStreamJWActivity.this, "Your comment posted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isVpn(this)) {
            ToastUtil.showDialogBox3(this, "Please disable VPN");
        }
        Log.d("resume", "check rusem");
        deletechat = true;
        //  checkdeletechat="0";*/
        checkfordeletechat = true;
        checkForShowUser = true;
    }

    @Override
    protected void onPause() {
        Log.d("pause", "check pause");
        deletechat = true;
        //  checkdeletechat="0";*/
        checkfordeletechat = true;

        simpleExoPlayer.setPlayWhenReady(false);
        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
            simpleExoPlayerAds.release();
        }
        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
            simpleExoPlayerAds1.release();
        }
        simpleExoPlayer.release();
        super.onPause();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();


        simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();
        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
            simpleExoPlayerAds.release();
        }
        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
            simpleExoPlayerAds1.release();
        }
        Log.d("destroy", "onDestroy2");
        unregisterReceiver(onDownloadComplete);

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof ViewCommentsFragment) {
            super.onBackPressed();
        } else {
            if (isLike || isComment) {
                isLike = true;
            }
            Intent intent = new Intent();
            intent.putExtra("Is_like_no", Is_like_no);
            intent.putExtra("VIEWlike", VIEWlike);
            setResult(RESULT_OK, intent);
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;

        if (API_NAME.equals(API.CHANNEL_LIST)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("current_version", "" + Utils.getVersionCode(activity))
                    .setMultipartParameter("device_type", "1");
        } else if (API_NAME.equals(API.LIKE_CHANNEL) || API_NAME.equals(API.UNLIKE_CHANNEL)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("channel_id", id);
        } else if (API_NAME.equals(API.INFORM_DELETE_CHAT)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                    .setMultipartParameter("node_id", deleted_node_id);

        } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
            Log.d("shantanu", advertisement_id + " " + time_slot_id);
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("advertisement_id", advertisement_id)
                    .setMultipartParameter("time_slot_id", time_slot_id)
                    .setMultipartParameter("advertisement_status", String.valueOf(2));
        } else if (API_NAME.equals(API.TV_GUIDE)) {
            //Log.d("shantanu", advertisement_id + " " + time_slot_id);
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId());
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        Log.d("livestream", "123456789");

        if (result.optBoolean("status")) {
            if (API_NAME.equals(API.LIKE_CHANNEL)) {
                this.homeChannelList.clear();


                LikeDislike(true);
                getHomeData();

            } else if (API_NAME.equals(API.UNLIKE_CHANNEL)) {
                this.homeChannelList.clear();

                LikeDislike(false);
                getHomeData();


            } else if (API_NAME.equals(API.CHANNEL_LIST)) {
                if (FROMDEEPLINK.equalsIgnoreCase("1")) {
                    FROMDEEPLINK = "";
                    Log.d("livestream", "asdfgh");
                    setHomeData2(result);
                } else {
                    Log.d("livestream", "asdfghij");

                    setHomeData(result);
                    notifyAdapters();
                }
            } else if (API_NAME.equals(API.INFORM_DELETE_CHAT)) {
                Toast.makeText(this, " you delete chat", Toast.LENGTH_SHORT).show();


            } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                Log.d("shantanu", "updated");
                //Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
            } else if (API_NAME.equals(API.TV_GUIDE)) {
                Log.d("shantanu", "tv_guide");

                TvGuiderMaster tvGuiderMaster = new Gson().fromJson(result.toString(), TvGuiderMaster.class);
                SharedPreference.getInstance(this).setEPGResponse(tvGuiderMaster);

                when_Forward_backward = tvGuiderMaster.getDays().size() - 1;

                initLandingPageAdapter(tvGuiderMaster.getDays().size() - 1, channel_position);

            }
        }
    }

    public void initLandingPageAdapter(int index, int channel) {

        ZonedDateTime nowZoned = null;
        long seconds = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            nowZoned = ZonedDateTime.now();
            Instant midnight = nowZoned.toLocalDate().atStartOfDay(nowZoned.getZone()).toInstant();
            Duration duration = Duration.between(midnight, Instant.now());
            seconds = duration.getSeconds() * 1000;
            Log.d("shantanu", "current time: " + seconds);
        }

        TvGuiderMaster tvGuiderMaster = SharedPreference.getInstance(this).getEPGResponse();

        daysArrayList = new ArrayList<>();

        daysArrayList.addAll(tvGuiderMaster.getDays());

        currentDayData = new ArrayList<>();

        currentDayData.addAll(daysArrayList.get(index));

        availableChannelsTitle.clear();
        availableChannelsLogo.clear();
        availableChannelEvents.clear();
        availableChannelsId.clear();
        availableChannelsUrl.clear();

        for (Day day : currentDayData) {
            releasedDate = day.getReleaseDate();
            availableChannelsTitle.add(day.getName());
            availableChannelsLogo.add(day.getImage());
            availableChannelsId.add(day.getId());
            availableChannelsUrl.add(day.getChannelUrl());
            availableChannelEvents.put(day.getName(), day.getEvents());
        }


        // new AsyncLoadEPGData(epg).execute();

        int currentEventPosition = -1;
        long startTime;
        long endTime;
        long schedulerTime = 0;


        Log.d("shantanu", "channel position: " + channel);
        if (channel < currentDayData.size() && currentDayData.get(channel).getEvents() != null && currentDayData.get(channel).getEvents().size() > 0) {
            playIconIV.setVisibility(View.GONE);
            channel_image1.setVisibility(View.GONE);
            channel_image_layout.setVisibility(View.GONE);
            for (int i = 0; i < currentDayData.get(channel).getEvents().size(); i++) {
                startTime = currentDayData.get(channel).getEvents().get(i).getStartTimeMilliseconds();
                endTime = currentDayData.get(channel).getEvents().get(i).getEndTimeMilliseconds();
                if (startTime <= seconds && seconds <= endTime) {
                    currentEventPosition = i;
                    schedulerTime = endTime - seconds;
                }
            }
            event_recyclerView.setVisibility(View.VISIBLE);
            epgEventNewAdapter = new EpgEventNewAdapter(context, currentDayData.get(channel).getEvents(), currentEventPosition, currentDayData.get(channel).getChannelUrl(), currentDayData.get(channel).getReleaseDate(), isToday);
            event_recyclerView.setAdapter(epgEventNewAdapter);

            if (currentEventPosition != -1) {
                linearLayoutManager1.scrollToPositionWithOffset(currentEventPosition, 0);
            }

            Log.d("shantanu", "" + schedulerTime);

            new Handler().postDelayed(() -> initLandingPageAdapter(index, channel), schedulerTime);

        } else {
            epg_event_name.setText(channel_name);
            event_recyclerView.setVisibility(View.INVISIBLE);
            progressvideo.setVisibility(View.GONE);
            playIconIV.setVisibility(View.GONE);
            channel_image1.setVisibility(View.GONE);
            channel_image_layout.setVisibility(View.GONE);
            Uri uri = Uri.parse(newUrl);
            if (LiveFragment.Is_premium.equalsIgnoreCase("0")) {
                if (SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds() != null && SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds().length != 0) {
                    liveTvAds = SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds();
                    if (Networkconstants.adsCount > liveTvAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    Log.d("shantanu", newUrl);
                    playVideo1(uri);
                    //playads(liveTvAds[Networkconstants.adsCount], uri);
                } else {
                    playVideo1(uri);
                }
            } else {
                playVideo1(uri);
            }
        }
    }


    public Map<EPGChannel, List<EPGEvent>> getMockData() {
        HashMap<EPGChannel, List<EPGEvent>> result; /*= Maps.newLinkedHashMap();*/

        long nowMillis = System.currentTimeMillis();

        for (int i = 0; i < availableChannelsTitle.size(); i++) {
            EPGChannel epgChannel = new EPGChannel(availableChannelsLogo.get(i),
                    availableChannelsTitle.get(i), availableChannelsId.get(i), availableChannelsUrl.get(i));

            //result.put(epgChannel, createEvents(epgChannel, nowMillis));
        }

        return null;
    }

    private List<EPGEvent> createEvents(EPGChannel epgChannel, long nowMillis) {
//        List<EPGEvent> result = Lists.newArrayList();

        long epgStart = nowMillis - EPG.DAYS_BACK_MILLIS;
        long epgEnd = nowMillis + EPG.DAYS_FORWARD_MILLIS;

        long currentTime = epgStart;

        /*while (currentTime <= epgEnd) {
            long eventEnd = getEventEnd(currentTime);
            EPGEvent epgEvent = new EPGEvent(currentTime, eventEnd, availableChannelsEventsTitle.get(randomBetween(0,6)));
            result.add(epgEvent);
            currentTime = eventEnd;
            i++;
        }*/

        availableChannelsEventsTitle = new ArrayList<>();
        availableChannelsEventLength = new ArrayList<>();
        availableChannelsEventStartTime = new ArrayList<>();
        availableChannelsEventsStartingTime = new ArrayList<>();
        availableChannelsEventsEndingTime = new ArrayList<>();

        for (int i = 0; i < availableChannelEvents.get(epgChannel.getName()).size(); i++) {
            availableChannelsEventsTitle.add(availableChannelEvents.get(epgChannel.getName()).get(i).getProgramTitle());
            availableChannelsEventStartTime.add(availableChannelEvents.get(epgChannel.getName()).get(i).getStartTimeMilliseconds());
            availableChannelsEventsStartingTime.add(availableChannelEvents.get(epgChannel.getName()).get(i).getStartTime());
            availableChannelsEventsEndingTime.add(availableChannelEvents.get(epgChannel.getName()).get(i).getEndTime());
            availableChannelsEventLength.add(availableChannelEvents.get(epgChannel.getName()).get(i).getDurationMilliseconds());
        }
        int i = 0;
        while (i < availableChannelsEventsTitle.size() && currentTime <= epgEnd) {
            if (availableChannelEvents.containsKey(epgChannel.getName())) {
                long eventEnd = getEventEnd(currentTime, i);

                EPGEvent epgEvent = new EPGEvent(currentTime, eventEnd, availableChannelsEventsStartingTime.get(i)
                        , availableChannelsEventsEndingTime.get(i), availableChannelsEventsTitle.get(i), epgChannel.getUrl(), releasedDate);
                //result.add(epgEvent);
                currentTime = eventEnd;
                i++;
            }
        }

        return null;
    }

    private long getEventEnd(long eventStartMillis, int i) {
        long length = availableChannelsEventLength.get(i);
        return eventStartMillis + length;
    }

    private int randomBetween(int start, int end) {
        return start + new Random().nextInt((end - start) + 1);
    }

    private class AsyncLoadEPGData extends AsyncTask<Void, Void, EPGData> {

        EPG epg;

        public AsyncLoadEPGData(EPG epg) {
            this.epg = epg;
        }

        @Override
        protected EPGData doInBackground(Void... voids) {
            return new EPGDataImpl(getMockData());
        }

        @Override
        protected void onPostExecute(EPGData epgData) {
            epg.setEPGData(epgData);
            epg.recalculateAndRedraw(false);
        }
    }

    public void getHomeData() {
        if (isConnectingToInternet(this)) {
            networkCall.NetworkAPICall(API.CHANNEL_LIST, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    private void notifyAdapters() {
        liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();
    }


    private void setHomeData(JSONObject result) {


        mainChannelData = new Gson().fromJson(result.toString(), MainChannelData.class);
        SharedPreference.getInstance(context).setChannelData(mainChannelData);
        InitAdapter();


    }

    private void InitAdapter() {
        MainChannelData mainChannelData = SharedPreference.getInstance(context).getChannelData();
        homeChannelList.addAll(mainChannelData.getData());
        liveStreamOnTopLiveChannelAdapter = new LiveStreamOnTopLiveChannelAdapter(homeChannelList, this, LiveStreamJWActivity.this);

        channelNewAdapter = new ChannelNewAdapter(context, homeChannelList, channel_position);

        channel_recyclerView.setAdapter(channelNewAdapter);


        ((LiveStreamJWActivity) context).channel_recyclerView.setClipToPadding(false);
        linearLayoutManager.scrollToPositionWithOffset(channel_position, 25);

        // channel_recyclerView.smoothScrollToPosition(channel_position);

        LinearLayoutManager ontoplivetvLM = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        fragment_home_rv_livetv.setLayoutManager(ontoplivetvLM);
        fragment_home_rv_livetv.setAdapter(liveStreamOnTopLiveChannelAdapter);
    }

    private void setHomeData2(JSONObject result) {
        JSONArray jsonArrayChannel = result.optJSONArray("channel");

        if (jsonArrayChannel != null) {
            for (int i = 0; i < jsonArrayChannel.length(); i++) {
                channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
                if (channel.getId().equalsIgnoreCase(id)) {
                    Log.d("livestream", "asdfghi");

                    VIEWlike = channellist.get(i).getLikes();
                    Is_like_no = channellist.get(i).getIs_likes();
                    Log.d("checklive", VIEWlike);
                    Log.d("checklive1", Is_like_no);
                }
            }
        }
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
            } else {
                like_number_watch_channel.setText(likenum + 1 + " likes");

            }
            like_channel_button.setImageResource(R.mipmap.audio_liked);
//            video.setLikes(String.valueOf(likenum + 1));
//            video.setIs_like("1");
        } else {
            VIEWlike = String.valueOf(Integer.parseInt(VIEWlike) - 1);

            if (likenum == 1) {
                like_number_watch_channel.setText("0 like");
            } else if (likenum == 2) {
                like_number_watch_channel.setText(likenum - 1 + " like");
            } else {
                like_number_watch_channel.setText(likenum - 1 + " likes");
            }
            like_channel_button.setImageResource(R.mipmap.white_like);
//            video.setLikes(String.valueOf(likenum - 1));
//            video.setIs_like("0");
        }
        //  isLike = true;

    }


    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        // swipeRefreshLayout.setRefreshing(false);
        //  ToastUtil.showDialogBox(this, jsonstring);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.share_watch_video:
                // shareTextUrl();
//                //  shareAppUrl();
//                try {
//                    shareAppUrl();
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
                break;
            case R.id.share_iv:
                DynamicLinking();


                break;

        }

    }

    private void DynamicLinking() {
        SharedPreference.getInstance(this).putString(Constants.ISLIKE, Is_like_no);

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.sanskar.tv/?postId=" + livevideourl + "&id=" + id + "&view=" + VIEWlike))
                .setDynamicLinkDomain("sanskar.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            String msgHtml = String.format("<p>Let's see live video on sanskar tv app."
                                    + "<a href=\"%s\">Click Here</a>!</p>", shortLink.toString());

                            String msg = "Let's see live video.. Click on Link : " + shortLink.toString();

                            //sendLink(msg, msgHtml, this, bitmap);
                            // sendLink(activity, msg, msgHtml);


                            sendLink(LiveStreamJWActivity.this, "sanskar", msg,
                                    msgHtml
                            );

                        } else {
                            Log.d("TASK_EXCEPTION", task.getException().toString());
                            Toast.makeText(context, "Article link could not be generated please try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void shareApp() {
        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Uri imageUri = Uri.parse("android.resource://" + getPackageName() + "/mipmap/" + "ic_launcher_round");
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        //i.setType("image/*");

        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar App");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);
        i.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(i, "Share via"));
    }

    private void getBundleData() {
        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey("video_data")) {
                videosArray = (Videos[]) bundle.getSerializable("video_data");
                pos = bundle.getInt("position");
                video = videosArray[pos];
            } else if (bundle.containsKey("video_data_guru")) {
                video = (Videos) bundle.getSerializable("video_data_guru");
            }

        }
    }


    @SuppressLint("WrongViewCast")
    private void initView() {
        headerrel = findViewById(R.id.headerrel);
        ll_all_option = findViewById(R.id.ll_all_option);
        ll_option = findViewById(R.id.ll_option);
        rc_chat = findViewById(R.id.rc_chat);
        ll_chat = findViewById(R.id.ll_chat);
        fragment_watch_video_comment_layout = findViewById(R.id.fragment_watch_video_comment_layout);
        progressvideo = findViewById(R.id.progressvideo);
        progressvideo.setVisibility(View.GONE);
        container = findViewById(R.id.container);
        layout = findViewById(R.id.layout);
        share_iv = findViewById(R.id.share_iv);
        events_name = findViewById(R.id.events_name);
        share_s = findViewById(R.id.share_s);
        events_name.setSelected(true);
        fragment_home_rv_livetv = findViewById(R.id.fragment_home_rv_livetv);
        share_iv.setOnClickListener(this);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        videoResponses = new ArrayList<>();

    }

    public void handleToolbar() {
        toolbar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onFullscreen(boolean b) {

    }

    /*public static void sendLink(Activity activity, String link,String msgHTML){
        String shareBody = link;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, msgHTML);
        activity.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }*/

    private void MessageDatabase(String channelDatabase, String channelmain, String channel_id) {


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("sanskarliveChannels/");
        current_user_ref = channelmain;
        channelID = channel_id;
        channel_database = channelDatabase;
        loadmessage(channelmain);
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkfordeletechat = true;
                deletechat = true;

                String message = et_chat.getText().toString();
                if (signupResponse.getUsername() == null || signupResponse.getUsername().isEmpty()) {

                    Toast.makeText(LiveStreamJWActivity.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();

                } else {
                    if (!(message == null || message.equals(""))) {

                        DatabaseReference user_message_push = mRootReference.child(channelmain).push();

                        push_id = user_message_push.getKey();
                        chat_user_ref = channelDatabase;
                        Map messageMap = new HashMap();
                        messageMap.put("message", message.replaceFirst("\\s++$", ""));
                        messageMap.put("seen", false);
                        messageMap.put("type", "text");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", signupResponse.getId());
                        messageMap.put("status", "1");
                        messageMap.put("name", signupResponse.getUsername());
                        messageMap.put("img", signupResponse.getProfile_picture());
                  /*  messageMap.put("id", channelmain);
                    messageMap.put("channel_id", channel_id);*/
                        messageMap.put("pushid", push_id);
                        /*   messageMap.put("channel_name",channelDatabase);*/
                        Map messageUserMap = new HashMap();

                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

                        et_chat.setText("");
                        mRootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {

                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Log.e("CHAT_ACTIVITY", "Cannot add message to database");
                                } else {
                                    Toast.makeText(LiveStreamJWActivity.this, "Your comment posted successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }
            }
        });
        messageCount = String.valueOf(messageArrayList.size());

        getcomment(messageCount);
        chatAdapter = new ChatAdapter(this, messageArrayList);

        mLinearLayoutManager = new LinearLayoutManager(LiveStreamJWActivity.this);
        mLinearLayoutManager.setStackFromEnd(true);
        rc_chat.setHasFixedSize(true);
        rc_chat.setLayoutManager(mLinearLayoutManager);
        rc_chat.setAdapter(chatAdapter);

    }

    private void loadmessage(String channelmain) {

        DatabaseReference messageRef = null;
        DatabaseReference messageRef2 = null;

        messageRef = mRootReference.child(channelmain);


        //   Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
        messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (deletechat == true) {
                    Log.d("delete", "abc");

                    Message messages = (Message) dataSnapshot.getValue(Message.class);
                    messages.setKey(dataSnapshot.getKey());
                    keyList.add(dataSnapshot.getKey());
                    itemPos++;

                    if (itemPos == 1) {
                        String mMessageKey = dataSnapshot.getKey();

                        mLastKey = mMessageKey;
                        mPrevKey = mMessageKey;
                    }
                    if (messages.getStatus().equals("1"))
                        messageArrayList.add(messages);


                    chatAdapter.notifyDataSetChanged();

                    rc_chat.scrollToPosition(messageArrayList.size() - 1);

                    messageCount = String.valueOf(messageArrayList.size());
                    getcomment(messageCount);
                    Log.d("messagesize", String.valueOf(messageArrayList.size()));


                } else {
                    Log.d("delete", "def");
                    //checkfordeletechat=true;
                }
                // Log.d("messagescount", String.valueOf(messageCount));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (checkfordeletechat == false) {
                    Log.d("delete", "cde");
                    //    networkCall.NetworkAPICall(API.INFORM_DELETE_CHAT, true);
                    int index = keyList.indexOf(dataSnapshot.getKey());
                    messageArrayList.remove(index);
                    messageCount = String.valueOf(messageArrayList.size());
                    Log.d("msgcount", String.valueOf(messageCount));
                    getcomment(messageCount);
                    keyList.remove(index);
                    chatAdapter.notifyDataSetChanged();
                    // keyList.notifyAll();

                    deletechat = false;
                    checkfordeletechat = true;
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };

        messageQuery.addChildEventListener(childEventListener);
    }


    private void ShowUsers(String channelUser) {
        Log.d("take1", "check");
        DatabaseReference messageRef = null;
        Particularchannel = channelUser;
        //  DatabaseReference messageRef2 = null;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Particularchannel);
        //  mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Particularchannel);
        messageRef = mDatabaseReference;

        Log.d("take2", "check");

        Log.d("checkchangechnl1", Particularchannel);

        //   Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
        showmessagequery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEM_TO_LOAD);
        Log.d("take3", "check");

       /* messageRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Log.d("transcation","tr1");
                int countModel=mutableData.getValue(CountModel.class).getCount() ;
                Log.d("transcation","tr3");
                if(IsSHOWuser==true){
                   mutableData.getValue(CountModel.class).setCount(countModel+1);
                   Log.d("transcation","tr2");
                }
                else {
                    mutableData.getValue(CountModel.class).setCount(countModel-1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });



        ValueEventListener eventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int countModel=dataSnapshot.getValue(CountModel.class).getCount();
                Log.d("TAG", String.valueOf(countModel) + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        messageRef.addListenerForSingleValueEvent(eventListener);


*/


        showchildEventListner = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // int Count=dataSnapshot.getValue();
                Log.d("onchild Added", "checking1");

                if (checkForShowUser == true) {

                    Log.d("show", dataSnapshot.getKey());
                    Log.d("onchild Added", "checking2");
                    Long countModel = (Long) dataSnapshot.getValue();
                    ccount = countModel + 1;
                    Log.d("onchild Added", "checking3");
                    // UserOnline(Particularchannel);
                    UserOnline(channelDatabase, channelmain, channel_id);
                } else {
                    Log.d("show2", dataSnapshot.getKey());
                    Log.d("onchild Added", "checking4");
                    Long countModel = (Long) dataSnapshot.getValue();
                    ccount = countModel - 1;
                    Log.d("onchild Added", "checking5");

                    //  UserOnline(Particularchannel);
                    UserOnline(channelDatabase, channelmain, channel_id);

                    // checkForShowUser=false;
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("checkforlistner", "onChildChanged");
                Log.d("checkforlr", String.valueOf(ccount));


                getLiveView(ccount);


                //  UserOnline(channelDatabase, channelmain,channel_id);

                if (checkForShowUser == false) {
                    Log.d("show", dataSnapshot.getKey());
                    Long countModel = (Long) dataSnapshot.getValue();
                    ccount = countModel - 1;
                    UserOnline(channelDatabase, channelmain, channel_id);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("checkforlistner", "onChildRemoved");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("checkforlistner", "onChildMoved");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };
        Log.d("onchild Added", "checkingnotinchildadded");

        showmessagequery.addChildEventListener(showchildEventListner);
    }

    private void getcomment(String messageCount) {
        if (messageCount.equals("0")) {
            comment_number_watch_channel.setText("0 Comment");
        } else if (messageCount.equals("1")) {
            comment_number_watch_channel.setText(messageCount + " Comment");
        } else {
            comment_number_watch_channel.setText(messageCount + " Comments");
        }
    }

    private void getLiveView(Long messageCount) {
        if (messageCount.equals("0")) {
            view_user_watch_channel.setText("0 View");
        } else if (messageCount.equals("1")) {
            view_user_watch_channel.setText(messageCount + " view");
        } else {
            view_user_watch_channel.setText(messageCount + " Views");
        }
    }

    public void LoadChannel(String viewlike, String is_likes, String channel_url, String channel_id, String image, int position, String name) {

        if (simpleExoPlayer.getPlayWhenReady()) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        if (simpleExoPlayerAds != null) {
            simpleExoPlayerAds.setPlayWhenReady(false);
            playerViewAdsHome.setVisibility(View.GONE);
        }

        channel_name = name;

        if (id.equalsIgnoreCase("27")){
            newUrl = channel_url;
        } else {
            newUrl = channel_url + "?start=" + previousDate() + "&end=";
        }

        if (simpleExoPlayerAds1 != null) {
            simpleExoPlayerAds1.setPlayWhenReady(false);
            playerViewAdsHome.setVisibility(View.GONE);
        }

        messageQuery.removeEventListener(childEventListener);
//       showmessagequery.removeEventListener(showchildEventListner);
//
        messageArrayList = new ArrayList<>();
        keyList = new ArrayList<>();

        Log.d("checkchangechnl", "not a problem");

        video_url = channel_url;
        id = channel_id;

        deletechat = true;
        // checkdeletechat="0";
        // deletechat = false;
        checkfordeletechat = true;
        //  checkForShowUser = true;
        getChannelChat(id);

        // Likes_no=viewlike;
        VIEWlike = viewlike;
        Is_like_no = is_likes;
        Glide.with(this)
                .load(image)
                .apply(new RequestOptions().error(R.mipmap.landscape_placeholder).placeholder(R.mipmap.landscape_placeholder))
                .into(channel_image1);
        Channel_image = image;
        //  setChannelData();
        setChannelRelatedData();
        /*progressvideo.setVisibility(View.GONE);
        playIconIV.setVisibility(View.VISIBLE);
        channel_image1.setVisibility(View.VISIBLE);
        channel_image_layout.setVisibility(View.VISIBLE);
        Glide.with(this).load(Channel_image).into(videoImage);
        playIconIV.setOnClickListener(v -> {
            playIconIV.setVisibility(View.GONE);
            channel_image1.setVisibility(View.GONE);
            channel_image_layout.setVisibility(View.GONE);

            Uri uri = Uri.parse(newUrl);
            if (LiveFragment.Is_premium.equalsIgnoreCase("0")) {
                if (SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds() != null && SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds().length != 0) {
                    liveTvAds = SharedPreference.getInstance(LiveStreamJWActivity.this).getLiveTvAds();
                    if (Networkconstants.adsCount > liveTvAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    Log.d("shantanu", newUrl);
                    playVideo1(uri);
                    //playads(liveTvAds[Networkconstants.adsCount], uri);
                } else {
                    playVideo1(uri);
                }
            } else {
                playVideo1(uri);
            }
        });*/

        days_txtView.setText("Today");


        date_rl.setVisibility(View.VISIBLE);
        event_recyclerView.setVisibility(View.VISIBLE);
        Log.d("shantanu", "position: " + position);
        when_Forward_backward = daysArrayList.size() - 1;
        initLandingPageAdapter(daysArrayList.size() - 1, position);


    }


    public void removemsg(String msg, String mm, int i, List<Message> messages) {
        FirebaseDatabase.getInstance().getReference().child(msg)
                .child(mm)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        chatAdapter.notifyDataSetChanged();

                        liveStreamOnTopLiveChannelAdapter.notifyDataSetChanged();


                        Toast.makeText(activity, "deleted", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
