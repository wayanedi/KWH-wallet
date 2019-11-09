package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    Button btn;
    private TextView hyperlink;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextEmail;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUsername = findViewById(R.id.username);

        btn = findViewById(R.id.dbmhs);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
//                String username =editTextUsername.getText().toString().trim();
//                Query query = FirebaseDatabase.getInstance().getReference("users")
//                        .orderByChild("username").equalTo(username);
//                query.addListenerForSingleValueEvent(valueEventListener);

            }
        });

        hyperlink = findViewById(R.id.textView_login);

        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    private void addUser(){
        String username =editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        final User user = new User(username, email);
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        System.out.println("berhasil");
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(email)
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    System.out.println("berhasil menambahkan user");
                                    Toast.makeText(SignUp.this, "User berhasil didaftarkan",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        System.out.println("gagal");
                        Toast.makeText(SignUp.this, "Daftar gagal",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
    }

    public boolean cekEmail(String emailx){
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailx.matches(emailPattern))
        {
            return  true;
        }
        else
        {
            return  false;
        }

    }
}
