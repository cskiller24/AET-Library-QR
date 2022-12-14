package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotpwActivity extends AppCompatActivity {

    EditText resetemail;
    Button btnreset;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpw);
        resetemail = findViewById(R.id.resetemail);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        btnreset = findViewById(R.id.btnreset);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformReset();

            }
        });
    }

    private void PerformReset() {
        String email = resetemail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(ForgotpwActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
        progressDialog.setMessage("Sending link to your email");
        progressDialog.setTitle("Reset Password");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.setLanguageCode("en");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotpwActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotpwActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ForgotpwActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        }
    }
}