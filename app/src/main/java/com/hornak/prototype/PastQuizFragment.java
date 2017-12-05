package com.hornak.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY;

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
        firebaseDBRef = database.getReference(QUIZZES_KEY);
        frbsHelper = new FirebaseHelper(firebaseDBRef);

        setUpFirebaseAdapter();
        return mView;
    }

    private void setUpFirebaseAdapter() {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Quiz, MainActivity.QuizViewHolder>(Quiz.class, R.layout.main_list_item, MainActivity.QuizViewHolder.class, firebaseDBRef) {

            @Override
            protected void populateViewHolder(MainActivity.QuizViewHolder viewHolder, Quiz model, int position) {
                viewHolder.bindView(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

}
