package com.hornak.prototype;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.hornak.prototype.model.User;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;
import com.hornak.prototype.model.teams.TeamData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void makeTestData() {
        //insert data into database - use in admin app
        forgeQuizDatas();
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

    private void forgeTeamDatas() {
        HashMap<String, TeamData> testTeamMap = new HashMap<>();
        ArrayList<String> testUserRegisteredToTeam1 = new ArrayList<>();
        testUserRegisteredToTeam1.add("DrBuServant1@gmail-com");
        testUserRegisteredToTeam1.add("DrBuServant2@gmail-com");
        TeamData testTeamData1 = new TeamData("myTeam1", "1", "DrBu@gmail.com", 0);
        testTeamData1.usersRegistered = testUserRegisteredToTeam1;
        testTeamMap.put("1-2-3-4-5", testTeamData1);
        testTeamMap.put("1-2-3-4-4", new TeamData("myTeam2", "2", "DrHo@gmail.com", 0));
        db.getDatabase().getReference(QUIZZES_TEAMS).setValue(testTeamMap);
    }

    private void forgeUserDatas() {
        HashMap<String, User> testUserList = new HashMap<>();
        User user;
        user = new User("4b9f2ece-33e1-4f03-abda-b61e86c0f8ab", "dony66@gmail.com", true);
        testUserList.put(user.getEmail(), user);
        user = new User("1-2-3-4-5", "DrBu@gmail.com", true);
        testUserList.put(user.getEmail(), user);
        user = new User("1-2-3-4-4", "DrHO@gmail.com", true);
        testUserList.put(user.getEmail(), user);
        user = new User("3-3-4-5-6", "DrBuServant1@gmail.com", false);
        testUserList.put(user.getEmail(), user);
        user = new User("2-3-4-5-6", "DrBuServant2@gmail.com", false);
        testUserList.put(user.getEmail(), user);
        db.getDatabase().getReference(QUIZZES_USERS).setValue(testUserList);
    }
}
