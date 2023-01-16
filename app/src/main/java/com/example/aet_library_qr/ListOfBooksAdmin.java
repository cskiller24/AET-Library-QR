package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
    String redirectType;
    TextView noBooksView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_books_admin);
        recyclerView = findViewById(R.id.bookRecycler);
        noBooksView = findViewById(R.id.noBooksView);

        daoBook = new DAOBook();
        books = new ArrayList<>();
        bookKeys = new ArrayList<>();
        redirectType = getIntent().getStringExtra("redirectType");
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    if (redirectType.equals("UPDATE") && book.isIs_available()) {
                        books.add(book);
                        bookKeys.add(dataSnapshot.getKey());
                    } else if (redirectType.equals("REMOVE") && book.isIs_available()) {
                        books.add(book);
                        bookKeys.add(dataSnapshot.getKey());
                    } else if (redirectType.equals("VIEW")) {
                        books.add(book);
                        bookKeys.add(dataSnapshot.getKey());
                    }
                }
                if (books.isEmpty()) {
                    noBooksView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    return;
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