package com.sanskar.tv.Others.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin1 on 19/2/18.
 */

public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            if (isOnline(context)) {
                //dialog(true);
            } else {
               // dialog(false);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public void showDialogBox(Context context,Boolean b){
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getResources().getString(R.string.app_name))
//                .setCancelable(false)
//                .show();
//        if(b){
//            builder.setMessage(R.string.internet_connected);
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                public void run() {
//                    builder.dismiss(); // when the task active then close the dialog
//                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                }
//            }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.
//
//        }
//        else
//            builder.setMessage(R.string.no_internet);
//
//    }

}