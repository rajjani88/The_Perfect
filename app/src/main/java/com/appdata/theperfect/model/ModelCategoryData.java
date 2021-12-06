package com.appdata.theperfect.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelCategoryData implements Parcelable {

    @SerializedName("categoryid")
    private int CategoryId;

    @SerializedName("categoryname")
    private String CategoryName;

    @SerializedName("iconimage")
    private String IconImage;

    private String title;

    private boolean selected;

    public ModelCategoryData(){

    }


    protected ModelCategoryData(Parcel in) {
        CategoryId = in.readInt();
        CategoryName = in.readString();
        IconImage = in.readString();
        title = in.readString();
        selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(CategoryId);
        dest.writeString(CategoryName);
        dest.writeString(IconImage);
        dest.writeString(title);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelCategoryData> CREATOR = new Creator<ModelCategoryData>() {
        @Override
        public ModelCategoryData createFromParcel(Parcel in) {
            return new ModelCategoryData(in);
        }

        @Override
        public ModelCategoryData[] newArray(int size) {
            return new ModelCategoryData[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //{"categoryid":"1","categoryname":"Real Estate","iconimage":"Noimages.png","status":"1"},{"categoryid":"2","categoryname":"Car & truck","iconimage":"Noimages.png","status":"1"},
    // {"categoryid":"3","categoryname":"R.V","iconimage":"Noimages.png","status":"1"}

    public int getCategoryId() {
        return CategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public String getIconImage() {
        return IconImage;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public void setIconImage(String iconImage) {
        IconImage = iconImage;
    }
}

// {
//            "CategoryId": 1,
//            "CategoryName": "Real Estate",
//            "IconImage": "http://mobileapi.trivaan.in/images/CategoryIcon/RealEstate.png"
//        }
