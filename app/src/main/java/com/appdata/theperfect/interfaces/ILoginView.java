package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseDefaultData;
import com.appdata.theperfect.model.ResponseOauthLogin;

public interface ILoginView extends IView {

    void onLoginSuccess(ResponseOauthLogin body);

    void onSendSuccess(ResponseDefaultData body);

}
