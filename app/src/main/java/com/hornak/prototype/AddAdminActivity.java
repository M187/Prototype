package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.hornak.prototype.model.Admin;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hornak.prototype.MainActivity.QUIZZES_ADMINS;

/**
 * Created by michal.hornak on 12/17/2017.
 */

public class AddAdminActivity extends AppCompatActivity {

    @BindView(R.id.admin_email)
    EditText emailET;

    private ArrayList<Admin> adminList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);
        adminList = getIntent().getExtras().getParcelableArrayList(QUIZZES_ADMINS);
    }

    public void addAdmin(View view) {
        String mail = emailET.getText().toString().toString().replace(".", "-");
        boolean isPresent = false;
        Iterator iter = adminList.iterator();
        while (iter.hasNext()) {
            if (((Admin) iter.next()).getEmail().equals(mail)) {
                isPresent = true;
            }
        }
        if (isPresent) {
            Toast.makeText(getApplicationContext(), "Tento emailET uz je administrator!", Toast.LENGTH_LONG).show();
        } else {
            adminList.add(new Admin("", mail));
            FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS).setValue(adminList);
            emailET.getText().clear();
        }
    }

    public void removeAdmin(View view) {
        boolean isPresent = false;
        Iterator iter = adminList.iterator();
        while (iter.hasNext()) {
            if (((Admin) iter.next()).getEmail().equals(emailET.getText().toString().replace(".", "-"))) {
                iter.remove();
                isPresent = true;
            }
        }
        if (isPresent) {
            FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS).setValue(adminList);
        } else {
            Toast.makeText(getApplicationContext(), "Tento emailET nieje administrator!", Toast.LENGTH_LONG).show();
        }
    }
}
