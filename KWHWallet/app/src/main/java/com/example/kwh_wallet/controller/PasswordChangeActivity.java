package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeActivity extends AppCompatActivity {
    private Button ubahPassword;
    private TextView passwordLama;
    private TextView passwordBaru;
    private TextView retypePassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        passwordBaru = findViewById(R.id.passwordBaru);
        passwordLama = findViewById(R.id.passwordLama);
        retypePassword = findViewById(R.id.retypePassword);

        ubahPassword = findViewById(R.id.changePasswordBtn);
        ubahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder  alertBuilder = new AlertDialog.Builder(PasswordChangeActivity.this);
                alertBuilder.setMessage("Are you sure want to change your Password?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                changePassword();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Password Change");
                alert.show();
            }
        });
    }

    private void changePassword(){

        String getPasswordLama = passwordLama.getText().toString().trim();
        String getPasswordBaru = passwordBaru.getText().toString().trim();
        String getRetypePassword = retypePassword.getText().toString().trim();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PasswordChangeActivity.this);
        if(getPasswordLama.isEmpty()){

            alertBuilder.setMessage("password lama tidak boleh kosong !");
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Warning !");
            alert.show();

            return;
        }

        if(getPasswordBaru.isEmpty()){

            alertBuilder.setMessage("password baru tidak boleh kosong !");
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Warning !");
            alert.show();

            return;
        }

        if(!getPasswordBaru.equals(getRetypePassword)){

            alertBuilder.setMessage("password tidak sama !");
            AlertDialog alert = alertBuilder.create();
            alert.setTitle("Warning !");
            alert.show();

            return;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(firebaseUser.getEmail(), getPasswordLama);

        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){

                    alertBuilder.setMessage("password lama tidak sesuai !");
                    AlertDialog alert = alertBuilder.create();
                    alert.setTitle("Warning !");
                    alert.show();
                    return;
                }
            }
        });

        firebaseUser.updatePassword(getPasswordBaru).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    alertBuilder.setMessage("password berhasil di ganti !").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            PasswordChangeActivity.this.finish();

                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.setTitle("Information !");
                    alert.show();

                }
            }
        });


    }
}
