package com.sanskar.tv.jwPlayer;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import java.io.File;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Fragment.ViewCommentsFragment;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.sanskar.tv.Others.Helper.Utils.clearEditText;
import static com.sanskar.tv.Others.Helper.Utils.getDate;
import static com.sanskar.tv.module.goLiveModule.controller.GoLiveActivity.isConnectingToInternet;

public class JwPlayerFragment extends Fragment implements VideoPlayerEvents.OnFullscreenListener, NetworkCall.MyNetworkCallBack, View.OnClickListener {
    JWPlayerView playerView;
    KeepScreenOnHandler keepScreenOnHandler;
    String url;
    private RelativeLayout noDataFound;
    TextView name, description, date,downloadwatch_video;
    ImageView back;
    Videos video;
    RecyclerView relatedVideosList;
    ViewAllAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RelativeLayout share;
    TextView viewNumber, likeNumber;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText comment;
    TextView postComment;
    private ArrayList<Videos> videoResponses;
    private NetworkCall networkCall;
    private LinearLayout nonFullScreenLayout,backImage;
    private ImageView like;
    ProgressBar commentProgress;
    private TextView commentNumbers;
    private Videos[] videos;
    private Videos[] vd=new Videos[]{};
    File file;
    private Videos[] videosArray;
    private int pos;
    private Context context;
    private CircleImageView profileImg;
    public SignupResponse signupResponse;
    private RelativeLayout container;
    private boolean isLike = false;
    private boolean isComment = false;
    private RelativeLayout layout, toolbar;
    private long enqueue;
    private DownloadManager dm;
    private int mPage = 1;
    private DownloadManager downloadManager;
    private long refid;
    private Uri Download_Uri;
    ArrayList<Long> list = new ArrayList<>();
    private ImageView download;
    BottomSheetBehavior behavior;
    private long downloadID;
    ImageView profile_iv;
    ImageView tv_iv;
    private TextView endTimeTV, endTimeTV1;
    private SeekBar seekBar, seekBar1;
    private TextView startTimeTV, startTimeTV1;
    private Handler handler;
    YoutubeLayout youtubeLayout;


    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                Toast.makeText(getActivity(), "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jwplayer,null);
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);

        initView(view);
        getBundleData();


        Download_Uri = Uri.parse(API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/");
        //Utils.setStatusBarGradiant(this, container);

        String baseUrl = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/";
        if (video.getVideo_url().contains("//")) {
            url = API.BASE_URL_VIDEO + "/_definst_/amazons3/mp4:bhaktiappproduction/videos/SampleVideo_1280x720_1mb.mp4/playlist.m3u8";
        } else {
            url = baseUrl + video.getVideo_url() + "/playlist.m3u8";
        }
        playerView = view.findViewById(R.id.playerView);
        Log.d("request11","url"+url);

        PlaylistItem item = new PlaylistItem(url);//"https://s3.ap-south-1.amazonaws.com/dams-apps-production/12BD5F37-6AFF-4C45-9F98-C6925CEC8DB3-1458-000003B4C7C6745B.mp4");//("http://techslides.com/demos/sample-videos/small.mp4");
        keepScreenOnHandler = new KeepScreenOnHandler(playerView, getActivity().getWindow());
        playerView.addOnFullscreenListener(this);
        playerView.load(item);
        playerView.play();

        networkCall = new NetworkCall(this, getActivity());

        setVideoData();
        getRelatedVideos();

       /* ((HomeActivityy) getActivity()).searchParent.setVisibility(View.GONE);
        ((HomeActivityy) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        ((HomeActivityy) getActivity()).toolbar.setVisibility(View.VISIBLE);*/

//        back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getRelatedVideos() {
        if (isConnectingToInternet(getActivity())) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, getActivity());
            networkCall.NetworkAPICall(API.RELATED_VIDEOS, false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(getActivity(), Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void setVideoData() {
        if (video != null) {
            setRelatedVideoData();
        }
    }

    private void setRelatedVideoData() {
        name.setText(video.getAuthor_name());
        date.setText(getDate(Long.parseLong(video.getCreation_time())));

        String desc = Html.fromHtml(video.getVideo_desc()).toString();

        description.setText(desc);

        /*if (video.getComments().equalsIgnoreCase("0")) {
            commentNumbers.setText("No comment");
            commentNumbers.setEnabled(false);
        } else if (video.getComments().equalsIgnoreCase("1")) {
            commentNumbers.setText("View comment");
            commentNumbers.setEnabled(true);
        } else {
            commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
            commentNumbers.setEnabled(true);
        }*/

        if (video.getLikes().equals("0")) {
            likeNumber.setText("no like");
        } else if (video.getLikes().equals("1")) {
            likeNumber.setText(video.getLikes() + " like");
        } else {
            likeNumber.setText(video.getLikes() + " likes");
        }

        if (video.getViews().equals("0")) {
            viewNumber.setText("no view");
        } else if (video.getViews().equals("1")) {
            viewNumber.setText(video.getViews() + " view");
        } else {
            viewNumber.setText(video.getViews() + " views");
        }

        if (video.getIs_like().equals("1")) {
            like.setImageResource(R.drawable.liked);
            like.setTag(true);
        }
    }


    private void initView(View view) {
        downloadwatch_video=view.findViewById(R.id.downloadwatch_video) ;
        backImage = view.findViewById(R.id.dropdown);
        toolbar = view.findViewById(R.id.toolbar);
        name = view.findViewById(R.id.name_watch_video);
        description = view.findViewById(R.id.description_watch_video);
        date = view.findViewById(R.id.date_watch_video);
        back = view.findViewById(R.id.back);
        share = view.findViewById(R.id.share_watch_video);
        viewNumber = view.findViewById(R.id.views_number_watch_video);
        like = view.findViewById(R.id.like_video_watch_video);
        like.setTag(false);
        likeNumber = view.findViewById(R.id.like_number_watch_video);
        nonFullScreenLayout = view.findViewById(R.id.watch_video_non_full_screen);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_watch_video1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRelatedVideos();
            }
        });
        //title = findViewById(R.id.toolbar_title);
        noDataFound = view.findViewById(R.id.no_data_found_watvh_video);

        container = view.findViewById(R.id.container);
        layout = view.findViewById(R.id.layout);
        download = view.findViewById(R.id.download);
        profile_iv = view.findViewById(R.id.profile_iv);
        tv_iv = view.findViewById(R.id.tv_iv);
        downloadwatch_video.setOnClickListener(this);
        share.setOnClickListener(this);
        backImage.setOnClickListener(this);
//        back.setOnClickListener(this);
        like.setOnClickListener(this);
        download.setOnClickListener(this);
//        tv_iv.setOnClickListener(this);
        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        relatedVideosList = view.findViewById(R.id.related_video_recycler_view_watch_video);
        relatedVideosList.setNestedScrollingEnabled(false);

        layoutManager = new LinearLayoutManager(getActivity());
        relatedVideosList.setLayoutManager(layoutManager);

        /*if (signupResponse.getProfile_picture().toString().isEmpty()
                || signupResponse.getProfile_picture().toString() == null) {
            //profileImg.setImageResource(R.mipmap.profile);
        } else {
            Ion.with(this).load(signupResponse.getProfile_picture().toString())
                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
              //      profileImg.setImageBitmap(result);
                }
            });
        }*/

        videoResponses = new ArrayList<>();


    }
    private void getBundleData(){
        if (getArguments() != null) {
            Bundle bundle = getArguments();

            if (bundle.containsKey("video_data")) {
                videosArray = (Videos[]) bundle.getSerializable("video_data");
                pos = bundle.getInt("position");
                video = videosArray[pos];
            } else if (bundle.containsKey("video_data_guru")) {
                video = (Videos) bundle.getSerializable("video_data_guru");
            }

        }
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, video.getAuthor_name());
        share.putExtra(Intent.EXTRA_SUBJECT, video.getVideo_title());
        share.putExtra(Intent.EXTRA_TEXT, video.getThumbnail_url());
        Log.d("request11","url"+url);
        startActivity(Intent.createChooser(share, "Share link!"));
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.share_watch_video:
                shareTextUrl();
                 // shareAppUrl();
                break;

            case R.id.like_video_watch_video:
                if (isConnectingToInternet(getActivity())) {
                    if (!Boolean.parseBoolean(like.getTag().toString())) {
                        networkCall.NetworkAPICall(API.LIKE_VIDEO, false);
                    } else {
                        networkCall.NetworkAPICall(API.UNLIKE_VIDEO, false);
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(getActivity(), Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;

            case R.id.back:
                break;

            case R.id.watch_video_comment_post_tv:

                postComment.setVisibility(View.GONE);
                commentProgress.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(getActivity());
                networkCall.NetworkAPICall(API.POST_COMMENT, false);
                break;

            case R.id.fragment_watch_video_tv_view_all_comments:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.VIDEO_ID, video.getId());
                bundle.putSerializable("video", video);
                Fragment fragment = new ViewCommentsFragment();
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("VIEW_COMMENT")
                        .commit();
                break;
            case R.id.download:
                beginDownload();
                break;



           /* case R.id.tv_iv:

                Intent intent=new Intent(LiveStreamJWActivity.this, (LiveTvFragment.class));
                startActivity(intent);
                break;*/
        }
    }

    @Override
    public void onFullscreen(boolean b) {
        playerView.setFullscreen(true, true);

    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.RELATED_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("video_id", video.getId())
                     .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        } else if (API_NAME.equals(API.LIKE_VIDEO) || API_NAME.equals(API.UNLIKE_VIDEO)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("video_id", video.getId());
        } else if (API_NAME.equals(API.POST_COMMENT)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("video_id", video.getId())
                    .setMultipartParameter("comment", comment.getText().toString());
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if (result.optBoolean("status")) {
            if (apitype.equals(API.RELATED_VIDEOS)) {
             /*   nonFullScreenLayout.setVisibility(View.VISIBLE);
                JSONArray jsonArray = result.optJSONArray("data");
                Log.e("watch_video", jsonArray.toString());
                if (videoResponses.size() > 0)
                    videoResponses.clear();
                if (jsonArray.length() > 0) {
                    relatedVideosList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                    videos = new Videos[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                        videos[i] = video;
                    }
                    if (this != null) {
                        adapter = new ViewAllAdapter(videos, getActivity());
                        relatedVideosList.setAdapter(adapter);
                    }
                } else {
                    relatedVideosList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }
*/

                nonFullScreenLayout.setVisibility(View.VISIBLE);
                JSONArray jsonArray = result.optJSONArray("data");
                Log.e("watch_video", jsonArray.toString());
                if (videoResponses.size() > 0)
                    videoResponses.clear();
                if (jsonArray.length() > 0) {
                    relatedVideosList.setVisibility(View.VISIBLE);
                    noDataFound.setVisibility(View.GONE);
                    videos = new Videos[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Videos video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                        videos[i] = video;
                    }



                    Videos[] temp= new Videos[vd.length + jsonArray.length()];

                    for (int i = 0; i < vd.length; i++){
                        temp[i] = vd[i];
                    }

                    int j=0;
                    for (int i = vd.length; i < temp.length; i++) {


                        temp[i]=videos[j];

                        j++;

                    }
                    vd = temp;

                    adapter = new ViewAllAdapter(vd, getActivity());
                    relatedVideosList.setAdapter(adapter);
                }
                else {
                    relatedVideosList.setVisibility(View.GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }
            } else if (apitype.equals(API.LIKE_VIDEO)) {
                LikeDislike(true);
            } else if (apitype.equals(API.UNLIKE_VIDEO)) {
                LikeDislike(false);
            } else if (apitype.equals(API.POST_COMMENT)) {


                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);
                video.setComments(String.valueOf(Integer.parseInt(video.getComments()) + 1));
                if (video.getComments().equalsIgnoreCase("1")) {
                    commentNumbers.setText((video.getComments() + "View comment").toString());
                    commentNumbers.setEnabled(true);
                } else {
                    commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
                    commentNumbers.setEnabled(true);
                }
                isComment = true;
                ToastUtil.showShortToast(getActivity(), result.optString("message"));
            }
        } else {
            if (apitype.equals(API.HOME_PAGE_VIDEOS)) {
                nonFullScreenLayout.setVisibility(View.GONE);
            } else if (apitype.equals(API.POST_COMMENT)) {
                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);

                ToastUtil.showShortToast(getActivity(), result.optString("message"));
            }
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showDialogBox(getActivity(), jsonstring);

    }


    public void setTotalComments() {
        video.setComments(String.valueOf(Integer.parseInt(video.getComments()) + 1));
        if (video.getComments().equalsIgnoreCase("1")) {
            commentNumbers.setText("View comment");
            commentNumbers.setEnabled(true);
        } else {
            commentNumbers.setText(getString(R.string.view_all_comments, video.getComments()));
            commentNumbers.setEnabled(true);
        }
        isComment = true;
    }

    private void LikeDislike(boolean b) {
        like.setTag(b);
        int likenum = 0;
        if (likeNumber.getText().toString().equalsIgnoreCase("0 like")) {
            likenum = 0;
        } else {
            likenum = Integer.parseInt(likeNumber.getText().toString().split(" ")[0]);
        }
        if (b) {
            if (likenum == 0) {
                likeNumber.setText(likenum + 1 + " like");
            } else {
                likeNumber.setText(likenum + 1 + " likes");
            }
            like.setImageResource(R.drawable.liked);
//            video.setLikes(String.valueOf(likenum + 1));
//            video.setIs_like("1");
        } else {
            if (likenum == 1) {
                likeNumber.setText("0 like");
            } else if (likenum == 2) {
                likeNumber.setText(likenum - 1 + " like");
            } else {
                likeNumber.setText(likenum - 1 + " likes");
            }
            like.setImageResource(R.drawable.like_default);
//            video.setLikes(String.valueOf(likenum - 1));
//            video.setIs_like("0");
        }
        isLike = true;
    }


    @Override
    public void onResume() {
        super.onResume();
        playerView.onResume();
    }

    @Override
    public void onPause() {
        playerView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        playerView.onDestroy();
        super.onDestroyView();
        getActivity().unregisterReceiver(onDownloadComplete);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

    private void beginDownload() {
        File file = new File(getActivity().getExternalFilesDir(null), "Dummy");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://speedtest.ftp.otenet.gr/files/test10Mb.db"))
                .setTitle(video.getVideo_title())// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                // .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }
}
