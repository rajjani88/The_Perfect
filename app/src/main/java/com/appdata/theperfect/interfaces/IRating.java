package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseRatingData;
import com.appdata.theperfect.model.ResponseSellerListData;

public interface IRating extends IView {

    void onRatingSuccess(ResponseRatingData body);

}
