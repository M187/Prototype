package com.hornak.prototype;

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

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.ui.DynamicHeightNetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ArrayList<String> testList = new ArrayList<>();

    private String QUIZZES_KEY = "QUIZZES_NODE";
    private FirebaseHelper frbsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testList.add("a");
        testList.add("b");
        testList.add("c");
        testList.add("d");


        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        frbsHelper = new FirebaseHelper(database.getReference(QUIZZES_KEY));

//        //Listening
//        myRef.addValueEventListener((new ValueEventListener() {
//
//            Context mContext;
//
//            public ValueEventListener setContext(Context context){
//                mContext = context;
//                return this;
//            }
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again whenever data at this location is updated.
//                mQuizzes value = dataSnapshot.getValue(mQuizzes.class);
//                Log.i("-------------->", "I was talking to db!");
//                Toast.makeText(mContext, value.getQuizList().get(1).getName(), Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Toast.makeText(mContext, "Something went wrong during fetching from DB", Toast.LENGTH_SHORT).show();
//            }
//        }).setContext(this));


        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        Adapter adapter = new Adapter(frbsHelper.retrieve());
        //Adapter adapter = new Adapter(testList);
        adapter.setHasStableIds(true);

//        FirebaseListAdapter adapter = new FirebaseListAdapter<Quiz>(this, Quiz.class, R.layout.main_list_item, myRef) {
//
//            @Override
//            protected void populateView(View view, Quiz myObj, int position) {
//                //Set the value for the views
//                ((TextView)view.findViewById(R.id.article_title)).setText(myObj.getName());
//                //...
//            }
//        };


        mRecyclerView.setAdapter(adapter);
        int columnCount = getResources().getInteger(R.integer.list_column_count);
        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sglm);

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
