package com.example.homepc.tutti;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maklumi on 29-03-16.
 */
public class ItemsModel implements Parcelable {

    private String imageFile;
    private String title, date, price, objectId, phone, desc;

    protected ItemsModel(Parcel in) {
        String[] array = new String[7];
        in.readStringArray(array);
        title = array[0];
        price = array[1];
        date  = array[2];
        imageFile = array[3];
        objectId = array[4];
        phone = array[5];
        desc = array[6];
    }

    public ItemsModel() {

    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static final Creator<ItemsModel> CREATOR = new Creator<ItemsModel>() {
        @Override
        public ItemsModel createFromParcel(Parcel in) {
            return new ItemsModel(in);
        }

        @Override
        public ItemsModel[] newArray(int size) {
            return new ItemsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {title, price, date, imageFile, objectId, phone, desc});
    }
}
