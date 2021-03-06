package com.hornak.prototype;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;
import com.hornak.prototype.model.teams.QuizData;
import com.hornak.prototype.model.teams.TeamData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_PAST;
import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;
import static com.hornak.prototype.MainActivity.mTeamData;
import static com.hornak.prototype.MainActivity.mUserData;

/**
 * Created by michal.hornak on 11/28/2017.
 */

public class PendingQuizDetailActivity extends AppCompatActivity {

    private static final String IMAGE_DIRECTORY_NAME = "AndroidCamera";
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
    @BindView(R.id.move_to_done_button)
    Button moveToDoneButton;
    @BindView(R.id.header_layout)
    LinearLayout headerLayout;
    View quizTeamLayout;
    boolean isMyTeamRegistered = false;
    FirebaseStorage storageRef = FirebaseStorage.getInstance();
    private Quiz quiz;
    private String pictureTeamName = "default";
    private Uri file;

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

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

        if (mUserData.admin) {
            moveToDoneButton.setVisibility(View.VISIBLE);
        } else {
            moveToDoneButton.setVisibility(View.GONE);
        }
        headerLayout.invalidate();
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
                    addTeamsHeader();
                    if (mUserData.isAdmin()) {
                        createTeamLayoutForAdmin();
                    } else {
                        createTeamLayoutBasic();
                    }
                } catch (NullPointerException e) {
                }
                checkSignUpTeamButtonLogic();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

            private void addTeamsHeader() {
                quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line_quizzes, teamsPlaceholder, false);
                ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText("Meno");
                ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setTypeface(null, Typeface.BOLD);
                ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText("Body");
                ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setTypeface(null, Typeface.BOLD);

                //LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                //float d = getApplicationContext().getResources().getDisplayMetrics().density;
                //llp.setMargins((int) (15 * d), 0, 0, 0);
                //quizTeamLayout.setLayoutParams(llp);

                teamsPlaceholder.addView(quizTeamLayout);
            }

            private void createTeamLayoutForAdmin() {
                for (Team team : quiz.getTeams()) {
                    quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line_quizzes, teamsPlaceholder, false);

                    Glide.with(getApplicationContext())
                            .load("https://firebasestorage.googleapis.com/v0/b/pubquizapp-8129c.appspot.com/o/testData.jpg?alt=media&token=4907ee29-bdfa-48a5-b7e8-f2f51e3e7c34")
                            .into((ImageView) quizTeamLayout.findViewById(R.id.team_photo));

                    ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
                    try {
                        if (team.getPointsAchieved() > 0) {
                            ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(String.valueOf(team.getPointsAchieved()));
                        }
                    } catch (NullPointerException e) {
                    }
                    quizTeamLayout.findViewById(R.id.edit_team_points).setVisibility(View.VISIBLE);
                    quizTeamLayout.findViewById(R.id.take_picture_of_team).setVisibility(View.VISIBLE);
                    quizTeamLayout.invalidate();
                    teamsPlaceholder.addView(quizTeamLayout);
                }
            }

            private void createTeamLayoutBasic() {
                for (Team team : quiz.getTeams()) {
                    quizTeamLayout = getLayoutInflater().inflate(R.layout.team_line_quizzes, teamsPlaceholder, false);
                    ((TextView) quizTeamLayout.findViewById(R.id.team_name)).setText(team.getName());
                    try {
                        if (team.getPointsAchieved() > 0) {
                            ((TextView) quizTeamLayout.findViewById(R.id.team_points)).setText(String.valueOf(team.getPointsAchieved()));
                        }
                    } catch (NullPointerException e) {
                    }
                    teamsPlaceholder.addView(quizTeamLayout);
                }
            }
        });
    }

    public void signUpTeamClick(View view) {
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

    public void editPoints(View view) {
        final String teamName = ((TextView) ((ViewGroup) view.getParent().getParent()).findViewById(R.id.team_name)).getText().toString();
        final EditText taskEditText = new EditText(this);

        AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setCancelable(true)
                .setMessage("Zadaj dosiahnute body teamu.")
                .setView(taskEditText)
                .setPositiveButton("Ano",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    for (Team team : quiz.getTeams()) {
                                        if (team.name.equals(teamName)) {
                                            team.pointsAchieved = Integer.valueOf(taskEditText.getText().toString());
                                        }
                                    }
                                    FirebaseDatabase.getInstance().getReference(QUIZZES_KEY_FUTURE.concat("/").concat(quiz.getName()).concat("/teams")).setValue(quiz.getTeams());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Zle data.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();

    }

    public void takeTeamPicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        file = Uri.fromFile(getOutputMediaFile(1)); //(getOutputMediaFile(1));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        pictureTeamName = ((TextView)((LinearLayout)view.getParent()).findViewById(R.id.team_name)).getText().toString();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                StorageReference riversRef = storageRef.getReference().child("images/" + file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Upload failure!", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getApplicationContext(), "Upload success!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
