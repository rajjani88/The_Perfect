package com.appdata.theperfect.presenter;

import android.accounts.NetworkErrorException;
import android.content.Context;

import androidx.annotation.NonNull;

import com.appdata.theperfect.R;
import com.appdata.theperfect.api.AppController;
import com.appdata.theperfect.interfaces.ICategoryView;
import com.appdata.theperfect.interfaces.ITimeSlotView;
import com.appdata.theperfect.model.ResponseAppointmentTimeData;
import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseSaveAppointment;
import com.appdata.theperfect.model.ResponseUpdateAppointment;

import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSlotPresenter extends BasePresenter<ITimeSlotView> {

    public static int retryCount = 0;

    public void appointmentDatetime(Context context) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().appointmentDatetime().enqueue(new Callback<ResponseAppointmentTimeData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAppointmentTimeData> call, @NonNull Response<ResponseAppointmentTimeData> response) {
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
                    getView().onGetDataSlot(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAppointmentTimeData> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        appointmentDatetime(context);
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

    public void submitAppointment(Context context, JSONObject jsonParams) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().saveappointment(AppController.requestBodyJsonObject("" + jsonParams)).enqueue(new Callback<ResponseSaveAppointment>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSaveAppointment> call, @NonNull Response<ResponseSaveAppointment> response) {
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
                    getView().onSubmitData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseSaveAppointment> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        submitAppointment(context,jsonParams);
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

    public void updateAppointment(Context context, JSONObject jsonParams) {

        getView().enableLoadingBar(true, "");

        AppController.getInstance().getApiInterface().updateAppointment(AppController.requestBodyJsonObject("" + jsonParams)).enqueue(new Callback<ResponseUpdateAppointment>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUpdateAppointment> call, @NonNull Response<ResponseUpdateAppointment> response) {
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
                    getView().onUpdateData(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseUpdateAppointment> call, @NonNull Throwable t) {
                try {
                    t.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                    retryCount++;
                    if (retryCount < 3) {
                        getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                        updateAppointment(context,jsonParams);
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
