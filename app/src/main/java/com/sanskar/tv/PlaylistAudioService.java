package com.sanskar.tv;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.model.PlayListModel;
import com.sanskar.tv.module.HomeModule.Fragment.PlayListFrag;

import java.io.IOException;
import java.util.ArrayList;

public class PlaylistAudioService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,

        AudioManager.OnAudioFocusChangeListener {
    public static MediaPlayer offlinemediaPlayer;
    public static final String ACTION_PLAY = "com.sanskar.tv.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.sanskar.tv.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.sanskar.tv.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.sanskar.tv.ACTION_NEXT";
    public static final String ACTION_STOP = "com.sanskar.tv.ACTION_STOP";

    private MediaSessionManager mediaSessionManager;
    private MediaSession mediaSession;
    private MediaController.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;

    //Used to pause/resume MediaPlayer
    private int resumePosition;

    //AudioFocus
    private AudioManager audioManager;

    // Binder given to clients
    private final IBinder iBinder = new PlaylistAudioService.LocalBinder();

    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    Bundle bundle=new Bundle();
    Bitmap bitmap=null;
    private int audioIndex = -1;
    private ArrayList<PlayListModel> audioList;
    private PlayListModel activeAudio;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        callStateListener();
        registerBecomingNoisyReceiver();
        register_playNewAudio();
    }

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(PlayListFrag.Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get the new media index form SharedPreferences
            audioList = (ArrayList<PlayListModel>) intent.getSerializableExtra("offlinebhajanarray");
            audioIndex = intent.getIntExtra("index",0);

            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }

            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio
            stopMedia();
            offlinemediaPlayer.reset();
            initMediaPlayer();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);
        }
    };

    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (offlinemediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (offlinemediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }
    private void playMedia() {
        if (!offlinemediaPlayer.isPlaying()) {
            offlinemediaPlayer.start();
        }
    }

    private void buildNotification(PlaybackStatus playbackStatus) {

        /**
         * Notification actions -> playbackAction()
         *  0 -> Play
         *  1 -> Pause
         *  2 -> Next track
         *  3 -> Previous track
         */

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher_round); //replace with your own image

        // Create a new Notification
        Notification.Builder notificationBuilder = (Notification.Builder) new Notification.Builder(this)
                // Hide the timestamp
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new Notification.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compat view
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(getResources().getColor(R.color.colorAccent))
                // Set the large and small icons
                .setLargeIcon(bitmap)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText(activeAudio.get_name())
                .setContentTitle(activeAudio.get_name())
                .setContentInfo(activeAudio.get_name())
                .setPriority(Notification.PRIORITY_MAX).setWhen(0)
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "PLAY_SONG_OFFLINE", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            // notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void stopMedia() {
        if (offlinemediaPlayer == null) return;
        if (offlinemediaPlayer.isPlaying()) {
            offlinemediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        if (offlinemediaPlayer.isPlaying()) {
            offlinemediaPlayer.pause();
            resumePosition = offlinemediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!offlinemediaPlayer.isPlaying()) {
            offlinemediaPlayer.seekTo(resumePosition);
            offlinemediaPlayer.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            audioList = (ArrayList<PlayListModel>) intent.getSerializableExtra("offlinebhajanarray");
            audioIndex = intent.getIntExtra("index",0);

            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }

        }catch (NullPointerException e){
            stopSelf();
            e.printStackTrace();
        }
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }
        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();
            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }
            buildNotification(PlaybackStatus.PLAYING);
        }

        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);

        return super.onStartCommand(intent, flags, startId);
    }
    private void initMediaSession() throws RemoteException {
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSession(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSession.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();

                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);

            }

            @Override
            public void onPause() {
                super.onPause();
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);

            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();

                skipToNext();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();

                skipToPrevious();
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    private void skipToNext() {
        //  List<BhajanCat> bhajanlist=Arrays.asList(bhajans);
        if (audioIndex == audioList.size() - 1){
            audioIndex = 0;
            activeAudio = audioList.get(audioIndex);
        }else{
            activeAudio = audioList.get(++audioIndex);
        }

        // Constants.AUDIO_INDEX = audioIndex;

        PreferencesHelper.getInstance().setValue("offlineindex",audioIndex);

        stopMedia();

        offlinemediaPlayer.reset();
        initMediaPlayer();
    }
    private void updateMetaData() {
        if (activeAudio.getImg_url()!=null) {

            Ion.with(getApplicationContext()).load(activeAudio.getImg_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    bitmap = result;
                }
            });
        }
        if (bitmap==null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        }

        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.mipmap.thumbnail_placeholder);
        mediaSession.setMetadata(new MediaMetadata.Builder()
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, bitmap)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, activeAudio.get_name())
                .putString(MediaMetadata.METADATA_KEY_ALBUM, activeAudio.get_name())
                .putString(MediaMetadata.METADATA_KEY_TITLE, activeAudio.get_name())
                .build());
    }

    private void skipToPrevious(){
        //   List<BhajanCat> bhajanlist=Arrays.asList(bhajans);
        if (audioIndex == 0){
            audioIndex = audioList.size() - 1;
            activeAudio = audioList.get(audioIndex);
        }else{
            activeAudio = audioList.get(--audioIndex);
        }
        //  Constants.AUDIO_INDEX=audioIndex;
        PreferencesHelper.getInstance().setValue("index",audioIndex);
        stopMedia();
        //reset mediaPlayer
        offlinemediaPlayer.reset();
        initMediaPlayer();
    }
    private void initMediaPlayer() {
        if (offlinemediaPlayer == null)
            offlinemediaPlayer = new MediaPlayer();//new MediaPlayer instance

        //Set up MediaPlayer event listeners
        try {
            offlinemediaPlayer.setOnCompletionListener(this);
            offlinemediaPlayer.setOnErrorListener(this);
            offlinemediaPlayer.setOnPreparedListener(this);
            offlinemediaPlayer.setOnBufferingUpdateListener(this);
            offlinemediaPlayer.setOnSeekCompleteListener(this);
            offlinemediaPlayer.setOnInfoListener(this);
            //Reset so that the MediaPlayer is not pointing to another data source
            offlinemediaPlayer.reset();


            offlinemediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                // Set the data source to the mediaFile location
                offlinemediaPlayer.setDataSource(activeAudio.get_url());
            } catch (IOException e) {
                e.printStackTrace();
                stopSelf();
            }
            offlinemediaPlayer.prepareAsync();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }
    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        removeNotification();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (offlinemediaPlayer != null) {
            stopMedia();
            offlinemediaPlayer.release();
        }
        removeAudioFocus();
        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        removeNotification();

        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);

    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }


    @Override
    public void onAudioFocusChange(int i) {
        switch (i) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (offlinemediaPlayer == null) initMediaPlayer();
                else if (!offlinemediaPlayer.isPlaying()) offlinemediaPlayer.start();
                offlinemediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (offlinemediaPlayer.isPlaying()) offlinemediaPlayer.stop();
                offlinemediaPlayer.release();
                offlinemediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (offlinemediaPlayer.isPlaying()) offlinemediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (offlinemediaPlayer.isPlaying()) offlinemediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();

        removeNotification();
        //stop the service
        stopSelf();

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int extra) {
        switch (i) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();

    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    public class LocalBinder extends Binder {
        public PlaylistAudioService getService(){
            return PlaylistAudioService.this;
        }

    }

}
