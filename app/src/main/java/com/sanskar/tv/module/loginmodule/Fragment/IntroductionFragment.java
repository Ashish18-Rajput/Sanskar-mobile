package com.sanskar.tv.module.loginmodule.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
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
import com.sanskar.tv.module.loginmodule.Adapter.Intro_View_Pager_Adapter;
import com.sanskar.tv.module.loginmodule.Pojo.GuestUserResponse;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;
import com.sanskar.tv.module.loginmodule.activity.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by appsquadz on 1/19/18.
 */

public class IntroductionFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, NetworkCall.MyNetworkCallBack {

    private static ViewPager mPager;
    private static int NUM_PAGES = 0;
    private final int RC_SIGN_IN = 200;
    public GoogleApiClient mGoogleApiClient;
    LinearLayout google_fb_parent_layout, intro_or_line;
    TextSwitcher textSwitcher;
    ImageView introIndicator1, introIndicator2, introIndicator3, googleLogin;
    Context ctx;
    String[] strings;
    RelativeLayout facebookLogin;
    TextView getStarted;
    GoogleSignInOptions gso;
    GoogleSignInResult result;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    NetworkCall networkCall;
    private String login_type,username, name, mobile, profiilePic = "";
    String device_model, android_id, login_with, id, jwt, guestDeviceToken, device_token = "";
    TextView tnc, tnc1;
    CheckBox checkbox_terms, checkbox_policy;
    boolean guestUser = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction, null);
        ctx = getActivity();
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isComplete()) {
                    device_token = task.getResult();
                }
            }
        });

        initView(view);

        callbackManager = CallbackManager.Factory.create();
        faceboolLogin();
        android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreference.getInstance(ctx).putString("android_id", android_id);
        device_model = getDeviceName();
        return view;
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

    private void initView(View view) {
        tnc = view.findViewById(R.id.tnc);
        tnc1 = view.findViewById(R.id.tnc1);
        checkbox_terms = view.findViewById(R.id.checkbox_terms);
        checkbox_policy = view.findViewById(R.id.checkbox_policy);
        textSwitcher = view.findViewById(R.id.text_switcher_intro);
        introIndicator1 = view.findViewById(R.id.intro_indicator_1);
        introIndicator2 = view.findViewById(R.id.intro_indicator_2);
        introIndicator3 = view.findViewById(R.id.intro_indicator_3);
        getStarted = view.findViewById(R.id.getStarted);
        googleLogin = view.findViewById(R.id.googleLogin);
        facebookLogin = view.findViewById(R.id.facebookLogin);
        loginButton = view.findViewById(R.id.facebook_login_button);
        mPager = view.findViewById(R.id.text_switcher_view_pager);
        intro_or_line = view.findViewById(R.id.intro_or_line);
        google_fb_parent_layout = view.findViewById(R.id.google_fb_parent_layout);
        if (!SplashActivity.IS_GMAIL.equalsIgnoreCase("1") && !SplashActivity.IS_FACEBOOK.equalsIgnoreCase("1")) {
            google_fb_parent_layout.setVisibility(View.GONE);
            intro_or_line.setVisibility(View.GONE);
        }
        if (!SplashActivity.IS_GMAIL.equalsIgnoreCase("2")) {
            google_fb_parent_layout.setVisibility(View.VISIBLE);
            intro_or_line.setVisibility(View.VISIBLE);
        }
        if (!SplashActivity.IS_FACEBOOK.equalsIgnoreCase("2")) {
            google_fb_parent_layout.setVisibility(View.VISIBLE);
            intro_or_line.setVisibility(View.VISIBLE);
        }
        if (!SplashActivity.IS_GMAIL.equalsIgnoreCase("") && !SplashActivity.IS_FACEBOOK.equalsIgnoreCase("")) {
            if (SplashActivity.IS_GMAIL.equalsIgnoreCase("1")) {
                googleLogin.setVisibility(View.VISIBLE);
            } else {
                googleLogin.setVisibility(View.GONE);
            }
            if (SplashActivity.IS_FACEBOOK.equalsIgnoreCase("1")) {
                facebookLogin.setVisibility(View.VISIBLE);
            } else {
                facebookLogin.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getStarted.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);

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
                ds.linkColor = getResources().getColor(R.color.link_color);
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

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                        .enableAutoManage((LoginSignupActivity) ctx, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        strings = new String[]{getResources().getString(R.string.intro_text_1), getResources().getString(R.string.intro_text_2), getResources().getString(R.string.intro_text_3)};

        textSwitcher.setFactory(() -> {
            TextView t = new TextView(ctx);
            t.setGravity(Gravity.CENTER);
            t.setTextSize(18);
            return t;
        });

        slidImage();

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    introIndicator1.setImageResource(R.drawable.intro_indicator_selected);
                    introIndicator2.setImageResource(R.drawable.intro_indicator_unselected);
                    introIndicator3.setImageResource(R.drawable.intro_indicator_unselected);
                } else if (position == 1) {
                    introIndicator1.setImageResource(R.drawable.intro_indicator_unselected);
                    introIndicator2.setImageResource(R.drawable.intro_indicator_selected);
                    introIndicator3.setImageResource(R.drawable.intro_indicator_unselected);
                } else if (position == 2) {
                    introIndicator1.setImageResource(R.drawable.intro_indicator_unselected);
                    introIndicator2.setImageResource(R.drawable.intro_indicator_unselected);
                    introIndicator3.setImageResource(R.drawable.intro_indicator_selected);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mGoogleApiClient.isConnected()) {
            signOut();
        }
    }

    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.getStarted:
                if (checkbox_policy.isChecked() && checkbox_terms.isChecked()) {
                    guestUser = true;
                    if (Utils.isConnectingToInternet(ctx)) {
                        networkCall = new NetworkCall(this, ctx);
                        networkCall.NetworkAPICall(API.GUEST_USER_LOGIN, true);
                    } else
                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                } else {
                    ToastUtil.showDialogBox(getContext(), "Please Agree to Our Privacy Policy and Terms And Condition");
                }
                break;

            case R.id.googleLogin:
                if (Utils.isConnectingToInternet(ctx)) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
                break;

            case R.id.facebookLogin:
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(IntroductionFragment.this, Arrays.asList("public_profile,email,user_birthday,user_location"));
                break;
        }
    }


    //  Integration for  Google_______________________________________________________________________...

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                login_type = "2";
                username = account.getDisplayName();
                if (account.getEmail() != null) {
                    mobile = account.getEmail();
                    login_with = "2";
                }

                name = account.getGivenName();
                //id = account.getServerAuthCode();
                profiilePic = String.valueOf(account.getPhotoUrl());
                id = account.getId();
                Log.d("Gmail_info", mobile + name + id + profiilePic);

                fetchdata(true);
            } else
                ToastUtil.showShortSnackBar(((LoginSignupActivity) ctx).containerLayout, result.getStatus().getStatusMessage());
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtil.showShortSnackBar(((LoginSignupActivity) ctx).containerLayout, connectionResult.getErrorMessage());
    }

    private void slidImage() {
        mPager.setAdapter(new Intro_View_Pager_Adapter(getActivity(), strings));
        final float density = getResources().getDisplayMetrics().density;
        NUM_PAGES = strings.length;

    }

//    Integration for facebook______________________________________________________________________.....

    public void faceboolLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("AccessToken", (loginResult.getAccessToken()).toString());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("SIGNUP_FACEBOOK", object.toString());
                                getfacebookProfileInfo(object);

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture,gender,birthday,friends{name,id}");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("AccessToken", "");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("AccessToken", "");
            }
        });
    }

    private void getfacebookProfileInfo(JSONObject object) {
        try {
            Log.d("facebook_info", object.toString());
            String firstName = object.getString("name");
            String userId = object.optString("email");

            //  JSONObject jsonObject=object.optJSONObject("picture")
            String profileImageUrl = "";
            if (object.optJSONObject("picture").optJSONObject("data").optString("url") != null) {
                profileImageUrl = object.optJSONObject("picture").optJSONObject("data").optString("url");
                Log.d("shantanu", object.optJSONObject("picture").optJSONObject("data").optString("url"));
            }


            login_type = "1";
            name = firstName;
            mobile = userId;
            login_with = "2";
            profiilePic = profileImageUrl;
            id = object.optString("id");
            Log.d("Shantanu", object.optString("id"));

            fetchdata(true);

        } catch (JSONException e) {
            Log.d("facebook_info_err", e.toString());
            e.printStackTrace();
        }
    }

    public void fetchdata(Boolean b) {
        if (Utils.isConnectingToInternet(ctx)) {
            networkCall = new NetworkCall(this, ctx);
            networkCall.NetworkAPICall(API.SIGN_UP, b);
        } else
            ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
    }


    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;

        if (API_NAME.equals(API.GUEST_USER_LOGIN)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setMultipartParameter("mobile", "9811792554");
        }
        else if(API_NAME.equals(API.SIGN_UP)){
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setHeader("Deviceid", android_id)
                    .setMultipartParameter("mobile", mobile)
                    .setMultipartParameter("login_type", "2")
                    .setMultipartParameter("login_with", String.valueOf(1))
                    .setMultipartParameter("source", "normal")
                    .setMultipartParameter("device_type", String.valueOf(1))
                    .setMultipartParameter("device_model", device_model)
                    .setMultipartParameter("device_token", device_token)
                    .setMultipartParameter("device_id", android_id);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.GUEST_USER_LOGIN)) {
            if (jsonstring.optBoolean("status")) {
                PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, ((LoginSignupActivity) ctx).signupResponse);
                SharedPreference.getInstance(ctx).putString(Constants.GUEST_ID, jsonstring.optJSONObject("data").optString("id"));
                Intent intent = new Intent(ctx, HomeActivityy.class);
                intent.putExtra(Constants.FROM_GUEST, "1");
                ((LoginSignupActivity) ctx).finish();
                startActivity(intent);
            }
            else{
                if(jsonstring.optString("error").equalsIgnoreCase("999999")) {
                    Utils.clearUserData(ctx);
                    Intent intent = new Intent(ctx, HomeActivityy.class);
                    intent.putExtra(Constants.FROM_GUEST, "1");
                    ((LoginSignupActivity) ctx).finish();
                    startActivity(intent);
                   /* Intent i = new Intent(ctx, LoginSignupActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ctx.startActivity(i);*/
                    Log.d("shantanu", "after device detached");
                }
            }
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        Log.e("1234", "ErrorCallBack: "+jsonstring );
    }
}


