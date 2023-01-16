package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookInfoStudent extends AppCompatActivity {
    TextView infoTitle, infoAuthor, infoYearPub, infoStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_student);
        infoTitle = findViewById(R.id.infoTitle);
        infoAuthor = findViewById(R.id.infoAuthor);
        infoYearPub = findViewById(R.id.infoYearPub);
        infoStatus = findViewById(R.id.infoStatus);

        String resultID1 = getIntent().getExtras().getString("resultID");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching book data");
        dialog.setTitle("Loading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        FirebaseDatabase.getInstance()
                .getReference("Book")
                .child(resultID1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dialog.dismiss();
                        if (snapshot.getValue() != null) {
                            Book info = snapshot.getValue(Book.class);
                            infoTitle.setText(info.getTitle());
                            infoAuthor.setText(info.getAuthor());
                            infoYearPub.setText(info.getYearPublished());
                            infoStatus.setText(info.isIs_available() ? "Available" : "Not Available");
                        } else {
                            Toast.makeText(BookInfoStudent.this, "Book does not exist", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BookInfoStudent.this, HomeStudent.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BookInfoStudent.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BookInfoStudent.this, HomeStudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}