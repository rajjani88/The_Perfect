package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelZipData implements Parcelable {

    @SerializedName("ZipCodeName")
    private String ZipCodeName;

    @Override
    public String toString() {
        return this.ZipCodeName; // What to display in the Spinner list.
    }

    public String getZipCodeName() {
        return ZipCodeName;
    }

    protected ModelZipData(Parcel in) {
        ZipCodeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ZipCodeName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelZipData> CREATOR = new Creator<ModelZipData>() {
        @Override
        public ModelZipData createFromParcel(Parcel in) {
            return new ModelZipData(in);
        }

        @Override
        public ModelZipData[] newArray(int size) {
            return new ModelZipData[size];
        }
    };
}