package com.appdata.theperfect.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.appdata.theperfect.api.ApiInterface.BASE_URL;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private static Timer timer = null;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ApiInterface serviceTimeOut30Sec, serviceTimeOut15Sec, service;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Log.e(TAG, "onCreate: Called");

        mInstance = this;

        serviceTimeOut30Sec = createRetrofitService30Sec(ApiInterface.class, BASE_URL);
        serviceTimeOut15Sec = createRetrofitService15Sec(ApiInterface.class, BASE_URL);
        service = createRetrofitService(ApiInterface.class, BASE_URL);

//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
//            Log.e(TAG, "newToken: " + s);
//            new StorageUtil(getApplicationContext()).setDeviceToken(s);
//        });

      /*  FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                try {
                    String token = task.getResult();
                    new StorageUtil(getApplicationContext()).setDeviceToken(token);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });*/



//        Foreground.init(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    }

    public <T> T createRetrofitService30Sec(final Class<T> clazz, final String endPoint) {

        Gson gson = new GsonBuilder().create();

        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(0, 5 * 60 * 1000, TimeUnit.SECONDS))
                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 30))
                .cache(cache)
                .build();


        //init retrofit
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(endPoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(clazz);
    }

    public <T> T createRetrofitService15Sec(final Class<T> clazz, final String endPoint) {

        Gson gson = new GsonBuilder().create();

        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(0, 60 * 1000, TimeUnit.SECONDS))
                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 15))
                .cache(cache)
                .build();

        //init retrofit
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(endPoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(clazz);
    }

    public <T> T createRetrofitService(final Class<T> clazz, final String endPoint) {

        Gson gson = new GsonBuilder().create();

        File httpCacheDirectory = new File(getCacheDir(), "cache_file");
        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(0, 60 * 1000, TimeUnit.SECONDS))
                .addInterceptor(new CustomInterceptor(getInstance(), Locale.getDefault().getLanguage(), "", 120))
                .cache(cache)
                .build();


        //init retrofit
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(endPoint)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(clazz);
    }

    public ApiInterface getApiInterfaceTimeOut30Sec() {
        return serviceTimeOut30Sec;
    }

    public ApiInterface getApiInterfaceTimeOut15Sec() {
        return serviceTimeOut15Sec;
    }

    public ApiInterface getApiInterface() {
        return service;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public static RequestBody requestBodyJsonObject(String jsonParams) {

        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonParams);
    }

}
