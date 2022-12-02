package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;

import org.w3c.dom.Text;

public class BookInfoAdmin extends AppCompatActivity {

    TextView bookTitle;
    TextView bookAuthor;
    TextView bookYearPublished;
    TextView bookIsAvailable;
    Button generateQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_admin);

        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        bookYearPublished = (TextView) findViewById(R.id.bookYearPublished);
        bookIsAvailable = (TextView) findViewById(R.id.bookIsAvailable);

        generateQR = (Button) findViewById(R.id.bookGenerateQR);

        /**
         * Pag kukuha ng book information need dito ng key
         * Example book key: -NHiHLPWnjfS9csUJOSM (from firebase)
         *
         * Bale gagamit ng getIntents() para makuha yung key
         * if ever na walang key, automatic redirect sa admin home page with message
         */
        Intent intent = getIntent();

        if(intent.getStringExtra("key") == null) {
            changeActivity(MainActivity.class, false);
            Toast.makeText(BookInfoAdmin.this, "Cannot search the book", Toast.LENGTH_SHORT).show();
            return;
        }
        String key = intent.getStringExtra("key");
        getBook(key);

        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrGenerateActivity = new Intent(BookInfoAdmin.this, GenerateQrAdmin.class);
                qrGenerateActivity.putExtra("key", key);
                startActivity(qrGenerateActivity);
            }
        });
    }

    private void getBook(String key)
    {
        DAOBook daoBook = new DAOBook();

        daoBook.getBook(key, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() == null) {
                    changeActivity(MainActivity.class, false);
                    Toast.makeText(BookInfoAdmin.this, "Cannot search the book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Book book = snapshot.getValue(Book.class);

                bookTitle.setText(book.getTitle());
                bookAuthor.setText(book.getAuthor());
                bookYearPublished.setText(book.getYearPublished());
                bookIsAvailable.setText(book.isIs_available() ? "Available" : "Not Available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeActivity(Class<?> cls, boolean returnable)
    {
        Intent changeActivity = new Intent(BookInfoAdmin.this, cls);

        if(returnable) {
            startActivity(changeActivity);
        } else {
            startActivity(changeActivity);
            finish();
        }
    }

}