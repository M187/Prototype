package com.hornak.prototype;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal.hornak on 11/27/2017.
 */

public class FirebaseHelper {

    DatabaseReference db;
    Boolean saved = null;
    ArrayList<Quiz> mQuizzes = new ArrayList<>();

    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }

    //WRITE
    public Boolean save(Quiz quiz) {
        if (quiz == null) {
            saved = false;
        } else {
            try {
                db.child("Quiz").push().setValue(quiz);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    //READ
    public ArrayList<Quiz> retrieve() {
        mQuizzes.clear();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return mQuizzes;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        mQuizzes.add(dataSnapshot.getValue(Quiz.class));
    }


    public void makeTestData() {
        //insert data into database - use in admin app
        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team("myTeam1", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam2", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam3", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam4", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam5", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam6", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam7", String.valueOf((int)(Math.random()*30))));
        teamList.add(new Team("myTeam8", String.valueOf((int)(Math.random()*30))));
        Quiz testQuiz = new Quiz("First quiz", "21092017", teamList);

        List<Team> teamList2 = new ArrayList<>();
        teamList2.add(new Team("myTeam1", "11"));
        teamList2.add(new Team("myTeam2", "16"));
        Quiz testQuiz2 = new Quiz("Second quiz", "21092017", teamList2);

        List<Team> teamList3 = new ArrayList<>();
        teamList3.add(new Team("myTeam1", "4"));
        teamList3.add(new Team("myTeam2", "6"));
        Quiz testQuiz3 = new Quiz("Third quiz", "21092017", teamList3);

        List<Team> teamList4 = new ArrayList<>();
        teamList4.add(new Team("myTeam1", "7"));
        teamList4.add(new Team("myTeam2", "12"));
        Quiz testQuiz4 = new Quiz("Fourth quiz", "21092017", teamList4);

        List<Team> teamList5 = new ArrayList<>();
        teamList5.add(new Team("myTeam1", "13"));
        teamList5.add(new Team("myTeam2", "16"));
        Quiz testQuiz5 = new Quiz("Fifth quiz", "21092017", teamList5);

        List<Team> teamList6 = new ArrayList<>();
        teamList6.add(new Team("myTeam1", "11"));
        teamList6.add(new Team("myTeam2", "9"));
        Quiz testQuiz6 = new Quiz("Sixth quiz", "21092017", teamList6);

        db.child(testQuiz.getName()).setValue(testQuiz);
        db.child(testQuiz2.getName()).setValue(testQuiz2);
        db.child(testQuiz3.getName()).setValue(testQuiz3);
        db.child(testQuiz4.getName()).setValue(testQuiz4);
        db.child(testQuiz5.getName()).setValue(testQuiz5);
        db.child(testQuiz6.getName()).setValue(testQuiz6);
    }
}
