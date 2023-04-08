package com.sanskar.tv.module.HomeModule.Fragment;

import static com.sanskar.tv.Others.Helper.Utils.isConnectingToInternet;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.Plans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PaymentFragment extends Fragment implements NetworkCall.MyNetworkCallBack {
    RadioButton first_radioButton, second_radioButton, third_radioButton;
    ImageView intro_indicator_1, intro_indicator_2, intro_indicator_3, intro_indicator_4;
    TextView intro_textView1, intro_textView2, intro_textView3, intro_textView4, signIn_textView;
    Button btn_proceed;
    RelativeLayout payment_layout;
    String amount = "", mobile = "";
    View view1, view2, view3;
    Context context;
    NetworkCall networkCall;
    HomeActivityy activity;
    Plans plans;
    List<Plans> plansList = new ArrayList<>();
    TextView firstTextView, secondTextView, thirdTextView, first_rupee, second_rupee, third_rupee;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = (HomeActivityy) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        networkCall = new NetworkCall(this, context);
        fetchData();

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        first_radioButton = view.findViewById(R.id.first_radioButton);
        second_radioButton = view.findViewById(R.id.second_radioButton);
        third_radioButton = view.findViewById(R.id.third_radioButton);

        intro_indicator_1 = view.findViewById(R.id.intro_indicator_1);
        intro_indicator_2 = view.findViewById(R.id.intro_indicator_2);
        intro_indicator_3 = view.findViewById(R.id.intro_indicator_3);
        intro_indicator_4 = view.findViewById(R.id.intro_indicator_4);

        intro_textView1 = view.findViewById(R.id.intro_textView1);
        intro_textView2 = view.findViewById(R.id.intro_textView2);
        intro_textView3 = view.findViewById(R.id.intro_textView3);
        intro_textView4 = view.findViewById(R.id.intro_textView4);

        btn_proceed = view.findViewById(R.id.btn_proceed);

        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);
        view3 = view.findViewById(R.id.view3);

        firstTextView = view.findViewById(R.id.firstTextView);
        secondTextView = view.findViewById(R.id.secondTextView);
        thirdTextView = view.findViewById(R.id.thirdTextView);

        first_rupee = view.findViewById(R.id.first_rupee);
        second_rupee = view.findViewById(R.id.second_rupee);
        third_rupee = view.findViewById(R.id.third_rupee);

        payment_layout = view.findViewById(R.id.payment_layout);
        view1.setBackgroundResource(R.color.unselected);
        first_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_radioButton.setChecked(false);
                third_radioButton.setChecked(false);
            }
        });

        second_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first_radioButton.setChecked(false);
                third_radioButton.setChecked(false);
            }
        });

        third_radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                second_radioButton.setChecked(false);
                first_radioButton.setChecked(false);
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!first_radioButton.isChecked() && !second_radioButton.isChecked() && !third_radioButton.isChecked()) {
                    Toast.makeText(context, "Please Select any option", Toast.LENGTH_SHORT).show();
                } else {
                    if (first_radioButton.isChecked()) {
                        amount = plansList.get(0).getAmount();

                    } else if (second_radioButton.isChecked()) {
                        amount = plansList.get(1).getAmount();

                    } else {
                        amount = plansList.get(2).getAmount();

                    }
                    view1.setBackgroundResource(R.color.selected);
                    showDialogBox();
                    Toast.makeText(context, "Amount: Rs" + amount, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        view1.setBackgroundResource(R.color.unselected);
        intro_indicator_2.setImageResource(R.drawable.intro_indicator_unselected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intro_textView2.setTextColor(context.getColor(R.color.unselected));
        }

    }

    private void showDialogBox() {
        final BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onResume();
            }
        });
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


        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        dialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showDialogBox();
            }
        });
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language1);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        intro_indicator_2.setImageResource(R.drawable.intro_indicator_selected);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intro_textView2.setTextColor(context.getColor(R.color.selected));
        }

        Button btn_proceed1 = dialog1.findViewById(R.id.btn_proceed1);
        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

                view2.setBackgroundResource(R.color.selected);
                intro_indicator_3.setImageResource(R.drawable.intro_indicator_selected);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intro_textView3.setTextColor(context.getColor(R.color.selected));
                }
                payment_layout.setVisibility(View.GONE);
            }
        });

        dialog1.show();
    }


    public void fetchData() {

        if (isConnectingToInternet(context)) {
            networkCall.NetworkAPICall(API.GET_PREMIUM_PLAN, true);
        } else {
            //swipeRefreshLayout.setRefreshing(false);
            ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
        }

    }


    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;

        if (apitype.equalsIgnoreCase(API.GET_PREMIUM_PLAN)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(getContext()).getString(Constants.JWT) != null ? SharedPreference.getInstance(getContext()).getString(Constants.JWT) : com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid", activity.signupResponse.getId())
                    .setMultipartParameter("user_id", activity.signupResponse.getId());
        }

        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String apitype) throws JSONException {

        if (result.optBoolean("status")) {
            JSONArray jsonArray = result.optJSONArray("data");

            if (plansList.size() > 0) {
                plansList.clear();
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                plans = new Gson().fromJson(jsonArray.opt(i).toString(), Plans.class);
                plansList.add(plans);
            }
            firstTextView.setText(plansList.get(0).getPlanName());
            secondTextView.setText(plansList.get(1).getPlanName());
            thirdTextView.setText(plansList.get(2).getPlanName());

            first_rupee.setText("Rs." + plansList.get(0).getAmount());
            second_rupee.setText("Rs." + plansList.get(1).getAmount());
            third_rupee.setText("Rs." + plansList.get(2).getAmount());

        } else {
            ToastUtil.showDialogBox(context, "Error");
        }

    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        //  ToastUtil.showDialogBox(context,jsonstring); //---by sumit
    }
}