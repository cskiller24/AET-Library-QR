package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookLogStudent extends AppCompatActivity {

    TextView bookStudentNum, bookIDcode, bkTitle, bkAuthor, bkYear, stdNum1, stdName1;
    Button btnConfirmBorrow;

    ListView bookLogsView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Transaction bookLogs;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_log_student);

        String resultID1 = getIntent().getExtras().getString("resultID");
        String bookID1 = getIntent().getExtras().getString("bookID");

        bkTitle = findViewById(R.id.bkTitle);
        bkAuthor = findViewById(R.id.bkAuthor);
        bkYear = findViewById(R.id.bkYear);
        stdName1 = findViewById(R.id.stdName);
        stdNum1 = findViewById(R.id.stdNum);

        bookStudentNum = findViewById(R.id.bookStudentNum);
        bookIDcode = findViewById(R.id.bookIDcode);

        bookStudentNum.setText(resultID1);
        bookIDcode.setText(bookID1);

        //ListView?
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        bookLogs = new Transaction();
        bookLogsView = findViewById(R.id.bookLogsView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.transaction_info,R.id.text1, list);

        FirebaseDatabase.getInstance()
                .getReference("Transaction")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            bookLogs = ds.getValue(Transaction.class);
                            list.add("\uD83D\uDD32 ● " + bookLogs.getBookTitle());
                        }
                        bookLogsView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance()
                .getReference("Book")
                .child(bookID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Book info = snapshot.getValue(Book.class);
                            bkTitle.setText(info.getTitle());
                            bkAuthor.setText(info.getAuthor());
                            bkYear.setText(info.getYearPublished());
                            info.setIs_available(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance()
                .getReference("Student")
                .child(resultID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Student info1 = snapshot.getValue(Student.class);
                            String name = info1.getFname() + " " + info1.getLname();
                            stdNum1.setText(info1.getStdnum());
                            stdName1.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        DAOBook daoBook = new DAOBook();
        DAOTransaction dao = new DAOTransaction();
        btnConfirmBorrow = findViewById(R.id.btnConfirmBorrow);
        btnConfirmBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction tran = new Transaction(bookIDcode.getText().toString(), bkTitle.getText().toString(), bkAuthor.getText().toString(), bkYear.getText().toString(), bookStudentNum.getText().toString(), stdNum1.getText().toString(), stdName1.getText().toString());

                dao.add(tran).addOnSuccessListener(suc -> {
                    sendUserToNextActivity();
                    Toast.makeText(BookLogStudent.this, "Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er -> {
                    Toast.makeText(BookLogStudent.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });

                //Will add update => Book is_available = true/false
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("is_available", false);

                daoBook.updateBorrow(bookID1, hashMap).addOnSuccessListener(suc ->{
                    sendUserToNextActivity();
                    Toast.makeText(BookLogStudent.this, "Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(BookLogStudent.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(BookLogStudent.this, HomeAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}