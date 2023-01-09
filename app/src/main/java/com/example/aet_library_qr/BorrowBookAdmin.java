package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BorrowBookAdmin extends AppCompatActivity {

    TextView tvaTitle, tvaAuthor, tvaYear, tvaAvail;
    Button btnBorrow, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book_admin);

        tvaTitle = findViewById(R.id.tvaTitle);
        tvaAuthor = findViewById(R.id.tvaAuthor);
        tvaYear = findViewById(R.id.tvaYear);
        tvaAvail = findViewById(R.id.tvaAvail);

        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching book data");
        dialog.setTitle("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String resultID1 = getIntent().getExtras().getString("resultID");

        FirebaseDatabase.getInstance()
                .getReference("Book")
                .child(resultID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialog.dismiss();
                        if (snapshot.getValue() != null) {
                            Book info = snapshot.getValue(Book.class);
                            tvaTitle.setText(info.getTitle());
                            tvaAuthor.setText(info.getAuthor());
                            tvaYear.setText(info.getYearPublished());
                            if (info.isIs_available() == true) {
                                tvaAvail.setText("Available");
                                btnReturn.setEnabled(false);
                                btnBorrow.setEnabled(true);
                            } else if (info.isIs_available() == false) {
                                tvaAvail.setText("Not Available");
                                btnReturn.setEnabled(true);
                                btnBorrow.setEnabled(false);
                            } else if (resultID1 == null) {
                                tvaAvail.setText("Book does not exist");
                                btnReturn.setEnabled(false);
                                btnBorrow.setEnabled(false);
                                Toast.makeText(BorrowBookAdmin.this, "Book does not exist", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BorrowBookAdmin.this, HomeAdmin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                tvaAvail.setText("Book does not exist");
                                btnReturn.setEnabled(false);
                                btnBorrow.setEnabled(false);
                                Toast.makeText(BorrowBookAdmin.this, "Book does not exist", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BorrowBookAdmin.this, HomeAdmin.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            tvaAvail.setText("Book does not exist");
                            btnReturn.setEnabled(false);
                            btnBorrow.setEnabled(false);
                            Toast.makeText(BorrowBookAdmin.this, "Book does not exist", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BorrowBookAdmin.this, HomeAdmin.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BorrowBookAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //Button pass data (KEY)
        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String resultID1 = getIntent().getExtras().getString("resultID");
                Intent intent = new Intent(BorrowBookAdmin.this, QRScan.class);
                intent.putExtra("classType", "BorrowBookAdmin");
                intent.putExtra("resultID1", resultID1);
                startActivity(intent);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DAOBook daoBook = new DAOBook();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("is_available", true);

                daoBook.updateBorrow(resultID1, hashMap).addOnSuccessListener(suc -> {
                    Intent intent = new Intent(BorrowBookAdmin.this, HomeAdmin.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(BorrowBookAdmin.this, "Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(BorrowBookAdmin.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });

                FirebaseDatabase.getInstance()
                        .getReference("Transaction")
                        .orderByChild("bookID")
                        .equalTo(resultID1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    ds.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(BorrowBookAdmin.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BorrowBookAdmin.this, HomeAdmin.class);
        startActivity(intent);
        finish();
    }
}