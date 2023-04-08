package com.sanskar.tv.module.HomeModule.Fragment;


import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.model.PlayListModel;
import com.sanskar.tv.model.VideoPlaylistModel;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.MyPlayListVideoAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.R;
import com.sanskar.tv.sqlite.DatabaseManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayListFrag extends Fragment implements SearchInterface, NetworkCall.MyNetworkCallBack {

    private RecyclerView recyclerView;
    public TextView textaudios, textvideos;
    List<PlayListModel> itemlist=new ArrayList<>();
    List<PlayListModel> itemlist2=new ArrayList<>();
    List<VideoPlaylistModel> videoitemlist=new ArrayList<>();
    List<PlayListModel> alphabeticalcharlist=new ArrayList<>();
    DatabaseManager databaseManager;
    private TextView noDataFound;

    private Context ctx;
    HomeActivityy homeActivityy;
    NetworkCall networkCall;
    public static boolean serviceBound = false;
    public static AudioPlayerService player;
    List<Bhajan> bhajanList;
    SearchInterface searchInterface;
    MyPlayListAdapter adapter;
    MyPlayListVideoAdapter myPlayListVideoAdapter;
    ImageView delete_song;
    public static final String Broadcast_PLAY_NEW_AUDIO = "play playlist audio";
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

    public PlayListFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* setSongData();
        setPlayPause();*/
        ((HomeActivityy)getContext()).handleToolbar();
       ((HomeActivityy)getContext()).setSetSearchListener(searchInterface);



        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchInterface =this;
        homeActivityy=(HomeActivityy)getActivity();
    }

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy)getContext()).searchView.onActionViewCollapsed();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseManager=new DatabaseManager(getActivity());
        HomeActivityy.img_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivityy.playerlayout1.setVisibility(View.GONE);
                AudioPlayerService.mediaPlayer.pause();
            }
        });
        videoitemlist=new ArrayList<>();
        textaudios = view.findViewById(R.id.textaudios);
        textvideos = view.findViewById(R.id.textvideos);
        initView(view);

        fetchData(true);

        if (databaseManager.getAllData()!=null){
         for(int i=0;i < databaseManager.getAllData().size();i++){
           if( databaseManager.getAllData().get(i)._url.endsWith(".mp3")||databaseManager.getAllData().get(i)._url.endsWith(".mp4")||databaseManager.getAllData().get(i)._url.endsWith(".aac")||databaseManager.getAllData().get(i)._url.endsWith(".m4a")||databaseManager.getAllData().get(i)._url.endsWith(".wav")||databaseManager.getAllData().get(i)._url.endsWith(".wma")){
              // itemlist.add(databaseManager.getAllData().get(i));

           }
         }}
    //    itemlist=databaseManager.getAllData();


        if (databaseManager.getAllvideoData()!=null){
            for(int i=0;i < databaseManager.getAllvideoData().size();i++){

              /*  if( databaseManager.getAllData().get(i)._url.endsWith(".mp3")||databaseManager.getAllData().get(i)._url.endsWith(".mp4")||databaseManager.getAllData().get(i)._url.endsWith(".aac")||databaseManager.getAllData().get(i)._url.endsWith(".m4a")||databaseManager.getAllData().get(i)._url.endsWith(".wav")||databaseManager.getAllData().get(i)._url.endsWith(".wma")){
                    itemlist.add(databaseManager.getAllData().get(i));

                }
*/

                if(!( databaseManager.getAllData().get(i)._url.endsWith(".mp3")||databaseManager.getAllData().get(i)._url.endsWith(".mp4")||databaseManager.getAllData().get(i)._url.endsWith(".aac")||databaseManager.getAllData().get(i)._url.endsWith(".m4a")||databaseManager.getAllData().get(i)._url.endsWith(".wav")||databaseManager.getAllData().get(i)._url.endsWith(".wma"))){
                    videoitemlist.add(databaseManager.getAllvideoData().get(i));

                }
            }}
      /*  if (databaseManager.getAllvideoData()!=null)
            videoitemlist=databaseManager.getAllvideoData();*/
   /*     if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
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

    /*    recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                playAudio(index);
            }
        }));*/

        HomeActivityy.playpause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AudioPlayerService.mediaPlayer.isPlaying()) {
                    AudioPlayerService.mediaPlayer.start();
                    HomeActivityy.playpause1.setImageResource(R.mipmap.audio_pause);

                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                    Constants.IS_PLAYING = "true";
                    intent.putExtra("isplaying", "true");
                    getActivity().sendBroadcast(intent);
                }else {
                    AudioPlayerService.mediaPlayer.pause();
                    HomeActivityy.playpause1.setImageResource(R.mipmap.audio_play);

                    Constants.IS_PLAYING = "false";
                    Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                    intent.putExtra("isplaying", "false");
                    getActivity().sendBroadcast(intent);
                }

            }
        });
        HomeActivityy.img_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AudioPlayerService.mediaPlayer != null){
                    Constants.IS_PAUSEDFROMHOME="true";
                    AudioPlayerService.mediaPlayer.stop();
                    NotificationManager notificationManager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }
                HomeActivityy.playerlayout1.setVisibility(View.GONE);
                Constants.CALL_RESUME = "false";
            }
        });

    }

    private void fetchData(boolean b) {
        if (Utils.isConnectingToInternet(ctx)){
            networkCall=new NetworkCall(PlayListFrag.this,ctx);
            networkCall.NetworkAPICall(API.GET_PLAYLIST,b);
        }else{
            ToastUtil.showShortToast(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
   /* public void setPlayPause(){
        IntentFilter intentFilter = new IntentFilter(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
        getActivity().registerReceiver(playpause,intentFilter);
    }
    public void setSongData(){
        IntentFilter filter = new IntentFilter(BhajansCategoryFragment.BROADCAST_TITLE_SONG);
        getActivity().registerReceiver(playNewAudio, filter);
    }
*/
   /* private BroadcastReceiver playpause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }
            ((HomeActivityy) context).playerlayout1.setVisibility(View.VISIBLE);
        }
    };
*/


  /*  private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            ((HomeActivityy) context).titleaudio1.setText(intent.getStringExtra("title"));
            ((HomeActivityy) context).descriptionaudio1.setText(Html.fromHtml(intent.getStringExtra("description")).toString());

            Ion.with(getContext()).load(intent.getStringExtra("thumb")).asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            ((HomeActivityy) context).thumbaudio1.setImageBitmap(result);
                        }
                    });

            if (intent.getStringExtra("isplaying").equals("true")){
                ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);
            }else if (intent.getStringExtra("isplaying").equals("false")){
                ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);
                Constants.IS_PLAYING = "false";
            }

            ((HomeActivityy) context).playerlayout1.setVisibility(View.VISIBLE);
            checkplay = "true";


        }
    };
*/
    private void playAudio(int audioIndex) {
        if (!DownloadsFragment.serviceBound) {
            if (!BhajanViewAllFragment.serviceBound) {
                if (!HomeFragment.serviceBound) {
                    if (!BhajansCategoryFragment.serviceBound) {
                        if (!serviceBound) {
                            Intent intent = new Intent(getActivity(), AudioPlayerService.class);
                            intent.putExtra("index", audioIndex);
                            intent.putExtra("playlist", (Serializable) itemlist);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            getActivity().startService(intent);
                            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("playlist", (Serializable) itemlist);
                            intent.putExtra("index", audioIndex);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            getActivity().sendBroadcast(intent);

                        }
                    } else {
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("playlist", (Serializable) itemlist);
                        intent.putExtra("index", audioIndex);
                        PreferencesHelper.getInstance().setValue("index", audioIndex);
                        getActivity().sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("playlist", (Serializable) itemlist);
                    intent.putExtra("index", audioIndex);
                    PreferencesHelper.getInstance().setValue("index", audioIndex);
                    getActivity().sendBroadcast(intent);
                }
            } else {
                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                intent.putExtra("playlist", (Serializable) itemlist);
                intent.putExtra("index", audioIndex);
                PreferencesHelper.getInstance().setValue("index", audioIndex);
                getActivity().sendBroadcast(intent);
            }
        }else{
            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
            intent.putExtra("playlist", (Serializable) itemlist);
            intent.putExtra("index", audioIndex);
            PreferencesHelper.getInstance().setValue("index", audioIndex);
            getActivity().sendBroadcast(intent);
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
       /* if (serviceBound) {
            getActivity().unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();

        ((HomeActivityy) ctx).invalidateOptionsMenu();
        ((HomeActivityy) ctx).handleToolbar();
    }

    private void initView(View view) {
        ctx = getActivity();

        recyclerView = view.findViewById(R.id.recycler_view);
        noDataFound = view.findViewById(R.id.no_data_found);

        String bhajanString = PreferencesHelper.getInstance().getBhajanString(Constants.MY_PLAY_LIST);

        bhajanList = new ArrayList<>();
/*
        if (!TextUtils.isEmpty(bhajanString)) {

            try {
                JSONArray jsonArray = new JSONArray(bhajanString);
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        BhajanCat bhajan = new Gson().fromJson(jsonObject.toString(), BhajanCat.class);
                        bhajanList.add(bhajan);
                    }
                } else {
                    noDataFound.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            noDataFound.setVisibility(View.VISIBLE);
        }*/

       /* if (itemlist!=null){
        adapter = new MyPlayListAdapter(itemlist, alphabeticalcharlist,ctx);

            recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayout.VERTICAL,
                    false));
            recyclerView.setAdapter(adapter);
        }else{
            noDataFound.setVisibility(View.VISIBLE);
        }
*/
            /* MyPlayListAdapter adapter = new MyPlayListAdapter(bhajanList, ctx);
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayout.VERTICAL,
                    false));
            recyclerView.setAdapter(adapter); */
        }

    @Override
    public void textchanged(String Query) {
        adapter.filter(Query);
    }
    private void setVideoData() {
        if (videoitemlist!=null){
            myPlayListVideoAdapter = new MyPlayListVideoAdapter(ctx,videoitemlist);

            recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayout.VERTICAL,
                    false));
            recyclerView.setAdapter(myPlayListVideoAdapter);
        }else{
            noDataFound.setVisibility(View.VISIBLE);
        }
    }
    private void setAudioData() {
        if (itemlist!=null){
            adapter = new MyPlayListAdapter(itemlist, alphabeticalcharlist,ctx,PlayListFrag.this);

            recyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayout.VERTICAL,
                    false));
            recyclerView.setAdapter(adapter);
        }else{
            noDataFound.setVisibility(View.VISIBLE);
        }

    }

    public void update(){
        fetchData(true);
    }


    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion=null;
        if (apitype.equalsIgnoreCase(API.GET_PLAYLIST)){
            ion= (Builders.Any.B) Ion.with(ctx).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(homeActivityy).getString(Constants.JWT)!=null?SharedPreference.getInstance(homeActivityy).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",homeActivityy.signupResponse.getId())
                    .setMultipartParameter("user_id",homeActivityy.signupResponse.getId());
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.GET_PLAYLIST)){
            if (jsonstring.optBoolean("status")){
                JSONArray jsonArray=jsonstring.optJSONArray("data");
                JSONArray jsonArray1=jsonArray.optJSONObject(0).optJSONArray("bhajan");
                if (itemlist.size()>0){
                    itemlist.clear();
                }
                for (int i=0;i<jsonArray1.length();i++){
                    PlayListModel playListModel=new Gson().fromJson(jsonArray1.opt(i).toString(),PlayListModel.class);
                    itemlist.add(playListModel);
                }
                adapter.notifyDataSetChanged();
            }else{

            }
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}


