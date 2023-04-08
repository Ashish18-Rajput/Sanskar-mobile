package com.sanskar.tv.Premium.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Adapter.PromoPremiumAdapter2;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMaster;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class PremiumCategoryFragment extends Fragment implements NetworkCall.MyNetworkCallBack {
    NetworkCall networkCall;
    Context context;
    MenuMasterList menuMasterList;
    List<MenuMaster> masterList = new ArrayList<>();
    List<MenuMasterList> menuMasterLists = new ArrayList<>();
    TextView title_textView;
    RecyclerView recyclerView;
    String Video_id = "";
    HomeActivityy homeActivityy;
    String Category_name="";
    LinearLayoutManager linearLayoutManager;
    int page=1;
    boolean loading=false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    PromoPremiumAdapter2 promoPremiumAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        homeActivityy = (HomeActivityy) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_premium_category, container, false);
        getBundleData();
        initViews(view);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                visibleItemCount = linearLayoutManager.getChildCount();
                Log.d("Shantanu", "visibleItemCount " + visibleItemCount);
                totalItemCount = linearLayoutManager.getItemCount();
                Log.d("Shantanu", "totalItemCount " + totalItemCount);
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d("Shantanu", "pastVisiblesItems " + pastVisiblesItems);

                if (loading && visibleItemCount+pastVisiblesItems>=totalItemCount && menuMasterLists.size()>8   ) {
                    loading = false;
                    page++;

                    Log.d("Shantanu", "shantanu");
                   // fetchData(false);
                }
            }
        });

        promoPremiumAdapter=new PromoPremiumAdapter2(context, menuMasterLists);
        recyclerView.setAdapter(promoPremiumAdapter);

        return view;
    }

    private void initViews(View view) {
        linearLayoutManager=new GridLayoutManager(context,2);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        title_textView=view.findViewById(R.id.title_textView);
        title_textView.setText(Category_name);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData(false);
    }

    private void getBundleData() {
        Bundle bundle=getArguments();
        if (bundle.containsKey("Id")) {
            Video_id = bundle.getString("Id");
        }
        if (bundle.containsKey("Category_name")) {
            Category_name = bundle.getString("Category_name");
        }
    }

    private void fetchData(boolean b) {
        if (isConnectingToInternet(context)) {
            networkCall=new NetworkCall(this,context);
            networkCall.NetworkAPICall(API.GET_SEASON_BY_CATEGORY, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equalsIgnoreCase(API.GET_SEASON_BY_CATEGORY)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setHeader("jwt", SharedPreference.getInstance(homeActivityy).getString(Constants.JWT)!=null?SharedPreference.getInstance(homeActivityy).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",homeActivityy.signupResponse.getId())
                    .setMultipartParameter("user_id", homeActivityy.signupResponse.getId())
                    .setMultipartParameter("category_id", Video_id)
                    .setMultipartParameter("page_no", String.valueOf(page))
                    .setMultipartParameter("limit", String.valueOf(100));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (result.optBoolean("status")) {
            if (API_NAME.equalsIgnoreCase(API.GET_SEASON_BY_CATEGORY)) {

                JSONArray jsonArray = result.optJSONArray("data");

                if (menuMasterLists.size()>0){
                    menuMasterLists.clear();
                }
                if (jsonArray.length()>0){

                    for (int i=0;i<jsonArray.length();i++){
                        menuMasterList=new Gson().fromJson(jsonArray.opt(i).toString(),MenuMasterList.class);
                        menuMasterLists.add(menuMasterList);
                    }
                    promoPremiumAdapter.notifyDataSetChanged();

                    loading=true;
                }
                else{
                    loading=false;
                }


            }
        } else {
            ToastUtil.showDialogBox(context, result.optString("message"));
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}