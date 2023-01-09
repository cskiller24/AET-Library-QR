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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

public class BookLogsStudent extends AppCompatActivity implements RefreshInterface {
    RecyclerView recyclerView;
    ArrayList<Transaction> transactions;
    BookLogListAdapter adapter;
    FirebaseAuth mAuth;
    SwipeRefreshLayout refreshLayout;
    DAOTransaction transaction;
    String uid;
    TextView tvNoBookLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_logs_student);

        recyclerView = findViewById(R.id.bookLogsRecycler);
        transactions = new ArrayList<>();
        adapter = new BookLogListAdapter(BookLogsStudent.this, transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookLogsStudent.this));
        recyclerView.setAdapter(adapter);
        transaction = new DAOTransaction();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        recyclerView.setAdapter(adapter);
        tvNoBookLogs = (TextView) findViewById(R.id.noBookLogs);
        getData();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshBookLogsStudent);
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
        transactions.removeAll(transactions);
        transaction.getStudentTransactionsByUID(uid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Transaction transaction1 = dataSnapshot.getValue(Transaction.class);
                        transactions.add(transaction1);
                }

                if(transactions.isEmpty()) {
                    tvNoBookLogs.setVisibility(View.VISIBLE);
                } else {
                    tvNoBookLogs.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookLogsStudent.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}