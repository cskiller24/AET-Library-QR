package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    TextView tvaKey, tvaTitle, tvaAuthor, tvaYear, tvaAvail;
    Button btnBorrow, btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book_admin);

        tvaKey = findViewById(R.id.tvaKey);
        tvaTitle = findViewById(R.id.tvaTitle);
        tvaAuthor = findViewById(R.id.tvaAuthor);
        tvaYear = findViewById(R.id.tvaYear);
        tvaAvail = findViewById(R.id.tvaAvail);

        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);

        String resultID1 = getIntent().getExtras().getString("resultID");
        tvaKey.setText(resultID1);

        FirebaseDatabase.getInstance()
                .getReference("Book")
                .child(resultID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            Book info = snapshot.getValue(Book.class);
                            tvaTitle.setText(info.getTitle());
                            tvaAuthor.setText(info.getAuthor());
                            tvaYear.setText(info.getYearPublished());
                            if(info.isIs_available() == true){
                                tvaAvail.setText("Available");
                                btnReturn.setEnabled(false);
                            }
                            else{
                                tvaAvail.setText("Not Available");
                                btnBorrow.setEnabled(false);
                            }
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
                Intent intent=new Intent(BorrowBookAdmin.this, QRScan.class);
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

                daoBook.updateBorrow(resultID1, hashMap).addOnSuccessListener(suc ->{
                    Intent intent=new Intent(BorrowBookAdmin.this, HomeAdmin.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(BorrowBookAdmin.this, "Done", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(BorrowBookAdmin.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });

            }
        });

    }
}