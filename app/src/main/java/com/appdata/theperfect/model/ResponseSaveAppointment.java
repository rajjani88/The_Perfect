package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSaveAppointment implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ModelAppointmentSave data;


    protected ResponseSaveAppointment(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.readParcelable(ModelAppointmentSave.class.getClassLoader());
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

    public static final Creator<ResponseSaveAppointment> CREATOR = new Creator<ResponseSaveAppointment>() {
        @Override
        public ResponseSaveAppointment createFromParcel(Parcel in) {
            return new ResponseSaveAppointment(in);
        }

        @Override
        public ResponseSaveAppointment[] newArray(int size) {
            return new ResponseSaveAppointment[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ModelAppointmentSave getData() {
        return data;
    }
}
