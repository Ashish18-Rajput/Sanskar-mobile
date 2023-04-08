package com.sanskar.tv.Others.Helper.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public  static String refreshedToken;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        refreshedToken = s;
        Log.i(Constants.DEVICE_TOKEN, refreshedToken);
        storeRegIdInPref(refreshedToken);
    }


    private void storeRegIdInPref(String token) {
        PreferencesHelper
                .getInstance()
                .setValue(Constants.DEVICE_TOKEN, token);
    }
}