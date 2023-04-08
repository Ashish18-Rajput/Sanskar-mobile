package com.sanskar.tv.module.HomeModule.Fragment;

import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.OfflineData;

import java.io.File;
import java.util.ArrayList;

public class DownloadsFragment extends Fragment {
    public static final String Broadcast_PLAY_NEW_AUDIO = "play offline audio";
    public ArrayList<OfflineData> itemlist;
    TextView textaudios, textvideos;
    RecyclerView recyclerdownloads;
    OfflineAudioVideoAdapter adapter;
    String name, size, description, title, thumb, path;
    public static boolean serviceBound = false;
    public static AudioPlayerService player;
    String checkplay="";
    public static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      /*  setSongData();
        setPlayPause();*/
        ((HomeActivityy)getContext()).handleToolbar();
        return inflater.inflate(R.layout.fragment_downloads, container, false);
    }
   /* public void setPlayPause(){
        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause,intentFilter);
    }*/
  /*  public void setSongData(){
        IntentFilter filter = new IntentFilter(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }*/
  /*  private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }
            ((HomeActivityy) context).playerlayout2.setVisibility(View.VISIBLE);
        }
    };*/
   /* private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            ((HomeActivityy) context).titleaudio2.setText(intent.getStringExtra("title"));
            ((HomeActivityy) context).descriptionaudio2.setText(Html.fromHtml(intent.getStringExtra("description")).toString());

            Ion.with(getContext()).load(intent.getStringExtra("thumb")).asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (result!=null)
                            ((HomeActivityy) context).thumbaudio2.setImageBitmap(result);
                        }
                    });

            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }

            ((HomeActivityy) context).playerlayout2.setVisibility(View.VISIBLE);
            checkplay = "true";


        }
    };*/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            serviceBound = savedInstanceState.getBoolean("serviceStatus");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout.setVisibility(View.GONE);*/

        /*if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE) {
            HomeActivityy.playerlayout.setVisibility(View.GONE);
            AudioPlayerService.mediaPlayer.stop();
            AudioPlayerService.mediaPlayer.release();
        }*/

        itemlist = new ArrayList<>();
        ((HomeActivityy) getActivity()).searchParent.setVisibility(View.GONE);
        textaudios = view.findViewById(R.id.textaudios);
        textvideos = view.findViewById(R.id.textvideos);
        recyclerdownloads = view.findViewById(R.id.recycler_downloads);
        recyclerdownloads.setLayoutManager(new LinearLayoutManager(getActivity()));

      /*  if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout.setVisibility(View.GONE);*/

        textaudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.AUDIO_OR_VIDEO = "audio";
                textaudios.setBackgroundResource(R.color.colorPrimary);
                textvideos.setBackgroundResource(R.color.whiteBgColor);
                textaudios.setTextColor(getResources().getColor(R.color.whiteBgColor));
                textvideos.setTextColor(getResources().getColor(R.color.colorBlack));
                setAudioData();
            }
        });

        textvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.AUDIO_OR_VIDEO = "video";
                textaudios.setBackgroundResource(R.color.whiteBgColor);
                textvideos.setBackgroundResource(R.color.colorPrimary);
                textvideos.setTextColor(getResources().getColor(R.color.whiteBgColor));
                textaudios.setTextColor(getResources().getColor(R.color.colorBlack));
                setVideoData();

            }
        });

        textaudios.performClick();
/*
        recyclerdownloads.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                if (Constants.AUDIO_OR_VIDEO.equals("video")) {
                    Intent intent = new Intent(getActivity(), OfflineVideoPlayer.class);
                    intent.putExtra("absolutepath", itemlist.get(index).getPath());
                    getActivity().startActivity(intent);
                } else {
                    playAudio(index);
                }
            }
        }));*/

        HomeActivityy.img_cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AudioPlayerService.mediaPlayer != null){
                    Constants.IS_PAUSEDFROMHOME="true";
                    AudioPlayerService.mediaPlayer.stop();
                    NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }
                HomeActivityy.playerlayout2.setVisibility(View.GONE);
                Constants.CALL_RESUME = "false";
            }
        });

    /*    HomeActivityy.playpause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AudioPlayerService.mediaPlayer.isPlaying()) {
                    AudioPlayerService.mediaPlayer.start();
                    HomeActivityy.playpause2.setImageResource(R.mipmap.audio_pause);

                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                    Constants.IS_PLAYING = "true";
                    intent.putExtra("isplaying", "true");
                    getActivity().sendBroadcast(intent);
                }else {
                    AudioPlayerService.mediaPlayer.pause();
                    HomeActivityy.playpause2.setImageResource(R.mipmap.audio_play);

                    Constants.IS_PLAYING = "false";
                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                    intent.putExtra("isplaying", "false");
                    getActivity().sendBroadcast(intent);
                }

            }
        });*/


        HomeActivityy.playpause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    /*private void playAudio(int audioIndex) {
        if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout.setVisibility(View.GONE);

        if (!PlayListFrag.serviceBound) {
            if (!BhajanViewAllFragment.serviceBound) {
                if (!HomeFragment.serviceBound) {
                    if (!BhajansCategoryFragment.serviceBound) {
                        if (!serviceBound) {
                            Intent intent = new Intent(getActivity(), AudioPlayerService.class);
                            intent.putExtra("index", audioIndex);
                            intent.putExtra("offlinebhajanarray", itemlist);
                            intent.putExtra("status", "download");
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            getActivity().startService(intent);
                            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("offlinebhajanarray", itemlist);
                            intent.putExtra("index", audioIndex);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            getActivity().sendBroadcast(intent);

                        }
                    } else {
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("offlinebhajanarray", itemlist);
                        intent.putExtra("index", audioIndex);
                        PreferencesHelper.getInstance().setValue("index", audioIndex);
                        getActivity().sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("offlinebhajanarray", itemlist);
                    intent.putExtra("index", audioIndex);
                    PreferencesHelper.getInstance().setValue("index", audioIndex);
                    getActivity().sendBroadcast(intent);
                }
            } else {
                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                intent.putExtra("offlinebhajanarray", itemlist);
                intent.putExtra("index", audioIndex);
                PreferencesHelper.getInstance().setValue("index", audioIndex);
                getActivity().sendBroadcast(intent);
            }
        }else{
            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
            intent.putExtra("offlinebhajanarray", itemlist);
            intent.putExtra("index", audioIndex);
            PreferencesHelper.getInstance().setValue("index", audioIndex);
            getActivity().sendBroadcast(intent);
        }

    }
*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* if (serviceBound) {
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }*/
       //TODO by sumit

       /* getActivity().unregisterReceiver(playNewAudio);
        getActivity().unregisterReceiver(playpause);
        if (checkplay.equals("true")){
            HomeActivityy.playerlayout2.setVisibility(View.GONE);
            AudioPlayerService.mediaPlayer.pause();
        }*/

       /* if( Constants.ISDOWNLOADPLAY==true){
            if (downloadmediaplayer.isPlaying()){
                ((HomeActivityy) getActivity()).playerlayout2.setVisibility(View.GONE);
                downloadmediaplayer.pause();
            }
            else {
                ((HomeActivityy) getActivity()).playerlayout3.setVisibility(View.GONE);
                downloadmediaplayer.pause();
            }}
        else{}*/
    }

    public void setAudioData() {
        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.sanskar.tv/files" + "/bhajanaudio";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        // Log.d("Files", "Size: "+ file.length);
        itemlist = new ArrayList<>();
        if (itemlist != null) {
            itemlist.clear();
        }
        if (file != null) {
            if (file.length > 0) {
                //itemlist.clear();
                for (int i = 0; i < file.length; i++) {
                    OfflineData offlineData = new OfflineData();
                    offlineData.setName(file[i].getName());
                    offlineData.setSize(file[i].getAbsoluteFile());
                    offlineData.setThumb(file[i].getAbsolutePath());
                    offlineData.setPath(file[i].getPath());


                    itemlist.add(offlineData);

                    // Log.d("FileNames",file[i].getName());
                }
                adapter = new OfflineAudioVideoAdapter(getActivity(), itemlist);
                recyclerdownloads.setAdapter(adapter);
                recyclerdownloads.setVisibility(View.VISIBLE);
            } else {
                recyclerdownloads.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Audio Available ! ", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            recyclerdownloads.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Audio Available ! ", Toast.LENGTH_SHORT).show();
        }
    }

    public void setVideoData() {
        String path = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.sanskar.tv/files" + "/bhakti";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        itemlist = new ArrayList<>();

        if (itemlist != null) {
            itemlist.clear();
        }

        if (file != null) {
            if (file.length > 0) {

                for (int i = 0; i < file.length; i++) {
                    OfflineData offlineData = new OfflineData();
                    offlineData.setName(file[i].getName());
                    offlineData.setSize(file[i].getAbsoluteFile());
                    offlineData.setThumb(file[i].getAbsolutePath());
                    offlineData.setPath(file[i].getPath());

                    itemlist.add(offlineData);

                    // Log.d("FileNames",file[i].getName());
                }
                adapter = new OfflineAudioVideoAdapter(getActivity(), itemlist);
                recyclerdownloads.setAdapter(adapter);
                recyclerdownloads.setVisibility(View.VISIBLE);

            }else {
                recyclerdownloads.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No Video Available ! ", Toast.LENGTH_SHORT).show();
            }
        } else {
            recyclerdownloads.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Video Available ! ", Toast.LENGTH_SHORT).show();
        }
    }
}
