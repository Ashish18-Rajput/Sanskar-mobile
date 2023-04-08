package com.sanskar.tv.module.goLiveModule.controller;


import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Adapter.LiveGuruAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class LiveGuruActivity extends Fragment  implements NetworkCall.MyNetworkCallBack {

//    JWPlayerView playerView;
//    KeepScreenOnHandler keepScreenOnHandler;
//    String url;
    private RecyclerView rvGuruLive;
    SwipeRefreshLayout swipeRefreshLayout;
    private NetworkCall networkCall;
    private ArrayList<LiveGuru> liveGuruArrayList= new ArrayList();
    LiveGuruAdapter liveGuruAdapter;

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy)getContext()).handleToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_live_guru,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setAdapter();
        if (((HomeActivityy) getActivity()).bhajanResponseArrayList.isEmpty()) {
            if (isConnectingToInternet(getActivity())) {
                networkCall = new NetworkCall(this, getActivity());
                networkCall.NetworkAPICall(API.GET_LIVE_GURU, true);
            } else {
                ToastUtil.showDialogBox(getActivity(), Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        }

    }
    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    private void loadLiveGuru() {
            if (isConnectingToInternet(getActivity())) {
                networkCall = new NetworkCall(LiveGuruActivity.this, getActivity());
                networkCall.NetworkAPICall(API.GET_LIVE_GURU, true);
            } else {
//                swipeRefreshLayout.setRefreshing(false);
                ToastUtil.showDialogBox(getActivity(), Networkconstants.ERR_NETWORK_TIMEOUT);
            }


    }

    private void setAdapter() {
         liveGuruAdapter = new LiveGuruAdapter(getContext(),liveGuruArrayList);
        rvGuruLive.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvGuruLive.setLayoutManager(mLayoutManager);
        rvGuruLive.setItemAnimator(new DefaultItemAnimator());
       // rvGuruLive.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvGuruLive.setAdapter(liveGuruAdapter);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
//        playerView.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
        super.onConfigurationChanged(newConfig);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (playerView.getFullscreen()) {
//                playerView.setFullscreen(false, false);
//                return false;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    private void initView(View view) {
//        playerView = findViewById(R.id.playerView);
        rvGuruLive = view.findViewById(R.id.rv_live_guru);

//        url = "http://13.127.166.4:1935/live/myStream/playlist.m3u8";
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GET_LIVE_GURU)) {
            ion = (Builders.Any.B) Ion.with(getActivity()).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(getActivity()).getString(Constants.JWT)!=null?SharedPreference.getInstance(getActivity()).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",((HomeActivityy) getActivity()).signupResponse.getId())
                    .setMultipartParameter("user_id", ((HomeActivityy) getActivity()).signupResponse.getId());
        }
        return  ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
//        swipeRefreshLayout.setRefreshing(false);
        if (jsonstring.optBoolean("status")) {
            JSONArray jsonArray = jsonstring.optJSONArray("data");
            if (apitype.equals(API.GET_LIVE_GURU)) {
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        LiveGuru liveGuru = new Gson().fromJson(jsonArray.opt(i).toString(), LiveGuru.class);
                        liveGuruArrayList.add(liveGuru);
                    }
                    liveGuruAdapter.notifyDataSetChanged();
                }
            } else {
                    ToastUtil.showDialogBox(getContext(), getString(R.string.no_data_found));
                }
            }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

}
