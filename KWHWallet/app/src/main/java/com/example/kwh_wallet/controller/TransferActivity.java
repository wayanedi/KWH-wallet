package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private double current_saldo = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final EditText email = findViewById(R.id.email);
        checkSaldo(firebaseUser.getEmail());



        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button transfer = findViewById(R.id.transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(email.getText().toString().equals(firebaseUser.getEmail()))
                    Toast.makeText(getApplication(), "Tidak bisa transfer ke rekening sendiri!", Toast.LENGTH_SHORT).show();
                else
                    selectData(email.getText().toString());
            }
        });
    }

    private void selectData(String email) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    User user = snapshot.getValue(User.class);
                    System.out.println("email user " + user.getEmail() +"saldo user: " + user.getSaldo());
                    EditText value = findViewById(R.id.value);
                    if(value.getText().toString().isEmpty())
                        Toast.makeText(getApplication(), "Saldo tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    else if(value.getText().toString().matches("\\d+")) {
                        System.out.println(current_saldo);
                        if(Double.parseDouble(value.getText().toString())<10000)
                            Toast.makeText(getApplication(), "Minimal transfer Rp 10.000!", Toast.LENGTH_SHORT).show();
                        else if(current_saldo>=Double.parseDouble(value.getText().toString())){
                            updateSaldo(Double.parseDouble(value.getText().toString()) + user.getSaldo(), key);
                            updateSaldo(current_saldo-Double.parseDouble(value.getText().toString()), firebaseUser.getUid());
                        }
                        else
                            Toast.makeText(getApplication(), "Saldo anda tidak mencukupi!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(getApplication(), "Saldo hanya boleh angka!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplication(), "User tidak terdaftar!", Toast.LENGTH_SHORT).show();
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void checkSaldo(String email) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(valueEventListener2);
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    current_saldo=user.getSaldo();
                    TextView saldo = findViewById(R.id.saldo);
                    saldo.setText(String.valueOf(current_saldo));
                    System.out.println("saldo user: " + user.getSaldo());
                }
            }else{
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateSaldo(double saldo, String key) {
        EditText email = findViewById(R.id.email);
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            Toast.makeText(getApplication(), "Transfer Berhasil", Toast.LENGTH_SHORT).show();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle(null);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Transfer Sukses!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        finish();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
