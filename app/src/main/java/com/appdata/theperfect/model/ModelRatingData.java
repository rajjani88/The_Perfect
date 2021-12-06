package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelRatingData implements Parcelable {

    //{"Message":"Your rating submission successfull.",
    // "Error":false,"data":{"RateUserID":"20","Seller_id":"21","RatingStar":"4.0","txtDescription":null}}

    @SerializedName("RateUserID")
    private String RateUserID;

    @SerializedName("Seller_id")
    private String Seller_id;

    @SerializedName("RatingStar")
    private String RatingStar;

    @SerializedName("email")
    private String email;

    @SerializedName("txtDescription")
    private String txtDescription;


    protected ModelRatingData(Parcel in) {
        RateUserID = in.readString();
        Seller_id = in.readString();
        RatingStar = in.readString();
        email = in.readString();
        txtDescription = in.readString();
    }

    public static final Creator<ModelRatingData> CREATOR = new Creator<ModelRatingData>() {
        @Override
        public ModelRatingData createFromParcel(Parcel in) {
            return new ModelRatingData(in);
        }

        @Override
        public ModelRatingData[] newArray(int size) {
            return new ModelRatingData[size];
        }
    };

    public String getRateUserID() {
        return RateUserID;
    }

    public String getSeller_id() {
        return Seller_id;
    }

    public String getRatingStar() {
        return RatingStar;
    }

    public String getEmail() {
        return email;
    }

    public String getTxtDescription() {
        return txtDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RateUserID);
        parcel.writeString(Seller_id);
        parcel.writeString(RatingStar);
        parcel.writeString(email);
        parcel.writeString(txtDescription);
    }
}
