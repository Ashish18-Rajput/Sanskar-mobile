package com.sanskar.tv.module.HomeModule.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Adapter.ActiveDevicesAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.ActiveDevicesModel;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ActiveDevicesFragment extends Fragment implements NetworkCall.MyNetworkCallBack, ActiveDevicesAdapter.SelectDevicesForDetach {

    RecyclerView activeDevicesRecyclerView;
    Context context;
    List<ActiveDevicesModel> activeDevicesModelList = new ArrayList<>();
    ActiveDevicesAdapter activeDevicesAdapter;
    NetworkCall networkCall;
    RelativeLayout no_data_found_rl;
    int position;
    String user_id = "";
    boolean isFragmentInstance = false;
    boolean wantToMoveBack = false;
    public Map<Integer, String> brandsMap = new ArrayMap<>();
    Button detachDevicesBtn;

    public ActiveDevicesFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        //  activity = (HomeActivityy) getActivity();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_devices, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            if (getArguments().containsKey("id")) {
                user_id = getArguments().getString("id");
            }
            if (getArguments().containsKey("activity")) {
                isFragmentInstance = getArguments().getString("activity").equalsIgnoreCase("true");
            }
        }
        activeDevicesRecyclerView = view.findViewById(R.id.activeDevicesRecyclerView);
        detachDevicesBtn = view.findViewById(R.id.detachDevicesBTn);
        no_data_found_rl = view.findViewById(R.id.no_data_found_rl);
        activeDevicesAdapter = new ActiveDevicesAdapter(context, this, activeDevicesModelList, this);
        activeDevicesRecyclerView.setAdapter(activeDevicesAdapter);
        activeDevicesRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        networkCall = new NetworkCall(this, context);

        detachDevicesBtn.setOnClickListener(v->{
            if (Utils.isConnectingToInternet(context))
                networkCall.NetworkAPICall(API.Device_Logout, true);
            else Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();

        });

        getDeviceList();
    }


    private void getDeviceList() {
        if (Utils.isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.GET_DEVICE_LIST, true);
        } else Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);
        Builders.Any.B ion = null;
        if (API_NAME.equalsIgnoreCase(API.GET_DEVICE_LIST)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", user_id);

        } else if (API_NAME.equalsIgnoreCase(API.Device_Logout)) {
            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
                    .setMultipartParameter("user_id", user_id)
                    .setMultipartParameter("device_id", SharedPreference.getInstance(context).getString("android_id") )
                    .setMultipartParameter("login_record_id", new Gson().toJson(brandsMap.values().toArray(new String[0])))
                    ;

        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (jsonstring.optBoolean("status")) {
            if (apitype.equalsIgnoreCase(API.GET_DEVICE_LIST)) {
                activeDevicesModelList.clear();
                brandsMap.clear();
                JSONArray jsonArray = jsonstring.optJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    no_data_found_rl.setVisibility(View.GONE);
                    activeDevicesRecyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ActiveDevicesModel activeDevicesModel = new Gson().fromJson(jsonArray.opt(i).toString(), ActiveDevicesModel.class);
                        activeDevicesModelList.add(activeDevicesModel);
                    }
                    activeDevicesAdapter.updateSelectedDevicesList(activeDevicesModelList,brandsMap);
                    activeDevicesAdapter.notifyDataSetChanged();
                } else {
                    no_data_found_rl.setVisibility(View.VISIBLE);
                    activeDevicesRecyclerView.setVisibility(View.GONE);
                }
                if (isFragmentInstance && wantToMoveBack) {
                    if (context instanceof LoginSignupActivity)
                        ((LoginSignupActivity) context).onBackPressed();
                }
            } else if (apitype.equalsIgnoreCase(API.Device_Logout)) {
                detachDevicesBtn.setVisibility(View.GONE);
                Toast.makeText(context, jsonstring.optString("message"), Toast.LENGTH_SHORT).show();
                getDeviceList();

                if (isFragmentInstance) wantToMoveBack = true;
            }


        } else Toast.makeText(context, jsonstring.optString("message"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    public void detachDevice(int position) {
        brandsMap.clear();
        this.position = position;
        brandsMap.put(position,activeDevicesModelList.get(position).getId());
        if (Utils.isConnectingToInternet(context))
            networkCall.NetworkAPICall(API.Device_Logout, true);
        else Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectDevices(RecyclerView.ViewHolder viewHolder, int index) {
        ActiveDevicesModel activeDevicesModel = activeDevicesModelList.get(index);
        if (activeDevicesModel.getIsSelected()) {
            brandsMap.remove(index);
            activeDevicesModel.setIsSelected(false);
            activeDevicesAdapter.updateSelectedDevicesList(activeDevicesModelList, brandsMap);
            activeDevicesAdapter.notifyItemChanged(index);
        } else {
            brandsMap.put(index, activeDevicesModel.getId());
            activeDevicesModel.setIsSelected(true);
            activeDevicesAdapter.updateSelectedDevicesList(activeDevicesModelList, brandsMap);
            activeDevicesAdapter.notifyItemChanged(index);
        }
        if (brandsMap.isEmpty()) detachDevicesBtn.setVisibility(View.GONE);
        else detachDevicesBtn.setVisibility(View.VISIBLE);
    }


}