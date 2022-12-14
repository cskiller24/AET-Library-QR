package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateProfile2 extends AppCompatActivity {

    TextView tvEmail;
    EditText createlname;
    EditText createfname;
    EditText createmname;
    EditText createstudentnum;
    EditText createage;
    Spinner createcollege, createyrlvl2;
    Button btngo;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile2);

        createlname = findViewById(R.id.createlname);
        createfname = findViewById(R.id.createfname);
        createmname = findViewById(R.id.createmname);
        createcollege = findViewById(R.id.createcollege);
        createyrlvl2 = findViewById(R.id.createyrlvl2);
        createstudentnum = findViewById(R.id.createstudentnum);
        createage = findViewById(R.id.createage);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        DAOStudent dao = new DAOStudent();

        String currentuser = mAuth.getUid();

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Student")
                .child(currentuser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Student info = snapshot.getValue(Student.class);
                            createstudentnum.setText(info.getStdnum());
                            createstudentnum.setEnabled(false);
                            createlname.setText(info.getLname());
                            createfname.setText(info.getFname());
                            createmname.setText(info.getMname());
                            createage.setText(info.getAge());

                        }
                        else{
                            Toast.makeText(UpdateProfile2.this, "No Data Available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpdateProfile2.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        btngo = findViewById(R.id.btngo);
        btngo.setOnClickListener(v ->
        {
            if(createlname.getText().toString().isEmpty()){
                createlname.setError("Enter your Lastname");
                Toast.makeText(UpdateProfile2.this, "Lastname Required", Toast.LENGTH_SHORT).show();
            }
            else if(createfname.getText().toString().isEmpty()){
                createfname.setError("Enter your Firstname");
                Toast.makeText(UpdateProfile2.this, "Firstname Required", Toast.LENGTH_SHORT).show();
            }
            else if(createage.getText().toString().isEmpty()){
                createage.setError("Enter your Age");
                Toast.makeText(UpdateProfile2.this, "Age Required", Toast.LENGTH_SHORT).show();
            }
            else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("lname", createlname.getText().toString());
                hashMap.put("fname", createfname.getText().toString());
                hashMap.put("mname", createmname.getText().toString());
                hashMap.put("cdept", createcollege.getSelectedItem().toString());
                hashMap.put("yrlevel", createyrlvl2.getSelectedItem().toString());
                hashMap.put("age", createage.getText().toString());

                //needs to put current user logged UID
                dao.update(mUser.getUid(), hashMap).addOnSuccessListener(suc ->
                {
                    sendUserToNextActivity();
                    Toast.makeText(UpdateProfile2.this, "Update Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(UpdateProfile2.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(UpdateProfile2.this, HomeStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
