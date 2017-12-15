package com.hornak.prototype.model.teams;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by michal.hornak on 12/13/2017.
 */

public class TeamData implements Parcelable, Comparable {

    // Parcelling part
    public static final Creator<TeamData> CREATOR = new Creator<TeamData>() {
        @Override
        public TeamData createFromParcel(Parcel in) {
            return new TeamData(in);
        }

        @Override
        public TeamData[] newArray(int size) {
            return new TeamData[size];
        }
    };

    public String name;
    public String uid;
    public String ownerEmail;
    public int points;
    public HashMap<String, Object> quizDatas = new HashMap<>();

    public TeamData() {
    }

    public TeamData(String name, String uid, String ownerEmail, int points) {
        this.name = name;
        this.uid = uid;
        this.ownerEmail = ownerEmail;
        this.points = points;
    }

    public TeamData(Parcel in) {
        this.name = in.readString();
        this.uid = in.readString();
        this.ownerEmail = in.readString();
        this.points = in.readInt();
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

    public int getPoints() {
        return points;
    }

    public HashMap<String, Object> getQuizDatas() {
        return quizDatas;
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
        dest.writeInt(this.points);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof TeamData) {
            return this.points - ((TeamData) o).points;
        } else return 1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
