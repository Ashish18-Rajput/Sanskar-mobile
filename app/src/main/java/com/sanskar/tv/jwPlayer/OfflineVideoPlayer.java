package com.sanskar.tv.jwPlayer;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.sanskar.tv.R;

public class OfflineVideoPlayer extends YouTubeBaseActivity implements  YouTubePlayer.OnInitializedListener{
    VideoView video_view;
    String path;
    YoutubeLayout playerView;
    YouTubePlayerView youTubePlayerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_player);

       // video_view = findViewById(R.id.video_view);

       /* if(videoplaylisttype==1){*/
         //   video_view.setVisibility(View.GONE);

            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.playerView);
            try {
                youTubePlayerView.initialize(getResources().getString(R.string.api_keys_usable_gcp), this);

            } catch (Exception e) {
            }
     /*  }
        */

        /*else{
            video_view.setVisibility(View.VISIBLE);
            path = getIntent().getStringExtra("absolutepath");



            video_view.setVideoPath(path);
            MediaController mediaController = new MediaController(this);
            video_view.setMediaController(mediaController);
            mediaController.setMediaPlayer(video_view);
            video_view.start();
            //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            video_view.requestLayout();
        }*/






    }

   /* @Override
    protected void onStop() {
        super.onStop();
        video_view.stopPlayback();
    }*/

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        path = getIntent().getStringExtra("absolutepath");
        youTubePlayer.cueVideo(path);
        youTubePlayer.loadVideo(path);

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
