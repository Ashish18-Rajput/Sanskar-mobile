package com.sanskar.tv.ScannerView;

import static com.sanskar.tv.module.loginmodule.Fragment.SignUpFragment.android_id;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.goodiebag.pinview.Pinview;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.AES;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.network.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.ProfileFragment;
import com.sanskar.tv.module.loginmodule.Fragment.OtpFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler, NetworkCall.MyNetworkCallBack {

    ZXingScannerView scannerView;
    private NetworkCall networkCall;
    String pin = "";
    String isLoginQr, deviceQrKey, deviceType, deviceId, deviceModel, deviceToken = "";
    public SignupResponse signupResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }


    @Override
    public void handleResult(Result rawResult) {
        Networkconstants.scanner_text = rawResult.getText();


        try {

            isLoginQr = Networkconstants.scanner_text.split(";")[0];
            deviceQrKey = Networkconstants.scanner_text.split(";")[1];
            deviceType = Networkconstants.scanner_text.split(";")[2];
            deviceId = Networkconstants.scanner_text.split(";")[3];
            deviceToken = Networkconstants.scanner_text.split(";")[4];
            deviceModel = Networkconstants.scanner_text.split(";")[5];
        } catch (Exception e) {
            e.printStackTrace();
        }

        String user_id = "";
        try {
            String token = Networkconstants.scanner_text.split(":")[2];
            String normal = Networkconstants.scanner_text.split(":")[0];
            user_id = AES.decrypt(normal.split(":")[0], AES.generatekey(token), AES.generateVector(token));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isLoginQr.equalsIgnoreCase("1")) {
//            showDialogBox(this, "Please enter pin to verify.");
            if (Utils.isConnectingToInternet(this)) {
                networkCall = new NetworkCall(ScannerView.this, ScannerView.this);
                networkCall.NetworkAPICall(API.GET_QR_LOGIN_USER_DETAIL, true, Networkconstants.MULTIPART);
            } else
                ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        } else if (!TextUtils.isEmpty(user_id)) {
            startActivity(new Intent(this, PaymentActivity.class).putExtra("qr_code_user_id", user_id.split(":")[1]));
            finish();
        } else {
            Toast.makeText(this, "Use Valid Qr Code. Please Try Again Later", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }


    public void showDialogBox(Context context, String message) {

        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.qr_login_dialogbox);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = dialog1.findViewById(R.id.message);

        textView.setText(message);

        Button btn_proceed1 = dialog1.findViewById(R.id.btn_Yes);
        Button btn_proceed2 = dialog1.findViewById(R.id.btn_NO);
        Pinview pinview = dialog1.findViewById(R.id.pinview);



        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = pinview.getValue();
                if (Utils.isConnectingToInternet(context)) {
                    networkCall = new NetworkCall(ScannerView.this, ScannerView.this);
                    networkCall.NetworkAPICall(API.GET_QR_LOGIN_USER_DETAIL, true, Networkconstants.MULTIPART);
                } else
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);

                dialog1.dismiss();
            }
        });
        btn_proceed2.setOnClickListener(v -> dialog1.dismiss());

        dialog1.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public Builders.Any.M getAPI(String API_NAME) {
        Builders.Any.M ion = null;
        if (API_NAME.equals(API.GET_QR_LOGIN_USER_DETAIL)) {

            ion = (Builders.Any.M) Ion.with(this).load(API_NAME)
                    .setHeader("DeviceId",SharedPreference.getInstance(this).getString("android_id"))
                    .setHeader("userid",signupResponse.getId())
                    .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT)!=null?SharedPreference.getInstance(this).getString(Constants.JWT):Utils.JWT)
                    .setMultipartParameter(Constants.USER_ID, signupResponse.getId())
                    .setMultipartParameter("device_qr_key", deviceQrKey)
                    .setMultipartParameter("device_type", deviceType)
                    .setMultipartParameter("device_model", deviceModel)
                    .setMultipartParameter("device_id", deviceId)
                    .setMultipartParameter("device_token", deviceToken)
                    .setMultipartParameter(Constants.PIN, "1234");

        }
        return ion;
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        /*Builders.Any.B ion = null;
        if (apitype.equals(API.GET_QR_LOGIN_USER_DETAIL)) {

            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setHeader("DeviceId",deviceId)
                    .setMultipartParameter(Constants.USER_ID, signupResponse.getId())
                    .setMultipartParameter("device_qr_key", deviceQrKey)
                    .setMultipartParameter("device_type", deviceType)
                    .setMultipartParameter("device_model", deviceModel)
                    .setMultipartParameter("device_id", deviceId)
                    .setMultipartParameter("device_token", deviceToken)
                    .setMultipartParameter(Constants.PIN, pin);


        }*/
        return null;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String ApiName) throws JSONException {
        if (ApiName.equals(API.GET_QR_LOGIN_USER_DETAIL)){
            if (result.optBoolean("status")){
                Toast.makeText(this, result.optString(Constants.MESSAGES), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.optString(Constants.MESSAGES), Toast.LENGTH_SHORT).show();
            }
            ScannerView.this.finish();
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }
}