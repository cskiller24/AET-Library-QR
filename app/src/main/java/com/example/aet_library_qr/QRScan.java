package com.example.aet_library_qr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRScan extends AppCompatActivity {
    String classType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        classType = getIntent().getStringExtra("classType");
        scanBook();

    }

    private void scanBook() {
        String type = getIntent().getStringExtra("classType");
        ScanOptions options = new ScanOptions();
        if (type.equals("HomeAdmin") || type.equals("HomeStudent")) {
            options.setPrompt("Scan a book qr");
        }
        if (type.equals("BorrowBookAdmin")) {
            options.setPrompt("Scan student qr");
        }
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScanBook.class);
        options.addExtra("classType", classType);
        barLauncher.launch(options);

    }


    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String type1 = getIntent().getExtras().getString("classType");
            String resultID = result.getContents();

            if (type1.equals("HomeAdmin")) {
                Intent intent = new Intent(QRScan.this, BorrowBookAdmin.class);
                intent.putExtra("resultID", resultID);
                startActivity(intent);
            } else if (type1.equals("HomeStudent")) {
                Intent intent = new Intent(QRScan.this, BookInfoStudent.class);
                intent.putExtra("resultID", resultID);
                startActivity(intent);
            } else if (type1.equals("BorrowBookAdmin")) {
                //Student Book Log
                String bookID = getIntent().getExtras().getString("resultID1");
                Intent intent = new Intent(QRScan.this, BookLogStudent.class);
                intent.putExtra("resultID", resultID); //StudentNum
                intent.putExtra("bookID", bookID);//BookID
                startActivity(intent);
            }
        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (classType.equals("HomeAdmin") || classType.equals("BorrowBookAdmin")) {
            Intent intent = new Intent(QRScan.this, HomeAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (classType.equals("HomeStudent")) {
            Intent intent = new Intent(QRScan.this, Student.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}