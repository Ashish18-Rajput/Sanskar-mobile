package com.sanskar.tv.module.loginmodule.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.AES;
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
import com.sanskar.tv.module.HomeModule.Pojo.Version;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

public class SplashActivity extends AppCompatActivity implements NetworkCall.MyNetworkCallBack {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 10001;
    AlertDialog dialog;
    public static String IS_SOCIAL_LOGINN = "";
    public static String IS_GMAIL = "";
    public static String IS_FACEBOOK = "";
    boolean is_hard_update = false;
    NetworkCall networkCall;
    SignupResponse signupResponse;
    String source = "";
    String key = "";
    String mobile = "normal";
    String device_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Log.d("1234", "onCreatedataaa: " + getIntent().getData().toString());
        /*  // ZFMZx26Zs8zMP1MAFPu5FUmTyPtF8qDIGnnpoYntIAg=:MTIzNDU2Nzg5MDEyMzQ1Ng==
        // 3997394835965030

        String decrypttt = AES.decrypt("ZFMZx26Zs8zMP1MAFPu5FUmTyPtF8qDIGnnpoYntIAg=:MTIzNDU2Nzg5MDEyMzQ1Ng==", AES.generatekey("3997394835965030"),AES.generateVector("3997394835965030"));
        Toast.makeText(this, "Dec: " + decrypttt, Toast.LENGTH_SHORT).show();*/

        FirebaseApp.initializeApp(this);
        networkCall = new NetworkCall(this, this);
        if (PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class)!=null){
         signupResponse =  PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        }
        FirebaseCrashlytics.getInstance().setUserId(signupResponse!=null? signupResponse.getId() : "");
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        APP_IS_SOCIAL_LOGIN(false);

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sanskar.tv",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("Shantanu", "Fetching FCM registration token failed", task.getException());
                return;
            }

            // Get new FCM registration token
            device_token = task.getResult();
            device_token = task.getResult();

            SharedPreference.getInstance(this).putString(Constants.DEVICETOKEN, device_token);

            // Log and toast
            Log.d("Shantanu", "Device_token  " + device_token);

        });

//        checkDeepLink();
        checkAppPermissions();


    }

    private void checkDeepLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        if (deepLink.getBooleanQueryParameter("data", false)) {
                            String data1 = deepLink.getQueryParameter("data");
                            byte[] data = Base64.decode(data1, Base64.DEFAULT);
                            try {
                                String text = new String(data, "UTF-8");
                                deepLink = Uri.parse(API.BASE_URL + "?" + text);
                                Log.e("vibhu", "checkDeepLink: "+  deepLink );

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        if (getIntent()!=null){
            if (getIntent().getData()!=null){
                Uri data = getIntent().getData();
                if (data.getQueryParameter("qrLogin")!=null){
                    Toast.makeText(this, data.getQueryParameter("qrLogin"), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void GET_API_GET_APP_VERSION() {
        if (Utils.isVpn(this)){
            ToastUtil.showDialogBox3(this,"Please disable VPN");
        }else if(Utils.isDeviceRooted()){
            ToastUtil.showDialogBox3(this,"App doesn't run on Rooted Device");
        } else{
            if (isConnectingToInternet(this)) {
                NetworkCall networkCall = new NetworkCall(this, this);
                networkCall.NetworkAPICall(API.API_GET_APP_VERSION, false);
            } else {
                ToastUtil.showDialogBox(this, "No Internet Connection");
            }
        }
    }

    private void APP_IS_SOCIAL_LOGIN(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall.NetworkAPICall(API.IS_SOCIAL_LOGIN, b);
        } else {
            ToastUtil.showShortToast(this, "No Internet Connection");
        }
    }

    private void APP_LOGIN(boolean b) {
        if (isConnectingToInternet(this)) {
            networkCall.NetworkAPICall(API.SIGN_UP, b);
        } else {
            ToastUtil.showShortToast(this, "No Internet Connection");
        }
    }

    public void hashFromSHA1(String sha1) {
        String[] arr = sha1.split(":");
        byte[] byteArr = new byte[arr.length];

        for (int i = 0; i < arr.length; i++) {
            byteArr[i] = Integer.decode("0x" + arr[i]).byteValue();
        }

        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CALL_PHONE)) {
                GET_API_GET_APP_VERSION();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.INTERNET,
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.ACCESS_NETWORK_STATE,
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            GET_API_GET_APP_VERSION();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GET_API_GET_APP_VERSION();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage("Permission Required")
                        .setPositiveButton(R.string.allow, (dialog, id) -> {
                            // FIRE ZE MISSILES!
                            openPermissionScreen();
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            // User cancelled the dialog
                            dialog.dismiss();
                        });
                dialog = builder.show();
            }
            return;
        }
    }

    public void go_next(long millseconds) {
        new Handler().postDelayed(() -> {
            Intent intent = null;
            if (SharedPreference.getInstance(SplashActivity.this).getBoolean(Constants.LOGIN_SESSION)) {
                intent = new Intent(SplashActivity.this, HomeActivityy.class);
                if (getIntent().getData()!=null && !(getIntent().getData().getQueryParameterNames().contains("source"))){
                    intent.putExtra("OuterData",getIntent().getData().toString());
                }

                else if (getIntent().getData()!=null && getIntent().getData().getQueryParameterNames().contains("source")){
                    Log.d("shantanu","Query Parameter:  mobile :   "+getIntent().getData().getQueryParameter("user_id"));
                    SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,SignupResponse.class);
                    if (!getIntent().getData().getQueryParameter("user_id").contains(signupResponse.getMobile()))
                        ToastUtil.showDialogBox1(this,"Another User Already logged-in. Please Logout first and the try again.");
                }
            }
            else {
                if (getIntent().getData()==null )
                intent = new Intent(SplashActivity.this, LoginSignupActivity.class);
                else {
                    if (getIntent().getData()!=null){
                        Log.e("1234", "scanner dataaaa"+ getIntent().getData().toString() );
                        source = getIntent().getData().getQueryParameter("source");
                        key = getIntent().getData().getQueryParameter("key");
                        //mobile = getIntent().getData().getQueryParameter("user_id");
                        if (source == null || source.equalsIgnoreCase("")){
                            intent = new Intent(SplashActivity.this, LoginSignupActivity.class);
                        } else {
                            Log.d("shantanu",source);
                            Log.d("shantanu",key);
                            APP_LOGIN(true);
                        }
                    }
                }
            }
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }, millseconds);
    }


    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equalsIgnoreCase(API.IS_SOCIAL_LOGIN)) {
            ion = (Builders.Any.B) Ion.with(this).load(API.IS_SOCIAL_LOGIN)
                    .setMultipartParameter("device_type", "1");
        }else if (apitype.equalsIgnoreCase(API.SIGN_UP)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("country_code", "+91")
                    .setMultipartParameter("mobile", "")
                    .setMultipartParameter("login_type", "0")
                    .setMultipartParameter("source", source)
                    .setMultipartParameter("key", key)
                    .setMultipartParameter("login_with", String.valueOf(1))
                    .setMultipartParameter("device_type", String.valueOf(1))
                    .setMultipartParameter("device_model", getDeviceName())
                    .setMultipartParameter("device_token", device_token)
                    .setMultipartParameter("device_id", Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID));
        }
        else{
            ion = (Builders.Any.B) Ion.with(this).load(apitype);
        }
        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (apitype.equalsIgnoreCase(API.IS_SOCIAL_LOGIN)) {
            if (jsonstring.optBoolean("status")) {
                JSONObject jsonObject = jsonstring.optJSONObject("data");
                if (jsonObject != null) {
                    if (jsonObject.has("social_login")) {
                        IS_SOCIAL_LOGINN = jsonObject.optString("social_login");
                    }
                    IS_FACEBOOK = jsonObject.optString("is_facebook");
                    IS_GMAIL = jsonObject.optString("is_google");
                    Networkconstants.iso=jsonObject.optString("iso");
                }
            } else {
                Log.d("shantanu", "status false");
            }

        }else if (apitype.equalsIgnoreCase(API.SIGN_UP)){

            if (jsonstring.optBoolean("status")){
                JSONObject jsonObject = jsonstring.optJSONObject("data");

                if (jsonObject != null && jsonObject.has("jwt") && !TextUtils.isEmpty(jsonObject.optString("jwt"))) {
                    Utils.JWT = jsonObject.optString("jwt");
                    SharedPreference.getInstance(this).putString(Constants.JWT, jsonObject.optString("jwt"));
                }

                if (!TextUtils.isEmpty(source) && source.equalsIgnoreCase(jsonObject.optString("source"))){
                    SignupResponse signupResponse = new Gson().fromJson(jsonstring.optJSONObject("data").optJSONObject("user_record").toString(), SignupResponse.class);
                    PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
                    Intent intent = new Intent(this, HomeActivityy.class);
                    finish();
                    startActivity(intent);
                }else{
                    finish();
                    startActivity(new Intent(this,LoginSignupActivity.class));
                }

            }else ToastUtil.showDialogBox(this,jsonstring.optString("message"));


        } else{
            JSONObject jsonObject = jsonstring.optJSONObject("data");


            int versionCode = 0;
            if (jsonObject != null) {
              versionCode = Integer.parseInt(jsonObject.optString("android"));
                is_hard_update =  jsonObject.optString("is_hard_update_android").equalsIgnoreCase("1");
                SharedPreference.getInstance(SplashActivity.this).putString(Constants.AWS_SMS_ANDROID,jsonObject.optString(Constants.AWS_SMS_ANDROID));
            }


            if (versionCode != 0) {
                if (Utils.getVersionCode(this)<versionCode){
                    getVersionUpdateDialog(this);
                }else{
                    go_next(300);
                }
            }
        }
    }


    public void getVersionUpdateDialog(final Activity ctx) {
        androidx.appcompat.app.AlertDialog.Builder alertBuild = new androidx.appcompat.app.AlertDialog.Builder(ctx);
        alertBuild
                .setTitle("Sanskar")
                .setMessage("Available in a new version , you are using an old version")
                .setCancelable(false)
                .setPositiveButton("Update", (dialog, which) -> ctx.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()))))
                .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    dialog.dismiss();
                    if (is_hard_update)
                    ctx.finishAffinity();
                    else go_next(0);
                });
        androidx.appcompat.app.AlertDialog dialog = alertBuild.create();
        dialog.show();
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

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
    protected void onPause() {
        super.onPause();

    }
}