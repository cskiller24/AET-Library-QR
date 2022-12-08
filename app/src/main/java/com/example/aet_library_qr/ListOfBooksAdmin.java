package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.aet_library_qr.Contracts.RefreshInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfBooksAdmin extends AppCompatActivity implements RefreshInterface {
    RecyclerView recyclerView;
    ArrayList<Book> books;
    ArrayList<String> bookKeys;
    BookListAdapter adapter;
    SwipeRefreshLayout refreshLayout;
    DAOBook daoBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books_admin);
        recyclerView = findViewById(R.id.bookRecycler);

        daoBook = new DAOBook();
        books = new ArrayList<>();
        bookKeys = new ArrayList<>();
        String redirectType = getIntent().getStringExtra("redirectType").toString();
        adapter = new BookListAdapter(ListOfBooksAdmin.this, books, bookKeys, redirectType);

        recyclerView.setLayoutManager(new LinearLayoutManager(ListOfBooksAdmin.this));
        recyclerView.setAdapter(adapter);

        getData();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshListBooksAdmin);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void getData() {
        books.removeAll(books);
        daoBook.getAllBooks(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                Toast.makeText(ListOfBooksAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}