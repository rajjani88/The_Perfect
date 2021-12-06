package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelUser implements Parcelable {

    @SerializedName("LoginId")
    private int LoginId;

    @SerializedName("Userid")
    private String UserId;

    @SerializedName("Password")
    private String Password;

    @SerializedName("Username")
    private String UserName;

    @SerializedName("MobileNo")
    private String MobileNo;

    @SerializedName("AlternateMobile")
    private String AlternateMobile;

    @SerializedName("EmailId")
    private String EmailID;

    @SerializedName("Role")
    private String Role;

    @SerializedName("Address")
    private String Address;

    @SerializedName("Province")
    private String Province;

    @SerializedName("Town")
    private String Town;

    @SerializedName("Zipcode")
    private String ZipCode;

    @SerializedName("Country")
    private String Country;

    @SerializedName("Occupation")
    private String Occupation;

    @SerializedName("LoopkingFor")
    private String LoopkingFor;

    @SerializedName("BusinessName")
    private String BusinessName;

    @SerializedName("ProductName")
    private String ProductName;

    @SerializedName("BusinessLicence")
    private String BusinessLicence;

    @SerializedName("EmergencyContactNo")
    private String EmergencyContactNo;


    protected ModelUser(Parcel in) {
        LoginId = in.readInt();
        UserId = in.readString();
        Password = in.readString();
        UserName = in.readString();
        MobileNo = in.readString();
        AlternateMobile = in.readString();
        EmailID = in.readString();
        Role = in.readString();
        Address = in.readString();
        Province = in.readString();
        Town = in.readString();
        ZipCode = in.readString();
        Country = in.readString();
        Occupation = in.readString();
        LoopkingFor = in.readString();
        BusinessName = in.readString();
        ProductName = in.readString();
        BusinessLicence = in.readString();
        EmergencyContactNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LoginId);
        dest.writeString(UserId);
        dest.writeString(Password);
        dest.writeString(UserName);
        dest.writeString(MobileNo);
        dest.writeString(AlternateMobile);
        dest.writeString(EmailID);
        dest.writeString(Role);
        dest.writeString(Address);
        dest.writeString(Province);
        dest.writeString(Town);
        dest.writeString(ZipCode);
        dest.writeString(Country);
        dest.writeString(Occupation);
        dest.writeString(LoopkingFor);
        dest.writeString(BusinessName);
        dest.writeString(ProductName);
        dest.writeString(BusinessLicence);
        dest.writeString(EmergencyContactNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelUser> CREATOR = new Creator<ModelUser>() {
        @Override
        public ModelUser createFromParcel(Parcel in) {
            return new ModelUser(in);
        }

        @Override
        public ModelUser[] newArray(int size) {
            return new ModelUser[size];
        }
    };

    public int getLoginId() {
        return LoginId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getPassword() {
        return Password;
    }

    public String getUserName() {
        return UserName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getAlternateMobile() {
        return AlternateMobile;
    }

    public String getEmailID() {
        return EmailID;
    }

    public String getRole() {
        return Role;
    }

    public String getAddress() {
        return Address;
    }

    public String getProvince() {
        return Province;
    }

    public String getTown() {
        return Town;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public String getCountry() {
        return Country;
    }

    public String getOccupation() {
        return Occupation;
    }

    public String getLoopkingFor() {
        return LoopkingFor;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getBusinessLicence() {
        return BusinessLicence;
    }

    public String getEmergencyContactNo() {
        return EmergencyContactNo;
    }
}


