package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aet_library_qr.Contracts.RefreshInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeStudent extends AppCompatActivity implements RefreshInterface {

    TextView infoname, infoemail, infocdept, infoyrlevel, infoage, infostudentnum;
    ImageButton findabookbutton, updateprofilestudent, btnChangePass;

    Button logoutstudent, generateStudentQrBtn, bookLogs;

    SwipeRefreshLayout refreshLayout;

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


        if (mAuth.getUid() != null) {
            currentuser = mAuth.getUid();
        }
        getData();

        findabookbutton = findViewById(R.id.findabookbutton);
        findabookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findBook();
            }
        });

        logoutstudent = findViewById(R.id.logoutstudent);
        logoutstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        updateprofilestudent = findViewById(R.id.updateprofilestudent);
        updateprofilestudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeStudent.this, UpdateProfile2.class);
                startActivity(intent);
            }
        });

        generateStudentQrBtn = (Button) findViewById(R.id.generateStudentQrBtn);
        generateStudentQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateStudentQr();
            }
        });

        bookLogs = findViewById(R.id.bookLogsBtn);
        bookLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookLogs();
            }
        });

        btnChangePass = findViewById(R.id.btnChangePass);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshHomeStudent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void changePassword() {
        Intent intent = new Intent(HomeStudent.this, ChangePassword.class);
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(HomeStudent.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void findBook() {
        Intent intent = new Intent(HomeStudent.this, QRScan.class);
        intent.putExtra("classType", "HomeStudent");
        startActivity(intent);
    }

    private void generateStudentQr() {
        Intent intent = new Intent(HomeStudent.this, GenerateQRStudent.class);
        startActivity(intent);
    }

    private void bookLogs() {
        Intent intent = new Intent(HomeStudent.this, BookLogsStudent.class);
        startActivity(intent);
    }

    @Override
    public void getData() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Student")
                .child(currentuser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Student info = snapshot.getValue(Student.class);
                            String name = info.getFname() + " " + info.getMname() + ". " + info.getLname();
                            infoname.setText(name);
                            infoemail.setText(info.getEmail());
                            infocdept.setText(info.getCdept());
                            infoyrlevel.setText(info.getYrlevel());
                            infoage.setText(info.getAge());
                            infostudentnum.setText(info.getStdnum());
                        } else {
                            Toast.makeText(HomeStudent.this, "No Data Available", Toast.LENGTH_SHORT).show();
                            logout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeStudent.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}