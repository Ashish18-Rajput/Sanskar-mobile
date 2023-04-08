package com.sanskar.tv.module.HomeModule.Fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by appsquadz on 1/30/18.
 */

public class TNCFragment extends Fragment implements NetworkCall.MyNetworkCallBack {

   // TextView toolbarTitle;
    //ImageView back;
    Context ctx;
    WebView webView;
    String privacyOrTerms;
    AppBarLayout toolbar;
    AppTextView tncPrivacyTxt,tncTermsTxt;
    //RelativeLayout toobar, progressBAr;
    private NetworkCall networkCall;

//    private AppTextView privacyTncTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_termsandcondition,null);
        ctx = getActivity();
        initView(view);
        //toolbar.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        privacyOrTerms = getArguments().getString("privacyOrTerms");
        //setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        webView = view.findViewById(R.id.web_view_tnc);
        tncPrivacyTxt = view.findViewById(R.id.tnc_privacy_txt);
        tncTermsTxt = view.findViewById(R.id.tnc_terms_txt);
        toolbar = view.findViewById(R.id.toolbar);
        networkCall = new NetworkCall(this, ctx);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setVisibility(View.GONE);

        if(privacyOrTerms.equals("terms")){
            ((HomeActivityy)ctx).getSupportActionBar().show();
            tncTermsTxt.setVisibility(View.VISIBLE);
            tncPrivacyTxt.setVisibility(View.GONE);


            webView.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                }
                @TargetApi(android.os.Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }
            });
            //webView.loadUrl("http://164.52.192.251/privacy.html");

            webView.loadUrl("https://app.sanskargroup.in/terms.html");
        //        networkCall.NetworkAPICall(API.TNC_API, true);
    }

        else {
            ((HomeActivityy)ctx).getSupportActionBar().show();
            tncTermsTxt.setVisibility(View.GONE);
            tncPrivacyTxt.setVisibility(View.VISIBLE);

            webView.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
                }
                @TargetApi(android.os.Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }
            });

          //  http://app.sanskargroup.in/sanskar/
            webView.loadUrl("https://app.sanskargroup.in/privacy.html");
       //    networkCall.NetworkAPICall(API.PRIVACY_API, true);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) ctx).invalidateOptionsMenu();
        ((HomeActivityy) ctx).handleToolbar();
//        ((HomeActivityy) ctx).handleToolBar();
    }


    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.TNC_API)) {
            ion =  Ion.with(ctx).load(API_NAME);
        } else if (API_NAME.equals(API.PRIVACY_API)) {
            ion =  Ion.with(ctx).load(API_NAME);
        }
//        else if(API_NAME.equals(API.RECENT_VIEW)){
//            ion = (Builders.Any.B) Ion.with(context).load(API_NAME)
//                    .setMultipartParameter("user_id", ((HomeActivityy) context).signupResponse.getId())
//                    .setMultipartParameter("type", "2")
//                    .setMultipartParameter("media_id", video_id);
//        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {

        if (result.getBoolean("status")) {

            Log.d("result....",""+result);
            String urlString = result.optString("urlLink");
            String desString = Html.fromHtml(urlString).toString();
            webView.loadUrl(desString);
        }
    }


    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}
