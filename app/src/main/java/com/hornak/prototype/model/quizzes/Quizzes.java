package com.hornak.prototype.model.quizzes;

import java.util.List;

/**
 * Created by michal.hornak on 11/24/2017.
 */

public class Quizzes {

    List<Quiz> quizList;

    public Quizzes() {
    }

    public Quizzes(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }
}
