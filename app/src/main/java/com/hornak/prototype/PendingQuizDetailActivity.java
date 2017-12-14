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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_PAST;
import static com.hornak.prototype.MainActivity.mTeam;

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
    private String mUID;

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

        //this.mUID = mFirebaseUser.getUid();
        this.mUID = "4b9f2ece-33e1-4f03-abda-b61e86c0f8ab";

        this.signUpTeamButton.setClickable(false);
        setupTeams();
    }

    private void checkSignUpTeamButtonLogic() {
        try {
            teamPlaceAvailability.setText(String.valueOf(quiz.getTeams().size()).concat("/").concat(String.valueOf(quiz.getNoOfTeams())));
            if (quiz.getNoOfTeams() <= quiz.getTeams().size()) {
                signUpTeamButton.setClickable(false);
                signUpTeamButton.setText("PLNO.");
            } else {
                signUpTeamButton.setClickable(true);
                signUpTeamButton.setText("PRIHLAS MA");
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
                signUpTeamButton.setClickable(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signUpTeamClick(View view) {
        //todo - validate if there is room
        if (mTeam == null) {
            Toast.makeText(getApplicationContext(), "Nemas registrovany team!", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setCancelable(true);
            builder.setMessage("Prihlasit team?");
            builder.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            quiz.getTeams().add(new Team(mTeam));
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
}
