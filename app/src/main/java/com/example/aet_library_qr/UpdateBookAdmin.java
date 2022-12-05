package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class UpdateBookAdmin extends AppCompatActivity {
    EditText bookUpdateTitle, bookUpdateAuthor, bookUpdateYearPublished;
    Button bookUpdateBtn;

    DAOBook daoBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book_admin);

        String key = getIntent().getStringExtra("key");

        if (key == null) {
            Intent intent = new Intent(UpdateBookAdmin.this, HomeAdmin.class);
            Toast.makeText(UpdateBookAdmin.this, "Book not found", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
            return;
        }
        daoBook = new DAOBook();

        bookUpdateTitle = (EditText) findViewById(R.id.bookUpdateTitle);
        bookUpdateAuthor = (EditText) findViewById(R.id.bookUpdateAuthor);
        bookUpdateYearPublished = (EditText) findViewById(R.id.bookUpdateYearPublished);

        daoBook.getBook(key, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    Intent intent = new Intent(UpdateBookAdmin.this, HomeAdmin.class);
                    Toast.makeText(UpdateBookAdmin.this, "Book not found", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                    return;
                }
                Book book = snapshot.getValue(Book.class);
                bookUpdateTitle.setText(book.getTitle());
                bookUpdateAuthor.setText(book.getAuthor());
                bookUpdateYearPublished.setText(book.getYearPublished());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateBookAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        bookUpdateBtn = (Button) findViewById(R.id.bookUpdateBtn);
        bookUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Book book = new Book(
                            bookUpdateTitle.getText().toString(),
                            bookUpdateAuthor.getText().toString(),
                            bookUpdateYearPublished.getText().toString()
                            );
                    daoBook.updateBook(key, book).addOnSuccessListener(suc -> {
                        Intent intent = new Intent(UpdateBookAdmin.this, HomeAdmin.class);
                        Toast.makeText(UpdateBookAdmin.this, "Update success", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(err ->{
                        Toast.makeText(UpdateBookAdmin.this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private boolean validate() {
        int yearPub = Integer.parseInt(bookUpdateYearPublished.getText().toString());
        if (bookUpdateAuthor.getText().toString().isEmpty()) {
            bookUpdateAuthor.setError("This field is required");
            return false;
        }

        if (bookUpdateTitle.getText().toString().isEmpty()) {
            bookUpdateTitle.setError("This field is required");
            return false;
        }

        if (yearPub > 2023 || yearPub < 1700) {
            bookUpdateYearPublished.setError("Invalid year published field");
            return false;
        }

        return true;
    }
}