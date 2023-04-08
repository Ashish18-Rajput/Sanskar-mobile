package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.LiveChannelAdapter;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

/**
 * Created by appsquadz on 2/14/2018.
 */

public class LiveTvFragment extends Fragment {

    Context context;
    RecyclerView recyclerView;
    LiveChannelAdapter adapter;
    LinearLayoutManager layoutManager;
    private NetworkCall networkCall;
    RelativeLayout toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    private String lastchannelId="";
    private RelativeLayout noDataFound;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private ImageView bannerImg;
    private AppTextView guruTxt;
    private AppTextView guruTxtHeader;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_tv,null);
        context = getActivity();
        initView(view);

        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        noDataFound = view.findViewById(R.id.no_data_found_view_all);
        bannerImg = view.findViewById(R.id.banner_img);
        guruTxt = view.findViewById(R.id.guru_txt);
        guruTxtHeader = view.findViewById(R.id.guru_txt_header);
        guruTxt.setVisibility(View.GONE);
        guruTxtHeader.setVisibility(View.GONE);
        bannerImg.setImageResource(R.mipmap.articles);
        layoutManager = new LinearLayoutManager(context);
        adapter = new LiveChannelAdapter(((HomeActivityy)context).homeChannelList, context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((HomeActivityy)context).homeChannelList.size()>0) ((HomeActivityy)context).homeChannelList.clear();
                adapter.notifyDataSetChanged();
                lastchannelId="";

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading /*&& !(lastCommentId.equalsIgnoreCase(firstCommentId))*/)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            lastchannelId = ((HomeActivityy)context).newsList.get(((HomeActivityy)context).homeChannelList.size()-1).getId();

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).invalidateOptionsMenu();
        toolbar.setVisibility(View.GONE);
//        ((HomeActivityy) context).handleToolBar();

        ((HomeActivityy) context).handleToolbar();
    }




}
