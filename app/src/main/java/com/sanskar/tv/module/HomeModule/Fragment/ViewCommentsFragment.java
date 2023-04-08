package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.jwPlayer.LiveStreamJWActivity;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.CommentsAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Comments;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by appsquadz on 4/10/2018.
 */

public class ViewCommentsFragment extends Fragment implements View.OnClickListener, NetworkCall.MyNetworkCallBack {

    //    RelativeLayout toolbar;
//    TextView title;
//    ImageView back;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CommentsAdapter adapter;
    ArrayList<Comments> commentsList;
    private Context context;
    private LiveStreamJWActivity activity;
    private String lastCommentId = "";
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private TextView postComment;
    private NetworkCall networkCall;
    private ProgressBar commentProgress;
    private EditText comment;
    private CardView cardView;
    private CircleImageView profileImg;
    RelativeLayout toolbar;
    TextView title;
    ImageView back;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = ((LiveStreamJWActivity) context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_comment, null);
        initView(view);
//        toolbar.setVisibility(View.GONE);
        return view;
    }

    private void initView(View view) {
//        toolbar = view.findViewById(R.id.toobar_view_all_videos);
//        title = toolbar.findViewById(R.id.toolbar_title);
//        back = toolbar.findViewById(R.id.back);
        recyclerView = view.findViewById(R.id.view_all_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_view_all);
        postComment = view.findViewById(R.id.watch_video_comment_post_tv);
        commentProgress = view.findViewById(R.id.watch_video_comment_post_pb);
        comment = view.findViewById(R.id.watch_video_comment_et);
        toolbar = view.findViewById(R.id.toobar_view_all_videos);
        title     = toolbar.findViewById(R.id.toolbar_title);
        back      = toolbar.findViewById(R.id.back);
        cardView = view.findViewById(R.id.card_view);
        profileImg = view.findViewById(R.id.fragment_watch_comment_image);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        commentsList = new ArrayList<>();
        networkCall = new NetworkCall(ViewCommentsFragment.this, context);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LiveStreamJWActivity)context).onBackPressed();
            }
        });

        adapter = new CommentsAdapter(context, commentsList);
        recyclerView.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment.setVisibility(View.GONE);
                commentProgress.setVisibility(View.VISIBLE);
                Utils.closeKeyboard(activity);
                networkCall.NetworkAPICall(API.POST_COMMENT, false);
            }
        });
        cardView.setVisibility(View.VISIBLE);

        if (((LiveStreamJWActivity) context).signupResponse.getProfile_picture().toString().isEmpty()
                || ((LiveStreamJWActivity) context).signupResponse.getProfile_picture().toString() == null) {
            profileImg.setImageResource(R.mipmap.profile);
        } else {
            Ion.with(context).load(((LiveStreamJWActivity) context).signupResponse.getProfile_picture().toString())
                    .asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    profileImg.setImageBitmap(result);
                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setClickListeners(back);
        NetworkHit(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetworkHit(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading /*&& !(lastCommentId.equalsIgnoreCase(firstCommentId))*/) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");


                            //Do pagination.. i.e. fetch new data
                            lastCommentId = commentsList.get(commentsList.size() - 1).getId();
                            NetworkHit(false);

                        }
                    }

                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        title.setVisibility(View.VISIBLE);
//        title.setText(getString(R.string.all_comments));
        activity.handleToolbar();
    }

    private void NetworkHit(boolean progress) {
        NetworkCall networkCall = new NetworkCall(ViewCommentsFragment.this, context);
        networkCall.NetworkAPICall(API.GET_ALL_COMMENTS, progress);
    }

    void setClickListeners(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back:
                activity.onBackPressed();
                break;
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.POST_COMMENT)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter("user_id", activity.signupResponse.getId())
                    .setMultipartParameter("video_id", getArguments().getString("video_id"))
                    .setMultipartParameter("comment", comment.getText().toString());
        } else if (apitype.equals(API.GET_ALL_COMMENTS)) {
            ion = (Builders.Any.B) Ion.with(context).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",activity.signupResponse.getId())
                    .setMultipartParameter(Constants.VIDEO_ID, getArguments().getString("video_id"))
                    .setMultipartParameter("last_comment_id", lastCommentId);
        }
        return ion;
    }



    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
     /*   if (jsonstring.optBoolean("status")) {
            if (apitype.equals(API.POST_COMMENT)) {
                clearEditText(comment);
                postComment.setVisibility(View.VISIBLE);
                commentProgress.setVisibility(View.GONE);
                Videos videos = (Videos) getArguments().getSerializable("video");
                //videos.setComments(String.valueOf(Integer.parseInt(videos.getComments()) + 1));
                activity.setTotalComments();
                ToastUtil.showShortToast(context, jsonstring.optString("message"));
            } else if (apitype.equals(API.GET_ALL_COMMENTS)) {
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                commentsList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    Comments comment = new Gson().fromJson(jsonArray.get(i).toString(), Comments.class);
                    commentsList.add(comment);
//                    if (firstTimeHit) {
//                        firstCommentId = comment.getId();
//                        firstTimeHit = false;
//                    }
                }

                loading = true;
                adapter.notifyDataSetChanged();
            }
        } else {
            ToastUtil.showDialogBox(context, jsonstring.optString("message"));
        }*/
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtil.showDialogBox(context, jsonstring);
    }
}
