package com.hornak.prototype;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 12/17/2017.
 */

public class AddAdminActivity extends AppCompatActivity {

    @BindView(R.id.admin_email)
    EditText emailET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);
    }

    public void addAdmin(View view) {
//        String mail = emailET.getText().toString().toString().replace(".", "-");
//        boolean isPresent = false;
//        Iterator iter = adminList.iterator();
//        while (iter.hasNext()) {
//            if (((Admin) iter.next()).getEmail().equals(mail)) {
//                isPresent = true;
//            }
//        }
//        if (isPresent) {
//            Toast.makeText(getApplicationContext(), "Tento email uz je administrator!", Toast.LENGTH_LONG).show();
//        } else {
//            adminList.add(new Admin("", mail));
//            FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS).setValue(adminList);
//            emailET.getText().clear();
//        }
    }

    public void removeAdmin(View view) {
//        boolean isPresent = false;
//        Iterator iter = adminList.iterator();
//        while (iter.hasNext()) {
//            if (((Admin) iter.next()).getEmail().equals(emailET.getText().toString().replace(".", "-"))) {
//                iter.remove();
//                isPresent = true;
//            }
//        }
//        if (isPresent) {
//            FirebaseDatabase.getInstance().getReference(QUIZZES_ADMINS).setValue(adminList);
//        } else {
//            Toast.makeText(getApplicationContext(), "Tento emailET nieje administrator!", Toast.LENGTH_LONG).show();
//        }
    }
}
