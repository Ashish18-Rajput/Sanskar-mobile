package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommonSearchFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    Context context;
    HomeActivityy homeActivityy;
    NetworkCall networkCall;
    RecyclerView common_search_recyclerView;
    List<MenuMasterList> menuMasterListList = new ArrayList<>();



    public CommonSearchFragment() {
    }

    public static CommonSearchFragment newInstance(String param1, String param2) {
        return new CommonSearchFragment();
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
       return inflater.inflate(R.layout.fragment_common_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        common_search_recyclerView = view.findViewById(R.id.common_search_recyclerView);
        common_search_recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        return null;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}