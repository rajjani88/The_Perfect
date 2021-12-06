package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseOauthLogin;

public interface ICategoryView extends IView {

    void onGetCategoryData(ResponseCategoryData body);

}
