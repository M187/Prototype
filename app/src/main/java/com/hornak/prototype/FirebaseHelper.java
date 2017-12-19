package com.hornak.prototype;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.hornak.prototype.model.Admin;
import com.hornak.prototype.model.User;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;
import com.hornak.prototype.model.teams.TeamData;

import java.util.ArrayList;
import java.util.List;

import static com.hornak.prototype.MainActivity.QUIZZES_ADMINS;
import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;
import static com.hornak.prototype.MainActivity.QUIZZES_USERS;

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
                //db.push().setValue(quiz);
                db.child(quiz.getName()).setValue(quiz);
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
        forgeQuizDatas();
        forgeAdminDatas();
        forgeUserDatas();
        forgeTeamDatas();
    }

    private void forgeQuizDatas() {
        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team("myTeam1", "1", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam2", "2", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam3", "3", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam4", "4", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam5", "5", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam6", "6", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam7", "7", (int) (Math.random() * 30)));
        teamList.add(new Team("myTeam8", "8", (int) (Math.random() * 30)));
        Quiz testQuiz = new Quiz("Quiz I", "07-Dec-2017", 10, "Earth", "...", true, teamList);

        List<Team> teamList2 = new ArrayList<>();
        teamList2.add(new Team("myTeam1", "1", 11));
        teamList2.add(new Team("myTeam2", "2", 16));
        Quiz testQuiz2 = new Quiz("Quiz II", "03-Jan-2018", 10, "Earth", "...", true, teamList2);

        List<Team> teamList3 = new ArrayList<>();
        teamList3.add(new Team("myTeam1", "1", 4));
        teamList3.add(new Team("myTeam2", "2", 6));
        Quiz testQuiz3 = new Quiz("Quiz III", "03-Jan-2018", 10, "Earth", "...", false, teamList3);

        List<Team> teamList4 = new ArrayList<>();
        teamList4.add(new Team("myTeam1", "1", 7));
        teamList4.add(new Team("myTeam2", "2", 12));
        Quiz testQuiz4 = new Quiz("Quiz IV", "03-Jan-2018", 10, "Earth", "...", false, teamList4);

        List<Team> teamList5 = new ArrayList<>();
        teamList5.add(new Team("myTeam1", "1", 13));
        teamList5.add(new Team("myTeam2", "2", 16));
        Quiz testQuiz5 = new Quiz("Quiz V", "03-Jan-2018", 10, "Earth", "...", false, teamList5);

        List<Team> teamList6 = new ArrayList<>();
        teamList6.add(new Team("myTeam1", "1", 11));
        teamList6.add(new Team("myTeam2", "2", 9));
        Quiz testQuiz6 = new Quiz("Quiz VI", "03-Jan-2018", 10, "Earth", "...", false, teamList6);

        db.child(testQuiz.getName()).setValue(testQuiz);
        db.child(testQuiz2.getName()).setValue(testQuiz2);
        db.child(testQuiz3.getName()).setValue(testQuiz3);
        db.child(testQuiz4.getName()).setValue(testQuiz4);
        db.child(testQuiz5.getName()).setValue(testQuiz5);
        db.child(testQuiz6.getName()).setValue(testQuiz6);
    }

    private void forgeAdminDatas() {
        ArrayList<Admin> testAdminList = new ArrayList<>();
        testAdminList.add(new Admin("4b9f2ece-33e1-4f03-abda-b61e86c0f8ab", "dony66@gmail.com"));
        testAdminList.add(new Admin("1-2-3-4-5", "DrBu@gmail.com"));
        testAdminList.add(new Admin("1-2-3-4-4", "DrHO@gmail.com"));
        db.getDatabase().getReference(QUIZZES_ADMINS).setValue(testAdminList);
    }

    private void forgeTeamDatas() {
        ArrayList<TeamData> testTeamList = new ArrayList<>();
        ArrayList<String> testUserRegisteredToTeam1 = new ArrayList<>();
        testUserRegisteredToTeam1.add("DrBuServant1@gmail-com");
        testUserRegisteredToTeam1.add("DrBuServant2@gmail-com");
        TeamData testTeamData1 = new TeamData("myTeam1", "1", "DrBu@gmail.com", 0);
        testTeamData1.usersRegistered = testUserRegisteredToTeam1;
        testTeamList.add(testTeamData1);
        testTeamList.add(new TeamData("myTeam2", "2", "DrHo@gmail.com", 0));
        db.getDatabase().getReference(QUIZZES_TEAMS).setValue(testTeamList);
    }

    private void forgeUserDatas() {
        ArrayList<User> testUserList = new ArrayList<>();
        testUserList.add(new User("3-3-4-5-6", "DrBuServant1@gmail.com"));
        testUserList.add(new User("2-3-4-5-6", "DrBuServant2@gmail.com"));
        db.getDatabase().getReference(QUIZZES_USERS).setValue(testUserList);
    }
}
