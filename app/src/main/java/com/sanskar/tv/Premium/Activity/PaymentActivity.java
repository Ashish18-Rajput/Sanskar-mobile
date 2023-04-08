package com.sanskar.tv.Premium.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Adapter.CouponAdapter;
import com.sanskar.tv.Premium.Adapter.PremiumPlanAdapter;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.Layer3Fragment;
import com.sanskar.tv.module.HomeModule.Pojo.Coupon;
import com.sanskar.tv.module.HomeModule.Pojo.Plans;
import com.sanskar.tv.module.HomeModule.Pojo.PromoCode;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

public class PaymentActivity extends AppCompatActivity implements NetworkCall.MyNetworkCallBack, PaymentResultListener {
    RadioButton first_radioButton, second_radioButton, third_radioButton;
    ImageView intro_indicator_1, intro_indicator_2, intro_indicator_3, intro_indicator_4;
    TextView intro_textView1, intro_textView2, intro_textView3, intro_textView4, signIn_textView, initial_amount, total_amount, bt_total_pay;
    AppCompatButton btn_proceed;
    RelativeLayout payment_layout, pay_layout, payment_done, promo_code_details_layout;
    public static String amount = "", plan_id = "", validity = "", coupon_applied = "", promo_code_applied = "";
    public static boolean isChecked = false;
    String pre_transaction_id = "", post_transaction_id = "", mobile = "", amount1 = "", amount_promo = "";
    View view1, view2, view3;
    Context context;
    public TextView coupon;

    private int countOfBackPressed = -1;
    public boolean is_coupon_available = false;

    int total_amount_pay;
    NetworkCall networkCall;
    HomeActivityy activity;
    SignupResponse signupResponse;
    LinearLayoutManager linearLayoutManager;
    PremiumPlanAdapter premiumPlanAdapter;
    RecyclerView recyclerView, recycler_Coupon;
    Plans plans;
    PromoCode promoCode;
    Coupon coupons;
    List<Coupon> couponList = new ArrayList<>();
    LinearLayout pay_layout_ll;
    List<Plans> plansList = new ArrayList<>();
    TextView firstTextView, secondTextView, thirdTextView, first_rupee, second_rupee, third_rupee, go_back_text, promo_code_apply;
    TextView promo_code_details_title, promo_code_details_type_text, promo_code_details_value_text, promo_code_details_duration_text, apply_promo_code;
    TextView back_promo_code;
    EditText promo_code_editText;
    RelativeLayout no_coupon_layout, promo_code_layout;
    CouponAdapter couponAdapter;
    String qr_code_user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        setContentView(R.layout.activity_payment);
        if (getIntent() != null) {
            if (getIntent().hasExtra("qr_code_user_id")) {
                qr_code_user_id = getIntent().getStringExtra("qr_code_user_id");
            } else {
                qr_code_user_id = signupResponse.getId();
            }
        } else {
            qr_code_user_id = signupResponse.getId();
        }
        context = this;
        coupon_applied = "";
        isChecked = false;
        fetchData();
        initViews();

        bt_total_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intialize_payment();
            }
        });
    }

    private void initViews() {
        first_radioButton = findViewById(R.id.first_radioButton);
        second_radioButton = findViewById(R.id.second_radioButton);
        third_radioButton = findViewById(R.id.third_radioButton);

        initial_amount = findViewById(R.id.initial_amount);
        coupon = findViewById(R.id.coupon);

        coupon.setOnClickListener(v -> {
            countOfBackPressed = 2;
            GET_COUPON();
            pay_layout_ll.setVisibility(View.GONE);
            promo_code_layout.setVisibility(View.VISIBLE);
            recycler_Coupon.setVisibility(View.VISIBLE);
        });

        total_amount = findViewById(R.id.total_amount);
        pay_layout = findViewById(R.id.pay_layout);
        pay_layout.setVisibility(View.GONE);
        bt_total_pay = findViewById(R.id.bt_total_pay);

        intro_indicator_1 = findViewById(R.id.intro_indicator_1);
        intro_indicator_2 = findViewById(R.id.intro_indicator_2);
        intro_indicator_3 = findViewById(R.id.intro_indicator_3);
        intro_indicator_4 = findViewById(R.id.intro_indicator_4);

        intro_textView1 = findViewById(R.id.intro_textView1);
        intro_textView2 = findViewById(R.id.intro_textView2);
        intro_textView3 = findViewById(R.id.intro_textView3);
        intro_textView4 = findViewById(R.id.intro_textView4);

        btn_proceed = findViewById(R.id.btn_proceed);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);

        firstTextView = findViewById(R.id.firstTextView);
        secondTextView = findViewById(R.id.secondTextView);
        thirdTextView = findViewById(R.id.thirdTextView);

        pay_layout_ll = findViewById(R.id.pay_layout_ll);
        promo_code_details_layout = findViewById(R.id.promo_code_details_layout);

        first_rupee = findViewById(R.id.first_rupee);
        second_rupee = findViewById(R.id.second_rupee);
        third_rupee = findViewById(R.id.third_rupee);

        recyclerView = findViewById(R.id.recyclerView);
        recycler_Coupon = findViewById(R.id.recycler_Coupon);
        no_coupon_layout = findViewById(R.id.no_coupon_layout);
        go_back_text = findViewById(R.id.go_back_text);

        promo_code_layout = findViewById(R.id.promo_code_layout);

        go_back_text.setOnClickListener(v -> {
            pay_layout_ll.setVisibility(View.VISIBLE);
            no_coupon_layout.setVisibility(View.GONE);
            promo_code_layout.setVisibility(View.GONE);
        });

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recycler_Coupon.setLayoutManager(new LinearLayoutManager(context));

        apply_promo_code = findViewById(R.id.apply_promo_code);
        promo_code_details_title = findViewById(R.id.promo_code_details_title);
        promo_code_details_type_text = findViewById(R.id.promo_code_details_type_text);
        promo_code_details_value_text = findViewById(R.id.promo_code_details_value_text);
        promo_code_details_duration_text = findViewById(R.id.promo_code_details_duration_text);
        back_promo_code = findViewById(R.id.back_promo_code);

        payment_layout = findViewById(R.id.payment_layout);
        payment_layout.setVisibility(View.VISIBLE);
        payment_done = findViewById(R.id.payment_done);
        payment_done.setVisibility(View.GONE);
        view1.setBackgroundResource(R.color.unselected);
        promo_code_apply = findViewById(R.id.promo_code_apply);
        promo_code_editText = findViewById(R.id.promo_code_editText);
        promo_code_apply.setOnClickListener(v -> {
            if (promo_code_editText.getText().toString().isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Enter Promo Code first", Toast.LENGTH_SHORT).show();
                promo_code_editText.setError("blank");
            } else {
                Validate_Promo_code(true);
            }
        });

        btn_proceed.setOnClickListener(v -> {
            if (!isChecked) {
                Toast.makeText(context, "Please Select any option", Toast.LENGTH_SHORT).show();
            } else {
                coupon_applied = "";
                promo_code_applied = "";
                countOfBackPressed = 1;
                view1.setBackgroundResource(R.color.selected);
                if (SharedPreference.getInstance(context).getBoolean(Constants.LOGIN_SESSION)) {

                    intro_indicator_2.setImageResource(R.drawable.intro_indicator_selected);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intro_textView2.setTextColor(context.getColor(R.color.selected));
                    }
                    view2.setBackgroundResource(R.color.selected);
                    intro_indicator_3.setImageResource(R.drawable.intro_indicator_selected);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intro_textView3.setTextColor(context.getColor(R.color.selected));
                    }
                    payment_layout.setVisibility(View.GONE);
                    pay_layout.setVisibility(View.VISIBLE);
                    amount1 = amount;
                    amount_promo = amount;
                    if (plansList.get(0).getCurrency().equalsIgnoreCase("INR")){
                        initial_amount.setText("\u20B9 " + amount);
                        total_amount.setText("\u20B9 " + amount);
                    }

                    if (plansList.get(0).getCurrency().equalsIgnoreCase("USD")){
                        initial_amount.setText("$ " + amount);
                        total_amount.setText("$ " + amount);
                    }

                    total_amount_pay = Math.round(Float.parseFloat(amount) * 100);
                } else {
                    showDialogBox();
                }
            }
        });


        apply_promo_code.setOnClickListener(v -> {
            if (promoCode != null) {
                coupon.setText("PromoCode applied");
                if (promoCode.getPromocodeType().equalsIgnoreCase("1")) {
                    if (Integer.parseInt(amount_promo) < Integer.parseInt(promoCode.getPromocodeValue())) {
                        ToastUtil.showDialogBox(context, "Can't apply this coupon");
                    } else {
                        int amount = Integer.parseInt(amount_promo) - Integer.parseInt(promoCode.getPromocodeValue());
                        UpdateTotalAmount(amount);
                    }

                }
                if (promoCode.getPromocodeType().equalsIgnoreCase("2")) {
                    int amount1 = Integer.parseInt(amount_promo) * Integer.parseInt(promoCode.getPromocodeValue()) / 100;
                    int amount = Integer.parseInt(amount_promo) - amount1;
                    UpdateTotalAmount(amount);
                }
            }
        });

        back_promo_code.setOnClickListener(v -> {
            if (is_coupon_available) {
                recycler_Coupon.setVisibility(View.VISIBLE);
                no_coupon_layout.setVisibility(View.GONE);
            } else {
                no_coupon_layout.setVisibility(View.VISIBLE);
                recycler_Coupon.setVisibility(View.GONE);
            }
            promo_code_details_layout.setVisibility(View.GONE);
            promo_code_editText.setText("");
            countOfBackPressed = 2;
        });

    }

    private void Validate_Promo_code(boolean b) {
        if (Utils.isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.GET_PROMO_CODE, b);
        } else {
            Toast.makeText(context, Networkconstants.ERR_NETWORK_TIMEOUT, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        /* view1.setBackgroundResource(R.color.unselected);
        intro_indicator_2.setImageResource(R.drawable.intro_indicator_unselected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intro_textView2.setTextColor(context.getColor(R.color.unselected));
        }*/

        //coupon_applied="";

    }

    @Override
    public void onBackPressed() {
        if (countOfBackPressed == 1) {
            pay_layout.setVisibility(View.GONE);
            payment_layout.setVisibility(View.VISIBLE);
            countOfBackPressed = -1;
            view1.setBackgroundResource(R.color.unselected);
            intro_indicator_2.setImageResource(R.drawable.intro_indicator_unselected);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intro_textView2.setTextColor(context.getColor(R.color.unselected));
            }
            view2.setBackgroundResource(R.color.unselected);
            intro_indicator_3.setImageResource(R.drawable.intro_indicator_unselected);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intro_textView3.setTextColor(context.getColor(R.color.unselected));
            }

            if (!coupon.getText().toString().isEmpty()) {
                coupon.setText("");
            }

        } else if (countOfBackPressed == 2) {
            pay_layout_ll.setVisibility(View.VISIBLE);
            promo_code_layout.setVisibility(View.GONE);
            recycler_Coupon.setVisibility(View.GONE);
            no_coupon_layout.setVisibility(View.GONE);
            countOfBackPressed = 1;
        } else if (countOfBackPressed == 3) {
            if (is_coupon_available) {
                recycler_Coupon.setVisibility(View.VISIBLE);
                no_coupon_layout.setVisibility(View.GONE);
            } else {
                no_coupon_layout.setVisibility(View.VISIBLE);
                recycler_Coupon.setVisibility(View.GONE);
            }
            promo_code_details_layout.setVisibility(View.GONE);
            promo_code_editText.setText("");
            countOfBackPressed = 2;
        } else {
            super.onBackPressed();
        }

    }

    private void showDialogBox() {
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnCancelListener(dialog1 -> onResume());
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.dialogbox_language);
        Window window = dialog.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        intro_indicator_2.setImageResource(R.drawable.intro_indicator_unselected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intro_textView2.setTextColor(context.getColor(R.color.unselected));
        }
        final EditText fillintheblanks = dialog.findViewById(R.id.fillintheblanks);
        Button btn_proceed1 = dialog.findViewById(R.id.btn_proceed1);


        btn_proceed1.setOnClickListener(v -> {
            if (fillintheblanks.getText().toString().isEmpty()) {

                fillintheblanks.setError("Enter mobile no.");
            } else if (fillintheblanks.getText().toString().length() < 10) {
                Toast.makeText(context, "Mobile no. is less than 10 digits", Toast.LENGTH_SHORT).show();
            } else if (fillintheblanks.getText().toString().length() > 10) {
                Toast.makeText(context, "Mobile no. is greater than 10 digits", Toast.LENGTH_SHORT).show();
            } else {
                mobile = fillintheblanks.getText().toString();
                dialog.dismiss();
                showDialogBox1();
                Toast.makeText(context, "Mobile no.: " + mobile, Toast.LENGTH_SHORT).show();
            }

        });
        if (!fillintheblanks.getText().toString().isEmpty()) {
            fillintheblanks.setText("");
        } else {
            fillintheblanks.setText(mobile);
        }

        dialog.show();
    }

    private void showDialogBox1() {
        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setOnCancelListener(dialog -> showDialogBox());
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language1);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        intro_indicator_2.setImageResource(R.drawable.intro_indicator_selected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intro_textView2.setTextColor(context.getColor(R.color.selected));
        }


        Button btn_proceed1 = dialog1.findViewById(R.id.btn_proceed1);
        btn_proceed1.setOnClickListener(v -> {

            dialog1.dismiss();

            view2.setBackgroundResource(R.color.selected);
            intro_indicator_3.setImageResource(R.drawable.intro_indicator_selected);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                intro_textView3.setTextColor(context.getColor(R.color.selected));
            }
            payment_layout.setVisibility(View.GONE);

        });

        dialog1.show();
    }

    public void fetchData() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.GET_PREMIUM_PLAN, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void Intialize_payment() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.INITIALIZE_TRANSACTION, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void Complete_transaction() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.COMPLETE_TRANSACTION, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    public void GET_COUPON() {
        if (isConnectingToInternet(context)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.GET_COUPON, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;

        if (apitype.equalsIgnoreCase(API.INITIALIZE_TRANSACTION)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", qr_code_user_id)
                    .setMultipartParameter("plan_id", plan_id)
                    .setMultipartParameter("amount", amount1)
                    .setMultipartParameter("pay_via", "0")
                    .setMultipartParameter("device_type", "1")
                    .setMultipartParameter("validity", validity)
                    .setMultipartParameter("currency", plansList.get(0).getCurrency())
                    .setMultipartParameter("coupon_applied", coupon_applied)
                    .setMultipartParameter("promocode_applied", promo_code_applied)
                    .setMultipartParameter("promocode", promo_code_editText.getText().toString());
        }
        if (apitype.equalsIgnoreCase(API.COMPLETE_TRANSACTION)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", qr_code_user_id)
                    .setMultipartParameter("pre_transaction_id", pre_transaction_id)
                    .setMultipartParameter("post_transaction_id", post_transaction_id)
                    .setMultipartParameter("plan_id", plan_id);
        }
        if (apitype.equalsIgnoreCase(API.GET_PREMIUM_PLAN)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", qr_code_user_id);
        }
        if (apitype.equalsIgnoreCase(API.GET_COUPON)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", qr_code_user_id);
        }

        if (apitype.equalsIgnoreCase(API.GET_PROMO_CODE)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setMultipartParameter("user_id", qr_code_user_id)
                    .setMultipartParameter("promocode", promo_code_editText.getText().toString());
        }


        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {

        if (apitype.equalsIgnoreCase(API.GET_PREMIUM_PLAN)) {
            if (result.optBoolean("status")) {
                JSONArray jsonArray = result.optJSONArray("data");

                if (plansList.size() > 0) {
                    plansList.clear();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    plans = new Gson().fromJson(jsonArray.opt(i).toString(), Plans.class);
                    plansList.add(plans);
                }


                premiumPlanAdapter = new PremiumPlanAdapter(context, plansList);
                recyclerView.setAdapter(premiumPlanAdapter);
            } else {
                ToastUtil.showDialogBox(context, result.optString("message"));
            }
        }
        if (apitype.equalsIgnoreCase(API.INITIALIZE_TRANSACTION)) {
            if (result.optBoolean("status")) {
                JSONObject jsonObject = result.optJSONObject("data");
                if (jsonObject != null) {
                    pre_transaction_id = jsonObject.optString("pre_transaction_id");
                }
                Checkout checkout = new Checkout();
                checkout.setKeyID(/*"rzp_test_wlwMtEPTnFcCm2"*/"rzp_live_PK0RcDLwVflaxC");
                checkout.setImage(R.mipmap.splash_logo);
                JSONObject object = new JSONObject();
                try {
                    object.put("name", "Sanskar TV Info Pvt. Ltd.");
                    object.put("description", "Payment");
                    object.put("theme.color", "#F9AA31");
                    object.put("description", "Payment");
                    object.put("currency", plansList.get(0).getCurrency());
                    object.put("amount", total_amount_pay);
                    object.put("prefill.contact", signupResponse.getMobile());

                    checkout.open(PaymentActivity.this, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ToastUtil.showDialogBox(context, result.optString("message"));
            }
        }
        if (apitype.equalsIgnoreCase(API.GET_COUPON)) {
            if (result.optBoolean("status")) {
                JSONArray jsonArray = result.optJSONArray("data");
                if (couponList.size() > 0) {
                    couponList.clear();
                }
                is_coupon_available = true;
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        coupons = new Gson().fromJson(jsonArray.opt(i).toString(), Coupon.class);
                        couponList.add(coupons);
                    }
                    couponAdapter = new CouponAdapter(context, couponList);
                    recycler_Coupon.setAdapter(couponAdapter);
                } else {

                }

            } else {
                is_coupon_available = false;
                //  ToastUtil.showDialogBox(context, result.optString("message"));
                promo_code_layout.setVisibility(View.VISIBLE);
                recycler_Coupon.setVisibility(View.GONE);
                no_coupon_layout.setVisibility(View.VISIBLE);
            }
        }
        if (apitype.equalsIgnoreCase(API.COMPLETE_TRANSACTION)) {
            if (result.optBoolean("status")) {
                final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // dialog1.setCancelable(true);
                dialog1.setCancelable(false);
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.setContentView(R.layout.dialogbox_language2);
                Window window = dialog1.getWindow();
                window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

                TextView textView = dialog1.findViewById(R.id.message);

                textView.setText(result.optString("message"));

                Button btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
                btn_proceed1.setOnClickListener(v -> {
                    dialog1.dismiss();
                    pay_layout.setVisibility(View.GONE);
                    view3.setBackgroundResource(R.color.selected);
                    intro_indicator_4.setImageResource(R.drawable.intro_indicator_selected);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        intro_textView4.setTextColor(context.getColor(R.color.selected));
                    }
                    payment_done.setVisibility(View.VISIBLE);
                    Layer3Fragment.is_premium = 0;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(PaymentActivity.this, HomeActivityy.class);
                            startActivity(intent);
                        }
                    }, 1000);
                });

                dialog1.show();
            } else {
                Layer3Fragment.is_premium = 1;
                ToastUtil.showDialogBox(context, result.optString("message"));
            }
        }
        if (apitype.equalsIgnoreCase(API.GET_PROMO_CODE)) {
            if (result.optBoolean("status")) {
                coupon_applied = "";
                promo_code_details_layout.setVisibility(View.VISIBLE);
                countOfBackPressed = 3;
                no_coupon_layout.setVisibility(View.GONE);
                recycler_Coupon.setVisibility(View.GONE);
                JSONObject jsonObject = result.optJSONObject("data");
                promoCode = new Gson().fromJson(jsonObject.toString(), PromoCode.class);
                promo_code_details_title.setText(promoCode.getTitle());
                if (promoCode.getPromocodeType().equalsIgnoreCase("1")) {
                    promo_code_details_type_text.setText("Flat");
                    promo_code_details_value_text.setText("Rs " + promoCode.getPromocodeValue());
                }
                if (promoCode.getPromocodeType().equalsIgnoreCase("2")) {
                    promo_code_details_type_text.setText("Percent");
                    promo_code_details_value_text.setText(promoCode.getPromocodeValue() + " %");
                }
                promo_code_details_duration_text.setText(promoCode.getEnd());
                promo_code_applied = promoCode.getId();

            } else {
                ToastUtil.showDialogBox(context, result.optString("message"));
            }
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {

    }

    public void UpdateTotalAmount(int amo) {
        promo_code_layout.setVisibility(View.GONE);
        recycler_Coupon.setVisibility(View.GONE);
        pay_layout_ll.setVisibility(View.VISIBLE);
        promo_code_details_layout.setVisibility(View.GONE);

        countOfBackPressed = 1;

        Log.d("shantanu", plan_id);
        amount1 = String.valueOf(amo);
        if (plansList.get(0).getCurrency().equalsIgnoreCase("INR")){
            total_amount.setText("\u20B9 " + amo);
        }
        if (plansList.get(0).getCurrency().equalsIgnoreCase("USD")){
            total_amount.setText("$ " + amo);
        }
        total_amount_pay = Math.round(Float.parseFloat(String.valueOf(amo)) * 100);
    }


    @Override
    public void onPaymentSuccess(String s) {
        /*ToastUtil.showDialogBox(context,s);*/
        post_transaction_id = s;
        Complete_transaction();
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            ToastUtil.showDialogBox(context, jsonObject.optJSONObject("error").optString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}