package com.sanskar.tv;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.module.HomeModule.Adapter.LiveStreamOnTopLiveChannelAdapter;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Demo extends AppCompatActivity implements NetworkCall.MyNetworkCallBack {
    Activity activity;
    RecyclerView recycle;
    NetworkCall networkCall;
    Channel channel;
    public ArrayList<Channel> homeChannelList;
    public SignupResponse signupResponse;
    private LiveStreamOnTopLiveChannelAdapter liveStreamOnTopLiveChannelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        activity=this;
        recycle=findViewById(R.id.recycle);
        homeChannelList=new ArrayList<>();
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        networkCall = new NetworkCall(this, Demo.this);
        networkCall.NetworkAPICall(API.GET_BHAJANS, true);

    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.GET_BHAJANS)) {
           ion=  (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", signupResponse.getId());
        }
        return  ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {


            // JSONArray jsonArray = result.optJSONArray("data");
            if ( apitype.equals(API.GET_BHAJANS)) {
                if (jsonstring.optBoolean("status")) {
                setChannelData(jsonstring);
                //notifyAdapters();

            }
        }
    }

   /* private void notifyAdapters() {
        liveStreamOnTopLiveChannelAdapter = new LiveStreamOnTopLiveChannelAdapter(homeChannelList, this);

        LinearLayoutManager ontoplivetvLM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle.setLayoutManager(ontoplivetvLM);
        recycle.setAdapter(liveStreamOnTopLiveChannelAdapter);

    }*/

    private void setChannelData(JSONObject jsonstring) {


            JSONArray jsonArrayChannel = jsonstring.optJSONArray("channel");




            if (jsonArrayChannel != null) {
                for (int i = 0; i < jsonArrayChannel.length(); i++) {
                    channel = new Gson().fromJson(jsonArrayChannel.opt(i).toString(), Channel.class);
                    homeChannelList.add(channel);
                }

            }


    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}
