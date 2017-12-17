package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_ADMINS;

/**
 * Created by michal.hornak on 12/17/2017.
 */

public class AddAdminActivity extends AppCompatActivity {

    @BindView(R.id.admin_email)
    EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);
    }

    public void addAdmin(View view) {
        FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS).setValue(email.getText().toString());
    }

    public void removeAdmin(View view) {
        FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS.concat("/").concat(email.getText().toString())).removeValue();
    }
}
