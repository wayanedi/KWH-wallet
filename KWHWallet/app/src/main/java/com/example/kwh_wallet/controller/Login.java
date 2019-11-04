package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kwh_wallet.MainActivity;
import com.example.kwh_wallet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextView hyperlink;
    private Button btn;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        hyperlink = findViewById(R.id.HyperlinkDaftar);
        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });


        btn = findViewById(R.id.dbmhs);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();

            }
        });
    }

    public void loginUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent i = new Intent(Login.this, MenuActivity.class);
                            Toast.makeText(Login.this, "Login Success",
                                    Toast.LENGTH_LONG).show();
                            startActivity(i);
                        } else {
                            System.out.println("gagal");
                            Toast.makeText(Login.this, "Login anda gagal",
                                    Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }


}
