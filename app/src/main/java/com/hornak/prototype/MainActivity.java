package com.hornak.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.ui.DynamicHeightNetworkImageView;

public class MainActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter mFirebaseAdapter;
    private RecyclerView mRecyclerView;
    private String QUIZZES_KEY = "QUIZZES_NODE";
    private FirebaseHelper frbsHelper;
    private DatabaseReference firebaseDBRef;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        //Recycler view
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDBRef = database.getReference(QUIZZES_KEY);
        frbsHelper = new FirebaseHelper(firebaseDBRef);

        setUpFirebaseAdapter();
        if (savedInstanceState == null) {
            //refresh();
        }


    }


    private void setUpFirebaseAdapter() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Quiz, QuizViewHolder>(Quiz.class, R.layout.main_list_item, QuizViewHolder.class, firebaseDBRef) {

            @Override
            protected void populateViewHolder(QuizViewHolder viewHolder, Quiz model, int position) {
                viewHolder.bindView(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    public void sendNewData(View view) {
        frbsHelper.makeTestData();
        Toast.makeText(this, "Sent data to DB!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        //todo mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //todo mFirebaseAdapter.startListening();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        DynamicHeightNetworkImageView thumbnailView;
        TextView titleView;
        TextView subtitleView;
        Quiz mQuiz;

        public QuizViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
            view.setOnClickListener(this);
        }

        public void bindView(Quiz quiz) {
            mQuiz = quiz;
            this.titleView.setText(quiz.getName());
            this.subtitleView.setText(
                    DateUtils.getRelativeTimeSpanString(
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString());
        }

        @Override
        public void onClick(View view) {
            Intent temp = new Intent(view.getContext(), QuizDetailActivity.class);
            this.getLayoutPosition();
            temp.putExtra("QUIZ", mQuiz);
            view.getContext().startActivity(temp);
        }
    }
}
