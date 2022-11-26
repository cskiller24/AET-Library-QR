package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddBookAdmin extends AppCompatActivity {

    EditText addBookAuthor;
    EditText addBookTitle;
    EditText addYearPublished;
    Button bookAddSubmit;

    private final String YEAR_PUBLISHED_PATTERN = "^\\d{4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_admin);

        addBookAuthor = (EditText) findViewById(R.id.addBookAuthor);
        addBookTitle = (EditText) findViewById(R.id.addBookTitle);
        addYearPublished = (EditText) findViewById(R.id.addYearPublished);
        bookAddSubmit = (Button) findViewById(R.id.addBookButton);

        bookAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    DAOBook daoBook = new DAOBook();
                    Book book = new Book(addBookAuthor.getText().toString(), addBookTitle.getText().toString(), addYearPublished.getText().toString());
                    daoBook.addBook(book).addOnCompleteListener(success -> {
                        Toast.makeText(AddBookAdmin.this, "Successfully added a book", Toast.LENGTH_SHORT).show();
                        changeActivity(HomeAdmin.class, false);
                    }).addOnFailureListener(err -> {
                        Toast.makeText(AddBookAdmin.this, "" + err.getMessage(), Toast.LENGTH_SHORT).show();
                        changeActivity(HomeAdmin.class, false);
                    });
                }
            }
        });
    }

    private boolean validate()
    {
        int yearPub = Integer.parseInt(addYearPublished.getText().toString());
        if(addBookAuthor.getText().toString().isEmpty())
        {
            addBookAuthor.setError("This field is required");
            return false;
        }

        if(addBookTitle.getText().toString().isEmpty())
        {
            addBookTitle.setError("This field is required");
            return false;
        }

        if(yearPub > 2023 || yearPub < 1700)
        {
            addYearPublished.setError("Invalid year published field");
            return false;
        }

        return true;
    }

    private void changeActivity(Class<?> cls, boolean returnable)
    {
        Intent intent = new Intent(getApplicationContext(), cls);
        if(returnable) {
            startActivity(intent);
        } else {
            startActivity(intent);
            finish();
        }

    }
}