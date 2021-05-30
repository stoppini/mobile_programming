package com.example.MagicShop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    PreferenceUtils(){
    }

    public static boolean isLogged(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.KEY_LOGGED, true);
    }
    public static void logging(boolean log, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(Constants.KEY_LOGGED, log);
        prefsEditor.apply();
    }
    public static boolean saveId(String id, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_ID, id);
        prefsEditor.apply();
        return true;
    }

    public static String getId(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_ID, null);
    }

//    public static boolean saveUsername(String username, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_USERNAME, username);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getUsername(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_USERNAME, null);
//    }
//
//    public static boolean saveEmail(String email, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_EMAIL, email);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getEmail(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_EMAIL, null);
//    }
//
//    public static boolean saveLocation(String location, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_LOCATION, location);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getLocation(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_LOCATION, null);
//    }
//
//    public static boolean saveAddress(String address, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_ADDRESS, address);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getAddress(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_ADDRESS, null);
//    }
//
//    public static boolean saveCap(String cap, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_CAP, cap);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getCap(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_CAP, null);
//    }
//
//    public static boolean savePassword(String password, Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor prefsEditor = prefs.edit();
//        prefsEditor.putString(Constants.KEY_PASSWORD, password);
//        prefsEditor.apply();
//        return true;
//    }
//
//    public static String getPassword(Context context){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        return prefs.getString(Constants.KEY_PASSWORD, null);
//    }

    public static void logOut(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();
        logging(false, context);
    }


}
