package com.sanskar.tv;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class MediaPlayerManager implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {
    public static MediaPlayer mMediaPlayer;
    public static Uri mCurrentMusicUri;
    public static boolean isPlaying;

    private static int mPauseDuration = 0;

    public static void startNewTrack(String uri) throws IOException {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(uri);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        isPlaying = true;
    }

    public static void startPlaying() {
        if (mMediaPlayer == null || mCurrentMusicUri == null) {
            throw new RuntimeException("Start player.");
        }

        mMediaPlayer.seekTo(mPauseDuration);
        mMediaPlayer.start();
        isPlaying = true;
    }

    public static boolean isInitialized() {
        return mMediaPlayer != null;
    }

    public static void pauseMusic() {
        if (mMediaPlayer == null) return;

        mPauseDuration = mMediaPlayer.getCurrentPosition();
        mMediaPlayer.pause();
        isPlaying = false;
    }

    public static void stopMusic() {
        mPauseDuration = 0;
        mCurrentMusicUri = null;
        if (mMediaPlayer == null) return;

        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;

        isPlaying = false;
    }

    public static boolean isPlaying() {
        return isInitialized() && isPlaying;
    }

    public static int getTotalDuration() {
        return isInitialized() ? mMediaPlayer.getDuration() : 0;
    }

    public static int getCurrentTime() {
        return isInitialized() ? mMediaPlayer.getCurrentPosition() : 0;
    }

    public static void seekTo(int seekTo) {
        if (isInitialized()) {
            if (mMediaPlayer.getDuration() < seekTo)
                throw new RuntimeException("Seek to duration cannot exceed to audio file duration.");
            mPauseDuration = seekTo;
            mMediaPlayer.seekTo(seekTo);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
      //  mediaPlayer.reset();
        /*try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
