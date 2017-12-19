package com.hornak.prototype;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;
import com.hornak.prototype.model.teams.QuizData;
import com.hornak.prototype.model.teams.TeamData;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_PAST;
import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;
import static com.hornak.prototype.MainActivity.mTeamData;

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
    boolean isMyTeamRegistered = false;
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

        this.signUpTeamButton.setClickable(false);
        setupTeams();
    }

    private void checkSignUpTeamButtonLogic() {
        try {
            checkMyTeamIsRegistered();
            teamPlaceAvailability.setText(String.valueOf(quiz.getTeams().size()).concat("/").concat(String.valueOf(quiz.getNoOfTeams())));
            if (isMyTeamRegistered) {
                signUpTeamButton.setClickable(true);
                signUpTeamButton.setText("ODHLAS");
            } else if (quiz.getNoOfTeams() <= quiz.getTeams().size()) {
                signUpTeamButton.setClickable(false);
                signUpTeamButton.setText("PLNO");
            } else {
                signUpTeamButton.setClickable(true);
                signUpTeamButton.setText("PRIHLAS MA");
            }
        } catch (NullPointerException e) {
        }
    }

    private void checkMyTeamIsRegistered() {
        isMyTeamRegistered = false;
        for (Team a : quiz.getTeams()) {
            if (a.equals(mTeamData)) {
                isMyTeamRegistered = true;
            }
        }
    }

    private void setupTeams() {
        DatabaseReference databaseReference = (FirebaseDatabase.getInstance().getReference(QUIZZES_KEY_FUTURE.concat("/").concat(quiz.getName())));
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                quiz = snapshot.getValue(Quiz.class);
                teamsPlaceholder.removeAllViews();
                try {
                    for (Team team : quiz.getTeams()) {
                        quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line, teamsPlaceholder, false);
                        ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
                        teamsPlaceholder.addView(quizTeamLayout);
                    }
                } catch (NullPointerException e) {
                }
                checkSignUpTeamButtonLogic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signUpTeamClick(View view) {
        //todo - validate if there is room
        if (mTeamData == null) {
            Toast.makeText(getApplicationContext(), "Nemas registrovany team!", Toast.LENGTH_LONG).show();
        } else if (!isMyTeamRegistered) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setCancelable(true);
            builder.setMessage("Prihlasit team?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            quiz.getTeams().add(new Team(mTeamData));
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(QUIZZES_KEY_FUTURE).child(quiz.getName().concat("/teams/")).setValue(quiz.getTeams());

                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (isMyTeamRegistered) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setCancelable(true);
            builder.setMessage("Odhlasit team?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Iterator tIter = quiz.getTeams().iterator();
                            while (tIter.hasNext()) {
                                if (tIter.next().equals(mTeamData)) {
                                    tIter.remove();
                                }
                            }
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(QUIZZES_KEY_FUTURE).child(quiz.getName().concat("/teams/")).setValue(quiz.getTeams());
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void moveToDone(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setCancelable(true);
        builder.setMessage("Presunut Quiz do DONE?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveQuizToDone();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void moveQuizToDone() {
        final ProgressDialog pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        for (Team team : quiz.getTeams()) {
            db.getReference(QUIZZES_TEAMS.concat("/").concat(team.getName()).concat("/quizDatas/")).child(quiz.getName()).setValue(new QuizData(quiz.getName(), team.getPointsAchieved()));
            updateTeamOverallPoints(team, db);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                quiz.isPending = false;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference(QUIZZES_KEY_FUTURE).child(quiz.getName()).removeValue();
                FirebaseHelper fbHelper2 = new FirebaseHelper(database.getReference(QUIZZES_KEY_PAST));
                fbHelper2.save(quiz);
            }
        }).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pd.dismiss();
        this.finish();
    }

    private void updateTeamOverallPoints(final Team team, final FirebaseDatabase db) {
        db.getReference(QUIZZES_TEAMS.concat("/").concat(team.getName())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TeamData temp = dataSnapshot.getValue(TeamData.class);
                int newPoints;
                try {
                    newPoints = temp.points + team.pointsAchieved;
                } catch (NullPointerException e) {
                    newPoints = team.pointsAchieved;
                }
                db.getReference(QUIZZES_TEAMS.concat("/").concat(team.getName())).child("points").setValue(newPoints);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
