package com.sanskar.tv.module.loginmodule.Fragment;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.sanskar.tv.module.HomeModule.Fragment.ActiveDevicesFragment;
import com.sanskar.tv.module.HomeModule.Pojo.MainMenuMaster;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by appsquadz on 1/24/18.
 */

public class OtpFragment extends Fragment implements View.OnClickListener, NetworkCall.MyNetworkCallBack, View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    TextView submit, resendOtp, login_with_pin;
    ImageView back;
    Context ctx;
    SignupResponse data;
    private NetworkCall networkCall;
    Pinview pinview;
    private int TYPE = 1;
    String otptext = "", mobile = "";
    String deviceId = "";
    int is_number = 0;
    TextView textView_ll;
    String is_forgot;
    private AppEditText mPinFirstDigitEditText;
    private AppEditText mPinSecondDigitEditText;
    private AppEditText mPinThirdDigitEditText;
    private AppEditText mPinForthDigitEditText;
    private AppEditText mPinHiddenEditText;
    boolean wantsNormal = false;
    boolean verify = false;
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
        data = (SignupResponse) getArguments().getSerializable("data");
        if (getArguments().getString(Constants.MOBILE) != null) {
            mobile = getArguments().getString(Constants.MOBILE);
        }
        if (getArguments().getString(Constants.IS_FORGOT) != null) {
            is_forgot = getArguments().getString(Constants.IS_FORGOT);
        }
        if (getArguments().getString(Constants.WANTS_NORMAL) != null) {
            wantsNormal = getArguments().getString(Constants.WANTS_NORMAL).equalsIgnoreCase("1");
        }
        if (getArguments().getString(Constants.PIN) != null) {
            pin = getArguments().getString(Constants.PIN).equalsIgnoreCase("1");
        }
        if (getArguments().getString(Constants.VERIFY) != null) {
            verify = getArguments().getString(Constants.VERIFY).equalsIgnoreCase("1");
        }
        is_number = SignUpFragment.number;
        Log.d("shantanu", String.valueOf(is_number));
        initView(view);

        if (!pin){
            login_with_pin.setVisibility(View.GONE);
        } else {
            login_with_pin.setVisibility(View.VISIBLE);
        }

        if (SharedPreference.getInstance(ctx).getString("android_id") != null)
            deviceId = SharedPreference.getInstance(ctx).getString("android_id");

        return view;
    }

    private void initView(View view) {
        pinview = view.findViewById(R.id.fragment_otp_pinview);
        textView_ll = view.findViewById(R.id.textView_ll);
        if (is_number == 1) {
            textView_ll.setText(getResources().getString(R.string.otp_text)+ "  " + mobile);
        }
        if (is_number == 2) {
            textView_ll.setText(getResources().getString(R.string.otp_text_email));
        }
        mPinFirstDigitEditText = view.findViewById(R.id.OPT1ET);
        mPinSecondDigitEditText = view.findViewById(R.id.OPT2ET);
        mPinThirdDigitEditText = view.findViewById(R.id.OPT3ET);
        mPinForthDigitEditText = view.findViewById(R.id.OPT4ET);
        mPinHiddenEditText = view.findViewById(R.id.pin_hidden_edittext);


        submit = view.findViewById(R.id.submit);
        resendOtp = view.findViewById(R.id.resend_otp);
        login_with_pin = view.findViewById(R.id.login_with_pin);
        back = view.findViewById(R.id.back);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submit.setOnClickListener(this);
        back.setOnClickListener(this);

        setPINListeners();
        setFocus(mPinHiddenEditText);

        SpannableString ss = new SpannableString(getResources().getString(R.string.resed_otp));
        final ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (Utils.isConnectingToInternet(ctx)) {
                    networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(OtpFragment.this, ctx);
                    networkCall.NetworkAPICall(API.RESEND_VERIFICATION_OTP, true);
                } else
                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
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
        resendOtp.setHighlightColor(Color.TRANSPARENT);
        resendOtp.setMovementMethod(LinkMovementMethod.getInstance());



        SpannableString ssPin = new SpannableString(getResources().getString(R.string.login_with_pin));
        final ClickableSpan clickableSpanPin = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                Fragment fragment;
                fragment = new PinVerifyFragment();

                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();

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
        ssPin.setSpan(clickableSpanPin, 12, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_with_pin.setText(ssPin);
        login_with_pin.setHighlightColor(Color.TRANSPARENT);
        login_with_pin.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void fetchData() {
        if (isConnectingToInternet(getContext())) {
            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(OtpFragment.this, ctx);
            networkCall.NetworkAPICall(API.MENU_MASTER, false);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(getContext(), Networkconstants.ERR_NETWORK_TIMEOUT);
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                otptext = mPinHiddenEditText.getText().toString().trim();

                if (!verify){
                    if (Utils.isConnectingToInternet(ctx)) {
                        networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(OtpFragment.this, ctx);
                        networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);
                    } else
                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                }else{
                    if (Utils.isConnectingToInternet(ctx)) {
                        networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(OtpFragment.this, ctx);
                        networkCall.NetworkAPICall(API.SEND_OTP_LOGIN_USERID, true);
                    } else
                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                }


                break;


            case R.id.back:
                ((LoginSignupActivity) ctx).onBackPressed();
                break;
        }
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

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
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
        if (API_NAME.equalsIgnoreCase(API.MENU_MASTER)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setMultipartParameter("user_id", "");
        } else if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {
            if (!wantsNormal) {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("otp", otptext)
                        .setMultipartParameter("pin", "")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            } else {
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("otp", otptext)
                        .setMultipartParameter("login_with_otp", "1")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            }

        } else if (API_NAME.equals(API.RESEND_VERIFICATION_OTP)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setMultipartParameter("mobile", data.getMobile())
                    .setMultipartParameter("device_id", deviceId);
        } else if (API_NAME.equals(API.GET_USER_WITH_PROFILE_TOKEN)) {
//            Log.e("vibhujwt",SharedPreference.getInstance(ctx).getString(Constants.JWT)+" "+Utils.JWT +"user_id "+((HomeActivityy) ctx).signupResponse.getId());

            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setHeader("mobile", mobile)
                    .setMultipartParameter("login_type", "0")
                    .setMultipartParameter("device_id", deviceId);
        } else if (API_NAME.equalsIgnoreCase(API.SEND_OTP_LOGIN_USERID)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setMultipartParameter("mobile", data.getMobile())
                    .setMultipartParameter("otp", otptext);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {

        if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {
            if (result.optBoolean("status")) {
                mPinHiddenEditText.setText("");
                mPinFirstDigitEditText.setText("");
                mPinSecondDigitEditText.setText("");
                mPinThirdDigitEditText.setText("");
                mPinForthDigitEditText.setText("");
                if (!wantsNormal) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.OTP, otptext);
                    bundle.putSerializable("data", data);
                    bundle.putString(Constants.IS_FORGOT, is_forgot);
                    SetOtpFragment setOtpFragment = new SetOtpFragment();
                    setOtpFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().addToBackStack("Set_Pin_Fragment").replace(R.id.container_layout_login, setOtpFragment).commit();
                } else {
                    if (!verify) {
                        if (Utils.isConnectingToInternet(ctx)) {
                            networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(OtpFragment.this, ctx);
                            networkCall.NetworkAPICall(API.GET_USER_WITH_PROFILE_TOKEN, true);

                        } else
                            ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                    } else {
                        getFragmentManager().beginTransaction().addToBackStack("Set_Pin_Fragment").replace(R.id.container_layout_login, new ActiveDevicesFragment()).commit();
                    }

                }

            } else {
                Toast.makeText(ctx, result.optString(Constants.MESSAGES), Toast.LENGTH_SHORT).show();
            }

        } else if (API_NAME.equals(API.MENU_MASTER)) {
            MainMenuMaster mainMenuMaster;
            if (result.optBoolean("status")) {
                mainMenuMaster = new Gson().fromJson(result.toString(), MainMenuMaster.class);
                SharedPreference.getInstance(getActivity()).setMenuMaster(mainMenuMaster);
            }
        } else if (API_NAME.equals(API.GET_USER_WITH_PROFILE_TOKEN)) {
            if (result.optBoolean("status")) {
                ((LoginSignupActivity) ctx).signupResponse = new Gson().fromJson(result.optJSONObject("data").toString(), SignupResponse.class);
                SharedPreference.getInstance(getContext()).putBoolean(Constants.LOGIN_SESSION, true);
                PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, ((LoginSignupActivity) ctx).signupResponse);
                Intent intent = new Intent(ctx, HomeActivityy.class);
                ((LoginSignupActivity) ctx).finish();
                startActivity(intent);
            } else {
                ToastUtil.showShortToast(getContext(), result.optString("message"));
            }
        } else if (API_NAME.equals(API.RESEND_VERIFICATION_OTP)) {
            if (result.optBoolean("status")) {
                ToastUtil.showShortToast(getContext(), "Otp re-send Successfully");
            }else Toast.makeText(ctx, result.optString("message"), Toast.LENGTH_SHORT).show();
        }else if (API_NAME.equals(API.SEND_OTP_LOGIN_USERID)) {
            if (result.optBoolean("status")) {
                Bundle bundle = new Bundle();
                bundle.putString("id",result.optJSONObject("data").optString("id"));
                bundle.putString("activity","false");
                Fragment fragment = new ActiveDevicesFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container_layout_login, fragment).commit();
            }else Toast.makeText(ctx, result.optString("message"), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}
