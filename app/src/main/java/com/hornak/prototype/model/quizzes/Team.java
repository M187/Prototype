package com.hornak.prototype.model.quizzes;

import android.os.Parcel;
import android.os.Parcelable;

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
    public String pointsAchieved;

public Team(){}

        public Team(String name, String pointsAchieved){
        this.name = name;
        this.pointsAchieved = pointsAchieved;
    };

    public Team(Parcel in){
        this.name = in.readString();
        this.pointsAchieved =  in.readString();
    }

public String getName() {
        return name;
    }

    public String getPointsAchieved() {
        return pointsAchieved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.pointsAchieved);
    }
}
