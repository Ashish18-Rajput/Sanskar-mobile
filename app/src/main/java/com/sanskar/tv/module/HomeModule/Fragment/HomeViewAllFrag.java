package com.sanskar.tv.module.HomeModule.Fragment;


import android.content.Context;
import android.os.Bundle;
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
import com.sanskar.tv.module.HomeModule.Adapter.HomeViewAllAdapter;
import com.sanskar.tv.R;

public class HomeViewAllFrag extends Fragment {

    private RecyclerView viewAllRV;
    private HomeViewAllAdapter viewAllAdapter;
    private LinearLayoutManager layoutManager;
    private Context ctx;
    private String listType;
    RelativeLayout toolbar;
    ImageView back;
    TextView title;

    public HomeViewAllFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_view_all, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBundleData();
        initViews(getView());
//        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) ctx).handleToolbar();
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            listType = bundle.getString("HOME_LIST_TYPE");
        }
    }

    private void initViews(View view) {
        ctx = getActivity();
        back=view.findViewById(R.id.back);
        viewAllRV = view.findViewById(R.id.view_all_recycler_view);
        viewAllAdapter = new HomeViewAllAdapter(ctx, listType);
        layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        viewAllRV.setLayoutManager(layoutManager);
        viewAllRV.setAdapter(viewAllAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivityy)getActivity()).onBackPressed();
            }
        });
    }
}
