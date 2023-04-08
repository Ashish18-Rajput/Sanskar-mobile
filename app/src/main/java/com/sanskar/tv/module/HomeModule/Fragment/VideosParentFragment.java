package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Adapter.VideoCategoryNameAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.VideosParentViewPagerAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.Category;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.checkedsnackbar;
import static com.sanskar.tv.module.HomeModule.Fragment.NotificationFragment.snackbar;

/**
 * Created by appsquadz on 2/15/2018.
 */

public class VideosParentFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    public Context context;
    HomeActivityy activityy;
    TabLayout tabLayout;
    ViewAllAdapter viewAllAdapter;
    Spinner spinner;
    private int type;
    RecyclerView search_recycler;

    RelativeLayout rl_search, rl_video_container;
    RecyclerView video_category_recycler;
    VideoCategoryNameAdapter videoCategoryNameAdapter;

    private Videos[] videoss = new Videos[]{};

    public AppViewPager viewPager;
    VideosParentViewPagerAdapter videosParentViewPagerAdapter;
    private NetworkCall networkCall;
    private int tabPosition;
    public RelativeLayout toolbar;
    public SearchView searchView;
    private ImageView backImg;
    private ImageView closeIV;
    private Banners[] banners = new Banners[]{};
    private int mPage = 1;
    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;
    ViewPager imageSwitcherViewPager;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;
    private List<ImageView> viewList;
    private String[] imgString;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    int currentPage = 0;
    public Handler handler;
    public Runnable update;
    private List<String> categories;
    public AppTextView edit, done;
    String searchContent = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activityy = ((HomeActivityy) context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos_parent, null);
        initView(view);
        edit.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        if (checkedsnackbar.equalsIgnoreCase("snackbar")) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        }
        return view;
    }

    private void initView(View view) {

        if (((HomeActivityy) getActivity()).playerlayout.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout.setVisibility(View.GONE);
            AudioPlayerService.mediaPlayer.pause();
        }
        if (((HomeActivityy) getActivity()).playerlayout1.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout1.setVisibility(View.GONE);

        }
        if (((HomeActivityy) getActivity()).playerlayout2.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) context).playerlayout2.setVisibility(View.GONE);

        }
        if (downloadmediaplayer != null) {
            if (downloadmediaplayer.isPlaying()) {
                downloadmediaplayer.pause();
            }
        } else {

        }
        if (playlistmediaplayer != null) {
            if (playlistmediaplayer.isPlaying()) {
                playlistmediaplayer.pause();
            }
        } else {

        }
        edit = ((HomeActivityy) context).editTV;
        done = ((HomeActivityy) context).cancle_noti;

        toolbar = view.findViewById(R.id.toolbar_watch_video);
        tabLayout = view.findViewById(R.id.tab_layout_videos_parent);
        viewPager = view.findViewById(R.id.view_pager_videos_parent);
        video_category_recycler = view.findViewById(R.id.video_category_recycler);
        rl_search = view.findViewById(R.id.rl_search);
        rl_video_container = view.findViewById(R.id.rl_video_container);
        search_recycler = view.findViewById(R.id.search_recycler);
        viewPager.setPagingEnabled(true);
        searchView = toolbar.findViewById(R.id.search_view);
        backImg = toolbar.findViewById(R.id.back);
        closeIV = toolbar.findViewById(R.id.close_right);
        spinner = (Spinner) view.findViewById(R.id.spinner);

        viewList = new ArrayList<>();
        imgString = new String[]{};
        searchView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewPager.setCurrentItem(position);
                //        Toast.makeText(context, "ys", Toast.LENGTH_SHORT).show();
                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((HomeActivityy) context).searchContent = "";
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activityy.getSupportActionBar().show();

        ((HomeActivityy) context).invalidateOptionsMenu();
       /* if (activityy.videoFragmentVideoesCategoryList.isEmpty() || activityy.isLike) {
            getCategoryList();
        } else {
            setTabs(true);
            //        categories.clear();
            setData();
//            setBannerImg();
            video_category_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            videoCategoryNameAdapter = new VideoCategoryNameAdapter(activityy.videoFragmentVideoesCategoryList, getActivity(), VideosParentFragment.this);

            video_category_recycler.setAdapter(videoCategoryNameAdapter);
        }*/


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tabPosition = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((HomeActivityy) context).searchContent = "";
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

//        if (tabLayout.getTabCount() > 0) {
//            tabLayout.getTabAt(tabPosition).select();
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();

        /*if (activityy.videoFragmentVideoesCategoryList.isEmpty() || activityy.isLike) {
            getCategoryList();
        } else {
            setTabs(true);
            //        categories.clear();
            setData();
//            setBannerImg();
            video_category_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            videoCategoryNameAdapter = new VideoCategoryNameAdapter(activityy.videoFragmentVideoesCategoryList, getActivity(), VideosParentFragment.this);

            video_category_recycler.setAdapter(videoCategoryNameAdapter);
            LoadQuestion(activityy.videoFragmentVideoesCategoryList.get(0).getId(), 0);

        }*/

        getCategoryList();


        //((HomeActivityy) context).toolbar.setElevation(100);
    }

    public void getCategoryList() {
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);
            Log.e("TAG", "getCategoryList: " + ((HomeActivityy) context).searchContent);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {


            if (type == 2) {

                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("search_content", ((HomeActivityy) context).searchContent)
                        .setMultipartParameter("video_category", "")
                        .setMultipartParameter("last_video_id", "")
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage))
                        .setMultipartParameter("current_version", ""+ Utils.getVersionCode(getContext()))
                        .setMultipartParameter("device_type", "1");

            } else {
                ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("search_content", "")
                        .setMultipartParameter("video_category", "")
                        .setMultipartParameter("last_video_id", "")
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(mPage))
                        .setMultipartParameter("current_version", ""+Utils.getVersionCode(getContext()))
                        .setMultipartParameter("device_type", "1");
            }
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {

        if (apitype.equals(API.GET_SEARCH_VIDEOS)) {

            if (type == 2) {
                /*     rl_video_container.setVisibility(View.GONE);*/
                /*  rl_search.setVisibility(View.VISIBLE);*/
                if (jsonstring.optBoolean("status")) {


                    VideoResponseNew videoResponseNew = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), VideoResponseNew.class);

                    videoss = videoResponseNew.getVideos();
                      /*  viewAllAdapter = new ViewAllAdapter(videoss, context);
                       LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        search_recycler.setLayoutManager(layoutManager);
                        search_recycler.setAdapter(viewAllAdapter);
*/
                    if (videoss.length != 0) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("videoList", videoss);
                        SearchVideoFragment frag = new SearchVideoFragment();
                        frag.setArguments(bundle);
                        ((HomeActivityy) context)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("ViDEO_SEARCH")
                                .replace(R.id.container_layout_home, frag)
                                .commit();
                    } else {
                        ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                    }
                } else {
                    searchContent = "";
                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                }

            } else {

                if (jsonstring.optBoolean("status")) {
                    VideoResponseNew videoResponseNew = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), VideoResponseNew.class);
                    if (activityy.bannersList.size() > 0) {
                        activityy.bannersList.clear();
                    }
                    banners = videoResponseNew.getBanners();
                    for (int i = 0; i < banners.length; i++) {
                        Banners banner = banners[i];
                        activityy.bannersList.add(banner);
                    }
//                setBannerImg();
                    //imageSliderViewPagerAdapter.notifyDataSetChanged();

                    activityy.videoFragmentVideoesCategoryList.clear();


                    for (int i = 0; i < videoResponseNew.getCategory().length; i++) {
                        activityy.videoFragmentVideoesCategoryList.add(videoResponseNew.getCategory()[i]);
                    }
                    setTabs(true);
//                categories.clear();
                    setData();

                    video_category_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                    videoCategoryNameAdapter = new VideoCategoryNameAdapter(activityy.videoFragmentVideoesCategoryList, getActivity(), VideosParentFragment.this);

                    video_category_recycler.setAdapter(videoCategoryNameAdapter);

                    LoadQuestion(activityy.videoFragmentVideoesCategoryList.get(0).getId(), 0);

                }
            }
        }
    }

    private void setData() {
        categories = new ArrayList<String>();
        for (int i = 0; i < activityy.videoFragmentVideoesCategoryList.size(); i++) {
            categories.add(activityy.videoFragmentVideoesCategoryList.get(i).getCategory_name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activityy, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_drop_down_item);
        spinner.setAdapter(dataAdapter);
    }

    private void setTabs(boolean n) {
        tabLayout.removeAllTabs();
        if (!activityy.videoFragmentVideoesCategoryList.get(0).getId().equals(""))
            activityy.videoFragmentVideoesCategoryList.add(0, new Category());
        for (Category category : activityy.videoFragmentVideoesCategoryList) {
            tabLayout.addTab(tabLayout.newTab().setText(category.getCategory_name()).setTag(category.getId()));

        }
        if (isAdded()) {
            videosParentViewPagerAdapter = new VideosParentViewPagerAdapter(getChildFragmentManager(), context, activityy.videoFragmentVideoesCategoryList.size(), ((HomeActivityy) context).searchContent);
            viewPager.setAdapter(videosParentViewPagerAdapter);

        } else {

        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
       // ToastUtil.showDialogBox(context, jsonstring);//---by sumit
    }

    public void getCategorySearchList() {
        type = 2;
        if (isConnectingToInternet(context)) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);
            Log.e("TAG", "getCategoryList: " + ((HomeActivityy) context).searchContent);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void LoadQuestion(String id, int position) {
        for (int i = 0; i < activityy.videoFragmentVideoesCategoryList.size(); i++) {
            activityy.videoFragmentVideoesCategoryList.get(i).setClicked(false);
        }

        activityy.videoFragmentVideoesCategoryList.get(position).setClicked(true);
        videoCategoryNameAdapter.notifyDataSetChanged();
/*
        for (int i = 0; i < data.getQuestions().size(); i++) {
            if (data.getQuestions().get(i).getSectionId().equals(id)) {
                Question question = data.getQuestions().get(i);
                questions.add(question);
            }
        }

        if (questions.size() == 0) {
            tvNoDataFound.setVisibility(View.VISIBLE);
        } else {
            tvNoDataFound.setVisibility(View.GONE);
        }

        questionAdapter.notifyDataSetChanged();*/

        viewPager.setCurrentItem(position);
        videoCategoryNameAdapter.notifyDataSetChanged();
        //        Toast.makeText(context, "ys", Toast.LENGTH_SHORT).show();


    }
}

