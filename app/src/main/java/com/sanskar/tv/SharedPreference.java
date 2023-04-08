package com.sanskar.tv;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.sanskar.tv.EPG.TvGuiderMaster;
import com.sanskar.tv.Others.Helper.Constants;
import com.sanskar.tv.module.HomeModule.Pojo.AdvertisementAds;
import com.sanskar.tv.module.HomeModule.Pojo.Bhajan;
import com.sanskar.tv.module.HomeModule.Pojo.BhajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.Channel;
import com.sanskar.tv.module.HomeModule.Pojo.MainAdvertisement;
import com.sanskar.tv.module.HomeModule.Pojo.MainBajanResponse;
import com.sanskar.tv.module.HomeModule.Pojo.MainChannelData;
import com.sanskar.tv.module.HomeModule.Pojo.MainMenuMaster;
import com.sanskar.tv.module.HomeModule.Pojo.MainNewsResponse;
import com.sanskar.tv.module.HomeModule.Pojo.MenuMaster;
import com.sanskar.tv.module.HomeModule.Pojo.PremiumCategory;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import static android.provider.Contacts.SettingsColumns.KEY;

public class SharedPreference {
    public static final String MY_PREFERENCES = "MY_PREFERENCES";
    public static final int MODE = Context.MODE_PRIVATE;
    private static SharedPreference pref;
    private Context mCtx;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    private static final String SHARED_PREF_NAME = "sign_shared_preference_owner";


    public SharedPreference(Context mCtx) {
        if(mCtx!=null) {
            this.sharedPreference = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            this.editor = sharedPreference.edit();
            this.mCtx = mCtx;
        }
        else {

        }
    }


   /* private SharedPreference() {
        sharedPreference = mCtx.getSharedPreferences(MY_PREFERENCES, MODE);
        editor = sharedPreference.edit();
    }
*/
    public static SharedPreference getInstance(Context mCtx) {
        if (pref == null) {
            pref = new SharedPreference(mCtx);
        }
        return pref;
    }

    public String getString(String key) {
        return sharedPreference.getString(key, "");
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public int getInt(String key) {
        return sharedPreference.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }


    public long getLong(String key) {
        return sharedPreference.getLong(key, 0l);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    public float getFloat(String key) {
        return sharedPreference.getFloat(key, 0.5f);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public boolean getBoolean(String key) {
        //    editor.putBoolean(key, value).commit();
        return sharedPreference.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

   /* public <T>void setprefrenceValue(String key, T[]array) {
        JSONArray jArray = new JSONArray();
        for (T t : array) {
            jArray.put(t);

    }
        return sharedPreference.edit(key, false);


    }*/



    public void saveArrayList(Bhajan[] list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);
        SharedPreferences.Editor editor = prefs.edit();
      /*  Gson gson = new Gson();
        String json = gson.toJson(list);*/
        JSONArray jArray = new JSONArray();
        for (Bhajan t : list) {
            jArray.put(t);
        }
        editor.putString(key,  new Gson().toJson(jArray));
        editor.apply();

    }
    public TvGuiderMaster getEPGResponse() {
        TvGuiderMaster response = null;
        String responsestr = sharedPreference.getString(Constants.SIGNUP_RESPONSE, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, TvGuiderMaster.class);
        return response;
    }

    public void setEPGResponse(TvGuiderMaster response) {
        editor.putString(Constants.SIGNUP_RESPONSE, new Gson().toJson(response));
        editor.commit();
    }


    public Bhajan[] getArrayValue(String KEY) {
        Bhajan[] results = null;
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mCtx);

            JSONArray jArray = new JSONArray(prefs.getString(KEY, ""));
            for (int i = 0; i < jArray.length(); i++) {
                results[i] = (Bhajan) jArray.get(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }


    public MainMenuMaster getMenuMaster() {
        MainMenuMaster response = null;
        String responsestr = sharedPreference.getString(Constants.MENUMASTER, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, MainMenuMaster.class);
        return response;
    }

    public void setMenuMaster(MainMenuMaster response) {
        editor.putString(Constants.MENUMASTER, new Gson().toJson(response));
        editor.commit();
    }
    public MainAdvertisement getAdvertisement() {
        MainAdvertisement response = null;
        String responsestr = sharedPreference.getString(Constants.ADVERTISEMENT, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, MainAdvertisement.class);
        return response;
    }

    public void setAdvertisement(MainAdvertisement response) {
        editor.putString(Constants.ADVERTISEMENT, new Gson().toJson(response));
        editor.commit();
    }

    public AdvertisementAds[] getVideosAds() {
        AdvertisementAds[] response = null;
        String responsestr = sharedPreference.getString(Constants.VIDEOS_ADS, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, AdvertisementAds[].class);
        return response;
    }

    public void setVideoAds(AdvertisementAds[] response) {
        editor.putString(Constants.VIDEOS_ADS, new Gson().toJson(response));
        editor.commit();
    }

    public AdvertisementAds[] getPictureAds() {
        AdvertisementAds[] response = null;
        String responsestr = sharedPreference.getString(Constants.PICTURE_ADS, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, AdvertisementAds[].class);
        return response;
    }

    public void setLiveTvAds(AdvertisementAds[] response) {
        editor.putString(Constants.LIVETV_ADS, new Gson().toJson(response));
        editor.commit();
    }
    public AdvertisementAds[] getLiveTvAds() {
        AdvertisementAds[] response = null;
        String responsestr = sharedPreference.getString(Constants.LIVETV_ADS, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, AdvertisementAds[].class);
        return response;
    }

    public void setPictureAds(AdvertisementAds[] response) {
        editor.putString(Constants.PICTURE_ADS, new Gson().toJson(response));
        editor.commit();
    }
    public PremiumCategory getPremiumCategory() {
        PremiumCategory response = null;
        String responsestr = sharedPreference.getString(Constants.PREMIUM, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, PremiumCategory.class);
        return response;
    }

    public void setPremiumCategory(PremiumCategory response) {
        editor.putString(Constants.PREMIUM, new Gson().toJson(response));
        editor.commit();
    }
    public MainChannelData getChannelData() {
        MainChannelData response = null;
        String responsestr = sharedPreference.getString(Constants.CHANNEL, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, MainChannelData.class);
        return response;
    }

    public void setChannelData(MainChannelData response) {
        editor.putString(Constants.CHANNEL, new Gson().toJson(response));
        editor.commit();
    }
    public MainBajanResponse getBhajan() {
        MainBajanResponse response = null;
        String responsestr = sharedPreference.getString(Constants.BHAJAN, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, MainBajanResponse.class);
        return response;
    }

    public void setBhajan(MainBajanResponse response) {
        editor.putString(Constants.BHAJAN, new Gson().toJson(response));
        editor.commit();
    }

    public MainNewsResponse getNews() {
        MainNewsResponse response = null;
        String responsestr = sharedPreference.getString(Constants.NEWS, null);
        if (responsestr != null && responsestr.trim().length() > 0)
            response = new Gson().fromJson(responsestr, MainNewsResponse.class);
        return response;
    }

    public void setNews(MainNewsResponse response) {
        editor.putString(Constants.NEWS, new Gson().toJson(response));
        editor.commit();
    }

    public Channel getchannel() {
        Channel channel = null;
        String userJson = sharedPreference.getString(Constants.HOME_CHANNEL_DATA, null);
        if (userJson != null && userJson.trim().length() > 0)
            channel = new Gson().fromJson(userJson, Channel.class);
        return channel;
    }

    public void setChannel(Channel channel) {
        editor.putString(Constants.HOME_CHANNEL_DATA, new Gson().toJson(channel));
        editor.commit();
    }
}