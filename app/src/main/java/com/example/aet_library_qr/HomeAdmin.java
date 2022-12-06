package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeAdmin extends AppCompatActivity {

    ImageButton bookreturnscanner, studenttransactionscanner, addabook,
            listofbooks, removeabook;
    Button logoutbtnadmin;
    TextView bookCount, studentCount;
    DAOBook daoBook;
    DAOStudent daoStudent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        daoBook = new DAOBook();
        daoStudent = new DAOStudent();

        mAuth = FirebaseAuth.getInstance();

        studentCount = (TextView) findViewById(R.id.studentCount);
        bookCount = (TextView) findViewById(R.id.bookCount);

        daoStudent.getAllStudents(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String studentCountStr = String.valueOf(snapshot.getChildrenCount());
                studentCount.setText(studentCountStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        daoBook.getAllBooks(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bookCountStr = String.valueOf(snapshot.getChildrenCount());
                bookCount.setText(bookCountStr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        logoutbtnadmin = (Button) findViewById(R.id.logoutbtnadmin);
        logoutbtnadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { logout(); }
        });

        addabook = (ImageButton) findViewById(R.id.addabook);
        addabook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(AddBookAdmin.class, true);
            }
        });

        removeabook = (ImageButton) findViewById(R.id.removeabook);
        removeabook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(RemoveBookAdmin.class, true);
            }
        });

        listofbooks = (ImageButton) findViewById(R.id.listofbooks);
        listofbooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity(ListOfBooksAdmin.class, true);
            }
        });

        studenttransactionscanner = findViewById(R.id.studenttransactionscanner);
        studenttransactionscanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(QRScan.class, true);
            }
        });
    }

    private void logout() {
        mAuth.signOut();
        changeActivity(MainActivity.class, false);
    }

    private void changeActivity(Class<?> cls, boolean returnable)
    {
        Intent intent = new Intent(HomeAdmin.this, cls);
        if(returnable) {
            intent.putExtra("classType", "HomeAdmin");
            startActivity(intent);
        } else {
            startActivity(intent);
            finish();
        }
    }
}