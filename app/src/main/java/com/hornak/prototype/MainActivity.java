package com.hornak.prototype;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.ui.FadingImageViewHandler;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String QUIZZES_KEY_FUTURE = "QUIZZES_NODE_FUTURE";
    public static final String QUIZZES_KEY_PAST = "QUIZZES_NODE_PAST";
    public static final String QUIZZES_TEAMS = "QUIZZES_TEAMS";
    public static final String DATE_FORMAT = "DD-MMM-YYYY";
    // Firebase instance variables
    private static FirebaseAuth mFirebaseAuth;
    private static FirebaseUser mFirebaseUser;
    AppBarLayout appBarLayout;
    ImageView mPhotoView;
    private FirebaseHelper frbsHelper;
    private String mUsername;
    private String mPhotoUrl;
    private FadingImageViewHandler fadingImageViewHandler;

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

        setContentView(R.layout.activity_main);
        mPhotoView = (ImageView) findViewById(R.id.photo_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.toolbar_container);
        fadingImageViewHandler = new FadingImageViewHandler(this.mPhotoView, (Toolbar) findViewById(R.id.toolbar));
        this.appBarLayout.addOnOffsetChangedListener(this);

        frbsHelper = new FirebaseHelper(FirebaseDatabase.getInstance().getReference(QUIZZES_KEY_FUTURE));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Pripravujeme"));
        tabLayout.addTab(tabLayout.newTab().setText("Historia"));
        tabLayout.addTab(tabLayout.newTab().setText("Liga"));

        //replace default fragment
        replaceFragment(new FutureQuizFragment());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new FutureQuizFragment());
                } else if (tab.getPosition() == 1) {
                    replaceFragment(new PastQuizFragment());
                } else if (tab.getPosition() == 2) {
                    replaceFragment(new LeagueFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        if (savedInstanceState == null) {
            //refresh();
        }

        setupFab();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        fadingImageViewHandler.handleScrolling(percentage);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        transaction.commit();
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
        rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_team_add_black_24dp));
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

        rlIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterNewTeam.class));
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

    public static class RegisterNewTeam extends AppCompatActivity {

        com.hornak.prototype.model.teams.Team mTeam;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            this.setContentView(R.layout.activity_sign_up_team);
        }

        public void addTeam(View view) {

            //Todo - UID hardcoded for now
            //mTeam = new com.hornak.prototype.model.teams.Team(((EditText) findViewById(R.id.team_name)).getText().toString(), mFirebaseUser.getUid(), mFirebaseUser.getEmail(), 0);
            mTeam = new com.hornak.prototype.model.teams.Team(((EditText) findViewById(R.id.team_name)).getText().toString(), "4b9f2ece-33e1-4f03-abda-b61e86c0f8ab", "some@gmail.com", 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setCancelable(true);
            builder.setMessage("Registrovat team?");
            builder.setPositiveButton("Registruj",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(QUIZZES_TEAMS).setValue(mTeam);
                            finish();
                        }
                    });
            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

}
