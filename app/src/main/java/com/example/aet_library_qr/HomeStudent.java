package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeStudent extends AppCompatActivity {

    TextView infoname, infoemail, infocdept, infoyrlevel, infoage, infostudentnum;
    ImageButton findabookbutton, booktransactionsbutton, borrowabookbutton, returnabookbutton, updateprofilestudent;

    Button logoutstudent;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        infoname = findViewById(R.id.infoname);
        infoemail = findViewById(R.id.infoemail);
        infocdept = findViewById(R.id.infocdept);
        infoyrlevel = findViewById(R.id.infoyrlevel);
        infoage = findViewById(R.id.infoage);
        infostudentnum = findViewById(R.id.infostudentnum);

        findabookbutton = findViewById(R.id.findabookbutton);
        booktransactionsbutton = findViewById(R.id.booktransactionsbutton);
        borrowabookbutton = findViewById(R.id.borrowabookbutton);
        returnabookbutton = findViewById(R.id.returnabookbutton);
        logoutstudent = findViewById(R.id.logoutstudent);
        updateprofilestudent = findViewById(R.id.updateprofilestudent);

        if(mAuth.getUid() != null) {
            currentuser = mAuth.getUid();
        }

        FirebaseDatabase.getInstance()
                .getReference()
                .child("Student")
                .child(currentuser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        Student info = snapshot.getValue(Student.class);
                        String name = info.getFname() + " " + info.getMname() + ". " + info.getLname();
                        infoname.setText(name);
                        infoemail.setText(info.getEmail());
                        infocdept.setText(info.getCdept());
                        infoyrlevel.setText(info.getYrlevel());
                        infoage.setText(info.getAge());
                        infostudentnum.setText(info.getStdnum());
                    }
                    else{
                        Toast.makeText(HomeStudent.this, "No Data Available", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeStudent.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        findabookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findBook();
            }
        });

        booktransactionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTransaction();
            }
        });

        borrowabookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrowBook();
            }
        });

        returnabookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBook();
            }
        });

        logoutstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        updateprofilestudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeStudent.this, UpdateProfile2.class);
                startActivity(intent);
            }
        });

    }


    private void logout() {
        mAuth.signOut();
        Intent intent=new Intent(HomeStudent.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void returnBook() {
    }

    private void borrowBook() {
    }

    private void bookTransaction() {
    }

    private void findBook() {
    }
}