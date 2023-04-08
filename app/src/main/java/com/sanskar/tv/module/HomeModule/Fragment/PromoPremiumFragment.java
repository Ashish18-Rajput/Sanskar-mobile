package com.sanskar.tv.module.HomeModule.Fragment;

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
import android.widget.AbsListView;
import android.widget.TextView;

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
import com.sanskar.tv.module.HomeModule.Adapter.PromoPremiumAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMaster;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class PromoPremiumFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    NetworkCall networkCall;
    Context context;
    JSONArray jsonArray1;
    MenuMaster menuMaster;
    MenuMasterList menuMasterList;
    List<MenuMaster> masterList = new ArrayList<>();
    List<MenuMasterList> menuMasterLists = new ArrayList<>();
    RecyclerView recyclerView;
    String Video_id = "";
    String Category = "";
    String Premium_Auth_Id = "";
    String Premium_Cat_Id = "";
    String Category_name = "";
    HomeActivityy homeActivityy;
    LinearLayoutManager linearLayoutManager;
    int page=1;
    boolean loading=false;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    PromoPremiumAdapter promoPremiumAdapter;
    TextView title_textView;

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
        View view = inflater.inflate(R.layout.fragment_promo_premium, container, false);
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
                visibleItemCount = linearLayoutManager.getChildCount();
                Log.d("Shantanu", "visibleItemCount " + visibleItemCount);
                totalItemCount = linearLayoutManager.getItemCount();
                Log.d("Shantanu", "totalItemCount " + totalItemCount);
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                Log.d("Shantanu", "pastVisiblesItems " + pastVisiblesItems);

                if (loading && visibleItemCount+pastVisiblesItems>totalItemCount) {
                    loading = false;
                    page++;

                    //progress_layout.setVisibility(View.VISIBLE);
                    //lastGuruId = ((HomeActivityy) context).homeGuruList.get(totalItemCount - 1).getId();
                    //relativeLayout.setVisibility(View.VISIBLE);

                    Log.d("Shantanu", "shantanu");
                    fetchData(false);
                }
            }
        });

        promoPremiumAdapter=new PromoPremiumAdapter(context, menuMasterLists);
        recyclerView.setAdapter(promoPremiumAdapter);

        return view;
    }

    private void initViews(View view) {
        if (Category.equalsIgnoreCase("3")){
            linearLayoutManager = new GridLayoutManager(context, 2);
        }else{
            linearLayoutManager = new LinearLayoutManager(context);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        title_textView=view.findViewById(R.id.title_textView);

        title_textView.setText(Category_name);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle.containsKey("Id")) {
            Video_id = bundle.getString("Id");
        }
        if (bundle.containsKey("Category")) {
            Category = bundle.getString("Category");
        }
        if (bundle.containsKey("Premium_Cat_Id")) {
            Premium_Cat_Id = bundle.getString("Premium_Cat_Id");
        }
        if (bundle.containsKey("Premium_Auth_Id")) {
            Premium_Auth_Id = bundle.getString("Premium_Auth_Id");
        }
        if (bundle.containsKey("Category_name")) {
            Category_name = bundle.getString("Category_name");
        }

    }

    public void fetchData(boolean b) {

        if (isConnectingToInternet(context)) {
            networkCall=new NetworkCall(this,context);
            networkCall.NetworkAPICall(API.GET_SEASON_LIST_BY_MENUMASTER, b);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equalsIgnoreCase(API.GET_SEASON_LIST_BY_MENUMASTER)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    
                    .setMultipartParameter("user_id", homeActivityy.signupResponse.getId())
                    .setMultipartParameter("menu_master_id", Video_id)
                    .setMultipartParameter("category", Category)
                    .setMultipartParameter("premium_auth_id", Premium_Auth_Id)
                    .setMultipartParameter("premium_cat_id", Premium_Cat_Id)
                    .setMultipartParameter("page_no", String.valueOf(page))
                    .setMultipartParameter("limit", String.valueOf(10));
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (result.optBoolean("status")) {
            if (API_NAME.equalsIgnoreCase(API.GET_SEASON_LIST_BY_MENUMASTER)) {

                JSONArray jsonArray = result.optJSONArray("data");

                if (jsonArray.length()>0){
                    loading=true;
                    for (int i=0;i<jsonArray.length();i++){
                        menuMasterList=new Gson().fromJson(jsonArray.opt(i).toString(),MenuMasterList.class);
                        menuMasterLists.add(menuMasterList);
                    }
                    promoPremiumAdapter.notifyDataSetChanged();
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