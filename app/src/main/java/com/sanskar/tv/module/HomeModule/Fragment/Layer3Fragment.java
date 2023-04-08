package com.sanskar.tv.module.HomeModule.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
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
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.AES;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;


import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.EventLogger;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.PremiumEpisodeAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.PremiumSeriesAdapter2;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.SeasonDetail;
import com.sanskar.tv.module.TrackSelectionHelper;
import com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.utility.YoutubeExtractor.ExtractorException;
import com.sanskar.tv.utility.YoutubeExtractor.YoutubeStreamExtractor;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTMedia;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTSubtitles;
import com.sanskar.tv.utility.YoutubeExtractor.model.YoutubeMeta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

public class Layer3Fragment extends Fragment implements NetworkCall.MyNetworkCallBack {
    MenuMasterList menuMasterList, menuMasterList1;
    List<MenuMasterList> seasonDetails;
    List<MenuMasterList> menuMasterLists = new ArrayList<>();
    ImageView imageView, mute_on;
    TextView video_title, video_desc, text;
    AppCompatButton btn_premium, btn_view_all;
    RecyclerView recyclerView,moreLikeRV;
    public PlayerView playerView;
    public SimpleExoPlayer simpleExoPlayer;
    DefaultHttpDataSourceFactory factory;
    DataSource.Factory dataSourceFactory;
    TrackSelector trackSelector;
    DefaultTrackSelector selector;
    DefaultTrackSelector.Parameters trackParameters;
    TrackSelectionHelper trackSelectionHelper;
    private TrackGroupArray lastSeenTrackGroupArray;
    EventLogger eventLogger;
    TextView quality;
    BandwidthMeter bandwidthMeter;
    public ImageView full_Screen, option_menu;
    LoadControl loadControl;
    ExtractorsFactory extractorsFactory;
    ProgressBar progress_bar, progress_bar_1;
    boolean tag = false, flag = false;
    RelativeLayout video_basic, episodesRL,moreLikeRL;
    String youtubeLink;
    String BASE_URL = "https://www.youtube.com";
    NetworkCall networkCall;
    HomeActivityy homeActivityy;
    String season_id = "";
    String episodes_id = "";
    String pause_at = "";
    String pause_at1 = "";
    RelativeLayout about_rl;
    TextView title,description, moreLikeTV, episodeTV;
    String status = "";
    String token = "";
    boolean isPlaying = true;
    boolean isPlayBackState = true;
    public static int is_premium = -1;

    PremiumEpisodeAdapter premiumEpisodeAdapter;
    LinearLayoutManager linearLayoutManager;
    String category = "";
    Context context;

    public int indexOfRunningEpisodes =0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_layer3, container, false);


        getBundleData();
        initViews(view);
        List<MenuMasterList> moreLikeList = new ArrayList<>();

        if (!category.equalsIgnoreCase("episodes")){
            for (MenuMasterList menuMaster : seasonDetails){
                if (!menuMaster.getSeason_id().equalsIgnoreCase(menuMasterList.getSeason_id())) moreLikeList.add(menuMaster);
            }
        }

        if (moreLikeList.size() > 0){
            moreLikeRL.setVisibility(View.VISIBLE);
        } else {
            moreLikeRL.setVisibility(View.GONE);
        }

        moreLikeRV.setAdapter(new PremiumSeriesAdapter2(context,seasonDetails,moreLikeList,true));

        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );

        factory = new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );

        extractorsFactory = new DefaultExtractorsFactory();
        trackParameters = new DefaultTrackSelector.ParametersBuilder().build();

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(defaultBandwidthMeter);
        selector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        selector.setParameters(trackParameters);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                context, selector, loadControl
        );

        trackSelectionHelper = new TrackSelectionHelper(selector, adaptiveTrackSelectionFactory);
        dataSourceFactory = new DefaultDataSourceFactory(context, defaultBandwidthMeter, buildHttpDataSourceFactory(defaultBandwidthMeter));
        lastSeenTrackGroupArray = null;
        eventLogger = new EventLogger(selector);
        setVideoData(menuMasterList, category);
        fetchData();


        return view;
    }

    public void fetchMetaData(){
        networkCall = new NetworkCall(this,context);
        networkCall.NetworkAPICall(API.GET_PREMIUM_VIDEO_META_DATA,true);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory("exoplayer_video", bandwidthMeter);
    }
    private void fetchData() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_EPISODES_LIST_BY_SEASON_ID, false);
        } else {
            ToastUtil.showDialogBox(context, "NO Internet Connection");
        }

        playerView.setOnDragListener((v, event) -> false);

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ((HomeActivityy) context).toolbar.setVisibility(View.GONE);
            ((HomeActivityy) context).bottomNavigationView.setVisibility(View.GONE);
            video_basic.setVisibility(View.GONE);
            episodesRL.setVisibility(View.GONE);
            // getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            //First Hide other objects (listview or recyclerview), better hide them using Gone.
            //  swipeRefreshLayout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            playerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //unhide your objects here.
            // swipeRefreshLayout.setVisibility(View.VISIBLE);
            video_basic.setVisibility(View.VISIBLE);
            episodesRL.setVisibility(View.VISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((HomeActivityy) context).toolbar.setVisibility(View.VISIBLE);
            ((HomeActivityy) context).bottomNavigationView.setVisibility(View.VISIBLE);
            //   getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) (250 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
        }
    }

    public void setOrientation() {
        full_Screen.setImageDrawable(getResources()
                .getDrawable(R.drawable.ic_fullscreen_exit));

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((HomeActivityy) context).toolbar.setVisibility(View.GONE);
        HomeActivityy.bottomNavigationView.setVisibility(View.GONE);

        ((HomeActivityy)context).isInFullScreen = true;

        video_basic.setVisibility(View.GONE);
        episodesRL.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        flag = true;
    }

    public void changeOrientation(){
        full_Screen.setImageDrawable(getResources()
                .getDrawable(R.drawable.ic_full));

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((HomeActivityy) context).toolbar.setVisibility(View.VISIBLE);
        HomeActivityy.bottomNavigationView.setVisibility(View.VISIBLE);

        ((HomeActivityy)context).isInFullScreen = false;

        video_basic.setVisibility(View.VISIBLE);
        episodesRL.setVisibility(View.VISIBLE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        flag = true;
    }


    public void setVideoData(MenuMasterList menuMasterList, String category) {
        season_id = menuMasterList.getSeason_id();
        if (menuMasterList.getDescription() != null) {
            text.setText(Html.fromHtml(menuMasterList.getDescription()));
          //  title.setText(menuMasterLists.get(indexOfRunningEpisodes).getEpisode_title());
            description.setText(Html.fromHtml(menuMasterList.getDescription()));
        }
        if (menuMasterList.getEpisode_description() != null) {
            text.setText(Html.fromHtml(menuMasterList.getEpisode_description()));
           // title.setText(menuMasterLists.get(indexOfRunningEpisodes).getEpisode_title());
            description.setText(Html.fromHtml(menuMasterList.getEpisode_description()));
        }
        if (menuMasterList.getEpisode_id() != null) {
            episodes_id = menuMasterList.getEpisode_id();
        } else {
            episodes_id = "";
        }
        if (menuMasterList.getSeason_title() != null) {
            title.setText(menuMasterList.getSeason_title());
            video_title.setText(menuMasterList.getSeason_title());
        }
        if (menuMasterList.getEpisode_title() != null) {
            title.setText(menuMasterList.getEpisode_title());
            video_title.setText(menuMasterList.getEpisode_title());
        }

        if (menuMasterList.getPromo_video() != null && !TextUtils.isEmpty(menuMasterList.getPromo_video())) {
            //   ((HomeActivityy)context).progress.hide();
            playVideo(Uri.parse(menuMasterList.getPromo_video()),menuMasterList.getPause_at(),0);

        }


        if (menuMasterList.getToken() != null && !TextUtils.isEmpty(menuMasterList.getToken())) {
            //   ((HomeActivityy)context).progress.hide();

            /*String url = AES.decrypt(menuMasterList.getEncrypted_url(),AES.generateKeyNew(menuMasterList.getToken()),AES.generateVectorNew(menuMasterList.getToken()));
            Log.e("shantanu",url);
            playVideo(Uri.parse(url),menuMasterList.getPause_at(),1);*/

            token = menuMasterList.getToken();
            fetchMetaData();

        }


        if (menuMasterList.getEpisode_url() != null && !TextUtils.isEmpty(menuMasterList.getEpisode_url())) {
            //   ((HomeActivityy)context).progress.hide();
            playVideo(Uri.parse(menuMasterList.getEpisode_url()),menuMasterList.getPause_at(),1);
        }


        if (menuMasterList.getCustom_promo_video() != null && !TextUtils.isEmpty(menuMasterList.getCustom_promo_video())){
            playVideo(Uri.parse(menuMasterList.getCustom_promo_video()),menuMasterList.getPause_at(),0);
        }
    }

    private void initViews(View view) {
        imageView = view.findViewById(R.id.imageView);
        video_title = view.findViewById(R.id.video_title);
        //video_desc = view.findViewById(R.id.video_desc);
        text = view.findViewById(R.id.text);
        btn_premium = view.findViewById(R.id.btn_premium);
        moreLikeRL = view.findViewById(R.id.moreLikeRL);

        recyclerView = view.findViewById(R.id.recyclerView);
        description = view.findViewById(R.id.description);
        episodeTV = view.findViewById(R.id.episodeTV);
        moreLikeTV = view.findViewById(R.id.moreLikeTV);
        title = view.findViewById(R.id.title);
        about_rl = view.findViewById(R.id.about_rl);
        moreLikeRV = view.findViewById(R.id.moreLikeRV);
        moreLikeRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        btn_view_all = view.findViewById(R.id.btn_view_all);

        playerView = view.findViewById(R.id.player_view);
        progress_bar = view.findViewById(R.id.progress_bar);
        mute_on = playerView.findViewById(R.id.mute_on);
        full_Screen = view.findViewById(R.id.bt_full);
        option_menu = view.findViewById(R.id.option_menu);
        episodesRL = view.findViewById(R.id.episodesRL);
        video_basic = view.findViewById(R.id.video_basic);
        progress_bar_1 = view.findViewById(R.id.progress_bar_1);
        progress_bar_1.setVisibility(View.VISIBLE);
        quality = view.findViewById(R.id.quality);



        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        btn_premium.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentActivity.class);
            startActivity(intent);
        });
        btn_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((HomeActivityy) context).Video_id = season_id;
                ((HomeActivityy) context).Category = episodes_id;
                ((HomeActivityy) context).Category_name = menuMasterList.getCat_name();
                ((HomeActivityy) context).showPremiumEpisodeFrag();
            }
        });

        switch (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK){
            case Configuration.UI_MODE_NIGHT_YES:
                episodeTV.setTextColor(Color.parseColor("#FFFFFF"));
                moreLikeTV.setTextColor(Color.parseColor("#FFFFFF"));
                video_title.setTextColor(Color.parseColor("#FFFFFF"));
                text.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                episodeTV.setTextColor(Color.parseColor("#000000"));
                moreLikeTV.setTextColor(Color.parseColor("#000000"));
                video_title.setTextColor(Color.parseColor("#000000"));
                text.setTextColor(Color.parseColor("#000000"));
                break;
        }

    }


    private void playVideo(Uri videourl,String pause,int type) {
        // Uri videourl = Uri.parse(video_url);
        isPlayBackState = true;
        if (type == 0){
            MediaSource mediaSource = new ExtractorMediaSource(videourl
                    , factory, extractorsFactory, null, null);
            simpleExoPlayer.prepare(mediaSource);

        }else{
            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(videourl);

            simpleExoPlayer.prepare(hlsMediaSource);
        }
        playerView.setPlayer(simpleExoPlayer);

        //keep screen on
        playerView.setKeepScreenOn(true);
        playerView.setFocusable(true);

        simpleExoPlayer.setVolume(1f);


        simpleExoPlayer.setPlayWhenReady(isPlaying);
        if (pause!=null&&!pause.equalsIgnoreCase("")){
            simpleExoPlayer.seekTo(Long.parseLong(pause));
        }
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                if (trackGroups != lastSeenTrackGroupArray) {
                    MappingTrackSelector.MappedTrackInfo mappedTrackInfo = selector.getCurrentMappedTrackInfo();
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
                if (playWhenReady) {
                    progress_bar.setVisibility(View.GONE);
                }
                if (playbackState == Player.STATE_BUFFERING) {
                    progress_bar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    progress_bar.setVisibility(View.GONE);
                } else if (playbackState == Player.STATE_ENDED) {
                    status = String.valueOf(1);
                    if (indexOfRunningEpisodes!= -1 && menuMasterLists.get(indexOfRunningEpisodes).getIs_locked().equalsIgnoreCase("0") && isPlayBackState){
                        isPlayBackState = false;
                        if (menuMasterLists.get(indexOfRunningEpisodes).getDescription() != null)
                            text.setText(Html.fromHtml(menuMasterLists.get(indexOfRunningEpisodes).getDescription()));
                        if (menuMasterLists.get(indexOfRunningEpisodes).getEpisode_description() != null)
                            text.setText(Html.fromHtml(menuMasterLists.get(indexOfRunningEpisodes).getEpisode_description()));
                        if (menuMasterLists.get(indexOfRunningEpisodes).getEpisode_id() != null) {
                            episodes_id = menuMasterLists.get(indexOfRunningEpisodes).getEpisode_id();
                        } else {
                            episodes_id = "";
                        }
                        if (menuMasterLists.get(indexOfRunningEpisodes).getSeason_title() != null) {
                            video_title.setText(menuMasterLists.get(indexOfRunningEpisodes).getSeason_title());
                        }
                        if (menuMasterLists.get(indexOfRunningEpisodes).getEpisode_title() != null) {
                            video_title.setText(menuMasterLists.get(indexOfRunningEpisodes).getEpisode_title());
                        }
                        if (menuMasterLists.get(indexOfRunningEpisodes).getToken() != null && !TextUtils.isEmpty(menuMasterLists.get(indexOfRunningEpisodes).getToken())) {
                            token = menuMasterLists.get(indexOfRunningEpisodes).getToken();
                            fetchMetaData();
                        }

                        for (int i =0; i<menuMasterLists.size();i++){
                            menuMasterLists.get(i).setIs_now_playing(i == indexOfRunningEpisodes);
                        }

                        premiumEpisodeAdapter = new PremiumEpisodeAdapter(context, menuMasterLists, Layer3Fragment.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(premiumEpisodeAdapter);

                        indexOfRunningEpisodes = indexOfRunningEpisodes<menuMasterLists.size()-1? indexOfRunningEpisodes+1 : -1;
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

        mute_on.setOnClickListener(v -> {
            if (!tag) {
                mute_on.setImageResource(R.drawable.mute_on);
                simpleExoPlayer.setVolume(0f);
                tag = true;
            } else {
                mute_on.setImageResource(R.drawable.mute_off);
                simpleExoPlayer.setVolume(1f);
                tag = false;
            }
        });
        full_Screen.setOnClickListener(view -> {
            if (flag) {
                full_Screen.setImageDrawable(getResources().getDrawable(R.drawable.ic_full));
                //set portrait orientation
                video_basic.setVisibility(View.VISIBLE);
                episodesRL.setVisibility(View.VISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ((HomeActivityy) context).toolbar.setVisibility(View.VISIBLE);
                ((HomeActivityy) context).bottomNavigationView.setVisibility(View.VISIBLE);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                about_rl.setVisibility(View.GONE);
                flag = false;
            } else {
                full_Screen.setImageDrawable(getResources()
                        .getDrawable(R.drawable.ic_fullscreen_exit));
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                ((HomeActivityy) context).toolbar.setVisibility(View.GONE);
                ((HomeActivityy) context).bottomNavigationView.setVisibility(View.GONE);
                video_basic.setVisibility(View.GONE);
                episodesRL.setVisibility(View.GONE);
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                about_rl.setVisibility(View.VISIBLE);

                flag = true;
            }
        });

        quality.setOnClickListener(v -> {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = selector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                trackSelectionHelper.showSelectionDialog(context, "Quality",
                        selector.getCurrentMappedTrackInfo(), 0);
            }
        });

        boolean[] checkedItems = {false, true, false, false, false, false};
        option_menu.setOnClickListener(v -> {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
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

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(false);

        pause_at = String.valueOf(simpleExoPlayer.getCurrentPosition());

        if (status.equalsIgnoreCase("1")) {
            status = "1";
        } else {
            status = "0";
        }
        isPlaying = false;
        CONTINUE_WATCHING(false);
    }

    public void CONTINUE_WATCHING(boolean b) {
        if (GoLiveActivity.isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.CONTINUE_WATCHING, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchData();
        isPlaying = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(false);
        simpleExoPlayer.release();
        pause_at = String.valueOf(simpleExoPlayer.getCurrentPosition());

    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle.containsKey("data")) {
            menuMasterList = (MenuMasterList) bundle.getSerializable("data");
        }
        if (bundle.containsKey("dataMain")) {
            seasonDetails = (List<MenuMasterList>) bundle.getSerializable("dataMain");
        }
        if (bundle.containsKey("type")) {
            category = bundle.getString("type");
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        if (apitype.equalsIgnoreCase(API.GET_EPISODES_LIST_BY_SEASON_ID)) {
            ion = (Builders.Any.B) Ion.with(this)
                    .load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("season_id", season_id)
                    .setMultipartParameter("page_no", String.valueOf(1))
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("episode_id", episodes_id);
        }
        if (apitype.equalsIgnoreCase(API.GET_PREMIUM_VIDEO_META_DATA)) {
            ion = (Builders.Any.B) Ion.with(this)
                    .load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("token", token);
        }
        if (apitype.equalsIgnoreCase(API.CONTINUE_WATCHING)) {
            ion = (Builders.Any.B) Ion.with(this)
                    .load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("type", String.valueOf(2))
                    .setMultipartParameter("season_id", season_id)
                    .setMultipartParameter("media_id", episodes_id)
                    .setMultipartParameter("pause_at", pause_at)
                    .setMultipartParameter("status", status);
        }
        return ion;
    }


    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.GET_EPISODES_LIST_BY_SEASON_ID)) {
            progress_bar_1.setVisibility(View.GONE);
            if (jsonstring.optBoolean("status")) {
                if (menuMasterLists.size() > 0) {
                    menuMasterLists.clear();
                }

                int is_premium = jsonstring.optInt("is_premium");
                if (is_premium == 0) {
                    btn_premium.setVisibility(View.VISIBLE);
                } else {
                    btn_premium.setVisibility(View.GONE);
                }

                //SharedPreference.getInstance(context).putString(Constants.IS_PREMIUM, String.valueOf(is_premium));

                JSONArray jsonArray = jsonstring.optJSONArray("data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        menuMasterList1 = new Gson().fromJson(jsonArray.opt(i).toString(), MenuMasterList.class);
                        menuMasterLists.add(menuMasterList1);
                    }

                    premiumEpisodeAdapter = new PremiumEpisodeAdapter(context, menuMasterLists, Layer3Fragment.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(premiumEpisodeAdapter);
                    premiumEpisodeAdapter.notifyDataSetChanged();

                }
            } else {
                ToastUtil.showDialogBox(context, jsonstring.optString("message"));
            }
        }
        if (apitype.equalsIgnoreCase(API.GET_PREMIUM_VIDEO_META_DATA)){
            if (jsonstring.optBoolean("status")){
                String url = AES.decrypt(jsonstring.optJSONObject("data").optString("encrypted_url"),AES.generateKeyNew(token),AES.generateVectorNew(token));
                Log.e("shantanu","decrypted url:  "+url);
                playVideo(Uri.parse(url),"0",1);
            }else{
                ToastUtil.showDialogBox(context, jsonstring.optString("message"));
            }
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        progress_bar_1.setVisibility(View.GONE);
    }
}