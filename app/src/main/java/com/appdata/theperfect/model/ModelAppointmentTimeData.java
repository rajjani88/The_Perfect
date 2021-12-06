package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelAppointmentTimeData implements Parcelable {

    @SerializedName("TimeSlot")
    private String TimeSlot;

    private boolean selected;

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    protected ModelAppointmentTimeData(Parcel in) {
        TimeSlot = in.readString();
    }

    public ModelAppointmentTimeData(){

    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TimeSlot);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelAppointmentTimeData> CREATOR = new Creator<ModelAppointmentTimeData>() {
        @Override
        public ModelAppointmentTimeData createFromParcel(Parcel in) {
            return new ModelAppointmentTimeData(in);
        }

        @Override
        public ModelAppointmentTimeData[] newArray(int size) {
            return new ModelAppointmentTimeData[size];
        }
    };
}