///*
//package com.sanskar.totalbhakti;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.media.AudioManager;
//import android.media.MediaMetadata;
//import android.media.MediaPlayer;
//import android.media.session.MediaController;
//import android.media.session.MediaSession;
//import android.media.session.MediaSessionManager;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.support.annotation.Nullable;
//import android.telephony.PhoneStateListener;
//import android.telephony.TelephonyManager;
//import android.util.Log;
//import android.view.View;
//
//import com.koushikdutta.async.future.FutureCallback;
//import com.koushikdutta.ion.Ion;
//import com.sanskar.totalbhakti.Others.Helper.Constants;
//import com.sanskar.totalbhakti.Others.Helper.PreferencesHelper;
//import com.sanskar.totalbhakti.module.HomeModule.Activity.HomeActivityy;
//import com.sanskar.totalbhakti.module.HomeModule.Fragment.BhajanPlayFragment;
//import com.sanskar.totalbhakti.module.HomeModule.Fragment.BhajansCategoryFragment;
//import com.sanskar.totalbhakti.module.HomeModule.Fragment.DownloadsFragment;
//import com.sanskar.totalbhakti.module.HomeModule.Pojo.BhajanCat;
//import com.sanskar.totalbhakti.module.HomeModule.Pojo.OfflineData;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class NewAudioService extends Service implements MediaPlayer.OnCompletionListener,
//        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
//        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
//
//        AudioManager.OnAudioFocusChangeListener {
//
//    public static final String ACTION_PLAY = "com.sanskar.totalbhakti.ACTION_PLAY";
//    public static final String ACTION_PAUSE = "com.sanskar.totalbhakti.ACTION_PAUSE";
//    public static final String ACTION_PREVIOUS = "com.sanskar.totalbhakti.ACTION_PREVIOUS";
//    public static final String ACTION_NEXT = "com.sanskar.totalbhakti.ACTION_NEXT";
//    public static final String ACTION_STOP = "com.sanskar.totalbhakti.ACTION_STOP";
//    public static final String NOTIFICATION_CHANNEL_ID = "10001";
//    //AudioPlayer notification ID
//    private static final int NOTIFICATION_ID = 101;
//    public static MediaPlayer mediaPlayer;
//    // Binder given to clients
//    private final IBinder iBinder = new LocalBinder();
//    public MediaSessionManager mediaSessionManager;
//    public MediaSession mediaSession;
//    public MediaController.TransportControls transportControls;
//    BhajanCat[] bhajans;
//    OfflineData[] offlineData;
//    BhajanCat activeBhajan;
//    OfflineData activeAudio;
//    List<BhajanCat> bhajanList = new ArrayList<>();
//    List<OfflineData> offlineList = new ArrayList<>();
//    Bundle bundle = new Bundle();
//    Bitmap bitmap = null;
//    String status = "";
//    //Used to pause/resume MediaPlayer
//    private int resumePosition;
//    //AudioFocus
//    private AudioManager audioManager;
//    private int audioIndex = -1;
//    private boolean ongoingCall = false;
//    private PhoneStateListener phoneStateListener;
//    private TelephonyManager telephonyManager;
//    private NotificationManager mNotificationManager;
//
//    private BroadcastReceiver playnext = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (Constants.FORWARD_SONG.equals("true")) {
//                Constants.FORWARD_SONG = "false";
//                skipToNext();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
//            } else if (Constants.REVERSE_SONG.equals("true")) {
//                Constants.REVERSE_SONG = "false";
//                skipToPrevious();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
//            }
//
//        }
//    };
//    private BroadcastReceiver showplaypause = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getStringExtra("isplaying").equals("true")) {
//                buildNotification(PlaybackStatus.PLAYING);
//                //  pauseMedia();
//            } else if (intent.getStringExtra("isplaying").equals("false")) {
//                buildNotification(PlaybackStatus.PAUSED);
//                //  resumeMedia();
//            }
//        }
//    };
//    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            //pause audio on ACTION_AUDIO_BECOMING_NOISY
//            pauseMedia();
//            buildNotification(PlaybackStatus.PAUSED);
//        }
//    };
//    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (HomeActivityy.playerView != null) {
//                HomeActivityy.playerView.stop();
//                HomeActivityy.frameLayout.setVisibility(View.GONE);
//            }
//
//            //Get the new media index form SharedPreferences
//            //  bhajanList=Arrays.asList(bhajans);
//
//            // audioIndex = Constants.AUDIO_INDEX;
//
//            if (intent.getSerializableExtra("bhajanlist") != null) {
//
//                status = "bhajan";
//
//                bhajans = (BhajanCat[]) intent.getSerializableExtra("bhajanlist");
//
//                bhajanList = Arrays.asList(bhajans);
//
//                audioIndex = PreferencesHelper.getInstance().getIntValue("index", 0);
//                if (audioIndex != -1 && audioIndex < bhajanList.size()) {
//                    //index is in a valid range
//                    activeBhajan = bhajanList.get(audioIndex);
//                } else {
//                    stopSelf();
//                }
//            } else if(intent.getSerializableExtra("offlinebhajanarray") != null){
//
//                status = "download";
//                offlineList = (List<OfflineData>) intent.getSerializableExtra("offlinebhajanarray");
//                //offlineList = Arrays.asList(offlineData);
////                offlineList = (ArrayList<OfflineData>) intent.getSerializableExtra("offlinebhajanarray");
////                audioIndex = intent.getIntExtra("index", 0);
//                audioIndex = PreferencesHelper.getInstance().getIntValue("index", 0);
//
//                if (audioIndex != -1 && audioIndex < offlineList.size()) {
//                    //index is in a valid range
//                    activeAudio = offlineList.get(audioIndex);
//                } else {
//                    stopSelf();
//                }
//            }
//
//            //A PLAY_NEW_AUDIO action received
//            //reset mediaPlayer to play the new Audio
//            stopMedia();
//            mediaPlayer.reset();
//            initMediaPlayer();
//            updateMetaData();
//            buildNotification(PlaybackStatus.PLAYING);
//            */
///*if (Constants.FORWARD_SONG.equals("true")){
//                Constants.FORWARD_SONG="false";
//                skipToNext();
//            }*//*
//
//        }
//    };
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return iBinder;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        callStateListener();
//
//        registerBecomingNoisyReceiver();
//
//        register_playNewAudio();
//        handlePlayPause();
//        nextsong();
//    }
//
//    private void nextsong() {
//        IntentFilter intentFilter = new IntentFilter(BhajanPlayFragment.BROADCAST_PLAY_NEXT_SONG);
//        registerReceiver(playnext, intentFilter);
//    }
//
//    private void handlePlayPause() {
//        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
//        registerReceiver(showplaypause, intentFilter);
//
////        IntentFilter filter2 = new IntentFilter(DownloadsFragment.BROADCAST_ISPLAYING_SONG);
////        registerReceiver(playNewAudio, filter2);
//    }
//
//    private void register_playNewAudio() {
//        if (Constants.COMING_FROM.equals("bhajan")) {
//            status = "bhajan";
//            IntentFilter filter = new IntentFilter(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
//            registerReceiver(playNewAudio, filter);
//
//        } else if (Constants.COMING_FROM.equals("download")) {
//            status = "download";
//            IntentFilter filter2 = new IntentFilter(DownloadsFragment.Broadcast_PLAY_NEW_AUDIO);
//            registerReceiver(playNewAudio, filter2);
//        }
//    }
//
//    private void registerBecomingNoisyReceiver() {
//        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        registerReceiver(becomingNoisyReceiver, intentFilter);
//    }
//
//    private void callStateListener() {
//        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        phoneStateListener = new PhoneStateListener() {
//            @Override
//            public void onCallStateChanged(int state, String incomingNumber) {
//                switch (state) {
//                    //if at least one call exists or the phone is ringing
//                    //pause the MediaPlayer
//                    case TelephonyManager.CALL_STATE_OFFHOOK:
//                    case TelephonyManager.CALL_STATE_RINGING:
//                        if (mediaPlayer != null) {
//                            pauseMedia();
//                            ongoingCall = true;
//                        }
//                        break;
//                    case TelephonyManager.CALL_STATE_IDLE:
//                        // Phone idle. Start playing.
//                        if (mediaPlayer != null) {
//                            if (ongoingCall) {
//                                ongoingCall = false;
//                                resumeMedia();
//                            }
//                        }
//                        break;
//                }
//            }
//        };
//        telephonyManager.listen(phoneStateListener,
//                PhoneStateListener.LISTEN_CALL_STATE);
//    }
//
//    private void playMedia() {
//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//        }
//    }
//
//    private void stopMedia() {
//        try {
//            if (mediaPlayer == null) return;
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//            }
//        } catch (IllegalStateException e) {
//        }
//    }
//
//    private void pauseMedia() {
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            resumePosition = mediaPlayer.getCurrentPosition();
//        }
//    }
//
//    private void resumeMedia() {
//        if (!mediaPlayer.isPlaying()) {
//            mediaPlayer.seekTo(resumePosition);
//            mediaPlayer.start();
//        }
//    }
//
//    private void skipToNext() {
//        //  List<BhajanCat> bhajanlist=Arrays.asList(bhajans);
//        if (audioIndex == bhajanList.size() - 1) {
//            audioIndex = 0;
//            activeBhajan = bhajanList.get(audioIndex);
//        } else {
//            activeBhajan = bhajanList.get(++audioIndex);
//        }
//
//        // Constants.AUDIO_INDEX = audioIndex;
//
//        PreferencesHelper.getInstance().setValue("index", audioIndex);
//
//        stopMedia();
//
//        mediaPlayer.reset();
//        initMediaPlayer();
//    }
//
//    private void skipToPrevious() {
//        //   List<BhajanCat> bhajanlist=Arrays.asList(bhajans);
//        if (audioIndex == 0) {
//            audioIndex = bhajanList.size() - 1;
//            activeBhajan = bhajanList.get(audioIndex);
//        } else {
//            activeBhajan = bhajanList.get(--audioIndex);
//        }
//        //  Constants.AUDIO_INDEX=audioIndex;
//        PreferencesHelper.getInstance().setValue("index", audioIndex);
//        stopMedia();
//        //reset mediaPlayer
//        mediaPlayer.reset();
//        initMediaPlayer();
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        try {
//            // bhajans=(BhajanCat[]) bundle.getSerializable("bhajanlist");
//            //  bhajans=PreferencesHelper.getInstance().getArrayValue("bhajan");
//            //   audioIndex= Integer.parseInt(intent.getStringExtra("position"));
//            if (AudioPlayerService.mediaPlayer != null) {
//                Constants.IS_PAUSEDFROMHOME = "true";
//                AudioPlayerService.mediaPlayer.stop();
//
//                HomeActivityy.playerlayout.setVisibility(View.GONE);
//                Constants.CALL_RESUME = "false";
//            }
//            if (HomeActivityy.playerView != null) {
//                HomeActivityy.playerView.stop();
//                HomeActivityy.frameLayout.setVisibility(View.GONE);
//            }
//
//            audioIndex = PreferencesHelper.getInstance().getIntValue("index", 0);
//
//            if (status.equals("bhajan")) {
//
//                if (bhajans == null)
//                    bhajans = (BhajanCat[]) intent.getSerializableExtra("bhajanarray");
//
//                bhajanList = Arrays.asList(bhajans);
//
//                if (audioIndex != -1 && audioIndex < bhajanList.size()) {
//                    activeBhajan = bhajanList.get(audioIndex);
//
//                } else {
//                    stopSelf();
//
//                }
//            } else if (status.equals("download")) {
//                offlineList = (List<OfflineData>) intent.getSerializableExtra("offlinebhajanarray");
////                if (offlineData == null) {
////                    offlineData = (OfflineData[]) intent.getSerializableExtra("offlinebhajanarray");
////
////                }
//
//                //offlineList = Arrays.asList(offlineData);
//
//                if(offlineList.size() == 1){
//                    audioIndex = 0;
//                }
//
//                if (audioIndex != -1 && audioIndex < offlineList.size()) {
//                    activeAudio = offlineList.get(audioIndex);
//
//                } else {
//                    stopSelf();
//                }
//            }
//        } catch (Exception e) {
//            stopSelf();
//        }
//
//        if (requestAudioFocus() == false) {
//            //Could not gain focus
//            stopSelf();
//        }
//
//        if (mediaSessionManager == null) {
//            try {
//                initMediaSession();
//                initMediaPlayer();
//
//            } catch (RemoteException e) {
//                e.printStackTrace();
//                stopSelf();
//            }
//            buildNotification(PlaybackStatus.PLAYING);
//        }
//        handleIncomingActions(intent);
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    private void updateMetaData() {
//        InputStream in;
//        // bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
//        if (status.equals("bhajan")) {
//            if (activeBhajan.getImage() != null) {
//
//                Ion.with(getApplicationContext()).load(activeBhajan.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
//                    @Override
//                    public void onCompleted(Exception e, Bitmap result) {
//                        bitmap = result;
//                    }
//                });
//            }
//        }
//
////        else if(status.equals("download")) {
////            if (activeAudio.getThumb() != null) {
////
////                Ion.with(getApplicationContext()).load(activeAudio.getThumb()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
////                    @Override
////                    public void onCompleted(Exception e, Bitmap result) {
////                        bitmap = result;
////                    }
////                });
////            }
////        }
//
//      */
///*  try{
//        URL url = new URL(activeBhajan.getImage());
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoInput(true);
//        connection.connect();
//        in = connection.getInputStream();
//        bitmap = BitmapFactory.decodeStream(in);
//        //return bitmap;
//    }catch(MalformedURLException e){
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }*//*
//
//        if (bitmap == null) {
//            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        }
//
//        if (status.equals("bhajan")) {
//            mediaSession.setMetadata(new MediaMetadata.Builder()
//                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, bitmap)
//                    .putString(MediaMetadata.METADATA_KEY_ARTIST, activeBhajan.getArtist_name())
//                    .putString(MediaMetadata.METADATA_KEY_ALBUM, activeBhajan.getTitle())
//                    .putString(MediaMetadata.METADATA_KEY_TITLE, activeBhajan.getDescription())
//                    .build());
//        } else if (status.equals("download")) {
//            mediaSession.setMetadata(new MediaMetadata.Builder()
//                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, bitmap)
//                    .putString(MediaMetadata.METADATA_KEY_ARTIST, activeAudio.getName())
//                    .putString(MediaMetadata.METADATA_KEY_ALBUM, activeAudio.getTitle())
//                    .putString(MediaMetadata.METADATA_KEY_TITLE, activeAudio.getName())
//                    .build());
//        }
//
//    }
//
//    private void buildNotification(PlaybackStatus playbackStatus) {
//
//        */
///**
//         * Notification actions -> playbackAction()
//         *  0 -> Play
//         *  1 -> Pause
//         *  2 -> Next track
//         *  3 -> Previous track
//         *//*
//
//
//        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
//        PendingIntent play_pauseAction = null;
//
//        //Build a new notification according to the current state of the MediaPlayer
//        if (playbackStatus == PlaybackStatus.PLAYING) {
//            notificationAction = android.R.drawable.ic_media_pause;
//            //create the pause action
//            play_pauseAction = playbackAction(1);
//        } else if (playbackStatus == PlaybackStatus.PAUSED) {
//            notificationAction = android.R.drawable.ic_media_play;
//            //create the play action
//            play_pauseAction = playbackAction(0);
//        }
//
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_launcher); //replace with your own image
//        Notification.Builder notificationBuilder = (Notification.Builder) new Notification.Builder(this);
//        // Create a new Notification
//        if (status.equals("bhajan")) {
//            notificationBuilder
//                    // Hide the timestamp
//                    .setShowWhen(false)
//                    // Set the Notification style
//                    .setStyle(new Notification.MediaStyle()
//                            // Attach our MediaSession token
//                            .setMediaSession(mediaSession.getSessionToken())
//                            // Show our playback controls in the compat view
//                            .setShowActionsInCompactView(0, 1, 2))
//                    // Set the Notification color
//                    .setColor(getResources().getColor(R.color.colorAccent))
//                    // Set the large and small icons
//                    .setLargeIcon(bitmap)
//                    .setSmallIcon(android.R.drawable.stat_sys_headset)
//                    // Set Notification content information
//                    .setContentText(activeBhajan.getArtist_name())
//                    .setContentTitle(activeBhajan.getTitle())
//                    .setContentInfo(activeBhajan.getDescription())
//                    // Add playback actions
//                    .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
//                    .addAction(notificationAction, "pause", play_pauseAction)
//                    .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));
//
//        } else if (status.equals("download")) {
//            notificationBuilder
//                    // Hide the timestamp
//                    .setShowWhen(false)
//                    // Set the Notification style
//                    .setStyle(new Notification.MediaStyle()
//                            // Attach our MediaSession token
//                            .setMediaSession(mediaSession.getSessionToken())
//                            // Show our playback controls in the compat view
//                            .setShowActionsInCompactView(0, 1, 2))
//                    // Set the Notification color
//                    .setColor(getResources().getColor(R.color.colorAccent))
//                    // Set the large and small icons
//                    .setLargeIcon(bitmap)
//                    .setSmallIcon(android.R.drawable.stat_sys_headset)
//                    // Set Notification content information
//                    .setContentText(activeAudio.getName())
//                    .setContentTitle(activeAudio.getTitle())
//                    .setContentInfo(activeAudio.getName())
//                    // Add playback actions
//                    .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
//                    .addAction(notificationAction, "pause", play_pauseAction)
//                    .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));
//
//        }
//
//        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            assert mNotificationManager != null;
//            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//            mNotificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
//    }
//
//    private PendingIntent playbackAction(int actionNumber) {
//        Intent playbackAction = new Intent(this, AudioPlayerService.class);
//        switch (actionNumber) {
//            case 0:
//                // Play
//                playbackAction.setAction(ACTION_PLAY);
//                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
//            case 1:
//                // Pause
//                playbackAction.setAction(ACTION_PAUSE);
//                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
//            case 2:
//                // Next track
//                playbackAction.setAction(ACTION_NEXT);
//                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
//            case 3:
//                // Previous track
//                playbackAction.setAction(ACTION_PREVIOUS);
//                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
//            default:
//                break;
//        }
//        return null;
//    }
//
//    private void initMediaPlayer() {
//        if (mediaPlayer == null)
//            mediaPlayer = new MediaPlayer();//new MediaPlayer instance
//
//        //Set up MediaPlayer event listeners
//        mediaPlayer.setOnCompletionListener(this);
//        mediaPlayer.setOnErrorListener(this);
//        mediaPlayer.setOnPreparedListener(this);
//        mediaPlayer.setOnBufferingUpdateListener(this);
//        mediaPlayer.setOnSeekCompleteListener(this);
//        mediaPlayer.setOnInfoListener(this);
//        //Reset so that the MediaPlayer is not pointing to another data source
//        mediaPlayer.reset();
//
//        if (status.equals("bhajan")) {
//
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                // Set the data source to the mediaFile location
//                mediaPlayer.setDataSource(activeBhajan.getMedia_file());
//
//                if (!Constants.DONT_SHOW.equals("true")) {
//                    Intent broadcastintent = new Intent(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
//                    broadcastintent.putExtra("title", activeBhajan.getTitle());
//                    broadcastintent.putExtra("description", activeBhajan.getDescription());
//                    broadcastintent.putExtra("thumb", activeBhajan.getImage());
//                    broadcastintent.putExtra("isplaying", "true");
//
//                    Constants.TITLE_SONG = activeBhajan.getTitle();
//                    Constants.DESCRIPTION_SONG = activeBhajan.getDescription();
//                    Constants.THUMB_SONG = activeBhajan.getImage();
//                    Constants.ISPLAYING_SONG = "true";
//                    Constants.AUDIO_INDEX = audioIndex;
//
//
//                    Constants.IS_PLAYING = "true";
//                    sendBroadcast(broadcastintent);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                stopSelf();
//            }
//        } else if (status.equals("download")) {
//
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            try {
//                // Set the data source to the mediaFile location
//                mediaPlayer.setDataSource(activeAudio.getPath());
//
//                if (!Constants.DONT_SHOW.equals("true")) {
//                    Intent broadcastintent = new Intent(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
//                    broadcastintent.putExtra("title", activeAudio.getTitle());
//                    broadcastintent.putExtra("description", activeAudio.getName());
//                    broadcastintent.putExtra("thumb", activeAudio.getThumb());
//                    broadcastintent.putExtra("isplaying", "true");
//
//                    Constants.TITLE_SONG = activeAudio.getTitle();
//                    Constants.DESCRIPTION_SONG = activeAudio.getName();
//                    Constants.THUMB_SONG = activeAudio.getThumb();
//                    Constants.ISPLAYING_SONG = "true";
//                    Constants.AUDIO_INDEX = audioIndex;
//
//
//                    Constants.IS_PLAYING = "true";
//                    sendBroadcast(broadcastintent);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                stopSelf();
//            }
//        }
//        mediaPlayer.prepareAsync();
//    }
//
//    private void initMediaSession() throws RemoteException {
//        if (mediaSessionManager != null) return; //mediaSessionManager exists
//
//        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
//        // Create a new MediaSession
//        mediaSession = new MediaSession(getApplicationContext(), "AudioPlayer");
//        //Get MediaSessions transport controls
//        transportControls = mediaSession.getController().getTransportControls();
//        //set MediaSession -> ready to receive media commands
//        mediaSession.setActive(true);
//        //indicate that the MediaSession handles transport control commands
//        // through its MediaSessionCompat.Callback.
//        mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
//
//        //Set mediaSession's MetaData
//        updateMetaData();
//
//        // Attach Callback to receive MediaSession updates
//        mediaSession.setCallback(new MediaSession.Callback() {
//            // Implement callbacks
//            @Override
//            public void onPlay() {
//                super.onPlay();
//
//                resumeMedia();
//                buildNotification(PlaybackStatus.PLAYING);
//
//                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
//                Constants.IS_PLAYING = "true";
//                intent.putExtra("isplaying", "true");
//                sendBroadcast(intent);
//
//               */
///* if (Constants.IS_PLAYING_ON_CATEGORY.equals("true")) {
//                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
//                    Constants.IS_PLAYING = "true";
//                    intent.putExtra("isplaying", "true");
//                    sendBroadcast(intent);
//                }else if (Constants.IS_PLAYING_ONPLAY.equals("true")){
//                    Intent intent = new Intent(BhajanPlayFragment.BROADCAST_ISPLAYING_SONG);
//                    Constants.IS_PLAYING = "true";
//                    intent.putExtra("isplaying", "true");
//                    sendBroadcast(intent);
//                }*//*
//
//            }
//
//            @Override
//            public void onPause() {
//                super.onPause();
//
//                pauseMedia();
//                buildNotification(PlaybackStatus.PAUSED);
//
//                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
//                Constants.IS_PLAYING = "false";
//                intent.putExtra("isplaying", "false");
//                sendBroadcast(intent);
//
//               */
///* if (Constants.IS_PLAYING_ON_CATEGORY.equals("true")) {
//                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
//                    Constants.IS_PLAYING = "false";
//                    intent.putExtra("isplaying", "false");
//                    sendBroadcast(intent);
//                }
//                else if (Constants.IS_PLAYING_ONPLAY.equals("true")){
//                    Intent intent = new Intent(BhajanPlayFragment.BROADCAST_ISPLAYING_SONG);
//                    Constants.IS_PLAYING = "false";
//                    intent.putExtra("isplaying", "false");
//                    sendBroadcast(intent);
//                }*//*
//
//            }
//
//            @Override
//            public void onSkipToNext() {
//                super.onSkipToNext();
//
//                skipToNext();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
//            }
//
//            @Override
//            public void onSkipToPrevious() {
//                super.onSkipToPrevious();
//
//                skipToPrevious();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
//            }
//
//            @Override
//            public void onStop() {
//                super.onStop();
//                removeNotification();
//                //Stop the service
//                stopSelf();
//            }
//
//            @Override
//            public void onSeekTo(long position) {
//                super.onSeekTo(position);
//            }
//        });
//    }
//
//    private void handleIncomingActions(Intent playbackAction) {
//        if (playbackAction == null || playbackAction.getAction() == null) return;
//
//        String actionString = playbackAction.getAction();
//        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
//            transportControls.play();
//        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
//            transportControls.pause();
//        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
//            transportControls.skipToNext();
//        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
//            transportControls.skipToPrevious();
//        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
//            transportControls.stop();
//        }
//    }
//
//    public void removeNotification() {
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(NOTIFICATION_ID);
//    }
//
//    private boolean requestAudioFocus() {
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            //Focus gained
//            return true;
//        }
//        //Could not gain focus
//        return false;
//    }
//
//
//    private boolean removeAudioFocus() {
//        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
//                audioManager.abandonAudioFocus(this);
//    }
//
//    @Override
//    public void onAudioFocusChange(int i) {
//        try {
//            switch (i) {
//
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    // resume playback
//                    if (mediaPlayer == null) initMediaPlayer();
//                    else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
//                    mediaPlayer.setVolume(1.0f, 1.0f);
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    // Lost focus for an unbounded amount of time: stop playback and release media player
//                    if (mediaPlayer.isPlaying()) mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    // Lost focus for a short time, but we have to stop
//                    // playback. We don't release the media player because playback
//                    // is likely to resume
//                    if (mediaPlayer.isPlaying()) mediaPlayer.pause();
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    // Lost focus for a short time, but it's ok to keep playing
//                    // at an attenuated level
//                    if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
//                    break;
//            }
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mediaPlayer) {
//        stopMedia();
//
//        removeNotification();
//        //stop the service
//        stopSelf();
//
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        mediaSession.release();
//        mediaPlayer.release();
//        removeNotification();
//        return super.onUnbind(intent);
//    }
//
//    @Override
//    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//        switch (i) {
//            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
//                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + i1);
//                break;
//            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
//                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + i1);
//                break;
//            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
//                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + i1);
//                break;
//        }
//        return false;
//    }
//
//    @Override
//    public void onDestroy() {
//
//        if (mediaPlayer != null) {
//            //stopMedia();
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            stopSelf();
//        }
//        removeAudioFocus();
//        //Disable the PhoneStateListener
//        if (phoneStateListener != null) {
//            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
//        }
//
//        removeNotification();
//
//        //unregister BroadcastReceivers
//        unregisterReceiver(becomingNoisyReceiver);
//        unregisterReceiver(playNewAudio);
//        unregisterReceiver(showplaypause);
//        unregisterReceiver(playnext);
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
//        return false;
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mediaPlayer) {
//        playMedia();
//
//    }
//
//    @Override
//    public void onSeekComplete(MediaPlayer mediaPlayer) {
//
//    }
//
//    public class LocalBinder extends Binder {
//        public AudioPlayerService getService() {
//            // Return this instance of LocalService so clients can call public methods
//            return AudioPlayerService.this;
//        }
//    }
//}
//*/
