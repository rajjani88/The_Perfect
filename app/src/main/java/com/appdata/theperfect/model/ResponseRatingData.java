package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseRatingData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ModelRatingData data;

    protected ResponseRatingData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.readParcelable(ModelRatingData.class.getClassLoader());
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

    public static final Creator<ResponseRatingData> CREATOR = new Creator<ResponseRatingData>() {
        @Override
        public ResponseRatingData createFromParcel(Parcel in) {
            return new ResponseRatingData(in);
        }

        @Override
        public ResponseRatingData[] newArray(int size) {
            return new ResponseRatingData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ModelRatingData getData() {
        return data;
    }
}
