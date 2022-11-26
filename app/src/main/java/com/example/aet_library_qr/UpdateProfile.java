package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

public class UpdateProfile extends AppCompatActivity {

    TextView tvEmail;
    EditText createlname, createfname, createmname, createstudentnum, createage;
    Spinner createcollege, createyrlvl2;
    Button btngo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        createlname = findViewById(R.id.createlname);
        createfname = findViewById(R.id.createfname);
        createmname = findViewById(R.id.createmname);
        createcollege = findViewById(R.id.createcollege);
        createyrlvl2 = findViewById(R.id.createyrlvl2);
        createstudentnum = findViewById(R.id.createstudentnum);
        createage = findViewById(R.id.createage);
        tvEmail = findViewById(R.id.tvEmail);

        String Email1 = getIntent().getExtras().getString("email");
        String uid = getIntent().getExtras().getString("uid");
        tvEmail.setText(Email1);

        DAOStudent dao = new DAOStudent();

        btngo = findViewById(R.id.btngo);
        btngo.setOnClickListener(v ->
        {
            Student std = new Student(tvEmail.getText().toString(), createlname.getText().toString(), createfname.getText().toString(), createmname.getText().toString(), createcollege.getSelectedItem().toString(), createyrlvl2.getSelectedItem().toString(), createstudentnum.getText().toString(), createage.getText().toString());

            dao.add(std, uid).addOnSuccessListener(suc ->
            {
                sendUserToNextActivity();
                Toast.makeText(UpdateProfile.this, "Update Done", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er ->
            {
                Toast.makeText(UpdateProfile.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(UpdateProfile.this, HomeStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
