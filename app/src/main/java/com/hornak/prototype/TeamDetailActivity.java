package com.hornak.prototype;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.teams.TeamData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;

/**
 * Created by michal.hornak on 12/14/2017.
 */

public class TeamDetailActivity extends AppCompatActivity {

    TeamData mTeamData;
    String mUid;

    @BindView(R.id.team_name)
    TextView teamNameTV;
    @BindView(R.id.team_points_overall)
    TextView overallPointsTV;
    @BindView(R.id.member_mail)
    EditText memberMailTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTeamData = MainActivity.mTeamData;
        //mUid = mFirebaseUser.getUid();
        mUid = "4b9f2ece-33e1-4f03-abda-b61e86c0f8ab";

        this.setContentView(R.layout.activity_team_detail);
        ButterKnife.bind(this);

        teamNameTV.setText(mTeamData.getName());
        overallPointsTV.setText(String.valueOf(mTeamData.getPoints()));

        //todo: points
    }


    public void eraseTeam(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setCancelable(true);
        builder.setMessage("Vymazat team? Je to nevratne!");
        builder.setPositiveButton("Ano",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference(QUIZZES_TEAMS.concat("/").concat(mUid)).removeValue();
                        finish();
                    }
                });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addUserToTeam(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setCancelable(true);
        builder.setMessage("Pridat clena teamu?");
        builder.setPositiveButton("Ano",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userMail = memberMailTV.getText().toString();
                        if (!userMail.equals("") & !(userMail == null)) {
                            mTeamData.getUsersRegistered().add(userMail);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(QUIZZES_TEAMS.concat("/").concat(mUid)).setValue(mTeamData);
                        } else {
                            Toast.makeText(getApplicationContext(), "Nedobry email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void removeUserFromTeam(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setCancelable(true);
        builder.setMessage("Odstranit clena teamu?");
        builder.setPositiveButton("Ano",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userMail = memberMailTV.getText().toString();
                        if (!userMail.equals("") & !(userMail == null)) {
                            if (mTeamData.getUsersRegistered().remove(userMail)) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference(QUIZZES_TEAMS.concat("/").concat(mUid)).setValue(mTeamData);
                            } else {
                                Toast.makeText(getApplicationContext(), "Clen s takymto emailom v teame nieje!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Nedobry email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
