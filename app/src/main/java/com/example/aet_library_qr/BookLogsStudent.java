package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookLogsStudent extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Transaction> transactions;
    BookLogListAdapter adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_logs_student);

        recyclerView = findViewById(R.id.bookLogsRecycler);
        transactions = new ArrayList<>();
        adapter = new BookLogListAdapter(BookLogsStudent.this, transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookLogsStudent.this));
        recyclerView.setAdapter(adapter);
        DAOTransaction transaction = new DAOTransaction();
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        transaction.getStudentTransactionsByUID(uid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Transaction transaction1 = dataSnapshot.getValue(Transaction.class);
                    transactions.add(transaction1);
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}