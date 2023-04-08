package com.sanskar.tv.Others;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.sanskar.tv.Others.Helper.PreferencesHelper;

/**
 * Created by appsquadz on 1/25/18.
 */

public class ApplicationClass extends MultiDexApplication {

  private static Context appContext;

  public static Context getAppContext() {
    return appContext;
  }
  @Override
  public void onCreate() {
    super.onCreate();
    appContext = this;
    //MultiDex.install(this);
    PreferencesHelper.initHelper(this);
  }

  @Override
  protected void attachBaseContext(Context base) {
    MultiDex.install(this);

    super.attachBaseContext(base);
  }
}
