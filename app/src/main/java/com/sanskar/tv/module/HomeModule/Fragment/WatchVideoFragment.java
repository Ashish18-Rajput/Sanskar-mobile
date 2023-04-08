//package com.sanskar.sanskartv.module.HomeModule.Fragment;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.android.exoplayer2.DefaultLoadControl;
//import com.google.android.exoplayer2.DefaultRenderersFactory;
//import com.google.android.exoplayer2.ExoPlayerFactory;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.google.android.exoplayer2.source.ExtractorMediaSource;
//import com.google.android.exoplayer2.source.MediaSource;
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//import com.google.android.exoplayer2.ui.PlaybackControlView;
//import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
//import com.google.android.exoplayer2.util.Util;
//import com.google.gson.Gson;
//import com.koushikdutta.async.future.FutureCallback;
//import com.koushikdutta.ion.Ion;
//import com.koushikdutta.ion.builder.Builders;
//import com.sanskar.sanskartv.module.HomeModule.Activity.HomeActivityy;
//import com.sanskar.sanskartv.module.HomeModule.Adapter.ViewAllAdapter;
//import com.sanskar.sanskartv.module.HomeModule.Pojo.Videos;
//import com.sanskar.sanskartv.Others.Helper.API;
//import com.sanskar.sanskartv.Others.Helper.Constants;
//import com.sanskar.sanskartv.Others.Helper.Progress;
//import com.sanskar.sanskartv.Others.Helper.ToastUtil;
//import com.sanskar.sanskartv.Others.Helper.Utils;
//import com.sanskar.sanskartv.Others.NetworkNew.NetworkCall;
//import com.sanskar.sanskartv.Others.network.Networkconstants;
//import com.sanskar.sanskartv.R;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static com.sanskar.sanskartv.Others.Helper.Utils.clearEditText;
//import static com.sanskar.sanskartv.Others.Helper.Utils.getDate;
//import static com.sanskar.sanskartv.Others.Helper.Utils.isConnectingToInternet;
//
///**
// * Created by appsquadz on 2/9/18.
// */
//
//public class WatchVideoFragment extends Fragment
//        implements NetworkCall.MyNetworkCallBack, View.OnClickListener {
//
//    RelativeLayout toolbar, noDataFound;
//    private SimpleExoPlayerView simpleExoPlayerView;
//    TextView name, description, date, title;
//    ImageView back;
//    Videos video;
//    RecyclerView relatedVideosList;
//    ViewAllAdapter adapter;
//    RecyclerView.LayoutManager layoutManager;
//    RelativeLayout share;
//    TextView viewNumber, likeNumber;
//    FrameLayout videoViewFullLayout;
//    PlaybackControlView controlView;
//    SwipeRefreshLayout swipeRefreshLayout;
//    EditText comment;
//    TextView postComment;
//    private Context context;
//    private Progress progress;
//    private ArrayList<Videos> videoResponses;
//    private NetworkCall networkCall;
//    private LinearLayout nonFullScreenLayout;
//    private FrameLayout mFullScreenButton;
//    private ImageView mFullScreenIcon, like;
//    private int position;
//    private SimpleExoPlayer player;
//    private long playbackPosition = 0;
//    private int currentWindow = 0;
//    private boolean playWhenReady = true;
//    private HomeActivityy activity;
//    ProgressBar commentProgress;
//    private TextView commentNumbers;
//    private Dialog mFullScreenDialog;
//    private boolean mExoPlayerFullscreen;
//    private View view;
//    private ImageButton exoPrev;
//    private Videos[] videos;
//    private Videos[] videosArray;
//    private int pos;
//    private CircleImageView profileImg;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        this.context = context;
//        activity = ((HomeActivityy) context);
//        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Util.SDK_INT > 23) {
//            initializePlayer();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer();
//        }
//
//        ((HomeActivityy) context).handleToolBar("");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT <= 23) {
//            releasePlayer();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (Util.SDK_INT > 23) {
//            releasePlayer();
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_watch_video, null);
//        getBundleData();
//        initView(view);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        networkCall = new NetworkCall(WatchVideoFragment.this, context);
//        progress = new Progress(context);
//        ((HomeActivityy) context).getSupportActionBar().hide();
//        title.setVisibility(View.VISIBLE);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getRelatedVideos();
//            }
//        });
//
//        setVideoData();
//        getRelatedVideos();
//
//        back.setOnClickListener(this);
//    }
//
//    @Override
//    public Builders.Any.B getAPIB(String API_NAME) {
//        Builders.Any.B ion = null;
//        if (API_NAME.equals(API.RELATED_VIDEOS)) {
//            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
//                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
//                    .setMultipartParameter("video_id", video.getId());
//        } else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
//            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
//                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
//                    .setMultipartParameter("video_id", video.getId());
//        } else if (API_NAME.equals(API.POST_COMMENT)) {
//            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
//                    .setMultipartParameter("user_id", activity.signupResponse.getId())
//                    .setMultipartParameter("video_id",video.getId())
//                    .setMultipartParameter("comment",comment.getText().toString());
//        }
//        return ion;
//    }
//
//    @Override
//    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
//        swipeRefreshLayout.setRefreshing(false);
//        if (result.optBoolean("status")) {
//            if (apitype.equals(API.RELATED_VIDEOS)) {
//                nonFullScreenLayout.setVisibility(View.VISIBLE);
//                JSONArray jsonArray = result.optJSONArray("data");
//                Log.e("watch_video", jsonArray.toString());
//                if (videoResponses.size() > 0)
//                    videoResponses.clear();
//                if (jsonArray.length() > 0) {
//                    relatedVideosList.setVisibility(View.VISIBLE);
//                    noDataFound.setVisibility(View.GONE);
//                    videos = new Videos[jsonArray.length()];
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
//                        videos[i] = video;
//                    }
//                    if (context != null) {
//                        adapter = new ViewAllAdapter(videos, context);
//                        relatedVideosList.setAdapter(adapter);
//                    }
//                } else {
//                    relatedVideosList.setVisibility(View.GONE);
//                    noDataFound.setVisibility(View.VISIBLE);
//                }
//            } else if (apitype.equals(API.LIKE_VIDEO)) {
//                LikeDislike(true);
//            } else if (apitype.equals(API.UNLIKE_VIDEO)) {
//                LikeDislike(false);
//            } else if(apitype.equals(API.POST_COMMENT)){
//                clearEditText(comment);
//                postComment.setVisibility(View.VISIBLE);
//                commentProgress.setVisibility(View.GONE);
//                video.setComments(String.valueOf(Integer.parseInt(video.getComments())+1));
//                commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
//                ToastUtil.showShortToast(context,result.optString("message"));
//
//            }
//        } else {
//            if (apitype.equals(API.HOME_PAGE_VIDEOS)) {
//                nonFullScreenLayout.setVisibility(View.GONE);
//            }
//            else if(apitype.equals(API.POST_COMMENT)){
//                clearEditText(comment);
//                postComment.setVisibility(View.VISIBLE);
//                commentProgress.setVisibility(View.GONE);
//
//                ToastUtil.showShortToast(context,result.optString("message"));
//            }
//        }
//
//    }
//
//    @Override
//    public void ErrorCallBack(String jsonstring, String apitype) {
//        swipeRefreshLayout.setRefreshing(false);
//        ToastUtil.showDialogBox(context, jsonstring);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.share_watch_video:
//                shareTextUrl();
//                break;
//            case R.id.like_video_watch_video:
//                if (isConnectingToInternet(context)) {
//                    if (!Boolean.parseBoolean(like.getTag().toString())) {
//                        networkCall.NetworkAPICall(API.LIKE_VIDEO, false);
//                    } else {
//                        networkCall.NetworkAPICall(API.UNLIKE_VIDEO, false);
//                    }
//                } else {
//                    swipeRefreshLayout.setRefreshing(false);
//                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
//                break;
////            case R.id.exo_fullscreen_button:
////                mExoPlayerFullscreen = true;
////                break;
//            case R.id.back:
//                ((HomeActivityy) context).onBackPressed();
//                break;
//
//            case R.id.watch_video_comment_post_tv:
//                postComment.setVisibility(View.GONE);
//                commentProgress.setVisibility(View.VISIBLE);
//                Utils.closeKeyboard(activity);
//                networkCall.NetworkAPICall(API.POST_COMMENT, false);
//                break;
//
//            case R.id.fragment_watch_video_tv_view_all_comments:
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.VIDEO_ID,video.getId());
//                bundle.putSerializable("video", video);
//                Fragment fragment = new ViewCommentsFragment();
//                fragment.setArguments(bundle);
//                getFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack("View All")
//                        .replace(R.id.container_layout_home, fragment)
//                        .commit();
//                break;
//
//        }
//    }
//
//    private void hideSystemUi() {
//        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                Log.i("Config: ", newConfig.toString());
//                openFullscreenDialog();
//
//            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                closeFullscreenDialog();
//                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//            }
//    }
//
//    private void initializePlayer() {
//        player = ExoPlayerFactory.newSimpleInstance(
//                new DefaultRenderersFactory(context),
//                new DefaultTrackSelector(), new DefaultLoadControl());
//
//        simpleExoPlayerView.setPlayer(player);
//
//        player.setPlayWhenReady(playWhenReady);
//        player.seekTo(currentWindow, playbackPosition);
//
//        Uri uri = Uri.parse(video.getVideo_url());
//        MediaSource mediaSource = buildMediaSource(uri);
//        player.prepare(mediaSource, true, false);
//    }
//
//    private MediaSource buildMediaSource(Uri uri) {
//        return new ExtractorMediaSource.Factory(
//                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
//                createMediaSource(uri);
//    }
//
//    private void getBundleData() {
//        if (getArguments() != null) {
//
//            Bundle bundle = getArguments();
//            if (bundle.containsKey("video_data")) {
//                videosArray = (Videos[]) bundle.getSerializable("video_data");
//                pos = bundle.getInt("position");
//                video = videosArray[pos];
//            }
//        }
//    }
//
//    private void initView(View view) {
//
//        toolbar = view.findViewById(R.id.toolbar_watch_video);
//        simpleExoPlayerView = view.findViewById(R.id.video_view_watch_video);
//        name = view.findViewById(R.id.name_watch_video);
//        description = view.findViewById(R.id.description_watch_video);
//        date = view.findViewById(R.id.date_watch_video);
//        back = toolbar.findViewById(R.id.back);
//        share = view.findViewById(R.id.share_watch_video);
//        viewNumber = view.findViewById(R.id.views_number_watch_video);
//        like = view.findViewById(R.id.like_video_watch_video);
//        like.setTag(false);
//        likeNumber = view.findViewById(R.id.like_number_watch_video);
//        nonFullScreenLayout = view.findViewById(R.id.watch_video_non_full_screen);
//        videoViewFullLayout = view.findViewById(R.id.video_view_layout_watch_video);
//        controlView = simpleExoPlayerView.findViewById(R.id.exo_controller);
//        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_watch_video);
//        title = toolbar.findViewById(R.id.toolbar_title);
//        noDataFound = view.findViewById(R.id.no_data_found_watvh_video);
//        comment = view.findViewById(R.id.watch_video_comment_et);
//        postComment = view.findViewById(R.id.watch_video_comment_post_tv);
//        commentProgress = view.findViewById(R.id.watch_video_comment_post_pb);
//        commentNumbers = view.findViewById(R.id.fragment_watch_video_tv_view_all_comments);
//        profileImg = view.findViewById(R.id.fragment_watch_comment_image);
//
//        share.setOnClickListener(this);
//        back.setOnClickListener(this);
//        like.setOnClickListener(this);
//        postComment.setOnClickListener(this);
//        commentNumbers.setOnClickListener(this);
//
//        this.view = view;
//        initFullscreenDialog();
//        initFullscreenButton(simpleExoPlayerView);
//
//        relatedVideosList = view.findViewById(R.id.related_video_recycler_view_watch_video);
//        layoutManager = new LinearLayoutManager(context);
//        relatedVideosList.setLayoutManager(layoutManager);
//
//        videoResponses = new ArrayList<>();
//
//        if (((HomeActivityy)context).signupResponse.getProfile_picture().toString().isEmpty()
//                || ((HomeActivityy)context).signupResponse.getProfile_picture().toString() == null) {
//            profileImg.setImageResource(R.mipmap.profile);
//        } else {
//            Ion.with(context).load(((HomeActivityy)context).signupResponse.getProfile_picture().toString())
//                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
//                @Override
//                public void onCompleted(Exception e, Bitmap result) {
//                    profileImg.setImageBitmap(result);
//                }
//            });
//        }
//    }
//
//    private void setVideoData() {
//        if (video != null){
//            title.setText(video.getVideo_title());
//            setRelatedVideoData();
//        }
//    }
//
//    private void releasePlayer() {
//        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
//            playWhenReady = player.getPlayWhenReady();
//            player.release();
//            player = null;
//        }
//    }
//
//    private void LikeDislike(boolean b) {
//        like.setTag(b);
//        int likenum = Integer.parseInt(likeNumber.getText().toString().split(" ")[0]);
//        if (b) {
//            like.setImageResource(R.mipmap.like_active);
//            likeNumber.setText(likenum + 1 + " likes");
//            video.setLikes(String.valueOf(likenum + 1));
//            video.setIs_like("1");
//        } else {
//            like.setImageResource(R.mipmap.like_gray);
//            likeNumber.setText(likenum - 1 + " likes");
//            video.setLikes(String.valueOf(likenum - 1));
//            video.setIs_like("0");
//        }
//    }
//
//    private void shareTextUrl() {
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//
//        // Add data to the intent, the receiving app will decide
//        // what to do with it.
//        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
//        share.putExtra(Intent.EXTRA_TEXT, video.getThumbnail_url());
//
//        startActivity(Intent.createChooser(share, "Share link!"));
//    }
//
//    private void initFullscreenButton(final View view) {
//
//        PlaybackControlView controlView = view.findViewById(R.id.exo_controller);
//
//        mFullScreenIcon = view.findViewById(R.id.exo_fullscreen_icon);
//        mFullScreenButton = view.findViewById(R.id.exo_fullscreen_button);
//
//        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mExoPlayerFullscreen)
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                else {
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//
//            }
//
//        });
//
//        exoPrev = view.findViewById(R.id.exo_prev);
////        exoNext = controlView.findViewById(R.id.exo_next);
//
//        exoPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (videos.length > position) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("video_data", videos);
//                    bundle.putInt("position", position);
//
//                    Fragment fragment = new WatchVideoFragment();
//                    fragment.setArguments(bundle);
//                    ((HomeActivityy) context)
//                            .getSupportFragmentManager()
//                            .beginTransaction()
//                            .addToBackStack("Watch_Video")
//                            .replace(R.id.container_layout_home, fragment)
//                            .commit();
//                }
//            }
//        });
//    }
//
//    private void initFullscreenDialog() {
//
//        mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
//            public void onBackPressed() {
//                if (mExoPlayerFullscreen) {
////                    closeFullscreenDialog();
//                    //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
////                    isBackDialogPressed++;
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//                    //res();
//                }
//               //ivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////                 if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                    //hideSystemUi();
//                    //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
////                }
//                super.onBackPressed();
//            }
//        };
//    }
//
//    private void openFullscreenDialog() {
//        hideSystemUi();
//        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
//        mFullScreenDialog.addContentView(simpleExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mFullScreenIcon.setImageResource(R.mipmap.fullscreen_exit_white);
//        mExoPlayerFullscreen = true;
//        mFullScreenDialog.show();
//    }
//
//    private void closeFullscreenDialog() {
//        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
////        activity.getWindow().getDecorView()
////                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        ((ViewGroup) simpleExoPlayerView.getParent()).removeView(simpleExoPlayerView);
//        ((FrameLayout) view.findViewById(R.id.video_view_layout_watch_video)).addView(simpleExoPlayerView);
//        mExoPlayerFullscreen = false;
//        mFullScreenDialog.dismiss();
//        mFullScreenIcon.setImageResource(R.mipmap.fullscreen_white);
//    }
//
//    private void setRelatedVideoData() {
//        name.setText(video.getAuthor_name());
//        date.setText(getDate(Long.parseLong(video.getCreation_time())));
//        description.setText(video.getVideo_desc());
//        commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
//        if (video.getLikes().equals(1) || video.getLikes().equals(0)) {
//            likeNumber.setText(video.getLikes() + " like");
//        } else {
//            likeNumber.setText(video.getLikes() + " likes");
//        }
//        viewNumber.setText(Integer.parseInt(video.getViews()) + " views");
//        if (Integer.parseInt(video.getIs_like()) == 1) {
//            like.setImageResource(R.mipmap.like_active);
//            like.setTag(true);
//        }
//    }
//
//    private void getRelatedVideos() {
//        if (isConnectingToInternet(context)) {
//            networkCall = new com.sanskar.sanskartv.Others.NetworkNew.NetworkCall(WatchVideoFragment.this, context);
//            networkCall.NetworkAPICall(API.RELATED_VIDEOS, false);
//        } else {
//            swipeRefreshLayout.setRefreshing(false);
//            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//        }
//    }
//
//}
