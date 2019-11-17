package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private TextView hyperlink;
    private Button btn;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private User user;

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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void loginUser(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.equals("") || password.equals(""))
            Toast.makeText(Login.this, "Email or Password can't be empty!",
                 Toast.LENGTH_LONG).show();
        else
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Login Success",
                                    Toast.LENGTH_LONG).show();
                            cekPin();

                        } else {
                            System.out.println("gagal");
                            Toast.makeText(Login.this, "Login anda gagal",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);


                    System.out.println("pin user: " + user.getPin());



                    if(user.getPin().equals("NONE")){

                        Intent i = new Intent(Login.this, Code_activity.class);
                        i.putExtra("USER", user);
                        startActivity(i);

                    }
                    else{
                        Intent i = new Intent(Login.this, MenuActivity.class);
                        startActivity(i);
                    }
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private boolean cekPin(){

        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addListenerForSingleValueEvent(valueEventListener);

        return true;
    }


}
