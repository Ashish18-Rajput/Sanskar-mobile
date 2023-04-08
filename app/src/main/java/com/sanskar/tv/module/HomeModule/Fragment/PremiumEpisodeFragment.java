package com.sanskar.tv.module.HomeModule.Fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Premium.Adapter.PremiumEpisodeAdapter2;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMasterList;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;


public class PremiumEpisodeFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

    MenuMasterList menuMasterList1;
    NetworkCall networkCall;
    Context context;
    List<MenuMasterList> menuMasterLists = new ArrayList<>();
    RecyclerView recyclerView;

    String season_id = "";
    String episodes_id = "";
    String Category_name = "";

    TextView title_textView;
    View view1;
    PremiumEpisodeAdapter2 premiumEpisodeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promo_premium, container, false);
        getBundleData();
        fetchData();
        title_textView = view.findViewById(R.id.title_textView);
        title_textView.setVisibility(View.GONE);
        view1 = view.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
        title_textView.setText(Category_name);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle.containsKey("Id")) {
            season_id = bundle.getString("Id");
        }
        if (bundle.containsKey("Category")) {
            episodes_id = bundle.getString("Category");
        }
        if (bundle.containsKey("Category_name")) {
            Category_name = bundle.getString("Category_name");
        }
    }

    public void fetchData() {

        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, context);
            networkCall.NetworkAPICall(API.GET_EPISODES_LIST_BY_SEASON_ID, true);
        } else {
            ToastUtil.showDialogBox(context, "No Internet Connection");
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        if (API_NAME.equalsIgnoreCase(API.GET_EPISODES_LIST_BY_SEASON_ID)) {
            ion = (Builders.Any.B) Ion.with(this)
                    .load(API_NAME)
                    
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("season_id", season_id)
                    .setMultipartParameter("page_no", String.valueOf(1))
                    .setMultipartParameter("limit", String.valueOf(10))
                    .setMultipartParameter("episode_id", episodes_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String API_NAME) throws JSONException {
        if (API_NAME.equalsIgnoreCase(API.GET_EPISODES_LIST_BY_SEASON_ID)) {
            if (jsonstring.optBoolean("status")) {
                if (menuMasterLists.size() > 0) {
                    menuMasterLists.clear();
                }

                JSONArray jsonArray = jsonstring.optJSONArray("data");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        menuMasterList1 = new Gson().fromJson(jsonArray.opt(i).toString(), MenuMasterList.class);
                        menuMasterLists.add(menuMasterList1);
                    }

                    premiumEpisodeAdapter = new PremiumEpisodeAdapter2(context, menuMasterLists);
                    premiumEpisodeAdapter.notifyDataSetChanged();
                    recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    recyclerView.setAdapter(premiumEpisodeAdapter);
                }
            } else {
                ToastUtil.showDialogBox(context, jsonstring.optString("message"));
            }
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}