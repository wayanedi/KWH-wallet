package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {
    Button simpan;
    ImageView backBtn;
    EditText emailTxt;
    EditText userTxt;
    private String key;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        setContentView(R.layout.profile_edit);

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//            }
//        }, 2000);
        getData();
        emailTxt = findViewById(R.id.emailTxt);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        emailTxt.setEnabled(false);
        simpan = findViewById(R.id.ubahProfile);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userTxt.getText().toString().length()>2){


                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditProfileActivity.this);
                alertBuilder.setMessage("Apakah anda yakin ingin merubah profil anda ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateName(key, userTxt.getText().toString());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = alertBuilder.create();
                alert.setTitle("Edit Profile");
                alert.show();
                }
                else
                    Toast.makeText(getApplication(), "Nama harus terdiri dari minimal 3 karakter", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                    key = snapshot.getKey();
                    userTxt = findViewById(R.id.usernameTxt);
                    emailTxt = findViewById(R.id.emailTxt);
                    userTxt.setText(user.getUsername());
                    emailTxt.setText(user.getEmail());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateName(String key, String name) {
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("username").setValue(name);
            Toast.makeText(getApplication(), "Update Profile Berhasil", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

