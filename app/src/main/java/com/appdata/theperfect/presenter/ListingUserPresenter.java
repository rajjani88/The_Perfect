package com.appdata.theperfect.presenter;

import android.accounts.NetworkErrorException;
import android.content.Context;

import androidx.annotation.NonNull;

import com.appdata.theperfect.R;
import com.appdata.theperfect.api.AppController;
import com.appdata.theperfect.interfaces.ICategoryView;
import com.appdata.theperfect.interfaces.IListView;
import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseSellerListData;
import com.appdata.theperfect.model.ResponseZipData;

import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListingUserPresenter extends BasePresenter<IListView> {

    public static int retryCount = 0;

    public void getSellerList(Context context, JSONObject jsonParams) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().getSellerList(AppController.requestBodyJsonObject("" + jsonParams)).enqueue(new Callback<ResponseSellerListData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSellerListData> call, @NonNull Response<ResponseSellerListData> response) {
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
                    getView().onGetSellerList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSellerListData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        getSellerList(context, jsonParams);
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

    public void getAppointmentList(Context context, final HashMap<String, RequestBody> map) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().getAppointmentList(map).enqueue(new Callback<ResponseSellerListData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSellerListData> call, @NonNull Response<ResponseSellerListData> response) {
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
                    getView().onGetSellerList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSellerListData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        getAppointmentList(context, map);
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


    public void getDealAppointmentList(Context context, final HashMap<String, RequestBody> map) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().getDealAppointmentList(map).enqueue(new Callback<ResponseSellerListData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSellerListData> call, @NonNull Response<ResponseSellerListData> response) {
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
                    getView().onGetSellerList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSellerListData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        getDealAppointmentList(context, map);
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

    public void getListUsingZip(Context context, final HashMap<String, RequestBody> map) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().getListUsingZip(map).enqueue(new Callback<ResponseSellerListData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSellerListData> call, @NonNull Response<ResponseSellerListData> response) {
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
                    getView().onGetSellerList(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSellerListData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        getListUsingZip(context, map);
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

    public void getZipListData(Context context) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().getZipData().enqueue(new Callback<ResponseZipData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseZipData> call, @NonNull Response<ResponseZipData> response) {
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
                    getView().onGetZipData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseZipData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        getZipListData(context);
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
