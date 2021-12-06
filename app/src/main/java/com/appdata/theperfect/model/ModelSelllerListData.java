package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelSelllerListData implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("town")
    private String town;

    @SerializedName("email")
    private String email;

    @SerializedName("Profileimage")
    private String Profileimage;

    @SerializedName("role_id")
    private String role_id;

    @SerializedName("Active")
    private String Active;

    @SerializedName("RatingStar")
    private String RatingStar;

    @SerializedName("businessname")
    private String businessname;

    @SerializedName("AppointmentDate")
    private String AppointmentDate;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("Longitute")
    private String Longitute;

    @SerializedName("AppointTimeId")
    private String AppointTimeId;

    @SerializedName("TimeSlot")
    private String TimeSlot;

    @SerializedName("BuyerID")
    private String BuyerID;

    @SerializedName("SellerID")
    private String SellerID;

    @SerializedName("appointmentid")
    private String appointmentid;

    @SerializedName("cdate")
    private String cdate;

    protected ModelSelllerListData(Parcel in) {
        id = in.readString();
        name = in.readString();
        town = in.readString();
        email = in.readString();
        Profileimage = in.readString();
        role_id = in.readString();
        Active = in.readString();
        RatingStar = in.readString();
        businessname = in.readString();
        AppointmentDate = in.readString();
        Latitude = in.readString();
        Longitute = in.readString();
        AppointTimeId = in.readString();
        TimeSlot = in.readString();
        BuyerID = in.readString();
        SellerID = in.readString();
        appointmentid = in.readString();
        cdate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(town);
        dest.writeString(email);
        dest.writeString(Profileimage);
        dest.writeString(role_id);
        dest.writeString(Active);
        dest.writeString(RatingStar);
        dest.writeString(businessname);
        dest.writeString(AppointmentDate);
        dest.writeString(Latitude);
        dest.writeString(Longitute);
        dest.writeString(AppointTimeId);
        dest.writeString(TimeSlot);
        dest.writeString(BuyerID);
        dest.writeString(SellerID);
        dest.writeString(appointmentid);
        dest.writeString(cdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelSelllerListData> CREATOR = new Creator<ModelSelllerListData>() {
        @Override
        public ModelSelllerListData createFromParcel(Parcel in) {
            return new ModelSelllerListData(in);
        }

        @Override
        public ModelSelllerListData[] newArray(int size) {
            return new ModelSelllerListData[size];
        }
    };

    public String getBusinessname() {
        return businessname;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public String getProfileimage() {
        return Profileimage;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getActive() {
        return Active;
    }

    public String getRatingStar() {
        return RatingStar;
    }

    public String getEmail() {
        return email;
    }

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitute() {
        return Longitute;
    }

    public String getAppointTimeId() {
        return AppointTimeId;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public String getCdate() {
        return cdate;
    }

    public String getAppointmentid() {
        return appointmentid;
    }
}
