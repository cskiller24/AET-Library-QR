package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aet_library_qr.Contracts.RefreshInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BookInfoAdmin extends AppCompatActivity implements RefreshInterface {

    TextView bookTitle;
    TextView bookAuthor;
    TextView bookYearPublished;
    TextView bookIsAvailable;
    Button generateQR, bookInfoUpdateBook, bookInfoDeleteBook;
    String key;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_admin);

        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        bookYearPublished = (TextView) findViewById(R.id.bookYearPublished);
        bookIsAvailable = (TextView) findViewById(R.id.bookIsAvailable);

        generateQR = (Button) findViewById(R.id.bookGenerateQR);
        bookInfoUpdateBook = (Button) findViewById(R.id.bookInfoUpdateBook);
        bookInfoDeleteBook = (Button) findViewById(R.id.bookInfoDeleteBook);

        DAOBook daoBook;

        /**
         * Pag kukuha ng book information need dito ng key
         * Example book key: -NHiHLPWnjfS9csUJOSM (from firebase)
         *
         * Bale gagamit ng getIntents() para makuha yung key
         * if ever na walang key, automatic redirect sa admin home page with message
         */
        Intent intent = getIntent();
        daoBook = new DAOBook();

        if (intent.getStringExtra("key") == null) {
            changeActivity(MainActivity.class, false);
            Toast.makeText(BookInfoAdmin.this, "Cannot search the book", Toast.LENGTH_SHORT).show();
            return;
        }
        key = intent.getStringExtra("key");
        getData();

        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrGenerateActivity = new Intent(BookInfoAdmin.this, GenerateQrAdmin.class);
                qrGenerateActivity.putExtra("key", key);
                startActivity(qrGenerateActivity);
            }
        });

        bookInfoUpdateBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bookUpdateActivity = new Intent(BookInfoAdmin.this, UpdateBookAdmin.class);
                bookUpdateActivity.putExtra("key", key);
                startActivity(bookUpdateActivity);
            }
        });

        bookInfoDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookInfoAdmin.this);
                builder.setTitle("Delete book");
                builder.setMessage("Confirm delete " + bookTitle.getText().toString());
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        daoBook.deleteBook(key).addOnSuccessListener(suc -> {
                            Intent homeAdmin = new Intent(getApplicationContext(), HomeAdmin.class);
                            Toast.makeText(BookInfoAdmin.this, "Book deleted", Toast.LENGTH_SHORT).show();
                            startActivity(homeAdmin);
                            finish();
                        }).addOnFailureListener(err -> {
                            Toast.makeText(BookInfoAdmin.this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshBookInfoAdmin);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void getBook(String key) {
        DAOBook daoBook = new DAOBook();
        daoBook.getBook(key, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    changeActivity(MainActivity.class, false);
                    Toast.makeText(BookInfoAdmin.this, "Cannot search the book", Toast.LENGTH_SHORT).show();
                    return;
                }
                Book book = snapshot.getValue(Book.class);

                bookTitle.setText(book.getTitle());
                bookAuthor.setText(book.getAuthor());
                bookYearPublished.setText(book.getYearPublished());
                bookIsAvailable.setText(book.isIs_available() ? "True" : "False");
                if(! book.isIs_available()) {
                    bookInfoUpdateBook.setEnabled(false);
                    bookInfoDeleteBook.setEnabled(false);
                } else {
                    bookInfoUpdateBook.setEnabled(true);
                    bookInfoDeleteBook.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookInfoAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeActivity(Class<?> cls, boolean returnable) {
        Intent changeActivity = new Intent(BookInfoAdmin.this, cls);

        if (returnable) {
            startActivity(changeActivity);
        } else {
            startActivity(changeActivity);
            finish();
        }
    }

    @Override
    public void getData() {
        getBook(this.key);
    }
}