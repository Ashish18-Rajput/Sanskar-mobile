package com.sanskar.tv.newaudiocode;

import android.media.MediaPlayer;

public class Listeners {
    public interface LoadSongListener {
        void onSongLoaded();
    }

    public interface LoadImageListener {
        void onImageLoaded();
    }

    public interface MediaPlayerListener {
        void onMediaPlayerStarted(MediaPlayer mp);
    }
}
