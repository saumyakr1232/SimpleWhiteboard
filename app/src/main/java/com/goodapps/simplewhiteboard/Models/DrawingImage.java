package com.goodapps.simplewhiteboard.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;


public class DrawingImage implements Parcelable {
    public static final Creator<DrawingImage> CREATOR = new Creator<DrawingImage>() {
        @Override
        public DrawingImage createFromParcel(Parcel in) {
            return new DrawingImage(in);
        }

        @Override
        public DrawingImage[] newArray(int size) {
            return new DrawingImage[size];
        }
    };
    private String name;
    private final Timestamp timestamp;
    private final String url;


    public DrawingImage(String name, String url) {
        this.timestamp = Timestamp.now();
        this.name = name;
        this.url = url;
    }

    protected DrawingImage(Parcel in) {
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        name = in.readString();
        url = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "DrawingImage{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(timestamp, flags);
        dest.writeString(name);
        dest.writeString(url);
    }
}
