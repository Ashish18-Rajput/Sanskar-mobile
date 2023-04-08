package com.sanskar.tv;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;

import java.io.IOException;

public class MediaPlayerService extends Service {

    public static final String ACTION_PLAY = "com.sanskar.tv.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.sanskar.tv.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.sanskar.tv.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.sanskar.tv.ACTION_NEXT";
    public static final String ACTION_STOP = "com.sanskar.tv.ACTION_STOP";


    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";

    public static MediaPlayer mMediaPlayer = new MediaPlayer();
    private MediaSessionManager mManager;
    private MediaSession mSession;
    private MediaController mController;
    Bundle bundle=new Bundle();
    private Bhajan[] bhajans;
    String url;
    String songname;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private  void handleIntent( Intent intent ) {
        if( intent == null || intent.getAction() == null )
            return;

        String action = intent.getAction();

        if( action.equalsIgnoreCase( ACTION_PLAY ) ) {
            mController.getTransportControls().play();
            try {
                MediaPlayerManager.startNewTrack(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if( action.equalsIgnoreCase( ACTION_PAUSE ) ) {
            mController.getTransportControls().pause();
            MediaPlayerManager.pauseMusic();
        } else if( action.equalsIgnoreCase( ACTION_FAST_FORWARD ) ) {
            mController.getTransportControls().fastForward();
        } else if( action.equalsIgnoreCase( ACTION_REWIND ) ) {
            mController.getTransportControls().rewind();
        } else if( action.equalsIgnoreCase( ACTION_PREVIOUS ) ) {
            mController.getTransportControls().skipToPrevious();
        } else if( action.equalsIgnoreCase( ACTION_NEXT ) ) {
            mController.getTransportControls().skipToNext();
        } else if( action.equalsIgnoreCase( ACTION_STOP ) ) {
            mController.getTransportControls().stop();
        }
    }

    private Notification.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
        intent.setAction( intentAction );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder( icon, title, pendingIntent ).build();
    }

    private void buildNotification( Notification.Action action ) {
        Notification.MediaStyle style = new Notification.MediaStyle();

        Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
        intent.setAction( ACTION_STOP );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        Notification.Builder builder = new Notification.Builder( this )
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle( "Media Title" )
                .setContentText( "Media Artist" )
                .setDeleteIntent( pendingIntent )
                .setStyle(style);

        builder.addAction( generateAction( android.R.drawable.ic_media_previous, "Previous", ACTION_PREVIOUS ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND ) );
        builder.addAction( action );
        builder.addAction( generateAction( android.R.drawable.ic_media_ff, "Fast Foward", ACTION_FAST_FORWARD ) );
        builder.addAction( generateAction( android.R.drawable.ic_media_next, "Next", ACTION_NEXT ) );
        style.setShowActionsInCompactView(0,1,2,3,4);

        NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );
        notificationManager.notify( 1, builder.build() );
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if( mManager == null ) {
            initMediaSessions();
        }
        url=intent.getStringExtra("url");
        songname = intent.getStringExtra("songname");

        if (bhajans==null)
        bhajans=(Bhajan[]) bundle.getSerializable("audiodetails");

        new Thread(new Runnable() {
            @Override public void run() {
                if (ACTION_PLAY.equals(intent.getAction())) {
                   /* mediaPlayer.stop();
                    mediaPlayer.reset();*/
                    Log.d("TAG", "onStartCommand#run#action_play");
                    try {
                        MediaPlayerManager.startNewTrack(url);
                        /*mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(MediaPlayerService.this);
                        mediaPlayer.setOnErrorListener(onErrorListener);
                        mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
                        duration = mediaPlayer.getDuration();
                        handler.sendEmptyMessage(MSG_UPDATE_PROGRESS);*/
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (ACTION_PAUSE.equals(intent.getAction())) {
                    Log.d("TAG", "onStartCommand#run#action_pause");
                    MediaPlayerManager.pauseMusic();
                } else if (ACTION_PLAY.equals(intent.getAction())) {
                    Log.d("TAG", "onStartCommand#run#action_resume");
                    MediaPlayerManager.startPlaying();
                }
            }
        }).start();


        handleIntent( intent );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (bundle.getSerializable("audiodetails")!=null) {
            bhajans = (Bhajan[]) bundle.getSerializable("audiodetails");
        }
    }

    private void initMediaSessions() {
        /*mMediaPlayer = new MediaPlayer();

        mMediaPlayer = new MediaPlayer();*/
        mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        //mSession = mManager.createSession("sample session");

        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mController =new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback(){
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     //mMediaPlayer.start();
                                     Log.e( "MediaPlayerService", "onPlay");
                                     buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) );
                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                   //  mMediaPlayer.pause();
                                     Log.e( "MediaPlayerService", "onPause");
                                     buildNotification(generateAction(android.R.drawable.ic_media_play, "Play", ACTION_PLAY));
                                 }

                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();
                                     Log.e( "MediaPlayerService", "onSkipToNext");
                                     //Change media here
                                     buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) );
                                 }

                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     Log.e( "MediaPlayerService", "onSkipToPrevious");
                                     //Change media here
                                     buildNotification( generateAction( android.R.drawable.ic_media_pause, "Pause", ACTION_PAUSE ) );
                                 }

                                 @Override
                                 public void onFastForward() {
                                     super.onFastForward();
                                     Log.e( "MediaPlayerService", "onFastForward");
                                     //Manipulate current media here
                                 }

                                 @Override
                                 public void onRewind() {
                                     super.onRewind();
                                     Log.e( "MediaPlayerService", "onRewind");
                                     //Manipulate current media here
                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e( "MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                     notificationManager.cancel( 1 );
                                     Intent intent = new Intent( getApplicationContext(), MediaPlayerService.class );
                                     stopService( intent );
                                 }

                                 @Override
                                 public void onSeekTo(long pos) {
                                     super.onSeekTo(pos);
                                 }

                                 @Override
                                 public void onSetRating(Rating rating) {
                                     super.onSetRating(rating);
                                 }
                             }
        );
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }
}
