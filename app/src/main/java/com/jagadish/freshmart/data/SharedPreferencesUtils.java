package com.jagadish.freshmart.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class SharedPreferencesUtils {

    public static final String SHARED_KEY_RESULTS= "TABLEAU";
    public static final String PREF_USER_LOGIN = "user_login";
    public static final String PREF_USER_NAME = "user_name";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_USER_MOBILE = "user_mobile";
    public static final String PREF_USER_DATA = "user_date";
    public static final String PREF_APP_FIRST_TIME = "app_first_time";
    public static final String PREF_APP_FCM_TOKEN = "app_fcm_token";
    public static final String PREF_DEVICE_CART = "device_cart";
    public static final String PREF_DEVICE_CART_ID= "device_cart_id";

    public SharedPreferencesUtils() {
    }

    private static WeakReference<SharedPreferences> sharedPref;
    private static WeakReference<SharedPreferences> appSharedPref;
    private static Context mContext;

    private static final String PREF_NAME = "fresh-mart";
    private static final String APP_PREF_NAME="app-fresh-mart";

    public synchronized static void init(Context context) {
        if (mContext == null) {
            mContext = context;
            sharedPref = new WeakReference<>(context
                    .getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE));
            appSharedPref = new WeakReference<>(context
                    .getSharedPreferences(APP_PREF_NAME, Activity.MODE_PRIVATE));
        }
    }

    private static SharedPreferences getSharedPref() {
        if (sharedPref == null || sharedPref.get() == null) {
            sharedPref = new WeakReference<>(mContext
                    .getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE));
        }

        return sharedPref.get();
    }

    private static SharedPreferences getAppSharedPref() {
        if (appSharedPref == null || appSharedPref.get() == null) {
            appSharedPref = new WeakReference<>(mContext
                    .getSharedPreferences(APP_PREF_NAME, Activity.MODE_PRIVATE));
        }

        return appSharedPref.get();
    }


    //region STRING PREF
    public static void setStringPreference(final String key, final String value) {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringPreference(final String key) {
        return getSharedPref().getString(key, null);
    }

    public static void setAppStringPreference(final String key, final String value) {
        SharedPreferences.Editor editor = getAppSharedPref().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getAppStringPreference(final String key) {
        return getAppSharedPref().getString(key, "");
    }

    public static String getStringPreference(final String key, String defaultValue) {
        return getSharedPref().getString(key, defaultValue);
    }

    public static void setBooleanPreference(final String key, final boolean value ){
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBooleanPreference(final String key){
        return getSharedPref().getBoolean(key,false);
    }

    public static void setIntPreference(final String key, final int value ){
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getIntPreference(final String key){
        return getSharedPref().getInt(key,0);
    }
    //endregion

    public static void removePreference(String key) {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.remove(key);
        editor.apply();
    }


    public static void clearAllPreferences() {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.clear();
        editor.apply();
    }


    public static void setAppBooleanPreference(final String key, final boolean value ){
        SharedPreferences.Editor editor = getAppSharedPref().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getAppBooleanPreference(final String key){
        return getAppSharedPref().getBoolean(key,false);
    }
}