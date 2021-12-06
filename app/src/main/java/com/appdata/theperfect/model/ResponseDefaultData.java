package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDefaultData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    protected ResponseDefaultData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeByte((byte) (error ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseDefaultData> CREATOR = new Creator<ResponseDefaultData>() {
        @Override
        public ResponseDefaultData createFromParcel(Parcel in) {
            return new ResponseDefaultData(in);
        }

        @Override
        public ResponseDefaultData[] newArray(int size) {
            return new ResponseDefaultData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

}
