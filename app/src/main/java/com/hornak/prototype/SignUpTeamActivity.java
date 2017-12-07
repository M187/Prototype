package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by michal.hornak on 12/7/2017.
 */

public class SignUpTeamActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_sign_up_team);
    }

    public void addTeam(View view) {

        this.finish();
    }
}
