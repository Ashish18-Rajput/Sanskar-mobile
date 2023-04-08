package com.sanskar.tv.Others.network;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.Progress;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cbc-03 on 05/26/17.
 */

public class NetworkCall {

    MyNetworkCallBack myNetworkCallBack;
    public Progress progressDialog;
    Context context;

    public NetworkCall(MyNetworkCallBack callBackInterface, Context context) {
        progressDialog = new Progress(context);
        progressDialog.setCancelable(false);
        myNetworkCallBack = callBackInterface;
        this.context = context;
    }

    public void NetworkAPICall(final String API_NAME, final boolean showprogress, String API_TYPE) {
        Log.e("NetworkAPI Interface", "================" + API_NAME);
        if (Utils.isConnectingToInternet(context)) {
            if (showprogress) {
                progressDialog.show();
            }

            if (API_TYPE.equals(Networkconstants.JSON_BODY)) {
                myNetworkCallBack.getAPIB(API_NAME).asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String jsonString) {
                                Log.e("NetworkAPI Interface", "================" + jsonString);
                                if (showprogress) progressDialog.dismiss();
                                try {
                                    if (e == null) {
                                        if (jsonString != null && !jsonString.isEmpty()) {
                                            JSONObject jsonObject = new JSONObject(jsonString);

                                            myNetworkCallBack.SuccessCallBack(jsonObject, API_NAME);
                                        } else {
                                            myNetworkCallBack.ErrorCallBack(context.getString(R.string.jsonparsing_error_message), API_NAME);
                                        }
                                    } else {
                                        myNetworkCallBack.ErrorCallBack(context.getString(R.string.exception_api_error_message), API_NAME);
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
            } else {
                myNetworkCallBack.getAPI(API_NAME).asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String jsonString) {
                                Log.e("NetworkAPI Interface", "================" + jsonString);
                                if (showprogress) progressDialog.dismiss();
                                try {
                                    if (e == null) {
                                        if (jsonString != null && !jsonString.isEmpty()) {
                                            JSONObject jsonObject = new JSONObject(jsonString);

                                            myNetworkCallBack.SuccessCallBack(jsonObject, API_NAME);
                                        } else {
                                            myNetworkCallBack.ErrorCallBack(context.getString(R.string.jsonparsing_error_message), API_NAME);
                                        }
                                    } else {
                                        myNetworkCallBack.ErrorCallBack(context.getString(R.string.exception_api_error_message), API_NAME);
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
            }
        } else {
            myNetworkCallBack.ErrorCallBack(context.getString(R.string.internet_error), API_NAME);
        }
    }

    public interface MyNetworkCallBack {
        Builders.Any.M getAPI(String API_NAME);

        Builders.Any.B getAPIB(String API_NAME);

        void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException;

        void ErrorCallBack(String message, String API_NAME);
    }


}
