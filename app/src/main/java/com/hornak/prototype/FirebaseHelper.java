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
        teamList.add(new Team("myTeam1", "10"));
        teamList.add(new Team("myTeam2", "15"));
        Quiz testQuiz = new Quiz("First quiz press", "21092017", teamList);

        List<Team> teamList2 = new ArrayList<>();
        teamList2.add(new Team("myTeam1", "11"));
        teamList2.add(new Team("myTeam2", "16"));
        Quiz testQuiz2 = new Quiz("Second quiz press", "21092017", teamList2);

        List<Quiz> quizzList = new ArrayList<>();
        db.child("Qiuz").setValue(testQuiz);
        db.child("Quiz").setValue(testQuiz2);
    }
}
