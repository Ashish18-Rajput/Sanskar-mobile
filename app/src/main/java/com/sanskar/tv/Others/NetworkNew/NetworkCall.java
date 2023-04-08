package com.sanskar.tv.Others.NetworkNew;


import android.content.Context;
import android.util.Log;

import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.PhantomReference;

public class NetworkCall {

    MyNetworkCallBack myCBI;
    public Progress mprogress;
    Context context;
    String id;
    SignupResponse signupResponse;

    public NetworkCall(MyNetworkCallBack callBackInterface, Context context) {
        mprogress = new Progress(context);
        mprogress.setCancelable(false);
        myCBI = callBackInterface;
        this.context = context;
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,SignupResponse.class);
    }

    public void NetworkAPICall(final String apiType, final boolean showprogress) {

        Log.e("NetworkAPI Interface", "================" + apiType);
        if (Utils.isConnectingToInternet(context)) {
            if (showprogress) mprogress.show();
            String user_id ="";

            if (signupResponse!=null && signupResponse.getId()!=null){
                user_id = signupResponse.getId();
            }

            myCBI.getAPIB(apiType)
                    .addHeader(Constants.VERSION, "" + Utils.getVersionCode(context))
                    .addHeader(Constants.DEVICE_TYPE, "1")
                    .addHeader("jwt", SharedPreference.getInstance(context).getString(Constants.JWT)!=null?SharedPreference.getInstance(context).getString(Constants.JWT):Utils.JWT)
                    .addHeader("Deviceid", SharedPreference.getInstance(context).getString("android_id"))
                    .addHeader("userid",user_id)
                    .asString()
                    .setCallback((e, jsonString) -> {

                        if (showprogress) {
                            mprogress.dismiss();
                        }
                        try {
                            if (e == null) {
                                if (jsonString != null && !jsonString.isEmpty()) {
                                    Log.e("jsonString", "jsonString" + jsonString);
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    myCBI.SuccessCallBack(jsonObject, apiType);
                                } else {
                                    myCBI.ErrorCallBack(context.getString(R.string.jsonparsing_error_message), apiType);
                                }
                            } else {
                                myCBI.ErrorCallBack(context.getString(R.string.exception_api_error_message), apiType);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    });
        } else {
            myCBI.ErrorCallBack(Networkconstants.ERR_NETWORK_TIMEOUT, apiType);
        }
    }

    public void onSessionExpired() {
    }

    public interface MyNetworkCallBack {

        Builders.Any.B getAPIB(String apitype);

        void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException;

        void ErrorCallBack(String jsonstring, String apitype);
    }


}
