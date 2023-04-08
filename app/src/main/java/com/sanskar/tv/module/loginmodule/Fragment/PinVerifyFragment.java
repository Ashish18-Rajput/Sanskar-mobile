package com.sanskar.tv.module.loginmodule.Fragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.CustomViews.AppEditText;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class PinVerifyFragment extends Fragment implements View.OnClickListener,NetworkCall.MyNetworkCallBack, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    TextView submit, forgot_pin, send_otp_with_otp;
    ImageView back;
    Context ctx;
    SignupResponse data;
    private NetworkCall networkCall;
    Pinview pinview;
    private int TYPE = 1;
    String otp = "", otptext = "", mobile = "";
    String deviceId = "";
    TextView textView_ll;
    boolean isForgot = false;
    boolean wantsNormal = false;

    private AppEditText mPinFirstDigitEditText;
    private AppEditText mPinSecondDigitEditText;
    private AppEditText mPinThirdDigitEditText;
    private AppEditText mPinForthDigitEditText;
    private AppEditText mPinHiddenEditText;

    public PinVerifyFragment() {
        // Required empty public constructor
    }

    public static PinVerifyFragment newInstance(String param1, String param2) {
        return new PinVerifyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getContext();
        networkCall = new NetworkCall(this,ctx);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pin_verify, container, false);
        ctx = getActivity();
        data = (SignupResponse) getArguments().getSerializable("data");

        if (getArguments().getString(Constants.OTP) != null) {
            otp = getArguments().getString(Constants.OTP);
        }
        if (getArguments().getString(Constants.MOBILE) != null) {
            mobile = getArguments().getString(Constants.MOBILE);
        }


        initView(view);
        if (SharedPreference.getInstance(ctx).getString("android_id") != null) {
            deviceId = SharedPreference.getInstance(ctx).getString("android_id");
        }

        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        forgot_pin.setOnClickListener(this);
        send_otp_with_otp.setOnClickListener(this);

        setPINListeners();
        setFocus(mPinHiddenEditText);


        return view;
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }


    public void ChangeBackground(AppEditText ET, int set) {
        if (set == 1)
            ET.setBackgroundResource(R.drawable.bg_white_btn);
        else if (set == 2)
            ET.setBackgroundResource(R.drawable.bg_gray_btn);
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    private void initView(View view) {
        pinview = view.findViewById(R.id.fragment_otp_pinview);
        textView_ll = view.findViewById(R.id.textView_ll);
        forgot_pin = view.findViewById(R.id.forgot_pin);
        mPinFirstDigitEditText = view.findViewById(R.id.OPT1ET);
        mPinSecondDigitEditText = view.findViewById(R.id.OPT2ET);
        mPinThirdDigitEditText = view.findViewById(R.id.OPT3ET);
        mPinForthDigitEditText = view.findViewById(R.id.OPT4ET);
        mPinHiddenEditText = view.findViewById(R.id.pin_hidden_edittext);


        submit = view.findViewById(R.id.submit);
        back = view.findViewById(R.id.back);
        send_otp_with_otp = view.findViewById(R.id.send_otp_with_otp);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            mPinFirstDigitEditText.setText("");
        } else if (s.length() == 1) {
            ChangeBackground(mPinFirstDigitEditText, 1);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");

        } else if (s.length() == 2) {
            ChangeBackground(mPinSecondDigitEditText, 1);

            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 3) {
            ChangeBackground(mPinThirdDigitEditText, 1);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
        } else if (s.length() == 4) {
            ChangeBackground(mPinForthDigitEditText, 1);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            hideSoftKeyboard(mPinForthDigitEditText);
            onClick(submit);
        }
    }

    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }


    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                isForgot = false;
                wantsNormal = false;
                otptext = mPinHiddenEditText.getText().toString().trim();
                if (Utils.isConnectingToInternet(ctx)) {
                    networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);
                } else
                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);

                //Toast.makeText(ctx, "pin: "+otptext +"  "+"otp: "+otp, Toast.LENGTH_SHORT).show();
                break;


            case R.id.back:
                ((LoginSignupActivity) ctx).onBackPressed();
                break;

            case R.id.forgot_pin:
                isForgot = true;
                wantsNormal = false;
                networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);

            case R.id.send_otp_with_otp:
                wantsNormal = true;
                isForgot = false;
                networkCall.NetworkAPICall(API.SIGN_UP, true);


        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        final int id = v.getId();
        switch (id) {
            case R.id.OPT1ET:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.OPT2ET:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.OPT3ET:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.OPT4ET:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            default:
                break;

        }

    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = view.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (i == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4) {
                            ChangeBackground(mPinForthDigitEditText, 2);
                            mPinForthDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 3) {
                            ChangeBackground(mPinThirdDigitEditText, 2);

                            mPinThirdDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 2) {
                            ChangeBackground(mPinSecondDigitEditText, 2);

                            mPinSecondDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() == 1) {
                            ChangeBackground(mPinFirstDigitEditText, 2);

                            mPinFirstDigitEditText.setText("");
                        }

                    }

                    break;

                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {
            if (!isForgot){
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("pin", otptext)
                        .setMultipartParameter("otp", "")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            }else{
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("pin", "")
                        .setMultipartParameter("otp", "")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            }

        } else if (API_NAME.equals(API.SIGN_UP)){
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setHeader("Deviceid", deviceId)
                    .setMultipartParameter("country_code", data.getCountry_code())
                    .setMultipartParameter("mobile", data.getMobile())
                    .setMultipartParameter("login_with", String.valueOf(0))
                    .setMultipartParameter("login_type", data.getLogin_type())
                    .setMultipartParameter("device_type", String.valueOf(1))
                    .setMultipartParameter("device_model", getDeviceName())
                    .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN))
                    .setMultipartParameter("device_id", deviceId)
                    .setMultipartParameter("source", "normal")
                    .setMultipartParameter("login_with_otp", "1");

            Log.e("1234", "ccp"+ data.getCountry_code() + "mobile" + data.getMobile() + "logintype" + data.getLogin_type());

        } else if (API_NAME.equals(API.GET_USER_WITH_PROFILE_TOKEN)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setHeader("mobile",data.getMobile())
                    .setMultipartParameter("login_type", "0")
                    .setMultipartParameter("device_id", deviceId);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.SEND_VERIFICATION_OTP)){
            mPinHiddenEditText.setText("");
            mPinFirstDigitEditText.setText("");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            setFocus(mPinHiddenEditText);
            ChangeBackground(mPinFirstDigitEditText,2);
            ChangeBackground(mPinSecondDigitEditText,2);
            ChangeBackground(mPinThirdDigitEditText,2);
            ChangeBackground(mPinForthDigitEditText,2);
            if (!isForgot){
                if (jsonstring.optBoolean("status"))
                    networkCall.NetworkAPICall(API.GET_USER_WITH_PROFILE_TOKEN, true);
                else Toast.makeText(ctx, jsonstring.optString(Constants.MESSAGES), Toast.LENGTH_SHORT).show();
            }else{
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                bundle.putString(Constants.MOBILE, data.getMobile());
                bundle.putString(Constants.PIN,"0");
                bundle.putString(Constants.IS_FORGOT,"0");
                OtpFragment fragment = new OtpFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();

            }

        }else if (apitype.equalsIgnoreCase(API.GET_USER_WITH_PROFILE_TOKEN)){
            if (jsonstring.optBoolean("status")) {
                ((LoginSignupActivity) ctx).signupResponse = new Gson().fromJson(jsonstring.optJSONObject("data").toString(), SignupResponse.class);
                SharedPreference.getInstance(getContext()).putBoolean(Constants.LOGIN_SESSION,true);
                PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, ((LoginSignupActivity) ctx).signupResponse);
                Intent intent = new Intent(ctx, HomeActivityy.class);
                ((LoginSignupActivity) ctx).finish();
                startActivity(intent);
            } else {
                ToastUtil.showShortToast(getContext(), jsonstring.optString("message"));
            }
        } else if (apitype.equalsIgnoreCase(API.SIGN_UP)){
            Constants.NUMBER_WITH_COUNTRYCODE = data.getCountry_code() + data.getMobile();
            JSONObject jsonObject = jsonstring.optJSONObject("data");

            if (jsonObject.optString("jwt") != null) {
                Utils.JWT = jsonObject.optString("jwt");
                SharedPreference.getInstance(getContext()).putString(Constants.JWT, jsonObject.optString("jwt"));
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            bundle.putString(Constants.MOBILE, data.getMobile());
            bundle.putString(Constants.PIN,"0");
            bundle.putString(Constants.WANTS_NORMAL, "1");
            OtpFragment fragment = new OtpFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();

        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {

            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}