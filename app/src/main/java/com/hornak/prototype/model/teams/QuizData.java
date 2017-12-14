package com.hornak.prototype.model.teams;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by michal.hornak on 12/14/2017.
 */

public class QuizData implements Parcelable {

    // Parcelling part
    public static final Creator<QuizData> CREATOR = new Creator<QuizData>() {
        @Override
        public QuizData createFromParcel(Parcel in) {
            return new QuizData(in);
        }

        @Override
        public QuizData[] newArray(int size) {
            return new QuizData[size];
        }
    };

    public String name;
    public int pointsAchieved;

    public QuizData() {
    }

    public QuizData(Parcel in) {
        this.name = in.readString();
        this.pointsAchieved = in.readInt();
    }

    public String getName() {
        return name;
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

    }
}
