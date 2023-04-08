package com.sanskar.tv.newaudiocode;

import android.content.Context;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Adapter.BhajanCategoryListAdapter;
import com.sanskar.tv.module.HomeModule.Fragment.SearchBhajanFrag;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

public class NewAudioPlayerActivity extends AppCompatActivity implements NetworkCall.MyNetworkCallBack {

    private Bhajan[] bhajanList;
    private ImageView bannerImg;
    RecyclerView recyclerView;
    BhajanCategoryListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    private NetworkCall networkCall;
    public ArrayList<BhajanResponse> bhajanResponseArrayList = new ArrayList<>();
    public SignupResponse signupResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_audio_player);
        networkCall = new NetworkCall(NewAudioPlayerActivity.this, this);
        bhajanResponseArrayList=(ArrayList<BhajanResponse>) getIntent().getSerializableExtra("bhajanarraylist");
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        initview();
        context = this;

        if (bhajanResponseArrayList.isEmpty()) {

            if (isConnectingToInternet(this)) {
                networkCall.NetworkAPICall(API.GET_BHAJANS, true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
            }
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isConnectingToInternet(context)) {
                    //   networkCall = new com.sanskar.totalbhakti.Others.NetworkNew.NetworkCall(BhajansCategoryFragment.this, context);
                    networkCall.NetworkAPICall(API.GET_BHAJANS, false);
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }
            }
        });

        setCatAdapter();
    }

    private void initview(){
        bannerImg = findViewById(R.id.banner_img);
        bannerImg.setImageResource(R.mipmap.bhajan);
        bannerImg.setVisibility(View.VISIBLE);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_view_all);
        recyclerView = findViewById(R.id.view_all_recycler_view);
    }


    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.GET_BHAJANS)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId());
        } else if (API_NAME.equals(API.BHAJAN_SEARCH)) {
            ion = (Builders.Any.B) Ion.with(this).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("search_content", "search"/*((HomeActivityy) context).searchContent*/);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        swipeRefreshLayout.setRefreshing(false);
        if (jsonstring.optBoolean("status")) {
            JSONArray jsonArray = jsonstring.optJSONArray("data");
            if (apitype.equals(API.GET_BHAJANS)) {
                if (jsonArray.length() > 0) {
                    if (bhajanResponseArrayList.size() > 0)
                        bhajanResponseArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        BhajanResponse bhajanResponse = new Gson().fromJson(jsonArray.opt(i).toString(), BhajanResponse.class);
                        bhajanResponseArrayList.add(bhajanResponse);
                    }
                    adapter.notifyDataSetChanged();
                }
            } else if (apitype.equals(API.BHAJAN_SEARCH)) {
                if (jsonArray.length() > 0) {
                    bhajanList = new Bhajan[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Bhajan bhajan = new Gson().fromJson(jsonArray.opt(i).toString(), Bhajan.class);
                        bhajanList[i] = bhajan;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bhajanList", bhajanList);
                    SearchBhajanFrag frag = new SearchBhajanFrag();
                    frag.setArguments(bundle);
                    ((HomeActivityy) context)
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("BHAJAN_SEARCH")
                            .replace(R.id.container_layout_home, frag)
                            .commit();
                } else {
                    //searchContent = "";
                    ToastUtil.showDialogBox(context, getString(R.string.no_data_found));
                }
            }
        }
    }

    @Override
    public void ErrorCallBack(String message, String API_NAME) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //((HomeActivityy) context).invalidateOptionsMenu();
        //toolbar.setVisibility(View.GONE);
       // ((HomeActivityy) context).handleToolbar();
    }
    private void setCatAdapter(){
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BhajanCategoryListAdapter(bhajanResponseArrayList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        ViewCompat.setNestedScrollingEnabled(recyclerView, false);
    }
}
