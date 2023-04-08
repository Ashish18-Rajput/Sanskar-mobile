package com.sanskar.tv.Others.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.sanskar.tv.R;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by khang on 11/14/2017.
 */

public class PreferencesHelper {
    private static PreferencesHelper instance;
    private SharedPreferences prefs;

    private PreferencesHelper(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    private PreferencesHelper(Context context, String sharePreferencesName) {
        prefs = context.getApplicationContext().getSharedPreferences(sharePreferencesName, Context.MODE_PRIVATE);
    }

    public static PreferencesHelper getInstance() {
        return instance;
    }

    public static PreferencesHelper initHelper(Context context) {
        if (instance == null)
            instance = new PreferencesHelper(context);
        return instance;
    }

    public static PreferencesHelper initHelper(Context context, String sharePreferencesName) {
        if (instance == null)
            instance = new PreferencesHelper(context, sharePreferencesName);
        return instance;
    }

    public void setValue(String KEY, boolean value) {
        prefs.edit().putBoolean(KEY, value).apply();
    }

    public void setValue(String KEY, String value) {
        prefs.edit().putString(KEY, value).apply();
    }

    public void setValue(String KEY, Object value) {
        prefs.edit().putString(KEY, new Gson().toJson(value)).apply();
    }

    public void setValue(String KEY, int value) {
        prefs.edit().putInt(KEY, value).apply();
    }

    public void setValue(String KEY, long value) {
        prefs.edit().putLong(KEY, value).apply();
    }

    public void setValue(String KEY, float value) {
        prefs.edit().putFloat(KEY, value).apply();
    }

    public void setValue(String KEY, double defValue) {
        setValue(KEY, String.valueOf(defValue));
    }

    public <T> void setValue(String KEY, List<T> strings) {
        setValue(KEY, new Gson().toJson(strings));
    }

    public <T> void setValue(String KEY, T[] array) {
        JSONArray jArray = new JSONArray();
        for (T t : array) {
            jArray.put(t);
        }
        prefs.edit().putString(KEY, new Gson().toJson(jArray)).apply();
    }

    public <T> T[] getArrayValue(String KEY) {
        T[] results = null;
        try {
            JSONArray jArray = new JSONArray(prefs.getString(KEY, ""));
            for (int i = 0; i < jArray.length(); i++) {
                results[i] = (T) jArray.get(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    public <T> List<T> getListValue(String KEY) {
        List<T> objects = null;
        try {
            String obj = prefs.getString(KEY, "");
            objects = new Gson().fromJson(obj, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return objects;
    }

    public boolean getBooleanValue(String KEY, boolean defvalue) {
        return prefs.getBoolean(KEY, defvalue);
    }

    public String getStringValue(String KEY, String defvalue) {
        return prefs.getString(KEY, defvalue);
    }

    public <T> T getObjectValue(String KEY, Class<T> mModelClass) {
        Object object = null;
        try {
            object = new Gson().fromJson(prefs.getString(KEY, ""), mModelClass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Primitives.wrap(mModelClass).cast(object);
    }

    public int getIntValue(String KEY, int defValue) {
        return prefs.getInt(KEY, defValue);
    }

    public long getLongValue(String KEY, long defValue) {
        return prefs.getLong(KEY, defValue);
    }

    public float getFloatValue(String KEY, float defValue) {
        return prefs.getFloat(KEY, defValue);
    }

    public double getDoubleValue(String KEY, double defValue) {
        return Double.parseDouble(getStringValue(KEY, String.valueOf(defValue)));
    }

    public void removeKey(String KEY) {
        prefs.edit().remove(KEY).apply();
    }

    public boolean contain(String KEY) {
        return prefs.contains(KEY);
    }

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
    }

//    public ArrayList<BhajanCat> getListObject(String key, Class<BhajanCat> mClass) {
//        Gson gson = new Gson();
//
//        ArrayList<String> objStrings = getListString(key);
//        ArrayList<BhajanCat> objects = new ArrayList<BhajanCat>();
//
//        for (String jObjString : objStrings) {
//            BhajanCat value = gson.fromJson(jObjString, mClass);
//            objects.add(value);
//        }
//        return objects;
//    }
//
//    public ArrayList<String> getListString(String key) {
//        return new ArrayList<String>(Arrays.asList(TextUtils.split(prefs.getString(key, ""), "‚‗‚")));
//    }
//
//    public void putListObject(String key, ArrayList<BhajanCat> objArray) {
//        checkForNullKey(key);
//        Gson gson = new Gson();
//        ArrayList<String> objStrings = new ArrayList<String>();
//        for (BhajanCat obj : objArray) {
//            objStrings.add(gson.toJson(obj));
//        }
//        putListString(key, objStrings);
//    }
//
//    public void putListString(String key, ArrayList<String> stringList) {
//        checkForNullKey(key);
//        String[] myStringList = stringList.toArray(new String[stringList.size()]);
//        prefs.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
//    }

    //    public void setBhajanPlayList(String KEY, ArrayList<BhajanCat> list){
//        ArrayList<String> stringArrayList = new ArrayList<>();
//        for(BhajanCat bhajan : list){
//            stringArrayList.add(bhajan.toString());
//        }
//
//        putListString(KEY,stringArrayList);
////        prefs.edit().putString(KEY,stringArrayList);
//    }
//    public ArrayList<BhajanCat> getBhajanPlayList(String KEY){
//        ArrayList<String> stringArrayList = getListString(KEY);
//        ArrayList<BhajanCat> bhajanArrayList = new ArrayList<>();
//        for(String s : stringArrayList){
//            bhajanArrayList.add(new Gson().fromJson(s,BhajanCat.class));
//        }
//        return bhajanArrayList;
//    }
//
////    public void putListObject(String key, ArrayList<Object> objArray){
////        checkForNullKey(key);
////        Gson gson = new Gson();
////        ArrayList<String> objStrings = new ArrayList<String>();
////        for(Object obj : objArray){
////            objStrings.add(gson.toJson(obj));
////        }
////        setValue(key, objStrings);
////    }?
//
    public void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }
//
//
//    public void putListString(String key, ArrayList<String> stringList) {
//        checkForNullKey(key);
//        String[] myStringList = stringList.toArray(new String[stringList.size()]);
//        prefs.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
//    }
//    public ArrayList<String> getListString(String key) {
//        return new ArrayList<String>(Arrays.asList(TextUtils.split(prefs.getString(key, ""), "‚‗‚")));
//    }

    public void setObject(String key, Object object) {
        prefs.edit().putString(key, new Gson().toJson(object)).commit();
        //prefs.edit().apply();
    }

    //    public <T> T getObject(String key, Class<T> a) {
//
//        String strJson = prefs.getString(key, null);
//        if (TextUtils.isEmpty(strJson)) {
//            return null;
//        } else {
//            try {
//                return new Gson().fromJson(strJson, a);
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
//            }
//        }
    public String getBhajanString(String key) {
        Object result = null;
        String strJson = prefs.getString(key, null);
        return strJson;
    }

//    public void setBhajanList(String key, ArrayList<BhajanCat> bhajanArrayList) {
//        Set<String> set = new HashSet<String>();
//        set.addAll(bhajanArrayList);
//        prefs.edit().putStringSet(key, set).commit();
//    }
//    }
}
