package com.constructefile.democonstract.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Optimus Prime on 4/24/2017.
 */

public class GetsHour implements Parcelable {


    public String id;
    public String date;
    public String supervisor;
    public String remarks;
    public String updatedOn;
    public String name;
    public String job;
    public String clockInTime;
    public String clockOutTIme;
    public String totalTime;


    public GetsHour() {
    }

    protected GetsHour(Parcel in) {
        id = in.readString();
        name = in.readString();
        supervisor = in.readString();
        date = in.readString();
        remarks = in.readString();
        updatedOn = in.readString();
        job = in.readString();
        clockInTime = in.readString();
        clockOutTIme = in.readString();
        totalTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(supervisor);
        dest.writeString(date);
        dest.writeString(remarks);
        dest.writeString(updatedOn);
        dest.writeString(job);
        dest.writeString(clockInTime);
        dest.writeString(clockOutTIme);
        dest.writeString(totalTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetsHour> CREATOR = new Creator<GetsHour>() {
        @Override
        public GetsHour createFromParcel(Parcel in) {
            return new GetsHour(in);
        }

        @Override
        public GetsHour[] newArray(int size) {
            return new GetsHour[size];
        }
    };
}