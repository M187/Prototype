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
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hornak.prototype.model.teams.TeamData;
import com.hornak.prototype.ui.FadingImageViewHandler;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String QUIZZES_KEY_FUTURE = "QUIZZES_NODE_FUTURE";
    public static final String QUIZZES_KEY_PAST = "QUIZZES_NODE_PAST";
    public static final String QUIZZES_TEAMS = "QUIZZES_TEAMS";
    public static final String DATE_FORMAT = "DD-MMM-YYYY";
    public static FirebaseUser mFirebaseUser;
    public static TeamData mTeamData;
    // Firebase instance variables
    private static FirebaseAuth mFirebaseAuth;
    FloatingActionButton rightLowerButton;
    private String mUsername;
    private String mPhotoUrl;
    private AppBarLayout appBarLayout;
    private ImageView mPhotoView;
    private FirebaseHelper frbsHelper;
    private FadingImageViewHandler fadingImageViewHandler;
    private boolean isFabOpened = false;

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

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference(QUIZZES_TEAMS.concat("/").concat(mFirebaseUser.getUid()));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(QUIZZES_TEAMS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mTeamData = dataSnapshot.child("4b9f2ece-33e1-4f03-abda-b61e86c0f8ab").getValue(TeamData.class);
                    ((ViewManager) rightLowerButton.getParent()).removeView(rightLowerButton);
                } catch (NullPointerException e) {
                }
                setupFab();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

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
        super.onPause();
        try {
            if (isFabOpened) {
                rightLowerButton.callOnClick();
            }
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setupFab() {

        boolean hasTeam = false;
        final ImageView fabIconNew = new ImageView(this);

        fabIconNew.setImageDrawable(getResources().getDrawable(R.mipmap.ic_settings_white_24dp));
        //fabIconNew.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));

        rightLowerButton = new FloatingActionButton.Builder(this)
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
        if (mTeamData == null) {
            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_team_add_black_24dp));
        } else {
            rlIcon3.setImageDrawable(getResources().getDrawable(R.drawable.ic_team_black_24dp));
            hasTeam = true;
        }
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
                rightLowerButton.callOnClick();
            }
        });

        if (hasTeam) {
            rlIcon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent temp = new Intent(getApplicationContext(), TeamDetailActivity.class);
                    //temp.putExtra("TEAM", mTeamData);
                    startActivity(temp);
                    rightLowerButton.callOnClick();
                }
            });
        } else {
            rlIcon3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RegisterNewTeam.class));
                    rightLowerButton.callOnClick();
                }
            });
        }

        rlIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frbsHelper.makeTestData();
                rightLowerButton.callOnClick();
            }
        });

        // Listen menu open and close events to animate the button content view
        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {

            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees clockwise
                isFabOpened = true;
                fabIconNew.setRotation(0);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
                isFabOpened = false;
                fabIconNew.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
                animation.start();
            }
        });
    }

    public static class RegisterNewTeam extends AppCompatActivity {

        TeamData mTeamData;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.activity_sign_up_team);
        }

        public void addTeam(View view) {

            // todo - UID hardcoded for now
            //final String myUID = mFirebaseUser.getUid();
            //String myEmail = mFirebaseUser.getEmail();
            final String myUID = "4b9f2ece-33e1-4f03-abda-b61e86c0f8ab";
            String myEmail = "some@gmail.com";

            final TeamData teamData = new TeamData(((EditText) findViewById(R.id.team_name)).getText().toString(), myUID, myEmail, 0);

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            builder.setCancelable(true);
            builder.setMessage("Registrovat team?");
            builder.setPositiveButton("Registruj",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference(QUIZZES_TEAMS).child(teamData.getUid()).setValue(teamData);
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
