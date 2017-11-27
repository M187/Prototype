package com.hornak.prototype.model.quizzes;

/**
 * Created by michal.hornak on 11/23/2017.
 */

public class Team {

    public String name;
    public String pointsAchieved;

public Team(){}

    public Team(String name, String pointsAchieved){
        this.name = name;
        this.pointsAchieved = pointsAchieved;
    }

        public String getName() {
        return name;
    };

    public String getPointsAchieved() {
        return pointsAchieved;
    }
}
