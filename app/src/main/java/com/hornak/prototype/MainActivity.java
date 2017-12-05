package com.hornak.prototype;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.quizzes.Quiz;
import com.hornak.prototype.ui.DynamicHeightNetworkImageView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity {

    public static final String QUIZZES_KEY = "QUIZZES_NODE";

    FirebaseRecyclerAdapter mFirebaseAdapter;
    private RecyclerView mRecyclerView;
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
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        if (mFirebaseUser == null) {
//            // Not signed in, launch the Sign In activity
//            startActivity(new Intent(this, SignInActivity.class));
//            finish();
//            return;
//        } else {
//            mUsername = mFirebaseUser.getDisplayName();
//            if (mFirebaseUser.getPhotoUrl() != null) {
//                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
//            }
//        }

        //Recycler view
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        setupFab();

        //Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseDBRef = database.getReference(QUIZZES_KEY);
        frbsHelper = new FirebaseHelper(firebaseDBRef);

        setUpFirebaseAdapter();
        if (savedInstanceState == null) {
            //refresh();
        }
    }

//    private void loadMyImage() {
//        Glide.with(this)
//                .load(mPhotoUrl)
//                .override(600, 600)
//                .fitCenter()
//                .into((ImageView) findViewById(R.id.fab));
//    }

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

    public void setupFab() {

        // Set up the white button on the lower right corner
        // more or less with default parameter
        final ImageView fabIconNew = new ImageView(this);
        fabIconNew.setImageDrawable(getResources().getDrawable(R.mipmap.ic_settings_white_24dp));
        //fabIconNew.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));

        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(this)
                .setContentView(fabIconNew)
                .setTheme(SubActionButton.THEME_DARK)
                .build();

        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
        ImageView rlIcon1 = new ImageView(this);
        ImageView rlIcon2 = new ImageView(this);
        ImageView rlIcon3 = new ImageView(this);
        ImageView rlIcon4 = new ImageView(this);

        rlIcon1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_24dp));
        rlIcon2.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));
        rlIcon4.setImageDrawable(getResources().getDrawable(R.drawable.ic_cloud_upload_black_24dp));

        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
        // Set 4 default SubActionButtons
        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
                .attachTo(rightLowerButton)
                .build();

        rlIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddQuizActivity.class));
            }
        });

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frbsHelper.makeTestData();
            }
        });


        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {

            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });
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
