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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.R;

public class SearchSankirtanFrag extends Fragment {

    RecyclerView recyclerView;
    RelativeLayout toolbar;
    TextView title;
    ImageView back;
    Context context;
    ViewAllAdapter adapter;
    Videos[] sankirtanVideos;
    MenuMasterList[] masterLists;
    RecyclerView.LayoutManager layoutManager;


    public SearchSankirtanFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBundleData();
        initView(view);
        toolbar.setVisibility(View.GONE);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("search_sankirtan")) {
                sankirtanVideos = (Videos[]) bundle.getSerializable("search_sankirtan");
            }
        }
    }

    private void initView(View view) {
        context = getActivity();

        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        title = toolbar.findViewById(R.id.toolbar_title);
        back = toolbar.findViewById(R.id.back);
        adapter = new ViewAllAdapter(sankirtanVideos, context);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ((HomeActivityy)context).getSupportActionBar().hide();
        title.setText("Search Sankirtan");
        title.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivityy)context).onBackPressed();
            }
        });
    }
}
