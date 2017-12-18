package com.hornak.prototype.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michal.hornak on 12/17/2017.
 */

public class User implements Parcelable {

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public String uid;
    public String email;
    public String[] teams;

    public User() {
    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email.replace(".", "-");
    }

    public User(Parcel in) {
        uid = in.readString();
        email = in.readString();
        in.readStringArray(teams);
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email.replace(".", "-");
    }

    public String[] getTeams() {
        return teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(email);
        dest.writeSerializable(teams);
    }
}
