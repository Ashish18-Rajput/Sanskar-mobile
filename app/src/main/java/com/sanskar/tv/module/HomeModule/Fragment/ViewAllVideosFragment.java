package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponse;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by appsquadz on 2/6/18.
 */

public class ViewAllVideosFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    RecyclerView recyclerView;
    ViewAllAdapter viewAllAdapter;
    RecyclerView.LayoutManager layoutManager;
    Videos[] videos = new Videos[]{};
    Context context;
    private static String categoryName;
    RelativeLayout toolbar;
    ImageView back;
    TextView title;
    private static String categoryId;
    private HomeActivityy activity;
    private VideoResponse videoResponse = new VideoResponse();
    private String videoCount="0";
    NetworkCall networkCall;
    private static String viewAllType;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = ((HomeActivityy)context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all,null);
        getBundleData();
        initView(view);
//        toolbar.setVisibility(View.GONE);
        return view;
    }

    private void getBundleData() {
        if(getArguments() != null){
            Bundle bundle = getArguments();
            if(bundle.containsKey("category")){
                categoryId = bundle.getString("category").split(",")[0];
                categoryName = bundle.getString("category").split(",")[1];
                viewAllType = Constants.HOME_VIEW_ALL;
            } else if (bundle.containsKey(Constants.SANKIRTAN_VIEW_ALL)) {
                categoryId = bundle.getString(Constants.SANKIRTAN_VIEW_ALL).split(",")[0];
                categoryName = bundle.getString(Constants.SANKIRTAN_VIEW_ALL).split(",")[1];
                viewAllType = Constants.SANKIRTAN_VIEW_ALL;
            }
        }
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        videos = new Videos[]{};
        back = view.findViewById(R.id.back);
        title = view.findViewById(R.id.toolbar_title);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.handleToolbar();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivityy)context).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        title.setText(categoryName);
        title.setVisibility(View.VISIBLE);

        networkCall = new NetworkCall(this,context);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(isConnectingToInternet(context)){
//                    getViewAllVideos();
//                }
//                else{
//                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
//                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivityy)context).onBackPressed();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getViewAllVideos();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if(apitype.equals(API.VIEW_ALL_VIDEOS_HOME)){
         ion = (Builders.Any.B) Ion.with(context).load(apitype)
                 .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                 .setHeader("userid",((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter(Constants.USER_ID,activity.signupResponse.getId())
                    .setMultipartParameter("total_received_count",String.valueOf(videos.length))
                    .setMultipartParameter("video_category",categoryId);
        } else if(apitype.equals(API.VIEW_ALL_VIDEOS_SANKIRTAN)){
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setMultipartParameter(Constants.USER_ID,activity.signupResponse.getId())
                    .setMultipartParameter("total_received_count",String.valueOf(videos.length))
                    .setMultipartParameter("video_category",categoryId);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if(apitype.equals(API.VIEW_ALL_VIDEOS_HOME) || apitype.equals(API.VIEW_ALL_VIDEOS_SANKIRTAN)){

            JSONObject jsonObject = jsonstring.optJSONObject("data");
            JSONArray jsonArray = jsonObject.optJSONArray("videos");
            videos = new  Videos[jsonArray.length()];
            for(int i=0;i<jsonArray.length();i++){
                Videos video = new Gson().fromJson(jsonArray.get(i).toString(),Videos.class);
                videos[i] = video;
            }
            viewAllAdapter = new ViewAllAdapter(videos, context);
            recyclerView.setAdapter(viewAllAdapter);
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
       // ToastUtil.showDialogBox(context,jsonstring);//--by sumit
    }

    public void getViewAllVideos() {
        if (viewAllType.equalsIgnoreCase(Constants.HOME_VIEW_ALL)) {
            networkCall.NetworkAPICall(API.VIEW_ALL_VIDEOS_HOME,true);
        } else if (viewAllType.equalsIgnoreCase(Constants.SANKIRTAN_VIEW_ALL)) {
            networkCall.NetworkAPICall(API.VIEW_ALL_VIDEOS_SANKIRTAN,true);
        }
    }
}