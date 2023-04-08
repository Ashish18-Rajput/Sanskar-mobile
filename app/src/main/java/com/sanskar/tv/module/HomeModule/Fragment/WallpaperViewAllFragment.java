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

import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.WallPaperAdapter3;
import com.sanskar.tv.module.HomeModule.Pojo.WallPaperModel;

import java.util.List;


public class WallpaperViewAllFragment extends Fragment {

    Context context;
    HomeActivityy homeActivityy;
    List<WallPaperModel> wallPaperModels;
    RecyclerView recyclerView_viewAll;


    public WallpaperViewAllFragment() {
        // Required empty public constructor
    }

    public static WallpaperViewAllFragment newInstance(String param1, String param2) {
        return new WallpaperViewAllFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        homeActivityy = (HomeActivityy) getActivity();


    }

    private void getBundleData() {
        if (getArguments()!=null){
            wallPaperModels = (List<WallPaperModel>) getArguments().getSerializable(Constants.DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_wallpaper_view_all, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundleData();
        initViews(view);
    }

    private void initViews(View view) {
        recyclerView_viewAll = view.findViewById(R.id.recyclerView_viewAll);
        recyclerView_viewAll.setLayoutManager(new LinearLayoutManager(context));
        recyclerView_viewAll.setAdapter(new WallPaperAdapter3(context,homeActivityy,wallPaperModels));
    }



}