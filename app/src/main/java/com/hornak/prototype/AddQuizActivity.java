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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_add_quiz);
    }

    public void addQuiz(View view) {

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseHelper fbHelper = new FirebaseHelper(database.getReference(QUIZZES_KEY));

        String name = ((EditText) view.findViewById(R.id.quiz_name_edit)).getText().toString();
        String date = ((EditText) view.findViewById(R.id.quiz_date_edit)).getText().toString();
        String noOfTeams = ((EditText) view.findViewById(R.id.quiz_number_of_teams_edit)).getText().toString();
        String place = ((EditText) view.findViewById(R.id.quiz_name_edit)).getText().toString();
        String theme = ((EditText) view.findViewById(R.id.quiz_name_edit)).getText().toString();

        //todo: initialize listOfTeams?
        Quiz newQuiz = new Quiz(name, date, noOfTeams, place, theme, null);

        //todo - validations

        fbHelper.save(newQuiz);
        Toast.makeText(getApplicationContext(), "Entry succ. added!", Toast.LENGTH_LONG);
    }
}
