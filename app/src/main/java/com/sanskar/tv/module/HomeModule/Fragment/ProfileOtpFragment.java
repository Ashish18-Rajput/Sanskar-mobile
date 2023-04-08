package com.sanskar.tv.module.HomeModule.Fragment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
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
import com.mukesh.OtpView;
import com.sanskar.tv.CustomViews.AppEditText;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Fragment.OtpFragment;
import com.sanskar.tv.module.loginmodule.Fragment.SetOtpFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by appsquadz on 1/24/18.
 */

public class ProfileOtpFragment extends Fragment implements View.OnClickListener, com.sanskar.tv.Others.NetworkNew.NetworkCall.MyNetworkCallBack, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    TextView submit, resendOtp, login_with_pin;
    ImageView back;
    Context ctx;
    SignupResponse signupResponse;
    com.sanskar.tv.Others.NetworkNew.NetworkCall networkCall;

    String otptext = "", mobile = "", is_forgot = "";

    private AppEditText mPinFirstDigitEditText;
    private AppEditText mPinSecondDigitEditText;
    private AppEditText mPinThirdDigitEditText;
    private AppEditText mPinForthDigitEditText;
    private AppEditText mPinHiddenEditText;
    Pinview pinview;

    boolean pin = false;

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, null);
        ctx = getActivity();
        signupResponse = (SignupResponse) getArguments().getSerializable("data");

        if (getArguments().getString(Constants.MOBILE) != null) {
            mobile = getArguments().getString(Constants.MOBILE);
        }
        if (getArguments().getString(Constants.IS_FORGOT) != null) {
            is_forgot = getArguments().getString(Constants.IS_FORGOT);
        }

        if (getArguments().getString(Constants.PIN) != null) {
            pin = getArguments().getString(Constants.PIN).equalsIgnoreCase("1");
        }

        initView(view);
        return view;
    }

    private void initView(View view) {
//    otpView = view.findViewById(R.id.otp_view);
        submit = view.findViewById(R.id.submit);
        resendOtp = view.findViewById(R.id.resend_otp);
        login_with_pin = view.findViewById(R.id.login_with_pin);
        back = view.findViewById(R.id.back);
//        back.setVisibility(View.GONE);

        pinview = view.findViewById(R.id.fragment_otp_pinview);

        mPinFirstDigitEditText = view.findViewById(R.id.OPT1ET);
        mPinSecondDigitEditText = view.findViewById(R.id.OPT2ET);
        mPinThirdDigitEditText = view.findViewById(R.id.OPT3ET);
        mPinForthDigitEditText = view.findViewById(R.id.OPT4ET);
        mPinHiddenEditText = view.findViewById(R.id.pin_hidden_edittext);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        resendOtp.setText(Html.fromHtml(getResources().getString(R.string.resed_otp)));
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
//        ToastUtil.showShortToast(ctx, signupResponse.getOtp());

        setPINListeners();
        setFocus(mPinHiddenEditText);

        SpannableString ss = new SpannableString(getResources().getString(R.string.resed_otp));
        final ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //  startActivity(new Intent(MyActivity.this, NextActivity.class));
                if (Utils.isConnectingToInternet(ctx)) {
                    networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileOtpFragment.this, ctx);
                    networkCall.NetworkAPICall(API.PROFILE_UPDATE, true);
                } else
                    ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, Networkconstants.ERR_NETWORK_TIMEOUT);
            }

            @Override
            public CharacterStyle getUnderlying() {
                return super.getUnderlying();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);

            }
        };
        ss.setSpan(clickableSpan, 25, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        resendOtp.setText(ss);
        resendOtp.setMovementMethod(LinkMovementMethod.getInstance());
        resendOtp.setHighlightColor(Color.TRANSPARENT);
//        resendOtp.setHighlightColor(getResources().getColor(R.color.colorPrimary));
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

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) ctx).handleToolbar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                otptext = mPinHiddenEditText.getText().toString().trim();

                if (Utils.isConnectingToInternet(ctx)) {
                    networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileOtpFragment.this, ctx);
                    networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);
                } else
                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);

//                    if (Utils.isConnectingToInternet(ctx)) {
//                        networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileOtpFragment.this, ctx);
//                        networkCall.NetworkAPICall(API.SEND_OTP_LOGIN_USERID, true);
//                    } else
//                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
//
        /*if(otpView.hasValidOTP())
        {
          if(otpView.getOTP().equals(signupResponse.getOtp())){
            ToastUtil.showShortSnackBar(((HomeActivityy)ctx).container,"Profile Successfully Updated");
            Intent intent = new Intent(ctx, HomeActivityy.class);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN,signupResponse);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION,true);
            ((HomeActivityy)ctx).finish();
            startActivity(intent);
          }
          else
            ToastUtil.showShortSnackBar(((HomeActivityy)ctx).container,"Wrong OTP");
        }
        else
          ToastUtil.showShortSnackBar(((HomeActivityy)ctx).container,"Please enter a valid OTP");
        *//*
//                if (otpView.hasValidOTP()) {
                if (otptext.equals(signupResponse.getOtp())) {
                    if (Utils.isConnectingToInternet(ctx)) {

                        networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileOtpFragment.this, ctx);
                        networkCall.NetworkAPICall(API.OTP_VERIFY, true);
                    } else
                        ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, Networkconstants.ERR_NETWORK_TIMEOUT);


                } else
//                        ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Wrong OTP");
                    ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter a valid OTP");*/
//                } else
//                    ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter a valid OTP");

                break;
            case R.id.back:
                ((HomeActivityy) ctx).onBackPressed();
                break;
        }
    }


    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;
        if (API_NAME.equals(API.OTP_VERIFY)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setMultipartParameter("id", signupResponse.getId())
                    .setMultipartParameter("username", signupResponse.getUsername())
                    .setMultipartParameter("email", signupResponse.getEmail())
                    .setMultipartParameter("otp_verification", "1")
                    .setMultipartParameter("about", signupResponse.getAbout())
                    .setMultipartParameter("mobile", signupResponse.getMobile())
                    .setMultipartParameter("country_code", signupResponse.getCountry_code())
                    .setMultipartParameter("gender", signupResponse.getGender())
                    .setMultipartParameter("device_type", signupResponse.getDevice_type())
                    .setMultipartParameter("profile_picture", signupResponse.getProfile_picture());
        } else if (API_NAME.equals(API.PROFILE_UPDATE)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)

                    .setMultipartParameter("id", signupResponse.getId())
                    .setMultipartParameter("mobile", signupResponse.getMobile())
                    .setMultipartParameter("country_code", signupResponse.getCountry_code())
                    .setMultipartParameter("email", signupResponse.getEmail())
                    .setMultipartParameter("username", signupResponse.getUsername())
                    .setMultipartParameter("about", signupResponse.getAbout())
                    .setMultipartParameter("gender", signupResponse.getGender())
                    .setMultipartParameter("device_type", signupResponse.getDevice_type())
                    .setMultipartParameter("profile_picture", signupResponse.getProfile_picture());
        } else if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {

            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setMultipartParameter("mobile", signupResponse.getMobile())
                    .setMultipartParameter("otp", otptext)
                    .setMultipartParameter("pin", "")
                    .setMultipartParameter("device_id", SharedPreference.getInstance(ctx).getString("android_id"))
                    .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));

        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (API_NAME.equals(API.OTP_VERIFY)) {
            ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, result.optString("message"));
            Intent intent = new Intent(ctx, HomeActivityy.class);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION, true);
            ((HomeActivityy) ctx).finish();
            startActivity(intent);
        } else if (API_NAME.equals(API.PROFILE_UPDATE)) {
            if (result.optBoolean("status"))
                signupResponse = new Gson().fromJson(result.optString("data"), SignupResponse.class);
//            ToastUtil.showShortToast(ctx, signupResponse.getOtp());
      /*if(signupResponse.getOtp_verification().equals("0")){
        Bundle bundle = new Bundle();
        Fragment fragment= new ProfileOtpFragment();
        bundle.putSerializable("data",signupResponse);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_home,fragment).commit();
      */
        }
        if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {
            if (result.optBoolean("status")) {
                mPinHiddenEditText.setText("");
                mPinFirstDigitEditText.setText("");
                mPinSecondDigitEditText.setText("");
                mPinThirdDigitEditText.setText("");
                mPinForthDigitEditText.setText("");

                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.OTP, otptext);
                    bundle.putSerializable("data", signupResponse);
                    bundle.putString(Constants.IS_FORGOT, is_forgot);
                    SetOtpFragment setOtpFragment = new SetOtpFragment();
                    setOtpFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().addToBackStack("Set_Pin_Fragment").replace(R.id.container_layout_home, setOtpFragment).commit();

            } else {
                Toast.makeText(ctx, result.optString(Constants.MESSAGES), Toast.LENGTH_SHORT).show();
            }


        } else {
            Intent intent = new Intent(ctx, HomeActivityy.class);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION, true);
            ((HomeActivityy) ctx).finish();
            startActivity(intent);
        }
    }

    @Override
    public void ErrorCallBack(String message, String API_NAME) {
        ToastUtil.showDialogBox(ctx, message);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /*@Override
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
        }
    }*/

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
//
//                        if (mPinHiddenEditText.length() > 0)
//                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

//                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }
        return false;
    }
}
