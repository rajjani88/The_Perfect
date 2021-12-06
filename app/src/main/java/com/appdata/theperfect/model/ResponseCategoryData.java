package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseCategoryData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ArrayList<ModelCategoryData> data;

    protected ResponseCategoryData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.createTypedArrayList(ModelCategoryData.CREATOR);
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

    public static final Creator<ResponseCategoryData> CREATOR = new Creator<ResponseCategoryData>() {
        @Override
        public ResponseCategoryData createFromParcel(Parcel in) {
            return new ResponseCategoryData(in);
        }

        @Override
        public ResponseCategoryData[] newArray(int size) {
            return new ResponseCategoryData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<ModelCategoryData> getData() {
        return data;
    }
}
