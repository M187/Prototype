package com.hornak.prototype;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 11/28/2017.
 */

public class PastQuizDetailActivity extends AppCompatActivity {

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
        quizDate.setText("Datum: ".concat(quiz.getTimestamp()));
        quizTheme.setText("Tema: ".concat(quiz.getTheme()));
        quizPlace.setText("Miesto: ".concat(quiz.getPlace()));

        for (Team team : quiz.getTeams()) {
            quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line_quizzes, teamsPlaceholder, false);

            Glide.with(getApplicationContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/pubquizapp-8129c.appspot.com/o/testData.jpg?alt=media&token=4907ee29-bdfa-48a5-b7e8-f2f51e3e7c34")
                    .into((ImageView) quizTeamLayout.findViewById(R.id.team_photo));

            ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
            ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(String.valueOf(team.getPointsAchieved()));
            teamsPlaceholder.addView(quizTeamLayout);
        }
    }

    public void rate(View view) {
        final Dialog rankDialog = new Dialog(this, R.style.myDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar) rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(3);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        //text.setText(name);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }
}
