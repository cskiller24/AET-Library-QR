package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfBooksAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Book> books;
    ArrayList<String> bookKeys;
    BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books_admin);
        recyclerView = findViewById(R.id.bookRecycler);

        DAOBook daoBook = new DAOBook();
        books = new ArrayList<>();
        bookKeys = new ArrayList<>();
        adapter = new BookListAdapter(ListOfBooksAdmin.this, books, bookKeys);

        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfBooksAdmin.this));
        recyclerView.setAdapter(adapter);
        daoBook.getAllBooks(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("List of book", "" + snapshot.getValue());
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Book book = dataSnapshot.getValue(Book.class);
                    books.add(book);
                    bookKeys.add(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}