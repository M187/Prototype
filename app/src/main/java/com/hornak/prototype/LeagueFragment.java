package com.hornak.prototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private RecyclerView teamLadderRV;
    private TeamLadderAdapter teamLadderAdapter;
    private List<TeamData> teamDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_league, container, false);
        getTeamData();

        teamLadderAdapter = new TeamLadderAdapter(teamDataList);
        teamLadderRV = (RecyclerView) mView.findViewById(R.id.sorted_team_list);
        teamLadderRV.setLayoutManager(new LinearLayoutManager(getContext()));
        teamLadderRV.setAdapter(teamLadderAdapter);

        return mView;
    }

    private void getTeamData() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(QUIZZES_TEAMS);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    teamDataList.clear();
                    for (DataSnapshot dS : dataSnapshot.getChildren()) {
                        teamDataList.add(dS.getValue(TeamData.class));
                    }
                    Collections.sort(teamDataList, new Comparator<TeamData>() {
                        @Override
                        public int compare(TeamData lhs, TeamData rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
                    try {
                        teamLadderAdapter.notifyDataSetChanged();
                    } catch (NullPointerException e) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public static class TeamLadderViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        TextView teamPoints;

        public TeamLadderViewHolder(View view) {
            super(view);
            this.teamName = (TextView) view.findViewById(R.id.team_name);
            this.teamPoints = (TextView) view.findViewById(R.id.team_points);
        }

        public void bindView(TeamData teamData) {
            this.teamName.setText(teamData.getName());
            this.teamPoints.setText(teamData.getPoints());
        }
    }

    public class TeamLadderAdapter extends RecyclerView.Adapter<TeamLadderViewHolder> {

        List<TeamData> teamDataList;

        public TeamLadderAdapter(List<TeamData> teamDataList) {
            this.teamDataList = teamDataList;
        }

        @Override
        public TeamLadderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_line_ladder, parent, false);
            return new TeamLadderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TeamLadderViewHolder holder, int position) {
            holder.bindView(teamDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return teamDataList.size();
        }
    }
}
