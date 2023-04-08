package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.PremiumSeriesAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.PremiumThumbnailFragAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.PremiumCategory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Fragment.PremiumThumbnailFragment.simpleExoPlayer;


public class PremiumSeriesFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    MenuMasterList promo;
    List<MenuMasterList> promoList = new ArrayList<>();

    Context context;

    RecyclerView recyclerView, promo_imageView_recycler;
    LinearLayoutManager linearLayoutManager, linearLayoutManager1;

    HomeActivityy homeActivityy;
    NetworkCall networkCall;
    ViewPager promo_imageView;

    PremiumCategory premiumCategory;

    String[] thumbnails1;
    MenuMasterList[] thumbNails;

    SwipeRefreshLayout swipeRefreshLayout;
    PremiumThumbnailFragAdapter premiumThumbnailAdapter;

    LinearLayout dot_layout;
    private ImageView[] dots;
    int page = 1;
    boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean fromGuest = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        homeActivityy = (HomeActivityy) getActivity();

        homeActivityy.toolbar.setVisibility(View.GONE);

        if (homeActivityy.getIntent().getExtras() != null) {
            if (homeActivityy.getIntent().getStringExtra(Constants.FROM_GUEST).equalsIgnoreCase("1")) {
                fromGuest = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premium_series, container, false);
        initViews(view);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (SharedPreference.getInstance(context).getPremiumCategory() != null) {
            InitLandingPageAdapter();
        } else {
            fetchData(true);
        }

        swipeRefreshLayout.setOnRefreshListener(() -> fetchData(true));

        return view;
    }

    private void sliderImages() {

        createDots(0);

        /*promo_imageView_recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("shantanu", String.valueOf(linearLayoutManager1.findFirstVisibleItemPosition()));
                createDots(linearLayoutManager1.findFirstVisibleItemPosition());

                //premiumThumbnailAdapterNew.pausePlayBack(PremiumThumbnailAdapterNew.ViewHolder,linearLayoutManager1.findFirstVisibleItemPosition());


            }
        });*/

        promo_imageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void fetchData(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_CATEGORY_LANDING_PAGE, b);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView_Promo);
        linearLayoutManager = new LinearLayoutManager(context);
        promo_imageView = view.findViewById(R.id.promo_imageView);
        dot_layout = view.findViewById(R.id.dotsLayout);
        promo_imageView_recycler = view.findViewById(R.id.promo_imageView_recycler);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_premium);
    }

    private void createDots(int currentPosition) {
        if (dot_layout != null) {
            dot_layout.removeAllViews();
        }
        dots = new ImageView[thumbNails.length];

        for (int i = 0; i < thumbNails.length; i++) {
            if (dots != null) {
                dots[i] = new ImageView(context);
                if (i == currentPosition) {
                    dots[i].setImageResource(

                            R.drawable.intro_indicator_selected);
                } else {
                    dots[i].setImageResource(R.drawable.intro_indicator_unselected);
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(15, 0, 15, 0);
                dot_layout.addView(dots[i], params);
            }
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {

        Builders.Any.B ion = null;

        if (apitype.equalsIgnoreCase(API.GET_CATEGORY_LANDING_PAGE)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(apitype)
                    .setMultipartParameter("user_id", homeActivityy.signupResponse.getId());
        }

        return ion;
}

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if (result.optBoolean("status")) {
            if (apitype.equalsIgnoreCase(API.GET_CATEGORY_LANDING_PAGE)) {

                premiumCategory = new Gson().fromJson(result.toString(), PremiumCategory.class);
                SharedPreference.getInstance(context).setPremiumCategory(premiumCategory);
                InitLandingPageAdapter();
            }
        } else {
            ToastUtil.showDialogBox(context, "Error!!");
        }
    }

    private void InitLandingPageAdapter() {

        PremiumCategory premiumCategory = SharedPreference.getInstance(context).getPremiumCategory();

        /*if (promoList.size() > 0) {
            promoList.clear();
        }*/

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new PremiumSeriesAdapter(context, premiumCategory.getData()));

        thumbNails = new MenuMasterList[premiumCategory.getSeason_promo_videos().size()];

        for (int i = 0; i < premiumCategory.getSeason_promo_videos().size(); i++) {
            thumbNails[i] = premiumCategory.getSeason_promo_videos().get(i);
        }

        thumbnails1 = new String[premiumCategory.getSeasonThumbnails().size()];

        for (int i = 0; i < premiumCategory.getSeasonThumbnails().size(); i++) {
            thumbnails1[i] = premiumCategory.getSeasonThumbnails().get(i);
        }


        premiumThumbnailAdapter = new PremiumThumbnailFragAdapter(getFragmentManager(), thumbNails, thumbnails1);
        promo_imageView.setOffscreenPageLimit(0);
        promo_imageView.setAdapter(premiumThumbnailAdapter);


        sliderImages();

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (simpleExoPlayer != null)
            simpleExoPlayer.setPlayWhenReady(false);

        fetchData(false);
    }
}