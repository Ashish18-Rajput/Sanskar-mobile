package com.sanskar.tv.Others.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Fragment.ActiveDevicesFragment;

public class ToastUtil {

    public static void showShortSnackBar(View view, String message) {
        if (message.contains("\"")) {
            message = message.replace("\"", "");
        }
        if (message.contains("[")) {
            message = message.replace("[", "");
        }
        if (message.contains("]")) {
            message = message.replace("]", "");
        }
        if (!message.equalsIgnoreCase(""))
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLongSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }



    public static String formatSeconds(int timeInSeconds)
    {
        if (timeInSeconds<0){
            return 00 + " h  " + 00 + " m  " + 00 + " s";
        }
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds / 3600);

        String HH = ((hours       < 10) ? "0" : "") + hours;
        String MM = ((minutes     < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        return HH + " h  " + MM + " m  " + SS + " s";
    }

    public static void showThreadShortToast(Context context, String message) {
        ((HomeActivityy) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialogBox(Context context, String message) {

        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language2);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);

        textView.setText(message);

        AppCompatButton btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        btn_proceed1.setOnClickListener(v -> dialog1.dismiss());

        dialog1.show();
    }


    public static void showDialogBox3(Context context, String message) {


        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language2);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);

        textView.setText(message);

        Button btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        btn_proceed1.setOnClickListener(v -> {
            dialog1.dismiss();
            ((Activity)context).finishAffinity();
        });

        dialog1.show();
    }
    public static void showDialogBoxForDeviceList(Context context, String message, FragmentTransaction otp_fragment) {


        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language2);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);

        textView.setText(message);

        Button btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        btn_proceed1.setOnClickListener(v -> {

            otp_fragment.commit();
            dialog1.dismiss();

        });

        dialog1.show();
    }

    public static void showDialogBoxForDetach(Context context, String message, Fragment fragment,int position) {


        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language_6);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);

        textView.setText(Html.fromHtml(message));

        AppCompatButton btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        AppCompatButton btn_proceed2 = dialog1.findViewById(R.id.btn_premium);
        btn_proceed1.setOnClickListener(v -> dialog1.dismiss());

        btn_proceed2.setOnClickListener(v -> {
            ((ActiveDevicesFragment)fragment).detachDevice(position);
            dialog1.dismiss();
        });

        dialog1.show();
    }

    public static void showDialogBox1(Context context, String message) {

        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language_4);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView=dialog1.findViewById(R.id.message);

        textView.setText(message);

        AppCompatButton btn_proceed1 = dialog1.findViewById(R.id.btn_Ok);
        AppCompatButton btn_proceed2 = dialog1.findViewById(R.id.btn_premium);
        btn_proceed1.setOnClickListener(v -> dialog1.dismiss());

        btn_proceed2.setOnClickListener(v -> {
            context.startActivity(new Intent(context,PaymentActivity.class));
            dialog1.dismiss();
        });

        dialog1.show();
    }

}
