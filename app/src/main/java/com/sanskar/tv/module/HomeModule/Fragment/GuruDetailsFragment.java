package com.sanskar.tv.module.HomeModule.Fragment;

import static android.view.View.GONE;
import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;
import static com.sanskar.tv.module.HomeModule.Adapter.AudioPlayAdapter.mp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.AudioPlayAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ParticularGuruVideoAdapter;
import com.sanskar.tv.module.HomeModule.Adapter.ThumbnailAdapterGuru;
import com.sanskar.tv.module.HomeModule.Adapter.WallpaperAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.HomeModule.Pojo.Thumbnails;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appsquadz on 2/19/2018.
 */

public class GuruDetailsFragment extends Fragment implements NetworkCall.MyNetworkCallBack, View.OnClickListener {

    LinearLayout like, share;

    RelativeLayout follow, rl_description;
    TextView viewsNumber, likesNumber, followText, description, title, guruName;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    GuruPojo guruDetail;
    MenuMasterList menuMasterList;
    int type;
    ImageView likeIcon, back, share_guru, imageview_guru;
    private Context context;
    private NetworkCall networkCall;
    RelativeLayout toolbar;
    private int position;
    LinearLayoutManager layoutManager;
    private List<Videos> guruVideosBeanList;
    private ParticularGuruVideoAdapter particularGuruVideoAdapter;
    private AudioPlayAdapter audioPlayAdapter;
    private WallpaperAdapter wallpaperAdapter;
    private TextView relatedVideosTV;
    private TextView relatedAudiosTV, related_about, related_image;
    private TextView relatedWallpaperTV;
    private RelativeLayout noDataFound, imageView_view_all;
    private ImageView bannerImg;

    ThumbnailAdapterGuru thumbnailAdapterGuru;
    Thumbnails thumbnails1;

    List<Thumbnails> thumbnails;
    int page_no = 1;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = false;
    Bhajan bhajan;
    Videos particularGuruVideosBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guru, container, false);
        initView(view);
        title.setVisibility(View.VISIBLE);
        getBundleData();
        return view;
    }

    private void getBundleData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey("data")) {
                guruDetail = (GuruPojo) bundle.getSerializable("data");
                type = bundle.getInt("type");
            }
            if (bundle.containsKey("data1")) {
                menuMasterList = (MenuMasterList) bundle.getSerializable("data1");
                type = bundle.getInt("type");
            }
            if (bundle.containsKey("position")) {
                position = bundle.getInt("position");
            }
        }
    }

    private void initView(View view) {

//      guruImagesSlider = view.findViewById(R.id.view_pager_guru);
        share = view.findViewById(R.id.share_guru);
        like = view.findViewById(R.id.like_guru);
        viewsNumber = view.findViewById(R.id.views_number_guru);
        likesNumber = view.findViewById(R.id.like_number_guru);
        recyclerView = view.findViewById(R.id.related_video_recycler_view_guru);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_guru);
        guruName = view.findViewById(R.id.name_guru);
        follow = view.findViewById(R.id.follow_guru);
        rl_description = view.findViewById(R.id.rl_description);
        followText = view.findViewById(R.id.follow_text_guru);
        likeIcon = view.findViewById(R.id.like_icon_guru);
        description = view.findViewById(R.id.description_guru);
        toolbar = view.findViewById(R.id.toolbar_guru);
        title = toolbar.findViewById(R.id.toolbar_title);
        back = toolbar.findViewById(R.id.back);

        relatedVideosTV = view.findViewById(R.id.related_videos);
        relatedAudiosTV = view.findViewById(R.id.related_audios);
        related_about = view.findViewById(R.id.related_about);
        related_image = view.findViewById(R.id.related_image);
        relatedWallpaperTV = view.findViewById(R.id.related_wallpaper);
        noDataFound = view.findViewById(R.id.no_data_found_view_all);
        bannerImg = view.findViewById(R.id.banner_img);
        imageView_view_all = view.findViewById(R.id.imageView_view_all);
        imageview_guru = view.findViewById(R.id.imageview_guru);

//      noDataFound.setBackgroundColor(getResources().getColor(R.color.layout_dark_bg_color));

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        /*guruVideosBeanList = new ArrayList<>();
        //guruAudioBeanList = new ArrayList<>();
        particularGuruVideoAdapter = new ParticularGuruVideoAdapter(guruVideosBeanList, context);
        audioPlayAdapter = new AudioPlayAdapter(((HomeActivityy) context).bhajanList, context);*/

        follow.setOnClickListener(this);
        like.setOnClickListener(this);
        ((HomeActivityy) context).tv_iv.setVisibility(View.VISIBLE);
        ((HomeActivityy) context).tv_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAppUrl();
            }
        });

        // share.setOnClickListener(this);
        back.setOnClickListener(this);
        relatedVideosTV.setOnClickListener(this);
        relatedAudiosTV.setOnClickListener(this);
        related_about.setOnClickListener(this);
        related_image.setOnClickListener(this);
        relatedWallpaperTV.setOnClickListener(this);
        follow.setTag(false);
        like.setTag(false);
        toolbar.setVisibility(View.GONE);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (guruDetail != null) {
            guruName.setText(guruDetail.getName());

            displayGuruData();
        }
        if (menuMasterList != null) {
            guruName.setText(menuMasterList.getName());
            displayGuruData1();
        }

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
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

                if (loading && visibleItemCount + pastVisiblesItems == totalItemCount) {
                    loading = false;
                    page_no++;

                    if (Constants.RELATED_GURU.equalsIgnoreCase("VIDEO")) {
                        getRelatedVideos();
                    }
                   /* if (Constants.RELATED_GURU.equalsIgnoreCase("AUDIO")){
                       // getRelatedAudios();
                    }*/
                    if (Constants.RELATED_GURU.equalsIgnoreCase("THUMBNAIL")) {
                        getRElatedTHumbnails();
                    }


                }
            }
        });

        guruVideosBeanList = new ArrayList<>();
        if (guruVideosBeanList.size() > 0) {
            guruVideosBeanList.clear();
        }

        if (((HomeActivityy) context).bhajanList.size() > 0) {
            ((HomeActivityy) context).bhajanList.clear();
        }
        //guruAudioBeanList = new ArrayList<>();

        /*((HomeActivityy) context).bhajanList.add(bhajan);
        guruVideosBeanList.add(particularGuruVideosBean);*/
        //  getRElatedTHumbnails();

        particularGuruVideoAdapter = new ParticularGuruVideoAdapter(guruVideosBeanList, context);
        audioPlayAdapter = new AudioPlayAdapter(((HomeActivityy) context).bhajanList, context);

        onClick(relatedVideosTV);
    }

    private void getRelatedVideos() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(GuruDetailsFragment.this, context);
            networkCall.NetworkAPICall(API.GURU_RELATED_VIDEOS, false);

        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    private void getRelatedAudios() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(GuruDetailsFragment.this, context);
            networkCall.NetworkAPICall(API.GURU_RELATED_AUDIOS, false);

        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void getRElatedTHumbnails() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(GuruDetailsFragment.this, context);
            networkCall.NetworkAPICall(API.GURU_RELATED_THUMBNAILS, false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    private void displayGuruData1() {
        Ion.with(context).load(menuMasterList.getBannerImage()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    bannerImg.setImageBitmap(result);
                }
            }
        });

        //binding.setGuru(guruDetail);

        if (menuMasterList.getFollowersCount().equals("0")) {
            viewsNumber.setText("no follower");
        } else if (menuMasterList.getFollowersCount().equals("1")) {
            viewsNumber.setText(menuMasterList.getFollowersCount() + " follower");
        } else {
            if (menuMasterList.getIs_follow().equals("1")) {
                viewsNumber.setText(String.valueOf(Integer.parseInt(menuMasterList.getFollowersCount()) - 1) + " followers");
            } else {

                viewsNumber.setText(String.valueOf(Integer.parseInt(menuMasterList.getFollowersCount()) + 1) + " followers");
            }
        }


        if (menuMasterList.getLikesCount().equals("0")) {
            likesNumber.setText("0 like");
        } else if (menuMasterList.getLikesCount().equals("1")) {
            likesNumber.setText(menuMasterList.getLikesCount() + " like");
        } else {
            likesNumber.setText(menuMasterList.getLikesCount() + " likes");
        }
        if (menuMasterList.getIs_follow().equals("1")) {
            followText.setText(getResources().getString(R.string.unfollow));
            follow.setTag(true);
        }
        if (menuMasterList.getIs_like().equals("1")) {
            likeIcon.setImageResource(R.mipmap.like_active);
            like.setTag(true);
        }
    }

    private void displayGuruData() {
        //String[] imgs = new String[]{guruDetail.getProfile_image(),guruDetail.getProfile_image(),guruDetail.getProfile_image()};
        //String[] imgs =
        //new String[]{ guruDetail.getBanner_image()};
        //imageSliderViewPagerAdapter = new Image_Slider_View_Pager_Adapter(context, imgs);
        //guruImagesSlider.setAdapter(imageSliderViewPagerAdapter);
        Ion.with(context).load(guruDetail.getBanner_image()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                if (result != null) {
                    bannerImg.setImageBitmap(result);
                }
            }
        });

        //binding.setGuru(guruDetail);

        /*String desc = Html.fromHtml(guruDetail.getDescription()).toString();
        description.setText(desc);
        if (desc.length() > 900) {
            //ResizableTV.doResizeTextView(description, 4, "View More", true);
            makeTextViewResizable(description, 4, "...Read More", true);
        }*/

        if (guruDetail.getFollowers_count().equals("0")) {
            viewsNumber.setText("no follower");
        } else if (guruDetail.getFollowers_count().equals("1")) {
            viewsNumber.setText(guruDetail.getFollowers_count() + " follower");
        } else {
            viewsNumber.setText(guruDetail.getFollowers_count() + " followers");
        }

        if (guruDetail.getFollowers_count().equals("0")) {
            viewsNumber.setText("no follower");
        } else if (guruDetail.getFollowers_count().equals("1")) {
            viewsNumber.setText(guruDetail.getFollowers_count() + " follower");
        } else {
            if (guruDetail.getIs_follow().equals("1")) {
                viewsNumber.setText(String.valueOf(Integer.parseInt(guruDetail.getFollowers_count()) - 1) + " followers");
            } else {

                viewsNumber.setText(String.valueOf(Integer.parseInt(guruDetail.getFollowers_count()) + 1) + " followers");
            }
        }


        if (guruDetail.getLikes_count().equals("0")) {
            likesNumber.setText("0 like");
        } else if (guruDetail.getLikes_count().equals("1")) {
            likesNumber.setText(guruDetail.getLikes_count() + " like");
        } else {
            likesNumber.setText(guruDetail.getLikes_count() + " likes");
        }
        if (guruDetail.getIs_follow().equals("1")) {
            followText.setText(getResources().getString(R.string.unfollow));
            follow.setTag(true);
        }
        if (guruDetail.getIs_like().equals("1")) {
            likeIcon.setImageResource(R.mipmap.like_active);
            like.setTag(true);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GURUS_FOLLOW) || apitype.equals(API.GURUS_UNFOLLOW) || apitype.equals(API.GURUS_LIKE) || apitype.equals(API.GURUS_UNLIKE)) {
            if (guruDetail != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("guru_id", guruDetail.getId());
            }
            if (menuMasterList != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("guru_id", menuMasterList.getId());
            }
        } else if (apitype.equals(API.GURU_RELATED_VIDEOS)) {

            if (guruDetail != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)
                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", guruDetail.getId())
                        .setMultipartParameter("guru_id", guruDetail.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(page_no))
                        .setMultipartParameter("current_version", ""+ Utils.getVersionCode(getContext()))
                        .setMultipartParameter("device_type", "1");;;
            }
            if (menuMasterList != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", menuMasterList.getId())
                        .setMultipartParameter("guru_id", menuMasterList.getId())
                        .setMultipartParameter("limit", String.valueOf(10))
                        .setMultipartParameter("page_no", String.valueOf(page_no))
                        .setMultipartParameter("current_version", ""+ Utils.getVersionCode(getContext()))
                        .setMultipartParameter("device_type", "1");;;
            }

        } else if (apitype.equals(API.GURU_RELATED_AUDIOS)) {
            if (guruDetail != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        /*.setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", guruDetail.getId())*/
                        .setMultipartParameter("guru_id", guruDetail.getId())
                        .setMultipartParameter("limit", String.valueOf(40))
                        .setMultipartParameter("page_no", String.valueOf(page_no));
            }
            if (menuMasterList != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        /*.setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", menuMasterList.getId())*/
                        .setMultipartParameter("guru_id", menuMasterList.getId())
                        .setMultipartParameter("limit", String.valueOf(40))
                        .setMultipartParameter("page_no", String.valueOf(page_no));
            }
        } else if (apitype.equals(API.GURU_RELATED_THUMBNAILS)) {
            if (guruDetail != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", guruDetail.getId())
                        .setMultipartParameter("limit", String.valueOf(40))
                        .setMultipartParameter("page_no", String.valueOf(page_no))
                        .setMultipartParameter("guru_id", guruDetail.getId());
            }
            if (menuMasterList != null) {
                ion = (Builders.Any.B) Ion.with(context).load(apitype)

                        .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
                        .setMultipartParameter("type", Constants.VIDEO_TYPE)
                        .setMultipartParameter("media_id", menuMasterList.getId())
                        .setMultipartParameter("limit", String.valueOf(40))
                        .setMultipartParameter("page_no", String.valueOf(page_no))
                        .setMultipartParameter("guru_id", menuMasterList.getId());
            }
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (jsonstring.optBoolean("status")) {
            if (apitype.equals(API.GURUS_FOLLOW)) {
                FollowUnFollow(true);
            } else if (apitype.equals(API.GURUS_UNFOLLOW)) {
                FollowUnFollow(false);
            } else if (apitype.equals(API.GURUS_LIKE)) {
                LikeDislike(true);
            } else if (apitype.equals(API.GURUS_UNLIKE)) {
                LikeDislike(false);
            } else if (apitype.equals(API.GURU_RELATED_VIDEOS)) {
               /* if (guruVideosBeanList.size() > 0) {
                    guruVideosBeanList.clear();
                }*/
                String dataString = jsonstring.optString("data");
                JSONArray dataArray = new JSONArray(dataString);
                if (dataArray.length() > 0) {
                    if (noDataFound.getVisibility() == View.VISIBLE) {
                        noDataFound.setVisibility(GONE);
                    }
                    for (int i = 0; i < dataArray.length(); i++) {
                        particularGuruVideosBean = new Gson().fromJson(dataArray.opt(i).toString(),
                                Videos.class);
                        guruVideosBeanList.add(particularGuruVideosBean);
                    }
                    particularGuruVideoAdapter.notifyDataSetChanged();
                    loading = true;
                } else {
                    loading = false;
                    // particularGuruVideoAdapter.notifyDataSetChanged();
                   /* noDataFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(GONE);*/
                }
            } else if (apitype.equals(API.GURU_RELATED_AUDIOS)) {
                if (((HomeActivityy) context).bhajanList.size() > 0) {
                    ((HomeActivityy) context).bhajanList.clear();
                }
                String dataString = jsonstring.optString("data");
                JSONArray dataArray = new JSONArray(dataString);
                if (dataArray.length() > 0) {
                    if (noDataFound.getVisibility() == View.VISIBLE) {
                        noDataFound.setVisibility(GONE);
                    }
                    for (int i = 0; i < dataArray.length(); i++) {
                        bhajan = new Gson().fromJson(dataArray.opt(i).toString(),
                                Bhajan.class);
                        ((HomeActivityy) context).bhajanList.add(bhajan);
                    }
                    audioPlayAdapter.notifyDataSetChanged();
                    loading = true;
                } else {
                    loading = false;
                    //  audioPlayAdapter.notifyDataSetChanged();
                    /*recyclerView.setVisibility(GONE);
                    noDataFound.setVisibility(View.VISIBLE);*/
                }
            } else if (apitype.equals(API.GURU_RELATED_THUMBNAILS)) {
                String dataString = jsonstring.optString("data");
                JSONArray dataArray = new JSONArray(dataString);
                thumbnails = new ArrayList<>();
                if (dataArray.length() > 0) {
                    if (noDataFound.getVisibility() == View.VISIBLE) {
                        noDataFound.setVisibility(GONE);
                    }
                    for (int i = 0; i < dataArray.length(); i++) {
                        thumbnails1 = new Gson().fromJson(dataArray.opt(i).toString(), Thumbnails.class);
                        thumbnails.add(thumbnails1);
                    }
                    thumbnailAdapterGuru = new ThumbnailAdapterGuru(context, thumbnails);
                    recyclerView.setAdapter(thumbnailAdapterGuru);
                    thumbnailAdapterGuru.notifyDataSetChanged();
                    loading = true;
                } else {
                    //loading=false;
                    // thumbnailAdapterGuru.notifyDataSetChanged();
                    // audioPlayAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(GONE);
                    noDataFound.setVisibility(View.VISIBLE);
                }
            }

        } else {
            if (apitype.equalsIgnoreCase(API.GURU_RELATED_VIDEOS)) {

            } else {
                recyclerView.setVisibility(GONE);
                noDataFound.setVisibility(View.VISIBLE);
            }

        }
    }

    private void LikeDislike(boolean b) {
        like.setTag(b);
        int likenum = 0;
        if (likesNumber.getText().toString().equalsIgnoreCase("0 like")) {
            likenum = 0;
        } else {
            likenum = Integer.parseInt(likesNumber.getText().toString().split(" ")[0]);
        }
        if (b) {
            likeIcon.setImageResource(R.mipmap.like_active);
            if (likenum == 0) {
                likesNumber.setText(likenum + 1 + " like");
            } else {
                likesNumber.setText(likenum + 1 + " likes");
            }
            if (guruDetail != null) {
                guruDetail.setLikes_count(String.valueOf(likenum + 1));
                guruDetail.setIs_like("1");
            }
            if (menuMasterList != null) {
                menuMasterList.setLikesCount(String.valueOf(likenum + 1));
                //menuMasterList.setIs_like("1");
            }


        } else {
            if (likenum == 1) {
                likesNumber.setText("no like");
            } else if (likenum == 2) {
                likesNumber.setText(likenum - 1 + " like");
            } else {
                likesNumber.setText(likenum - 1 + " likes");
            }
            likeIcon.setImageResource(R.mipmap.like_gray);
            if (guruDetail != null) {
                guruDetail.setLikes_count(String.valueOf(likenum - 1));
                guruDetail.setIs_like("0");
            }
            if (menuMasterList != null) {
                menuMasterList.setLikesCount(String.valueOf(likenum - 1));
                // guruDetail.setIs_like("0");
            }

        }
        //((HomeActivityy)context).guruList.set(position,guruDetail);
    }

    private void FollowUnFollow(boolean b) {
        follow.setTag(b);
        if (guruDetail != null) {
            if (b) {

                followText.setText(getResources().getString(R.string.unfollow));
                if (viewsNumber.getText().toString().equalsIgnoreCase("no follower")) {
                    viewsNumber.setText("1 follower");

                    guruDetail.setFollowers_count("1");
                } else {
                    viewsNumber.setText(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1 + " followers");
                    guruDetail.setFollowers_count(String.valueOf(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1));
                }
                guruDetail.setIs_follow("1");
//            followText.setTextColor(getResources().getColor(android.R.color.darker_gray));
//            follow.setBackground(getResources().getDrawable(R.drawable.unfollow_btn_bg));
            } else {
                followText.setText(getResources().getString(R.string.follow));
                if (viewsNumber.getText().toString().equalsIgnoreCase("1 follower")) {
                    viewsNumber.setText("no follower");
                    guruDetail.setFollowers_count("0");
                } else {
                    viewsNumber.setText(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) - 1 + " followers");
                    guruDetail.setFollowers_count(String.valueOf(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1));
                }
                guruDetail.setIs_follow("0");
            }
        }
        if (menuMasterList != null) {
            if (b) {

                followText.setText(getResources().getString(R.string.unfollow));
                if (viewsNumber.getText().toString().equalsIgnoreCase("no follower")) {
                    viewsNumber.setText("1 follower");

                    menuMasterList.setFollowersCount("1");
                } else {
                    viewsNumber.setText(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1 + " followers");
                    menuMasterList.setFollowersCount(String.valueOf(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1));
                }
                // menuMasterList.setIs_follow("1");
//            followText.setTextColor(getResources().getColor(android.R.color.darker_gray));
//            follow.setBackground(getResources().getDrawable(R.drawable.unfollow_btn_bg));
            } else {
                followText.setText(getResources().getString(R.string.follow));
                if (viewsNumber.getText().toString().equalsIgnoreCase("1 follower")) {
                    viewsNumber.setText("no follower");
                    menuMasterList.setFollowersCount("0");
                } else {
                    viewsNumber.setText(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) - 1 + " followers");
                    menuMasterList.setFollowersCount(String.valueOf(Integer.parseInt(viewsNumber.getText().toString().split(" ")[0]) + 1));
                }
                // menuMasterList.setIs_follow("0");
            }
        }

//            followText.setTextColor(getResources().getColor(android.R.color.white));
//            follow.setBackground(getResources().getDrawable(R.drawable.follow_btn_bg));

        //((HomeActivityy)context).guruList.set(position,guruDetail);
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                ((HomeActivityy) context).onBackPressed();
                break;

            case R.id.share:
                //  shareTextUrl();
                shareAppUrl();
                break;
            case R.id.like_guru:
                if (isConnectingToInternet(context)) {
                    if (!Boolean.parseBoolean(like.getTag().toString())) {
                        networkCall = new NetworkCall(GuruDetailsFragment.this, context);
                        networkCall.NetworkAPICall(API.GURUS_LIKE, false);
                    } else {
                        networkCall = new NetworkCall(GuruDetailsFragment.this, context);
                        networkCall.NetworkAPICall(API.GURUS_UNLIKE, false);
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;
            case R.id.follow_guru:
                if (isConnectingToInternet(context)) {
                    if (!Boolean.parseBoolean(follow.getTag().toString())) {
                        networkCall = new NetworkCall(GuruDetailsFragment.this, context);
                        networkCall.NetworkAPICall(API.GURUS_FOLLOW, true);
                    } else {
                        networkCall = new NetworkCall(GuruDetailsFragment.this, context);
                        networkCall.NetworkAPICall(API.GURUS_UNFOLLOW, true);
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
                break;
            case R.id.related_videos:
                noDataFound.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                // imageView_view_all.setVisibility(GONE);
                rl_description.setVisibility(GONE);
                recyclerView.setAdapter(particularGuruVideoAdapter);
                changeTabColour(relatedVideosTV);
                Constants.RELATED_GURU = "VIDEO";
                getRelatedVideos();
                break;
            case R.id.related_audios:
                noDataFound.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
                // imageView_view_all.setVisibility(GONE);
                rl_description.setVisibility(GONE);
                recyclerView.setAdapter(audioPlayAdapter);
                changeTabColour(relatedAudiosTV);
                Constants.RELATED_GURU = "Audio";
                getRelatedAudios();

                break;
            case R.id.related_about:
                noDataFound.setVisibility(GONE);
                recyclerView.setVisibility(GONE);
                rl_description.setVisibility(View.VISIBLE);
                // imageView_view_all.setVisibility(GONE);
                changeTabColour(related_about);
                String desc = "";
                if (menuMasterList != null) {
                    desc = Html.fromHtml(menuMasterList.getDescription()).toString();
                }
                if (guruDetail != null) {
                    desc = Html.fromHtml(guruDetail.getDescription()).toString();
                }

                if (desc == null || desc.equalsIgnoreCase("null")) {
                    noDataFound.setVisibility(GONE);
                } else {
                    description.setText(desc);
                    if (desc.length() > 900) {
                        //ResizableTV.doResizeTextView(description, 4, "View More", true);
                        makeTextViewResizable(description, 15, "...Read More", true);
                    }
                }
                break;

            case R.id.related_image:


                changeTabColour(related_image);
                noDataFound.setVisibility(GONE);
                Constants.RELATED_GURU = "THUMBNAIL";
                //imageView_view_all.setVisibility(View.VISIBLE);
                rl_description.setVisibility(GONE);

                getRElatedTHumbnails();

                break;

            case R.id.related_wallpaper:
                recyclerView.setVisibility(View.VISIBLE);
                rl_description.setVisibility(GONE);
                recyclerView.setAdapter(wallpaperAdapter);
                changeTabColour(relatedWallpaperTV);
                getRelatedAudios();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) context).handleToolbar();
    }

    private void changeTabColour(View view) {
        switch (view.getId()) {
            case R.id.related_videos:
                relatedVideosTV.setBackground(getResources().getDrawable(R.drawable.blue_curved_two_side));
                relatedAudiosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedWallpaperTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_image.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_about.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));


                relatedVideosTV.setTextColor(getResources().getColor(R.color.textWhiteColor));
                relatedAudiosTV.setTextColor(getResources().getColor(R.color.textsColor));
                relatedWallpaperTV.setTextColor(getResources().getColor(R.color.textsColor));
                related_image.setTextColor(getResources().getColor(R.color.textsColor));
                related_about.setTextColor(getResources().getColor(R.color.textsColor));

                break;
            case R.id.related_audios:
                relatedAudiosTV.setBackground(getResources().getDrawable(R.drawable.blue_curved_two_side));
                relatedVideosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedWallpaperTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_image.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_about.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));


                relatedAudiosTV.setTextColor(getResources().getColor(R.color.textWhiteColor));
                relatedVideosTV.setTextColor(getResources().getColor(R.color.textsColor));
                relatedWallpaperTV.setTextColor(getResources().getColor(R.color.textsColor));
                related_image.setTextColor(getResources().getColor(R.color.textsColor));
                related_about.setTextColor(getResources().getColor(R.color.textsColor));

                break;

            case R.id.related_about:
                related_about.setBackground(getResources().getDrawable(R.drawable.blue_curved_two_side));
                relatedVideosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedWallpaperTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_image.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedAudiosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));


                related_about.setTextColor(getResources().getColor(R.color.textWhiteColor));
                relatedVideosTV.setTextColor(getResources().getColor(R.color.textsColor));
                relatedWallpaperTV.setTextColor(getResources().getColor(R.color.textsColor));
                related_image.setTextColor(getResources().getColor(R.color.textsColor));
                relatedAudiosTV.setTextColor(getResources().getColor(R.color.textsColor));

                break;

            case R.id.related_image:
                related_image.setBackground(getResources().getDrawable(R.drawable.blue_curved_two_side));
                relatedVideosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedWallpaperTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_about.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedAudiosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));


                related_image.setTextColor(getResources().getColor(R.color.textWhiteColor));
                relatedVideosTV.setTextColor(getResources().getColor(R.color.textsColor));
                relatedWallpaperTV.setTextColor(getResources().getColor(R.color.textsColor));
                related_about.setTextColor(getResources().getColor(R.color.textsColor));
                relatedAudiosTV.setTextColor(getResources().getColor(R.color.textsColor));

                break;
            case R.id.related_wallpaper:
                relatedWallpaperTV.setBackground(getResources().getDrawable(R.drawable.curved_one_side_audio));
                relatedVideosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                relatedAudiosTV.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_image.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));
                related_about.setBackground(getResources().getDrawable(R.drawable.white_curved_two_sides));

                relatedWallpaperTV.setTextColor(getResources().getColor(R.color.textWhiteColor));
                relatedVideosTV.setTextColor(getResources().getColor(R.color.textsColor));
                relatedAudiosTV.setTextColor(getResources().getColor(R.color.textsColor));
                related_image.setTextColor(getResources().getColor(R.color.textsColor));
                related_about.setTextColor(getResources().getColor(R.color.textsColor));
                break;
        }
    }

/*
    private void shareTextUrl() {
        Uri uri = Uri.parse(guruDetail.getProfile_image());
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, guruDetail.getName());
        share.putExtra(Intent.EXTRA_SUBJECT, guruDetail.getDescription());
        share.putExtra(Intent.EXTRA_TEXT, uri);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share data!"));
    }
*/

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        //share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_SUBJECT, guruDetail.getName());
        share.putExtra(Intent.EXTRA_TEXT, guruDetail.getDescription());
        share.putExtra(Intent.EXTRA_TEXT, guruDetail.getBanner_image());
        share.setType("image/*");

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void shareAppUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);

        share.setType("text/plain");
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_SUBJECT, guruDetail.getName());
        share.putExtra(Intent.EXTRA_STREAM, Utils.getImageUri(context, Utils.getBitmapFromView(bannerImg)));
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        /*  *//* String post = "";
        if (questionsPojo == null) {
            post = UserPojo.getInstance().getFirst_name() + " " + UserPojo.getInstance().getLast_name()  + " wants to tag you on WittyLikes \n \n Download Wittylikes to tag Friends Anonymously on Funny Witty One-liners "
                    + Utils.shareUrl();
        } else {
            post = UserPojo.getInstance().getFirst_name() + " " + UserPojo.getInstance().getLast_name()  + " wants to tag you on " + questionsPojo.getQuestion() + " on WittyLikes. \n \n Download Wittylikes to tag Friends Anonymously on Funny Witty One-liners "
                    + Utils.shareUrl();
        }*//*
        share.putExtra(Intent.EXTRA_TEXT, post);*/
        startActivity(Intent.createChooser(share, "Share tag!"));
    }


    public static void makeTextViewResizable(final TextView tv,
                                             final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(
            final String strSpanned, final TextView tv, final int maxLine,
            final String spanableText, final boolean viewMore) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (strSpanned.contains(spanableText)) {
            ssb.setSpan(
                    new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            if (viewMore) {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, -4, "Read Less",
                                        false);
                                //tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, 4, "...Read More",
                                        true);
                                //tv.setTextColor(Color.BLACK);
                            }

                        }
                    }, strSpanned.indexOf(spanableText),
                    strSpanned.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (Constants.ISGURUPLAY == true) {
            if (mp.isPlaying()) {
                ((HomeActivityy) context).playerlayout3.setVisibility(View.GONE);
                mp.pause();
            } else {
                ((HomeActivityy) context).playerlayout3.setVisibility(View.GONE);
                mp.pause();
            }
        } else {
        }

    }
}
