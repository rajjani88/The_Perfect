package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseZipData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ArrayList<ModelZipData> data;

    protected ResponseZipData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.createTypedArrayList(ModelZipData.CREATOR);
    }

    public static final Creator<ResponseZipData> CREATOR = new Creator<ResponseZipData>() {
        @Override
        public ResponseZipData createFromParcel(Parcel in) {
            return new ResponseZipData(in);
        }

        @Override
        public ResponseZipData[] newArray(int size) {
            return new ResponseZipData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<ModelZipData> getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Message);
        parcel.writeByte((byte) (error ? 1 : 0));
        parcel.writeTypedList(data);
    }
}
