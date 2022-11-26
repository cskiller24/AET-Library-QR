package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText createemail;
    EditText createpassword;
    EditText createpassword2;
    Button btnreg;
    String emailPattern = "[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createemail = findViewById(R.id.createemail);
        createpassword = findViewById(R.id.createpassword);
        createpassword2 = findViewById(R.id.createpassword2);

        btnreg = findViewById(R.id.btnreg);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

    }

    private void PerformAuth() {
        String email = createemail.getText().toString();
        String password = createpassword.getText().toString();
        String password2 = createpassword2.getText().toString();
        if (!email.matches(emailPattern)) {
            createemail.setError("Enter Valid Email");
        } else if (password.isEmpty() || password.length() < 6) {
            createpassword.setError("Enter Valid Password");
        } else if (!password.equals(password2)) {
            createpassword2.setError("Password Doesn't Match");
        } else {
            progressDialog.setMessage("Please Wait for you Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendUserToNextActivity(user.getUid());
                        }
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthUserCollisionException e) {
                            createemail.setError("Email is already taken");
                        } catch (Exception e) {
                            Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity(String uid) {
        String email = createemail.getText().toString();
        Intent intent = new Intent(RegisterActivity.this, UpdateProfile.class);
        intent.putExtra("email", email);
        intent.putExtra("uid", uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}