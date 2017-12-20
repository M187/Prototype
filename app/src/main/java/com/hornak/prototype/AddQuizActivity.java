package com.hornak.prototype;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.quizzes.Team;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.hornak.prototype.MainActivity.DATE_FORMAT;
import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;

/**
 * Created by michal.hornak on 12/4/2017.
 */

public class AddQuizActivity extends AppCompatActivity {

    EditText mName;
    Button mDate;
    EditText mMaxTeams;
    EditText mPlace;
    EditText mTheme;
    private Calendar calendar;
    private int year, month, day;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_add_quiz);

        mName = (EditText) findViewById(R.id.quiz_name_edit);
        mDate = (Button) findViewById(R.id.quiz_date_edit);
        mMaxTeams = (EditText) findViewById(R.id.quiz_number_of_teams_edit);
        mPlace = (EditText) findViewById(R.id.quiz_place_edit);
        mTheme = (EditText) findViewById(R.id.quiz_theme_edit);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    public void addQuiz(View view) {
        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseHelper fbHelper = new FirebaseHelper(database.getReference(QUIZZES_KEY_FUTURE));

        try {
            String name = mName.getText().toString();
            String date = (new SimpleDateFormat(DATE_FORMAT)).format((new SimpleDateFormat("DD/MM/YYYY")).parse(mDate.getText().toString()));
            int noOfTeams = Integer.valueOf(mMaxTeams.getText().toString());
            String place = mPlace.getText().toString();
            String theme = mTheme.getText().toString();
            Quiz newQuiz = new Quiz(name, date, noOfTeams, place, theme, true, new ArrayList<Team>());

            if (name == null | date == null | place == null | theme == null) {
                throw new Exception();
            }

            fbHelper.save(newQuiz);
            Toast.makeText(getApplicationContext(), "Entry succ. added!", Toast.LENGTH_LONG);
            this.finish();
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Pocet tymov musi byt cislo, nolem, prekontroluj data!", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Daco zle, nolem, prekontroluj data!", Toast.LENGTH_LONG);
        }
    }

    public void invokeDatePicker(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        mDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
