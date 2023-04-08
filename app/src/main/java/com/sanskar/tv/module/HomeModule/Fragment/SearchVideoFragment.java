package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;

public class SearchVideoFragment extends Fragment {
    RecyclerView recyclerView;
    Context context;
    ViewAllAdapter adapter;
    Videos[] videolist=new Videos[]{};
    RecyclerView.LayoutManager layoutManager;


    public SearchVideoFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) getContext()).handleToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundleData();
        initView(view);

    }

    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("videoList")) {
                videolist = (Videos[]) bundle.getSerializable("videoList");
            }
        }
    }

    private void initView(View view) {
        context = getActivity();
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        //toolbar = view.findViewById(R.id.toobar_view_all_videos);
        //title = toolbar.findViewById(R.id.toolbar_title);
        //back = toolbar.findViewById(R.id.back);
        adapter = new ViewAllAdapter(videolist, context);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    /*    videoRecyclerView.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(context);*/
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ((HomeActivityy) context).getSupportActionBar().hide();
//        title.setText("Search BhajanCat");
//        title.setVisibility(View.VISIBLE);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((HomeActivityy)context).onBackPressed();
//            }
//        });
    }
}
