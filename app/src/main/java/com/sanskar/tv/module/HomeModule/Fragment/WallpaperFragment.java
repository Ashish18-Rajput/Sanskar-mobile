package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.WallPaperAdapter1;
import com.sanskar.tv.module.HomeModule.Pojo.WallPaperModelMain;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WallpaperFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    Context context;
    HomeActivityy homeActivityy;
    NetworkCall networkCall;
    RecyclerView recyclerView_Wallpaper;
    List<WallPaperModelMain> wallPaperModelMainList = new ArrayList<>();
    WallPaperAdapter1 wallPaperAdapter1;
    SwipeRefreshLayout swipe_refresh_wallpaper;


    public WallpaperFragment() {
        // Required empty public constructor
    }


    public static WallpaperFragment newInstance(String param1, String param2) {
        return new WallpaperFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        homeActivityy = (HomeActivityy) getActivity();
        networkCall = new NetworkCall(this,context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wallpaper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_Wallpaper = view.findViewById(R.id.recyclerView_Wallpaper);
        recyclerView_Wallpaper.setLayoutManager(new LinearLayoutManager(context));
        swipe_refresh_wallpaper = view.findViewById(R.id.swipe_refresh_wallpaper);
        swipe_refresh_wallpaper.setOnRefreshListener(() -> {
            fetchData();
        });

        fetchData();

        Toast.makeText(context, "LongPress on Any Image to download", Toast.LENGTH_SHORT).show();
        wallPaperAdapter1 = new WallPaperAdapter1(context,homeActivityy,wallPaperModelMainList);
        recyclerView_Wallpaper.setAdapter(wallPaperAdapter1);
    }

    private void fetchData() {
        networkCall.NetworkAPICall(API.GET_WALLPAPER_MENU,true);
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,SignupResponse.class);
        Builders.Any.B ion = null;
        if (apitype.equalsIgnoreCase(API.GET_WALLPAPER_MENU)){
            ion = (Builders.Any.B) Ion.with(context)
                    .load(apitype)
                    .setMultipartParameter(Constants.USER_ID,signupResponse.getId());
        }
        return ion ;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipe_refresh_wallpaper.setRefreshing(false);
        JSONArray jsonArray = jsonstring.optJSONArray(Constants.DATA);
        if (jsonArray!=null && jsonArray.length()>0){
            wallPaperModelMainList.clear();
            for (int index = 0; index < jsonArray.length(); index++){
                WallPaperModelMain wallPaperModelMain = new Gson().fromJson(jsonArray.opt(index).toString(),WallPaperModelMain.class);
                wallPaperModelMainList.add(wallPaperModelMain);
            }

            wallPaperAdapter1.notifyDataSetChanged();
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipe_refresh_wallpaper.setRefreshing(false);
    }
}