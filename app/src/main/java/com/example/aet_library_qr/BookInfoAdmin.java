package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BookInfoAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_admin);

        /**
         * Pag kukuha ng book information need dito ng key
         * Example book key: -NHiHLPWnjfS9csUJOSM (from firebase)
         *
         * Bale gagamit ng getIntents() para makuha yung key
         * if ever na walang key, automatic redirect sa admin home page with message
         */
        Intent intent = getIntent();
        Toast.makeText(BookInfoAdmin.this, "" + intent.getStringExtra("key"), Toast.LENGTH_SHORT).show();
    }
}