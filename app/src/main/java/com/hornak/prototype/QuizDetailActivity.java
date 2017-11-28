package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hornak.prototype.model.quizzes.Quiz;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 11/28/2017.
 */

public class QuizDetailActivity extends AppCompatActivity {

    @BindView(R.id.quiz_name)
    TextView quizName;

    private View mRootView;
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_detail_layout);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        this.quiz = data.getParcelable("QUIZ");

        quizName.setText(quiz.getName());
    }


}
