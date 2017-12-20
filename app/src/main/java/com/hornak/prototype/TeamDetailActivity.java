package com.hornak.prototype;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.User;
import com.hornak.prototype.model.teams.TeamData;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;
import static com.hornak.prototype.MainActivity.QUIZZES_USERS;

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
    @BindView(R.id.memeber_list)
    LinearLayout memberListLL;

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
        setTeamMembers();
        //todo: points
    }

    private void setTeamMembers() {
        Iterator usersMail = mTeamData.getUsersRegistered().iterator();
        while (usersMail.hasNext()) {
            TextView memberEmail = new TextView(getBaseContext());

            float d = getApplicationContext().getResources().getDisplayMetrics().density;
            memberEmail.setText((String) usersMail.next());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            llp.setMargins((int) (15 * d), 0, 0, 0);

            memberEmail.setLayoutParams(llp);
            memberListLL.addView(memberEmail);
        }
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
                        database.getReference(QUIZZES_TEAMS.concat("/").concat(mTeamData.getName())).removeValue();
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
                        final String userMail = memberMailTV.getText().toString().replace(".", "-");
                        if (!userMail.equals("") & !(userMail == null)) {
                            // check if user exist
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference(QUIZZES_USERS.concat("/").concat(userMail));
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user.getTeam() == null || user.getTeam() == "") {
                                            mTeamData.getUsersRegistered().add(userMail);
                                            database.getReference(QUIZZES_TEAMS.concat("/").concat(mTeamData.getName())).setValue(mTeamData);
                                            database.getReference(QUIZZES_USERS.concat("/").concat(userMail).concat("/").concat("team")).setValue(mTeamData.getName());
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Pouzivatel uz je v nejakom teame.", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Pouzivatel neexistuje.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Zly email!", Toast.LENGTH_LONG).show();
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
                                database.getReference(QUIZZES_TEAMS.concat("/").concat(mTeamData.getName())).setValue(mTeamData);
                                database.getReference(QUIZZES_USERS.concat("/").concat(userMail).concat("/").concat("team")).removeValue();

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
