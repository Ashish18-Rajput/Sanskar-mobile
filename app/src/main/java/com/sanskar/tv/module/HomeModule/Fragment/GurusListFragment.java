  package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.CustomViews.AppViewPager;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.GurusAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.Image_Slider_View_Pager_Adapter;
import com.sanskar.tv.module.HomeModule.Pojo.Banners;
import com.sanskar.tv.module.HomeModule.Pojo.VideoResponseNew;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

public class GurusListFragment extends Fragment implements SearchInterface, NetworkCall.MyNetworkCallBack {
    private int mPage = 1;
    public SearchView searchView;
    public RelativeLayout toolbar;
    RecyclerView recyclerView;
    GurusAdapter gurusAdapter;
    Videos[] videos;
    Context context;
    String title;
    RelativeLayout noDataFound;
    SwipeRefreshLayout swipeRefreshLayout;
    int pastVisiblesItems , visibleItemCount, totalItemCount ;

    private int firstVisibleItem, visibleThreshold = 5, previousTotalItemCount;
    private LinearLayoutManager layoutManager;
    private NetworkCall networkCall;
    private String lastGuruId = "";
    boolean loading = false;
    //    private ImageView closeIV;
//    private ImageView backIV;
    private String searchContent = "";
    private String listType;
    private ImageView bannerImg;
    private AppTextView guruTxt;
    private AppTextView guruTxtHeader;
    private CardView card_view;
    private NestedScrollView rl_nested;
    HomeActivityy activityy;

    private List<ImageView> viewList;
    //  ArrayList<GuruPojo> alphabeticalcharlist = new ArrayList<>();
    private GuruPojo[] gurulist;
    private String[] imgString;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    int currentPage = 0;
    public Handler handler;
    public Runnable update;
    public List<Banners> bannersList;
    private Banners[] banners;
    SearchInterface searchInterface;
    private LinearLayout indicatorLayout;
    private ImageView indicatorImg;
    AppViewPager imageSwitcherViewPager;
    private Image_Slider_View_Pager_Adapter imageSliderViewPagerAdapter;
    RelativeLayout intro_images_layout,progress_layout;

    ProgressBar progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_1, null);
        context = getActivity();
        getBundleData();
        initView(view);
        if (((HomeActivityy) context).homeGuruList.size()>0){
            ((HomeActivityy) context).homeGuruList.clear();
        }
        lastGuruId = "";
        getVideoList(true);


        //networkHit(true);
        ((HomeActivityy) getContext()).setSetSearchListener(searchInterface);
        return view;
    }

    public void getVideoList(boolean progress) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, true);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).invalidateOptionsMenu();
        if (title.equals("guru")) {
            ((HomeActivityy) context).getSupportActionBar().show();
        } else if (title.equals("bhajan")) {
            ((HomeActivityy) context).getSupportActionBar().show();
        }
        ((HomeActivityy) context).handleToolbar();
    }

    private void getBundleData() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey("type")) {
                title = bundle.getString("type");
                listType = bundle.getString("HOME_LIST_TYPE");
            }
        }
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.search_view);
        rl_nested = view.findViewById(R.id.rl_nested);
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        card_view = view.findViewById(R.id.card_view);
        layoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);
        videos = new Videos[]{};
        intro_images_layout = view.findViewById(R.id.intro_images_layout);
        intro_images_layout.setVisibility(View.GONE);
        indicatorLayout = view.findViewById(R.id.image_slider_dots_home_videos);
        imageSwitcherViewPager = view.findViewById(R.id.image_switcher_view_pager_videos);
        imageSwitcherViewPager.setPagingEnabled(true);
        update = null;
        timer = null;
        bannersList = new ArrayList<>();
        viewList = new ArrayList<>();
        progressBar=view.findViewById(R.id.progressBar);
        progress_layout=view.findViewById(R.id.progress_layout);

        //    toolbar = view.findViewById(R.id.toobar_view_all_videos);
       // swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        noDataFound = view.findViewById(R.id.no_data_found_view_all);

//        searchView = toolbar.findViewById(R.id.search_view);
        // closeIV = toolbar.findViewById(R.id.close_right);
        //backIV = toolbar.findViewById(R.id.back);
        bannerImg = view.findViewById(R.id.banner_img);
        guruTxt = view.findViewById(R.id.guru_txt);
        guruTxtHeader = view.findViewById(R.id.guru_txt_header);
        guruTxt.setVisibility(View.GONE);
        guruTxtHeader.setVisibility(View.GONE);
//        bannerImg.setImageResource(R.mipmap.guru_banner);
//        backIV.setVisibility(View.GONE);

        //searchView.setVisibility(View.GONE);
        //   toolbar.setVisibility(View.GONE);
        card_view.setVisibility(View.GONE);
        recyclerView.setNestedScrollingEnabled(true);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // toolbar.setVisibility(View.GONE);


        if (!TextUtils.isEmpty(listType) && listType != null) {
            //swipeRefreshLayout.setVisibility(View.GONE);
            //recyclerView.addOnScrollListener(null);

            ArrayList<GuruPojo> guruResponsesList = new ArrayList<>();

            for (int i = 0; i < ((HomeActivityy) context).homeGuruList.size(); i++) {
                GuruPojo guruResponse = ((HomeActivityy) context).homeGuruList.get(i);
                guruResponsesList.add(guruResponse);
            }


            gurusAdapter = new GurusAdapter(guruResponsesList, context);
            recyclerView.setAdapter(gurusAdapter);

//            recyclerView.setNestedScrollingEnabled(false);
            gurusAdapter.notifyDataSetChanged();

        } else {
            gurusAdapter = new GurusAdapter(((HomeActivityy) context).homeGuruList, context);
            recyclerView.setAdapter(gurusAdapter);
            /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (((HomeActivityy) context).homeGuruList.size() > 0)
                        ((HomeActivityy) context).homeGuruList.clear();


                    lastGuruId = "";
                    networkHit(false);
                    gurusAdapter.notifyDataSetChanged();
                }
            });*/


            if (((HomeActivityy) context).homeGuruList.isEmpty()) {
                networkHit(true);
            } else {
                gurusAdapter.notifyDataSetChanged();
            }
        }

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    loading = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = layoutManager.getChildCount();
                Log.d("Shantanu", "visibleItemCount " + visibleItemCount);
                totalItemCount = layoutManager.getItemCount();
                Log.d("Shantanu", "totalItemCount " + totalItemCount);
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                Log.d("Shantanu", "pastVisiblesItems " + pastVisiblesItems);

                if (loading && visibleItemCount+pastVisiblesItems==totalItemCount) {
                    loading = false;
                    progress_layout.setVisibility(View.VISIBLE);
                    lastGuruId = ((HomeActivityy) context).homeGuruList.get(totalItemCount - 1).getId();
                    Log.d("Shantanu", "shantanu");

                    networkHit(false);
                }
            }
        });


    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.GURUS_LIST)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("last_guru_id", lastGuruId);

        } else if (API_NAME.equals(API.SEARCH_GURU_LIST)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setBodyParameter("user_id", ((HomeActivityy) context).signupResponse.getId().toString())
                    .setBodyParameter("keyword", ((HomeActivityy) context).searchContent);
        } else if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                    .setMultipartParameter("search_content", "")
                    .setMultipartParameter("video_category", "")
                    .setMultipartParameter("last_video_id", "")
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
//        swipeRefreshLayout.setRefreshing(false);
        if (API_NAME.equals(API.GET_SEARCH_VIDEOS)) {
            if (result.optBoolean("status")) {
                VideoResponseNew videoResponseNew = new Gson().fromJson(result.optJSONObject("data").toString(), VideoResponseNew.class);

                if (bannersList.size() > 0) {
                    bannersList.clear();
                }
                banners = videoResponseNew.getBanners();
                for (int i = 0; i < banners.length; i++) {
                    Banners banner = banners[i];
                    bannersList.add(banner);
                }


                if (bannersList.size() > 0) {
                    if (indicatorLayout.getChildCount() > 0) {
                        //  indicatorLayout.removeAllViews();
                    }

                    imgString = new String[bannersList.size()];
                    for (int i = 0; i < bannersList.size(); i++) {
                        imgString[i] = bannersList.get(i).getImage();
                        indicatorLayout.addView(addSliderDotsView());
                    }
                    imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
                    imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

                    setViewPagerData();

                }
            }
        } else if (API.GURUS_LIST.equalsIgnoreCase(API_NAME)) {
            progress_layout.setVisibility(View.GONE);
            if (result.optBoolean("status")) {
                recyclerView.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);
                JSONArray jsonArray = result.optJSONArray("data");
                Log.d("Gurur", "" + jsonArray);
                if (jsonArray.length()>0){
                    //  ((HomeActivityy) context).homeGuruList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        GuruPojo guruResponse = new Gson().fromJson(jsonArray.opt(i).toString(), GuruPojo.class);
                        ((HomeActivityy) context).homeGuruList.add(guruResponse);
                    }
                    gurusAdapter.notifyDataSetChanged();
                    loading = true;
                }
                else{
                    loading=false;
                  //  Toast.makeText(context, "No more Data", Toast.LENGTH_SHORT).show();
                }


            }


        } else if (API.SEARCH_GURU_LIST.equalsIgnoreCase(API_NAME)) {
            if (result.optBoolean("status")) {
             /*   recyclerView.setVisibility(View.VISIBLE);
                noDataFound.setVisibility(View.GONE);*/
                JSONArray jsonArray = result.optJSONArray("data");
                if (jsonArray.length() > 0) {
                    gurulist = new GuruPojo[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GuruPojo guruResponse = new Gson().fromJson(jsonArray.opt(i).toString(), GuruPojo.class);
                        gurulist[i] = guruResponse;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gurulist", gurulist);
                    SearchGuruFrag frag = new SearchGuruFrag();
                    frag.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("GURU_SEARCH")
                            .replace(R.id.container_layout_home, frag)
                            .commit();
                } else {
                    searchContent = "";
                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                }
            }
        } else {
            if (((HomeActivityy) context).homeGuruList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }
            gurusAdapter.notifyDataSetChanged();
        }


//        } else if (API.SEARCH_GURU_LIST.equalsIgnoreCase(API_NAME)) {
//            if (result.optBoolean("status")) {
//                recyclerView.setVisibility(View.VISIBLE);
//                noDataFound.setVisibility(View.GONE);
//                JSONArray jsonArray = result.optJSONArray("data");
//
//                if (jsonArray.length() > 0) {
//                    ((HomeActivityy) context).homeGuruList.clear();
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        GuruPojo guruResponse = new Gson().fromJson(jsonArray.opt(i).toString(), GuruPojo.class);
//                        //((HomeActivityy) context).homeGuruList.add(guruResponse);
//                    }
//                    gurusAdapter.notifyDataSetChanged();
//                } else {
//                    searchContent = "";
//                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
//                }
//            } else {
//                searchContent = "";
//                ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
//            }
    }

//}

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
       // swipeRefreshLayout.setRefreshing(false);
       // ToastUtil.showDialogBox(context, jsonstring);//---------by sumit
    }


    public void networkHit(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {

            networkCall = new NetworkCall(GurusListFragment.this, context);
            networkCall.NetworkAPICall(API.GURUS_LIST, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    public void getSearchGuru() {
        //searchContent = query;
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(GurusListFragment.this, context);
            networkCall.NetworkAPICall(API.SEARCH_GURU_LIST, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    private LinearLayout addSliderDotsView() {
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, R.layout.layout_indicator, null);
        indicatorImg = linearLayout.findViewById(R.id.indicator_img);
        viewList.add(indicatorImg);
        return linearLayout;
    }

    private void setImgUnselected() {
        for (int i = 0; i < viewList.size(); i++) {
            viewList.get(i).setImageResource(R.drawable.intro_indicator_unselected);
        }
    }

    private void setViewPagerData() {

        imageSwitcherViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setImgUnselected();
                viewList.get(position).setImageResource(R.drawable.intro_indicator_selected);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler = new Handler();
        update = new Runnable() {
            public void run() {
                if (currentPage == imgString.length) {
                    currentPage = 0;
                }
                imageSwitcherViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }


    private void setBannerImg() {
        if (activityy.bannersList.size() > 0) {
            imgString = new String[activityy.bannersList.size()];
            for (int i = 0; i < activityy.bannersList.size(); i++) {
                imgString[i] = activityy.bannersList.get(i).getImage();

                indicatorLayout.addView(addSliderDotsView());
            }

            imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgString);
            imageSwitcherViewPager.setAdapter(imageSliderViewPagerAdapter);

            setViewPagerData();
        }
    }

    @Override
    public void textchanged(String Query) {
        gurusAdapter.filter(Query);
    }
}