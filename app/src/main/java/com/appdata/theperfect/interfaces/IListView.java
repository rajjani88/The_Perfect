package com.appdata.theperfect.interfaces;

import com.appdata.theperfect.model.ResponseCategoryData;
import com.appdata.theperfect.model.ResponseSellerListData;
import com.appdata.theperfect.model.ResponseZipData;

public interface IListView extends IView {

    void onGetSellerList(ResponseSellerListData body);

    void onGetZipData(ResponseZipData body);

}
