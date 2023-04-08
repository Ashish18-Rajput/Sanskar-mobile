package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;
import com.sanskar.tv.jwPlayer.OfflineVideoPlayer;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.OfflineData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;

public class OfflineAudioVideoAdapter extends RecyclerView.Adapter<OfflineAudioVideoAdapter.MyViewHolder> {
    Context context;
    ArrayList<OfflineData> itemlist;
    String path1;
    Bitmap bitmapaudio;


    public static int pos,playedposition;
    public static MediaPlayer downloadmediaplayer;
    public OfflineAudioVideoAdapter(Context context, ArrayList<OfflineData> itemlist) {
        this.context = context;
        this.itemlist = itemlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemofflineaudio, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.texttitlesong.setText(itemlist.get(position).getName().trim());


        holder.delete_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.AUDIO_OR_VIDEO.equals("audio")){
                    File file = new File(context.getExternalFilesDir("bhajanaudio"),itemlist.get(position).getName());
                    if (file.exists()){

                        file.delete();
                        itemlist.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context,"File Deleted Successfully",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
               else if (Constants.AUDIO_OR_VIDEO.equals("video")){
                    File file = new File(context.getExternalFilesDir("bhakti"),itemlist.get(position).getName());

                    if (file.exists()){
                        file.delete();
                        itemlist.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context,"File Deleted Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "File Not Found", Toast.LENGTH_SHORT).show();
                    }
               }
            }
        });

        Glide.with(context)
                .load(itemlist.get(position).getThumb())
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.AUDIO_OR_VIDEO.equals("video")) {
                    Intent intent = new Intent(context, OfflineVideoPlayer.class);
                    intent.putExtra("absolutepath", itemlist.get(position).getPath());
                    context.startActivity(intent);
                } else {
                    playAudio(position);
                }
            }
        });



        File filesize = itemlist.get(position).getSize();
        long size = filesize.length();
        size = size  / (1024 * 1024);
        holder.textsize.setText((int) size + " MB");
        if (Constants.AUDIO_OR_VIDEO.equals("audio")){
            holder.imgaudio.setVisibility(View.GONE);
        }else if (Constants.AUDIO_OR_VIDEO.equals("video")){
            holder.imgaudio.setVisibility(View.VISIBLE);
        }
        String path = itemlist.get(position).getPath();

        File file = itemlist.get(position).getSize();
        path1 = file.getAbsolutePath();

        /*path = path.substring(0,path.length() - 6);
        path = path + "mp3";*/
        if (Constants.AUDIO_OR_VIDEO.equals("video"))
//        holder.thumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path1,MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));
            holder.thumbnail.setImageBitmap(ThumbnailUtils.createVideoThumbnail(path1,MediaStore.Video.Thumbnails.MICRO_KIND));
        if (Constants.AUDIO_OR_VIDEO.equals("audio")) {
             bitmapaudio = BitmapFactory.decodeFile(path1);


            if (bitmapaudio!=null) {
                holder.thumbnail.setImageBitmap(bitmapaudio);
            }else{

            }
        }




            //holder.thumbnail.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path1),100,100));
       // holder.thumbnail.setImageBitmap(imgbitmap);

      /*  holder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = itemlist.get(position).getPath();

                if (Constants.AUDIO_OR_VIDEO.equals("audio")) {



                   */
        /* path = path.substring(0,path.length() - 6);
                    path = path + "mp3";*/
        /*
                } else if (Constants.AUDIO_OR_VIDEO.equals("video")) {
                    Intent intent = new Intent(context,LiveStreamJWActivity.class);
                   */
        /* path1 = path1.substring(0,path1.length()-3);
                    path1 = path1 + "mp4";*/
        /*

                    intent.putExtra("livevideourl",path1);
                    context.startActivity(intent);


                    */
        /*path = path.substring(0,path.length() - 3);
                    path = path + "mp4";*/
        /*
                }

            }
        });*/




        ((HomeActivityy)context).forward2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.ISDOWNLOADPLAY=true;
                if ((itemlist.size() - 1) > playedposition) {
                    pos = playedposition + 1;
                    final OfflineData offlineData=itemlist.get(pos);


                    ((HomeActivityy) context).playerlayout2.setVisibility(View.VISIBLE);
                  /*  Ion.with(context).load(offlineData.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });*/
                    ((HomeActivityy) context).titleaudio2.setText(offlineData.getName());
                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);
                    ((HomeActivityy) context).descriptionaudio2.setVisibility(View.GONE);

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying()){
                            AudioPlayerService.mediaPlayer.pause();}

                        if(playlistmediaplayer!=null){
                            if (playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.pause();}}
                        else{

                        }

                        if(downloadmediaplayer  !=null){
                            if(downloadmediaplayer.isPlaying()){
                                downloadmediaplayer.reset();}
                            else{}}


                        downloadmediaplayer = new MediaPlayer();
                        try {
                            downloadmediaplayer.setDataSource(offlineData.getPath());
                            downloadmediaplayer.prepare();
                            downloadmediaplayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadmediaplayer.stop();
                                // dialog.dismiss();
                                ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);






                            }
                        });

                        ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!downloadmediaplayer.isPlaying()) {
                                    downloadmediaplayer.start();
                                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                }else {
                                    downloadmediaplayer.pause();
                                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                }





                            }
                        });

                    }




                    else {
                        if(playlistmediaplayer!=null){
                            if (playlistmediaplayer.isPlaying()){
                                playlistmediaplayer.pause();}}
                        else{

                        }

                        if(downloadmediaplayer  !=null){
                            if(downloadmediaplayer.isPlaying()){
                                downloadmediaplayer.reset();}
                            else{}}




                        downloadmediaplayer = new MediaPlayer();
                            try {
                                downloadmediaplayer.setDataSource(offlineData.getPath());
                                downloadmediaplayer.prepare();
                                downloadmediaplayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downloadmediaplayer.stop();
                                    // dialog.dismiss();
                                    ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);






                                }
                            });

                            ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!downloadmediaplayer.isPlaying()) {
                                        downloadmediaplayer.start();
                                        ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                    }else {
                                        downloadmediaplayer.pause();
                                        ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                    }





                                }
                            });

                        }

                    playedposition=pos;

                }
                else{
                    Toast.makeText(context, "You are playing the last song", Toast.LENGTH_SHORT).show();
                }
            }
        });



        ((HomeActivityy) context).backward2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.ISDOWNLOADPLAY=true;
                if ( playedposition>0) {
                    pos = playedposition - 1;
                    final   OfflineData offlineData =itemlist.get(pos);

                    ((HomeActivityy) context).playerlayout2.setVisibility(View.VISIBLE);
             /*       Ion.with(context).load(forwardbhajan.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });*/
                    ((HomeActivityy) context).titleaudio2.setText(offlineData.getName());
                    ((HomeActivityy) context).descriptionaudio2.setVisibility(View.GONE);
                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying()){
                            AudioPlayerService.mediaPlayer.pause();}


                        if(downloadmediaplayer  !=null){
                            if(downloadmediaplayer.isPlaying()){
                                downloadmediaplayer.reset();}
                            else{}}

                        downloadmediaplayer = new MediaPlayer();
                        try {
                            downloadmediaplayer.setDataSource(offlineData.getPath());
                            downloadmediaplayer.prepare();
                            downloadmediaplayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                downloadmediaplayer.stop();
                                // dialog.dismiss();
                                ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);
                                /*count=0;*/


                            }
                        });

                        ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!downloadmediaplayer.isPlaying()) {
                                    downloadmediaplayer.start();
                                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                }else {
                                    downloadmediaplayer.pause();
                                    ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                }




                            }
                        });


                    }
                    else {
                        if(downloadmediaplayer  !=null){
                            if(downloadmediaplayer.isPlaying()){
                                downloadmediaplayer.reset();}
                            else{}}

                            downloadmediaplayer = new MediaPlayer();
                            try {
                                downloadmediaplayer.setDataSource(offlineData.getPath());
                                downloadmediaplayer.prepare();
                                downloadmediaplayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    downloadmediaplayer.stop();
                                    // dialog.dismiss();
                                    ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);
                                    /*count=0;*/


                                }
                            });

                            ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!downloadmediaplayer.isPlaying()) {
                                        downloadmediaplayer.start();
                                        ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                    }else {
                                        downloadmediaplayer.pause();
                                        ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                    }




                                }
                            });

                        }
                    playedposition=pos;

                }

                else{
                    Toast.makeText(context, "You are playing the first song", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

     private void playAudio(int audioIndex) {
         Constants.ISDOWNLOADPLAY=true;

         if (HomeActivityy.playerlayout.getVisibility() == View.VISIBLE)
            HomeActivityy.playerlayout.setVisibility(View.GONE);

         if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE)
             HomeActivityy.playerlayout1.setVisibility(View.GONE);

         playedposition=audioIndex;
         ((HomeActivityy) context).playerlayout2.setVisibility(View.VISIBLE);
        /* Ion.with(context).load(itemlist.get(audioIndex).getThumb()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
             @Override
             public void onCompleted(Exception e, Bitmap result) {
                 //holder.artistImage.setImageBitmap(result);
                 ((HomeActivityy) context).thumbaudio2.setImageBitmap(result);
             }
         });*/
        // ((HomeActivityy) context).thumbaudio2.setImageBitmap(bitmapaudio);

         ((HomeActivityy) context).titleaudio2.setText(itemlist.get(audioIndex).getName().trim());
         ((HomeActivityy) context).descriptionaudio2.setVisibility(View.GONE);
         ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);
         if (AudioPlayerService.mediaPlayer != null) {
             if (AudioPlayerService.mediaPlayer.isPlaying()){
                 AudioPlayerService.mediaPlayer.pause();}
             if(playlistmediaplayer!=null){
                 if (playlistmediaplayer.isPlaying()){
                     playlistmediaplayer.pause();}}
             else{

             }

             if(downloadmediaplayer  !=null){
                 if(downloadmediaplayer.isPlaying()){
                     downloadmediaplayer.reset();}
                 else{}}

             downloadmediaplayer = new MediaPlayer();
                 try {
                     downloadmediaplayer.setDataSource(itemlist.get(audioIndex).getPath());
                     downloadmediaplayer.prepare();
                     downloadmediaplayer.start();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


                 ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         downloadmediaplayer.stop();
                         // dialog.dismiss();
                         ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);
                         // count=0;


                     }
                 });

                 ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {

                         if (!downloadmediaplayer.isPlaying()) {
                             downloadmediaplayer.start();
                             ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                         }else {
                             downloadmediaplayer.pause();
                             ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                         }





                     }
                 });

         }
         else {

             if(playlistmediaplayer!=null){
                 if (playlistmediaplayer.isPlaying()){
                     playlistmediaplayer.pause();}}
             else{

             }


             if(downloadmediaplayer  !=null){
                 if(downloadmediaplayer.isPlaying()){
                     downloadmediaplayer.reset();}
                 else{}}

             downloadmediaplayer = new MediaPlayer();
             try {
                 downloadmediaplayer.setDataSource(itemlist.get(audioIndex).getPath());
                 downloadmediaplayer.prepare();
                 downloadmediaplayer.start();
             } catch (IOException e) {
                 e.printStackTrace();
             }


             ((HomeActivityy) context).img_cancel2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     downloadmediaplayer.stop();
                     // dialog.dismiss();
                     ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);
                    // count=0;


                 }
             });

             ((HomeActivityy) context).playpause2.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     if (!downloadmediaplayer.isPlaying()) {
                         downloadmediaplayer.start();
                         ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                     }else {
                         downloadmediaplayer.pause();
                         ((HomeActivityy) context).playpause2.setImageResource(R.mipmap.audio_play);

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
        return itemlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView texttitlesong,textsize;
        ImageView imgaudio;
        LinearLayout parentlayout;
        ImageView thumbnail,delete_song;
        public MyViewHolder(View itemView) {
            super(itemView);
            texttitlesong = itemView.findViewById(R.id.titlesong);
            textsize = itemView.findViewById(R.id.textsize);
            imgaudio = itemView.findViewById(R.id.imgvideo);
            parentlayout = itemView.findViewById(R.id.parentlayout);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            delete_song=itemView.findViewById(R.id.delete_song);
        }
    }
}
