package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ResponseCountryData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ModelCountryData data;


    protected ResponseCountryData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.readParcelable(ModelCountryData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeByte((byte) (error ? 1 : 0));
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseCountryData> CREATOR = new Creator<ResponseCountryData>() {
        @Override
        public ResponseCountryData createFromParcel(Parcel in) {
            return new ResponseCountryData(in);
        }

        @Override
        public ResponseCountryData[] newArray(int size) {
            return new ResponseCountryData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ModelCountryData getData() {
        return data;
    }
}
