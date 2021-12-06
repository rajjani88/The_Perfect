package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelCountryData implements Parcelable {

    @SerializedName("Data")
    private ArrayList<ModelInnerCountryData> Data;

    protected ModelCountryData(Parcel in) {
        Data = in.createTypedArrayList(ModelInnerCountryData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(Data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelCountryData> CREATOR = new Creator<ModelCountryData>() {
        @Override
        public ModelCountryData createFromParcel(Parcel in) {
            return new ModelCountryData(in);
        }

        @Override
        public ModelCountryData[] newArray(int size) {
            return new ModelCountryData[size];
        }
    };

    public ArrayList<ModelInnerCountryData> getData() {
        return Data;
    }
}