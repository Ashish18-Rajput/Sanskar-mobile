package com.sanskar.tv.module.HomeModule.Activity;

import static com.sanskar.tv.Others.Helper.Utils.clearEditText;
import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.module.HomeModule.Adapter.BhajanViewAllCategoryWiseAdapterRecentChange.bhajanforImage;
import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.NotificationAdapter.bhajanforImages;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.ReleatedSongAdapter.relatedbhajan;
import static com.sanskar.tv.module.HomeModule.Adapter.ReleatedSongAdapter1.relatedbhajan1;
import static com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment.BROADCAST_PLAY_NEXT_SONG;
import static com.sanskar.tv.module.HomeModule.Fragment.HomeFragment1.simpleExoPlayer;
import static com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity.isConnectingToInternet;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.koushikdutta.async.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.NotificationDialog;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Fragment.PremiumCategoryFragment;
import com.sanskar.tv.R;
import com.sanskar.tv.ScannerView.ScannerView;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.inAppUpdate.UpdateManager;
import com.sanskar.tv.jwPlayer.KeepScreenOnHandler;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.jwPlayer.OnSwipeTouchListener;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Fragment.ActiveDevicesFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanListByMenumaster;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanPlayFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajanViewAllFragment;
import com.sanskar.tv.module.HomeModule.Fragment.BhajansCategoryFragment;
import com.sanskar.tv.module.HomeModule.Fragment.CommonSearchFragment;
import com.sanskar.tv.module.HomeModule.Fragment.DownloadsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.GuruDetailsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.GuruListFragmentNew;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.HomeFragment1;
import com.sanskar.tv.module.HomeModule.Fragment.HomeViewAllFrag;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Fragment.LiveFragment;
import com.sanskar.tv.module.HomeModule.Fragment.LiveTvFragment;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFrag;
import com.sanskar.tv.module.HomeModule.Fragment.NewsDetailFragRecent;
import com.sanskar.tv.module.HomeModule.Fragment.NewsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PaymentFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;
import com.sanskar.tv.module.HomeModule.Fragment.PremiumEpisodeFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PremiumSeriesFragment;
import com.sanskar.tv.module.HomeModule.Fragment.ProfileFragment;
import com.sanskar.tv.module.HomeModule.Fragment.ProfileOtpFragment;
import com.sanskar.tv.module.HomeModule.Fragment.PromoPremiumFragment;
import com.sanskar.tv.module.HomeModule.Fragment.Related_Bhajan_expandable;
import com.sanskar.tv.module.HomeModule.Fragment.SankirtanListFrag;
import com.sanskar.tv.module.HomeModule.Fragment.SearchBhajanFrag;
import com.sanskar.tv.module.HomeModule.Fragment.SearchHomeFrag;
import com.sanskar.tv.module.HomeModule.Fragment.SearchInterface;
import com.sanskar.tv.module.HomeModule.Fragment.SearchSankirtanFrag;
import com.sanskar.tv.module.HomeModule.Fragment.SearchVideoFragment;
import com.sanskar.tv.module.HomeModule.Fragment.SeasonFragment;
import com.sanskar.tv.module.HomeModule.Fragment.TNCFragment;
import com.sanskar.tv.module.HomeModule.Fragment.VideoListByMenuMaster;
import com.sanskar.tv.module.HomeModule.Fragment.VideosChildFragment;
import com.sanskar.tv.module.HomeModule.Fragment.VideosParentFragment;
import com.sanskar.tv.module.HomeModule.Fragment.ViewAllVideosFragment;
import com.sanskar.tv.module.HomeModule.Fragment.ViewCommentsFragment;
import com.sanskar.tv.module.HomeModule.Fragment.WallpaperFragment;
import com.sanskar.tv.module.HomeModule.Fragment.WallpaperViewAllFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.Category;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.News;
import com.sanskar.tv.module.HomeModule.Pojo.SankirtanBean;
import com.sanskar.tv.module.HomeModule.Pojo.Seasons;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponse;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.HomeModule.Pojo.WallPaperModel;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;
import com.sanskar.tv.module.NotificationStatus;
import com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity;
import com.sanskar.tv.module.goLiveModule.controller.LiveGuruActivity;
import com.sanskar.tv.module.loginmodule.Fragment.SetOtpFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivityy extends AppCompatActivity implements View.OnClickListener,
        VideoPlayerEvents.OnFullscreenListener, NetworkCall.MyNetworkCallBack, YouTubePlayer.OnInitializedListener {
    public static TextView notifyTV;
    public static final String STOP_SERVICE = "stop service";
    public static Bhajan[] bhajans;
    public static MenuMasterList[] bhajans2;
    public static String FROMDEEPLINK = "";
    public boolean isInFullScreen = false;
    Channel channel;
    public static String frag_type = "", postid, RegType, youtubeid, commentid;
    public static Bhajan[] currentplayingbhajan;
    public static Bhajan currentplayingbhajannext;
    public static MenuMasterList currentplayingbhajannext1;
    public static final String PLAYVIDEO_JWPLAYER = "start video player";
    public static BottomNavigationView bottomNavigationView;
    public static RelativeLayout playerlayout, playerlayout1, playerlayout2, playerlayout3;
    public static ImageView playpause, thumbaudio, img_cancel, backward, forward;
    public static ImageView playpause1, thumbaudio1, img_cancel1, backward1, forward1;
    public static ImageView playpause3, thumbaudio3, img_cancel3, backward3, forward3;
    public static ImageView playpause2, thumbaudio2, img_cancel2, backward2, forward2;
    public static TextView titleaudio, descriptionaudio;
    public static TextView titleaudio1, descriptionaudio1;
    public static TextView titleaudio2, descriptionaudio2;
    public static TextView titleaudio3, descriptionaudio3;
    Switch notification_lay;
    String notification_type = "";
    String get_notification_status;
    public long gap_Duration_live_channel = 0;
    /* public static BrightcoveExoPlayerVideoView playerView;
     public static BrightcoveExoPlayerVideoView playerView2;*/
    public static YouTubePlayerFragment utubePlayer;
    public static YouTubePlayerFragment utubePlayer2;
    public static YouTubePlayer myoutubePlayer;
    public static YouTubePlayer youtubePlayer2;
    public static FrameLayout frameLayout;
    public static RelativeLayout framelayoututube;
    public DrawerLayout drawer;
    public FrameLayout container;
    public SignupResponse signupResponse;
    public ArrayList<BhajanResponse> bhajanResponseArrayList;
    public ArrayList<News> newsList;
    public ArrayList<GuruPojo[]> guruList;
    public ArrayList<Channel> homeChannelList;
    public List<MenuMasterList> homeChannelList1 = new ArrayList<>();
    public static ArrayList<Channel> channellist;
    public List<Bhajan> bhajanList;
    RelativeLayout notify_rl;
    public ArrayList<VideoResponse> homeVideosList;
    public List<SankirtanBean> sankirtanBeanList;
    public ArrayList<Category> videoFragmentVideoesCategoryList;
    public HashMap<String, Videos[]> videoFragmentVideosList;
    public List<Banners> bannersList;
    public ArrayList<VideoResponse> videoResponses;
    public boolean isLike = false;
    public List<Videos> homeVideoList;
    public List<Bhajan> homeBhajanList = new ArrayList<>();
    public List<Bhajan> homeBhajanList1 = new ArrayList<>();
    public List<MenuMasterList> homeBhajanListNew = new ArrayList<>();
    public List<MenuMasterList> homeBhajanList1New = new ArrayList<>();
    public List<News> homeNewsList;
    public List<Seasons> seasonsList;
    public List<GuruPojo> homeGuruList = new ArrayList<>();
    public List<MenuMasterList> homeGuruList1 = new ArrayList<>();
    public String back1 = "";
    public String promotionUrl = "";
    public String promotionType = "";
    public String searchContent;
    public AppTextView saveTV;
    public AppTextView cancle_noti;
    public AppTextView clear_notification;
    public AppTextView editTV, set_pin;
    public ImageView backIV, backNotification;
    public String homevideourl;
    public String check_notification_by_main_api;
    public String notification_counter;
    public RelativeLayout floatingbutton;
    public LinearLayout premiumLayout, premium_layout;
    public Toolbar toolbar;
    public SearchView searchView;
    public CardView searchParent;
    ImageView searchCommon;
    Context context;
    View navHeader;
    TextView navName, navEmail;
    CircleImageView profileImage;
    LinearLayout ll_share_link;
    public static RelativeLayout notify_ll;
    public String video_url, video_id, channel_name, view_like, is_like_no;
    public int channel_position;
    Boolean is_post_exit = true;
    RelativeLayout jwPlayerlayout;
    Boolean is_premium_active = true;
    LinearLayout live_channel, blank_space_1, blank_space_2;
    SearchView searchIv;
    ImageView floatingActionButton, floatingActionButton1;
    ImageView app_logo;
    boolean viewremoved = false;
    boolean likeyoutube = false;
    public int bhajanIndex;
    public int bhajanIndex1;
    ProgressBar progressvideo, progressvideo1, progressvideoutube, progressvideoutube1;
    KeepScreenOnHandler keepScreenOnHandler;
    String url;
    TextView name, description, date, downloadwatch_video;
    TextView name1, description1, date1, downloadwatch_video1;
    ImageView back;
    Videos video;
    RecyclerView relatedVideosList, relatedVideosList1;
    ViewAllAdapter adapter, adapter1;
    RecyclerView.LayoutManager layoutManager, layoutManager1;
    RelativeLayout share, share1;
    TextView viewNumber, likeNumber, viewNumber1, likeNumber1;
    NestedScrollView swipeRefreshLayout;
    SwipeRefreshLayout refresh;
    EditText comment;
    TextView postComment;
    ProgressBar commentProgress;
    public Progress progress;
    ImageView profile_iv;
    public ImageView tv_iv;
    LinearLayout youtubeLayout;
    BottomSheetBehavior behavior;
    LinearLayout layoutheader, tv_guide_layout;
    File file;
    ConstraintLayout layout;
    SearchInterface mSearchListener;
    ConstraintSet constraintsetold = new ConstraintSet();
    ConstraintSet constraintsetnew = new ConstraintSet();
    boolean altlayout;
    RelativeLayout headerrel, relative_l1;
    RelativeLayout headerrelyou, relativeyou_l1;
    FrameLayout frameyoutubecontainer;
    Boolean Rotation = false;
    Boolean RotationFull = false;
    public Fragment fragment;
    public List<Bhajan> bhajanList_new = new ArrayList<>();
    CircularProgressBar circularProgressBar;
    //Declare the UpdateManager
    UpdateManager mUpdateManager;
    public ImageView qrCode;
    private int mPage = 1;
    private Videos[] vd = new Videos[]{};
    private Bitmap bitmap;
    private LinearLayout homeLay;
    private LinearLayout videoLay;
    private LinearLayout guruLay;
    private LinearLayout sankirtanLay;
    private LinearLayout bhajanLay;
    private LinearLayout newsAndArticleLay;
    private LinearLayout goLiveLay;
    private LinearLayout myPlaylistLay;
    public static Bhajan[] bhajanforImagefromHome;
    public static int newspos1;
    private LinearLayout termsAndConditionsLay;
    private LinearLayout privacyLay, my_downloads_lay, backImage;
    //private LinearLayout logoutLay;
    //bottom nav
    private LinearLayout homeBottomLay;
    private LinearLayout videoBottomLay;
    private LinearLayout bhajanBottomLay;
    private LinearLayout guruBottomLay;
    private LinearLayout articleBottomLay;
    private ImageView notificationImg;
    private ImageView accountImg;
    private ImageView tvImg, ivaddPlayList;
    private ActionBarDrawerToggle toggle;
    List<Bhajan> bhajans1;
    LinearLayout wallpaper_rl;
    private int pageSize;
    //public  RelativeLayout profileLayout;
    private TextView toolbarTitle;
    private MenuItem itemSearch;
    private MenuItem itemNotification;
    private MenuItem tvMenu;
    private boolean loading = false;
    private long backPressed;
    private ImageView appLogo;
    private ImageView searchImg;
    private TextView searchText;
    private AppTextView closeSearchIV;
    private RelativeLayout noDataFound, noDataFound1;
    private ArrayList<Videos> videoResponses1;
    private NetworkCall networkCall;
    private LinearLayout nonFullScreenLayout, nonFullScreenLayout1;
    private ImageView like, like1;
    private RelativeLayout container1;
    private boolean isLike1 = false;
    private boolean isComment = false;
    private ImageView download;
    private long downloadID;
    private Uri Download_Uri;
    public String Video_id = "";
    public String Premium_Cat_Id = "";
    public String Premium_Auth_Id = "";
    public String Category = "";
    public String Bhajan_id = "";
    private TextView commentNumbers;
    private Videos[] videos;
    private Videos[] videosArray;
    private int pos;
    private CastContext mCastContext;
    private CastSession mCastSession;
    public static int audioplaymainpositionindex;
    public int AudioIndex;
    Activity activity;
    String id;
    String likeV;
    String IS_lik = "";
    String web_view_bhajan, web_view_news, web_view_video;
    String redirect;
    public String Category_name = "";
    public int plays = 0;
    public int pause = 0;
    public int pause_1 = 0;
    public static int ads_count = 0;
    public Bhajan[] dropbhajan;
    private String media_id = "";
    private String data_type = "";
    private String dataType = "";
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mQueueMenuItem;

    boolean fromGuest = false;


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            circularProgressBar.setVisibility(View.GONE);
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                //   Toast.makeText(HomeActivityy.this, "Download Completed", Toast.LENGTH_SHORT).show();
                download.setImageResource(R.drawable.download_complete);
                downloadwatch_video.setText("Downloaded");
            }
        }
    };

    private BroadcastReceiver startVideoPlayer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            framelayoututube.bringToFront();
            viewremoved = false;

            if (myoutubePlayer != null) {
                if (myoutubePlayer.isPlaying()) {
                    myoutubePlayer.pause();
                    utubePlayer.onStop();
                    framelayoututube.setVisibility(View.GONE);
                }
            }

            signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
            if (intent.getSerializableExtra("video_data") != null) {
                videosArray = (Videos[]) intent.getSerializableExtra("video_data");
                pos = intent.getIntExtra("position", 0);
                video = videosArray[pos];

                if (video.getYoutube_url().equals("")) {
                    initplayerLayout();
                    initplayer();
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.bringToFront();
                    headerrel.setVisibility(View.VISIBLE);

                }

            }
        }
    };

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Quiz Image", null);
        return Uri.parse(path);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (getIntent().getExtras() != null) {
            frag_type = getIntent().getExtras().getString(Constants.FRAG_TYPE);
        }

        Intent intent2 = getIntent();
        if (getIntent() != null && getIntent().hasExtra("OuterData")) {
            Uri data = Uri.parse(getIntent().getStringExtra("OuterData"));
            String url = data.toString();
            String[] Id = url.split("/");
            media_id = Id[Id.length - 1];
            data_type = Id[Id.length - 2];
            Log.d("shantanu", media_id);
            Log.d("shantanu", data_type);
            if (data_type.equals("video")) {
                dataType = "1";
            }
            if (data_type.equals("bhajan")) {
                dataType = "2";
            }
            if (data_type.equals("news")) {
                dataType = "3";
            }
            fetchUrlData(true);

            String postId = data.getQueryParameter("postId");
            id = data.getQueryParameter("id");

            likeV = data.getQueryParameter("view");

            if (SharedPreference.getInstance(this).getString(Constants.ISLIKE) != null || !SharedPreference.getInstance(this).getString(Constants.ISLIKE).equalsIgnoreCase("")) {
                IS_lik = SharedPreference.getInstance(this).getString(Constants.ISLIKE);
            } else {
                IS_lik = "0";
            }


            if (postId != null && id != null && likeV != null) {
                Intent sintent = new Intent(getApplicationContext(), LiveStreamJWActivity.class);
                sintent.putExtra("livevideourl", postId);
                sintent.putExtra("channel_id", id);
                sintent.putExtra("from", "livetv");
                sintent.putExtra("VIEWlike", likeV);
                sintent.putExtra("Is_like_no", IS_lik);
                this.startActivity(sintent);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity=this;
        networkCall = new NetworkCall(this, HomeActivityy.this);
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);


        FirebaseCrashlytics.getInstance().setUserId(signupResponse.getId());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        FirebaseAnalytics.getInstance(this).setUserId(signupResponse.getId());
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);


        if (getIntent() != null && getIntent().hasExtra("OuterData")) {
            Uri data = Uri.parse(getIntent().getStringExtra("OuterData"));
            Log.d("shantanu", data.toString());
            String url = data.toString();
            String[] Id = url.split("/");
            media_id = Id[Id.length - 1];
            data_type = Id[Id.length - 2];
            Log.d("shantanu", media_id);
            Log.d("shantanu", data_type);
            if (data_type.equals("video")) {
                dataType = "1";
            }
            if (data_type.equals("bhajan")) {
                dataType = "2";
            }
            if (data_type.equals("news")) {
                dataType = "3";
            }
            fetchUrlData(true);

            String postId = data.getQueryParameter("postId");
            id = data.getQueryParameter("id");

            likeV = data.getQueryParameter("view");
            if (SharedPreference.getInstance(this).getString(Constants.ISLIKE) != null || !SharedPreference.getInstance(this).getString(Constants.ISLIKE).equalsIgnoreCase("")) {
                IS_lik = SharedPreference.getInstance(this).getString(Constants.ISLIKE);
            } else {
                IS_lik = "0";
            }

            if (postId != null && id != null && likeV != null) {

                Intent sintent = new Intent(getApplicationContext(), LiveStreamJWActivity.class);
                sintent.putExtra("livevideourl", postId);
                sintent.putExtra("channel_id", id);
                sintent.putExtra("from", "livetv");
                sintent.putExtra("VIEWlike", likeV);
                sintent.putExtra("Is_like_no", IS_lik);
                this.startActivity(sintent);
            }
        }

        if (getIntent().getExtras() != null && getIntent().hasExtra(Constants.FROM_GUEST)) {
            if (getIntent().getStringExtra(Constants.FROM_GUEST).equalsIgnoreCase("1")) {
                fromGuest = true;
            } else {
                fromGuest = false;
            }
        }

        if (fromGuest){
            final BottomSheetDialog dialog1 = new BottomSheetDialog(this, R.style.BottomSheetDialog);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // dialog1.setCancelable(true);
            dialog1.setCancelable(false);
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setContentView(R.layout.dialogbox_language2);
            Window window = dialog1.getWindow();
            window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            TextView textView=dialog1.findViewById(R.id.message);

            textView.setText("Please login first..");

            Button btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
            btn_proceed1.setOnClickListener(v -> {
                dialog1.dismiss();
                Intent intent = new Intent(this, LoginSignupActivity.class);
                intent.putExtra(Constants.FROM_GUEST, "1");
                SharedPreference.getInstance(this).putBoolean(Constants.FROM_GUEST, false);
                (HomeActivityy.this).finish();
                startActivity(intent);

            });

            dialog1.show();
        }

        View decorView = getWindow().getDecorView();

        decorView.setOnSystemUiVisibilityChangeListener(i -> {
            if (frameLayout.getVisibility() == View.VISIBLE) {
                bottomNavigationView.setVisibility(View.VISIBLE);
                onWindowFocusChanged(true);
            }
        });

        setContentView(R.layout.activity_home_activityy);

        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        mCastContext = CastContext.getSharedInstance(this);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        getVideoDataToPlay();

        initViews();

        if (SharedPreference.getInstance(this).getString(Constants.IS_PREMIUM_ACTIVE) != null) {
            String is_active = SharedPreference.getInstance(this).getString(Constants.IS_PREMIUM_ACTIVE);
            if (is_active.equalsIgnoreCase("1")) {
                changeBottomBand(true);
            }
            if (is_active.equalsIgnoreCase("2")) {
                changeBottomBand(false);
            }
        }

        //Utils.setStatusBarGradiant(this, container);

        Intent bundle = getIntent();

        if (bundle.hasExtra("id")) {
            Video_id = bundle.getStringExtra("id");
        }

        if (bundle.hasExtra("category")) {
            Category = bundle.getStringExtra("category");
        }

        if (bundle.hasExtra("category")) {
            showPremiumEpisodeFrag();
        } else {
            onClick(homeLay);
        }

        //onClick(playpause);

        playerlayout.setOnClickListener(view -> {
            if (Constants.AUDIO_ACTIVE_LIST.equals("bhajan")) {

                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                audioplaymainpositionindex = PreferencesHelper.getInstance().getIntValue("audioplayindex", 0);
                bundle1.putSerializable("bhajans", bhajanResponseArrayList.get(audioplaymainpositionindex).getBhajan());
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            } else if (Constants.AUDIO_ACTIVE_LIST.equals("bhajans")) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                bundle1.putSerializable("bhajans", bhajanforImage);

                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (Constants.AUDIO_ACTIVE_LIST.equals("notificationbhajan")) {

                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                /*   bundle1.putSerializable("bhajans", bh);*/
                bundle1.putSerializable("bhajans", bhajanforImages);

                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (Constants.AUDIO_ACTIVE_LIST.equals("relatedbhajan")) {

                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                bundle1.putSerializable("bhajans", relatedbhajan);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);

                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (Constants.AUDIO_ACTIVE_LIST.equals("relatedbhajan1")) {
                Bundle bundle1 = new Bundle();

                bundle1.putInt("position", Constants.AUDIO_INDEX);

                bundle1.putSerializable("bhajans1", relatedbhajan1);

                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);

                //((HomeActivityy) getApplicationContext()).

                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (Constants.AUDIO_ACTIVE_LIST.equals("home")) {
                bhajans2 = new MenuMasterList[homeBhajanListNew.size()];
                for (int i = 0; i < homeBhajanListNew.size(); i++) {
                    bhajans2[i] = homeBhajanListNew.get(i);
                }

                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", Constants.AUDIO_INDEX);
                bundle1.putSerializable("bhajans1", bhajans2);

                if (Networkconstants.bhajan_name.equalsIgnoreCase("radio"))
                    bundle1.putString("status", "radio");
                if (Networkconstants.bhajan_name.equalsIgnoreCase("bhajan"))
                    bundle1.putString("status", "bhajan");


                //PreferencesHelper.getInstance().setValue("index",Constants.AUDIO_INDEX);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle1);
                /*((HomeActivityy) getApplicationContext()).*/
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });

        Intent intent = getIntent();
        if (intent.getSerializableExtra("video_data") != null) {
            videos = (Videos[]) intent.getSerializableExtra("video_data");

            int defaultValue = 0;
            int id = intent.getIntExtra("position", defaultValue);
            // String id = intent.getStringExtra("position");
            Intent intentstart = new Intent(PLAYVIDEO_JWPLAYER);
            intentstart.putExtra("position", id);
            intentstart.putExtra("video_data", videos);
            sendBroadcast(intentstart);
            //  String name = intent.getStringExtra("name");
        }

        if (Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Rotation = true;
            Log.e("Rotation", "On");
        } else {
            Rotation = false;
            Log.e("Rotation", "off");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        checksearchoption();
    }

    private void fetchUrlData(boolean b) {
        if (Utils.isConnectingToInternet(this)) {
            networkCall.NetworkAPICall(API.GET_URL_DATA, b);
        } else {
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void getdata(boolean progress) {
        Log.d("shantanu", "ss");
        if (Utils.isConnectingToInternet(this)) {
            networkCall.NetworkAPICall(API.HOME_PAGE_VIDEOS, progress);
        } else {
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void checksearchoption() {
        if (!searchIv.isIconified()) {
            searchIv.setIconified(true);
            searchIv.onActionViewCollapsed();
        }
             /*searchIv.setFocusable(false);
                searchIv.clearFocus();*/
        app_logo.setVisibility(View.VISIBLE);
        tvImg.setVisibility(View.GONE);
        profile_iv.setVisibility(View.VISIBLE);
    }

    public void setSetSearchListener(SearchInterface mSearchListener) {
        this.mSearchListener = mSearchListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.player, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item);
        mQueueMenuItem = menu.findItem(R.id.action_show_queue);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_show_queue).setVisible(
                (mCastSession != null) && mCastSession.isConnected());
        return super.onPrepareOptionsMenu(menu);
    }

    private void getVideoDataToPlay() {
        IntentFilter intentFilter = new IntentFilter(PLAYVIDEO_JWPLAYER);
        registerReceiver(startVideoPlayer, intentFilter);
    }

    private void getVideoDataToPlayYoutube() {
        IntentFilter intentFilter = new IntentFilter(PLAYVIDEO_JWPLAYER);
        registerReceiver(startVideoPlayer, intentFilter);
    }


    private void initplayerLayout() {
        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);
        toolbar.setVisibility(View.VISIBLE);
        //headerrel = findViewById(R.id.header);
        backImage = findViewById(R.id.dropdown);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_watch_video1);
        backImage.setOnClickListener(this);
    }


    private void initplayer() {
        download.setClickable(true);
        Download_Uri = Uri.parse(API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/");
        //Utils.setStatusBarGradiant(this, container);

        String baseUrl = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/";
        if (video.getVideo_url().contains("//")) {
            url = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/SampleVideo_1280x720_1mb.mp4/playlist.m3u8";
        } else {
            url = baseUrl + video.getVideo_url() + "/playlist.m3u8";
        }

        String url = video.getVideo_url();
        String[] urltosppend = url.split("com");

        url = "http://52.204.183.54:1935/vods3/" + "_definst_/mp4:amazons3/bhaktiappproduction" +
                urltosppend[1] + "/playlist.m3u8";

        file = new File(getExternalFilesDir("bhakti"), video.getVideo_title() + ".jwp");

        if (file.exists()) {
            download.setImageResource(R.drawable.download_complete);
            downloadwatch_video.setText("Downloaded");
        } else {
            download.setImageResource(R.drawable.download);
            downloadwatch_video.setText("Download");
        }


        // url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

        PlaylistItem item = new PlaylistItem(url);

        backImage.setOnClickListener(view -> {

            frameLayout.setVisibility(View.GONE);
            relative_l1.setVisibility(View.VISIBLE);
        });

        relative_l1.setOnTouchListener(new OnSwipeTouchListener(HomeActivityy.this) {
            public void onSwipeTop() {
                frameLayout.setVisibility(View.VISIBLE);
                relative_l1.setVisibility(View.GONE);
            }

            public void onSwipeRight() {
                // Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
            }
        });

        networkCall = new NetworkCall(this, HomeActivityy.this);

        setVideoData();
        getRelatedVideos();
    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    public void changeBottomBand(boolean b) {
        is_premium_active = b;
        if (b) {
            live_channel.setVisibility(View.VISIBLE);
            blank_space_1.setVisibility(View.GONE);
            blank_space_2.setVisibility(View.GONE);
            premiumLayout.setVisibility(View.VISIBLE);
            floatingbutton.setVisibility(View.GONE);
            premium_layout.setVisibility(View.VISIBLE);
        }
        if (!b) {
            live_channel.setVisibility(View.GONE);
            blank_space_1.setVisibility(View.VISIBLE);
            blank_space_2.setVisibility(View.VISIBLE);
            premiumLayout.setVisibility(View.GONE);
            floatingbutton.setVisibility(View.VISIBLE);
            premium_layout.setVisibility(View.GONE);
        }
        handleToolbar();
    }

    private void getRelatedVideos() {
        if (isConnectingToInternet(this)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, this);
            networkCall.NetworkAPICall(API.RELATED_VIDEOS, false);
        } else {
            // refresh.setRefreshing(false);
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void setVideoData() {
        if (video != null) {
            setRelatedVideoData();
        }
    }


    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");

        share.putExtra(Intent.EXTRA_TEXT, video.getAuthor_name());
        share.putExtra(Intent.EXTRA_SUBJECT, video.getVideo_title());
        share.putExtra(Intent.EXTRA_TEXT, video.getVideo_url());
        startActivity(Intent.createChooser(share, "Share URL"));
    }

    private void setRelatedVideoData() {
        if (video.getYoutube_url().equals("")) {
            name.setText(video.getAuthor_name());
            date.setText(getDate(Long.parseLong(video.getCreation_time())));

            String desc = Html.fromHtml(video.getVideo_desc()).toString();

            description.setText(desc);

            if (video.getLikes().equals("0")) {
                likeNumber.setText("no like");
            } else if (video.getLikes().equals("1")) {
                likeNumber.setText((video.getLikes() + " like"));
            } else {
                likeNumber.setText((video.getLikes() + " likes"));
            }

            if (video.getViews().equals("0")) {
                viewNumber.setText("no view");
            } else if (video.getViews().equals("1")) {
                viewNumber.setText((video.getViews() + " view"));
            } else {
                viewNumber.setText((video.getViews() + " views"));
            }

            if (video.getIs_like().equals("1")) {
                like.setImageResource(R.drawable.liked);
                //likeNumber.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.liked,0,0);
                like.setTag(true);
            }
        } else {
            name1.setText(video.getAuthor_name());
            date1.setText(getDate(Long.parseLong(video.getCreation_time())));

            String desc = Html.fromHtml(video.getVideo_desc()).toString();

            description1.setText(desc);

            if (video.getLikes().equals("0")) {
                likeNumber1.setText("no like");
            } else if (video.getLikes().equals("1")) {
                likeNumber1.setText((video.getLikes() + " like"));
            } else {
                likeNumber1.setText((video.getLikes() + " likes"));
            }

            if (video.getViews().equals("0")) {
                viewNumber1.setText("no view");
            } else if (video.getViews().equals("1")) {
                viewNumber1.setText((video.getViews() + " view"));
            } else {
                viewNumber1.setText((video.getViews() + " views"));
            }

            if (video.getIs_like().equals("1")) {
                like1.setImageResource(R.drawable.liked);
                //likeNumber.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.liked,0,0);
                like1.setTag(true);
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment) {
            backOnceAgain();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof WallpaperFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ActiveDevicesFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof WallpaperViewAllFragment) {
            super.onBackPressed();
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (framelayoututube.getVisibility() == View.VISIBLE) {

            Log.d("response", "framelayoututube.getVisibility()");
            framelayoututube.setVisibility(View.GONE);
            myoutubePlayer.pause();
            utubePlayer.onStop();
            //myoutubePlayer.release();
            //utubePlayer.onDestroyView();
        } else if (frameLayout.getVisibility() == View.VISIBLE) {
            if (RotationFull) {

                Log.d("response", "RotationFull");
                RotationFull = false;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 5;
                headerrel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600));
                swipeRefreshLayout.setVisibility(View.VISIBLE);
            } else {
                Log.d("response", "!RotationFull");
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //onBackPressed();
            }
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ViewAllVideosFragment) {
            super.onBackPressed();
//        }
//        else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeViewAllFrag) {
//            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ProfileOtpFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SetOtpFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NotificationFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruDetailsFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchBhajanFrag) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchVideoFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof LiveFragment) {
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanListByMenumaster) {
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideoListByMenuMaster) {
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PromoPremiumFragment) {
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PremiumEpisodeFragment) {

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStackImmediate();
            } else {
                super.onBackPressed();
            }
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PremiumCategoryFragment) {
            changeTabColour(premium_layout);
            changeBottomTabColor(premiumLayout);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new PremiumSeriesFragment())
                    .commit();

            if (!toolbar.isShown()) {
                toolbar.setVisibility(View.VISIBLE);
            }
            if (!bottomNavigationView.isShown()) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PremiumSeriesFragment) {
            backOnceAgain();

            /*if (!toolbar.isShown()) {
                toolbar.setVisibility(View.VISIBLE);
            }*/
            toolbar.setVisibility(View.GONE);

            if (!bottomNavigationView.isShown()) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof Layer3Fragment) {

            if (isInFullScreen) {
                Layer3Fragment layer3Fragment = (Layer3Fragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home);
                layer3Fragment.changeOrientation();
            } else super.onBackPressed();

            /*if (!toolbar.isShown()) {
                toolbar.setVisibility(View.VISIBLE);
            }*/
            toolbar.setVisibility(View.GONE);
            if (!bottomNavigationView.isShown()) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PaymentFragment) {
            /*changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new Vi())
                    .commit();*/
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchHomeFrag) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchSankirtanFrag) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ProfileFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsFragment) {
            backOnceAgain();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruListFragmentNew) {
            //backOnceAgain();
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajansCategoryFragment) {
            backOnceAgain();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanViewAllFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanPlayFragment) {
            // getSupportFragmentManager().popBackStack("BhajanPlayFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.d("response", "BhajanPlayFragment");
            Constants.CALL_RESUME = "true";
            Constants.IS_PLAYING_ONPLAY = "false";
            Constants.IS_PAUSEDFROMHOME = "false";

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStackImmediate();
            } else super.onBackPressed();

            if (!toolbar.isShown()) {
                toolbar.setVisibility(View.VISIBLE);
            }
            if (!bottomNavigationView.isShown()) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
            //((BhajanPlayFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).killMediaPlayer();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ViewCommentsFragment) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsDetailFrag) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsDetailFragRecent) {
            super.onBackPressed();
        } else if (
                getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SankirtanListFrag ||
                        getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PlayListFrag ||
                        getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof DownloadsFragment ||
                        getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeViewAllFrag
        ) {
            if (frameLayout.getVisibility() == View.VISIBLE) {
                //youtubeLayout.minimize();
                frameLayout.setVisibility(View.GONE);
                relative_l1.setVisibility(View.GONE);
               /* playerView.stopPlayback();
                playerView2.stopPlayback();*/
            } else if (relative_l1.getVisibility() == View.VISIBLE) {
                frameLayout.setVisibility(View.GONE);
                relative_l1.setVisibility(View.GONE);
                /*playerView.stopPlayback();
                playerView2.stopPlayback();*/
            }
            changeTabColour(homeLay);
            changeBottomTabColor(homeBottomLay);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new HomeFragment1())
                    .commit();
        } else if ((getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment)) {
            backOnceAgain();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof Related_Bhajan_expandable) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SubscriptionFragment) {
            /*getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("PROFILE_FRAGMENT")
                    .replace(R.id.container_layout_home, new ProfileFragment())
                    .commit();*/
            super.onBackPressed();
        } else {
            backOnceAgain();
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_layout_home);

        if (resultCode == Activity.RESULT_OK) {
            isLike = data.getBooleanExtra("data", true);
            if (f instanceof ProfileFragment) {
                f.onActivityResult(requestCode, resultCode, data);
            } else if (f instanceof HomeFragment1) {
                if (requestCode == 121) {
                    if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                        if (isLike) {
                            onClick(homeLay);
                        }
                    }
                }
            } else if (f instanceof SankirtanListFrag) {
                if (requestCode == 121) {
                    if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                        if (isLike) {
                            onClick(sankirtanLay);
                        }
                    }
                }
            } else if (f instanceof ViewAllVideosFragment) {
                if (requestCode == 121) {
                    if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                        if (isLike) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container_layout_home, new ViewAllVideosFragment())
                                    .commit();
                        }
                    }
                }
            } else if (f instanceof VideosParentFragment) {
                if (requestCode == 121) {
                    if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                        if (isLike) {
                            onClick(videoLay);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (Utils.isConnectingToInternet(HomeActivityy.this)) {
            if (id == R.id.bhajan_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);

                }

                changeTabColour(view);

                Fragment fragment = new BhajansCategoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (id == R.id.guru_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                changeBottomTabColor(homeBottomLay);
                Bundle bundle = new Bundle();
                bundle.putString("type", "guru");
                Fragment fragment = new GuruListFragmentNew();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();

            } else if (id == R.id.sankirtan_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                changeBottomTabColor(homeBottomLay);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, new SankirtanListFrag())
                        .commit();

            } else if (id == R.id.video_lay) {

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    // youtubeLayout.minimize();
                }

                changeTabColour(view);
                changeBottomTabColor(videoBottomLay);
                Fragment fragment = new VideosParentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();

            } else if (id == R.id.homeLay) {

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    // youtubeLayout.minimize();
                }
                changeTabColour(view);
                changeBottomTabColor(homeBottomLay);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, new HomeFragment1())
                        .commit();
            } else if (id == R.id.ll_share_link) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    //frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                bitmap = BitmapFactory.decodeResource(HomeActivityy.this.getResources(), R.drawable.ic_launcher_background);

                String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
                //Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/mipmap/" + "ic_launcher_round");
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                //i.setType("text/plain");
                if (bitmap != null) {
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getImageUri(HomeActivityy.this, bitmap));
                }

                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);

                if (i.resolveActivity(HomeActivityy.this.getPackageManager()) != null) {
                    this.startActivity(i);
                }

            } else if (id == R.id.terms_conditions_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
//                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                Bundle bundle = new Bundle();
                bundle.putString("privacyOrTerms", "terms");
                Fragment fragment = new TNCFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();

            } else if (id == R.id.privacy_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
//                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                Bundle bundle = new Bundle();
                bundle.putString("privacyOrTerms", "privacy");
                Fragment fragment = new TNCFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();
            } else if (id == R.id.notification_lay) {
                if (notification_lay.isChecked()) {
                    notification_type = "on";

                    networkCall.NetworkAPICall(API.NOTIFICATION_STATUS, false);
                    Toast.makeText(this, "notification ON", Toast.LENGTH_SHORT).show();
/*
                    SharedPreference.getInstance(context).putString(Constants.NOTIFICATION_STATUS, notification_counter );
*/
                } else {
                    notification_type = "off";

                    networkCall.NetworkAPICall(API.NOTIFICATION_STATUS, false);
                    Toast.makeText(this, "notification Off", Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.news_article_lay) {

                searchView.setVisibility(View.GONE);
                searchParent.setVisibility(View.GONE);
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);

                } else if (relative_l1.getVisibility() == View.VISIBLE) {

//                    frameLayout.setVisibility(View.GONE);

                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                changeBottomTabColor(guruBottomLay);
                Fragment fragment = new NewsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            } else if (id == R.id.my_play_list_lay) {

                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
//                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);
                Fragment fragment = new PlayListFrag();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            } else if (id == R.id.go_live_lay) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {

//                    frameLayout.setVisibility(View.GONE);

                    relative_l1.setVisibility(View.GONE);
                }
                SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                        SignupResponse.class);

                if (signupResponse.getGo_live().equals("1")) {
                    goLiveLay.setVisibility(View.VISIBLE);
                    changeTabColour(view);
                    Intent goLiveIntent = new Intent(this, GoLiveActivity.class);
                    startActivity(goLiveIntent);
                } else {
                    goLiveLay.setVisibility(View.GONE);
                }
            } else if (id == R.id.home_bottom_nav) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    //   youtubeLayout.minimize();
                }
                changeBottomTabColor(view);
                changeTabColour(homeLay);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, new HomeFragment1())
                        .commit();
            } else if (id == R.id.app_logo) {
                showHomeFragment();
            } else if (id == R.id.share_watch_video) {
                shareTextUrl();
            } else if (id == R.id.share_watch_video1) {
                shareTextUrl();
            } else if (id == R.id.like_video_watch_video) {
                if (isConnectingToInternet(this)) {
                    likeyoutube = false;
                    if (!Boolean.parseBoolean(like.getTag().toString())) {
                        networkCall.NetworkAPICall(API.LIKE_VIDEO, false);
                    } else {
                        networkCall.NetworkAPICall(API.UNLIKE_VIDEO, false);
                    }
                } else {
                    //swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
            } else if (id == R.id.like_video_watch_video1) {
                if (isConnectingToInternet(this)) {
                    likeyoutube = true;
                    if (!Boolean.parseBoolean(like1.getTag().toString())) {
                        networkCall.NetworkAPICall(API.LIKE_VIDEO, false);
                    } else {
                        networkCall.NetworkAPICall(API.UNLIKE_VIDEO, false);
                    }
                } else {
                    //swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
            } else if (id == R.id.watch_video_comment_post_tv) {
                postComment.setVisibility(View.GONE);
                commentProgress.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(this);
                networkCall.NetworkAPICall(API.POST_COMMENT, false);

            } else if (id == R.id.fragment_watch_video_tv_view_all_comments) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.VIDEO_ID, video.getId());
                bundle.putSerializable("video", video);
                Fragment fragment = new ViewCommentsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("VIEW_COMMENT")
                        .commit();

            } else if (id == R.id.download) {
                download.setClickable(false);
                if (!file.exists()) {
                    file.delete();
                    circularProgressBar.setVisibility(View.VISIBLE);
                    beginDownload();

                } else {
                    download.setClickable(true);
                    Toast.makeText(HomeActivityy.this, "File Already Exists", Toast.LENGTH_SHORT).cancel();
                }
            } else if (id == R.id.video_bottom_nav) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    //   youtubeLayout.minimize();
                }
                if (!searchIv.isIconified()) {
                    searchIv.setIconified(true);
                    searchIv.onActionViewCollapsed();
                }

                app_logo.setVisibility(View.VISIBLE);
                tvImg.setVisibility(View.GONE);
                profile_iv.setVisibility(View.VISIBLE);
                changeBottomTabColor(view);
                searchContent = "";
                changeTabColour(videoLay);
                Fragment fragment = new VideosParentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();

            } else if (id == R.id.bhajan_bottom_nav) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                  /*  playerView.stopPlayback();
                    playerView2.stopPlayback();*/
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                /*    playerView.stopPlayback();
                    playerView2.stopPlayback();*/
                    relative_l1.setVisibility(View.GONE);
                }

                if (!searchIv.isIconified()) {
                    searchIv.setIconified(true);
                    searchIv.onActionViewCollapsed();
                }
                app_logo.setVisibility(View.VISIBLE);
                tvImg.setVisibility(View.GONE);
                profile_iv.setVisibility(View.VISIBLE);


                changeBottomTabColor(view);
                changeTabColour(bhajanLay);
                Fragment fragment = new BhajansCategoryFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (id == R.id.guru_bottom_nav) {

                searchView.setVisibility(View.GONE);
                searchParent.setVisibility(View.GONE);

                if (!searchIv.isIconified()) {
                    searchIv.setIconified(true);
                    searchIv.onActionViewCollapsed();
                }


                app_logo.setVisibility(View.VISIBLE);
                tvImg.setVisibility(View.GONE);
                profile_iv.setVisibility(View.VISIBLE);
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
//                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }
                changeTabColour(view);

                changeBottomTabColor(view);
                changeTabColour(newsAndArticleLay);
                Bundle bundle = new Bundle();

                Fragment fragment = new NewsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_layout_home, fragment)
                        .commit();

            } else if (id == R.id.article_bottom_nav) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    relative_l1.setVisibility(View.GONE);
                }
                changeBottomTabColor(view);
                changeTabColour(goLiveLay);
                Bundle bundle = new Bundle();
                bundle.putString("type", "goLive");
                Fragment fragment = new LiveGuruActivity();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();
            } else if (id == R.id.nav_header) {
            } else if (id == R.id.back_iv) {
                onBackPressed();
            } else if (id == R.id.profile_iv) {
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                } else if (relative_l1.getVisibility() == View.VISIBLE) {
                    frameLayout.setVisibility(View.GONE);
                    relative_l1.setVisibility(View.GONE);
                }

                if (!fromGuest) {
                    showAccountFrag();
                } else {
                    Intent intent = new Intent(this, LoginSignupActivity.class);
                    intent.putExtra(Constants.FROM_GUEST, "1");
                    SharedPreference.getInstance(this).putBoolean(Constants.FROM_GUEST, false);
                    (HomeActivityy.this).finish();
                    startActivity(intent);
                }


               /* if (!SharedPreference.getInstance(this).getBoolean(Constants.FROM_GUEST)) {
                    showAccountFrag();
                } else {
                    Intent intent = new Intent(this, LoginSignupActivity.class);
                    intent.putExtra(Constants.FROM_GUEST, "1");
                    SharedPreference.getInstance(this).putBoolean(Constants.FROM_GUEST, false);
                    (HomeActivityy.this).finish();
                    startActivity(intent);
                }*/
            } else if (id == R.id.more_options_toolbar3) {
                showAddPlayListDialog();
            } else if (id == R.id.notification_iv) {

                if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeFragment1) {
                    ((HomeFragment1) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getNotificationFrag(0);
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajansCategoryFragment) {
                    ((BhajansCategoryFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getNotificationFrag();
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof LiveFragment)
                    ((LiveFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getNotificationFrag();

            } else if (id == R.id.tv_iv) {
                shareapplink();
            } else if (id == R.id.search_parent) {
                initSearchView();
            } else if (id == R.id.cancel_button) {
                searchText.setVisibility(View.VISIBLE);
                searchImg.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(searchContent) && searchContent != null) {
                    searchContent = "";
                    if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeFragment1) {
                        ((HomeFragment1) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).fetchData();
                    } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SankirtanListFrag) {
                        ((SankirtanListFrag) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSankirtanList();
                    } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruListFragmentNew) {
                        ((GuruListFragmentNew) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).fetchData(true);
                    } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment) {
                        ((VideosParentFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getCategoryList();
                    }
                }
                searchView.setActivated(false);
                searchView.setQuery("", false);
                closeSearchIV.setVisibility(View.GONE);
                searchView.onActionViewCollapsed();
                searchView.clearFocus();
                Utils.closeKeyboard(this);
            }
        } else {
            ToastUtil.showDialogBox(HomeActivityy.this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void share_app_link() {

    }

    private void showAddPlayListDialog() {
        new NotificationDialog(this, ((BhajanPlayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_layout_home))).showPopup();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews() {
        // frag_type = Constants.Notification;
        premiumLayout = findViewById(R.id.premiumLayout);
        premiumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPremiumSeriesFrag();
            }
        });
        if (getIntent().getExtras() != null) {
            frag_type = getIntent().getExtras().getString(Constants.FRAG_TYPE);
        }
        toolbar = findViewById(R.id.toolbar);
        notifyTV = findViewById(R.id.notifyTV);

        circularProgressBar = findViewById(R.id.circularProgressBar);
        notification_lay = findViewById(R.id.notification_lay);
        notify_ll = findViewById(R.id.notify_ll);
        name = findViewById(R.id.name_watch_video);
        downloadwatch_video = findViewById(R.id.downloadwatch_video);
        description = findViewById(R.id.description_watch_video);
        date = findViewById(R.id.date_watch_video);
        back = findViewById(R.id.back);
        share = findViewById(R.id.share_watch_video);
        viewNumber = findViewById(R.id.views_number_watch_video);
        like = findViewById(R.id.like_video_watch_video);
        like.setTag(false);
        headerrel = findViewById(R.id.headerrel);
        likeNumber = findViewById(R.id.like_number_watch_video);
        nonFullScreenLayout = findViewById(R.id.watch_video_non_full_screen);
        ll_share_link = findViewById(R.id.ll_share_link);
        name1 = findViewById(R.id.name_watch_video1);
        downloadwatch_video1 = findViewById(R.id.downloadwatch_video1);
        description1 = findViewById(R.id.description_watch_video1);
        date1 = findViewById(R.id.date_watch_video1);
        share1 = findViewById(R.id.share_watch_video1);
        viewNumber1 = findViewById(R.id.views_number_watch_video1);
        like1 = findViewById(R.id.like_video_watch_video1);
        like1.setTag(false);
        likeNumber1 = findViewById(R.id.like_number_watch_video1);
        nonFullScreenLayout1 = findViewById(R.id.watch_video_non_full_screen1);
        refresh = findViewById(R.id.refresh);

        searchIv = findViewById(R.id.searchIv);
        searchCommon = findViewById(R.id.searchCommon);


        premium_layout = findViewById(R.id.premium_layout);
        initSearchView();

        floatingbutton = findViewById(R.id.floatingbutton);
        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton1 = findViewById(R.id.floatingActionButton1);

        live_channel = findViewById(R.id.live_channel);
        blank_space_1 = findViewById(R.id.blank_space_1);
        blank_space_2 = findViewById(R.id.blank_space_2);
        qrCode = findViewById(R.id.qrCode);
        tv_guide_layout = findViewById(R.id.tv_guide_layout);
        tv_guide_layout.setOnClickListener(v -> showLiveTvActivityFrag());

        qrCode.setOnClickListener(v -> startActivity(new Intent(HomeActivityy.this, ScannerView.class)));

//

        // InitSearchView();
        app_logo = findViewById(R.id.app_logo);
        progressvideo = findViewById(R.id.progressvideo);
        progressvideo1 = findViewById(R.id.progressvideo1);

        progressvideoutube = findViewById(R.id.progressvideoyoutube);
        progressvideoutube1 = findViewById(R.id.progressvideoutube2);
        utubePlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.playerViewYoutube);
        utubePlayer2 = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.playerViewyoutube2);

        utubePlayer.initialize(getResources().getString(R.string.api_keys_usable_gcp), this);

        circularProgressBar.setProgressMax(200f);

        // Set ProgressBar Color
        circularProgressBar.setProgressBarColor(getResources().getColor(R.color.download_image));

        circularProgressBar.setProgressBarWidth(7f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP

        noDataFound = findViewById(R.id.no_data_found_watvh_video);
        noDataFound1 = findViewById(R.id.no_data_found_watvh_video1);

        wallpaper_rl = findViewById(R.id.wallpaper_rl);

        wallpaper_rl.setOnClickListener(view -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Bundle bundle = new Bundle();
            bundle.putString("id", signupResponse.getId());
            bundle.putString("activity", "true");
            Fragment fragment = new ActiveDevicesFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        });


        /*searchCommon.setOnClickListener(view -> {
            Fragment fragment = new CommonSearchFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, fragment)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
        });*/

        //     leftTextView=findViewById(R.id.leftTextView);

        container1 = findViewById(R.id.container);
        download = findViewById(R.id.download);
        profile_iv = findViewById(R.id.profile_iv);
        tv_iv = findViewById(R.id.tv_iv);
        tv_iv.setVisibility(View.GONE);
        ll_share_link.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        share.setOnClickListener(this);
        like.setOnClickListener(this);
        notification_lay.setOnClickListener(this);
        like1.setOnClickListener(this);
        share1.setOnClickListener(this);
        download.setOnClickListener(this);

        /* appLogo.setOnClickListener(this);*/

        tv_iv.setOnClickListener(this);

        //    notification_lay.setChecked(true);

        if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("0")) {
            notification_lay.setChecked(true);
        } else if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("1")) {
            notification_lay.setChecked(false);
        }

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        relatedVideosList = findViewById(R.id.related_video_recycler_view_watch_video);
        relatedVideosList1 = findViewById(R.id.related_video_recycler_view_watch_video1);
        //  relatedVideosList.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(this);
        layoutManager1 = new LinearLayoutManager(this);
        relatedVideosList.setLayoutManager(layoutManager);
        relatedVideosList1.setLayoutManager(layoutManager1);

        videoResponses = new ArrayList<>();

        youtubeLayout = (LinearLayout) findViewById(R.id.dragLayout);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        framelayoututube = (RelativeLayout) findViewById(R.id.framelayoututube);
        relative_l1 = findViewById(R.id.relative_l1);

        headerrelyou = findViewById(R.id.header1);
        relativeyou_l1 = findViewById(R.id.relative_l2);

        //layoutHeader = (LinearLayout) findViewById(R.id.header);

        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        container = findViewById(R.id.container_layout_home);
        drawer = findViewById(R.id.drawer_layout);
        homeLay = findViewById(R.id.homeLay);
        videoLay = findViewById(R.id.video_lay);
        guruLay = findViewById(R.id.guru_lay);
        sankirtanLay = findViewById(R.id.sankirtan_lay);
        bhajanLay = findViewById(R.id.bhajan_lay);
        newsAndArticleLay = findViewById(R.id.news_article_lay);
        goLiveLay = findViewById(R.id.go_live_lay);
        homeBottomLay = findViewById(R.id.home_bottom_nav);
        videoBottomLay = findViewById(R.id.video_bottom_nav);
        bhajanBottomLay = findViewById(R.id.bhajan_bottom_nav);
        guruBottomLay = findViewById(R.id.guru_bottom_nav);
        articleBottomLay = findViewById(R.id.article_bottom_nav);
        myPlaylistLay = findViewById(R.id.my_play_list_lay);
        termsAndConditionsLay = findViewById(R.id.terms_conditions_lay);
        privacyLay = findViewById(R.id.privacy_lay);
        my_downloads_lay = findViewById(R.id.my_downloads_lay);
        editTV = findViewById(R.id.edit_profile);
        set_pin = findViewById(R.id.set_pin);
        saveTV = findViewById(R.id.save_profile);
        cancle_noti = findViewById(R.id.cancle_noti);
        clear_notification = findViewById(R.id.clear_notification);

        backIV = findViewById(R.id.back_iv);

        playerlayout = findViewById(R.id.playerlayout);
        backward = findViewById(R.id.backward);
        forward = findViewById(R.id.forwad);
        playpause = findViewById(R.id.playpauseview);
        img_cancel = findViewById(R.id.img_cancel);
        thumbaudio = findViewById(R.id.thumbaudio);
        titleaudio = findViewById(R.id.titleaudio);
        descriptionaudio = findViewById(R.id.descriptionaudio);

        playerlayout1 = findViewById(R.id.playerlayout1);
        playpause1 = findViewById(R.id.playpauseview1);
        img_cancel1 = findViewById(R.id.img_cancel1);
        thumbaudio1 = findViewById(R.id.thumbaudio1);
        titleaudio1 = findViewById(R.id.titleaudio1);
        backward1 = findViewById(R.id.backward1);
        forward1 = findViewById(R.id.forward1);
        descriptionaudio1 = findViewById(R.id.descriptionaudio1);

        playerlayout2 = findViewById(R.id.playerlayout2);
        playpause2 = findViewById(R.id.playpauseview2);
        backward2 = findViewById(R.id.backward2);
        forward2 = findViewById(R.id.forward2);
        img_cancel2 = findViewById(R.id.img_cancel2);
        thumbaudio2 = findViewById(R.id.thumbaudio2);
        titleaudio2 = findViewById(R.id.titleaudio2);
        descriptionaudio2 = findViewById(R.id.descriptionaudio2);

        playerlayout3 = findViewById(R.id.playerlayout3);
        playpause3 = findViewById(R.id.playpauseview3);
        backward3 = findViewById(R.id.backward3);
        forward3 = findViewById(R.id.forward3);
        img_cancel3 = findViewById(R.id.img_cancel3);
        thumbaudio3 = findViewById(R.id.thumbaudio3);
        titleaudio3 = findViewById(R.id.titleaudio3);
        descriptionaudio3 = findViewById(R.id.descriptionaudio3);

        my_downloads_lay.setOnClickListener(view -> {
            changeTabColour(view);
            drawer.closeDrawers();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_layout_home, new DownloadsFragment())
                    .commit();
        });

        img_cancel.setOnClickListener(view -> {
            if (AudioPlayerService.mediaPlayer != null) {
                Constants.IS_PAUSEDFROMHOME = "true";
                AudioPlayerService.mediaPlayer.stop();
                AudioPlayerService.mediaPlayer.reset();
                AudioPlayerService.mediaSession.release();
                AudioPlayerService.mediaSessionManager = null;
                // AudioPlayerService.mediaPlayer.release();
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
            }
            playerlayout.setVisibility(View.GONE);
            Constants.CALL_RESUME = "false";
        });


        playpause.setOnClickListener(view -> {
            if (!AudioPlayerService.mediaPlayer.isPlaying()) {
                AudioPlayerService.mediaPlayer.start();
                playpause.setImageResource(R.mipmap.audio_pause);

                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                Constants.IS_PLAYING = "true";
                intent.putExtra("isplaying", "true");
                sendBroadcast(intent);
            } else {
                AudioPlayerService.mediaPlayer.pause();
                playpause.setImageResource(R.mipmap.audio_play);

                Constants.IS_PLAYING = "false";
                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                intent.putExtra("isplaying", "false");
                sendBroadcast(intent);
            }

        });

        forward.setOnClickListener(view -> {
            Constants.CLICK_ACTIVE_FORWARD = "true";
            //   isImgBTnClick = true;
            forwardBhajanSong();
        });

        backward.setOnClickListener(view -> {
            Constants.CLICK_ACTIVE_REWIND = "true";
            backwardSong();
        });


        //searchImg = findViewById(R.id.search_image);
        notificationImg = findViewById(R.id.notification_iv);
        accountImg = findViewById(R.id.profile_iv);
        tvImg = findViewById(R.id.tv_iv);
        ivaddPlayList = findViewById(R.id.more_options_toolbar3);
        //logoutLay = findViewById(R.id.logoutLay);
        //profileLayout = findViewById(R.id.relative_layout_profile);
        navHeader = findViewById(R.id.nav_header);
        navName = navHeader.findViewById(R.id.nav_Name);
        navEmail = navHeader.findViewById(R.id.nav_email);
        profileImage = navHeader.findViewById(R.id.nav_profile_image);

        app_logo.setOnClickListener(this);
        NavigationView navigationView = findViewById(R.id.nav_view);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        searchParent = findViewById(R.id.search_parent);
        searchView = findViewById(R.id.home_search_view);
        searchText = findViewById(R.id.text_search);
        closeSearchIV = findViewById(R.id.cancel_button);
        searchImg = findViewById(R.id.search_iv);

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        // toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        navigationView.setVerticalScrollBarEnabled(false);


        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        if (!fromGuest) {
            if (TextUtils.isEmpty(signupResponse.getUsername())) {
                navEmail.setText(signupResponse.getMobile());
            } else {
                navEmail.setText(signupResponse.getUsername());
            }
        } else {
            navEmail.setText("");
        }

        if (!TextUtils.isEmpty(signupResponse.getProfile_picture())) {
            Ion.with(this).load(signupResponse.getProfile_picture()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (e == null) {
                        profileImage.setImageBitmap(result);
                    }
                }
            });
        } else {
            profileImage.setImageResource(R.mipmap.thumbnail_placeholder);
        }


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fromGuest) {
                    showAccountFrag();
                } else {
                    Intent intent = new Intent(HomeActivityy.this, LoginSignupActivity.class);
                    intent.putExtra(Constants.FROM_GUEST, "1");
                    (HomeActivityy.this).finish();
                    startActivity(intent);
                }
            }
        });

//        profileImage.setOnClickListener(v -> showAccountFrag());

        bannersList = new ArrayList<>();
        videoResponses = new ArrayList<>();
        videoFragmentVideoesCategoryList = new ArrayList<>();
        videoFragmentVideosList = new HashMap<>();
        bhajanResponseArrayList = new ArrayList<>();
        guruList = new ArrayList<>();
        newsList = new ArrayList<>();
        homeChannelList = new ArrayList<>();
        channellist = new ArrayList<>();
        homeVideosList = new ArrayList<>();
        bhajanList = new ArrayList<>();
        sankirtanBeanList = new ArrayList<>();

        homeVideoList = new ArrayList<>();

        homeGuruList = new ArrayList<>();
        homeNewsList = new ArrayList<>();
        homeChannelList = new ArrayList<>();
        seasonsList = new ArrayList<>();
        //navHeader.setOnClickListener(this);
        homeLay.setOnClickListener(this);
        videoLay.setOnClickListener(this);
        guruLay.setOnClickListener(this);
        sankirtanLay.setOnClickListener(this);
        bhajanLay.setOnClickListener(this);
        newsAndArticleLay.setOnClickListener(this);
        goLiveLay.setOnClickListener(this);
        myPlaylistLay.setOnClickListener(this);
        termsAndConditionsLay.setOnClickListener(this);
        privacyLay.setOnClickListener(this);
        //logoutLay.setOnClickListener(this);

        homeBottomLay.setOnClickListener(this);
        videoBottomLay.setOnClickListener(this);
        bhajanBottomLay.setOnClickListener(this);
        guruBottomLay.setOnClickListener(this);
        articleBottomLay.setOnClickListener(this);

        notificationImg.setOnClickListener(this);
        accountImg.setOnClickListener(this);
        tvImg.setOnClickListener(this);
        ivaddPlayList.setOnClickListener(this);
        searchParent.setOnClickListener(this);
        closeSearchIV.setOnClickListener(this);

        premium_layout.setOnClickListener(v -> showPremiumSeriesFrag());


        backIV.setOnClickListener(this);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

                SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                        SignupResponse.class);

                String isGoLive;
                isGoLive = signupResponse.getGo_live();


                if (isGoLive != null) {
                    if (isGoLive.equals("1")) {
                        goLiveLay.setVisibility(View.VISIBLE);
                    } else if (isGoLive.equals("0")) {
                        goLiveLay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // networkCall.NetworkAPICall(API.GET_BHAJANS, true);


        switch (HomeActivityy.this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                searchCommon.setImageResource(R.drawable.ic_baseline_search_white);
//                tv_iv.setImageResource(R.drawable.ic_baseline_share_26);
                notificationImg.setImageResource(R.drawable.ic_notifications_white);
                profile_iv.setImageResource(R.drawable.ic_baseline_person_white);
                backIV.setImageResource(R.drawable.back_arrow_white);
                break;

            case Configuration.UI_MODE_NIGHT_NO:

                searchCommon.setImageResource((R.drawable.ic_search_black));
//                tv_iv.setImageResource(R.drawable.ic_baseline_share_27);
                notificationImg.setImageResource(R.drawable.ic_baseline_notifications_24);
                profile_iv.setImageResource(R.drawable.ic_baseline_person_24);
                backIV.setImageResource(R.drawable.ic_left_black);
                break;

        }


    }

    private void showLiveTvActivityFrag() {
        if (video_url == null) {
            Toast.makeText(context, "Please Select any Live Channel", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(context, LiveStreamJWActivity.class);
            intent.putExtra("livevideourl", video_url);
            intent.putExtra("channel_id", video_id);
            intent.putExtra("channel_position", String.valueOf(channel_position));
            intent.putExtra("channel_name", channel_name);
            intent.putExtra("from", "livetv");
            intent.putExtra("VIEWlike", view_like);
            intent.putExtra("Is_like_no", is_like_no);
            context.startActivity(intent);
        }
    }

    private void backwardSong() {
        if (Constants.AUDIO_ACTIVE_LIST.equals("bhajan")) {

            audioplaymainpositionindex = PreferencesHelper.getInstance().getIntValue("audioplayindex", 0);

            currentplayingbhajan = bhajanResponseArrayList.get(audioplaymainpositionindex).getBhajan();

            //if ((currentplayingbhajan.length >) > Constants.AUDIO_INDEX) {
            if (Constants.AUDIO_INDEX > 0) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX - 1;
            } else {
                Constants.AUDIO_INDEX = 0;
            }
            forwardRewindPlaySongs();

            Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);

            Constants.REVERSE_SONG = "true";

            this.sendBroadcast(intent);

        } else if (Constants.AUDIO_ACTIVE_LIST.equals("bhajans")) {

            currentplayingbhajan = bhajanforImage;


            if (Constants.AUDIO_INDEX > 0) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX - 1;
            } else {
                Constants.AUDIO_INDEX = 0;
            }

            forwardRewindPlaySongs();

            Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
            // intent.putExtra("bhajanlist",bhajans);
            Constants.REVERSE_SONG = "true";

            //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
            this.sendBroadcast(intent);


        } else if (Constants.AUDIO_ACTIVE_LIST.equals("relatedbhajan")) {

            if (Constants.AUDIO_INDEX > 0) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX - 1;
            } else {
                Constants.AUDIO_INDEX = 0;
            }

            forwardRewindPlaySongs();

            Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);

            // intent.putExtra("bhajanlist",bhajans);

            Constants.REVERSE_SONG = "true";

            //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);

            this.sendBroadcast(intent);


        } else if (Constants.AUDIO_ACTIVE_LIST.equals("home")) {
            MenuMasterList[] bhajans = new MenuMasterList[homeBhajanListNew.size()];
            for (int i = 0; i < homeBhajanListNew.size(); i++) {
                bhajans[i] = homeBhajanListNew.get(i);
            }

            if (Constants.AUDIO_INDEX > 0) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX - 1;
            } else {
                Constants.AUDIO_INDEX = 0;
            }


            currentplayingbhajannext1 = bhajans[Constants.AUDIO_INDEX];

            Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
            // intent.putExtra("bhajanlist",bhajans);
            Constants.REVERSE_SONG = "true";

            //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
            this.sendBroadcast(intent);
        }


    }

    private void forwardBhajanSong() {
        if (Constants.AUDIO_ACTIVE_LIST.equals("bhajan")) {

            audioplaymainpositionindex = PreferencesHelper.getInstance().getIntValue("audioplayindex", 0);

            currentplayingbhajan = bhajanResponseArrayList.get(audioplaymainpositionindex).getBhajan();

            if ((currentplayingbhajan.length - 1) > Constants.AUDIO_INDEX) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                forwardRewindPlaySongs();

                Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                this.sendBroadcast(intent);
            }
        } else if (Constants.AUDIO_ACTIVE_LIST.equals("bhajans")) {

            currentplayingbhajan = bhajanforImage;

            if ((currentplayingbhajan.length - 1) > Constants.AUDIO_INDEX) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                forwardRewindPlaySongs();

                Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                this.sendBroadcast(intent);
            }

        } else if (Constants.AUDIO_ACTIVE_LIST.equals("relatedbhajan")) {
            currentplayingbhajan = relatedbhajan;

            if ((currentplayingbhajan.length - 1) > Constants.AUDIO_INDEX) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                forwardRewindPlaySongs();

                Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                this.sendBroadcast(intent);
            }
        } else if (Constants.AUDIO_ACTIVE_LIST.equals("home")) {
            MenuMasterList[] bhajans = new MenuMasterList[homeBhajanListNew.size()];
            for (int i = 0; i < homeBhajanListNew.size(); i++) {
                bhajans[i] = homeBhajanListNew.get(i);
            }
            if ((bhajans.length - 1) > Constants.AUDIO_INDEX) {

                Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                currentplayingbhajannext1 = bhajans[Constants.AUDIO_INDEX];

                //forwardRewindPlaySongs();

                Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                //intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                this.sendBroadcast(intent);
            }
        }


    }


    public void forwardRewindPlaySongs() {

        if (Constants.AUDIO_ACTIVE_LIST.equals("bhajan")) {
            currentplayingbhajannext = currentplayingbhajan[Constants.AUDIO_INDEX];

        } else if (Constants.AUDIO_ACTIVE_LIST.equals("bhajans")) {

            currentplayingbhajan = bhajanforImage;

            currentplayingbhajannext = currentplayingbhajan[Constants.AUDIO_INDEX];

        } else if (Constants.AUDIO_ACTIVE_LIST.equals("relatedbhajan")) {
            currentplayingbhajan = relatedbhajan;
            currentplayingbhajannext = currentplayingbhajan[Constants.AUDIO_INDEX];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.Version_hit = 0;
        try {
            if (AudioPlayerService.mediaPlayer != null) {
                AudioPlayerService.mediaPlayer.stop();
            }

            if (HomeFragment1.serviceBound) {
                unbindService(HomeFragment1.serviceConnection);
                HomeFragment1.player.stopSelf();
            } else if (BhajansCategoryFragment.serviceBound) {
                unbindService(BhajansCategoryFragment.serviceConnection);
                BhajansCategoryFragment.player.stopSelf();
            } else if (BhajanViewAllFragment.serviceBound) {
                unbindService(BhajanViewAllFragment.serviceConnection);
                BhajanViewAllFragment.player.stopSelf();
            } else if (DownloadsFragment.serviceBound) {
                unbindService(DownloadsFragment.serviceConnection);
                DownloadsFragment.player.stopSelf();
            } else if (PlayListFrag.serviceBound) {
                unbindService(PlayListFrag.serviceConnection);
                PlayListFrag.player.stopSelf();
            }

            unregisterReceiver(onDownloadComplete);
            unregisterReceiver(startVideoPlayer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    private void changeTabColour(View view) {
        switch (view.getId()) {
//                break;
            case R.id.bhajan_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();
                imgSelected(bhajanLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(premium_layout);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.premium_layout:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();


                imgSelected(premium_layout);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;


            case R.id.guru_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();


                imgSelected(guruLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(premium_layout);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.sankirtan_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();


                imgSelected(sankirtanLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(guruLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.my_downloads_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(my_downloads_lay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(premium_layout);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                break;

            case R.id.video_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();


                imgSelected(videoLay);
                imgTextUnSelected(homeLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.homeLay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(homeLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.terms_conditions_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(termsAndConditionsLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.privacy_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(privacyLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.channel_view_all:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                //imgSelected(guruLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

            case R.id.news_article_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();
                searchView.setVisibility(View.GONE);
                imgSelected(newsAndArticleLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.my_play_list_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();
                imgSelected(myPlaylistLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.nav_header:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(bhajanLay);
                //imgTextUnSelected(logoutLay);
                imgTextUnSelected(goLiveLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;

            case R.id.go_live_lay:

                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(goLiveLay);
                imgTextUnSelected(homeLay);
                imgTextUnSelected(videoLay);
                imgTextUnSelected(guruLay);
                imgTextUnSelected(sankirtanLay);
                imgTextUnSelected(premium_layout);
                imgTextUnSelected(bhajanLay);
                imgTextUnSelected(newsAndArticleLay);
                imgTextUnSelected(myPlaylistLay);
                imgTextUnSelected(termsAndConditionsLay);
                imgTextUnSelected(privacyLay);
                imgTextUnSelected(my_downloads_lay);

                break;
        }
    }

    private void imgTextUnSelected(LinearLayout layout) {

        ImageView img = (ImageView) layout.getChildAt(0);
        TextView txt = (TextView) layout.getChildAt(1);
        txt.setTextColor(getResources().getColor(R.color.drawer_inactive));

        switch (layout.getId()) {
//            case R.id.logoutLay:
//                img.setImageResource(R.mipmap.logout_inactive);
//                break;
            case R.id.my_downloads_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.drawable.ic_downloads);
                break;

            case R.id.bhajan_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.bhajan_inactive);
                break;
            case R.id.premiumLayout:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.yellow_crown);
                break;
            case R.id.premium_layout:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.premium_default_side);
                break;
            case R.id.guru_lay:
                closeSearchIV.setVisibility(View.GONE);

                img.setImageResource(R.mipmap.guru_inactive);
                break;
            case R.id.sankirtan_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.sankirtan_gray);
                break;
            case R.id.video_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.video_inactive);
                break;
            case R.id.homeLay:
                closeSearchIV.setVisibility(View.GONE);

                img.setImageResource(R.mipmap.home_disable);
                break;
            case R.id.terms_conditions_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.terms_conditions);
                break;
            case R.id.privacy_lay:
                closeSearchIV.setVisibility(View.GONE);

                img.setImageResource(R.mipmap.privacy_policy_inactive);
                break;
            case R.id.news_article_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.news_articles_inactive);
                break;
            case R.id.my_play_list_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.myplaylist_inactive);
                break;
            case R.id.go_live_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.golive_inactive);
                break;

            case R.id.home_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);

                img.setImageResource(R.mipmap.home_default);
                break;
            case R.id.video_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchIv.setFocusable(false);
                searchIv.clearFocus();

                img.setImageResource(R.mipmap.bottom_videos_default);
                break;
            case R.id.bhajan_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);

                img.setImageResource(R.mipmap.bottom_bhajan_default);
                break;
            case R.id.guru_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.bottom_default_news);
                break;
            case R.id.article_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                img.setImageResource(R.mipmap.live_inactive);
                break;
        }
    }

    private void imgSelected(LinearLayout layout) {

        ImageView img = (ImageView) layout.getChildAt(0);
        TextView txt = (TextView) layout.getChildAt(1);
        txt.setTextColor(getResources().getColor(R.color.colorAccent));

        switch (layout.getId()) {
            /*case R.id.logoutLay:
                img.setImageResource(R.mipmap.logout_active);
                break;*/
            case R.id.bhajan_lay:
                img.setImageResource(R.mipmap.bhajan_active);
                break;
            case R.id.premium_layout:
                img.setImageResource(R.mipmap.yellow_crown);
                break;
            case R.id.guru_lay:
                img.setImageResource(R.mipmap.guru_active);
                break;
            case R.id.sankirtan_lay:
                img.setImageResource(R.mipmap.sankirtan_active);
                break;
            case R.id.video_lay:
                img.setImageResource(R.mipmap.video_active);
                break;
            case R.id.homeLay:
                img.setImageResource(R.mipmap.home_active_yellow);
                break;
            case R.id.terms_conditions_lay:
                img.setImageResource(R.mipmap.terms_condition_active);
                break;
            case R.id.privacy_lay:
                img.setImageResource(R.mipmap.privacy_policy_active);
                break;
            case R.id.news_article_lay:
                img.setImageResource(R.mipmap.news_articles_active);
                break;
            case R.id.my_play_list_lay:
                img.setImageResource(R.mipmap.myplaylist_active);
                break;
            case R.id.go_live_lay:
                img.setImageResource(R.mipmap.golive_active);
                break;
            case R.id.home_bottom_nav:
                img.setImageResource(R.mipmap.home_active);
                break;
            case R.id.premiumLayout:
                img.setImageResource(R.mipmap.premium_active);
                break;
            case R.id.video_bottom_nav:
                img.setImageResource(R.mipmap.bottom_video_active);
                break;
            case R.id.bhajan_bottom_nav:
                img.setImageResource(R.mipmap.bottom_bhajan_active);
                break;
            case R.id.guru_bottom_nav:
                img.setImageResource(R.mipmap.bottom_news_active);
                break;
            case R.id.article_bottom_nav:
                img.setImageResource(R.mipmap.live_active);
                break;
        }
    }

    private void changeBottomTabColor(View view) {
        switch (view.getId()) {
            case R.id.home_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(homeBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.premiumLayout:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(premiumLayout);
                // imgSelected(premium_layout);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

           /* case R.id.premium_layout:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                //imgSelected(premiumLayout);
                imgSelected(premium_layout);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;*/

            case R.id.video_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchIv.setFocusable(false);
                searchIv.clearFocus();

                imgSelected(videoBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.bhajan_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(bhajanBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.guru_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();


                imgSelected(guruBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.article_bottom_nav:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgTextUnSelected(articleBottomLay);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgSelected(guruBottomLay);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(my_downloads_lay);
                break;

            case R.id.my_downloads_lay:
                closeSearchIV.setVisibility(View.GONE);
                searchView.setFocusable(false);
                searchView.clearFocus();

                imgSelected(my_downloads_lay);
                imgTextUnSelected(videoBottomLay);
                imgTextUnSelected(premiumLayout);
                //imgTextUnSelected(premium_layout);
                imgTextUnSelected(bhajanBottomLay);
                imgTextUnSelected(guruBottomLay);
                imgTextUnSelected(homeBottomLay);
                imgTextUnSelected(articleBottomLay);
                break;

        }
    }

    private void backOnceAgain() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            backPressed = System.currentTimeMillis();
            ToastUtil.showShortSnackBar(container, getResources().getString(R.string.pressAgain));
        }
    }

    public void logOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.app_name)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        List<Bhajan> bhajanList = new ArrayList<>();
                        PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION, false);
                        PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, new SignupResponse());
                        PreferencesHelper.getInstance().setValue(Constants.MY_PLAY_LIST, bhajanList);
                        Intent intent = new Intent(HomeActivityy.this, LoginSignupActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showTvData() {
        PopupWindow popupwindow_obj = popupDisplay();
        //popupwindow_obj.getContentView().
        popupwindow_obj.showAsDropDown(tvImg, -40, 18); // where u want show on view click event popupwindow.showAsDropDown(view, x, y);
    }

    public void handleToolbar() {
        if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeFragment1) {
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
//            searchCommon.setVisibility(View.VISIBLE);

            checksearchoption();

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }


            bottomNavigationView.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            //    tvImg.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            notificationImg.setVisibility(View.VISIBLE);
            if (SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER).equalsIgnoreCase("0")) {
                notify_ll.setVisibility(View.GONE);
            } else {
                notify_ll.setVisibility(View.GONE);
            }

            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);

            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.VISIBLE);

            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));

        }
        if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeFragment1) {
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();

            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }


            bottomNavigationView.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            // tvImg.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            notificationImg.setVisibility(View.GONE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.VISIBLE);
            if (SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER).equalsIgnoreCase("0")) {
                notify_ll.setVisibility(View.GONE);
            } else {
                notify_ll.setVisibility(View.GONE);
            }


            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));

        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchBhajanFrag) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchVideoFragment) {

            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SearchHomeFrag) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }

            searchParent.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ViewAllVideosFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }

            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.GONE);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof LiveGuruActivity) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();

            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.GONE);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanViewAllFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.INVISIBLE);
           /* notificationImg.setVisibility(View.VISIBLE);
            notify_ll.setVisibility(View.VISIBLE);*/

            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.VISIBLE);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajansCategoryFragment
                || getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruListFragmentNew) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);
            searchIv.setFocusable(false);
            searchIv.clearFocus();

            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.VISIBLE);
            /* notify_ll.setVisibility(View.VISIBLE);*/
            if (SharedPreference.getInstance(context).getString(Constants.NOTIFICATION_COUNTER).equalsIgnoreCase("0")) {
                notify_ll.setVisibility(View.GONE);
            } else {
                notify_ll.setVisibility(View.GONE);
            }
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.VISIBLE);

        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruListFragmentNew) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            checksearchoption();
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);
            searchIv.setFocusable(false);
            searchIv.clearFocus();

            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.VISIBLE);

        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruDetailsFragment) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);
            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            searchParent.setVisibility(View.GONE);
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsFragment
                || getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SankirtanListFrag) {
            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof DownloadsFragment) {

            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));

        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NotificationFragment) {
            // } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NotificationFragmentNew) {
            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchParent.setVisibility(View.GONE);
            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            toolbar.setVisibility(View.VISIBLE);
            // accountImg.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.GONE);
            tvImg.setVisibility(View.GONE);
            // notificationImg.setVisibility(View.INVISIBLE);
            notificationImg.setVisibility(View.GONE);
            // notify_ll.setVisibility(View.INVISIBLE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));

        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PlayListFrag) {
            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.VISIBLE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof TNCFragment
                || getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof LiveTvFragment
            /*|| getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsDetailFrag*/) {

            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.GONE);
            }
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            backIV.setVisibility(View.GONE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsDetailFrag) {
            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            //   backIV.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof NewsDetailFragRecent) {
            ivaddPlayList.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();

            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);

            //   backIV.setVisibility(View.VISIBLE);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanPlayFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.GONE);
            }
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            backIV.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
            drawer.addDrawerListener(toggle);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ProfileFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            editTV.setVisibility(View.VISIBLE);
            set_pin.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.GONE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.GONE);
            }

         /*   searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();*/
//            searchCommon.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            accountImg.setVisibility(View.GONE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof ProfileOtpFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            toolbar.setVisibility(View.GONE);
            accountImg.setVisibility(View.GONE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);


        } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SetOtpFragment) {
            bottomNavigationView.setVisibility(View.GONE);
            searchParent.setVisibility(View.GONE);
            searchIv.setVisibility(View.GONE);

            searchIv.setFocusable(false);
            searchIv.clearFocus();
            checksearchoption();


            toolbar.setVisibility(View.GONE);
            accountImg.setVisibility(View.GONE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            backIV.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);


        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            searchParent.setVisibility(View.GONE);
            if (!is_premium_active) {
                floatingbutton.setVisibility(View.VISIBLE);
            }

            searchIv.setFocusable(false);
            searchIv.clearFocus();

            checksearchoption();

            editTV.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
            saveTV.setVisibility(View.GONE);
            clear_notification.setVisibility(View.GONE);
            ivaddPlayList.setVisibility(View.GONE);
            accountImg.setVisibility(View.VISIBLE);
            tvImg.setVisibility(View.GONE);
            notificationImg.setVisibility(View.GONE);
            notify_ll.setVisibility(View.GONE);
            getSupportActionBar().hide();

            backIV.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            //toggle.syncState();
        }
    }

    public PopupWindow popupDisplay() {
        final PopupWindow popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_tv, null);

        LinearLayout layout = view.findViewById(R.id.parent_layout_channel);

        for (int i = 0; i < homeChannelList.size(); i++) {

            LinearLayout horizontalLayout = new LinearLayout(this);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            final CircleImageView imageView = new CircleImageView(this);
            AppTextView channelName = new AppTextView(this);

            Ion.with(this).load(homeChannelList.get(i).getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        imageView.setImageBitmap(result);
                    } else {
                        imageView.setImageResource(R.mipmap.thumbnail_placeholder);
                    }
                }
            });

            channelName.setText(homeChannelList.get(i).getName());
            horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
            imageView.setPadding(13, 13, 13, 13);
            channelName.setPadding(0, 0, 13, 0);
            //channelName.setGravity(View.TEXT_ALIGNMENT_CENTER);
            horizontalLayout.addView(imageView, 110, 110);
            horizontalLayout.addView(channelName);
            final int finalI = i;

            horizontalLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(getApplicationContext(), "under developement", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(HomeActivityy.this, LiveStreamJWActivity.class);
                    intent.putExtra("livevideourl", homeChannelList.get(finalI).getChannel_url());
                    intent.putExtra("channel_id", homeChannelList.get(finalI).getId());
                    intent.putExtra("from", "livetv");
                    startActivity(intent);

                }
            });
            layout.addView(horizontalLayout);
        }

        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

    public void showVideoFrag() {
        changeTabColour(videoLay);
        changeBottomTabColor(videoBottomLay);
        Fragment fragment = new VideosParentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();

    }

    public void showGuruFrag() {
        changeTabColour(guruLay);
        // changeBottomTabColor(guruBottomLay);
        closeSearchIV.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();

        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Bundle bundle = new Bundle();
        bundle.putString("type", "guru");
        Fragment fragment = new GuruListFragmentNew();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();
    }

    public void showLiveFrag() {
        //changeTabColour(guruLay);
        //changeBottomTabColor(guruBottomLay);
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Fragment fragment = new LiveFragment();
        // fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).commit();
    }

    public void showLVideoListFrag() {
        //changeTabColour(guruLay);
        //changeBottomTabColor(guruBottomLay);
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);


        Bundle bundle = new Bundle();
        bundle.putString("Id", Video_id);
        bundle.putString("Category", Category);
        bundle.putString("Category_name", Category_name);
        Fragment fragment = new VideoListByMenuMaster();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).addToBackStack("Video").commit();
    }

    public void showLBhajanListFrag() {
        //changeTabColour(guruLay);
        //changeBottomTabColor(guruBottomLay);
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);

       /* toolbar.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);*/

        Bundle bundle = new Bundle();
        bundle.putString("Id", Bhajan_id);
        bundle.putString("Category", Category);
        bundle.putString("Category_name", Category_name);
        Fragment fragment = new BhajanListByMenumaster();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout_home, fragment).addToBackStack("BHAJANS").commit();
    }


    public void showBhajanFrag() {
        changeTabColour(bhajanLay);

        Fragment fragment = new BhajansCategoryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }


    public void showHomeFragment() {
        changeTabColour(homeLay);
        closeSearchIV.setVisibility(View.GONE);
        searchView.setFocusable(false);

        imgSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Fragment fragment = new HomeFragment1();
        /* fragment.setArguments(bundle);*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }


    public void showNewsFrag() {
        changeTabColour(newsAndArticleLay);
        changeBottomTabColor(guruBottomLay);
        Fragment fragment = new NewsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showSeasonFrag() {
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);

        Fragment fragment = new SeasonFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showPromoPremiumFrag() {
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Bundle bundle = new Bundle();
        bundle.putString("Id", Video_id);
        bundle.putString("Category", Category);
        bundle.putString("Category_name", Category_name);
        bundle.putString("Premium_Cat_Id", Premium_Cat_Id);
        bundle.putString("Premium_Auth_Id", Premium_Auth_Id);
        Fragment fragment = new PromoPremiumFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showPromoPremiumCategory() {
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Bundle bundle = new Bundle();
        bundle.putString("Id", Video_id);
        bundle.putString("Category_name", Category_name);
        Fragment fragment = new PremiumCategoryFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showPremiumEpisodeFrag() {
        closeSearchIV.setVisibility(View.GONE);
        tv_iv.setVisibility(View.GONE);
        notificationImg.setVisibility(View.GONE);
        notify_ll.setVisibility(View.GONE);
        profile_iv.setVisibility(View.GONE);
        searchIv.setVisibility(View.GONE);
        searchView.setFocusable(false);
        searchView.clearFocus();
        imgTextUnSelected(bhajanLay);
        imgTextUnSelected(homeLay);
        imgTextUnSelected(videoLay);
        imgTextUnSelected(guruLay);
        imgTextUnSelected(sankirtanLay);
        //imgTextUnSelected(logoutLay);
        imgTextUnSelected(newsAndArticleLay);
        imgTextUnSelected(goLiveLay);
        imgTextUnSelected(myPlaylistLay);
        imgTextUnSelected(termsAndConditionsLay);
        imgTextUnSelected(privacyLay);
        imgTextUnSelected(my_downloads_lay);
        imgTextUnSelected(homeBottomLay);
        imgTextUnSelected(videoBottomLay);
        imgTextUnSelected(bhajanBottomLay);
        imgTextUnSelected(guruBottomLay);
        imgTextUnSelected(articleBottomLay);
        imgTextUnSelected(my_downloads_lay);
        Bundle bundle = new Bundle();
        bundle.putString("Id", Video_id);
        bundle.putString("Category", Category);
        bundle.putString("Category_name", Category_name);
        Fragment fragment = new PremiumEpisodeFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("episodes")
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showPremiumSeriesFrag() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        changeTabColour(premium_layout);
        changeBottomTabColor(premiumLayout);
        Fragment fragment = new PremiumSeriesFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showLiveTvFrag() {
        Fragment fragment = new LiveTvFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    public void showAccountFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("PROFILE_FRAGMENT")
                .replace(R.id.container_layout_home, new ProfileFragment())
                .commit();
    }

    public void showSubscriptionFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("SUBSCRIPTION_FRAGMENT")
                .replace(R.id.container_layout_home, new SubscriptionFragment())
                .commit();
    }

    public void showActiveDeviceFrag() {
        Bundle bundle = new Bundle();
        bundle.putString("id", signupResponse.getId());
        bundle.putString("activity", "true");
        Fragment fragment = new ActiveDevicesFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }


   /* public void showNotificationFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("NOTIFICATION_FRAG")
                .replace(R.id.container_layout_home, new NotificationFragment())
                .commit();
    }*/


    public void showNotificationFrag() {
        Fragment fragment = new NotificationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment).commit();
    }


    public void showExpandableData() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("EXPANDABLE")
                .replace(R.id.container_layout_home, new Related_Bhajan_expandable())
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
                .replace(R.id.container_layout_home, fragment)
                .commit();
    }

    @Override
    public void onFullscreen(boolean b) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (b) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        Log.d("ssss", "1");
        if(!fromGuest) {
            if (API_NAME.equals(API.RELATED_VIDEOS)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            } else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId());
            } else if (API_NAME.equals(API.POST_COMMENT)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("comment", comment.getText().toString());
            } else if (API_NAME.equals(API.GET_BHAJANS)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)

                        .setMultipartParameter("user_id", signupResponse.getId());
            } else if (API_NAME.equals(API.NOTIFICATION_STATUS)) {
                if (notification_type.equalsIgnoreCase("off")) {
                    ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                            .setMultipartParameter("user_id", signupResponse.getId())
                            .setMultipartParameter("notification_status", "1");
                } else if (notification_type.equalsIgnoreCase("on")) {
                    ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                            .setMultipartParameter("user_id", signupResponse.getId())
                            .setMultipartParameter("notification_status", "0");
                }
            } else if (API_NAME.equals(API.HOME_PAGE_VIDEOS)) {
                Log.d("ssss", "2");
                Log.d("ssss", signupResponse.getId());
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", signupResponse.getId());
                Log.d("ssss", "abc");
            } else if (API_NAME.equals(API.GET_URL_DATA)) {
                Log.d("shantanu", "called ");
                Log.d("shantanu", signupResponse.getId());
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", signupResponse.getId())
                        .setMultipartParameter("media_id", media_id)
                        .setMultipartParameter("data_type", dataType);
            }
        } else {
            if (API_NAME.equals(API.RELATED_VIDEOS)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage));
            } else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("video_id", video.getId());
            } else if (API_NAME.equals(API.POST_COMMENT)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("video_id", video.getId())
                        .setMultipartParameter("comment", comment.getText().toString());
            } else if (API_NAME.equals(API.GET_BHAJANS)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", "163");
            } else if (API_NAME.equals(API.NOTIFICATION_STATUS)) {
                if (notification_type.equalsIgnoreCase("off")) {
                    ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                            .setMultipartParameter("user_id", "163")
                            .setMultipartParameter("notification_status", "1");
                } else if (notification_type.equalsIgnoreCase("on")) {
                    ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                            .setMultipartParameter("user_id", "163")
                            .setMultipartParameter("notification_status", "0");
                }
            } else if (API_NAME.equals(API.HOME_PAGE_VIDEOS)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", signupResponse.getId());
                Log.d("ssss", "abc");
            } else if (API_NAME.equals(API.GET_URL_DATA)) {
                ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                        .setMultipartParameter("user_id", "163")
                        .setMultipartParameter("media_id", media_id)
                        .setMultipartParameter("data_type", dataType);
            }
        }


        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        // refresh.setRefreshing(false);
        Log.d("ssss", "abcde");
        if (result.optBoolean("status")) {
            Log.d("ssss", "abcd");
            if (apitype.equals(API.RELATED_VIDEOS)) {
                if (video.getYoutube_url().equals("")) {
                    nonFullScreenLayout.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = result.optJSONArray("data");
                    Log.e("watch_video", jsonArray.toString());
                    if (videoResponses.size() > 0)
                        videoResponses.clear();
                    if (jsonArray.length() > 0) {
                        relatedVideosList.setVisibility(View.VISIBLE);
                        noDataFound.setVisibility(View.GONE);
                        videos = new Videos[jsonArray.length()];
                        /*for (int i = 0; i < jsonArray.length(); i++) {
                            Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                            videos[i] = video;
                        }
                        if (this != null) {
                            adapter = new ViewAllAdapter(videos, this);
                            relatedVideosList.setAdapter(adapter);
                        }*/

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                            videos[i] = video;
                        }


                        Videos[] temp = new Videos[vd.length + jsonArray.length()];

                        for (int i = 0; i < vd.length; i++) {
                            temp[i] = vd[i];
                        }

                        int j = 0;
                        for (int i = vd.length; i < temp.length; i++) {


                            temp[i] = videos[j];

                            j++;

                        }
                        vd = temp;

                        adapter = new ViewAllAdapter(vd, this);
                        relatedVideosList.setAdapter(adapter);
                    } else {
                        relatedVideosList.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }


                } else {
                    nonFullScreenLayout1.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = result.optJSONArray("data");
                    Log.e("watch_video", jsonArray.toString());
                    if (videoResponses.size() > 0)
                        videoResponses.clear();
                    if (jsonArray.length() > 0) {
                        relatedVideosList1.setVisibility(View.VISIBLE);
                        noDataFound1.setVisibility(View.GONE);
                        videos = new Videos[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                            videos[i] = video;
                        }


                        Videos[] temp = new Videos[vd.length + jsonArray.length()];

                        for (int i = 0; i < vd.length; i++) {
                            temp[i] = vd[i];
                        }

                        int j = 0;
                        for (int i = vd.length; i < temp.length; i++) {


                            temp[i] = videos[j];

                            j++;

                        }
                        vd = temp;

                        adapter1 = new ViewAllAdapter(vd, this);
                        relatedVideosList1.setAdapter(adapter1);
                    } else {
                        relatedVideosList.setVisibility(View.GONE);
                        noDataFound.setVisibility(View.VISIBLE);
                    }


                }
            } else if (apitype.equals(API.LIKE_VIDEO)) {
                LikeDislike(true);
            } else if (apitype.equals(API.UNLIKE_VIDEO)) {
                LikeDislike(false);
            } else if (apitype.equals(API.HOME_PAGE_VIDEOS)) {
                Log.d("checkliveaa", "gfh");

                setHomeData(result);
                //  notifyAdapters();
            } else if (apitype.equals(API.GET_URL_DATA)) {
                JSONObject jsonObject = result.optJSONObject("data");
                List<MenuMasterList> menuMasterLists = new ArrayList<>();
                MenuMasterList menuMasterList = new Gson().fromJson(jsonObject.toString(), MenuMasterList.class);
                menuMasterLists.add(menuMasterList);
                setUrlData(menuMasterLists);
                Log.d("shantanu", jsonObject.toString());

            } else if (apitype.equals(API.NOTIFICATION_STATUS)) {
/*
                LikeDislike(false);
*/
                JSONObject jsonObject = result.optJSONObject("data");

                NotificationStatus notificationStatus = new Gson().fromJson(result.optJSONObject("data").toString(), NotificationStatus.class);

                get_notification_status = notificationStatus.getNotificationStatus();

                SharedPreference.getInstance(this).putString(Constants.NOTIFICATION_STATUS, get_notification_status);


                if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("0")) {
                    notification_lay.setChecked(true);
                } else if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("1")) {
                    notification_lay.setChecked(false);
                }

                SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS);

                //  Toast.makeText(this, get_notification_status, Toast.LENGTH_SHORT).show();

            } else if (apitype.equals(API.GET_BHAJANS)) {


                JSONArray jsonArray = result.optJSONArray("data");
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
            if (apitype.equals(API.HOME_PAGE_VIDEOS)) {
                nonFullScreenLayout.setVisibility(View.GONE);
            } else if (apitype.equals(API.POST_COMMENT)) {
                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);
                ToastUtil.showShortToast(this, result.optString("message"));
            } else if (apitype.equals(API.NOTIFICATION_STATUS)) {
                if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("0")) {
                    notification_lay.setChecked(true);
                } else if (SharedPreference.getInstance(this).getString(Constants.NOTIFICATION_STATUS).equalsIgnoreCase("1")) {
                    notification_lay.setChecked(false);
                }
            }
        }

    }

    private void setUrlData(List<MenuMasterList> menuMasterLists) {
        if (data_type.equalsIgnoreCase("bhajan")) {
            MenuMasterList[] bhajans = new MenuMasterList[0];
            MenuMasterList[] bhajanforImagefromHome;
            if (homeBhajanListNew.size() > 0) {
                homeBhajanListNew.clear();
            }
            homeBhajanListNew.addAll(menuMasterLists);
            if (homeBhajanListNew != null) {
                bhajans = new MenuMasterList[homeBhajanListNew.size()];
                for (int i = 0; i < homeBhajanListNew.size(); i++) {
                    bhajans[i] = homeBhajanListNew.get(i);
                }
            }

            if (playerlayout1.getVisibility() == View.VISIBLE)
                playerlayout1.setVisibility(View.GONE);

            if (playerlayout2.getVisibility() == View.VISIBLE)
                playerlayout2.setVisibility(View.GONE);

            Bundle bundle = new Bundle();
            try {

                if (downloadmediaplayer != null) {
                    if (downloadmediaplayer.isPlaying()) {
                        downloadmediaplayer.pause();
                    }
                }

                if (playlistmediaplayer != null) {
                    if (playlistmediaplayer.isPlaying()) {
                        playlistmediaplayer.pause();
                    }
                }
                Constants.AUDIO_ACTIVE_LIST = "home";
                Intent intentservice = new Intent(HomeActivityy.this, AudioPlayerService.class);
                intentservice.putExtra("bhajanarray1", bhajans);
                intentservice.putExtra("status", "bhajan");
                intentservice.putExtra("redirect", Constants.AUDIO_ACTIVE_LIST);
                PreferencesHelper.getInstance().setValue("index", 0);
                startService(intentservice);
                bindService(intentservice, HomeFragment.serviceConnection, Context.BIND_AUTO_CREATE);
                Constants.IS_PLAYING_ON_CATEGORY = "false";
                // Constants.LIST_INDEX = bhajanResponseArrayList.get(position);
                Constants.LIST_INDEX = 0;
                bhajanforImagefromHome = bhajans;
                bundle.putInt("position", 0);
                back1 = "";
                bundle.putSerializable("bhajans1", bhajans);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("BHAJANS")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data_type.equalsIgnoreCase("news")) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("MasterlistNews", menuMasterLists.get(0));
            bundle.putSerializable("MasterlistNews1", (Serializable) menuMasterLists);
            bundle.putInt("type", 2);
            newspos1 = 0;
            Log.d("newspos", String.valueOf(0));
            NewsDetailFragRecent fragment = new NewsDetailFragRecent();
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(Constants.READ_NEWS)
                    .replace(R.id.container_layout_home, fragment)
                    .commit();

        } else if (data_type.equalsIgnoreCase("video")) {
            MenuMasterList[] videos1 = new MenuMasterList[menuMasterLists.size()];
            for (int i = 0; i < menuMasterLists.size(); i++) {
                videos1[i] = menuMasterLists.get(i);
            }
            Intent intent = new Intent(HomeActivityy.this, MainActivity.class);
            intent.putExtra("video_data1", videos1);
            intent.putExtra("type", 2);
            intent.putExtra("position", 0);
            startActivity(intent);
        }
    }

    private void setHomeData(JSONObject result) {
        web_view_bhajan = result.optString("web_view_bhajan");
        web_view_news = result.optString("web_view_news");
        web_view_video = result.optString("web_view_video");

        SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_BHAJAN, web_view_bhajan);
        SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_NEWS, web_view_news);
        SharedPreference.getInstance(context).putString(Constants.WEB_VIEW_VIDEO, web_view_video);
        JSONArray jsonArrayChannel = result.optJSONArray("channel");

        for (int i = 0; i < jsonArrayChannel.length(); i++) {
            channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
            /* homeChannelList.add(channel);
                channellist.add(channel);*/
            Log.d("check", "jj");
            Log.d("check", String.valueOf(channel));
            if (channel.getId().equalsIgnoreCase(id)) {
                likeV = channellist.get(i).getLikes();
                IS_lik = channellist.get(i).getIs_likes();
                Log.d("checklive", likeV);
                Log.d("checklive1", IS_lik);
            }

        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        //swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showDialogBox(this, jsonstring);
    }

    private void LikeDislike(boolean b) {
        if (likeyoutube) {
            like1.setTag(b);
            int likenum = 0;
            if (likeNumber1.getText().toString().equalsIgnoreCase("0 like")) {
                likenum = 0;
            } else {
                likenum = Integer.parseInt(likeNumber1.getText().toString().split(" ")[0]);
            }
            if (b) {
                if (likenum == 0) {
                    likeNumber1.setText(likenum + 1 + " like");
                } else {
                    likeNumber1.setText(likenum + 1 + " likes");
                }
                like1.setImageResource(R.drawable.liked);
                video.setLikes(String.valueOf(likenum + 1));
                video.setIs_like("1");
            } else {
                if (likenum == 1) {
                    likeNumber1.setText("0 like");
                } else if (likenum == 2) {
                    likeNumber1.setText(likenum - 1 + " like");
                } else {
                    likeNumber1.setText(likenum - 1 + " likes");
                }
                like1.setImageResource(R.drawable.like_default);
                video.setLikes(String.valueOf(likenum - 1));
                video.setIs_like("0");
            }
            isLike = true;
        } else {
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
                video.setLikes(String.valueOf(likenum - 1));
                video.setIs_like("0");
            }
            isLike = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isVpn(this)) {
            ToastUtil.showDialogBox3(this, "Please disable VPN");
        }
        // Continue updates when resumed
        mUpdateManager.continueUpdate();
        if (mQueueMenuItem != null) {
            mQueueMenuItem.setVisible((mCastSession != null) && mCastSession.isConnected());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onUserLeaveHint() {
        if (simpleExoPlayer.getPlayWhenReady()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (isInPictureInPictureMode()) {

                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                enterPictureInPictureMode();
            }
        }

    }

    private void beginDownload() {
        Constants.DOWNLOAD_ACTIVE = "true";
        //download.setClickable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean downloading = true;
                //    Toast.makeText(HomeActivityy.this, "Downloading In Progress...", Toast.LENGTH_SHORT).show();
                File file = new File(getExternalFilesDir("bhakti"), video.getVideo_title() + ".jwp");

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video.getVideo_url()))
                        .setTitle(video.getVideo_title())// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                        // .setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
                while (downloading) {


                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadID); //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);


                    runOnUiThread(() -> circularProgressBar.setProgress((int) dl_progress * 2));

                    // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();

                }

            }
        }).start();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        myoutubePlayer = youTubePlayer;
        youtubePlayer2 = youTubePlayer;

        youtubePlayer2.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        myoutubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

       /* if (!wasRestored) {
                          cueVideo
            youtubePlayer.loadVideo("9rLZYyMbJic");
        }
        else {
            youtubePlayer.play();
        }*/

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }


    private void initSearchView() {
        searchIv.setVisibility(View.GONE);
        searchIv.setFocusable(false);


        searchIv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIv.setFocusable(true);
                app_logo.setVisibility(View.GONE);
                tvImg.setVisibility(View.GONE);
                profile_iv.setVisibility(View.GONE);

            }
        });
        searchIv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!TextUtils.isEmpty(searchContent) && searchContent != null) {
                    Toast.makeText(HomeActivityy.this, "close", Toast.LENGTH_SHORT).show();
                }
                app_logo.setVisibility(View.VISIBLE);
                profile_iv.setVisibility(View.VISIBLE);
                //tvImg.setVisibility(View.VISIBLE);

                return false;
            }
        });

        //View searchPlateView = searchIv.findViewById(R.id.search_plate);
        //searchPlateView.setBackgroundResource(R.drawable.white_search_view_bg);
        //searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        //use this method for search process
        searchIv.setQueryHint("Search here");
        //use this method for search process
        searchIv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContent = query;
                Utils.closeKeyboard(HomeActivityy.this);
                searchIv.setFocusable(false);
                searchIv.clearFocus();
                //searchView.onActionViewCollapsed();
                if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof HomeFragment1) {
                    ((HomeFragment1) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchHomeVideos();
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajanViewAllFragment) {
                    ((BhajanViewAllFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchBhajan();
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosChildFragment) {
                    ((VideosChildFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchVideo();
                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof BhajansCategoryFragment) {
                    ((BhajansCategoryFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchBhajan();

                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof SankirtanListFrag) {
                    ((SankirtanListFrag) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchSankirtan();

                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof GuruListFragmentNew) {
                    ((GuruListFragmentNew) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getSearchGuru();

                } else if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof VideosParentFragment) {
                    ((VideosParentFragment) getSupportFragmentManager().findFragmentById(R.id.container_layout_home)).getCategorySearchList();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getSupportFragmentManager().findFragmentById(R.id.container_layout_home) != null && getSupportFragmentManager().findFragmentById(R.id.container_layout_home) instanceof PlayListFrag) {
                    searchIv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {

                            if (mSearchListener != null) {

                                mSearchListener.textchanged(s);

                            }
                            return false;
                        }

                    });

                }

                return false;
            }

        });
        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchIv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    public void loadsong(Bhajan[] bhajan, int audioindex) {
        AudioIndex = audioindex;
        dropbhajan = bhajan;
        Bundle bundle1 = new Bundle();
        bundle1.putInt("position", audioindex);
        //    bundle1.putSerializable("id",bhajanResponseArrayList.get(0).);
        bundle1.putSerializable("bhajans", bhajan);
    }

    public void shareapplink() {
        bitmap = BitmapFactory.decodeResource(HomeActivityy.this.getResources(), R.drawable.ic_launcher_background);

        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getPackageName();
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        //  i.setType("text/plain");
        if (bitmap != null) {
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, getImageUri(HomeActivityy.this, bitmap));
        }

        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);

        if (i.resolveActivity(HomeActivityy.this.getPackageManager()) != null) {
            this.startActivity(i);
        }
    }

    public void showViewAllFragmentForWallpaper(List<WallPaperModel> wallPaperModelList) {
        Fragment fragment = new WallpaperViewAllFragment();
        Bundle b = new Bundle();
        b.putSerializable(Constants.DATA, (Serializable) wallPaperModelList);
        fragment.setArguments(b);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_layout_home, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }
}