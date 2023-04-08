package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.model.PlayListModel;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.R;
import com.sanskar.tv.sqlite.DatabaseManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

/**
 * Created by kapil on 9/5/18.
 */

public class MyPlayListAdapter extends RecyclerView.Adapter<MyPlayListAdapter.ViewHolder> implements NetworkCall.MyNetworkCallBack {

    List<PlayListModel> bhajanList=new ArrayList<>();
    List<PlayListModel> alphabeticalcharlist;
    Context context;
    DatabaseManager databaseManager;
    int POSITION=-1;
    Fragment fragment;
    NetworkCall networkCall;

    public static int playlistpos,playlistplayedposition;
    public static MediaPlayer playlistmediaplayer;

    public MyPlayListAdapter(List<PlayListModel> bhajanList, List<PlayListModel> alphabeticalcharlist, Context context, Fragment fragment) {
        this.bhajanList = bhajanList;
        this.context = context;
        this.alphabeticalcharlist = new ArrayList<>();
        this.alphabeticalcharlist.addAll(bhajanList);
        this.fragment=fragment;
    }

    @Override
    public MyPlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPlayListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_guru_list, null));
    }

    @Override
    public void onBindViewHolder(final MyPlayListAdapter.ViewHolder holder, final int position) {


        PlayListModel mModel=bhajanList.get(position);
        Ion.with(context).load(bhajanList.get(position).getThumbnail1()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                //Bitmap roundedImg = RoundedCornerImage.getRoundedCornerBitmap(result, 30);
                holder.artistImage.setImageBitmap(result);
            }
        });
        holder.channelName.setText(bhajanList.get(position).getTitle());
        String desc = Html.fromHtml(bhajanList.get(position).getDescription()).toString();
        holder.description.setText(desc);

        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio(position);
            }
        });
        holder.delete_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POSITION=position;
                if (Utils.isConnectingToInternet(context)){
                    networkCall=new NetworkCall(MyPlayListAdapter.this,context);
                    networkCall.NetworkAPICall(API.ADD_TO_PLAYLIST,false);
                }else{
                    ToastUtil.showShortToast(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
               if(AudioPlayerService.mediaPlayer!=null){
                AudioPlayerService.mediaPlayer.stop();
                HomeActivityy.playerlayout1.setVisibility(View.GONE);


               /* databaseManager=new DatabaseManager(context);
                databaseManager.deleteContact(new PlayListModel(mModel.get_name(),mModel.getImg_url(),
                        mModel.get_url()));*/

                bhajanList.remove(position);
                notifyDataSetChanged();}
               else {

                   HomeActivityy.playerlayout1.setVisibility(View.GONE);

                   /*databaseManager=new DatabaseManager(context);
                   databaseManager.deleteContact(new PlayListModel(mModel.get_name(),mModel.getImg_url(),
                           mModel.get_url()));*/

                   bhajanList.remove(position);
                   notifyDataSetChanged();
               }
            }
        });


       /* holder.artistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                playAudio(position);
            }
        });*/



        ((HomeActivityy)context).forward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Constants.ISDOWNLOADPLAY=true;
                if ((bhajanList.size() - 1) > playlistplayedposition) {
                    playlistpos = playlistplayedposition + 1;
                    final PlayListModel playListdata=bhajanList.get(playlistpos);


                    ((HomeActivityy) context).playerlayout1.setVisibility(View.VISIBLE);
                  /*  Ion.with(context).load(offlineData.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });*/
                    ((HomeActivityy) context).titleaudio1.setText(playListdata.get_name());
                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);
                    ((HomeActivityy) context).descriptionaudio1.setVisibility(View.GONE);
                    Ion.with(context).load(playListdata.getImg_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio1.setImageBitmap(result);
                        }
                    });

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying()){
                            AudioPlayerService.mediaPlayer.pause();}


                        if(playlistmediaplayer  !=null){
                            if(playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.reset();}
                            else{}}

                        playlistmediaplayer = new MediaPlayer();
                        try {
                            playlistmediaplayer.setDataSource(playListdata.get_url());
                            playlistmediaplayer.prepare();
                            playlistmediaplayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playlistmediaplayer.stop();
                                // dialog.dismiss();
                                ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);
                            }
                        });

                        ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!playlistmediaplayer.isPlaying()) {
                                    playlistmediaplayer.start();
                                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                }else {
                                    playlistmediaplayer.pause();
                                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                }





                            }
                        });
                    }
                    else {
                        if(playlistmediaplayer  !=null){
                            if(playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.reset();}
                            else{}}

                        playlistmediaplayer = new MediaPlayer();
                            try {
                                playlistmediaplayer.setDataSource(playListdata.get_url());
                                playlistmediaplayer.prepare();
                                playlistmediaplayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playlistmediaplayer.stop();
                                    // dialog.dismiss();
                                    ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);


                                }
                            });

                            ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!playlistmediaplayer.isPlaying()) {
                                        playlistmediaplayer.start();
                                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                    }else {
                                        playlistmediaplayer.pause();
                                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                    }





                                }
                            });

                        }
                    playlistplayedposition=playlistpos;
                }

                else{
                    Toast.makeText(context, "You are playing the last song", Toast.LENGTH_SHORT).show();
                }
            }
        });



        ((HomeActivityy) context).backward1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Constants.ISDOWNLOADPLAY=true;
                if ( playlistplayedposition>0) {
                    playlistpos = playlistplayedposition - 1;
                    final   PlayListModel playListdata =bhajanList.get(playlistpos);

                    ((HomeActivityy) context).playerlayout1.setVisibility(View.VISIBLE);
             /*       Ion.with(context).load(forwardbhajan.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });*/
                    ((HomeActivityy) context).titleaudio1.setText(playListdata.get_name());
                    ((HomeActivityy) context).descriptionaudio1.setVisibility(View.GONE);
                    Ion.with(context).load(playListdata.getImg_url()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio1.setImageBitmap(result);
                        }
                    });

                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying()){
                            AudioPlayerService.mediaPlayer.pause();}



                        if(playlistmediaplayer  !=null){
                            if(playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.reset();}
                            else{}}

                        playlistmediaplayer = new MediaPlayer();
                        try {
                            playlistmediaplayer.setDataSource(playListdata.get_url());
                            playlistmediaplayer.prepare();
                            playlistmediaplayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                playlistmediaplayer.stop();
                                // dialog.dismiss();
                                ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);
                                /*count=0;*/


                            }
                        });

                        ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!playlistmediaplayer.isPlaying()) {
                                    playlistmediaplayer.start();
                                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                }else {
                                    playlistmediaplayer.pause();
                                    ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                }




                            }
                        });
                    }
                    else {
                        if(playlistmediaplayer  !=null){
                            if(playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.reset();}
                            else{}}

                        playlistmediaplayer = new MediaPlayer();
                            try {
                                playlistmediaplayer.setDataSource(playListdata.get_url());
                                playlistmediaplayer.prepare();
                                playlistmediaplayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    playlistmediaplayer.stop();
                                    // dialog.dismiss();
                                    ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);
                                    /*count=0;*/


                                }
                            });

                            ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!playlistmediaplayer.isPlaying()) {
                                        playlistmediaplayer.start();
                                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                    }else {
                                        playlistmediaplayer.pause();
                                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                    }




                                }
                            });


                        }
                    playlistplayedposition=playlistpos;
                }

                else{
                    Toast.makeText(context, "You are playing the first song", Toast.LENGTH_SHORT).show();
                }
            }
        });



       /* holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PLAY_LIST_DATA, (Serializable) bhajanList);
                bundle.putInt("position", position);
                Fragment fragment = new BhajanPlayFragment();
                fragment.setArguments(bundle);
                ((HomeActivityy) context)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("MyPlayList")
                        .replace(R.id.container_layout_home, fragment)
                        .commit();
            }
        });*/
    }
   /* private void playAudio(int audioIndex) {
        if (!DownloadsFragment.serviceBound) {
            if (!BhajanViewAllFragment.serviceBound) {
                if (!HomeFragment.serviceBound) {
                    if (!BhajansCategoryFragment.serviceBound) {
                        if (!serviceBound) {
                            Intent intent = new Intent(context, AudioPlayerService.class);
                            intent.putExtra("index", audioIndex);
                            intent.putExtra("playlist", (Serializable) bhajanList);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            context.startService(intent);
                            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("playlist", (Serializable) bhajanList);
                            intent.putExtra("index", audioIndex);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            context.sendBroadcast(intent);
                        }
                    } else {
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("playlist", (Serializable) bhajanList);
                        intent.putExtra("index", audioIndex);
                        PreferencesHelper.getInstance().setValue("index", audioIndex);
                        context.sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("playlist", (Serializable) bhajanList);
                    intent.putExtra("index", audioIndex);
                    PreferencesHelper.getInstance().setValue("index", audioIndex);
                    context.sendBroadcast(intent);
                }
            } else {
                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                intent.putExtra("playlist", (Serializable) bhajanList);
                intent.putExtra("index", audioIndex);
                PreferencesHelper.getInstance().setValue("index", audioIndex);
                context.sendBroadcast(intent);
            }
        }else{
            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
            intent.putExtra("playlist", (Serializable) bhajanList);
            intent.putExtra("index", audioIndex);
            PreferencesHelper.getInstance().setValue("index", audioIndex);
            context.sendBroadcast(intent);
        }
    }*/




    private void playAudio(int audioIndex) {
      //  Constants.ISDOWNLOADPLAY=true;

        if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout.setVisibility(View.GONE);

        if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout2.setVisibility(View.GONE);

        playlistplayedposition=audioIndex;

        ((HomeActivityy) context).playerlayout1.setVisibility(View.VISIBLE);
        ((HomeActivityy) context).titleaudio1.setText(bhajanList.get(audioIndex).getTitle().trim());
        ((HomeActivityy) context).descriptionaudio1.setVisibility(View.GONE);
           Ion.with(context).load(bhajanList.get(audioIndex).getThumbnail1()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio1.setImageBitmap(result);
                        }
                    });

           ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

        if (AudioPlayerService.mediaPlayer != null) {
            if (AudioPlayerService.mediaPlayer.isPlaying()){
                AudioPlayerService.mediaPlayer.pause();}
            if (downloadmediaplayer != null) {
                if (downloadmediaplayer.isPlaying()){
                    downloadmediaplayer.pause();}}
            else{

            }

            if(playlistmediaplayer  !=null){
                if(playlistmediaplayer.isPlaying()){
                    playlistmediaplayer.reset();}
                else{}}

            playlistmediaplayer = new MediaPlayer();
            try {
                playlistmediaplayer.setDataSource(bhajanList.get(audioIndex).getMedia_file());
                playlistmediaplayer.prepare();
                playlistmediaplayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


            ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playlistmediaplayer.stop();
                    // dialog.dismiss();
                    ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);
                    // count=0;


                }
            });

            ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!playlistmediaplayer.isPlaying()) {
                        playlistmediaplayer.start();
                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                    }else {
                        playlistmediaplayer.pause();
                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                    }





                }
            });

        }
        else {

            if (downloadmediaplayer != null) {
                if (downloadmediaplayer.isPlaying()){
                    downloadmediaplayer.pause();}}
            else{

            }

          /*  if (playlistmediaplayer != null) {
                if (playlistmediaplayer.isPlaying()){
                    playlistmediaplayer.pause();}}
            else{

            }*/
          /*  if(playlistmediaplayer.isPlaying()){
                playlistmediaplayer.stop();}*/

            if(playlistmediaplayer  !=null){
                if(playlistmediaplayer.isPlaying()){
                    playlistmediaplayer.reset();}
                else{}}

            playlistmediaplayer = new MediaPlayer();

            try {
                playlistmediaplayer.setDataSource(bhajanList.get(audioIndex).getMedia_file());
                playlistmediaplayer.prepare();
                playlistmediaplayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


            ((HomeActivityy) context).img_cancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playlistmediaplayer.stop();
                    // dialog.dismiss();
                    ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);
                    // count=0;


                }
            });

            ((HomeActivityy) context).playpause1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!playlistmediaplayer.isPlaying()) {
                        playlistmediaplayer.start();
                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                    }else {
                        playlistmediaplayer.pause();
                        ((HomeActivityy) context).playpause1.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                    }





                }
            });

        }



      /*  if (!PlayListFrag.serviceBound) {
            if (!BhajanViewAllFragment.serviceBound) {
                if (!serviceBound) {
                    if (!BhajansCategoryFragment.serviceBound) {
                        if (!serviceBound) {
                            Intent intent = new Intent(context, AudioPlayerService.class);
                            intent.putExtra("index", audioIndex);
                            intent.putExtra("offlinebhajanarray", itemlist);
                            intent.putExtra("status", "download");
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            context.startService(intent);
                            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

                        } else {
                            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                            intent.putExtra("offlinebhajanarray", itemlist);
                            intent.putExtra("index", audioIndex);
                            PreferencesHelper.getInstance().setValue("index", audioIndex);
                            context.sendBroadcast(intent);

                        }
                    } else {
                        Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                        intent.putExtra("offlinebhajanarray", itemlist);
                        intent.putExtra("index", audioIndex);
                        PreferencesHelper.getInstance().setValue("index", audioIndex);
                        context.sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                    intent.putExtra("offlinebhajanarray", itemlist);
                    intent.putExtra("index", audioIndex);
                    PreferencesHelper.getInstance().setValue("index", audioIndex);
                    context.sendBroadcast(intent);
                }
            } else {
                Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
                intent.putExtra("offlinebhajanarray", itemlist);
                intent.putExtra("index", audioIndex);
                PreferencesHelper.getInstance().setValue("index", audioIndex);
                context.sendBroadcast(intent);
            }
        }else{
            Intent intent = new Intent(BhajansCategoryFragment.Broadcast_PLAY_NEW_AUDIO);
            intent.putExtra("offlinebhajanarray", itemlist);
            intent.putExtra("index", audioIndex);
            PreferencesHelper.getInstance().setValue("index", audioIndex);
            context.sendBroadcast(intent);
        }*/
    }

    @Override
    public int getItemCount() {
        return bhajanList.size();
    }
    public void filter(String charText) {
        bhajanList.clear();
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            bhajanList.addAll(alphabeticalcharlist);
        } else {
            for (PlayListModel wp : alphabeticalcharlist) {
                if (wp.get_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    bhajanList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion=null;
        if (apitype.equalsIgnoreCase(API.ADD_TO_PLAYLIST)){
            ion= (Builders.Any.B) Ion.with(context)
                    .load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("bhajan_id", bhajanList.get(POSITION).getId())
                    .setMultipartParameter("type", "2");
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.ADD_TO_PLAYLIST)){
            if (jsonstring.optBoolean("status")){

               // ((PlayListFrag)fragment).update();
               // ((PlayListFrag)fragment).textaudios.performClick();
            }
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage,delete_song;
        TextView channelName, description;
        ConstraintLayout singleItem;


        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item_guru);
            channelName = itemView.findViewById(R.id.channel_name_single_item_guru);
            description = itemView.findViewById(R.id.description_single_item_guru);
            singleItem = itemView.findViewById(R.id.single_item_guru);
            delete_song=itemView.findViewById(R.id.delete_song);
        }
    }
}
