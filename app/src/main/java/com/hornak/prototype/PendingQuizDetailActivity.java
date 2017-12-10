package com.hornak.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_PAST;

/**
 * Created by michal.hornak on 11/28/2017.
 */

public class PendingQuizDetailActivity extends AppCompatActivity {

    @BindView(R.id.quiz_name)
    TextView quizName;
    @BindView(R.id.teams_placeholder)
    LinearLayout teamsPlaceholder;
    @BindView(R.id.quiz_date)
    TextView quizDate;
    @BindView(R.id.quiz_theme)
    TextView quizTheme;
    @BindView(R.id.quiz_place)
    TextView quizPlace;
    @BindView((R.id.team_place_availability))
    TextView teamPlaceAvailability;
    @BindView(R.id.sign_up_team)
    Button signUpTeamButton;

    View quizTeamLayout;

    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_pending);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        this.quiz = data.getParcelable("QUIZ");
        quizName.setText(quiz.getName());
        quizDate.setText("Datum: ".concat(quiz.getTimestamp()));
        quizTheme.setText("Tema: ".concat(quiz.getTheme()));
        quizPlace.setText("Miesto: ".concat(quiz.getPlace()));

        teamPlaceAvailability.setText(String.valueOf(quiz.getTeams().size()).concat("/").concat(String.valueOf(quiz.getNoOfTeams())));

        checkSignUpTeamButtonLogic();

        setupTeams();

        for (Team team : quiz.getTeams()) {
            quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line, teamsPlaceholder, false);
            ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
            ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(team.getPointsAchieved());
            teamsPlaceholder.addView(quizTeamLayout);
        }
    }

    private void checkSignUpTeamButtonLogic() {
        try {
            if (quiz.getNoOfTeams() <= quiz.getTeams().size()) {
                signUpTeamButton.setClickable(false);
                signUpTeamButton.setText("Sry, fulka.");
            } else {
                signUpTeamButton.setClickable(true);
                signUpTeamButton.setText("Add team");
            }
        } catch (NullPointerException e) {
        }
    }

    private void setupTeams() {
        DatabaseReference databaseReference = (FirebaseDatabase.getInstance().getReference(QUIZZES_KEY_FUTURE.concat("/").concat(quiz.getName())));
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                quiz = snapshot.getValue(Quiz.class);
                refreshQuizTeams();
                checkSignUpTeamButtonLogic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signUpTeamClick(View view) {
        //todo - validate if there is room
        Intent temp = new Intent(this, SignUpTeamActivity.class);
        temp.putExtra("QUIZ", this.quiz);
        startActivity(temp);
    }

    public void moveToDone(View view) {
        quiz.isPending = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference(QUIZZES_KEY_FUTURE).child(quiz.getName()).removeValue();
        FirebaseHelper fbHelper2 = new FirebaseHelper(database.getReference(QUIZZES_KEY_PAST));
        fbHelper2.save(quiz);
        this.finish();
    }

    private void refreshQuizTeams() {
        teamsPlaceholder.removeAllViews();
        try {
            for (Team team : quiz.getTeams()) {
                quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line, teamsPlaceholder, false);
                ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
                ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(team.getPointsAchieved());
                teamsPlaceholder.addView(quizTeamLayout);
            }
        } catch (NullPointerException e) {
        }
    }

    public static class SignUpTeamActivity extends AppCompatActivity {

        private Quiz quiz;
        private EditText quizNameInput;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_sign_up_team);

            this.quizNameInput = (EditText) findViewById(R.id.team_name);
            Bundle data = getIntent().getExtras();
            this.quiz = data.getParcelable("QUIZ");
        }

        public void addTeam(View view) {
            quiz.getTeams().add(new Team(quizNameInput.getText().toString(), "0"));
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference(QUIZZES_KEY_FUTURE).child(quiz.getName().concat("/teams")).setValue(this.quiz.getTeams());
            this.finish();
        }
    }
}
