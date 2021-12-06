package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelInnerCountryData implements Parcelable {

    @SerializedName("value")
    private String value;

    @SerializedName("id")
    private int id;

    @SerializedName("type")
    private int type;

    @SerializedName("parentid")
    private int parentid;

    @Override
    public String toString() {
        return this.value; // What to display in the Spinner list.
    }

    protected ModelInnerCountryData(Parcel in) {
        value = in.readString();
        id = in.readInt();
        type = in.readInt();
        parentid = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeInt(parentid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelInnerCountryData> CREATOR = new Creator<ModelInnerCountryData>() {
        @Override
        public ModelInnerCountryData createFromParcel(Parcel in) {
            return new ModelInnerCountryData(in);
        }

        @Override
        public ModelInnerCountryData[] newArray(int size) {
            return new ModelInnerCountryData[size];
        }
    };

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getParentid() {
        return parentid;
    }
}