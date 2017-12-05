package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY;

/**
 * Created by michal.hornak on 12/4/2017.
 */

public class AddQuizActivity extends AppCompatActivity {

    EditText mName;
    EditText mDate;
    EditText mMaxTeams;
    EditText mPlace;
    EditText mTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_add_quiz);

        mName = (EditText) findViewById(R.id.quiz_name_edit);
        mDate = (EditText) findViewById(R.id.quiz_date_edit);
        mMaxTeams = (EditText) findViewById(R.id.quiz_number_of_teams_edit);
        mPlace = (EditText) findViewById(R.id.quiz_place_edit);
        mTheme = (EditText) findViewById(R.id.quiz_theme_edit);

    }

    public void addQuiz(View view) {

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseHelper fbHelper = new FirebaseHelper(database.getReference(QUIZZES_KEY));

        String name = mName.getText().toString();
        String date = mDate.toString();
        String noOfTeams = mMaxTeams.getText().toString();
        String place = mPlace.getText().toString();
        String theme = mTheme.toString();

        //todo: initialize listOfTeams?
        Quiz newQuiz = new Quiz(name, date, noOfTeams, place, theme, null);

        //Quiz newQuiz = new Quiz("toTest", "heute", "2", "Earth", "Andoid", null);
        //todo - validations

        fbHelper.save(newQuiz);
        Toast.makeText(getApplicationContext(), "Entry succ. added!", Toast.LENGTH_LONG);
    }
}
