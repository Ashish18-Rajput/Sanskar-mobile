package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomTouchListener;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.cast.ExpandedControlsActivity;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Category;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.onItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

/**
 * Created by appsquadz on 2/15/2018.swipe_refresh_videos
 */

public class VideosChildFragment extends Fragment implements NetworkCall.MyNetworkCallBack {
    public static String search_content = "";
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    public Handler handler;
    public Runnable update;
    Boolean is_post_exit = true;
    AppViewPager imageSwitcherViewPager;
    RecyclerView recyclerView;
    ViewAllAdapter adapter;
    Fragment parent;
    NestedScrollView parent_videos;
    RecyclerView.LayoutManager layoutManager;
    Videos[] videos = new Videos[]{};


    private Videos[] videoss = new Videos[]{};
    Context context;
    HomeActivityy activityy;
    Category category;
    SwipeRefreshLayout swipeRefreshLayout;
    int mPageSize = 10;
    Timer timer;
    int currentPage = 0;
    Videos videofile;
    String video_id;
    private int mPage = 1;
    private int pageSize;
    private boolean loading = false;
    private String frag = "";
    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;
    private int position;
    private NetworkCall networkCall;
    private boolean isLoading;
    private String lastVideoId = "";
    private RelativeLayout noDataFound;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;
    private List<ImageView> viewList;
    private String[] imgString;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private MediaInfo mSelectedMedia;
    private PlaybackState mPlaybackState;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private PlaybackLocation mLocation;

    RelativeLayout progress_layout;
    private int type;
    String searchContent = "";

    public void VideosChildFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, null);
        mCastContext = CastContext.getSharedInstance(getActivity());
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
        setupCastListener();
        parent = getParentFragment();
        context = getActivity();
        activityy = ((HomeActivityy) context);
        getBundleData();
        initView(view);

        return view;
    }

    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("position")) {
                position = bundle.getInt("position");
                /*try {
                    category = activityy.videoFragmentVideoesCategoryList.get(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                category = activityy.videoFragmentVideoesCategoryList.get(position);
                if (activityy.videoFragmentVideosList.containsKey(category.getId())) {
                    videos = activityy.videoFragmentVideosList.get(category.getId());
                }
            }
        }
    }

    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                if (null != mSelectedMedia) {

                    if (mPlaybackState == PlaybackState.PLAYING) {
                        loadRemoteMedia(0, true);
                        return;
                    } else {
                        mPlaybackState = PlaybackState.IDLE;
                        updatePlaybackLocation(PlaybackLocation.REMOTE);
                    }
                }
                // updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                // updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
    }

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.registerCallback(new RemoteMediaClient.Callback() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(getActivity(), ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.unregisterCallback(this);
            }
        });

    }

    private void initView(View view) {
        noDataFound = view.findViewById(R.id.no_data_found_view_all);
        imageSwitcherViewPager = view.findViewById(R.id.image_switcher_view_pager_videos);
        imageSwitcherViewPager.setPagingEnabled(true);
        recyclerView = view.findViewById(R.id.recycler_view_videos);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);

        viewList = new ArrayList<>();
        parent_videos = view.findViewById(R.id.parent_videos);

        progress_layout = view.findViewById(R.id.progress_layout);

        noDataFound.setVisibility(View.GONE);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_videos);

        swipeRefreshLayout.setEnabled(false);

        swipeRefreshLayout.setOnRefreshListener(() -> swipeRefreshLayout.setRefreshing(false));


        update = null;
        timer = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ViewAllAdapter(videos, context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        Log.e("search_content", "search_content: " + activityy.searchContent);
        if (activityy.searchContent != null)
            search_content = activityy.searchContent;

        else {
            search_content = "";
        }
        if (activityy.bannersList.size() > 0) {
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

                indicatorLayout.addView(addSliderDotsView());
            }
            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            setViewPagerData();
        }

        //TODO BY SUMIT 14-10-20--------------------------


        parent_videos.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (loading) {
                        ++mPage;
                        if (is_post_exit == true) {
                            // lastVideoId = returnLastItemId(videos.length);
                            // Log.d("shantanu", "onScrolled: "+lastVideoId);
                            getVideoList(true);
                            progress_layout.setVisibility(View.VISIBLE);
                        } else if (is_post_exit == false) {
                            Toast.makeText(context, "BhajanCatData not found!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0) {
                    if ((!isLoading && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 1)) {
                        isLoading = true;
                        getVideoList(true);
                    }
                }
            }
        });




        recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                //togglePlayback(view, position);
            }
        }));
/*
        videos=new Videos[]{};
*/
        yoyo();
    }


    public void getSearchVideo() {

        type = 2;
        //searchContent = query;
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(VideosChildFragment.this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void yoyo() {
        initialState();
        is_post_exit = true;
        getVideoList(false);
        search_content = "";
        activityy.searchContent = "";
        swipeRefreshLayout.setRefreshing(false);
    }

    public void initialState() {
        mPage = 1;
        loading = true;
        pageSize = videos.length;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();
    }

    private void togglePlayback(View view, int position) {
        videofile = videos[position];
        if (mCastSession != null && mCastSession.isConnected()) {
            com.sanskar.tv.utility.Utils.showQueuePopup(getActivity(), view, videofile);
        } else {
            playVideo(view, position);
        }

    }

    private void playVideo(View view, int position) {
        if (isConnectingToInternet(context)) {
            video_id = videos[position].getId();
            networkCall = new NetworkCall(this, context);
            //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("video_data", videos);
        bundle.putInt("position", position);

        if (videos[position].getYoutube_url().equals("")) {
            ToastUtil.showDialogBox1(context,"Please Subscribe to Premium");

        } else {

            Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
            intent.putExtra("video_data", videos);
            intent.putExtra("position", position);
            context.sendBroadcast(intent);
        }


    }

    private String returnLastItemId(int sizeOfList) {
        int index = sizeOfList / mPageSize;
        return videos[index].getId();
    }

    public void getVideoList(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, false);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {

            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("search_content", search_content)
                    .setMultipartParameter("video_category", category.getId())
                    .setMultipartParameter("last_video_id", lastVideoId)
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("page_no", String.valueOf(mPage))
                    .setMultipartParameter("current_version", ""+ Utils.getVersionCode(getContext()))
                    .setMultipartParameter("device_type", "1");
        }

        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        progress_layout.setVisibility(View.GONE);
        if (apitype.equals(API.GET_SEARCH_VIDEOS)) {

            if (jsonstring.optBoolean("status")) {
                Log.d("GET_SEARCH_VIDEOS", "data" + jsonstring);
                VideoResponseNew videoResponseNew = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), VideoResponseNew.class);
                if (!TextUtils.isEmpty(search_content)) {
                    videos = videoResponseNew.getVideos();
                    adapter = new ViewAllAdapter(videos, context);
                    recyclerView.setAdapter(adapter);
                } else {
                   /* if (activityy.videoFragmentVideosList.containsKey(category.getId())) {
                        videos = Utils.concatenate(videos, videoResponseNew.getVideos());
                    } else {
                        videos = videoResponseNew.getVideos();
                    }*/
                    if (is_post_exit) {
                        videos = Utils.concatenate(videos, videoResponseNew.getVideos());
                    } else {
                        videos = new Videos[]{};

                        videos = videoResponseNew.getVideos();
                    }
                    adapter = new ViewAllAdapter(videos, context);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                if (videos.length < 1) {
                    noDataFound.setVisibility(View.VISIBLE);
                } else {
                    noDataFound.setVisibility(View.GONE);
                }
            }


        }

    }
/*    public void getVideoList(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }*/

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        progress_layout.setVisibility(View.GONE);
        // ToastUtil.showDialogBox(context, jsonstring);//---by sumit
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).invalidateOptionsMenu();
        ((HomeActivityy) context).handleToolbar();
        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);
    }

    public void setSearchData(String query) {
        search_content = query;
    }

    public void removeSearchData() {
        search_content = "";
        getVideoList(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (handler != null) {
            handler.removeCallbacksAndMessages(update);
            timer.cancel();
        }
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
    }

    private LinearLayout addSliderDotsView() {
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.layout_indicator, null);
        indicatorImg = linearLayout.findViewById(R.id.indicator_img);
        viewList.add(indicatorImg);
        return linearLayout;
    }

    private void setImgUnselected() {
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setImageResource(R.drawable.intro_indicator_unselected);
        }
    }

    private void setViewPagerData() {

        imageSwitcherViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setImgUnselected();
                viewList.get(position).setImageResource(R.drawable.intro_indicator_selected);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == imgString.length) {
                    currentPage = 0;
                }
                imageSwitcherViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (handler != null) {
            handler.removeCallbacksAndMessages(update);
            timer.cancel();
        }
    }

    private void setBannerImg() {
        if (activityy.bannersList.size() > 0) {
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

                indicatorLayout.addView(addSliderDotsView());
            }

            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            setViewPagerData();
        }
    }

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
    }


    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }
}
