package com.example.aet_library_qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {

    EditText oldPassword, newPassword, newPassword2;
    Button btnAuth, btnChangeGo;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        newPassword2 = findViewById(R.id.newPassword2);
        btnChangeGo = findViewById(R.id.btnChangeGo);
        btnAuth = findViewById(R.id.btnAuth);

        newPassword.setEnabled(false);
        newPassword2.setEnabled(false);
        btnChangeGo.setEnabled(false);

        btnAuth.setOnClickListener(v -> {
            String oldPass = oldPassword.getText().toString();

            if(oldPass.isEmpty()){
                Toast.makeText(ChangePassword.this, "Enter Current Password", Toast.LENGTH_SHORT).show();
                oldPassword.setError("Please enter your current password");
                oldPassword.requestFocus();
            }
            else{
                progressDialog.setMessage("Please wait a while");
                progressDialog.setTitle("Authentication");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), oldPass);
                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ChangePassword.this, "You can now proceed", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            oldPassword.setEnabled(false);
                            btnAuth.setEnabled(false);

                            newPassword.setEnabled(true);
                            newPassword2.setEnabled(true);
                            btnChangeGo.setEnabled(true);

                            btnChangeGo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String newPass = newPassword.getText().toString();
                                    String newPass2 = newPassword2.getText().toString();

                                    if(newPass.isEmpty()){
                                        Toast.makeText(ChangePassword.this, "Enter new password", Toast.LENGTH_SHORT).show();
                                        newPassword.setError("Enter new password");
                                        newPassword.requestFocus();
                                    }
                                    else if(newPass2.isEmpty()){
                                        Toast.makeText(ChangePassword.this, "Enter new password", Toast.LENGTH_SHORT).show();
                                        newPassword2.setError("Enter new password");
                                        newPassword2.requestFocus();
                                    }
                                    else if (!newPass.matches(newPass2)){
                                        Toast.makeText(ChangePassword.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                                    }
                                    else if (newPass.matches(oldPass) || newPass2.matches(oldPass)){
                                        Toast.makeText(ChangePassword.this, "Cannot be the same with old password", Toast.LENGTH_SHORT).show();
                                        newPassword.setError("Enter new password");
                                        newPassword.requestFocus();
                                    }
                                    else{
                                        progressDialog.setMessage("Please wait a while");
                                        progressDialog.setTitle("Changing Password");
                                        progressDialog.setCanceledOnTouchOutside(false);
                                        progressDialog.show();

                                        mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(ChangePassword.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(ChangePassword.this, HomeStudent.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    try{
                                                        throw task.getException();
                                                    }
                                                    catch (Exception e){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(ChangePassword.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }

                                }
                            });
                        }
                        else{
                            try{
                                throw task.getException();
                            }
                            catch (Exception e){
                                progressDialog.dismiss();
                                Toast.makeText(ChangePassword.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }
}