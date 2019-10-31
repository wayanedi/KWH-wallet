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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    DatabaseReference databaseUser;
    Button btn;
    private TextView hyperlink;
    EditText editTextUsername;
    EditText editTextPassword;
    EditText editTextEmail;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        databaseUser = FirebaseDatabase.getInstance().getReference("users");
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUsername = findViewById(R.id.username);

        btn = findViewById(R.id.dbmhs);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username =editTextUsername.getText().toString().trim();
                Query query = FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("username").equalTo(username);
                query.addListenerForSingleValueEvent(valueEventListener);

            }
        });

        hyperlink = findViewById(R.id.textView_login);

        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
            }
        });

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                System.out.println();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    System.out.println(user.getEmail());
                    addUser(true);
                }
            }
            else{
                addUser(false);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void addUser(boolean isExist){


        if(isExist) {
            System.out.println("this username has been registered");
            Toast.makeText(this, "this user has been registered !", Toast.LENGTH_LONG).show();
            return;
        }

        String username =editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(!cekEmail(email)){
            Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
            return;

        }

        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            String id = databaseUser.push().getKey();

            User user = new User(id, username, email, password);
            databaseUser.child(id).setValue(user);
            editTextUsername.getText().clear();
            editTextEmail.getText().clear();
            editTextPassword.getText().clear();
            Toast.makeText(this, "your register is success !", Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(this, "your should complete the field !", Toast.LENGTH_LONG).show();

        }
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
