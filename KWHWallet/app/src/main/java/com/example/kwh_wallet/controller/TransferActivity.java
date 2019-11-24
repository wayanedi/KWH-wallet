package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransferActivity extends AppCompatActivity {
    private double current_saldo = -1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    EditText value;
    private String key_ = "";
    private User user_ = new User();
    private DatabaseReference database;
    private String pin = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        checkSaldo(firebaseUser.getEmail());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();

                TransferFragment fragInfo = new TransferFragment();
                bundle.putDouble("saldo", current_saldo);
                fragInfo.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_view_transfer, fragInfo).commit();
            }
        },3000);

    }

    public void transfer(View view){

        value = findViewById(R.id.value);
        System.out.println(value.getText().toString());
        final EditText email = findViewById(R.id.email);
        if(email.getText().toString().equals(firebaseUser.getEmail()))
            Toast.makeText(getApplication(), "Tidak bisa transfer ke rekening sendiri!", Toast.LENGTH_SHORT).show();
        else
            selectData(email.getText().toString());
    }

    public void kembali(View view){
        finish();
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
                    key_ = snapshot.getKey();
                    user_ = snapshot.getValue(User.class);
                    System.out.println("email user " + user_.getEmail() +"saldo user: " + user_.getSaldo());
                    if(value.getText().toString().isEmpty())
                        Toast.makeText(getApplication(), "Saldo tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    else if(value.getText().toString().matches("\\d+")) {
                        System.out.println(current_saldo);
                        if(Double.parseDouble(value.getText().toString())<10000)
                            Toast.makeText(getApplication(), "Minimal transfer Rp 10.000!", Toast.LENGTH_SHORT).show();
                        else if(current_saldo>=Double.parseDouble(value.getText().toString())){
//                            System.out.println(pin);
                            PFLockScreenFragment fragment = new PFLockScreenFragment();
                            PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(TransferActivity.this)
                                    .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                                    .setTitle("Masukan security code anda")
                                    .setCodeLength(6);
                            fragment.setConfiguration(builder.build());
                            fragment.setEncodedPinCode(pin);
                            fragment.setLoginListener(mLoginListener);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_view_transfer, fragment).commit();
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
                    pin = user.getPin();
                    current_saldo=user.getSaldo();
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

    private void updateSaldo(double saldo, String key, String stats) {
        EditText email = findViewById(R.id.email);
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            History history = new History(formatter.format(calendar.getTime()), "+ Rp. "+value.getText().toString(), "Transfer");
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            mDatabase.child(key).child(formatter.format(calendar.getTime())).setValue(history);
            Toast.makeText(getApplication(), "Transfer Berhasil", Toast.LENGTH_SHORT).show();

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

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {
                @Override
                public void onCodeInputSuccessful() {
                    Toast.makeText(TransferActivity.this, "Berhasil",
                            Toast.LENGTH_LONG).show();
                    updateSaldo(Double.parseDouble(value.getText().toString()) + user_.getSaldo(), key_, "+");
                    updateSaldo(current_saldo-Double.parseDouble(value.getText().toString()), firebaseUser.getUid(), "-");
                    showDialog();
//                    finish();
                }

                @Override
                public void onFingerprintSuccessful() {

                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(TransferActivity.this, "Pin salah",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFingerprintLoginFailed() {

                }
            };

}
