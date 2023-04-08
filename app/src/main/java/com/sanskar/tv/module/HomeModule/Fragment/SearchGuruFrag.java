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
import com.sanskar.tv.module.HomeModule.Adapter.GurusAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGuruFrag extends Fragment {
    RecyclerView recyclerView;
    Context context;
    GurusAdapter adapter;
    GuruPojo[] gurulist;
    RecyclerView.LayoutManager layoutManager;


    public SearchGuruFrag() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) getContext()).handleToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_guru, container, false);
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
            if (bundle.containsKey("gurulist")) {
                gurulist = (GuruPojo[]) bundle.getSerializable("gurulist");
            }
        }
    }

    private void initView(View view) {
        context = getActivity();
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        //toolbar = view.findViewById(R.id.toobar_view_all_videos);
        //title = toolbar.findViewById(R.id.toolbar_title);
        //back = toolbar.findViewById(R.id.back);
        List<GuruPojo> guruPojoList= Arrays.asList(gurulist);
        adapter = new GurusAdapter(guruPojoList, context);
        layoutManager = new LinearLayoutManager(context);
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
