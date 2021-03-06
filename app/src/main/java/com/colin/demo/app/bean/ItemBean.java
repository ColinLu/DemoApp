package com.colin.demo.app.bean;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述：
 * <p>
 * 作者：Colin
 * 时间：2018/6/21
 */
public class ItemBean extends Object implements Parcelable {
    public int id;
    public String title;
    public String value;
    public String description;
    public Class<? extends Activity> clazz;

    public ItemBean(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public ItemBean(String title, String value,String description) {
        this.title = title;
        this.value = value;
        this.description = description;
    }

    public ItemBean(int id, String title, Class<? extends Activity> clazz) {
        this.id = id;
        this.title = title;
        this.clazz = clazz;
    }

    protected ItemBean(Parcel in) {
        id = in.readInt();
        title = in.readString();
        value = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(value);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemBean> CREATOR = new Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };
}
