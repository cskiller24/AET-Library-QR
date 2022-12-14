package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateProfile extends AppCompatActivity {

    TextView tvEmail;
    EditText createlname, createfname, createmname, createstudentnum, createage;
    Spinner createcollege, createyrlvl2;
    Button btngo, btnout;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ProgressDialog progressDialog;

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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        if(mUser.isEmailVerified() == false){
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
            builder.setTitle("Notice");
            builder.setMessage("Email not verified");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(UpdateProfile.this, MainActivity.class);
                    Toast.makeText(UpdateProfile.this, "Email not verified", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    dialog.dismiss();
                }
            }).show();
        }

        String Email1 = getIntent().getExtras().getString("email");
        String uid = getIntent().getExtras().getString("uid");
        tvEmail.setText(Email1);

        DAOStudent dao = new DAOStudent();

        btngo = findViewById(R.id.btngo);
        btngo.setOnClickListener(v ->
        {
            if(createlname.getText().toString().isEmpty()){
                createlname.setError("Enter your Lastname");
                Toast.makeText(UpdateProfile.this, "Lastname Required", Toast.LENGTH_SHORT).show();
            }
            else if(createfname.getText().toString().isEmpty()){
                createfname.setError("Enter your Firstname");
                Toast.makeText(UpdateProfile.this, "Firstname Required", Toast.LENGTH_SHORT).show();
            }
            else if(createage.getText().toString().isEmpty()){
                createage.setError("Enter your Age");
                Toast.makeText(UpdateProfile.this, "Age Required", Toast.LENGTH_SHORT).show();
            }
            else if(createstudentnum.getText().toString().isEmpty()){
                createage.setError("Enter your Student number");
                Toast.makeText(UpdateProfile.this, "Student Number Required", Toast.LENGTH_SHORT).show();
            }
            else {
                Student std = new Student(tvEmail.getText().toString(), createlname.getText().toString(), createfname.getText().toString(), createmname.getText().toString(), createcollege.getSelectedItem().toString(), createyrlvl2.getSelectedItem().toString(), createstudentnum.getText().toString(), createage.getText().toString());

                dao.add(std, uid).addOnSuccessListener(suc ->
                {
                    sendUserToNextActivity();
                    Toast.makeText(UpdateProfile.this, "Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(UpdateProfile.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        btnout = findViewById(R.id.btnout);
        btnout.setOnClickListener(v ->
        {
            mAuth.signOut();
            Intent intent=new Intent(UpdateProfile.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(UpdateProfile.this, HomeStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
