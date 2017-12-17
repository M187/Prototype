package com.hornak.prototype.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michal.hornak on 12/17/2017.
 */

public class Admin implements Parcelable {

    public static final Creator<Admin> CREATOR = new Creator<Admin>() {
        @Override
        public Admin createFromParcel(Parcel in) {
            return new Admin(in);
        }

        @Override
        public Admin[] newArray(int size) {
            return new Admin[size];
        }
    };
    public String uid;
    public String email;

    public Admin() {
    }

    public Admin(Parcel in) {
        uid = in.readString();
        email = in.readString();
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(email);
    }
}
