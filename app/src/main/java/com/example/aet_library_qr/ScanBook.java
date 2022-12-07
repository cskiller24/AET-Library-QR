package com.example.aet_library_qr;

import android.content.Intent;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanBook extends CaptureActivity {
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String classType = getIntent().getStringExtra("classType");
        if (classType.equals("HomeAdmin") || classType.equals("BorrowBookAdmin")) {
            Intent intent = new Intent(ScanBook.this, HomeAdmin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (classType.equals("HomeStudent")) {
            Intent intent = new Intent(ScanBook.this, HomeStudent.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
