package com.hornak.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        teamPlaceAvailability.setText(String.valueOf(quiz.getTeams().size()).concat("/").concat(quiz.getNoOfTeams()));

        for (Team team : quiz.getTeams()) {
            quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line, teamsPlaceholder, false);
            ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
            ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(team.getPointsAchieved());
            teamsPlaceholder.addView(quizTeamLayout);
        }
    }

    public void signUpTeamClick(View view) {

        //todo - validate if there is room

        Intent temp = new Intent(this, SignUpTeamActivity.class);
        startActivity(temp);
    }
}
