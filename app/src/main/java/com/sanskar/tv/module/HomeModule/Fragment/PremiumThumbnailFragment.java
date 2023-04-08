package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.utility.YoutubeExtractor.ExtractorException;
import com.sanskar.tv.utility.YoutubeExtractor.YoutubeStreamExtractor;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTMedia;
import com.sanskar.tv.utility.YoutubeExtractor.model.YTSubtitles;
import com.sanskar.tv.utility.YoutubeExtractor.model.YoutubeMeta;

import java.util.List;


public class PremiumThumbnailFragment extends Fragment {

    String image = "";
    MenuMasterList video_url ;
    Context context;
    String youtubeLink;
    String BASE_URL = "https://www.youtube.com";
    int count = 0;

    public PlayerView playerView;
    public static SimpleExoPlayer simpleExoPlayer;
    DefaultHttpDataSourceFactory factory;
    TrackSelector trackSelector;
    BandwidthMeter bandwidthMeter;
    LoadControl loadControl;
    ExtractorsFactory extractorsFactory;
    ImageView imageView, mute_on;
    boolean tag=false;

    public static PremiumThumbnailFragment newInstance(MenuMasterList param2, String param1) {
        PremiumThumbnailFragment fragment = new PremiumThumbnailFragment();
        Bundle args = new Bundle();
        args.putString("image", param1);
        args.putSerializable("video_url", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            image = getArguments().getString("image");
            video_url = (MenuMasterList) getArguments().getSerializable("video_url");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premium_thumbnail, container, false);

        initViews(view);

        loadControl = new DefaultLoadControl();
        bandwidthMeter = new DefaultBandwidthMeter();
        trackSelector = new DefaultTrackSelector(
                new AdaptiveTrackSelection.Factory(bandwidthMeter)
        );
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                context, trackSelector, loadControl
        );
        factory = new DefaultHttpDataSourceFactory(
                "exoplayer_video"
        );
        extractorsFactory = new DefaultExtractorsFactory();



        return view;
    }

    private void playVideo(Uri videourl) {

        imageView.setVisibility(View.VISIBLE);

        //Uri videourl = Uri.parse(video_url);
        MediaSource mediaSource = new ExtractorMediaSource(videourl
                , factory, extractorsFactory, null, null);
        playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        playerView.setKeepScreenOn(true);
        playerView.setFocusable(true);
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setVolume(0f);
       // playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        //simpleExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
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
                if (playWhenReady) {
                    imageView.setVisibility(View.GONE);
                    mute_on.setVisibility(View.VISIBLE);
                    playerView.setVisibility(View.VISIBLE);
                }
                if (playbackState==SimpleExoPlayer.STATE_ENDED){
                    imageView.setVisibility(View.VISIBLE);
                    mute_on.setVisibility(View.GONE);
                    playerView.setVisibility(View.GONE);
                    if (tag){
                        mute_on.setImageResource(R.mipmap.volume_off_new);
                        simpleExoPlayer.setVolume(0f);
                    }else{
                        mute_on.setImageResource(R.mipmap.volume_on_new);
                        simpleExoPlayer.setVolume(1f);
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
            if (!tag){
                mute_on.setImageResource(R.mipmap.volume_on_new);
                simpleExoPlayer.setVolume(1f);
                tag=true;
            }else{
                mute_on.setImageResource(R.mipmap.volume_off_new);
                simpleExoPlayer.setVolume(0f);
                tag=false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        simpleExoPlayer.setPlayWhenReady(true);
    }



    private void initViews(View view) {
        playerView = view.findViewById(R.id.player_view_exo);
        imageView = view.findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        playerView.setVisibility(View.GONE);
        mute_on = view.findViewById(R.id.mute_on);
        mute_on.setVisibility(View.GONE);

        Glide.with(context)
                .load(image)
                .apply(new RequestOptions().placeholder(R.mipmap.landscape_placeholder).error(R.mipmap.landscape_placeholder))
                .into(imageView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(() -> {
               /* playVideo();*/
                if (video_url.getYoutube()!=null){
                    youtubeLink = BASE_URL + "/watch?v=" + video_url.getYoutube();
                    Log.d("shantanu", youtubeLink);
                    new YoutubeStreamExtractor(new YoutubeStreamExtractor.ExtractorListner(){
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
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).useDefaultLogin().Extract(youtubeLink);
                  /*  @SuppressLint("StaticFieldLeak")
                    YouTubeExtractor extractor = new YouTubeExtractor(context) {
                        @Override
                        protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {

                            if (ytFiles != null) {
                                int itag = 18;
                                String downloadUrl = ytFiles.get(itag).getUrl();
                                Uri url = Uri.parse(downloadUrl);
                                playVideo(url);
                                Log.d("shantanu", downloadUrl);
                            } else {
                                Log.d("shantanu", String.valueOf(ytFiles));
                            }
                        }
                    };
                    extractor.extract(youtubeLink, true, true);*/
                }
                if (video_url.getNormal()!=null){
                    playVideo(Uri.parse(video_url.getNormal()));
                }
            }, 500);
            //playVideo();

        } else {
            if (simpleExoPlayer != null)
                simpleExoPlayer.setPlayWhenReady(false);
        }

    }
}