package com.hornak.prototype.model.quizzes;

import java.util.List;

/**
 * Created by michal.hornak on 11/23/2017.
 */

public class Quiz {

    public String name;
    public String timestamp;
    public List<Team> teams;

    public Quiz(){}

    public Quiz(String name, String timestamp, List<Team> teams){
        this.name = name;
        this.timestamp = timestamp;
        this.teams = teams;
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

}
