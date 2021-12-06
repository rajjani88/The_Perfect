package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSellerListData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ArrayList<ModelSelllerListData> data;

    protected ResponseSellerListData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.createTypedArrayList(ModelSelllerListData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseSellerListData> CREATOR = new Creator<ResponseSellerListData>() {
        @Override
        public ResponseSellerListData createFromParcel(Parcel in) {
            return new ResponseSellerListData(in);
        }

        @Override
        public ResponseSellerListData[] newArray(int size) {
            return new ResponseSellerListData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<ModelSelllerListData> getData() {
        return data;
    }
}
