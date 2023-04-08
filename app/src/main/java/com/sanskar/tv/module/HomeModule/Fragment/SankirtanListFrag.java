package com.sanskar.tv.module.HomeModule.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomTouchListener;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.cast.ExpandedControlsActivity;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.SankirtanAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.SankirtanBean;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.onItemClickListener;
import com.sanskar.tv.utility.Utils;
import com.sanskar.tv.youtube.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class SankirtanListFrag extends Fragment implements NetworkCall.MyNetworkCallBack {

    private Context context;
    private NetworkCall networkCall;
    private RelativeLayout noDataFound;
    private ScrollView parentScroll;
    private PlaybackLocation mLocation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PlaybackState mPlaybackState;
    private SankirtanAdapter sankirtanAdapter;
    public SearchView searchView;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private ImageView backImg;
    private ImageView closeIV;
    public  RelativeLayout toolbar;
    private CastContext mCastContext;
    private CastSession mCastSession;
    Activity activity;
    Videos videofile;
    private MediaInfo mSelectedMedia;
    private String searchContent;
    String video_id;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context=context;
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sankirtan_list, container, false);
        mCastContext = CastContext.getSharedInstance(activity);
        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();
//        return inflater.inflate(R.layout.fragment_view_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (HomeActivityy.playerlayout.getVisibility()==View.VISIBLE){
            HomeActivityy.playerlayout.setVisibility(View.GONE);
            Constants.SHOW_LAYOUT_AUDIO = "true";
        }
        initViews(view);
        getSankirtanList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isConnectingToInternet(context)){
                    networkCall = new NetworkCall(SankirtanListFrag.this, context);
                    networkCall.NetworkAPICall(API.SANKIRTAN_LIST,false);
                }
                else{
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new CustomTouchListener(getActivity(), new onItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                togglePlayback(view,position);
                //playVideo(view,position);

               /* Videos[] videos = new Videos[activity.homeVideoList.size()];
                for (int i = 0; i < activity.homeVideoList.size(); i++) {
                    videos[i] = activity.homeVideoList.get(i);
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable("video_data", videos);
                bundle.putInt("position", position);
                mSelectedMedia = (MediaInfo) Arrays.asList(videos);

                if (context instanceof HomeActivityy) {
                    if(AudioPlayerService.mediaPlayer != null){
                        try {
                            Constants.IS_PAUSEDFROMHOME = "true";
                            AudioPlayerService.mediaPlayer.stop();

                            HomeActivityy.playerlayout.setVisibility(View.GONE);
                            Constants.CALL_RESUME = "false";
                        }catch (IllegalStateException e){
                            e.printStackTrace();
                        }
                    }

                    Intent intent=new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                    intent.putExtra("video_data",videos);
                    intent.putExtra("position",position);
                    activity.sendBroadcast(intent);

                } else if (context instanceof LiveStreamJWActivity) {
                    Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
                    liveIntent.putExtras(bundle);
                    ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
                }

                if (isConnectingToInternet(context)) {
                    video_id = activity.homeVideoList.get(position).getId();
                    //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }*/
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).invalidateOptionsMenu();
        ((HomeActivityy)context).handleToolbar();

/*        mCastContext.getSessionManager().addSessionManagerListener(
                mSessionManagerListener, CastSession.class);*/
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion =null;
        if(apitype.equals(API.SANKIRTAN_LIST)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId());

        } else if(apitype.equals(API.SEARCH_SANKIRTAN_LIST)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter("user_id",((HomeActivityy)context).signupResponse.getId())
                    .setMultipartParameter("search_content", ((HomeActivityy) context).searchContent)
                    .setMultipartParameter("video_category","")
                    .setMultipartParameter("last_video_id","");
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);

        if (API.SANKIRTAN_LIST.equalsIgnoreCase(apitype)) {
            if (result.optBoolean("status")) {
                JSONArray jsonArray = result.optJSONArray("data");
                if (((HomeActivityy) context).sankirtanBeanList.size() > 0)
                    ((HomeActivityy) context).sankirtanBeanList.clear();
                if (jsonArray.length() > 0) {
                    noDataFound.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        SankirtanBean sankirtanBean = new Gson().fromJson(jsonArray.opt(i).toString(),
                                SankirtanBean.class);
                        ((HomeActivityy) context).sankirtanBeanList.add(sankirtanBean);
                    }
                    sankirtanAdapter.notifyDataSetChanged();
                } else {
                    sankirtanAdapter.notifyDataSetChanged();
                    noDataFound.setVisibility(View.VISIBLE);
                }

            } else {
                sankirtanAdapter.notifyDataSetChanged();
                noDataFound.setVisibility(View.VISIBLE);
            }
        } else if (API.SEARCH_SANKIRTAN_LIST.equalsIgnoreCase(apitype)) {
            if (result.optBoolean("status")) {
                JSONObject jsonObject = result.optJSONObject("data");
                JSONArray videosArray = jsonObject.optJSONArray("videos");
                if (videosArray.length() > 0) {
                    noDataFound.setVisibility(View.GONE);
                    Videos[] sankirtanVideos = new Videos[videosArray.length()];
                    for (int i = 0; i < videosArray.length(); i++) {
                        Videos videoBean = new Gson().fromJson(videosArray.opt(i).toString(),
                                Videos.class);
                        sankirtanVideos[i] = videoBean;
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("search_sankirtan", sankirtanVideos);
                    SearchSankirtanFrag frag = new SearchSankirtanFrag();
                    frag.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("SEARCH_SANKIRTAN")
                            .replace(R.id.container_layout_home, frag)
                            .commit();
                } else {
                    searchContent = "";
                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                }
//                else {
//                    sankirtanAdapter.notifyDataSetChanged();
//                    noDataFound.setVisibility(View.VISIBLE);
//                }
//
//            } else {
//                sankirtanAdapter.notifyDataSetChanged();
//                noDataFound.setVisibility(View.VISIBLE);
//            }
            }
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showDialogBox(context,jsonstring);
    }

    private void initViews(View view) {
        context = getActivity();
        networkCall = new NetworkCall(this, context);
        parentScroll = view.findViewById(R.id.scroll_view_home);
        noDataFound = view.findViewById(R.id.no_data_found_home);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
//        trendingRecyclerView = view.findViewById(R.id.fragment_home_rv_live_channel);
        recyclerView = view.findViewById(R.id.fragment_home_rv_videos_by_category);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        sankirtanAdapter = new SankirtanAdapter(((HomeActivityy) context).sankirtanBeanList, context);
        recyclerView.setAdapter(sankirtanAdapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        searchView = toolbar.findViewById(R.id.search_view);
        backImg = toolbar.findViewById(R.id.back);
        closeIV = toolbar.findViewById(R.id.close_right);

        searchView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

    }



    public void getSankirtanList() {
        if(((HomeActivityy) context).sankirtanBeanList.isEmpty() || ((HomeActivityy) context).isLike) {
            if (isConnectingToInternet(context)) {
                networkCall.NetworkAPICall(API.SANKIRTAN_LIST, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        }
    }

    public void getSearchSankirtan() {
        //searchContent = query;
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(SankirtanListFrag.this, context);
            networkCall.NetworkAPICall(API.SEARCH_SANKIRTAN_LIST, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void togglePlayback(View view, int position) {

        Videos[] videos = new Videos[((HomeActivityy)activity).homeVideoList.size()];
        for (int i = 0; i < ((HomeActivityy)activity).homeVideoList.size(); i++) {
            videos[i] = ((HomeActivityy)activity).homeVideoList.get(i);
        }
        //mSelectedMedia = (MediaInfo) Arrays.asList(videos);
        videofile = videos[position];
        if (mCastSession!=null && mCastSession.isConnected()){
            Utils.showQueuePopup(getActivity(), view, videofile);
        }else{
            playVideo(view,position);
        }
        //break;

        /*case PLAYING:
         *//*mPlaybackState = PlaybackState.PAUSED;
                mVideoView.pause();*//*
                break;

            case IDLE:
                switch (mLocation) {
                    case LOCAL:
                        playVideo(view,position);
                        *//*mVideoView.setVideoURI(Uri.parse(mSelectedMedia.getContentId()));
                        mVideoView.seekTo(0);
                        mVideoView.start();
                        mPlaybackState = PlaybackState.PLAYING;
                        restartTrickplayTimer();
                        updatePlaybackLocation(PlaybackLocation.LOCAL);*//*
                        break;
                    case REMOTE:
                        if (mCastSession != null && mCastSession.isConnected()) {
                            Utils.showQueuePopup(getActivity(), view, mSelectedMedia);
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
        // }
               break;*/
        updatePlayButton(mPlaybackState);
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
                updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                updatePlaybackLocation(PlaybackLocation.LOCAL);
                mPlaybackState = PlaybackState.IDLE;
                mLocation = PlaybackLocation.LOCAL;
                updatePlayButton(mPlaybackState);
                getActivity().invalidateOptionsMenu();
            }
        };
    }

    private void updatePlaybackLocation(PlaybackLocation location) {
        mLocation = location;
       /* if (location == PlaybackLocation.LOCAL) {
            if (mPlaybackState == PlaybackState.PLAYING
                    || mPlaybackState == PlaybackState.BUFFERING) {
                setCoverArtStatus(null);
                startControllersTimer();
            } else {
                stopControllersTimer();
                setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            }
        } else {
            stopControllersTimer();
            setCoverArtStatus(MediaUtils.getImageUrl(mSelectedMedia, 0));
            updateControllersVisibility(false);
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mCastContext.getSessionManager().removeSessionManagerListener(
                mSessionManagerListener, CastSession.class);
    }

    private void playVideo(View view,int position){
        Videos[] videos = new Videos[((HomeActivityy)activity).homeVideoList.size()];
        for (int i = 0; i < ((HomeActivityy)activity).homeVideoList.size(); i++) {
            videos[i] = ((HomeActivityy)activity).homeVideoList.get(i);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("video_data", videos);
        bundle.putInt("position", position);
        //mSelectedMedia = (MediaInfo) Arrays.asList(videos);
        videofile = videos[position];

        if (context instanceof HomeActivityy) {
            if(AudioPlayerService.mediaPlayer != null){
                try {
                    Constants.IS_PAUSEDFROMHOME = "true";
                    AudioPlayerService.mediaPlayer.stop();

                    HomeActivityy.playerlayout.setVisibility(View.GONE);
                    Constants.CALL_RESUME = "false";
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
            if (videos[position].getYoutube_url().equals("")) {
                Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                activity.sendBroadcast(intent);
            }else{
               /* Intent intent = new Intent(HomeActivityy.PLAYVIDEO_JWPLAYER);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                activity.sendBroadcast(intent);*/
                Intent intent=new Intent(activity, MainActivity.class);
                intent.putExtra("video_data", videos);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }

        } else if (context instanceof LiveStreamJWActivity) {
            Intent liveIntent = new Intent(context, LiveStreamJWActivity.class);
            liveIntent.putExtras(bundle);
            ((LiveStreamJWActivity) context).startActivityForResult(liveIntent, 121);
        }

        if (isConnectingToInternet(context)) {
            video_id = ((HomeActivityy)activity).homeVideoList.get(position).getId();
            //networkCall.NetworkAPICall(API.RECENT_VIEW, false);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }

    private void updatePlayButton(PlaybackState state) {
       /* Log.d("TAG", "Controls: PlayBackState: " + state);
        boolean isConnected = (mCastSession != null)
                && (mCastSession.isConnected() || mCastSession.isConnecting());

        switch (state) {
            case PLAYING:

                break;
            case IDLE:

                break;
            case PAUSED:

                break;
            case BUFFERING:

                break;
            default:
                break;
        }*/
    }

    public enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE
    }

    public enum PlaybackLocation {
        LOCAL,
        REMOTE
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
       /* remoteMediaClient.load(mSelectedMedia,
                new MediaLoadOptions.Builder()
                        .setAutoplay(autoPlay)
                        .setPlayPosition(position).build());*/
    }
}
