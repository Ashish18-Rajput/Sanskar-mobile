package com.sanskar.tv.module.HomeModule.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.R;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SubscriptionFragment extends Fragment implements NetworkCall.MyNetworkCallBack {


    Context context;
    HomeActivityy homeActivityy;
    SignupResponse signupResponse;
    NetworkCall networkCall;
    AppTextView edit;
    TextView plan_expires_date,validity_textView_2,plan_textView,validity_textView;

    public SubscriptionFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        networkCall.NetworkAPICall(API.GET_SUBSCRIPTION_PLAN, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        edit = ((HomeActivityy) context).editTV;
        edit.setVisibility(View.GONE);
        plan_expires_date = view.findViewById(R.id.plan_expires_date);
        validity_textView_2 = view.findViewById(R.id.validity_textView_2);
        plan_textView = view.findViewById(R.id.plan_textView);
        validity_textView = view.findViewById(R.id.validity_textView);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        homeActivityy = (HomeActivityy) getActivity();
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        networkCall = new NetworkCall(this, context);
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;

        ion = (Builders.Any.B) Ion.with(context)
                .load(API.GET_SUBSCRIPTION_PLAN)
                .setMultipartParameter("user_id", signupResponse.getId());

        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        String start2 = new SimpleDateFormat("dd MMM yyyy", Locale.US).format(new Date(Long.parseLong(jsonstring.optJSONObject("data").optString("expire_date"))));
        plan_expires_date.setText(start2);

        validity_textView_2.setText(jsonstring.optJSONObject("data").optString("plan_name"));
//        validity_textView.setText(Utils.JavaNumberOfDaysToMonthAndDay(jsonstring.optJSONObject("data").optString("validity")));
        String currency = jsonstring.optJSONObject("data").optString("currency");
       /* if (currency!=null && !TextUtils.isEmpty(currency) && currency.equalsIgnoreCase("INR"))
        plan_textView.setText("\u20B9"+jsonstring.optJSONObject("data").optString("amount"));
        else plan_textView.setText("$"+jsonstring.optJSONObject("data").optString("amount"));*/

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}
