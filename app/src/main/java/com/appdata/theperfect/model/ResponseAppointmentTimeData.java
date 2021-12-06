package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ResponseAppointmentTimeData implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ArrayList<ModelAppointmentTimeData> data;


    protected ResponseAppointmentTimeData(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.createTypedArrayList(ModelAppointmentTimeData.CREATOR);
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

    public static final Creator<ResponseAppointmentTimeData> CREATOR = new Creator<ResponseAppointmentTimeData>() {
        @Override
        public ResponseAppointmentTimeData createFromParcel(Parcel in) {
            return new ResponseAppointmentTimeData(in);
        }

        @Override
        public ResponseAppointmentTimeData[] newArray(int size) {
            return new ResponseAppointmentTimeData[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ArrayList<ModelAppointmentTimeData> getData() {
        return data;
    }
}
