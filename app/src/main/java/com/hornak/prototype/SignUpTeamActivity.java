package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;

/**
 * Created by michal.hornak on 12/7/2017.
 */

public class SignUpTeamActivity extends AppCompatActivity {

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
