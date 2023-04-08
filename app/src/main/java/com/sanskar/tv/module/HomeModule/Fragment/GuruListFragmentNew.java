package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
import com.sanskar.tv.module.HomeModule.Adapter.GurusAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.guruPojo.GuruPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class GuruListFragmentNew extends Fragment implements NetworkCall.MyNetworkCallBack {
    String title;
    private String listType;
    RecyclerView recyclerView;
    RelativeLayout progress_layout;
    GurusAdapter gurusAdapter;
    private String lastGuruId = "";
    Context context;
    private int mPage = 1;
    NetworkCall networkCall;
    boolean loading=false;
    int pastVisiblesItems , visibleItemCount, totalItemCount ;
    LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_guru_list_new, container, false);
        if (((HomeActivityy) context).homeGuruList.size()>0){
            ((HomeActivityy) context).homeGuruList.clear();
        }
        getBundleData();
        initViews(view);

        lastGuruId = "";
        getVideoList(false);
        fetchData(true);



     /*   recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount = linearLayoutManager.getChildCount();
                Log.d("Shantanu", "visibleItemCount " + visibleItemCount);
                totalItemCount = linearLayoutManager.getItemCount();
                Log.d("Shantanu", "totalItemCount " + totalItemCount);
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d("Shantanu", "pastVisiblesItems " + pastVisiblesItems);

                if (loading && (visibleItemCount+pastVisiblesItems)==totalItemCount) {
                    loading = false;
                    progress_layout.setVisibility(View.VISIBLE);
                    lastGuruId = ((HomeActivityy) context).homeGuruList.get(totalItemCount - 1).getId();

                    fetchData(false);
                }
            }
        });
*/


        return view;
    }
    public void fetchData(boolean progress) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GURUS_LIST, progress);
        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void getVideoList(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_SEARCH_VIDEOS, b);

        } else {
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    public void getSearchGuru() {

        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.SEARCH_GURU_LIST, true);
        } else {

            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }
    private void initViews(View view) {
        recyclerView=view.findViewById(R.id.recyclerView);
        progress_layout=view.findViewById(R.id.progress_layout);
        linearLayoutManager=new GridLayoutManager(context,2);
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeActivityy) context).searchView.onActionViewCollapsed();
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
                    .setMultipartParameter("limit", String.valueOf(30))
                    .setMultipartParameter("page_no", String.valueOf(mPage));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.GURUS_LIST)){
            progress_layout.setVisibility(View.GONE);
            if (jsonstring.optBoolean("status")){

                if (((HomeActivityy)context).homeGuruList.size()>0){
                    ((HomeActivityy)context).homeGuruList.clear();
                }
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                if (jsonArray.length()>0){

                    for(int i=0;i<jsonArray.length();i++){
                        GuruPojo guruPojo=new Gson().fromJson(jsonArray.opt(i).toString(),GuruPojo.class);
                        ((HomeActivityy)context).homeGuruList.add(guruPojo);
                    }

                    recyclerView.setLayoutManager(linearLayoutManager);
                    gurusAdapter=new GurusAdapter(((HomeActivityy)context).homeGuruList,context);
                    recyclerView.setAdapter(gurusAdapter);
                  //  gurusAdapter.notifyDataSetChanged();
                    
                }
            }
            else{
                ToastUtil.showDialogBox(context,jsonstring.optString("message"));
            }
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        progress_layout.setVisibility(View.GONE);
    }
}