package com.constructefile.democonstract.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Optimus Prime on 4/12/2017.
 */

public class Gets implements Parcelable {

    public String id;
    public String operator_id;
    public String supervisor_id;
    public String date;
    public String description;

    public Gets() {
    }

    protected Gets(Parcel in) {
        id = in.readString();
        operator_id = in.readString();
        supervisor_id = in.readString();
        date = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(operator_id);
        dest.writeString(supervisor_id);
        dest.writeString(date);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gets> CREATOR = new Creator<Gets>() {
        @Override
        public Gets createFromParcel(Parcel in) {
            return new Gets(in);
        }

        @Override
        public Gets[] newArray(int size) {
            return new Gets[size];
        }
    };
}