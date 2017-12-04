package com.hornak.prototype;

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

public class QuizDetailActivity extends AppCompatActivity {

    @BindView(R.id.quiz_name)
    TextView quizName;
    @BindView(R.id.teams_placeholder)
    LinearLayout teamsPlaceholder;
    View quizTeamLayout;

    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        this.quiz = data.getParcelable("QUIZ");
        quizName.setText(quiz.getName());

        for (Team team : quiz.getTeams()){
            quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line, teamsPlaceholder, false);
            ((TextView)quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
            ((TextView)quizTeamLayout.findViewById(R.id.team_points)).setText(team.getPointsAchieved());
            teamsPlaceholder.addView(quizTeamLayout);
        }
    }
}
