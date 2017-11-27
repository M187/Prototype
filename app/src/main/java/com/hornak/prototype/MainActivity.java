package com.hornak.prototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.ui.DynamicHeightNetworkImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private String QUIZZES_KEY = "QUIZZES_NODE";
    private FirebaseHelper frbsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recycler view
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        frbsHelper = new FirebaseHelper(database.getReference(QUIZZES_KEY));

        //Listening
        frbsHelper.db.addValueEventListener((new ValueEventListener() {

            Context mContext;
            List<Quiz> mQuizzes = new ArrayList<>();

            public ValueEventListener setContext(Context context){
                mContext = context;
                return this;
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.
                Iterator a = dataSnapshot.getChildren().iterator();
                mQuizzes.clear();
                while (a.hasNext()) {
                    mQuizzes.add(((DataSnapshot)a.next()).getValue(Quiz.class));
                }
                mRecyclerView.setAdapter(new Adapter(mQuizzes));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(mContext, "Something went wrong during fetching from DB", Toast.LENGTH_SHORT).show();
            }
        }).setContext(this));


        if (savedInstanceState == null) {
            //refresh();
        }
    }

    public void sendNewData(View view){
        frbsHelper.makeTestData();
        Toast.makeText(this, "Sent data to DB!", Toast.LENGTH_LONG).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        DynamicHeightNetworkImageView thumbnailView;
        TextView titleView;
        TextView subtitleView;

        ViewHolder(View view) {
            super(view);
            thumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        List<Quiz> mList;

        Adapter(List<Quiz> arrayList) {
            mList = arrayList;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.main_list_item, parent, false);
            final ViewHolder vh = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW));
                }
            });
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.titleView.setText(mList.get(position).getName());
            holder.subtitleView.setText(
                    DateUtils.getRelativeTimeSpanString(
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
