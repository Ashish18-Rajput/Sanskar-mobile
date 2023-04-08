package com.sanskar.tv.module.HomeModule.Fragment;

import static com.sanskar.tv.module.HomeModule.Adapter.MyPlayListAdapter.playlistmediaplayer;
import static com.sanskar.tv.module.HomeModule.Adapter.OfflineAudioVideoAdapter.downloadmediaplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.sanskar.tv.AudioPlayerService;
import com.sanskar.tv.CustomViews.AppButton;
import com.sanskar.tv.CustomViews.AppTextView;
import com.sanskar.tv.Others.Helper.API;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.Others.Helper.CropActivity;
import com.sanskar.tv.Others.Helper.ImageIntent;
import com.sanskar.tv.Others.Helper.PreferencesHelper;
import com.sanskar.tv.Others.Helper.ToastUtil;
import com.sanskar.tv.Others.Helper.Utils;
import com.sanskar.tv.Others.NetworkNew.NetworkCall;
import com.sanskar.tv.Others.network.Networkconstants;
import com.sanskar.tv.Premium.Activity.PaymentActivity;
import com.sanskar.tv.R;
import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.loginmodule.Fragment.OtpFragment;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.LoginSignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements com.sanskar.tv.Others.NetworkNew.NetworkCall.MyNetworkCallBack, View.OnClickListener {

    Context ctx;
    EditText name, email, phone, about;
    RadioGroup radioGroup;
    RadioButton male, female;
    AppTextView edit, save, set_pin;
    ImageView cameraIcon, premium_icon_profile;
    CircleImageView profileImage;
    SignupResponse signupResponse;
    String oldPhone;
    AlertDialog.Builder builder;
    TextView active_subscription_button, go_premium_button, manage_device_button;
    private NetworkCall networkCall;
    private String picpath;
    private AppButton logOut;
    private String androidId = "";

    TextView textView5, textView6, total_play_left;

    private String Is_premium = "";
    //RelativeLayout toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        ctx = getActivity();
        if (((HomeActivityy) getActivity()).playerlayout.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) ctx).playerlayout.setVisibility(View.GONE);
            AudioPlayerService.mediaPlayer.pause();
        }
        if (((HomeActivityy) getActivity()).playerlayout1.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) ctx).playerlayout1.setVisibility(View.GONE);

        }
        if (((HomeActivityy) getActivity()).playerlayout2.getVisibility() == View.VISIBLE) {
            ((HomeActivityy) ctx).playerlayout2.setVisibility(View.GONE);

        }
        if (downloadmediaplayer != null) {
            if (downloadmediaplayer.isPlaying()) {
                downloadmediaplayer.pause();
            }
        } else {

        }

        if (playlistmediaplayer != null) {
            if (playlistmediaplayer.isPlaying()) {
                playlistmediaplayer.pause();
            }
        } else {

        }

        initView(view);
        if (SharedPreference.getInstance(ctx).getString("android_id") != null) {
            androidId = SharedPreference.getInstance(ctx).getString("android_id");
        }

        if (SharedPreference.getInstance(ctx).getString(Constants.IS_PREMIUM) != null) {
            Is_premium = SharedPreference.getInstance(ctx).getString(Constants.IS_PREMIUM);
        }

        if (Is_premium.equalsIgnoreCase("0")) {
            textView5.setVisibility(View.VISIBLE);
            textView6.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.VISIBLE);
            about.setVisibility(View.VISIBLE);
            active_subscription_button.setVisibility(View.GONE);
            go_premium_button.setVisibility(View.VISIBLE);
            premium_icon_profile.setVisibility(View.GONE);
            total_play_left.setVisibility(View.VISIBLE);
        }
        if (Is_premium.equalsIgnoreCase("1")) {
            textView5.setVisibility(View.GONE);
            textView6.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            about.setVisibility(View.GONE);
            active_subscription_button.setVisibility(View.VISIBLE);
            go_premium_button.setVisibility(View.GONE);
            premium_icon_profile.setVisibility(View.VISIBLE);
            total_play_left.setVisibility(View.GONE);
        }

        if (SharedPreference.getInstance(getContext()).getString(Constants.TOTAL_PLAY) != null
                && !TextUtils.isEmpty(SharedPreference.getInstance(getContext()).getString(Constants.TOTAL_PLAY))
                && Is_premium.equalsIgnoreCase("0")) {

            total_play_left.setText("Total play left: " + ToastUtil.formatSeconds(Integer.parseInt(SharedPreference.getInstance(getContext()).getString(Constants.TOTAL_PLAY))));
            total_play_left.setVisibility(View.VISIBLE);
        } else if (TextUtils.isEmpty(SharedPreference.getInstance(getContext()).getString(Constants.TOTAL_PLAY))
                || Is_premium.equalsIgnoreCase("1")) {
            total_play_left.setVisibility(View.GONE);
        }
        return view;
    }

    private void initView(View view) {

        name = view.findViewById(R.id.name_profile);
        email = view.findViewById(R.id.email_profile);
        phone = view.findViewById(R.id.phone_profile);
        about = view.findViewById(R.id.about_profile);
        radioGroup = view.findViewById(R.id.radioGroups_profile);
        male = view.findViewById(R.id.male_radio_profile);
        female = view.findViewById(R.id.female_radio_profile);
        logOut = view.findViewById(R.id.log_out);
        //edit = view.findViewById(R.id.edit_profile);
        //save = view.findViewById(R.id.save_profile);
        profileImage = view.findViewById(R.id.profile_image);
        cameraIcon = view.findViewById(R.id.camera_icon_profile);
        premium_icon_profile = view.findViewById(R.id.premium_icon_profile);
        cameraIcon.setVisibility(View.GONE);

        edit = ((HomeActivityy) ctx).editTV;
        save = ((HomeActivityy) ctx).saveTV;
        set_pin = ((HomeActivityy) ctx).set_pin;

        textView5 = view.findViewById(R.id.textView5);
        textView6 = view.findViewById(R.id.textView6);
        total_play_left = view.findViewById(R.id.total_play_left);

        Utils.hideKeyboard(getActivity());


        active_subscription_button = view.findViewById(R.id.active_subscription_button);
        go_premium_button = view.findViewById(R.id.go_premium_button);

        active_subscription_button.setOnClickListener(v -> ((HomeActivityy) getContext()).showSubscriptionFrag());

        go_premium_button.setOnClickListener(v -> {
            Intent intent = new Intent(ctx, PaymentActivity.class);
            startActivity(intent);
        });

        manage_device_button = view.findViewById(R.id.manage_device_button);
        manage_device_button.setOnClickListener(v -> ((HomeActivityy) getContext()).showActiveDeviceFrag());

        edit.setVisibility(View.VISIBLE);
        set_pin.setVisibility(View.VISIBLE);

        if (save.getVisibility() == View.VISIBLE) {
            edit.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
        } else {
            edit.setVisibility(View.VISIBLE);
            set_pin.setVisibility(View.VISIBLE);
        }

        //back.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.closeKeyboard((HomeActivityy) ctx);
        edit.setOnClickListener(this);
        set_pin.setOnClickListener(this);
        save.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        signupResponse = PreferencesHelper.getInstance().getObjectValue(Constants.LOGIN_USER_BEAN, SignupResponse.class);
        name.setText(signupResponse.getUsername());
        email.setText(signupResponse.getEmail());
        phone.setText(signupResponse.getMobile());
        about.setText(signupResponse.getAbout());

        if (!TextUtils.isEmpty(signupResponse.getProfile_picture())) {
            picpath = signupResponse.getProfile_picture();
            Ion.with(ctx).load(picpath).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (e == null) {
                        profileImage.setImageBitmap(result);
                    }
                }
            });
        }
        oldPhone = signupResponse.getMobile();
        if (!TextUtils.isEmpty(signupResponse.getGender())) {
            if (signupResponse.getGender().equals("0"))
                radioGroup.check(R.id.male_radio_profile);
            else
                radioGroup.check(R.id.female_radio_profile);
        }
    }

    @Override
    public Builders.Any.B getAPIB(String API_NAME) {
        Builders.Any.B ion = null;

        if (API_NAME.equals(API.PROFILE_UPDATE)) {
            Log.d("check_id", signupResponse.getId());
            Log.d("check_mobile", phone.getText().toString());
            Log.d("check_email", email.getText().toString());
            Log.d("check_user", name.getText().toString());
            Log.d("check_c_code", signupResponse.getCountry_code());
            Log.d("check_about", about.getText().toString());
            // Log.d("check_go_live", signupResponse.getGo_live());
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setMultipartParameter("id", signupResponse.getId())
                    .setMultipartParameter("mobile", phone.getText().toString())
                    .setMultipartParameter("email", email.getText().toString())
                    .setMultipartParameter("username", name.getText().toString())
                    .setMultipartParameter("country_code", signupResponse.getCountry_code())
                    .setMultipartParameter("about", about.getText().toString());
            /* .setMultipartParameter("go_live", signupResponse.getGo_live());*/
            if (radioGroup.getCheckedRadioButtonId() == R.id.male_radio_profile)
                ion.setMultipartParameter("gender", "0");
            else
                ion.setMultipartParameter("gender", "1");
            if (!TextUtils.isEmpty(picpath)) {
                if (picpath.contains("http"))
                    ion.setMultipartParameter("profile_picture", picpath);
                else {
                    ion.setMultipartFile("profile_picture", new File(picpath));
                }
            }
        } else if (API_NAME.equals(API.LOGOUT)) {
            ion = (Builders.Any.B) Ion.with(ctx).load(API_NAME)
                    .setMultipartParameter("user_id", signupResponse.getId())
                    .setMultipartParameter("current_version", "" + Utils.getVersionCode(getContext()))
                    .setMultipartParameter("device_id", androidId);
        } else if (API_NAME.equals(API.SEND_VERIFICATION_OTP)) {
            ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                    .setMultipartParameter("mobile",signupResponse.getMobile())
                    .setMultipartParameter("pin", "")
                    .setMultipartParameter("otp", "")
                    .setMultipartParameter("device_id", SharedPreference.getInstance(ctx).getString("android_id"))
                    .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
           /* if (!isForgot){
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("pin", otptext)
                        .setMultipartParameter("otp", "")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            }else{
                ion = (Builders.Any.B) Ion.with(getContext()).load(API_NAME)
                        .setMultipartParameter("mobile", data.getMobile())
                        .setMultipartParameter("pin", "")
                        .setMultipartParameter("otp", "")
                        .setMultipartParameter("device_id", deviceId)
                        .setMultipartParameter("device_token", SharedPreference.getInstance(getContext()).getString(Constants.DEVICETOKEN));
            }*/
        }

        return ion;
    }

    @Override
    public void SuccessCallBack(JSONObject result, String API_NAME) throws JSONException {
        if (API_NAME.equals(API.PROFILE_UPDATE)) {
            if (result.optBoolean("status")) {
                signupResponse = new Gson().fromJson(result.optString("data"), SignupResponse.class);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                set_pin.setVisibility(View.VISIBLE);
                if (!signupResponse.getMobile().equals(oldPhone)) {
                    setViewVisibility(View.GONE);

                    Bundle bundle = new Bundle();
                    Fragment fragment = new ProfileOtpFragment();
                    bundle.putSerializable("data", signupResponse);
                    fragment.setArguments(bundle);
                    ((HomeActivityy) ctx).getSupportFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_home, fragment).commit();

                } else {
                    Intent intent = new Intent(ctx, HomeActivityy.class);
                    PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, signupResponse);
                    PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION, true);
                    ((HomeActivityy) ctx).finish();
                    startActivity(intent);
                }
            }
            ToastUtil.showDialogBox(ctx, result.optString("message"));
        } else if (API_NAME.equals(API.LOGOUT)) {
            if (result.optBoolean("status")) {
                Utils.clearUserData(getContext());
                Intent intent = new Intent(ctx, LoginSignupActivity.class);
                ((HomeActivityy) ctx).finish();
                startActivity(intent);
            } else {
                ToastUtil.showDialogBox(ctx, result.optString("message"));
            }
        } else if (API_NAME.equalsIgnoreCase(API.SEND_VERIFICATION_OTP)){
            if (result.optBoolean("status")){
               /* Intent intent = new Intent(ctx, LoginSignupActivity.class);
                intent.putExtra("otp_frag", "1");
                intent.putExtra("data", signupResponse);
                intent.putExtra(Constants.MOBILE, signupResponse.getMobile());
                intent.putExtra(Constants.PIN,"0");
                intent.putExtra(Constants.IS_FORGOT,"0");

                ((HomeActivityy) ctx).finish();
                startActivity(intent);*/

                Bundle bundle = new Bundle();
                Fragment fragment = new ProfileOtpFragment();
                bundle.putSerializable("data", signupResponse);
                bundle.putString(Constants.MOBILE, signupResponse.getMobile());
                bundle.putString(Constants.PIN,"0");
                bundle.putString(Constants.IS_FORGOT,"0");
                fragment.setArguments(bundle);
                ((HomeActivityy) ctx).getSupportFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_home, fragment).commit();


/*
                OtpFragment fragment = new OtpFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("Otp_Fragment").replace(R.id.container_layout_login, fragment).commit();*/
            } else {
                ToastUtil.showDialogBox(ctx, result.optString("message"));
            }
        }
    }

    @Override
    public void ErrorCallBack(String message, String API_NAME) {
        //   ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, message);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.profile_image:

                if (save.getVisibility() == View.VISIBLE) {
                    ImageIntent imageIntent = new ImageIntent(new ImageIntent.OnCameraClickedListener() {
                        @Override
                        public void onCameraImage(String path) {
                            picpath = path;
                        }
                    });
                    imageIntent.showImageChooser((Activity) ctx);
                }
                break;

            case R.id.edit_profile:
                editEnableDisable(true);
                cameraIcon.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                set_pin.setVisibility(View.GONE);
                break;
            case R.id.set_pin:
                editEnableDisable(false);
                cameraIcon.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                set_pin.setVisibility(View.VISIBLE);

//                networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);
                if (Utils.isConnectingToInternet(ctx)) {
                    networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileFragment.this, ctx);
                    networkCall.NetworkAPICall(API.SEND_VERIFICATION_OTP, true);
                } else
                    ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);

                break;

            case R.id.save_profile:

                if (validate()) {
                    editEnableDisable(false);
                    save.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    set_pin.setVisibility(View.VISIBLE);
                    cameraIcon.setVisibility(View.GONE);
                    if (Utils.isConnectingToInternet(ctx)) {
                        networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileFragment.this, ctx);
                        networkCall.NetworkAPICall(API.PROFILE_UPDATE, true);
                    } else
                        ToastUtil.showDialogBox(ctx, Networkconstants.ERR_NETWORK_TIMEOUT);
                } else {
                    editEnableDisable(true);
                    save.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.GONE);
                    set_pin.setVisibility(View.GONE);
                    cameraIcon.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.log_out:
                showDialogBox(ctx, "Do You really want to Logout?");
                // ((HomeActivityy) ctx).logOut();
                break;

        }

    }

    public void showDialogBox(Context context, String message) {

        final BottomSheetDialog dialog1 = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog1.setCancelable(true);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(R.layout.dialogbox_language3);
        Window window = dialog1.getWindow();
        window.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = dialog1.findViewById(R.id.message);

        textView.setText(message);

        Button btn_proceed1 = dialog1.findViewById(R.id.btn_Yes);
        Button btn_proceed2 = dialog1.findViewById(R.id.btn_NO);
        btn_proceed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Utils.isConnectingToInternet(context)) {
                    networkCall = new com.sanskar.tv.Others.NetworkNew.NetworkCall(ProfileFragment.this, ctx);
                    networkCall.NetworkAPICall(API.LOGOUT, false);
                    dialog1.dismiss();
                } else {
                    ToastUtil.showDialogBox(context, Networkconstants.ERR_NETWORK_TIMEOUT);
                }

            }
        });
        btn_proceed2.setOnClickListener(v -> dialog1.dismiss());

        dialog1.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivityy) ctx).invalidateOptionsMenu();
        ((HomeActivityy) ctx).handleToolbar();
        ((HomeActivityy) ctx).toolbar.setVisibility(View.VISIBLE);
        if (save.getVisibility() == View.VISIBLE) {
            edit.setVisibility(View.GONE);
            set_pin.setVisibility(View.GONE);
        }
    }

    private void editEnableDisable(boolean b) {
        name.setEnabled(b);
        email.setEnabled(b);
        phone.setEnabled(b);

        name.setFocusableInTouchMode(b);
        email.setFocusableInTouchMode(b);
        phone.setFocusableInTouchMode(b);
        male.setEnabled(b);
        female.setEnabled(b);

        if (!b) {
            profileImage.setOnClickListener(null);
            about.setBackgroundResource(R.drawable.about_rectangle_disable_background_profile);
        } else {
            profileImage.setOnClickListener(this);
            about.setBackgroundResource(R.drawable.about_rectangle_background_profile);
        }
        about.setEnabled(b);
        about.setFocusableInTouchMode(b);
    }

    private boolean validate() {
        Boolean flag = true;

        if (name.getText().toString().trim().isEmpty()) {
            flag = false;
            ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter name");

        } else if (phone.getText().toString().length() != 10) {
            flag = false;
            ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter a valid phone number");

        } else if (!TextUtils.isEmpty(email.getText())) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                flag = false;
                ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter a valid email address");
            }
        } else if (TextUtils.isEmpty(email.getText())) {
            flag = false;
            ToastUtil.showShortSnackBar(((HomeActivityy) ctx).container, "Please enter email address");
        }

        return flag;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_GALLERY) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            try {
                Cursor c = ctx.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picpath = c.getString(columnIndex);
                c.close();
                if (picpath == null) {
                    picpath = selectedImage.getPath();
                }
                //   doCrop(picpath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_CAMERA) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
            // Toast.makeText(ctx.getApplicationContext(), "Here " + data.getData(), Toast.LENGTH_LONG).show();
            Uri tempUri = getImageUri(ctx.getApplicationContext(), imageBitmap);
            picpath = getRealPathFromURI(tempUri);
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            if (picpath != null && !picpath.isEmpty()) {
                //   doCrop(picpath);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.CROP_REQUEST) {
            String pic_url = data.getExtras().getString(Constants.IMAGE_PATH);
            setProfilePic(pic_url);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void setProfilePic(String pic_url) {
        Ion.with(ctx)
                .load(pic_url)
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        profileImage.setImageBitmap(result);
                    }
                });
    }

    private void doCrop(String picturePath) {
        Intent croperIntent = new Intent(ctx, CropActivity.class);
        croperIntent.putExtra(Constants.IMAGE_PATH, picturePath);
        ((HomeActivityy) ctx).startActivityForResult(croperIntent, Constants.CROP_REQUEST);
    }

    public void setViewVisibility(int visibility) {
        edit.setVisibility(visibility);
        set_pin.setVisibility(visibility);
    }
}
