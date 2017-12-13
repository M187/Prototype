package com.hornak.prototype.model.teams;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michal.hornak on 12/13/2017.
 */

public class Team implements Parcelable {

    // Parcelling part
    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String name;
    public String uid;
    public String ownerEmail;
    public int pointsAchieved;

    public Team() {
    }

    public Team(String name, String uid, String ownerEmail, int pointsAchieved) {
        this.name = name;
        this.uid = uid;
        this.ownerEmail = ownerEmail;
        this.pointsAchieved = pointsAchieved;
    }

    public Team(Parcel in) {
        this.name = in.readString();
        this.uid = in.readString();
        this.ownerEmail = in.readString();
        this.pointsAchieved = in.readInt();
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public int getPointsAchieved() {
        return pointsAchieved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.uid);
        dest.writeString(this.ownerEmail);
        dest.writeInt(this.pointsAchieved);
    }
}
