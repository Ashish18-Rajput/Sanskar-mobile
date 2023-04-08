package com.sanskar.tv.module.HomeModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;

import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

/**
 * Created by kapil on 30/4/18.
 */

public class AudioPlayAdapter extends RecyclerView.Adapter<AudioPlayAdapter.ViewHolder>
        implements NetworkCall.MyNetworkCallBack{

    private List<Bhajan> list;
    Context context;
    private NetworkCall networkCall;
   public static String checkplayguru;
    String video_id;
    int count=0;

public static int pos,playedposition;
    public static MediaPlayer mp;
    public AudioPlayAdapter(List<Bhajan> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public AudioPlayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AudioPlayAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item_related_guru,null));
    }

    @Override
    public void onBindViewHolder(final AudioPlayAdapter.ViewHolder holder, final int position) {

        final Bhajan audioBean = list.get(position);
        Ion.with(context).load(audioBean.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                holder.artistImage.setImageBitmap(result);
            }
        });
        holder.channelName.setText(audioBean.getArtist_name());

        String desc = Html.fromHtml(audioBean.getDescription()).toString();

        holder.description.setText(desc);
        holder.date.setText(getDate(Long.parseLong(audioBean.getCreation_time())));
        holder.viewsNumber.setText(audioBean.getLikes()+" likes");
        mp = new MediaPlayer();

        holder.layout.setTag(audioBean.getId());

       /* holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count< 1) {
                    count=count+1;
                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                    dialog.setContentView(R.layout.popupplay);

                    dialog.setCancelable(false);
                    dialog.show();
                    TextView titletext = dialog.findViewById(R.id.titletext);
                    titletext.setText(audioBean.getTitle());
                    final ImageView imageView = dialog.findViewById(R.id.pauseplay);
                    Ion.with(context).load(audioBean.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            imageView.setImageBitmap(result);
                        }
                    });
                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying())
                            AudioPlayerService.mediaPlayer.pause();
                    } else {
                        final MediaPlayer mp = new MediaPlayer();
                        try {
                            mp.setDataSource(audioBean.getMedia_file());
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ImageView imageView1 = dialog.findViewById(R.id.cancelbutton);
                        imageView1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mp.stop();
                                dialog.dismiss();
                                count=0;
                                //mp.release();
                            }
                        });
                    }

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

              *//*  Bundle bundle = new Bundle();
//                    bundle.putSerializable("guruAudioData", (Serializable) audioBean);
                    bundle.putInt("guruAudioData", position);
                    Fragment fragment = new BhajanPlayFragment();
                    fragment.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJANS")
                            .replace(R.id.container_layout_home, fragment)
              }              .commit();*//*
                }
                else{

                }
            }
        });*/



       // --------------------------------------------------
        ((HomeActivityy) context).forward3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.ISGURUPLAY=true;
                if ((list.size() - 1) > playedposition) {
                    pos = playedposition + 1;
                  final   Bhajan forwardbhajan=list.get(pos);


                    ((HomeActivityy) context).playerlayout3.setVisibility(View.VISIBLE);
                    Ion.with(context).load(forwardbhajan.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });
                    ((HomeActivityy) context).titleaudio3.setText(forwardbhajan.getTitle());
                    ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);
                    ((HomeActivityy) context).descriptionaudio3.setVisibility(View.GONE);

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying())
                            AudioPlayerService.mediaPlayer.pause();
                    }
                    else {
                        if(mp!=null){
                        if(mp.isPlaying()){
                            mp.reset();}
                        else{}}

                        mp = new MediaPlayer();
                     //   mp = new MediaPlayer();
                        try {
                            mp.setDataSource(forwardbhajan.getMedia_file());
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        ((HomeActivityy) context).img_cancel3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mp.stop();
                                // dialog.dismiss();
                                ((HomeActivityy) context).playerlayout3.setVisibility(View.GONE);



                                count=0;


                            }
                        });

                        ((HomeActivityy) context).playpause3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (!mp.isPlaying()) {
                                    mp.start();
                                    ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                }else {
                                    mp.pause();
                                    ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                }





                            }
                        });
                            playedposition=pos;

                }}

                else{
                    Toast.makeText(context, "You are playing the last song", Toast.LENGTH_SHORT).show();
                }
            }
        });



        ((HomeActivityy) context).backward3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Constants.ISGURUPLAY=true;
                if ( playedposition>0) {
                    pos = playedposition - 1;
                    final   Bhajan forwardbhajan=list.get(pos);

                    ((HomeActivityy) context).playerlayout3.setVisibility(View.VISIBLE);
                    Ion.with(context).load(forwardbhajan.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            //holder.artistImage.setImageBitmap(result);
                            ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                        }
                    });
                    ((HomeActivityy) context).titleaudio3.setText(forwardbhajan.getTitle());
                    ((HomeActivityy) context).descriptionaudio3.setVisibility(View.GONE);
                    ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);

                    if (AudioPlayerService.mediaPlayer != null) {
                        if (AudioPlayerService.mediaPlayer.isPlaying())
                            AudioPlayerService.mediaPlayer.pause();
                    }
                    else {

                        if(mp!=null){
                            if(mp.isPlaying()){
                                mp.reset();}
                            else{}}
                        mp = new MediaPlayer();

                            try {
                                mp.setDataSource(forwardbhajan.getMedia_file());
                                mp.prepare();
                                mp.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ((HomeActivityy) context).img_cancel3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mp.stop();
                                    // dialog.dismiss();
                                    ((HomeActivityy) context).playerlayout3.setVisibility(View.GONE);
                                    count=0;


                                }
                            });

                            ((HomeActivityy) context).playpause3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (!mp.isPlaying()) {
                                        mp.start();
                                        ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                                    }else {
                                        mp.pause();
                                        ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                                    }




                                }
                            });
                            playedposition=pos;

                        }}

                else{
                    Toast.makeText(context, "You are playing the first song", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener(){
        public void onClick(View view) {

            if (HomeActivityy.playerlayout2.getVisibility() == View.VISIBLE)
                HomeActivityy.playerlayout2.setVisibility(View.GONE);


            if (HomeActivityy.playerlayout1.getVisibility() == View.VISIBLE){
                HomeActivityy.playerlayout1.setVisibility(View.GONE);}


            if (downloadmediaplayer != null) {
                if (downloadmediaplayer.isPlaying()){
                    downloadmediaplayer.pause();}}
            else{

            }


            if(playlistmediaplayer!=null){
                if (playlistmediaplayer.isPlaying()){
                    playlistmediaplayer.pause();}}
            else{

            }

            Constants.ISGURUPLAY=true;
            /*if(count< 1) {
                count=count+1;*/

                playedposition=position;
                ((HomeActivityy) context).playerlayout3.setVisibility(View.VISIBLE);
                Ion.with(context).load(audioBean.getImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        //holder.artistImage.setImageBitmap(result);
                        ((HomeActivityy) context).thumbaudio3.setImageBitmap(result);
                    }
                });
                ((HomeActivityy) context).titleaudio3.setText(audioBean.getTitle());
                ((HomeActivityy) context).descriptionaudio3.setVisibility(View.GONE);
                ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);
                if (AudioPlayerService.mediaPlayer != null) {
                    if (AudioPlayerService.mediaPlayer.isPlaying())
                        AudioPlayerService.mediaPlayer.pause();
                }
                else {
                    if(mp!=null){
                        if(mp.isPlaying()){
                            mp.reset();}
                        else{}}


                    mp = new MediaPlayer();

                    try {
                        mp.setDataSource(audioBean.getMedia_file());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    ((HomeActivityy) context).img_cancel3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mp.stop();
                            // dialog.dismiss();
                            ((HomeActivityy) context).playerlayout3.setVisibility(View.GONE);
                            count=0;


                        }
                    });

                    ((HomeActivityy) context).playpause3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!mp.isPlaying()) {
                               mp.start();
                                ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_pause);

                                /*Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                Constants.IS_PLAYING = "true";
                                intent.putExtra("isplaying", "true");
                                getActivity().sendBroadcast(intent);*/
                            }else {
                                mp.pause();
                                ((HomeActivityy) context).playpause3.setImageResource(R.mipmap.audio_play);

                            /*    Constants.IS_PLAYING = "false";
                                Intent intent = new Intent(BhajansCategoryFragment.BROADCAST_ISPLAYING_SONG);
                                intent.putExtra("isplaying", "false");
                                getActivity().sendBroadcast(intent);*/
                            }





                        }
                    });

                }
           /* }
            else{

            }*/
        }});
    }


 /*   public void forwardRewindPlaySongs() {
        killMediaPlayer();
        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
            bhajan = bhajans[position];

        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
            bhajan = ((HomeActivityy) context).bhajanList.get(position);
        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
            bhajan = bhajanList.get(position);
        }
        try {
            setBhajanData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Constants.CLICK_ACTIVE_FORWARD.equals("true")) {
            Constants.CLICK_ACTIVE_FORWARD = "false";
//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) + 1);
            position += 1;
            scrollPlay(position);
        } else if (Constants.CLICK_ACTIVE_REWIND.equals("true")) {
            Constants.CLICK_ACTIVE_REWIND = "false";
            position -= 1;
//            scrollPlay(PreferencesHelper.getInstance().getIntValue("index", 0) - 1);
            scrollPlay(position);
        }
    }

    private void forwardBhajanSong() {
        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
            if ((bhajans.length - 1) > position) {
                position = position + 1;
                forwardRewindPlaySongs();

                Intent intent = new Intent(BROADCAST_PLAY_NEXT_SONG);
                // intent.putExtra("bhajanlist",bhajans);
                Constants.FORWARD_SONG = "true";
                //intent.putExtra("index",PreferencesHelper.getInstance().getIntValue("index",0)+1);
                getActivity().sendBroadcast(intent);

            } else {
                position = bhajans.length - 1;
                isImgBTnClick = false;
            }
        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
            if (((((HomeActivityy) context).bhajanList).size() - 1) > position) {
                position = position + 1;
                forwardRewindPlaySongs();
            } else {
                isImgBTnClick = false;
            }
        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
            if ((bhajanList.size() - 1) > position) {
                position = position + 1;
                forwardRewindPlaySongs();
            } else {
                isImgBTnClick = false;
            }
        }
    }
*/
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GURU_RELATED_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("type", "2")
                    .setMultipartParameter("media_id", video_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        Log.d("RECENT_VIEW Response",jsonstring.toString());
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView channelName, description, date, viewsNumber;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image_single_item);
            channelName = itemView.findViewById(R.id.channel_name_single_item);
            description = itemView.findViewById(R.id.description_single_item);
            date = itemView.findViewById(R.id.date_single_item);
            viewsNumber = itemView.findViewById(R.id.views_number_single_item);
            layout = itemView.findViewById(R.id.single_item_layout_video);
        }
    }

}
