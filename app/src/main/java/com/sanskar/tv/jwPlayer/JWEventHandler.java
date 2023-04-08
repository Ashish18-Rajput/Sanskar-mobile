package com.sanskar.tv.jwPlayer;

import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.core.PlayerState;

import com.longtailvideo.jwplayer.events.ControlsEvent;

import com.longtailvideo.jwplayer.events.listeners.AdvertisingEvents;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.adaptive.QualityLevel;
import com.longtailvideo.jwplayer.media.adaptive.VisualQuality;
import com.longtailvideo.jwplayer.media.audio.AudioTrack;
import com.longtailvideo.jwplayer.media.captions.Caption;
import com.longtailvideo.jwplayer.media.meta.Metadata;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.List;

/**
 * Outputs all JW Player Events to logging, with the exception of time events.
 */
public class JWEventHandler implements VideoPlayerEvents.OnSetupErrorListener,
        VideoPlayerEvents.OnPlaylistListener,
        VideoPlayerEvents.OnPlaylistItemListener,
        VideoPlayerEvents.OnPlayListener,
        VideoPlayerEvents.OnPauseListener,
        VideoPlayerEvents.OnBufferListener,
        VideoPlayerEvents.OnIdleListener,
        VideoPlayerEvents.OnErrorListener,
        VideoPlayerEvents.OnSeekListener,
        VideoPlayerEvents.OnTimeListener,
        VideoPlayerEvents.OnFullscreenListener,
        VideoPlayerEvents.OnAudioTracksListener,
        VideoPlayerEvents.OnAudioTrackChangedListener,
        VideoPlayerEvents.OnCaptionsListListener,
        VideoPlayerEvents.OnMetaListener,
        VideoPlayerEvents.OnPlaylistCompleteListener,
        VideoPlayerEvents.OnCompleteListener,
        VideoPlayerEvents.OnLevelsChangedListener,
        VideoPlayerEvents.OnLevelsListener,
        VideoPlayerEvents.OnCaptionsChangedListener,
        VideoPlayerEvents.OnControlsListener,
        VideoPlayerEvents.OnDisplayClickListener,
        VideoPlayerEvents.OnMuteListener,
        VideoPlayerEvents.OnSeekedListener,
        VideoPlayerEvents.OnVisualQualityListener,
        VideoPlayerEvents.OnFirstFrameListener,
        AdvertisingEvents.OnAdClickListener,
        AdvertisingEvents.OnAdCompleteListener,
        AdvertisingEvents.OnAdSkippedListener,
        AdvertisingEvents.OnAdErrorListener,
        AdvertisingEvents.OnAdImpressionListener,
        AdvertisingEvents.OnAdTimeListener,
        AdvertisingEvents.OnAdPauseListener,
        AdvertisingEvents.OnAdPlayListener,
        AdvertisingEvents.OnBeforePlayListener,
        AdvertisingEvents.OnBeforeCompleteListener {
    private String TAG = JWEventHandler.class.getName();

    TextView mOutput;

    public JWEventHandler(JWPlayerView jwPlayerView, TextView output) {
        mOutput = output;
        // Subscribe to all JW Player events
        jwPlayerView.addOnFirstFrameListener(this);
        jwPlayerView.addOnSetupErrorListener(this);
        jwPlayerView.addOnPlaylistListener(this);
        jwPlayerView.addOnPlaylistItemListener(this);
        jwPlayerView.addOnPlayListener(this);
        jwPlayerView.addOnPauseListener(this);
        jwPlayerView.addOnBufferListener(this);
        jwPlayerView.addOnIdleListener(this);
        jwPlayerView.addOnErrorListener(this);
        jwPlayerView.addOnSeekListener(this);
        jwPlayerView.addOnTimeListener(this);
        jwPlayerView.addOnFullscreenListener(this);
        jwPlayerView.addOnLevelsChangedListener(this);
        jwPlayerView.addOnLevelsListener(this);
        jwPlayerView.addOnCaptionsListListener(this);
        jwPlayerView.addOnCaptionsChangedListener(this);
        //  jwPlayerView.addOnRelatedCloseListener(this);
        //  jwPlayerView.addOnRelatedOpenListener(this);
        //  jwPlayerView.addOnRelatedPlayListener(this);
        jwPlayerView.addOnControlsListener(this);
        jwPlayerView.addOnDisplayClickListener(this);
        jwPlayerView.addOnMuteListener(this);
        jwPlayerView.addOnVisualQualityListener(this);
        jwPlayerView.addOnSeekedListener(this);
        jwPlayerView.addOnAdClickListener(this);
        jwPlayerView.addOnAdCompleteListener(this);
        jwPlayerView.addOnAdSkippedListener(this);
        jwPlayerView.addOnAdErrorListener(this);
        jwPlayerView.addOnAdImpressionListener(this);
        jwPlayerView.addOnAdTimeListener(this);
        jwPlayerView.addOnAdPauseListener(this);
        jwPlayerView.addOnAdPlayListener(this);
        jwPlayerView.addOnMetaListener(this);
        jwPlayerView.addOnPlaylistCompleteListener(this);
        jwPlayerView.addOnCompleteListener(this);
        jwPlayerView.addOnBeforePlayListener(this);
        jwPlayerView.addOnBeforeCompleteListener(this);

    }

    private void updateOutput(String output) {
        mOutput.setText(output);
    }

    /**
     * Regular playback events below here
     */



    @Override
    public void onAdClick(String s) {

    }

    @Override
    public void onAdComplete(String s) {

    }

    @Override
    public void onAdError(String s, String s1) {

    }

    @Override
    public void onAdImpression(String s, String s1, String s2) {

    }

    @Override
    public void onAdPause(String s, PlayerState playerState) {

    }

    @Override
    public void onAdPlay(String s, PlayerState playerState) {

    }

    @Override
    public void onAdSkipped(String s) {

    }

    @Override
    public void onAdTime(String s, long l, long l1, int i) {

    }

    @Override
    public void onBeforeComplete() {

    }

    @Override
    public void onBeforePlay() {

    }

    @Override
    public void onAudioTrackChanged(int i) {

    }

    @Override
    public void onAudioTracks(List<AudioTrack> list) {

    }

    @Override
    public void onBuffer(PlayerState playerState) {

    }

    @Override
    public void onCaptionsChanged(int i, List<Caption> list) {

    }

    @Override
    public void onCaptionsList(List<Caption> list) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDisplayClick() {

    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onFirstFrame(int i) {

    }

    @Override
    public void onFullscreen(boolean b) {

    }

    @Override
    public void onIdle(PlayerState playerState) {

    }

    @Override
    public void onLevelsChanged(int i) {

    }

    @Override
    public void onLevels(List<QualityLevel> list) {

    }

    @Override
    public void onMeta(Metadata metadata) {

    }

    @Override
    public void onMute(boolean b) {

    }

    @Override
    public void onPause(PlayerState playerState) {

    }

    @Override
    public void onPlay(PlayerState playerState) {

    }

    @Override
    public void onPlaylistComplete() {

    }

    @Override
    public void onPlaylistItem(int i, PlaylistItem playlistItem) {

    }

    @Override
    public void onPlaylist(List<PlaylistItem> list) {

    }

    @Override
    public void onSeek(int i, int i1) {

    }

    @Override
    public void onSeeked() {

    }

    @Override
    public void onSetupError(String s) {

    }

    @Override
    public void onTime(long l, long l1) {

    }

    @Override
    public void onVisualQuality(VisualQuality visualQuality) {

    }

    @Override
    public void onControls(ControlsEvent controlsEvent) {

    }
}
