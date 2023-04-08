package com.sanskar.tv.module.loginmodule.activity;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.RelativeLayout;

import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.module.loginmodule.Fragment.IntroductionFragment;
import com.sanskar.tv.module.loginmodule.Fragment.OtpFragment;
import com.sanskar.tv.module.loginmodule.Fragment.SignUpFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.network.NetworkCall;
import com.sanskar.tv.R;

import java.util.Objects;

public class LoginSignupActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    public RelativeLayout containerLayout;
    public SignupResponse signupResponse = new SignupResponse();
    String data;
    boolean otp_frag = false;
    public String message;
    private NetworkCall networkCall;
    private long backPressed;
    String mobile = "", pin = "";
    boolean fromGuest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        Intent intent = getIntent();

        data = intent.getStringExtra("data");

        if (!Objects.equals(intent.getStringExtra("otp_frag"), "1")) {
            otp_frag = false;
        } else {
            otp_frag = true;
        }

        if (!Objects.equals(intent.getStringExtra(Constants.FROM_GUEST), "1")) {
            fromGuest = false;
        } else {
            fromGuest = true;
        }

        if (intent.getStringExtra(Constants.MOBILE) != null)
            mobile = intent.getStringExtra(Constants.MOBILE);

        if (intent.getStringExtra(Constants.PIN) != null)
            pin = intent.getStringExtra(Constants.PIN);

        if (intent.getStringExtra(Constants.IS_FORGOT) != null)
            pin = intent.getStringExtra(Constants.IS_FORGOT);

/*    ActionBar actionBar = getSupportActionBar();
    actionBar.hide();*/
        initView();


    }

    private void initView() {
        containerLayout = findViewById(R.id.container_layout_login);
        androidx.fragment.app.FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (fromGuest){
            Bundle bundle = new Bundle();
            bundle.putString("login_type", "0");
            Fragment fragment = new SignUpFragment();
            fragment.setArguments(bundle);
            transaction.add(R.id.container_layout_login, fragment, "Signup Fragment");
            transaction.commit();
        } else {
            if (!otp_frag) {
                transaction.add(R.id.container_layout_login, new IntroductionFragment(), "Introduction");
                transaction.commit();
            } else {
                transaction.add(R.id.container_layout_login, new OtpFragment(), "Otp Fragment");
                transaction.commit();
            }
        }
//    transaction.add(R.id.container_layout_login, new IntroductionFragment(), "Introduction");
//    transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isVpn(this)) {
            ToastUtil.showDialogBox3(this, "Please disable VPN");
        }
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container_layout_login) instanceof IntroductionFragment) {
            if (backPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                backPressed = System.currentTimeMillis();
                ToastUtil.showShortSnackBar(containerLayout, getResources().getString(R.string.pressAgain));
            }
        } else super.onBackPressed();
    }

}
