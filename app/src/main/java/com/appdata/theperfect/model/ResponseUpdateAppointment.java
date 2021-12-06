package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseUpdateAppointment implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ArrayList<ModelAppointmentSave> data;


    protected ResponseUpdateAppointment(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.createTypedArrayList(ModelAppointmentSave.CREATOR);
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

    public static final Creator<ResponseUpdateAppointment> CREATOR = new Creator<ResponseUpdateAppointment>() {
        @Override
        public ResponseUpdateAppointment createFromParcel(Parcel in) {
            return new ResponseUpdateAppointment(in);
        }

        @Override
        public ResponseUpdateAppointment[] newArray(int size) {
            return new ResponseUpdateAppointment[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<ModelAppointmentSave> getData() {
        return data;
    }
}
