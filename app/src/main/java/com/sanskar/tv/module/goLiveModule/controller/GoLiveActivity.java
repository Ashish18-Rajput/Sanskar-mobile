package com.sanskar.tv.module.goLiveModule.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WOWZCameraView;
import com.wowza.gocoder.sdk.api.errors.WOWZError;
import com.wowza.gocoder.sdk.api.errors.WOWZStreamingError;
import com.wowza.gocoder.sdk.api.status.WOWZState;
import com.wowza.gocoder.sdk.api.status.WOWZStatus;
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class GoLiveActivity extends AppCompatActivity
        implements WOWZStatusCallback, View.OnClickListener, NetworkCall.MyNetworkCallBack {

    // Properties needed for Android 6+ permissions handling
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    SignupResponse signupResponse;
    int status;
    // The top-level GoCoder API interface
    private WowzaGoCoder goCoder;
    // The GoCoder SDK camera view
    private WOWZCameraView goCoderCameraView;
    // The GoCoder SDK audio device
    private WOWZAudioDevice goCoderAudioDevice;
    // The GoCoder SDK broadcaster
    private WOWZBroadcast goCoderBroadcaster;
    // The broadcast configuration settings
    private WOWZBroadcastConfig goCoderBroadcastConfig;
    private NetworkCall networkCall;
    private boolean mPermissionsGranted = true;
    private String[] mRequiredPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    //
    // Utility method to check the status of a permissions request for an array of permission identifiers
    //
    private static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);

        // Initialize the GoCoder SDK
        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-C645-010C-6C98-C936-67C1");

        if (goCoder == null) {
            // If initialization failed, retrieve the last error and display it
            WOWZError goCoderInitError = WowzaGoCoder.getLastError();

            Toast.makeText(this,
                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Associate the WOWZCameraView defined in the U/I layout with the corresponding class member
        goCoderCameraView = (WOWZCameraView) findViewById(R.id.camera_preview);

        // Create an audio device instance for capturing and broadcasting audio
        goCoderAudioDevice = new WOWZAudioDevice();

        // Create a broadcaster instance
        goCoderBroadcaster = new WOWZBroadcast();

        // Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = new WOWZBroadcastConfig(WOWZMediaConfig.FRAME_SIZE_1920x1080);

        // Set the connection properties for the target Wowza Streaming Engine server or Wowza Streaming Cloud live stream
        goCoderBroadcastConfig.setHostAddress("13.126.59.178");
        goCoderBroadcastConfig.setPortNumber(1935);
        goCoderBroadcastConfig.setApplicationName("live");
        goCoderBroadcastConfig.setStreamName(signupResponse.getId());
        goCoderBroadcastConfig.setUsername("zeekkm@gmail.com");
        goCoderBroadcastConfig.setPassword("totalbhakti");

        // Designate the camera preview as the video broadcaster
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);

        // Designate the audio device as the audio broadcaster
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);

        // Associate the onClick() method as the callback for the broadcast button's click event
        Button broadcastButton = (Button) findViewById(R.id.broadcast_button);
        broadcastButton.setOnClickListener(this);
    }

    //
    // Called when an activity is brought to the foreground
    //
    @Override
    protected void onResume() {
        super.onResume();

        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
        // have been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
        } else
            mPermissionsGranted = true;

        // Start the camera preview display
        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isPreviewPaused())
                goCoderCameraView.onResume();
            else
                goCoderCameraView.startPreview();
        }

    }

    //
    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
    // the results of the permissions request
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    //
    // The callback invoked when the broadcast button is tapped
    //
    @Override
    public void onClick(View view) {
        // return if the user hasn't granted the app the necessary permissions
        if (!mPermissionsGranted) return;

        // Ensure the minimum set of configuration settings have been specified necessary to
        // initiate a broadcast streaming session
        WOWZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

        if (configValidationError != null) {
            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(this);
        } else {
            // Start streaming
            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String apitype) {
        Builders.Any.B ion = null;
        if (apitype.equals(API.SET_GURU_LIVE_STATUS)) {
            ion = (Builders.Any.B) Ion.with(this).load(apitype)
                    .setHeader("jwt", SharedPreference.getInstance(this).getString(Constants.JWT)!=null?SharedPreference.getInstance(this).getString(Constants.JWT): com.sanskar.tv.Others.Helper.Utils.JWT)
                    .setHeader("userid",signupResponse.getId())
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("on_off_live", String.valueOf(status));
        }
        return ion;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isShown())
                goCoderCameraView.onPause();
        }

    }

    @Override
    public void SuccessCallBack(JSONObject jsonstring, String apitype) throws JSONException {
        if (jsonstring.optBoolean("status")) {
            Log.e("true", "true");
        }
    }

    @Override
    public void ErrorCallBack(String jsonstring, String apitype) {
        ToastUtil.showDialogBox(this, jsonstring.toString());

    }

    public void setLiveStatus(int i, String id) {
        if (isConnectingToInternet(this)) {
            networkCall = new NetworkCall(this, this);
            networkCall.NetworkAPICall(API.SET_GURU_LIVE_STATUS, false);
        } else {
            ToastUtil.showDialogBox(this, Networkconstants.ERR_NETWORK_TIMEOUT);
        }
    }


    //
    // The callback invoked upon changes to the state of the steaming broadcast
    //
    @Override
    public void onWZStatus(final WOWZStatus goCoderStatus) {
        // A successful status transition has been reported by the GoCoder SDK
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");

        switch (goCoderStatus.getState()) {
            case WOWZState.STARTING:
                statusMessage.append("Broadcast initialization");
                break;

            case WOWZState.READY:
                statusMessage.append("Ready to begin streaming");
                break;

            case WOWZState.RUNNING:
                statusMessage.append("Streaming is active");
                //setLiveStatus(1,signupResponse.getId());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        status = 1;
                        setLiveStatus(0, signupResponse.getId());
                    }
                });
                break;

            case WOWZState.STOPPING:
                statusMessage.append("Broadcast shutting down");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        status = 0;
                        setLiveStatus(0, signupResponse.getId());
                    }
                });

                break;

            case WOWZState.IDLE:
                statusMessage.append("The broadcast is stopped");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        status = 0;
                        setLiveStatus(0, signupResponse.getId());
                    }
                });

                break;

            default:
                return;
        }

        // Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GoLiveActivity.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWZError(final WOWZStatus wowzStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GoLiveActivity.this,
                        "Streaming error: " + wowzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    // The callback invoked when an error occurs during a broadcast
    //


    //
    // Enable Android's immersive, sticky full-screen mode
    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null)
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


}

// The top level GoCoder API interface
//    private WowzaGoCoder goCoder;
//
//    // The GoCoder SDK camera view
//    private WOWZCameraView goCoderCameraView;
//
//    // The GoCoder SDK audio device
//    private WZAudioDevice goCoderAudioDevice;
//
//    // The GoCoder SDK broadcaster
//    private WZBroadcast goCoderBroadcaster;
//
//    // The broadcast configuration settings
//    private WZBroadcastConfig goCoderBroadcastConfig;
//
//    // Properties needed for Android 6+ permissions handling
//    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
//    private boolean mPermissionsGranted = true;
//    private String[] mRequiredPermissions = new String[] {
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO
//    };
//
//    private ImageView liveStreamingStartIMG;
//    private TimerView timerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_go_live);
//
//        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-9945-010F-E2F6-A48D-9EA0");
//
//        if (goCoder == null) {
//            // If initialization failed, retrieve the last error and display it
//            WOWZError goCoderInitError = WowzaGoCoder.getLastError();
//            Toast.makeText(this,
//                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // Associate the WOWZCameraView defined in the U/I layout with the corresponding class member
//        goCoderCameraView = (WOWZCameraView) findViewById(R.id.camera_preview);
//
//        // Create an audio device instance for capturing and broadcasting audio
//        goCoderAudioDevice = new WOWZAudioDevice();
//
//        // Create a broadcaster instance
//        goCoderBroadcaster = new WOWZBroadcast();
//
//        // Create a configuration instance for the broadcaster
//        goCoderBroadcastConfig = new WOWZBroadcastConfig(WOWZMediaConfig.FRAME_SIZE_1920x1080);
//
//        // Set the connection properties for the target Wowza Streaming Engine server or Wowza Streaming Cloud live stream
//        goCoderBroadcastConfig.setHostAddress("172.31.18.223");
//        goCoderBroadcastConfig.setPortNumber(1935);
//        goCoderBroadcastConfig.setApplicationName("live");
//        goCoderBroadcastConfig.setStreamName("myStream");
//        goCoderBroadcastConfig.setUsername("zeekkm@gmail.com");
//        goCoderBroadcastConfig.setPassword("Mktmkt@123");
//
//        // Designate the camera preview as the video broadcaster
//        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);
//
//        // Designate the audio device as the audio broadcaster
//        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);
//
//        // Associate the onClick() method as the callback for the broadcast button's click event
//        Button broadcastButton =  findViewById(R.id.broadcast_button);
//        broadcastButton.setOnClickListener(this);
//    }
//
//    //
//    // Called when an activity is brought to the foreground
//    //
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
//        // have been granted
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
//            if (!mPermissionsGranted)
//                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
//        } else
//            mPermissionsGranted = true;
//
//        // Start the camera preview display
//        if (mPermissionsGranted && goCoderCameraView != null) {
//            if (goCoderCameraView.isPreviewPaused())
//                goCoderCameraView.onResume();
//            else
//                goCoderCameraView.startPreview();
//        }
//
//    }
//
//    //
//    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
//    // the results of the permissions request
//    //
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        mPermissionsGranted = true;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE: {
//                // Check the result of each permission granted
//                for(int grantResult : grantResults) {
//                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                        mPermissionsGranted = false;
//                    }
//                }
//            }
//        }
//    }
//
//    //
//    // Utility method to check the status of a permissions request for an array of permission identifiers
//    //
//    private static boolean hasPermissions(Context context, String[] permissions) {
//        for(String permission : permissions)
//            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
//                return false;
//
//        return true;
//    }
//
//    //
//    // The callback invoked when the broadcast button is tapped
//    //
//    @Override
//    public void onClick(View view) {
//        // return if the user hasn't granted the app the necessary permissions
//        if (!mPermissionsGranted) return;
//
//        // Ensure the minimum set of configuration settings have been specified necessary to
//        // initiate a broadcast streaming session
//        WOWZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();
//
//        if (configValidationError != null) {
//            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
//        } else if (goCoderBroadcaster.getStatus().isRunning()) {
//            // Stop the broadcast that is currently running
//            goCoderBroadcaster.endBroadcast(this);
//        } else {
//            // Start streaming
//            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
//        }
//    }

//
// The callback invoked upon changes to the state of the steaming broadcast
//
//    @Override
//    public void onWZStatus(final WOWZStatus goCoderStatus) {
//        // A successful status transition has been reported by the GoCoder SDK
//
//    }
//
//    //
//    // The callback invoked when an error occurs during a broadcast
//    //
//    @Override
//    public void onWOWZError(final WOWZStatus goCoderStatus) {
//        // If an error is reported by the GoCoder SDK, display a message
//        // containing the error details using the U/I thread
//
//    }

//
// Enable Android's immersive, sticky full-screen mode
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        if (rootView != null)
//            rootView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }
//
//    @Override
//    public void onWZStatus(WZStatus wzStatus) {
//        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");
//
//        switch (goCoderStatus.getState()) {
//            case WOWZState.STARTING:
//                statusMessage.append("Broadcast initialization");
//                break;
//
//            case WOWZState.READY:
//                statusMessage.append("Ready to begin streaming");
//                break;
//
//            case WOWZState.RUNNING:
//                statusMessage.append("Streaming is active");
//                break;
//
//            case WOWZState.STOPPING:
//                statusMessage.append("Broadcast shutting down");
//                break;
//
//            case WOWZState.IDLE:
//                statusMessage.append("The broadcast is stopped");
//                break;
//
//            default:
//                return;
//        }
//
//        // Display the status message using the U/I thread
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(GoLiveActivity.this, statusMessage, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    public void onWZError(WZStatus wzStatus) {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(GoLiveActivity.this,
//                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}

// Initialize the GoCoder SDK
//        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-9945-010F-E2F6-A48D-9EA0");
//
//        if (goCoder == null) {
//            // If initialization failed, retrieve the last error and display it
//            WZError goCoderInitError = WowzaGoCoder.getLastError();
//            Toast.makeText(this,
//                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // Associate the WZCameraView defined in the U/I layout with the corresponding class member
//        goCoderCameraView = findViewById(R.id.camera_preview);
//
//        // Create an audio device instance for capturing and broadcasting audio
//        goCoderAudioDevice = new WZAudioDevice();
//
//        // Create a broadcaster instance
//        goCoderBroadcaster = new WZBroadcast();
//
//        // Create a configuration instance for the broadcaster
//        goCoderBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_1920x1080);
//
//        // Set the connection properties for the target Wowza Streaming Engine server or Wowza Cloud account
//        goCoderBroadcastConfig.setHostAddress("172.31.18.223");
//        goCoderBroadcastConfig.setPortNumber(1935);
//        goCoderBroadcastConfig.setApplicationName("live");
//        goCoderBroadcastConfig.setStreamName("myStream");
//        goCoderBroadcastConfig.setUsername("zeekkm@gmail.com");
//        goCoderBroadcastConfig.setPassword("Mktmkt@123");
//
////        goCoderBroadcastConfig.setHostAddress("rtsp://434324.entrypoint.cloud.wowza.com/app-9a16");
////        goCoderBroadcastConfig.setPortNumber(1935);
////        goCoderBroadcastConfig.setApplicationName("live");
////        goCoderBroadcastConfig.setStreamName("cc7a15fc");
////        goCoderBroadcastConfig.setUsername("client31972");
////        goCoderBroadcastConfig.setPassword("7e3f0ef5");
//
//        // Designate the camera preview as the video broadcaster
//        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);
//
//        // Designate the audio device as the audio broadcaster
//        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);
//
//        // Associate the onClick() method as the callback for the broadcast button's click event
//        liveStreamingStartIMG = findViewById(R.id.broadcast_button);
//        timerView             = findViewById(R.id.txtTimer);
//        liveStreamingStartIMG.setOnClickListener(this);
//    }
//
//    //
//    // Called when an activity is brought to the foreground
//    //
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
//        // have been granted
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
//            if (!mPermissionsGranted)
//                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
//        } else
//            mPermissionsGranted = true;
//
//        // Start the camera preview display
//        if (mPermissionsGranted && goCoderCameraView != null) {
//            if (goCoderCameraView.isPreviewPaused())
//                goCoderCameraView.onResume();
//            else
//                goCoderCameraView.startPreview();
//        }
//
//    }
//
//    //
//    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
//    // the results of the permissions request
//    //
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        mPermissionsGranted = true;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE: {
//                // Check the result of each permission granted
//                for(int grantResult : grantResults) {
//                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
//                        mPermissionsGranted = false;
//                    }
//                }
//            }
//        }
//    }
//
//    //
//    // Utility method to check the status of a permissions request for an array of permission identifiers
//    //
//    private static boolean hasPermissions(Context context, String[] permissions) {
//        for(String permission : permissions)
//            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
//                return false;
//
//        return true;
//    }
//
//    //
//    // The callback invoked when the broadcast button is pressed
//    //
//    @Override
//    public void onClick(View view) {
//        // return if the user hasn't granted the app the necessary permissions
//
//            if (!mPermissionsGranted) return;
//
//            // Ensure the minimum set of configuration settings have been specified necessary to
//            // initiate a broadcast streaming session
//            WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();
//
//            if (configValidationError != null) {
//
//                Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
//            } else if (goCoderBroadcaster.getStatus().isRunning()) {
//                // Stop the broadcast that is currently running
//                liveStreamingStartIMG.setImageResource(R.mipmap.ic_start);
//                goCoderBroadcaster.endBroadcast(this);
//            } else {
//                // Start streaming
//                liveStreamingStartIMG.setImageResource(R.mipmap.ic_stop);
//                goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
//            }
//
//    }
//
//    //
//    // The callback invoked upon changes to the state of the steaming broadcast
//    //
//    @Override
//    public void onWZStatus(final WZStatus goCoderStatus) {
//        // A successful status transition has been reported by the GoCoder SDK
//        //final StringBuffer statusMessage = new StringBuffer();
//        String statusMessage = "";
//
//        switch (goCoderStatus.getState()) {
//            case WZState.STARTING:
//                //statusMessage.append("Broadcast initialization");
//                break;
//
//            case WZState.READY:
//                //statusMessage.append("Ready to begin streaming");
//                break;
//
//            case WZState.RUNNING:
//                //statusMessage=("");
//
//                break;
//
//            case WZState.STOPPING:
//                //statusMessage=("Broadcast shutting down");
//                break;
//
//            case WZState.IDLE:
//                //statusMessage.append("The broadcast is stopped");
//                break;
//
//            default:
//                return;
//        }
//
//        // Display the status message using the U/I thread
//        //final String finalStatusMessage = statusMessage;
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//
//                if (goCoderStatus.isRunning()) {
//                    timerView.setVisibility(View.VISIBLE);
//                    timerView.startTimer();
//                    Toast.makeText(GoLiveActivity.this, "Streaming is active", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (timerView.getVisibility() == View.VISIBLE) {
//                        timerView.stopTimer();
//                        timerView.setVisibility(View.GONE);
//                        Toast.makeText(GoLiveActivity.this, "Streaming is stop", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });
//    }
//
//    //
//    // The callback invoked when an error occurs during a broadcast
//    //
//    @Override
//    public void onWZError(final WZStatus goCoderStatus) {
//        // If an error is reported by the GoCoder SDK, display a message
//        // containing the error details using the U/I thread
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(GoLiveActivity.this,
//                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    //
//    // Enable Android's sticky immersive full-screen mode
//    //
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        if (rootView != null)
//            rootView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }

//    private WowzaGoCoder goCoder;
//    private WZPlayerView wzPlayerView;
//    private WZPlayerConfig mStreamPlayerConfig;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       // setStatusBarGradiant(this);
//        setContentView(R.layout.activity_go_live);
//
//        initViews();
//
//        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-2745-0103-2C7C-AB11-5573");
//
//        if (goCoder == null) {
//            // If initialization failed, retrieve the last error and display it
//            WZError goCoderInitError = WowzaGoCoder.getLastError();
//            Toast.makeText(this,
//                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
//                    Toast.LENGTH_LONG).show();
//            Log.i("GO Coder License: " , goCoderInitError.getErrorDescription());
//            return;
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mStreamPlayerConfig = new WZPlayerConfig();
//        //mStreamPlayerConfig.s(true);
//        mStreamPlayerConfig.setHostAddress("13.127.166.4");
//        mStreamPlayerConfig.setApplicationName("live");
//        mStreamPlayerConfig.setStreamName("myStream");
//        mStreamPlayerConfig.setPortNumber(1935);
//        mStreamPlayerConfig.setUsername("zeekkm@gmail.com");
//        mStreamPlayerConfig.setPassword("Mktmkt@123");
//        mStreamPlayerConfig.setAudioEnabled(true);
//        mStreamPlayerConfig.setVideoEnabled(true);
//        wzPlayerView.setVolume(3);
//        wzPlayerView.setScaleMode(WZMediaConfig.FILL_VIEW);
//
//        WZStatusCallback statusCallback = new StatusCallback();
//        wzPlayerView.play(mStreamPlayerConfig, statusCallback);
//    }
//
//    private void initViews() {
////        toolbar = findViewById(R.id.toolbar);
//        wzPlayerView = findViewById(R.id.wz_player_view);
//    }
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void setStatusBarGradiant(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = activity.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//    }
//}
