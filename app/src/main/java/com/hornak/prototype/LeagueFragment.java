package com.hornak.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.hornak.prototype.MainActivity.QUIZZES_KEY_FUTURE;

/**
 * Created by michal.hornak on 12/11/2017.
 */

public class LeagueFragment extends Fragment {

    FirebaseRecyclerAdapter mFirebaseAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseHelper frbsHelper;
    private DatabaseReference firebaseDBRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_league, container, false);
        //mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDBRef = database.getReference(QUIZZES_KEY_FUTURE);
        frbsHelper = new FirebaseHelper(firebaseDBRef);

        //setUpFirebaseAdapter();
        return mView;
    }

//    private void setUpFirebaseAdapter() {
//
//        mFirebaseAdapter = new FirebaseRecyclerAdapter<Quiz, PastQuizFragment.QuizViewHolder>(Quiz.class, R.layout.main_list_item, PastQuizFragment.QuizViewHolder.class, firebaseDBRef) {
//
//            @Override
//            protected void populateViewHolder(PastQuizFragment.QuizViewHolder viewHolder, Quiz model, int position) {
//                viewHolder.bindView(model);
//            }
//        };
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.setAdapter(mFirebaseAdapter);
//    }

    private void createTeamEntry() {

    }

    private class TeamStatistics implements Comparable {

        String mName;
        int pointsOverAll;
        int quizAttended;
        Map<String, Integer> quizMap = new LinkedHashMap<>();

        @Override
        public int compareTo(Object o) {
            if (o instanceof TeamStatistics) {
                return this.pointsOverAll - ((TeamStatistics) o).pointsOverAll;
            } else return 1;
        }
    }
}
