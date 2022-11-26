package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class HomeAdmin extends AppCompatActivity {

    ImageButton bookreturnscanner, studenttransactionscanner, addabook,
            logoutbtnadmin, listofbooks, removeabook;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        mAuth = FirebaseAuth.getInstance();

        logoutbtnadmin = (ImageButton) findViewById(R.id.logoutbtnadmin);
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
    }

    private void logout() {
        mAuth.signOut();
        changeActivity(MainActivity.class, false);
    }

    private void changeActivity(Class<?> cls, boolean returnable)
    {
        Intent intent = new Intent(HomeAdmin.this, cls);
        if(returnable) {
            startActivity(intent);
        } else {
            startActivity(intent);
            finish();
        }

    }
}