package com.sanskar.tv.Others.Helper.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.R;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class MyFirebaseMessagingService1 extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "Sanskar-demo";

    private static final String TAG = MyFirebaseMessagingService1.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
/*        if (remoteMessage == null)
            return;

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getNotification().getBody().toString());
                Log.i("JSONDATA: ", json.toString());

                String deviceToken = PreferencesHelper
                        .getInstance()
                        .getStringValue(Constants.DEVICE_TOKEN, null);

                if (!TextUtils.isEmpty(deviceToken) && deviceToken != null) {
                    String image_url= remoteMessage.getData().get("image_url");

                    handleDataMessage1(json, image_url);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "BhajanCatData Payload: " + remoteMessage.getData().toString());
           *//* try {
                JSONObject json = new JSONObject(remoteMessage.getData().get("body").toString());
                Log.i("JSONDATA: ", json.toString());

                String deviceToken = PreferencesHelper
                        .getInstance()
                        .getStringValue(Constants.DEVICE_TOKEN, null);

                if (!TextUtils.isEmpty(deviceToken) && deviceToken != null) {
                    Log.i("DEVICE_TOKEN: ", deviceToken);
                    handleDataMessage(json);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }*//*



            try {
                if (remoteMessage.getData().containsKey(Constants.MESSAGES) && remoteMessage.getData().containsKey(Constants.TYPE)) {

                    JSONObject json = new JSONObject(remoteMessage.getData().get(Constants.MESSAGES).toString());
                    String notification_type;
                    if(remoteMessage.getData().get("notification_type").equalsIgnoreCase("URL")){
                      notification_type="General";
                    }else{
                     String   notification_type1=remoteMessage.getData().get("notification_type").toUpperCase();

                       String fist_letter= notification_type1.substring(0,1).toUpperCase();
                       String second_letter= notification_type1.substring(1,notification_type1.length()).toLowerCase();
                        notification_type=fist_letter + second_letter;

                    }
                       Log.d("notification_type",notification_type);
                    if(remoteMessage.getData().get("image_url")==null){
                        handleDataMessage1(json,notification_type);
                    }else {
                   String image_url= remoteMessage.getData().get("image_url");

                   if(image_url.equalsIgnoreCase("")){
                       handleDataMessage1(json,notification_type);
                   }else {
                       Log.d("checkImage", String.valueOf(image_url));
                       handleDataMessage(json, image_url,notification_type);
                   } }

                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }*/
        }

    private void handleNotification(String message) {
        // app is in foreground, broadcast the push message
        Log.i("Notification Msg: ", message);
        Intent pushNotification = new Intent();
        pushNotification.setAction("android.intent.action.MAIN");
        pushNotification.putExtra(Constants.MESSAGES, message);
       // pushNotification.putExtra(Constants.MESSAGES, message);
        sendBroadcast(pushNotification);

        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();

    }

    private void handleDataMessage(JSONObject json, String image_url,String notification_type) {
        createNotificationChannel();
        Intent intent = null;

        String push_type, id, isGoLive, message,url;
        url=image_url;
        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        if (json != null) {
          //  message = json.optString(Constants.MESSAGES);
             message = json.optString(Constants.MESSAGES);

            isGoLive = json.optString("go_live");

            signupResponse.setGo_live(isGoLive);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
//            push_type = json.optString(Constants.PUSH_TYPE);
//            id = json.optString(Constants.ID);
//            scheduleType = json.optString(Constants.SCHEDULE_TYPE);
//            System.out.print("MSG: " + json + " " + message + " " + " " + push_type + " " + id + " " + scheduleType);
            handleNotification(message);

            if (isGoLive.equals("1")) {// 90001 is anonumous notification to open the feeds fragment

                intent = new Intent(this, HomeActivityy.class);
                intent.putExtra("go_live", isGoLive);
            } else if (isGoLive.equals("0")) {// 90001 is anonumous notification to open the feeds fragment

                intent = new Intent(this, HomeActivityy.class);
                intent.putExtra("go_live", isGoLive);
            }
//            else if (push_type.equals(Constant.ADD_NEW_ADVENTURE_NOTIFICATION)
//                    || push_type.equals(Constant.UPDATE_ADVENTURE_NOTIFICATION)) {
//                intent = new Intent(this, AdventureSetupController.class);
//                intent.putExtra(Constant.NOTIFICATION_TYPE, push_type);
//                intent.putExtra(Constant.ID, id);
//            } else {
//                intent = new Intent(this, HomeModuleController.class);
//                intent.putExtra(Constant.NOTIFICATION_TYPE, push_type);
//                Log.e("ALERT", "Notification Arrived!");
//
//            }

            intent = new Intent(this, HomeActivityy.class); // comment fragment to open the related post.
            intent.putExtra(Constants.FRAG_TYPE, Constants.Notification);
           Log.d("checkimg1",url);
          // showNotification(message, getString(R.string.app_name), intent,url);
           showNotification(message,notification_type, intent,url);
      //      showNotification(message, getString(R.string.app_name));
        }
    }


    private void handleDataMessage1(JSONObject json,String notification_type) {
        createNotificationChannel();
        Intent intent = null;
        String push_type, id, isGoLive, message,url;

        SignupResponse signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN,
                SignupResponse.class);

        if (json != null) {
            //message = json.optString(Constants.MESSAGES);
            message = json.optString(Constants.MESSAGES);

            isGoLive = json.optString("go_live");

            signupResponse.setGo_live(isGoLive);
            PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
//          push_type = json.optString(Constants.PUSH_TYPE);
//          id = json.optString(Constants.ID);
//          scheduleType = json.optString(Constants.SCHEDULE_TYPE);
//          System.out.print("MSG: " + json + " " + message + " " + " " + push_type + " " + id + " " + scheduleType);
            handleNotification(message);

            if (isGoLive.equals("1")) {// 90001 is anonumous notification to open the feeds fragment

                intent = new Intent(this, HomeActivityy.class);
                intent.putExtra("go_live", isGoLive);
            } else if (isGoLive.equals("0")) {// 90001 is anonumous notification to open the feeds fragment

                intent = new Intent(this, HomeActivityy.class);
                intent.putExtra("go_live", isGoLive);
            }
//            else if (push_type.equals(Constant.ADD_NEW_ADVENTURE_NOTIFICATION)
//                    || push_type.equals(Constant.UPDATE_ADVENTURE_NOTIFICATION)) {
//                intent = new Intent(this, AdventureSetupController.class);
//                intent.putExtra(Constant.NOTIFICATION_TYPE, push_type);
//                intent.putExtra(Constant.ID, id);
//            } else {
//                intent = new Intent(this, HomeModuleController.class);
//                intent.putExtra(Constant.NOTIFICATION_TYPE, push_type);
//                Log.e("ALERT", "Notification Arrived!");
//            }

            intent = new Intent(this, HomeActivityy.class); // comment fragment to open the related post.
            intent.putExtra(Constants.FRAG_TYPE, Constants.Notification);

            //showNotification(message, getString(R.string.app_name), intent);
            showNotification(message, notification_type, intent);
            //showNotification(message, getString(R.string.app_name));
        }
    }

    private void showNotification(String pushMessage, String pushTitle, Intent intent,String url) {
        Log.d("checkimg1",url);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code*/ , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 120, 120, false))
                .setContentTitle(pushTitle)
                .setContentText(pushMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 500});
              // .setContentIntent(pendingIntent);
*/
      //  getBitmapFromURL(url);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 120, 120, false))
                .setContentTitle(pushTitle)
                .setContentText(pushMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(pushMessage))

                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture( getBitmapFromURL(url)))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 500})

              //  .setLargeIcon()
               .setContentIntent(pendingIntent);

       /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificationId = random.nextInt(10000);
        notificationManager.notify(notificationId*//* ID of notification *//*, notificationBuilder.build());*/




        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificationId = random.nextInt(10000);
       // ShortcutBadger.applyCount(getApplicationContext(), SharedPreference.getInstance().getInt(Constants.NOTIFICATION_COUNT));
        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }

    private void showNotification(String pushMessage, String pushTitle, Intent intent) {


        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code*/ , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      /*  NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 120, 120, false))
                .setContentTitle(pushTitle)
                .setContentText(pushMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 500});
              // .setContentIntent(pendingIntent);

*/
        //  getBitmapFromURL(url);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 120, 120, false))
                .setContentTitle(pushTitle)
                .setContentText(pushMessage)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(pushMessage))


                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{500, 500, 500, 500, 500})

                //  .setLargeIcon()
                .setContentIntent(pendingIntent);

       /* NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificationId = random.nextInt(10000);
        notificationManager.notify(notificationId*//* ID of notification *//*, notificationBuilder.build());*/




        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        int notificationId = random.nextInt(10000);
        // ShortcutBadger.applyCount(getApplicationContext(), SharedPreference.getInstance().getInt(Constants.NOTIFICATION_COUNT));
        notificationManager.notify(notificationId/* ID of notification */, notificationBuilder.build());
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private Bitmap getBitmapFromURL(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;

        }}
}