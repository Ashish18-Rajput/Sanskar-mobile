package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.ViewAllAdapter2;
import com.sanskar.tv.module.HomeModule.Pojo.Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VideoListByMenuMaster extends Fragment implements NetworkCall.MyNetworkCallBack {

    RecyclerView recyclerView;
    Videos[] videos;
    Videos video;
    ViewAllAdapter2 viewAllAdapter;
    public  String Video_id = "";
    String Category = "";
    String Category_name = "";
    Context context;
    HomeActivityy activityy;
    boolean loading = false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    List<Videos> videosList = new ArrayList<>();
    NetworkCall networkCall;
    int page_no = 1;
    TextView no_data_found_text,title_textView;

    RelativeLayout relativeLayout;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activityy = (HomeActivityy) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_list_by_menu_master, container, false);
        getBundleData();
        initViews(view);
        fetchData(true);

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


                if (loading && visibleItemCount+pastVisiblesItems==totalItemCount) {
                    loading = false;
                    page_no++;
                    //progress_layout.setVisibility(View.VISIBLE);
                    //lastGuruId = ((HomeActivityy) context).homeGuruList.get(totalItemCount - 1).getId();
                    relativeLayout.setVisibility(View.VISIBLE);
                    Log.d("Shantanu", "shantanu");

                    fetchData(false);
                }
            }
        });


        viewAllAdapter = new ViewAllAdapter2(videosList, context);
        recyclerView.setAdapter(viewAllAdapter);
       // recyclerView.scheduleLayoutAnimation();

        return view;
    }

    private void fetchData(boolean load) {

        networkCall = new NetworkCall(this, context);
        networkCall.NetworkAPICall(API.GET_VIDEO_LIST_BY_MENUMASTER, load);

    }

    private void getBundleData() {

        Bundle bundle = getArguments();
        if (bundle.containsKey("Id")) {
            Video_id = bundle.getString("Id");

            Networkconstants.Video_id=Video_id;
        }
        if (bundle.containsKey("Category")) {
            Category = bundle.getString("Category");
        }
        if (bundle.containsKey("Category_name")) {
            Category_name = bundle.getString("Category_name");
        }

    }

    private void initViews(View view) {

        recyclerView = view.findViewById(R.id.recyclerView);
        no_data_found_text = view.findViewById(R.id.no_data_found_text);
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        title_textView=view.findViewById(R.id.title_textView);

        title_textView.setText(Category_name);
        relativeLayout=view.findViewById(R.id.progress_layout);
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {

        Builders.Any.B ion = null;
        if (apitype.equals(API.GET_VIDEO_LIST_BY_MENUMASTER)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(apitype)
                    
                    .setMultipartParameter("user_id", activityy.signupResponse.getId())
                    .setMultipartParameter("menu_master_id", Video_id)
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("category", Category)
                    .setMultipartParameter("page_no", String.valueOf(page_no))
                    .setMultipartParameter("current_version", ""+ Utils.getVersionCode(getContext()))
                    .setMultipartParameter("device_type", "1");;

        }


        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {

        relativeLayout.setVisibility(View.GONE);
        if (jsonstring.optBoolean("status")) {
            recyclerView.setVisibility(View.VISIBLE);
            no_data_found_text.setVisibility(View.GONE);

            JSONArray jsonArray = jsonstring.optJSONArray("data");
            if (jsonArray.length() > 0) {

                // videos=new Videos[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    video = new Gson().fromJson(jsonArray.opt(i).toString(), Videos.class);
                    videosList.add(video);
                }


                viewAllAdapter.notifyDataSetChanged();
                loading=true;
            }else{
                loading=false;
               /* recyclerView.setVisibility(View.GONE);
                no_data_found_text.setVisibility(View.VISIBLE);*/
            }

        } else {
            recyclerView.setVisibility(View.GONE);
            no_data_found_text.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}