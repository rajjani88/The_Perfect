package com.appdata.theperfect.presenter;


import com.appdata.theperfect.interfaces.IView;
import org.json.JSONObject;

public abstract class BasePresenter<I extends IView> {

    private I iView;

    public BasePresenter() {
    }

    public I getView() {
        return iView;
    }

    public void setView(I iView) {
        this.iView = iView;
    }

    boolean handleError(retrofit2.Response response) {

        if (response.errorBody() != null) {
            try {
                String error = response.errorBody().string();
                JSONObject jObjError = new JSONObject(error);
                error = jObjError.getString("message");

                if (jObjError.has("active")
                        && !jObjError.getBoolean("active")) {
                    getView().dialogAccountDeactivate(!error.isEmpty() ? error : "");
                } else {
                    getView().onError(!error.isEmpty() ? error : null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                getView().onError(null);
                return true;
            }
            return true;
        }
        return false;
    }

}
