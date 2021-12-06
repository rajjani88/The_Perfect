package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelAppointmentSave implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("BuyerID")
    private String BuyerID;

    @SerializedName("SellerID")
    private String SellerID;

    @SerializedName("AppointmentDate")
    private String AppointmentDate;

    @SerializedName("AppointTimeId")
    private String AppointTimeId;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("Longitute")
    private String Longitute;

    @SerializedName("STATUS")
    private String STATUS;

    protected ModelAppointmentSave(Parcel in) {
        id = in.readString();
        BuyerID = in.readString();
        SellerID = in.readString();
        AppointmentDate = in.readString();
        AppointTimeId = in.readString();
        Latitude = in.readString();
        Longitute = in.readString();
        STATUS = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(BuyerID);
        dest.writeString(SellerID);
        dest.writeString(AppointmentDate);
        dest.writeString(AppointTimeId);
        dest.writeString(Latitude);
        dest.writeString(Longitute);
        dest.writeString(STATUS);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelAppointmentSave> CREATOR = new Creator<ModelAppointmentSave>() {
        @Override
        public ModelAppointmentSave createFromParcel(Parcel in) {
            return new ModelAppointmentSave(in);
        }

        @Override
        public ModelAppointmentSave[] newArray(int size) {
            return new ModelAppointmentSave[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getAppointTimeId() {
        return AppointTimeId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitute() {
        return Longitute;
    }

    public String getSTATUS() {
        return STATUS;
    }
}