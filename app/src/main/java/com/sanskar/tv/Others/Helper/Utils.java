package com.sanskar.tv.Others.Helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.icu.util.DateInterval;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.sanskar.tv.SharedPreference;
import com.sanskar.tv.module.HomeModule.Activity.HomeActivityy;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.loginmodule.Pojo.SignupResponse;
import com.sanskar.tv.module.loginmodule.activity.SplashActivity;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String JWT = "";
    public static int Count = 0;
    public static int Version_hit = 0;
    public static ContentResolver resolver;
    public static Cursor phones;
    public static String phone = "", oldNumber = "", NewNumber = "", name = "", image_thumb = "", Email = "";

    /*************************
     * method to hide keyboard
     ***********************/
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideStatusBar(Context ctx) {
        ((Activity) ctx).requestWindowFeature(Window.FEATURE_NO_TITLE);
        ((Activity) ctx).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static Bitmap Base64ToBitmap(String myImageData) {
        byte[] imageAsBytes = Base64.decode(myImageData.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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

    //methods to compress image starts//
    public static Bitmap decodeSampledBitmap(String url, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(url, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static boolean isEmailValid(String mail) {
        String exp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static void closeKeyboard(Activity cnx) {

        InputMethodManager imm = (InputMethodManager) cnx.getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            if (imm.isAcceptingText() || imm.isActive())
                imm.hideSoftInputFromWindow(cnx.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static String changeDateFormat(String appDateFormat, String targetDateFormat, String dateTo) {
        String formattedDate = "";
        try {
            DateFormat originalFormat = new SimpleDateFormat(appDateFormat, Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat(targetDateFormat);
            Date date = originalFormat.parse(dateTo);
            formattedDate = targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void shareProperty(Context ctx) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
        sendIntent.setType("text/plain");
        ctx.startActivity(sendIntent);
    }

    public static String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = android.text.format.DateFormat.format("EEE, d MMM yyyy hh:mm aaa", cal).toString();
        return date;
    }

    public static Date getDate(String time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(time));
        Date date = cal.getTime();
        return date;
    }

    public static String getAgoTime(String timeStamp) {
        String agoTime = null;
        Date past = getDate(timeStamp);
        Date now = new Date();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
        long months = days / 30;

        if (seconds < 60) {
            if (seconds < 2) {
                agoTime = "a second ago";
            } else {
                agoTime = String.format(seconds + " seconds ago");
            }
        } else if (minutes < 60) {
            if (minutes < 2) {
                agoTime = "a minute ago";
            } else {
                agoTime = String.format(minutes + " minutes ago");
            }
        } else if (hours < 24) {
            if (hours < 2) {
                agoTime = "a hour ago";
            } else {
                agoTime = String.format(hours + " hours ago");
            }
        } else if (days < 30) {
            if (days < 2) {
                agoTime = "a day ago";
            } else {
                agoTime = String.format(days + " days ago");
            }
        } else {
            if (months < 2) {
                agoTime = "a month ago";
            } else {
                agoTime = String.format(months + " months ago");
            }
        }
        return agoTime;
    }


    public static <Object> Object[] concatenate(Object[] a, Object[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        Object[] c = (Object[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    public static void clearEditText(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setText("");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static DateInterval getTimeDifference(long timestamp) {
        DateInterval interval = new DateInterval(timestamp, Calendar.getInstance().getTimeInMillis());
        return interval;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity, View container) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            boolean hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (!hasMenuKey) {
                int navBarHeight = 0;
                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int usableHeight = metrics.heightPixels;
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                int realHeight = metrics.heightPixels;
                if (realHeight > usableHeight) {
                    navBarHeight = realHeight - usableHeight;
                }
                container.setPadding(container.getPaddingLeft(), container.getPaddingTop(),
                        container.getPaddingRight(), navBarHeight);
            }
        }
    }

    public static Bitmap getBitmapFromView(ImageView view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

    public static boolean isVpn(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activeNetwork = connectivityManager.getActiveNetwork();
        }
        NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(activeNetwork);
        return caps!=null && caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
    }

    public static int getVersionCode(Context activity) {
        int version = 0;
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static String JavaNumberOfDaysToMonthAndDay(String number) {

        int num =Integer.parseInt(number);
        int days = num % 30;
        int month = num / 30;
        if (days == 0){
            if (month>1)
            return month + " Months";
            else return month +" Month";
        }else{
            if (month>1)
                return month +" Months and "+days +" days";
            else return month +" Month and "+days +" days";
        }
    }


    public static void SignOutUsernotifi(Context context) {
        clearUserData(context);
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show();
    }

    public static void clearUserData(Context context) {
        SharedPreference.getInstance(context).setMenuMaster(null);
        SharedPreference.getInstance(context).setAdvertisement(null);
        SharedPreference.getInstance(context).setBhajan(null);
        SharedPreference.getInstance(context).setChannelData(null);
        SharedPreference.getInstance(context).setChannel(null);
        SharedPreference.getInstance(context).setEPGResponse(null);
        SharedPreference.getInstance(context).setNews(null);
        SharedPreference.getInstance(context).setPremiumCategory(null);
        SharedPreference.getInstance(context).setPictureAds(null);
        SharedPreference.getInstance(context).setLiveTvAds(null);
        SharedPreference.getInstance(context).setVideoAds(null);
        List<Bhajan> bhajanList = new ArrayList<>();
        PreferencesHelper.getInstance().setValue(Constants.LOGIN_SESSION, false);
        PreferencesHelper.getInstance().setValue(Constants.LOGIN_USER_BEAN, new SignupResponse());
        PreferencesHelper.getInstance().setValue(Constants.MY_PLAY_LIST, bhajanList);
        SharedPreference.getInstance(context).putBoolean(Constants.LOGIN_SESSION, false);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "QuestionImage", null);
        return Uri.parse(path);
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


}




