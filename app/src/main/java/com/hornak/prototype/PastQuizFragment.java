package com.hornak.prototype;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.ui.DynamicHeightNetworkImageView;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_PAST;

/**
 * Created by michal.hornak on 12/5/2017.
 */

public class PastQuizFragment extends Fragment {

    FirebaseRecyclerAdapter mFirebaseAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseHelper frbsHelper;
    private DatabaseReference firebaseDBRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDBRef = database.getReference(QUIZZES_KEY_PAST);
        frbsHelper = new FirebaseHelper(firebaseDBRef);

        setUpFirebaseAdapter();
        return mView;
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
            Bundle bundle = ActivityOptions.makeCustomAnimation(view.getContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
            this.getLayoutPosition();
            temp.putExtra("QUIZ", mQuiz);
            view.getContext().startActivity(temp, bundle);
        }
    }
}
