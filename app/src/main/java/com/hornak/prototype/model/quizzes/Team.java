package com.hornak.prototype.model.quizzes;

import android.os.Parcel;
import android.os.Parcelable;

import com.hornak.prototype.model.teams.TeamData;

/**
 * Created by michal.hornak on 11/23/2017.
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
    public int pointsAchieved;


    public Team() {
    }

    public Team(String name, String uid, int pointsAchieved) {
        this.name = name;
        this.uid = uid;
        this.pointsAchieved = pointsAchieved;
    }

    public Team(TeamData teamData) {
        this.name = teamData.getName();
        this.uid = teamData.getUid();
        this.pointsAchieved = 0;
    }

    public Team(Parcel in) {
        this.name = in.readString();
        this.uid = in.readString();
        this.pointsAchieved = in.readInt();
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
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
        dest.writeInt(this.pointsAchieved);
    }
}
