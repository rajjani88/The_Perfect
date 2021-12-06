package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseCountryData;
import com.appdata.theperfect.model.ResponseOauthLogin;

public interface IRegisterView extends IView {

    void onRegisterSuccess(ResponseOauthLogin body);

    void onGetOtherData(ResponseOauthLogin body);

    void onGetLocationData(ResponseCountryData body);

}
