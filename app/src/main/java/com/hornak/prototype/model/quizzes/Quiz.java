package com.hornak.prototype.model.quizzes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 11/23/2017.
 */

public class Quiz implements Parcelable {

    // Parcelling part
    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String name;
    public String timestamp;
    public String noOfTeams;
    public String place;
    public String theme;
    public List<Team> teams;

    public Quiz(){}

    public Quiz(String name, String timestamp, String noOfTeams, String place, String theme, List<Team> teams) {
        this.name = name;
        this.timestamp = timestamp;
        this.noOfTeams = noOfTeams;
        this.place = place;
        this.theme = theme;
        this.teams = teams;
    }

    public Quiz(Parcel in){
        this.name = in.readString();
        this.timestamp = in.readString();
        this.noOfTeams = in.readString();
        this.place = in.readString();
        this.theme = in.readString();
        this.teams = new ArrayList<>();
        in.readTypedList(teams, Team.CREATOR);
    }

    public String getNoOfTeams() {
        return noOfTeams;
    }

    public String getPlace() {
        return place;
    }

    public String getTheme() {
        return theme;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.timestamp);
        dest.writeString(this.noOfTeams);
        dest.writeString(this.place);
        dest.writeString(this.theme);
        dest.writeTypedList(this.teams);
    }
}
