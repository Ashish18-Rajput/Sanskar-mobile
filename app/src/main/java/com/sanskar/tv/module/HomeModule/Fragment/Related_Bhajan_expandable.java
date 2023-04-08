package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanViewAllAdapter;
import com.sanskar.tv.module.HomeModule.BhajanInterface;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 3/5/2018.
 */

public class Related_Bhajan_expandable extends Fragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, NetworkCall.MyNetworkCallBack {
    private DiscreteScrollView itemPicker;
    ViewPager viewPager;
    RelativeLayout toolbar;
    TextView title;
    ImageView like, more, close;
    Context context;
    BhajanAdapter bhajanAdapter;
    BhajanPlayerView bhajanPlayAdapter;
    public Bhajan bhajan;
    public MediaPlayer mediaPlayer;
    TextView bhajanName, artistName;
    ImageView playPause, forward, rewind;
    private SeekBar seekBar;
    private TextView startTimeTV;
    private TextView endTimeTV;
    private AppTextView bhajanTitle;
    private AppTextView bhajanArtistName;
    private Handler handler;
    private Bhajan[] bhajans;
    private int audioType;
    private int position;
    private NetworkCall networkCall;
    private List<Bhajan> bhajanList;
    private ProgressBar playProgress;
    private String[] imgString;
    private Progress progress;
    private int tabPos;
    private boolean firstTimeSelected = true;
    private boolean isImgBTnClick = false;
    private RecyclerView relatedRV;
    private BhajanViewAllAdapter adapter;
    private Bhajan[] relatedBhajans;
    ImageView download;
    private long downloadID;
    BhajanInterface bhajanInterface;
    Button related_bhajan_expandable;
Fragment fragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.related_bhajan_expandable, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
//        getBundleData();
 //      initView(view);
//        getBhajanRelatedAudios();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

//            case R.id.play_btn_bhajan:
//                try {
//                    playAudio();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case R.id.forward_btn_bhajan:
//                isImgBTnClick = true;
//                forwardBhajanSong();
//                break;
//            case R.id.rewind_btn_bhajan:
//                if (position > 0) {
//                    position = position - 1;
//                    isImgBTnClick = true;
//                    forwardRewindPlaySongs();
//                }
//                break;
//            case R.id.like_bhajan:
//                if (isConnectingToInternet(context)) {
//                    if (!Boolean.parseBoolean(like.getTag().toString())) {
//                        networkCall.NetworkAPICall(API.BHAJAN_LIKE, false);
//                    } else {
//                        networkCall.NetworkAPICall(API.BHAJAN_UNLIKE, false);
//                    }
//                } else {
//                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
//                break;
//            case R.id.more_options_toolbar3:
//                new NotificationDialog(context, this).showPopup();
//                break;
//            case R.id.close_toolbar3:
//                killMediaPlayer();
//                toolbar.setVisibility(View.GONE);
//                FragmentManager fragmentManager = ((HomeActivityy) context).getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .remove(fragmentManager.findFragmentById(R.id.container_layout_home)).commit();
//
//                ((HomeActivityy) context).onBackPressed();
//                break;
//
//            case R.id.related_bhajan_rv:
//
//                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

//        if(mediaPlayer != null && fromUser){
//            mediaPlayer.seekTo(progress * 1000);
//        }
//        else{
//            // the event was fired from code and you shouldn't call player.seekTo()
//        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();

    }

    @Override
    public void onResume() {
        super.onResume();
       // ((HomeActivityy) context).handleToolbar();
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//            playPause.setImageResource(R.mipmap.play_active);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//            playPause.setImageResource(R.mipmap.play_active);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  killMediaPlayer();
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.BHAJAN_LIKE) || API_NAME.equals(API.BHAJAN_UNLIKE)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("bhajan_id", bhajan.getId());
        } else if (API_NAME.equals(API.RECENT_VIEW)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("type", "1")
                    .setMultipartParameter("media_id", bhajan.getId());
        } else if (API_NAME.equals(API.BHAJAN_RELATED)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("bhajan_id", bhajan.getId());
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (result.optBoolean("status")) {
            if (API_NAME.equals(API.BHAJAN_LIKE)) {

            } else if (API_NAME.equals(API.BHAJAN_UNLIKE)) {

            } else if (API_NAME.equals(API.BHAJAN_RELATED)) {
                //setBhajanRelatedData(result.optJSONArray("data"));
            }
        } else {
            //          ToastUtil.showDialogBox(context, result.optString("message"));
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        ToastUtil.showDialogBox(context, jsonstring);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("bhajans")) {
                bhajans = (Bhajan[]) bundle.getSerializable("bhajans");
                position = bundle.getInt("position");
                bhajan = bhajans[position];

                imgString = new String[bhajans.length];
                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = bhajans[i].getImage();
                }

                audioType = Constants.BHAJAN_AUDIO_TYPE;
            } else if (bundle.containsKey("guruAudioData")) {
                position = bundle.getInt("guruAudioData");
                bhajan = ((HomeActivityy) context).bhajanList.get(position);

                imgString = new String[((HomeActivityy) context).bhajanList.size()];
                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = ((HomeActivityy) context).bhajanList.get(position).getImage();
                }

                audioType = Constants.GURU_AUDIO_TYPE;
            } else if (bundle.containsKey(Constants.PLAY_LIST_DATA)) {
                bhajanList = (List<Bhajan>) bundle.getSerializable(Constants.PLAY_LIST_DATA);
                position = bundle.getInt("position");
                bhajan = bhajanList.get(position);

                imgString = new String[bhajanList.size()];
                for (int i = 0; i < imgString.length; i++) {
                    imgString[i] = bhajanList.get(position).getImage();
                }

                audioType = Constants.PLAY_LIST_AUDIO_TYPE;
            }
        }
    }

    private void initView(View view) {

        progress = new Progress(context);

        viewPager = view.findViewById(R.id.view_pager_play_bhajan);
//        toolbar = view.findViewById(R.id.toolbar_play_bhajan);
//        title = toolbar.findViewById(R.id.toolbar_title);
//        like = view.findViewById(R.id.like_bhajan);
//        more = toolbar.findViewById(R.id.more_options_toolbar3);
//        close = toolbar.findViewById(R.id.close_toolbar3);
//        bhajanName = toolbar.findViewById(R.id.bhajan_name_toolbar3);
//        artistName = toolbar.findViewById(R.id.artist_name_toolbar3);
        playPause = view.findViewById(R.id.play_btn_bhajan);
        forward = view.findViewById(R.id.forward_btn_bhajan);
        rewind = view.findViewById(R.id.rewind_btn_bhajan);
        seekBar = view.findViewById(R.id.seekbar_bhajan_player_view);
        startTimeTV = view.findViewById(R.id.start_time_tv);
        endTimeTV = view.findViewById(R.id.end_time_tv);
        playProgress = view.findViewById(R.id.play_progress_bar);

        bhajanTitle = view.findViewById(R.id.bhajan_title_tv);
        bhajanArtistName = view.findViewById(R.id.bhajan_artist_name_tv);
        download = view.findViewById(R.id.download);


//        bhajanTitle.setText(bhajan.getTitle());
//        bhajanArtistName.setText(bhajan.getArtist_name());

        relatedRV = view.findViewById(R.id.related_bhajan_rv);

//        toolbar.setVisibility(View.GONE);
//        itemPicker = (DiscreteScrollView) view.findViewById(R.id.view_pager_play_bhajan1);
//        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
//        itemPicker.addOnItemChangedListener(this);
//
//        // itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
//        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
//                .setMinScale(0.8f)
//                .build());

        //onItemChanged(bhajanList.get(0));

        networkCall = new NetworkCall(this, context);
        like.setTag(false);

        playPause.setOnClickListener(this);
        forward.setOnClickListener(this);
        rewind.setOnClickListener(this);
        like.setOnClickListener(this);
        more.setOnClickListener(this);
        close.setOnClickListener(this);
        download.setOnClickListener(this);
        related_bhajan_expandable.setOnClickListener(this);


        mediaPlayer = new MediaPlayer();

        Point screen = new Point();


        handler = new Handler();

        seekBar.setOnSeekBarChangeListener(this);
        boolean is_active=true;
        bhajanAdapter = new BhajanAdapter(context,bhajans, imgString, bhajanInterface,fragment);
        itemPicker.setAdapter(bhajanAdapter);

        bhajanPlayAdapter = new BhajanPlayerView(context, imgString);




    }

    private void playAudio() throws Exception {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPause.setImageResource(R.mipmap.audio_pause);
            ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_pause);
            if (isConnectingToInternet(context)) {
                networkCall = new NetworkCall(this, context);
                networkCall.NetworkAPICall(API.RECENT_VIEW, false);
            }
        } else {
            mediaPlayer.pause();
            playPause.setImageResource(R.mipmap.audio_play);
            ((HomeActivityy) context).playpause.setImageResource(R.mipmap.audio_play);
            Constants.IS_PLAYING = "false";
        }
        updateProgressBar();
        seekBar.setProgress(0);
        //seekBar.setMax(100);
    }

    private void setBhajanData() throws IOException {

        playPause.setVisibility(View.GONE);
        //progress.show();
        playProgress.setVisibility(View.VISIBLE);

        mediaPlayer.setDataSource(bhajan.getMedia_file());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mediaPlayer.prepareAsync();

        bhajanName.setText(bhajan.getTitle());
        artistName.setText(bhajan.getArtist_name());
        if (!TextUtils.isEmpty(bhajan.getIs_like()) && bhajan.getIs_like() != null) {
            if (bhajan.getIs_like().equals("1")) {
                like.setImageResource(R.drawable.liked);
                like.setTag(true);
            } else {
                like.setImageResource(R.drawable.like_default);
                like.setTag(false);
            }
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer player) {
                //progress.hide();
                playProgress.setVisibility(View.GONE);
                playPause.setVisibility(View.VISIBLE);

                onClick(playPause);
            }

        });

        if (isImgBTnClick) {
            viewPager.setCurrentItem(position);
        }
    }

    public void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public void updateProgressBar() {
        handler.postDelayed(mUpdateTimeTask, 1000);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            endTimeTV.setText("" + milliSecondsToTimer(totalDuration));

            startTimeTV.setText("" + milliSecondsToTimer(currentDuration));
            int progress = getProgressPercentage(currentDuration, totalDuration);
            Log.d("Progress", "" + progress);
            seekBar.setProgress(progress);

            handler.postDelayed(this, 1000);
        }
    };

    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = totalDuration / 1000;
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

//    private void forwardRewindPlaySongs() {
//        killMediaPlayer();
//        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
//            bhajan = bhajans[position];
//        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
//            bhajan = ((HomeActivityy) context).bhajanList.get(position);
//        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
//            bhajan = bhajanList.get(position);
//        }
//        try {
//            setBhajanData();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public void addPlayListData() {

        String bhajanString = PreferencesHelper
                .getInstance()
                .getBhajanString(Constants.MY_PLAY_LIST);
        List<Bhajan> bhajanList = new ArrayList<>();
        if (!TextUtils.isEmpty(bhajanString)) {

            try {
                JSONArray jsonArray = new JSONArray(bhajanString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    Bhajan bhaja = new Gson().fromJson(jsonObject.toString(), Bhajan.class);
                    bhajanList.add(bhaja);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boolean bhajanAlreadyAdded = false;

            for (int i = 0; i < bhajanList.size(); i++) {
                if (bhajanList.get(position).getId().equals(bhajan.getId())) {
                    bhajanAlreadyAdded = true;
                    break;
                }
            }
            if (!bhajanAlreadyAdded) {
                bhajanList.add(bhajan);
            }
        } else {
            bhajanList.add(bhajan);
        }

        PreferencesHelper
                .getInstance()
                .setObject(Constants.MY_PLAY_LIST, bhajanList);

    }

    private void forwardBhajanSong() {
        if (audioType == Constants.BHAJAN_AUDIO_TYPE) {
            if ((bhajans.length - 1) > position) {
                position = position + 1;
              //  forwardRewindPlaySongs();
            } else {
                isImgBTnClick = false;
            }
        } else if (audioType == Constants.GURU_AUDIO_TYPE) {
            if (((((HomeActivityy) context).bhajanList).size() - 1) > position) {
                position = position + 1;
              //  forwardRewindPlaySongs();
            } else {
                isImgBTnClick = false;
            }
        } else if (audioType == Constants.PLAY_LIST_AUDIO_TYPE) {
            if ((bhajanList.size() - 1) > position) {
                position = position + 1;
              //  forwardRewindPlaySongs();
            } else {
                isImgBTnClick = false;
            }
        }
    }




    private void getBhajanRelatedAudios() {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.BHAJAN_RELATED, false);
        } else {
            ToastUtil.showDialogBox(context, getString(R.string.no_internet));
        }
    }

    private void setBhajanRelatedData(JSONArray data) {
        if (data != null) {
            relatedBhajans = new Bhajan[data.length()];
            for (int i = 0; i < data.length(); i++) {
                relatedBhajans[i] = new Gson().fromJson(data.opt(i).toString(), Bhajan.class);
            }
            adapter = new BhajanViewAllAdapter(relatedBhajans, context, false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            relatedRV.setLayoutManager(layoutManager);
            relatedRV.setAdapter(adapter);
            ViewCompat.setNestedScrollingEnabled(relatedRV, false);
        }
    }





}
