package com.appdata.theperfect.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.appdata.theperfect.model.ModelUser;
import com.appdata.theperfect.model.ResponseOauthLogin;
import com.appdata.theperfect.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class StorageUtil {

    private final String STORAGE = AppConstants.sharedPrefName;
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
    }


    public void clearAll() {
        getSharedPreferences().edit()
                .clear()
                .apply();
    }

   /* public void storeUserInfo(ModelUser modelUser) {
        preferences = getSharedPreferences();

        Gson gson = new Gson();
        String json = gson.toJson(modelUser);
        Log.e("userInfo", json);
        preferences.edit().putString("userInfo", json).apply();
    }

    public ModelUser getUserInfo() {
        preferences = getSharedPreferences();
        Gson gson = new Gson();
        String json = preferences.getString("userInfo", null);
        if (json != null) {
            Log.e("userInfo", json);
        }
        Type type = new TypeToken<ModelUser>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/

    public void setDeviceToken(String id) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(AppConstants.SHARED_PREF_DEVICE_TOKEN, id);
        editor.apply();
    }

    public String getDeviceToken() {
        return getSharedPreferences().getString(AppConstants.SHARED_PREF_DEVICE_TOKEN, "");
    }

    /*public void saveTokenDetail(ResponseOauthLogin oauthLogin) {
        Gson gson = new Gson();
        String json = gson.toJson(oauthLogin);
        getSharedPreferences().edit().putString(AppConstants.SHARED_PREF_AUTH_SHOWN, json).apply();
    }

    public ResponseOauthLogin getTokenLoginDetail() {
        Gson gson = new Gson();
        String json = getSharedPreferences().getString(AppConstants.SHARED_PREF_AUTH_SHOWN, null);
        Type type = new TypeToken<ResponseOauthLogin>() {
        }.getType();
        return gson.fromJson(json, type);
    }*/

    public void saveUserDetail(ModelUser modelUserInfo) {
        Gson gson = new Gson();
        String json = gson.toJson(modelUserInfo);
        getSharedPreferences().edit().putString(AppConstants.SHARED_PREF_USER_MODEL, json).apply();
    }

    public ModelUser getUserDetail() {
        Gson gson = new Gson();
        String json = getSharedPreferences().getString(AppConstants.SHARED_PREF_USER_MODEL, null);
        Type type = new TypeToken<ModelUser>() {
        }.getType();
        return gson.fromJson(json, type);
    }

}