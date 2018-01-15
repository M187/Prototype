package com.hornak.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.model.teams.TeamData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.hornak.prototype.MainActivity.QUIZZES_TEAMS;

/**
 * Created by michal.hornak on 12/11/2017.
 */

public class LeagueFragment extends Fragment {

    private RecyclerView teamList;

    private List<TeamData> teamDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_league, container, false);

        teamList = (RecyclerView) mView.findViewById(R.id.sorted_team_list);
        getTeamData();

        return mView;
    }

    private void getTeamData() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(QUIZZES_TEAMS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dS : dataSnapshot.getChildren()) {
                        teamDataList.add(dS.getValue(TeamData.class));
                    }
                    Collections.sort(teamDataList, new Comparator<TeamData>() {
                        @Override
                        public int compare(TeamData lhs, TeamData rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView subtitleView;

        public TeamViewHolder(View view) {
            super(view);
        }

        public void bindView(Quiz quiz) {
            this.titleView.setText(quiz.getName());
            this.subtitleView.setText(
                    DateUtils.getRelativeTimeSpanString(
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString());
        }
    }
}
