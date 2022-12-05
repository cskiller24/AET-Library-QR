package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookInfoStudent extends AppCompatActivity {
    TextView textView3, infoTitle, infoAuthor, infoYearPub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info_student);
        textView3 = findViewById(R.id.textView3);
        infoTitle = findViewById(R.id.infoTitle);
        infoAuthor = findViewById(R.id.infoAuthor);
        infoYearPub = findViewById(R.id.infoYearPub);

        String resultID1 = getIntent().getExtras().getString("resultID");
        textView3.setText(resultID1);

        FirebaseDatabase.getInstance()
            .getReference("Book")
            .child(resultID1)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null){
                        Book info = snapshot.getValue(Book.class);
                        infoTitle.setText(info.getTitle());
                        infoAuthor.setText(info.getAuthor());
                        infoYearPub.setText(info.getYearPublished());
                    }
                    else{
                        Toast.makeText(BookInfoStudent.this, "Book does not exist", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(BookInfoStudent.this, HomeAdmin.class);
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
}