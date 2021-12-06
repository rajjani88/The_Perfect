package com.appdata.theperfect.presenter;

import android.accounts.NetworkErrorException;
import android.content.Context;

import androidx.annotation.NonNull;

import com.appdata.theperfect.R;
import com.appdata.theperfect.api.AppController;
import com.appdata.theperfect.interfaces.ILoginView;
import com.appdata.theperfect.interfaces.IRegisterView;
import com.appdata.theperfect.model.ResponseDefaultData;
import com.appdata.theperfect.model.ResponseOauthLogin;

import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<ILoginView> {

    public static int retryCount = 0;

    public void loginUser(Context context, JSONObject jsonParams) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().login(AppController.requestBodyJsonObject("" + jsonParams)).enqueue(new Callback<ResponseOauthLogin>() {
            @Override
            public void onResponse(@NonNull Call<ResponseOauthLogin> call, @NonNull Response<ResponseOauthLogin> response) {
                getView().enableLoadingBar(false, "");

                retryCount = 0;

                boolean active = true;
                String error = "";
                if (response.code() == 400 && response.errorBody() != null) {
                    try {
                        error = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error);
                        if (jObjError.has("active")
                                && !jObjError.getBoolean("active")) {
                            active = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!active) {
                    getView().dialogAccountDeactivate(!error.isEmpty() ? error : "");
                } else if (!error.isEmpty()) {
                    getView().onError(error);
                } else if (response.isSuccessful() && response.code() == 200) {
                    getView().onLoginSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseOauthLogin> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        loginUser(context, jsonParams);
                    } else {
                        getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                        getView().enableLoadingBar(false, "");
                    }
                } else {
                    if (t instanceof NetworkErrorException || t instanceof SocketException) {
                        getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                    } else {
                        getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                    }
                    retryCount = 3;
                    getView().enableLoadingBar(false, "");
                }
            }
        });
    }

    public void forgotPassword(Context context, JSONObject jsonParams) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().forgotPassword(AppController.requestBodyJsonObject("" + jsonParams)).enqueue(new Callback<ResponseDefaultData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseDefaultData> call, @NonNull Response<ResponseDefaultData> response) {
                getView().enableLoadingBar(false, "");

                retryCount = 0;

                boolean active = true;
                String error = "";
                if (response.code() == 400 && response.errorBody() != null) {
                    try {
                        error = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error);
                        if (jObjError.has("active")
                                && !jObjError.getBoolean("active")) {
                            active = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!active) {
                    getView().dialogAccountDeactivate(!error.isEmpty() ? error : "");
                } else if (!error.isEmpty()) {
                    getView().onError(error);
                } else if (response.isSuccessful() && response.code() == 200) {
                    getView().onSendSuccess(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseDefaultData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        forgotPassword(context, jsonParams);
                    } else {
                        getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                        getView().enableLoadingBar(false, "");
                    }
                } else {
                    if (t instanceof NetworkErrorException || t instanceof SocketException) {
                        getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                    } else {
                        getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                    }
                    retryCount = 3;
                    getView().enableLoadingBar(false, "");
                }
            }
        });
    }

}
