package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseOauthLogin implements Parcelable {

    @SerializedName("Message")
    private String Message;

    @SerializedName("Error")
    private boolean error;

    @SerializedName("data")
    private ModelUser data;

    protected ResponseOauthLogin(Parcel in) {
        Message = in.readString();
        error = in.readByte() != 0;
        data = in.readParcelable(ModelUser.class.getClassLoader());
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

    public static final Creator<ResponseOauthLogin> CREATOR = new Creator<ResponseOauthLogin>() {
        @Override
        public ResponseOauthLogin createFromParcel(Parcel in) {
            return new ResponseOauthLogin(in);
        }

        @Override
        public ResponseOauthLogin[] newArray(int size) {
            return new ResponseOauthLogin[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public boolean isError() {
        return error;
    }

    public ModelUser getData() {
        return data;
    }
}
