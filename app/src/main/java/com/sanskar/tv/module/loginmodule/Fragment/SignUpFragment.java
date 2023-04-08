package com.sanskar.tv.module.loginmodule.Fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.network.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.ProfileFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpFragment extends Fragment implements View.OnClickListener, NetworkCall.MyNetworkCallBack {

    Context ctx;
    TextView sendOtp, tnc, tnc1, iso_text, send_otp_with_otp;
    EditText phone, email_profile;
    RelativeLayout countryCodeLayout;
    TextInputLayout textInputLayout2, emailTextInputLayout, mobileTextInputLayout;
    CheckBox checkbox_terms, checkbox_policy;
    CountryCodePicker ccp, ccpNew;
    private Fragment fragment;
    private NetworkCall networkCall;
    Bundle bundle;
    SignupResponse data = new SignupResponse();
    TextInputEditText mobileEditText, emailEditText;
    ImageView back;
    public int is_number = 0;
    public static int number = 0;
    public static String android_id;
    private String device_model;
    String device_token = "";
    boolean is_aws_sms_android = false;
    RelativeLayout newLoginLayout, relativeLayout2;
    boolean wantsNormal = false;
    boolean fromGuestUser = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_1, null);
        ctx = getActivity();

        if (SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN) != null && !TextUtils.isEmpty(SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN)))
            device_token = SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN);
        else {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.w("Shantanu", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                device_token = task.getResult();

                SharedPreference.getInstance(getContext()).putString(Constants.DEVICETOKEN, device_token);

                // Log and toast
                Log.d("Shantanu", "Device_token  " + device_token);

            });
        }
        bundle = getArguments();
        if (bundle != null){
            Log.e("1234", "bundlevalue   " + bundle.getString("login_type"));
        } else {
            Toast.makeText(ctx, "asdfghjkl", Toast.LENGTH_SHORT).show();
        }

        initView(view);

        is_aws_sms_android = SharedPreference.getInstance(getContext()).getString(Constants.AWS_SMS_ANDROID).equalsIgnoreCase("1");

        if (is_aws_sms_android) {
            newLoginLayout.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.GONE);
        } else {
            newLoginLayout.setVisibility(View.GONE);
            relativeLayout2.setVisibility(View.VISIBLE);
            if (Networkconstants.iso.equalsIgnoreCase("")) {
                iso_text.setVisibility(View.GONE);
                ccp.setVisibility(View.VISIBLE);
                ccp.setCcpClickable(true);
                ccp.setFocusable(true);
                textInputLayout2.setVisibility(View.GONE);

            }
            if (Networkconstants.iso.equalsIgnoreCase("IN")) {
                ccp.setCcpClickable(false);
                ccp.setFocusable(false);

                ccp.setCcpDialogShowNameCode(true);
                textInputLayout2.setVisibility(View.GONE);
            }
            if (!Networkconstants.iso.equalsIgnoreCase("IN") && !Networkconstants.iso.equalsIgnoreCase("")) {
                countryCodeLayout.setVisibility(View.GONE);
                textInputLayout2.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private void initView(View view) {
        sendOtp = view.findViewById(R.id.send_otp);
        send_otp_with_otp = view.findViewById(R.id.send_otp_with_otp);
        tnc = view.findViewById(R.id.tnc);
        tnc1 = view.findViewById(R.id.tnc1);
        phone = view.findViewById(R.id.phone);
        ccp = view.findViewById(R.id.ccp);
        ccpNew = view.findViewById(R.id.ccpNew);

        ccpNew.setAutoDetectedCountry(true);
        ccpNew.setCcpClickable(false);
        ccpNew.setOnClickListener(v -> {

        });
        ccpNew.detectNetworkCountry(true);
        ccpNew.setOnCountryChangeListener(() -> {

        });

        ccp.registerCarrierNumberEditText(phone);
        checkbox_terms = view.findViewById(R.id.checkbox_terms);
        checkbox_policy = view.findViewById(R.id.checkbox_policy);
        back = view.findViewById(R.id.back);
        sendOtp.setOnClickListener(this);
        send_otp_with_otp.setOnClickListener(view1 -> {
            wantsNormal = true;
            if (is_aws_sms_android) {
                if (!mobileEditText.getText().toString().isEmpty() && !TextUtils.isEmpty(mobileEditText.getText().toString())) {
                    if (mobileEditText.getText().toString().length()==10) {
                        is_number = 1;

                        if (Utils.isConnectingToInternet(ctx)) {
                            networkCall = new NetworkCall(SignUpFragment.this, ctx);
                            networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                        } else
                            //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                            ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                    } else {
                        Toast.makeText(ctx, "Please enter valid mobile number.", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!emailEditText.getText().toString().isEmpty() && !TextUtils.isEmpty(emailEditText.getText().toString())) {
                    Log.d("shantanu", "email");
                    is_number = 2;
                    if (Utils.isEmailValid(emailEditText.getText().toString())) {
                        if (Utils.isConnectingToInternet(ctx)) {
                            networkCall = new NetworkCall(SignUpFragment.this, ctx);
                            networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                        } else
                            //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                            ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                    } else {
                        ToastUtil.showDialogBox(getContext(), "Invalid Email address");
                    }

                }

            } else {
                if (countryCodeLayout.isShown()) {
                    if (!phone.getText().toString().isEmpty()) {

                        is_number = 1;
                        if (validate()) {
                            if (Utils.isConnectingToInternet(ctx)) {
                                networkCall = new NetworkCall(SignUpFragment.this, ctx);
                                networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                            } else
                                //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                                ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                        }
                    } else {
                        Toast.makeText(ctx, "Mobile Field is Empty", Toast.LENGTH_SHORT).show();
                    }
                }

                if (textInputLayout2.isShown()) {
                    if (!email_profile.getText().toString().isEmpty()) {
                        Log.d("shantanu", "email");
                        is_number = 2;
                        if (Utils.isEmailValid(email_profile.getText().toString())) {
                            if (Utils.isConnectingToInternet(ctx)) {
                                networkCall = new NetworkCall(SignUpFragment.this, ctx);
                                networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                            } else
                                //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                                ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                        } else {
                            ToastUtil.showDialogBox(getContext(), "Invalid Email address");
                        }

                    } else {
                        Toast.makeText(ctx, "E-mail Field is Empty", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            number = is_number;
        });
        tnc.setOnClickListener(this);
        tnc1.setOnClickListener(this);
        back.setOnClickListener(this);
        countryCodeLayout = view.findViewById(R.id.countryCodeLayout);
        textInputLayout2 = view.findViewById(R.id.textInputLayout2);
        email_profile = view.findViewById(R.id.email_profile1);
        iso_text = view.findViewById(R.id.iso_text);
        relativeLayout2 = view.findViewById(R.id.relativeLayout2);
        newLoginLayout = view.findViewById(R.id.newLoginLayout);
        mobileEditText = view.findViewById(R.id.mobileEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        mobileTextInputLayout = view.findViewById(R.id.mobileTextInputLayout);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        mobileEditText.setOnClickListener(this);
        emailEditText.setOnClickListener(this);
        mobileTextInputLayout.setOnClickListener(this);
        emailTextInputLayout.setOnClickListener(this);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 9) {
                    hideSoftKeyboard(phone);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 9) {
                    hideSoftKeyboard(mobileEditText);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tnc.setText(Html.fromHtml(getResources().getString(R.string.tncc)));

        SpannableString ss = new SpannableString(getResources().getString(R.string.tncc));
        final ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Bundle bundle = new Bundle();
                bundle.putString("policy", "terms");
                TNCFragment tncFragment = new TNCFragment();
                tncFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, tncFragment).commit();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tnc.setText(ss);
        tnc.setMovementMethod(LinkMovementMethod.getInstance());

        tnc1.setText(Html.fromHtml(getResources().getString(R.string.tncc1)));

        SpannableString ss1 = new SpannableString(getResources().getString(R.string.tncc1));
        final ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Bundle bundle = new Bundle();
                bundle.putString("policy", "privacy");
                TNCFragment tncFragment = new TNCFragment();
                tncFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, tncFragment).commit();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tnc1.setText(ss1);
        tnc1.setMovementMethod(LinkMovementMethod.getInstance());
        /*countryCodeLayout.setVisibility(View.VISIBLE);
        textInputLayout2.setVisibility(View.GONE);*/
        ccp.setOnCountryChangeListener(() -> {
            if (ccp.getSelectedCountryCodeWithPlus().equals("+91")) {
                textInputLayout2.setVisibility(View.GONE);
                email_profile.setText("");
            }
            if (!ccp.getSelectedCountryCodeWithPlus().equals("+91")) {
                ToastUtil.showShortToast(getContext(), "Please Signup/Login with E-mail");
                textInputLayout2.setVisibility(View.VISIBLE);
                countryCodeLayout.setVisibility(View.GONE);
                phone.setText("");
            }
        });
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreference.getInstance(ctx).putString("android_id", android_id);
        device_model = getDeviceName();
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) mobileEditText.setText("");
        });
        mobileEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) emailEditText.setText("");
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_otp:
                wantsNormal = false;
                if (is_aws_sms_android) {
                    if (!mobileEditText.getText().toString().isEmpty() && !TextUtils.isEmpty(mobileEditText.getText().toString())) {
                        is_number = 1;

                        if (Utils.isConnectingToInternet(ctx)) {
                            networkCall = new NetworkCall(SignUpFragment.this, ctx);
                            networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                        } else
                            //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                            ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                    }

                    if (!emailEditText.getText().toString().isEmpty() && !TextUtils.isEmpty(emailEditText.getText().toString())) {
                        Log.d("shantanu", "email");
                        is_number = 2;
                        if (Utils.isEmailValid(emailEditText.getText().toString())) {
                            if (Utils.isConnectingToInternet(ctx)) {
                                networkCall = new NetworkCall(SignUpFragment.this, ctx);
                                networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                            } else
                                //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                                ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                        } else {
                            ToastUtil.showDialogBox(getContext(), "Invalid Email address");
                        }

                    }

                } else {
                    if (countryCodeLayout.isShown()) {
                        if (!phone.getText().toString().isEmpty()) {

                            is_number = 1;
                            if (validate()) {
                                if (Utils.isConnectingToInternet(ctx)) {
                                    networkCall = new NetworkCall(SignUpFragment.this, ctx);
                                    networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                                } else
                                    //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                            }
                        } else {
                            Toast.makeText(ctx, "Mobile Field is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (textInputLayout2.isShown()) {
                        if (!email_profile.getText().toString().isEmpty()) {
                            Log.d("shantanu", "email");
                            is_number = 2;
                            if (Utils.isEmailValid(email_profile.getText().toString())) {
                                if (Utils.isConnectingToInternet(ctx)) {
                                    networkCall = new NetworkCall(SignUpFragment.this, ctx);
                                    networkCall.NetworkAPICall(API.SIGN_UP, true, Networkconstants.MULTIPART);
                                } else
                                    //ToastUtil.showShortSnackBar(((LoginSignupActivity)ctx).containerLayout,Networkconstants.ERR_NETWORK_TIMEOUT);
                                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                            } else {
                                ToastUtil.showDialogBox(getContext(), "Invalid Email address");
                            }

                        } else {
                            Toast.makeText(ctx, "E-mail Field is Empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                number = is_number;
                break;
            case R.id.back:
                ((LoginSignupActivity) ctx).onBackPressed();
                break;

        }
    }

    private boolean validate() {
        Boolean flag = true;
        if (is_aws_sms_android) {
            if (TextUtils.isEmpty(mobileEditText.getText())) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            } else if (mobileEditText.getText().toString().length() < 10) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            } else if (mobileEditText.getText().toString().length() > 10) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            }
        } else {
            if (TextUtils.isEmpty(phone.getText())) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            } else if (phone.getText().toString().length() < 10) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            } else if (phone.getText().toString().length() > 10) {
                flag = false;
                ToastUtil.showDialogBox(ctx, "Please enter a valid phone number");
            }
        }
        return flag;
    }

    private Fragment getInstance() {
        if (fragment == null)
            fragment = new SignUpFragment();
        return fragment;
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
    public Builders.Any.M getAPI(String API_NAME) {
        Builders.Any.M ion = null;
        if (API_NAME.equals(API.SIGN_UP)) {

            if (is_aws_sms_android) {
                Log.d("shantanu", "Device_token" + device_token);

                Log.d("login_type", bundle.getString("login_type"));

                if (!wantsNormal) {
                    if (!mobileEditText.getText().toString().isEmpty()) {
                        is_number = 1;
                        ion = Ion.with(ctx).load(API_NAME)
                                .setHeader("Deviceid", android_id)
                                .setMultipartParameter("country_code", ccpNew.getSelectedCountryCodeWithPlus())
                                .setMultipartParameter("mobile", mobileEditText.getText().toString())
                                .setMultipartParameter("login_type", bundle.getString("login_type"))
                                .setMultipartParameter("login_with", String.valueOf(1))
                                .setMultipartParameter("source", "normal")
                                .setMultipartParameter("device_type", String.valueOf(1))
                                .setMultipartParameter("device_model", device_model)
                                .setMultipartParameter("device_token", device_token)
                                .setMultipartParameter("device_id", android_id);

                        data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                        data.setMobile(mobileEditText.getText().toString());
                        data.setLogin_type(bundle.getString("login_type"));
                    }
                    if (!emailEditText.getText().toString().isEmpty()) {
                        Log.d("shantanu", "email");
                        is_number = 2;
                        ion = Ion.with(ctx).load(API_NAME)
                                .setMultipartParameter("country_code", ccpNew.getSelectedCountryCodeWithPlus())
                                .setMultipartParameter("mobile", emailEditText.getText().toString())
                                .setMultipartParameter("login_type", bundle.getString("login_type"))
                                .setMultipartParameter("login_with", String.valueOf(2))
                                .setMultipartParameter("source", "normal")
                                .setMultipartParameter("device_type", String.valueOf(1))
                                .setMultipartParameter("device_model", device_model)
                                .setMultipartParameter("device_token", device_token)
                                .setMultipartParameter("device_id", android_id);

                        data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                        data.setMobile(emailEditText.getText().toString());
                        data.setLogin_type(bundle.getString("login_type"));
                    }
                } else {
                    if (!mobileEditText.getText().toString().isEmpty()) {
                        is_number = 1;
                        ion = Ion.with(ctx).load(API_NAME)
                                .setHeader("Deviceid", android_id)
                                .setMultipartParameter("country_code", ccpNew.getSelectedCountryCodeWithPlus())
                                .setMultipartParameter("mobile", mobileEditText.getText().toString())
                                .setMultipartParameter("login_type", bundle.getString("login_type"))
                                .setMultipartParameter("login_with", String.valueOf(1))
                                .setMultipartParameter("source", "normal")
                                .setMultipartParameter("login_with_otp", "1")
                                .setMultipartParameter("device_type", String.valueOf(1))
                                .setMultipartParameter("device_model", device_model)
                                .setMultipartParameter("device_token", device_token)
                                .setMultipartParameter("device_id", android_id);

                        data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                        data.setMobile(mobileEditText.getText().toString());
                        data.setLogin_type(bundle.getString("login_type"));
                    }
                    if (!emailEditText.getText().toString().isEmpty()) {
                        Log.d("shantanu", "email");
                        is_number = 2;
                        ion = Ion.with(ctx).load(API_NAME)
                                .setMultipartParameter("country_code", ccpNew.getSelectedCountryCodeWithPlus())
                                .setMultipartParameter("mobile", emailEditText.getText().toString())
                                .setMultipartParameter("login_type", bundle.getString("login_type"))
                                .setMultipartParameter("login_with", String.valueOf(1))
                                .setMultipartParameter("source", "normal")
                                .setMultipartParameter("login_with_otp", "1")
                                .setMultipartParameter("device_type", String.valueOf(1))
                                .setMultipartParameter("device_model", device_model)
                                .setMultipartParameter("device_token", device_token)
                                .setMultipartParameter("device_id", android_id);

                        data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                        data.setMobile(emailEditText.getText().toString());
                        data.setLogin_type(bundle.getString("login_type"));
                    }
                }

            } else {
                Log.d("shantanu", "Device_token" + device_token);

                Log.d("login_type", bundle.getString("login_type"));
                if (!phone.getText().toString().isEmpty()) {
                    Log.d("shantanu", "phone" + String.valueOf(FirebaseInstallations.getInstance().getId()));
                    is_number = 1;
                    ion = Ion.with(ctx).load(API_NAME)
                            .setMultipartParameter("country_code", ccp.getSelectedCountryCodeWithPlus())
                            .setMultipartParameter("mobile", phone.getText().toString())
                            .setMultipartParameter("login_type", bundle.getString("login_type"))
                            .setMultipartParameter("login_with", String.valueOf(1))
                            .setMultipartParameter("source", "normal")
                            .setMultipartParameter("device_type", String.valueOf(1))
                            .setMultipartParameter("device_model", device_model)
                            .setMultipartParameter("device_token", device_token)
                            .setMultipartParameter("device_id", android_id);

                    data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                    data.setMobile(phone.getText().toString());
                    data.setLogin_type(bundle.getString("login_type"));
                }
                if (!email_profile.getText().toString().isEmpty()) {
                    Log.d("shantanu", "email");
                    Log.d("shantanu", "phone" + String.valueOf(FirebaseInstallations.getInstance().getId()));
                    is_number = 2;
                    ion = Ion.with(ctx).load(API_NAME)
                            .setMultipartParameter("country_code", ccp.getSelectedCountryCodeWithPlus())
                            .setMultipartParameter("mobile", email_profile.getText().toString())
                            .setMultipartParameter("login_type", bundle.getString("login_type"))
                            .setMultipartParameter("source", "normal")
                            .setMultipartParameter("login_with", String.valueOf(2))
                            .setMultipartParameter("device_type", String.valueOf(1))
                            .setMultipartParameter("device_model", device_model)
                            .setMultipartParameter("device_token", device_token)
                            .setMultipartParameter("device_id", android_id);

                    data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                    data.setMobile(email_profile.getText().toString());
                    data.setLogin_type(bundle.getString("login_type"));
                }
            }

            if (bundle.getString("login_type").equals("1") || bundle.getString("login_type").equals("2")) {
                Log.d("shantanu", "phone" + FirebaseInstallations.getInstance().getId());
                ion.setMultipartParameter("username", bundle.getString("username"))
                        .setMultipartParameter("email", bundle.getString("email"))
                        .setMultipartParameter("name", bundle.getString("name"))
                        .setMultipartParameter("source", "normal")
                        .setMultipartParameter("device_type", "1")
                        .setMultipartParameter("device_token", device_token)
                        .setMultipartParameter("profile_picture", bundle.getString("profilePic"));//if profile pic gets added
                data.setEmail(bundle.getString("email"));
                data.setUsername(bundle.getString("username"));
                data.setName(bundle.getString("name"));
                data.setProfile_picture(bundle.getString("profilePic"));

            }
        } else if (API_NAME.equals(API.SEND_OTP)) {

            if (is_aws_sms_android) {
                ion = Ion.with(ctx).load(API_NAME)
                        .setMultipartParameter("mobile", ccp.getSelectedCountryCodeWithPlus() + mobileEditText.getText().toString().trim());

            } else {
                ion = Ion.with(ctx).load(API_NAME)
                        .setMultipartParameter("mobile", ccp.getSelectedCountryCodeWithPlus() + phone.getText().toString().trim());
            }

        }else if (API_NAME.equalsIgnoreCase(API.SEND_OTP_LOGIN_USERID)){
            if (!mobileEditText.getText().toString().isEmpty()) {
                ion = Ion.with(ctx).load(API_NAME)
                        .setMultipartParameter("mobile", mobileEditText.getText().toString())
                        .setMultipartParameter("otp", "");

                data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                data.setMobile(mobileEditText.getText().toString());
                data.setLogin_type(bundle.getString("login_type"));

            }
            if (!emailEditText.getText().toString().isEmpty()) {
                ion = Ion.with(ctx).load(API_NAME)
                        .setMultipartParameter("mobile", emailEditText.getText().toString())
                        .setMultipartParameter("otp", "");

                data.setCountry_code(ccp.getSelectedCountryCodeWithPlus());
                data.setMobile(emailEditText.getText().toString());
                data.setLogin_type(bundle.getString("login_type"));
            }
        }
        return ion;
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        return null;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {

        if (API_NAME.equals(API.SIGN_UP)) {
            if (is_aws_sms_android) {
                if (result.optBoolean("status")) {
                    Constants.NUMBER_WITH_COUNTRYCODE = ccp.getSelectedCountryCodeWithPlus() + mobileEditText.getText().toString().trim();
                    JSONObject jsonObject = result.optJSONObject("data");

                    if (jsonObject.optString("jwt") != null) {
                        Utils.JWT = jsonObject.optString("jwt");
                        SharedPreference.getInstance(getContext()).putString(Constants.JWT, jsonObject.optString("jwt"));
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);
                    if (!TextUtils.isEmpty(mobileEditText.getText().toString())) {
                        bundle.putString(Constants.MOBILE, mobileEditText.getText().toString());
                    }

                    if (!TextUtils.isEmpty(emailEditText.getText().toString())) {
                        bundle.putString(Constants.MOBILE, emailEditText.getText().toString());
                    }

                    Fragment fragment;

                    if (!wantsNormal) {
                        bundle.putString(Constants.PIN, jsonObject.optString("pin"));
                        bundle.putString(Constants.IS_FORGOT, "1");
                        if (jsonObject.optString("pin").equalsIgnoreCase("1"))
                            fragment = new PinVerifyFragment();
                        else fragment = new OtpFragment();

                    } else {
                        bundle.putString(Constants.WANTS_NORMAL, "1");
                        bundle.putString(Constants.PIN, jsonObject.optString("pin"));
                        fragment = new OtpFragment();
                    }
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();


                } else {
                    if (result.optString("error").equalsIgnoreCase("15001")) {
                        //<h1>Show device List</h1><br><h1>डिवाइस सूची दिखाएं<h1>

                        if (Utils.isConnectingToInternet(ctx)){
                            networkCall = new NetworkCall(SignUpFragment.this, ctx);
                            networkCall.NetworkAPICall(API.SEND_OTP_LOGIN_USERID, true, Networkconstants.MULTIPART);
                        }else{
                            Toast.makeText(ctx, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        ToastUtil.showDialogBox(ctx, result.optString("message"));
                    }


                }
            } else {
                if (result.optBoolean("status")) {
                    Constants.NUMBER_WITH_COUNTRYCODE = ccp.getSelectedCountryCodeWithPlus() + phone.getText().toString().trim();
                    JSONObject jsonObject = result.optJSONObject("data");

                    if (jsonObject.optString("jwt") != null) {
                        Utils.JWT = jsonObject.optString("jwt");
                        SharedPreference.getInstance(getContext()).putString(Constants.JWT, jsonObject.optString("jwt"));
                    }

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data);
                    if (!TextUtils.isEmpty(phone.getText().toString())) {
                        bundle.putString(Constants.MOBILE, phone.getText().toString());
                    }

                    if (!TextUtils.isEmpty(email_profile.getText().toString())) {
                        bundle.putString(Constants.MOBILE, email_profile.getText().toString());
                    }

                    bundle.putString(Constants.PIN, jsonObject.optString("pin"));
                    bundle.putString(Constants.IS_FORGOT, "1");

                    Fragment fragment;

                    if (jsonObject.optString("pin").equalsIgnoreCase("1"))
                        fragment = new PinVerifyFragment();
                    else fragment = new OtpFragment();

                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();
                } else {
                    ToastUtil.showDialogBox(ctx, result.optString("message"));
                }
            }
        } else if (API_NAME.equals(API.SEND_OTP)) {
            if (is_aws_sms_android) {
                String type = result.optString("type");
                Toast.makeText(getActivity(), type, Toast.LENGTH_SHORT).show();
                ((LoginSignupActivity) ctx).signupResponse = new Gson().fromJson(result.optJSONObject("data").toString(), SignupResponse.class);
                Log.d("otp", ((LoginSignupActivity) ctx).signupResponse.getOtp());
                Constants.NUMBER_WITH_COUNTRYCODE = ccp.getSelectedCountryCodeWithPlus() + mobileEditText.getText().toString().trim();
                Log.d("shantanu", String.valueOf(is_number));
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);

                Fragment fragment = new OtpFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();

            } else {
                String type = result.optString("type");
                Toast.makeText(getActivity(), type, Toast.LENGTH_SHORT).show();
                ((LoginSignupActivity) ctx).signupResponse = new Gson().fromJson(result.optJSONObject("data").toString(), SignupResponse.class);
                Log.d("otp", ((LoginSignupActivity) ctx).signupResponse.getOtp());
                Constants.NUMBER_WITH_COUNTRYCODE = ccp.getSelectedCountryCodeWithPlus() + phone.getText().toString().trim();
                Log.d("shantanu", String.valueOf(is_number));
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);

                Fragment fragment = new OtpFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();
            }
        } else if (API_NAME.equalsIgnoreCase(API.SEND_OTP_LOGIN_USERID)){
            if (result.optBoolean("status")){
                Bundle bundle = new Bundle();
                bundle.putString(Constants.VERIFY, "1");
                bundle.putString(Constants.WANTS_NORMAL, "1");
                bundle.putSerializable("data", data);
                Fragment fragment= new OtpFragment();

                fragment.setArguments(bundle);
                ToastUtil.showDialogBoxForDeviceList(ctx, "Device Limit Exceeded. Show Device List?", getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment));
            }else{
                Toast.makeText(ctx, result.optString("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void ErrorCallBack(String message, String API_NAME) {

    }

}
