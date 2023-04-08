package com.sanskar.tv.module.HomeModule.Fragment;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.MediaPlayerService;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.NotificationDialog;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanAdapter1;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ReleatedSongAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ReleatedSongAdapter1;
import com.sanskar.tv.module.HomeModule.BhajanInterface;

import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.service.AudioService;
import com.sanskar.tv.sqlite.DatabaseManager;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 3/5/2018.
 */

public class BhajanPlayFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnCompletionListener,
        SeekBar.OnSeekBarChangeListener, NetworkCall.MyNetworkCallBack, ViewPager.OnPageChangeListener, DiscreteScrollView.OnItemChangedListener, BhajanInterface,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.sanskar.tv.PlayNewAudio";
    public static final String BROADCAST_PLAY_NEXT_SONG = "play next song";
    public static final String BROADCAST_ISPLAYING_SONG = "song playscreen";

    TextView Queue_txv;
    LinearLayout ll_queue;
    ImageView Queue_img;

    RelativeLayout share_watch_video;

    int BHAJAN_AD_COUNTS = 0;

    boolean isPlaying = false;
    private int seekForwardTime = 15 * 1000; // default 15 second
    private int seekBackwardTime = 15 * 1000; // default 15 secondd

    public static MediaPlayer mediaPlayer;
    public static ImageView playPause, forward, rewind, playPause1, fast_forward_bhajan, fast_rewind_bhajan;
    // public Bhajan bhajan;
    //TODO by sumit 7-8-20

    public static Bhajan bhajan;
    public static MenuMasterList bhajan1;
    //--------------
    boolean serviceBound = false;
    ViewPager viewPager;

    private int audioIndex;

    TabLayout tab;
    ViewPager songviewpager;
    private LinearLayout indicatorLayout;
    private List<ImageView> viewList;
    private ImageView indicatorImg;
    private String[] img;
    public Runnable update;
    private String advertisement_id = "", time_slot_id = "";
    Timer timer;
    String status = "";
    private static int currentPage = 0;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;


    RelativeLayout toolbar, toolbar1, playList_layout, download_watch_video, like_watch_video;
    TextView title, views_number_watch_video;
    ImageView like, more, close, radio_image;
    LinearLayout dropdown;
    Context context;
    BhajanAdapter bhajanAdapter;
    BhajanAdapter1 bhajanAdapter1;


    public static PopupWindow popupWindowsong;

    BhajanPlayerView bhajanPlayAdapter;
    TextView bhajanName, artistName, downloadwatch_video;
    ImageView download, share_btn, playlist;
    TextView addToPlayList;
    BhajanInterface bhajanInterface;
    Toolbar toolbar_play_bhajan1;
    BottomSheetBehavior behavior;
    CoordinatorLayout coordinatorLayout;
    ImageView artistImage;
    RelativeLayout top_rl;
    File file;
    private DiscreteScrollView itemPicker;
    private AudioPlayerService player;
    private SeekBar seekBar, seekBar1;
    private TextView startTimeTV, startTimeTV1;
    private TextView endTimeTV, endTimeTV1;
    private AppTextView bhajanTitle, bhajanTitle1;
    private AppTextView bhajanArtistName, bhajanArtistName1;
    DatabaseManager databaseManager;
    private Handler handler;
    boolean isInserted;
    LinearLayout bottomlayout_like;

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if (AudioPlayerService.mediaPlayer != null) {

                long totalDuration = AudioPlayerService.mediaPlayer.getDuration();
                long currentDuration = AudioPlayerService.mediaPlayer.getCurrentPosition();

                endTimeTV.setText("" + milliSecondsToTimer(totalDuration));

                startTimeTV.setText("" + milliSecondsToTimer(currentDuration));
                endTimeTV1.setText("" + milliSecondsToTimer(totalDuration));

                startTimeTV1.setText("" + milliSecondsToTimer(currentDuration));


                int progress = getProgressPercentage(currentDuration, totalDuration);
                Log.d("Progress", "" + progress);
                seekBar.setProgress(progress);
                seekBar1.setProgress(progress);

                handler.postDelayed(this, 1000);
            }
        }
    };
    public static Bhajan[] bhajans;

    public static List<Bhajan> Relatedbhajanlist;
    public static MenuMasterList[] bhajans1;

    public static List<MenuMasterList> Relatedbhajanlist1;
    // private int audioType;
    public static int audioType;
    //  private static int position = 0;
    public static int position = 0;

    private NetworkCall networkCall;
    private List<Bhajan> bhajanList;
    private List<MenuMasterList> bhajanList1;
    private String Is_premium = "";
    private ProgressBar playProgress;
    private String[] imgString;
    private Progress progress;
    private int tabPos;
    private boolean firstTimeSelected = true;
    public static boolean isImgBTnClick = false;
    private RecyclerView relatedRV;
    private BhajanViewAllAdapter adapter;
    private Bhajan[] relatedBhajans;
    private MenuMasterList[] relatedBhajans1;
    private long downloadID;
    CircularProgressBar circularProgressBar;
    RelativeLayout rl_like_bhajan;
    AdvertisementAds[] pictureAds;

    private ExpandableWeightLayout mExpandLayout;
    /*private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.AudioBinder audioBinder = (AudioService.AudioBinder) service;
            audioService = audioBinder.getService();
            audioService.setListener(BhajanPlayFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };*/
    // public BottomNavigationView bottomNavigationView;
    private AudioService audioService;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            circularProgressBar.setVisibility(View.GONE);
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                download.setImageResource(R.mipmap.downloaded_complete);
                downloadwatch_video.setText("Downloaded");
            }
        }
    };

    /* private void stophandler(){
         IntentFilter intentFilter = new IntentFilter(HomeActivityy.STOP_HANDLER);
         getActivity().registerReceiver(stophandlers,intentFilter);
     }
     private BroadcastReceiver stophandlers = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             handler.removeCallbacks(mUpdateTimeTask);
         }
     };*/

    private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("isplaying").equals("true")) {
                playPause.setImageResource(R.mipmap.audio_pause);
                playPause1.setImageResource(R.mipmap.audio_pause);
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
                Constants.IS_PLAYING = "true";
                // onTrackPlay();

            } else if (intent.getStringExtra("isplaying").equals("false")) {
                playPause.setImageResource(R.mipmap.audio_play);
                playPause1.setImageResource(R.mipmap.audio_play);
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                //onTrackPause();

                Constants.IS_PLAYING = "false";
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_bhajan1, null);
        Constants.IS_PLAYING_ONPLAY = "true";

        databaseManager = new DatabaseManager(getActivity());

        if (((HomeActivityy) context).playerlayout.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
        }

        top_rl = view.findViewById(R.id.top_rl);
        views_number_watch_video = view.findViewById(R.id.views_number_watch_video);
        setPlayPause();
        setSongData();


        // stophandler();
        return view;

    }

    private void showDialogBox(AdvertisementAds menuMasterListList) {
        ++Networkconstants.adsCount;

        final Dialog dialog = new Dialog(getContext(), R.style.BottomSheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.premium_banner);

        ImageView imageView, imageView1;

        imageView = dialog.findViewById(R.id.banner_img);

        advertisement_id = menuMasterListList.getId();
        time_slot_id = menuMasterListList.getTime_slot_id();

        Glide.with(context)
                .load(menuMasterListList.getMedia())
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder).fitCenter())
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageView1 = dialog.findViewById(R.id.delete_banner_img);

        imageView1.setOnClickListener(v -> {
            updateAppAdvertisement(false);
            dialog.dismiss();
        });

        dialog.show();

    }

    private void updateAppAdvertisement(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(BhajanPlayFragment.this, context);
            networkCall.NetworkAPICall(API.UPDATE_APP_ADVERTISEMENT_COUNTER, b);
        } else {
            Toast.makeText(context, "Connection Error!!!", Toast.LENGTH_SHORT).show();
        }

    }


    public void setPlayPause() {
        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause, intentFilter);
    }

    public void setSongData() {
        IntentFilter filter = new IntentFilter(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bhajanTitle.setText(intent.getStringExtra("title"));
            bhajanArtistName.setText(intent.getStringExtra("description"));

            if (intent.getStringExtra("isplaying").equals("true")) {
                //   ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
                position = Constants.AUDIO_INDEX;
                if (bhajans != null) {
                    try {
                        setBhajanData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bhajans1 != null) {
                    try {
                        setBhajanData1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                scrollPlay(position);
            } else if (intent.getStringExtra("isplaying").equals("false")) {
                //   ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                position = Constants.AUDIO_INDEX;
                if (bhajans != null) {
                    try {
                        setBhajanData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bhajans1 != null) {
                    try {
                        setBhajanData1();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                scrollPlay(position);
            }

            //  ((HomeActivityy) context).playerlayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        getBundleData();
        initView(view);

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                startTimeTV.setTextColor(Color.parseColor("#FFFFFF"));
                endTimeTV.setTextColor(Color.parseColor("#FFFFFF"));

                break;
            case Configuration.UI_MODE_NIGHT_NO:
                startTimeTV.setTextColor(Color.parseColor("#000000"));
                endTimeTV.setTextColor(Color.parseColor("#000000"));
                break;
        }

        getBhajanRelatedAudios();
        if (SharedPreference.getInstance(context).getString(Constants.IS_PREMIUM) != null) {
            Is_premium = SharedPreference.getInstance(context).getString(Constants.IS_PREMIUM);
        }
        try {
            if (bhajans != null) {
                setBhajanData();
            }
            if (bhajans1 != null) {
                setBhajanData1();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Is_premium.equalsIgnoreCase("0")) {
            if (SharedPreference.getInstance(context).getPictureAds() != null && SharedPreference.getInstance(context).getPictureAds().length != 0) {
                Log.e("chaudhary in view", "true");
                pictureAds = SharedPreference.getInstance(context).getPictureAds();
                if (pictureAds.length > 0) {
                    if (Networkconstants.adsCount > pictureAds.length - 1) {
                        Networkconstants.adsCount = 0;
                    }
                    showDialogBox(pictureAds[Networkconstants.adsCount]);
                    BHAJAN_AD_COUNTS++;
                }

            } else {
                Log.e("chaudhary in view", "false");
            }
        }
        if (bhajans != null) {
            file = new File(getActivity().getExternalFilesDir("bhajanaudio"), bhajan.getTitle() + ".jwpmp3");
            if (file.exists()) {
                download.setImageResource(R.mipmap.downloaded_complete);
                downloadwatch_video.setText("Downloaded");
            }
        }
        if (bhajans1 != null) {
            file = new File(getActivity().getExternalFilesDir("bhajanaudio"), bhajan1.getTitle() + ".jwpmp3");
            if (file.exists()) {
                download.setImageResource(R.mipmap.downloaded_complete);
                downloadwatch_video.setText("Downloaded");
            }
        }

        updateBHajanCount(false);

//        if (((HomeActivityy) context).playerlayout.getVisibility() == View.VISIBLE) {
//            ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
//        }

        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        //   setSongData();

        SetTab();

    }

    private void updateBHajanCount(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(BhajanPlayFragment.this, context);
            networkCall.NetworkAPICall(API.BHAJAN_COUNT, b);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }

    private void SetTab() {
        setindicator();
        tab.addTab(tab.newTab().setText("Transparent"));
        //   tab.addTab(tab.newTab().setText("Songlist"));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabAdapter adapter = new TabAdapter(context, getChildFragmentManager(), tab.getTabCount());
        songviewpager.setAdapter(adapter);

        songviewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            public void onTabSelected(TabLayout.Tab tab) {
                songviewpager.setCurrentItem(tab.getPosition());
            }


            public void onTabUnselected(TabLayout.Tab tab) {

            }

            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setindicator() {

        img = new String[2];

        for (int i = 0; i < 2; i++) {

            indicatorLayout.addView(addSliderDotsView());
        }
        setViewPagerData();
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

        songviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
/*
        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == imgString.length) {
                    currentPage = 0;
                }
                viewpage.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);*/
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.play_btn_bhajan:
            case R.id.play_btn_bhajan1:
                try {
                    Constants.CLICK_ACTIVE = "true";
                    playAudio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.forward_btn_bhajan:
//                if((bhajans.length - 1)>= position ) {
                Constants.CLICK_ACTIVE_FORWARD = "true";
                isImgBTnClick = true;
                BHAJAN_AD_COUNTS++;

                if (Is_premium.equalsIgnoreCase("0")) {
                    if (SharedPreference.getInstance(context).getPictureAds() != null && SharedPreference.getInstance(context).getPictureAds().length != 0) {
                        Log.e("chaudhary", "true");
                        if (Networkconstants.adsCount >= pictureAds.length - 1) {
                            Networkconstants.adsCount = 0;
                        }
                        if (BHAJAN_AD_COUNTS == 3) {
                            showDialogBox(pictureAds[Networkconstants.adsCount]);
                            BHAJAN_AD_COUNTS = 0;
                        }

                    }
                }

                forwardBhajanSong();
//                }
                break;

            case R.id.fast_forward_bhajan:
//                if((bhajans.length - 1)>= position ) {

                fastforwardSong();
//                }
                break;

            case R.id.fast_rewind_bhajan:
//                if((bhajans.length - 1)>= position ) {

                fastRewindSong();
//                }
                break;

            case R.id.rewind_btn_bhajan:
//                if (position > 0) {

                BHAJAN_AD_COUNTS++;
                if (Is_premium.equalsIgnoreCase("0")) {
                    if (SharedPreference.getInstance(context).getPictureAds() != null && SharedPreference.getInstance(context).getPictureAds().length != 0) {
                        if (Networkconstants.adsCount >= pictureAds.length - 1) {
                            Networkconstants.adsCount = 0;
                        }
                        if (BHAJAN_AD_COUNTS == 3) {
                            showDialogBox(pictureAds[Networkconstants.adsCount]);
                            BHAJAN_AD_COUNTS = 0;
                        }

                    }
                }
                Constants.CLICK_ACTIVE_REWIND = "true";
                isImgBTnClick = true;
                backwardSong();

                break;

            case R.id.rl_like_bhajan:
                if (isConnectingToInternet(context)) {
                    if (like.getTag() != null) {
                        if (!Boolean.parseBoolean(like.getTag().toString())) {
                            networkCall.NetworkAPICall(API.BHAJAN_LIKE, false);
                        } else {
                            networkCall.NetworkAPICall(API.BHAJAN_UNLIKE, false);
                        }
                    } else {
                        like.setImageResource(R.mipmap.audio_liked);
                        //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;

            case R.id.more_options_toolbar3:
                new NotificationDialog(context, this).showPopup();
                break;

            case R.id.close_toolbar3:
                killMediaPlayer();
                toolbar.setVisibility(View.GONE);
                FragmentManager fragmentManager = ((HomeActivityy) context).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentById(R.id.container_layout_home)).commit();

                ((HomeActivityy) context).onBackPressed();
                break;

            case R.id.download_watch_video:
            case R.id.download:

                download.setClickable(false);
                if (!file.exists()) {
                    circularProgressBar.setVisibility(View.VISIBLE);
                    if (bhajans != null) {
                        beginDownload();
                    }
                    if (bhajans1 != null) {
                        beginDownload1();
                    }
//                        download.setClickable(true);
//                        Constants.DOWNLOAD_ACTIVE = "true";
                } else {
                    download.setClickable(true);
                    Toast.makeText(getActivity(), "File Already Exists", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.dropdown:
                Constants.CALL_RESUME = "true";
                Constants.IS_PLAYING_ONPLAY = "false";
                ((HomeActivityy) context).onBackPressed();
                handler.removeCallbacks(mUpdateTimeTask);
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
                break;

            case R.id.ll_queue:
                PopupWindow popupwindow_obj = popupDisplay();
                //popupwindow_obj.getContentView().
                popupwindow_obj.showAsDropDown(ll_queue, 70, 70); // where u want show on view click event popupwindow.showAsDropDown(view, x, y);
                break;
            case R.id.playList_layout:
            case R.id.playlist:
                addDataToPlayList();
                // addPlayListData();
//                playlist.setVisibility(View.GONE);
//                addToPlayList.setVisibility(View.GONE);
                break;
            case R.id.share_btn:
            case R.id.share_watch_video:
                //  shareTextUrl();
                shareapp();
                break;
        }
    }

    public PopupWindow popupDisplay() {
        popupWindowsong = new PopupWindow(context);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_song_list, null);


        RecyclerView releted_song_recycler;
        ReleatedSongAdapter releatedSongAdapter;
        ReleatedSongAdapter1 releatedSongAdapter1;
        List<Bhajan> bhajanList;
        List<MenuMasterList> bhajanList1;
        Context context;
        LinearLayoutManager linearLayoutManager;


        releted_song_recycler = view.findViewById(R.id.releted_song_recycler);
        if (bhajan != null) {
            context = getActivity();
            bhajanList = Relatedbhajanlist;
            releatedSongAdapter = new ReleatedSongAdapter(bhajanList, context);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            releted_song_recycler.setLayoutManager(linearLayoutManager);

            releted_song_recycler.setAdapter(releatedSongAdapter);
        }
        if (bhajans1 != null) {
            context = getActivity();
            bhajanList1 = Relatedbhajanlist1;
            releatedSongAdapter1 = new ReleatedSongAdapter1(bhajanList1, context);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            releted_song_recycler.setLayoutManager(linearLayoutManager);

            releted_song_recycler.setAdapter(releatedSongAdapter1);
        }


        popupWindowsong.setFocusable(true);
        float scale = getResources().getDisplayMetrics().density;
        int converter = (int) (350 * scale + 0.5f);
        popupWindowsong.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        //  popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindowsong.setHeight(converter);
        popupWindowsong.setContentView(view);
        return popupWindowsong;
    }

    private void shareapp() {

        String bhajan_url = SharedPreference.getInstance(context).getString(Constants.WEB_VIEW_BHAJAN);
        //String bhajan_url = "http://app.sanskargroup.in/sanskar_staging/index.php/web/Home/play_bhajan_by_id?id=";
        String playStoreLink = "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
        //  Uri imageUri = Uri.parse("android.resource://" + getPackageName()+ "/mipmap/" + "ic_launcher_round");
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        //  i.setType("text/plain");

        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Sanskar");
        /* i.putExtra(android.content.Intent.EXTRA_TEXT, "Download Sanskar App " + playStoreLink);*/
        if (bhajan != null) {
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Listen to bhajan " + bhajan_url + bhajan.getId());
        }
        if (bhajans1 != null) {
            i.putExtra(android.content.Intent.EXTRA_TEXT, "Listen to bhajan " + bhajan_url + bhajan1.getId());
        }
        //  i.putExtra(Intent.EXTRA_STREAM,imageUri);
        // startActivity(Intent.createChooser(i,"Share via"));

        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            this.startActivity(i);
        }

    }
       /*
       public void addDataToPlayList() {
        playlist.setClickable(false);

        databaseManager.addclient(new PlayListModel(bhajan.getTitle(),bhajan.getMedia_file(),
                bhajan.getArtist_image()));

        views_number_watch_video.setText("Add Sucess..");
    //    Toast.makeText(getActivity(),"Successfully Inserted",Toast.LENGTH_SHORT).show();
    }*/


    public void addDataToPlayList() {

        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(BhajanPlayFragment.this, context);
            networkCall.NetworkAPICall(API.ADD_TO_PLAYLIST, false);
        } else {
            ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

        playlist.setClickable(false);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {


        if (startTimeTV.getText().toString().equalsIgnoreCase(endTimeTV.getText().toString())) {
            Constants.CLICK_ACTIVE_FORWARD = "true";
            isImgBTnClick = true;
                /*BHAJAN_AD_COUNTS++;

                if (Is_premium.equalsIgnoreCase("0")){
                    if (SharedPreference.getInstance(context).getPictureAds()!=null){
                        if (Networkconstants.adsCount>pictureAds.length-1){
                            Networkconstants.adsCount=0;
                        }
                        if (BHAJAN_AD_COUNTS==3){
                            showDialogBox(pictureAds[Networkconstants.adsCount]);
                            BHAJAN_AD_COUNTS=0;
                        }

                    }
                }*/

            forwardBhajanSong();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = AudioPlayerService.mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        AudioPlayerService.mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("on resume", "check resume");
        Constants.IS_PLAYING_ONPLAY = "true";

        ((HomeActivityy) context).handleToolbar();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("on pause", "music pause");
        //progress.dismiss();
      /*  if (mediaPlayer != null) {
            mediaPlayer.pause();
            playPause.setImageResource(R.mipmap.audio_play);
            playPause1.setImageResource(R.mipmap.audio_play);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        Constants.IS_PLAYING_ONPLAY = "false";
        //progress.dismiss();
        handler.removeCallbacks(mUpdateTimeTask);
        if (MediaPlayerService.mMediaPlayer != null) {
            //  AudioPlayerService.mediaPlayer.pause();
            //  AudioPlayerService.mediaPlayer.release();
            //  playPause.setImageResource(R.mipmap.audio_play);
            // playPause1.setImageResource(R.mipmap.audio_play);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  killMediaPlayer();
        context.unregisterReceiver(onDownloadComplete);
        context.unregisterReceiver(playpause);
        context.unregisterReceiver(playNewAudio);
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.BHAJAN_LIKE) || API_NAME.equals(API.BHAJAN_UNLIKE)) {
            if (bhajans != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                        .setBodyParameter("bhajan_id", bhajan.getId());
            }
            if (bhajans1 != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                        .setBodyParameter("bhajan_id", bhajan1.getId());
            }


        } else if (API_NAME.equals(API.RECENT_VIEW)) {
            if (bhajans != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", "1")
                        .setMultipartParameter("media_id", bhajan.getId());
            }
            if (bhajans1 != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", "1")
                        .setMultipartParameter("media_id", bhajan1.getId());
            }

        } else if (API_NAME.equals(API.BHAJAN_RELATED)) {
            if (bhajans != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan.getId());
            }
            if (bhajans1 != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan1.getId());
            }

        } else if (API_NAME.equals(API.BHAJAN_COUNT)) {
            if (bhajans != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan.getId());
            }
            if (bhajans1 != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan1.getId());
            }

        } else if (API_NAME.equals(API.ADD_TO_PLAYLIST)) {
            if (bhajans != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan.getId())
                        .setMultipartParameter("type", "1");
            }
            if (bhajans1 != null) {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                        .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("bhajan_id", bhajan1.getId())
                        .setMultipartParameter("type", "1");
            }

        } else if (API_NAME.equals(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
            Log.d("shantanu", advertisement_id + " " + time_slot_id);
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT) != null ? SharedPreference.getInstance(context).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("advertisement_id", advertisement_id)
                    .setMultipartParameter("time_slot_id", time_slot_id)
                    .setMultipartParameter("advertisement_status", String.valueOf(2));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (result.optBoolean("status")) {
            if (API_NAME.equals(API.BHAJAN_LIKE)) {
                LikeDislike(true);
                try {
                    if (bhajans != null) {
                        setBhajanData();
                    }
                    if (bhajans1 != null) {
                        setBhajanData1();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (API_NAME.equals(API.BHAJAN_UNLIKE)) {
                LikeDislike(false);
                try {
                    if (bhajans != null) {
                        setBhajanData();
                    }
                    if (bhajans1 != null) {
                        setBhajanData1();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (API_NAME.equals(API.BHAJAN_RELATED)) {
                setBhajanRelatedData(result.optJSONArray("data"));
            } else if (API_NAME.equalsIgnoreCase(API.BHAJAN_COUNT)) {
                // Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
            } else if (API_NAME.equalsIgnoreCase(API.UPDATE_APP_ADVERTISEMENT_COUNTER)) {
                //  Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
            } else if (API_NAME.equalsIgnoreCase(API.ADD_TO_PLAYLIST)) {
                views_number_watch_video.setText("Success");
                playlist.setImageResource(R.mipmap.added_to_playlist);
                // Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (API_NAME.equalsIgnoreCase(API.ADD_TO_PLAYLIST)) {
                playlist.setImageResource(R.mipmap.added_to_playlist);

                views_number_watch_video.setText("Already Added");
            }
            //          ToastUtil.showDialogBox(context, result.optString("message"));
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        //  ToastUtil.showDialogBox(context, jsonstring);
    }

    public void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("bhajans")) {
                status = "bhajan";
                bhajans1 = null;
                bhajan1 = null;
                bhajans = (Bhajan[]) bundle.getSerializable("bhajans");
                position = bundle.getInt("position");
                bhajan = bhajans[position];
                Relatedbhajanlist = Arrays.asList(bhajans);

                imgString = new String[bhajans.length];

                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = bhajans[i].getImage();
                }

                // PreferencesHelper.getInstance().setValue("bannerget",bhajan.getImage());

                audioType = Constants.BHAJAN_AUDIO_TYPE;
            }

            if (bundle.containsKey("bhajans1")) {
                if (bundle.containsKey("status")) {
                    status = bundle.getString("status");
                } else {
                    status = "bhajan";
                }

                bhajans = null;
                bhajan = null;
                bhajans1 = (MenuMasterList[]) bundle.getSerializable("bhajans1");
                position = bundle.getInt("position");
                bhajan1 = bhajans1[position];
                Relatedbhajanlist1 = Arrays.asList(bhajans1);

                imgString = new String[bhajans1.length];

                for (int i = 0; i < imgString.length; i++) {

                    imgString[i] = bhajans1[i].getImage();

                }


                // PreferencesHelper.getInstance().setValue("bannerget",bhajan.getImage());

                audioType = Constants.BHAJAN_AUDIO_TYPE;
            } else if (bundle.containsKey("guruAudioData")) {

                status = "bhajan";
                position = bundle.getInt("guruAudioData");
                bhajan = ((HomeActivityy) context).bhajanList.get(position);

                imgString = new String[((HomeActivityy) context).bhajanList.size()];

                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = ((HomeActivityy) context).bhajanList.get(position).getImage();
                }

                audioType = Constants.GURU_AUDIO_TYPE;
            } else if (bundle.containsKey(Constants.PLAY_LIST_DATA)) {
                status = "bhajan";
                bhajanList = (List<Bhajan>) bundle.getSerializable(Constants.PLAY_LIST_DATA);
                bhajans = bhajanList.toArray(new Bhajan[bhajanList.size()]);
                position = bundle.getInt("position");
                bhajan = bhajanList.get(position);

                imgString = new String[bhajanList.size()];
                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = bhajanList.get(position).getImage();
                }

                audioType = Constants.PLAY_LIST_AUDIO_TYPE;
            }
        }
    }

    TextView likeCount;

    public void initView(View view) {

        Constants.CLICK_ACTIVE = "false";
        //progress = new Progress(context);
        bottomlayout_like = view.findViewById(R.id.bottomlayout_like);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        viewPager = view.findViewById(R.id.view_pager_play_bhajan);
        toolbar = view.findViewById(R.id.toolbar_play_bhajan);
        like_watch_video = view.findViewById(R.id.like_watch_video);
        toolbar1 = view.findViewById(R.id.too1bar_player);
        title = toolbar.findViewById(R.id.toolbar_title);
        like = view.findViewById(R.id.like_bhajan);
        likeCount = view.findViewById(R.id.like_number_watch_video);
        more = toolbar.findViewById(R.id.more_options_toolbar3);
        close = toolbar.findViewById(R.id.close_toolbar3);
        radio_image = view.findViewById(R.id.radio_image);
        bhajanName = toolbar.findViewById(R.id.bhajan_name_toolbar3);
        artistName = toolbar.findViewById(R.id.artist_name_toolbar3);
        playPause = view.findViewById(R.id.play_btn_bhajan);
        playPause1 = view.findViewById(R.id.play_btn_bhajan1);
        forward = view.findViewById(R.id.forward_btn_bhajan);
        fast_forward_bhajan = view.findViewById(R.id.fast_forward_bhajan);
        fast_rewind_bhajan = view.findViewById(R.id.fast_rewind_bhajan);
        rewind = view.findViewById(R.id.rewind_btn_bhajan);
        seekBar = view.findViewById(R.id.seekbar_bhajan_player_view);
        seekBar1 = view.findViewById(R.id.seekbar_bhajan_player_view1);
        startTimeTV = view.findViewById(R.id.start_time_tv);
        endTimeTV = view.findViewById(R.id.end_time_tv);
        startTimeTV1 = view.findViewById(R.id.start_time_tv1);
        endTimeTV1 = view.findViewById(R.id.end_time_tv1);
        playProgress = view.findViewById(R.id.play_progress_bar);
        playProgress.setVisibility(View.GONE);
        share_btn = view.findViewById(R.id.share_btn);
        playlist = view.findViewById(R.id.playlist);
        share_watch_video = view.findViewById(R.id.share_watch_video);
        share_watch_video.setOnClickListener(this);
        addToPlayList = view.findViewById(R.id.views_number_watch_video);
        dropdown = view.findViewById(R.id.dropdown);
        //mExpandLayout = (ExpandableWeightLayout) view.findViewById(R.id.expandableLayout);
        bhajanTitle = view.findViewById(R.id.bhajan_title_tv);
        bhajanArtistName = view.findViewById(R.id.bhajan_artist_name_tv);
        bhajanTitle1 = view.findViewById(R.id.bhajan_title);
        bhajanArtistName1 = view.findViewById(R.id.artist_name);
        download = view.findViewById(R.id.download);
        artistImage = view.findViewById(R.id.artist_image);
        downloadwatch_video = view.findViewById(R.id.downloadwatch_video);
        download_watch_video = view.findViewById(R.id.download_watch_video);
        download_watch_video.setOnClickListener(this);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        playList_layout = view.findViewById(R.id.playList_layout);


        if (bhajans != null) {
            bhajanTitle.setText(bhajans[position].getTitle());
            //bhajanTitle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_right_anim));
            bhajanArtistName.setText(bhajans[position].getArtist_name());
            bhajanTitle1.setText(bhajans[position].getTitle());
            bhajanArtistName1.setText(bhajans[position].getArtist_name());
        }

        if (bhajans1 != null) {

            if (status.equals("bhajan")) {
                bhajanTitle.setText(bhajans1[position].getTitle());
                //bhajanTitle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_right_anim));
                bhajanArtistName.setText(bhajans1[position].getArtistName());
                bhajanTitle1.setText(bhajans1[position].getTitle());
                bhajanArtistName1.setText(bhajans1[position].getArtistName());
            }

            if (status.equals("radio")) {
                bhajanTitle.setText("Radio Satsang");
                //bhajanTitle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.left_right_anim));
                bhajanArtistName.setText("Sanskar");
                bhajanTitle1.setText("Radio Satsang");
                bhajanArtistName1.setText("Sanskar");
            }

        }

        relatedRV = view.findViewById(R.id.related_bhajan_rv);
        rl_like_bhajan = view.findViewById(R.id.rl_like_bhajan);
        // Queue_txv=view.findViewById(R.id.Queue_txv);
        ll_queue = view.findViewById(R.id.ll_queue);
        // Queue_img=view.findViewById(R.id.Queue_img);

        tab = view.findViewById(R.id.tab);
        songviewpager = view.findViewById(R.id.songviewpager);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);
        viewList = new ArrayList<>();

        if (!status.isEmpty()) {

            if (status.equals("radio")) {
                rewind.setVisibility(View.GONE);
                forward.setVisibility(View.GONE);
                like_watch_video.setVisibility(View.GONE);
                ll_queue.setVisibility(View.GONE);
                bottomlayout_like.setVisibility(View.GONE);
                fast_forward_bhajan.setVisibility(View.GONE);
                fast_rewind_bhajan.setVisibility(View.GONE);

            }
            if (status.equals("bhajan")) {
                rewind.setVisibility(View.VISIBLE);
                forward.setVisibility(View.VISIBLE);
                like_watch_video.setVisibility(View.VISIBLE);
                ll_queue.setVisibility(View.VISIBLE);
                bottomlayout_like.setVisibility(View.VISIBLE);
                fast_forward_bhajan.setVisibility(View.VISIBLE);
                fast_rewind_bhajan.setVisibility(View.VISIBLE);
            }
        }

        toolbar.setVisibility(View.GONE);
        itemPicker = (DiscreteScrollView) view.findViewById(R.id.view_pager_play_bhajan1);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);


        // itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.1f)
                .build());

        //onItemChanged(bhajanList.get(0));

        networkCall = new NetworkCall(this, context);
        //  like.setTag(false);
        rl_like_bhajan.setTag(false);

        playPause.setOnClickListener(this);
        playPause1.setOnClickListener(this);
        forward.setOnClickListener(this);
        rewind.setOnClickListener(this);
        fast_forward_bhajan.setOnClickListener(this);
        fast_rewind_bhajan.setOnClickListener(this);
        //like.setOnClickListener(this);
        rl_like_bhajan.setOnClickListener(this);
        more.setOnClickListener(this);
        close.setOnClickListener(this);
        download.setOnClickListener(this);
        share_btn.setOnClickListener(this);
        playlist.setOnClickListener(this);
        dropdown.setOnClickListener(this);
        artistImage.setOnClickListener(this);
        playList_layout.setOnClickListener(this);
        // Queue_txv.setOnClickListener(this);
        ll_queue.setOnClickListener(this);


        circularProgressBar.setProgressMax(200f);

        // Set ProgressBar Color
        circularProgressBar.setProgressBarColor(getResources().getColor(R.color.download_bhajan));

        circularProgressBar.setProgressBarWidth(2f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(1f); // in DP

        context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        //  AudioService.mediaPlayer = new MediaPlayer();

        Point screen = new Point();
        handler = new Handler();

        seekBar.setOnSeekBarChangeListener(this);
        seekBar1.setOnSeekBarChangeListener(this);

        boolean is_active = true;

        if (bhajans != null) {
            bhajanAdapter = new BhajanAdapter(context, bhajans, imgString, bhajanInterface, this);
            itemPicker.setAdapter(bhajanAdapter);
        }


        if (bhajans1 != null) {
            bhajanAdapter1 = new BhajanAdapter1(context, bhajans1, imgString, bhajanInterface, this);
            itemPicker.setAdapter(bhajanAdapter1);
        }

        if (status.equals("radio")) {
            itemPicker.setVisibility(View.GONE);
            radio_image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(bhajans1[position].getImage())
                    .into(radio_image);
        }
        if (status.equals("bhajan")) {
            itemPicker.setVisibility(View.VISIBLE);
            radio_image.setVisibility(View.GONE);
        }


        //  releatedSongAdapter=new ReleatedSongAdapter(Relatedbhajanlist,context);
        //  PreferencesHelper.getInstance().setValue("index",position);
        //  scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0));
        scrollPlay(position);
        //        bhajanPlayAdapter = new BhajanPlayerView(context, imgString);
//        viewPager.setAdapter(bhajanPlayAdapter);
//
//        viewPager.addOnPageChangeListener(this);
//
//        if (position == 0) {
//            onPageSelected(0);
//        }
//        viewPager.setCurrentItem(position);

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

        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        download.setClickable(true);
        playlist.setClickable(true);
    }

    public void playAudio() throws Exception {
        if (!AudioPlayerService.mediaPlayer.isPlaying()) {
            AudioPlayerService.mediaPlayer.setOnCompletionListener(this);
            AudioPlayerService.mediaPlayer.start();
            playPause.setImageResource(R.mipmap.audio_pause);
            playPause1.setImageResource(R.mipmap.audio_pause);
            ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);

            Constants.CLICK_ACTIVE = "false";

            Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
            Constants.IS_PLAYING = "true";
            intent.putExtra("isplaying", "true");
            getActivity().sendBroadcast(intent);

            if (isConnectingToInternet(context)) {
                // networkCall = new NetworkCall(this, context);
                networkCall.NetworkAPICall(API.RECENT_VIEW, false);
            }
        } else {
            if (Constants.CLICK_ACTIVE.equals("true")) {
                AudioPlayerService.mediaPlayer.pause();
                playPause.setImageResource(R.mipmap.audio_play);
                playPause1.setImageResource(R.mipmap.audio_play);
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
                Constants.CLICK_ACTIVE = "false";

                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                Constants.IS_PLAYING = "false";
                intent.putExtra("isplaying", "false");
                getActivity().sendBroadcast(intent);
            } else {
                playPause.setImageResource(R.mipmap.audio_pause);
                playPause1.setImageResource(R.mipmap.audio_pause);
                ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
                Constants.IS_PLAYING = "false";
            }
        }
        updateProgressBar();
        seekBar.setProgress(0);
        seekBar1.setProgress(0);


        //seekBar.setMax(100);
    }

    boolean isLiked = false;

    public void setBhajanData() throws IOException {

        /*Intent intent=new Intent(getActivity(),AudioService.class);
        intent.setAction(AudioService.ACTION_PLAY);
        intent.putExtra("url",bhajan.getMedia_file());
        intent.putExtra("songname",bhajan.getTitle());
        getActivity().bindService(intent,conn,0);
        getActivity().startService(intent);
        Log.d("startService", "startService");*/
        Bundle bundle = new Bundle();

        /*Intent intent = new Intent( getActivity().getApplicationContext(), MediaPlayerService.class );
        intent.setAction( MediaPlayerService.ACTION_PLAY );
        bundle.putSerializable("audiodetails",bhajan);
        intent.putExtra("url",bhajan.getMedia_file());
        intent.putExtra("songname",bhajan.getTitle());
        getActivity().startService( intent );*/

        //playPause.setVisibility(View.INVISIBLE);
        // playPause1.setVisibility(View.INVISIBLE);
        //progress.show();
        //  playProgress.setVisibility(View.VISIBLE);
        //MediaPlayerService.mMediaPlayer=new MediaPlayer();
//        MediaPlayerService.mMediaPlayer.reset();
        // MediaPlayerService.mMediaPlayer.setDataSource(bhajan.getMedia_file());
        // MediaPlayerService.mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // MediaPlayerService.mMediaPlayer.prepareAsync();

//        if(position == 0){
//            rewind.setVisibility(View.INVISIBLE);
//            forward.setVisibility(View.VISIBLE);
//        }else if(position == (bhajans.length-1)){
//            rewind.setVisibility(View.VISIBLE);
//            forward.setVisibility(View.INVISIBLE);
//        }else{
//            rewind.setVisibility(View.VISIBLE);
//            forward.setVisibility(View.VISIBLE);
//        }


        if (!TextUtils.isEmpty(bhajan.getIs_like()) && bhajan.getIs_like() != null) {
            if (bhajan.getIs_like().equals("1")) {
                like.setImageResource(R.mipmap.audio_liked);
                isLiked = true;
                like.setTag(true);
            } else {
                like.setImageResource(R.mipmap.audio_like);
                isLiked = false;
                like.setTag(false);
            }
        }

        if (position >= 0 && position < bhajans.length) {

            if (bhajans[position].getLikes().equals("0")) {
                if (isLiked) {
                    likeCount.setText("1 Like");
                } else {
                    likeCount.setText("0 Likes");
                }

            } else if (bhajans[position].getLikes().equals("1")) {
                if (isLiked) {
                    likeCount.setText(((Integer.parseInt(bhajans[position].getLikes()) + 1) + " Likes").toString());
                } else {
                    likeCount.setText("1 Like");
                }

            } else {
                if (isLiked) {
                    likeCount.setText(((Integer.parseInt(bhajans[position].getLikes()) + 1) + " Likes").toString());
                } else {
                    likeCount.setText((bhajans[position].getLikes() + " Likes").toString());
                }
            }
        }


        file = new File(getActivity().getExternalFilesDir("bhajanaudio"), bhajan.getTitle() + ".jwpmp3");
        if (file.exists()) {
            download.setImageResource(R.mipmap.downloaded_complete);
            downloadwatch_video.setText("Downloaded");
        } else {
            download.setImageResource(R.mipmap.download_audio);
            downloadwatch_video.setText("Download");
        }

     /*   boolean isInserted= databaseManager.addclient(new PlayListModel(bhajan.getTitle(),bhajan.getMedia_file(),
                bhajan.getImage()));

        if(isInserted){
            views_number_watch_video.setText("Success");
            playlist.setImageResource(R.mipmap.added_to_playlist);


        }else {
            views_number_watch_video.setText("Add to playlist");
            playlist.setImageResource(R.mipmap.add_playlist);

        }*/


        bhajanName.setText(bhajans[position].getTitle());
        artistName.setText(bhajans[position].getArtist_name());
        bhajanTitle1.setText(bhajans[position].getTitle());
        bhajanArtistName.setText(bhajans[position].getArtist_name());
        bhajanArtistName1.setText(bhajans[position].getArtist_name());

        playPause.setImageResource(R.mipmap.audio_pause);
        playPause1.setImageResource(R.mipmap.audio_pause);
        ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);


    }

    public void setBhajanData1() throws IOException {
        if (!TextUtils.isEmpty(bhajan1.getIs_like()) && bhajan1.getIs_like() != null) {
            if (bhajan1.getIs_like().equals("1")) {
                like.setImageResource(R.mipmap.audio_liked);
                isLiked = true;
                like.setTag(true);
            } else {
                like.setImageResource(R.mipmap.audio_like);
                isLiked = false;
                like.setTag(false);
            }
        }

        if (position >= 0 && position < bhajans1.length) {

            if (bhajans1[position].getLikes().equals("0")) {
                if (isLiked) {
                    likeCount.setText("1 Like");
                } else {
                    likeCount.setText("0 Likes");
                }

            } else if (bhajans1[position].getLikes().equals("1")) {
                if (isLiked) {
                    likeCount.setText(((Integer.parseInt(bhajans1[position].getLikes()) + 1) + " Likes").toString());
                } else {
                    likeCount.setText("1 Like");
                }

            } else {
                if (isLiked) {
                    likeCount.setText(((Integer.parseInt(bhajans1[position].getLikes()) + 1) + " Likes").toString());
                } else {
                    likeCount.setText((bhajans1[position].getLikes() + " Likes").toString());
                }
            }
        }


        file = new File(getActivity().getExternalFilesDir("bhajanaudio"), bhajan1.getTitle() + ".jwpmp3");
        if (file.exists()) {
            download.setImageResource(R.mipmap.downloaded_complete);
            downloadwatch_video.setText("Downloaded");
        } else {
            download.setImageResource(R.mipmap.download_audio);
            downloadwatch_video.setText("Download");
        }

        if (bhajans1[position].getIs_audio_playlist_exist() != null) {
            if (bhajans1[position].getIs_audio_playlist_exist().equalsIgnoreCase("1")) {
                views_number_watch_video.setText("Success");
                playlist.setImageResource(R.mipmap.added_to_playlist);
            } else {
                views_number_watch_video.setText("Add to playlist");
                playlist.setImageResource(R.mipmap.add_playlist);
            }
        }


        if (status.equals("bhajan")) {
            bhajanName.setText(bhajans1[position].getTitle());
            artistName.setText(bhajans1[position].getArtistName());
            bhajanTitle1.setText(bhajans1[position].getTitle());
            bhajanArtistName.setText(bhajans1[position].getArtistName());
            bhajanArtistName1.setText(bhajans1[position].getArtistName());

        }

        if (status.equals("radio")) {
            bhajanName.setText("Radio Satsang");
            artistName.setText("Sanskar");
            bhajanTitle.setText("Radio Satsang");
            bhajanTitle1.setText("Radio Satsang");
            bhajanArtistName.setText("Sanskar");
            bhajanArtistName1.setText("Sanskar");

        }


        playPause.setImageResource(R.mipmap.audio_pause);
        playPause1.setImageResource(R.mipmap.audio_pause);
        ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);


    }

    public void killMediaPlayer() {
        handler.removeCallbacks(mUpdateTimeTask);
        if (AudioPlayerService.mediaPlayer != null) {
            try {
                AudioPlayerService.mediaPlayer.reset();
                // AudioService.mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 1000);
    }


    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public void forwardRewindPlaySongs() {

        killMediaPlayer();

        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
            bhajan = bhajans[position];

        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
            bhajan = ((HomeActivityy) context).bhajanList.get(position);
        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
            bhajan = bhajanList.get(position);
        }
        try {
            setBhajanData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Constants.CLICK_ACTIVE_FORWARD.equals("true")) {
            Constants.CLICK_ACTIVE_FORWARD = "false";
//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) + 1);
            position += 1;
            scrollPlay(position);
        } else if (Constants.CLICK_ACTIVE_REWIND.equals("true")) {
            Constants.CLICK_ACTIVE_REWIND = "false";
            if (position == 0) {
                position = bhajans.length - 1;
            } else {
                position -= 1;
            }
//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) - 1);
            scrollPlay(position);
        }
    }

    public void forwardRewindPlaySongs1() {
        killMediaPlayer();
        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
            bhajan1 = bhajans1[position];

        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
            bhajan = ((HomeActivityy) context).bhajanList.get(position);
        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
            bhajan1 = bhajanList1.get(position);
        }
        try {
            setBhajanData1();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Constants.CLICK_ACTIVE_FORWARD.equals("true")) {
            Constants.CLICK_ACTIVE_FORWARD = "false";
//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) + 1);
            position += 1;
            scrollPlay(position);
        } else if (Constants.CLICK_ACTIVE_REWIND.equals("true")) {
            Constants.CLICK_ACTIVE_REWIND = "false";
            if (position == 0) {
                position = bhajans1.length - 1;
            } else {
                position -= 1;
            }


//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) - 1);
            scrollPlay(position);
        }
    }

    private void LikeDislike(boolean b) {
        // like.setTag(b);
        rl_like_bhajan.setTag(b);

        if (bhajans != null) {
            int likenum = Integer.parseInt(bhajan.getLikes());
            if (b) {
                like.setImageResource(R.mipmap.audio_liked);
                bhajan.setIs_like(String.valueOf(likenum + 1));
                bhajan.setIs_like("1");
            } else {
                like.setImageResource(R.mipmap.audio_like);
                bhajan.setLikes(String.valueOf(likenum));
                bhajan.setIs_like("0");
            }
            if (audioType == Constants.GURU_AUDIO_TYPE) {
                ((HomeActivityy) context).bhajanList.set(position, bhajan);
            } else if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
                //((HomeActivityy)context).bhajanResponseArrayList.set(position, bhajans[position]);
            }
        }
        if (bhajans1 != null) {
            int likenum = Integer.parseInt(bhajan1.getLikes());
            if (b) {
                like.setImageResource(R.mipmap.audio_liked);
                bhajan1.setIs_like(String.valueOf(likenum + 1));
                bhajan1.setIs_like("1");
            } else {
                like.setImageResource(R.mipmap.audio_like);
                bhajan1.setLikes(String.valueOf(likenum));
                bhajan1.setIs_like("0");
            }
            if (audioType == Constants.GURU_AUDIO_TYPE) {
                ((HomeActivityy) context).bhajanList.set(position, bhajan);
            } else if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
                //((HomeActivityy)context).bhajanResponseArrayList.set(position, bhajans[position]);
            }
        }


    }

    public void shareData() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT, bhajan.getMedia_file());
        startActivity(Intent.createChooser(share, "Share url!"));
    }

    public void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, bhajan.getMedia_file());
        share.putExtra(Intent.EXTRA_SUBJECT, bhajan.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, bhajan.getArtist_image());

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    List<Bhajan> playListItems = new ArrayList<>();

    public void addPlayListData() {

        String bhajanString = PreferencesHelper
                .getInstance()
                .getBhajanString(Constants.MY_PLAY_LIST);
//        List<BhajanCat> bhajanList = new ArrayList<>();
        if (!TextUtils.isEmpty(bhajanString)) {


            try {
//                JSONArray jsonArray = new JSONArray(bhajanString);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.optJSONObject(i);
//                    BhajanCat bhaja = new Gson().fromJson(jsonObject.toString(), BhajanCat.class);
//                    bhajanList.add(bhaja);

                JSONArray jsonArray = new JSONArray(bhajanString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    Bhajan bhaja = new Gson().fromJson(jsonObject.toString(), Bhajan.class);
                    if (playListItems.isEmpty()) {
                        playListItems.add(bhaja);
                        Toast.makeText(context, "Added to Playlist", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean bhajaAdded = true;
                        for (int j = 0; j < playListItems.size(); j++) {
                            if (bhaja.getId().equals(playListItems.get(j).getId())) {

                                bhajaAdded = false;

                                Toast.makeText(context, "Already added in Playlist", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if (bhajaAdded) {
                            playListItems.add(bhaja);
                            Toast.makeText(context, "Added to Playlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            boolean bhajanAlreadyAdded = false;
//            //for (int i = bhajanList.size(); i > 1; i--)
//            for (int i = 0; i < bhajanList.size(); i++) {
//                if (bhajanList.get(position).getId().equals(bhajan.getId())) {
//                    bhajanAlreadyAdded = true;
//                    break;
//                }
//            }
//            if (!bhajanAlreadyAdded) {
//                bhajanList.add(bhajan);
//            }
        } else {
            playListItems.add(bhajan);
            Toast.makeText(context, "Added to Playlist", Toast.LENGTH_SHORT).show();
        }

        PreferencesHelper
                .getInstance()
                .setObject(Constants.MY_PLAY_LIST, playListItems);

    }

    private void backwardSong() {

        // if ((bhajans.length - 1) > position) {
        /*if (position > 0) {
           // position = position - 1;
        } else {
            position = 0;
        }
        forwardRewindPlaySongs();
        Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
        Constants.REVERSE_SONG = "true";
        getActivity().sendBroadcast(intent);*/

        if (Constants.AUDIO_INDEX > 0) {
            Constants.AUDIO_INDEX = Constants.AUDIO_INDEX - 1;
        } else {
            Constants.AUDIO_INDEX = 0;
        }

        if (bhajans != null) {
            forwardRewindPlaySongs();
        }

        if (bhajans1 != null) {
            forwardRewindPlaySongs1();
        }

        Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);

        Constants.REVERSE_SONG = "true";

        getActivity().sendBroadcast(intent);

    }

    private void forwardBhajanSong() {
        if (bhajans != null) {
            if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
                if ((bhajans.length - 1) > position) {
                    position = position;

                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }


               /* Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                getActivity().sendBroadcast(intent);*/

                    //   Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                    Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                    // intent.putExtra("bhajanlist",bhajans);
                    Constants.FORWARD_SONG = "true";
                    //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                    getActivity().sendBroadcast(intent);

                } else {
                    position = bhajans.length - 1;
                    isImgBTnClick = false;
                }
            } else if (audioType == Constants.GURU_AUDIO_TYPE) {
                if (((((HomeActivityy) context).bhajanList).size() - 1) > position) {
                    position = position + 1;
                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }

                } else {
                    isImgBTnClick = false;
                }
            } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
                if ((bhajanList.size() - 1) > position) {
                    position = position + 1;
                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }

                } else {
                    isImgBTnClick = false;
                }
            }
        }
        if (bhajans1 != null) {
            if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
                if ((bhajans1.length - 1) > position) {
                    position = position;
                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }


               /* Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                getActivity().sendBroadcast(intent);*/

                    //   Constants.AUDIO_INDEX = Constants.AUDIO_INDEX + 1;

                    Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                    // intent.putExtra("bhajanlist",bhajans);
                    Constants.FORWARD_SONG = "true";
                    //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                    getActivity().sendBroadcast(intent);

                } else {
                    position = bhajans1.length - 1;
                    isImgBTnClick = false;
                }
            } else if (audioType == Constants.GURU_AUDIO_TYPE) {
                if (((((HomeActivityy) context).bhajanList).size() - 1) > position) {
                    position = position + 1;
                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }

                } else {
                    isImgBTnClick = false;
                }
            } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
                if ((bhajanList1.size() - 1) > position) {
                    position = position + 1;
                    if (bhajans != null) {
                        forwardRewindPlaySongs();
                    }
                    if (bhajans1 != null) {
                        forwardRewindPlaySongs1();
                    }

                } else {
                    isImgBTnClick = false;
                }
            }
        }

    }

    public void fastforwardSong() {
        if (AudioPlayerService.mediaPlayer != null) {
            int currentPosition = AudioPlayerService.mediaPlayer.getCurrentPosition();
            //int currentPosition = AudioPlayerService.mediaPlayer.getCurrentPosition();
            if (currentPosition + seekForwardTime <= AudioPlayerService.mediaPlayer.getDuration()) {
                AudioPlayerService.mediaPlayer.seekTo(currentPosition + seekForwardTime);
            } else {
                AudioPlayerService.mediaPlayer.seekTo(AudioPlayerService.mediaPlayer.getDuration());
            }
        }
    }

    public void fastRewindSong() {
        if (AudioPlayerService.mediaPlayer != null) {
            int currentPosition = AudioPlayerService.mediaPlayer.getCurrentPosition();
            if (currentPosition - seekBackwardTime >= 0) {
                AudioPlayerService.mediaPlayer.seekTo(currentPosition - seekBackwardTime);
            } else {
                AudioPlayerService.mediaPlayer.seekTo(0);
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int pos) {
//        if (!isImgBTnClick) {
//            if (firstTimeSelected) {
//                try {
//                    setBhajanData();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                firstTimeSelected = false;
//            } else {
//
//                if (tabPos < pos) {
//                    forwardBhajanSong();
//                } else {
//                    if (position > 0) {
//                        position = position - 1;
//                        forwardRewindPlaySongs();
//                    }
//                }
//            }
//        }
//        isImgBTnClick = false;
//        tabPos = pos;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public void getBhajanRelatedAudios() {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.BHAJAN_RELATED, false);
        } else {
            ToastUtil.showDialogBox(context, getString(R.string.no_internet));
        }
    }

    public void setBhajanRelatedData(JSONArray data) {
        if (data != null) {
            relatedBhajans = new Bhajan[data.length()];
            for (int i = 0; i < data.length(); i++) {
                relatedBhajans[i] = new Gson().fromJson(data.opt(i).toString(), Bhajan.class);
            }
            adapter = new BhajanViewAllAdapter(relatedBhajans, context, false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            relatedRV.setLayoutManager(layoutManager);
            relatedRV.setAdapter(adapter);
            ViewCompat.setNestedScrollingEnabled(relatedRV, false);
        }
    }

    private void beginDownload() {
        Constants.DOWNLOAD_ACTIVE = "true";
        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;


                File file = new File(context.getExternalFilesDir("bhajanaudio"), bhajan.getTitle() + ".jwpmp3");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(bhajan.getMedia_file()))
                        .setTitle(bhajan.getArtist_name())// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                        //.setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network


                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadID); //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circularProgressBar.setProgress((int) dl_progress * 2);
                        }
                    });

                    // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();
                }

            }
        }).start();

    }

    private void beginDownload1() {
        Constants.DOWNLOAD_ACTIVE = "true";
        new Thread(new Runnable() {

            @Override
            public void run() {

                boolean downloading = true;


                File file = new File(context.getExternalFilesDir("bhajanaudio"), bhajan1.getTitle() + ".jwpmp3");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(bhajan1.getMediaFile()))
                        .setTitle(bhajan1.getArtistName())// Title of the Download Notification
                        .setDescription("Downloading")// Description of the Download Notification
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                        .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                        //.setRequiresCharging(false)// Set if charging is required to begin the download
                        .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                        .setAllowedOverRoaming(true);// Set if download is allowed on roaming network


                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

                while (downloading) {

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadID); //filter by id which you have receieved when reqesting download from download manager
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }

                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circularProgressBar.setProgress((int) dl_progress * 2);
                        }
                    });

                    // Log.d(Constants.MAIN_VIEW_ACTIVITY, statusMessage(cursor));
                    cursor.close();
                }

            }
        }).start();

    }

    public void onItemChanged() {
        if (bhajans != null) {
            bhajanTitle.setText(bhajans[position].getTitle());
            artistName.setText(bhajans[position].getArtist_name());
            bhajanTitle1.setText(bhajans[position].getTitle());
            bhajanArtistName.setText(bhajans[position].getArtist_name());
            bhajanArtistName1.setText(bhajans[position].getArtist_name());

        }
        if (bhajans1 != null) {
            bhajanTitle.setText(bhajans1[position].getTitle());
            artistName.setText(bhajans1[position].getArtistName());
            bhajanTitle1.setText(bhajans1[position].getTitle());
            bhajanArtistName.setText(bhajans1[position].getArtistName());
            bhajanArtistName1.setText(bhajans1[position].getArtistName());

        }

//        currentItemName.setText(item.getName());
//        currentItemPrice.setText(item.getPrice());
//        changeRateButtonState(item);
    }

    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = 0;
        if (bhajans != null) {
            positionInDataSet = (int) bhajanAdapter.getItemId(position);
        }
        if (bhajans1 != null) {
            positionInDataSet = (int) bhajanAdapter1.getItemId(position);
        }

        // onItemChanged(bhajanList.get(positionInDataSet));
//        if (!isImgBTnClick) {
//            if (firstTimeSelected) {
//                try {
//                    setBhajanData();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                firstTimeSelected = false;
//            } else {
//
//                if (tabPos < adapterPosition) {
//                    forwardBhajanSong();
//                } else {
//                    if (position > 0) {
//                        position = position - 1;
//                        forwardRewindPlaySongs();
//                    }
//                }
//            }
//        }
//        isImgBTnClick = false;
//        tabPos = adapterPosition;
//
//       onItemChanged();

    }

    @Override
    public void BhajanClicked(int pos) {
        position = pos;
        Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
    }

    public void scrollPlay(int position) {
        this.position = position;
        try {
            if (bhajans != null) {
                if (position >= 0 && bhajans.length > position) {
                    bhajan = bhajans[position];
                }
            }
            if (bhajans1 != null) {
                if (position >= 0 && bhajans1.length > position) {
                    bhajan1 = bhajans1[position];
                }
            }
            if (bhajans != null) {
                setBhajanData();
            }
            if (bhajans1 != null) {
                setBhajanData1();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        onItemChanged();
        itemPicker.scrollToPosition(position);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //playPause.setImageResource(R.mipmap.audio_play);
        //playPause1.setImageResource(R.mipmap.audio_playz);
    }

    public void artistImage() {
        if (bhajans != null) {
            Ion.with(context).load(bhajanList.get(position).getArtist_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        artistImage.setImageBitmap(result);
                    } else {
                        artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
                    }
                }
            });
        }
        if (bhajans1 != null) {
            Ion.with(context).load(bhajanList1.get(position).getArtistImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        artistImage.setImageBitmap(result);
                    } else {
                        artistImage.setImageResource(R.mipmap.thumbnail_placeholder);
                    }
                }
            });
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //getActivity().unbindService(conn);
    }

    @Override
    public void onStart() {
        super.onStart();
       /* if (audioService!=null){
            audioService.setListener(null);
        }*/
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        playProgress.setSecondaryProgress(i);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//        Toast.makeText(getActivity(), "error playing audio", Toast.LENGTH_SHORT).show();
        return false;
    }


    private void pop_dialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_song_list);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));  // backgroun transperent hua
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);// set height width of dialog
        // dialog.getWindow().setGravity();

        dialog.show();


    }


}
