package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;
import com.example.kwh_wallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TopUpActivity extends AppCompatActivity {

    Button topupBtn;
    TextView topupfield;
    private User user;
    private String getTopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_activity);

        user = getIntent().getParcelableExtra("USER");

        Bundle bundle = new Bundle();
        bundle.putDouble("saldo", user.getSaldo());
        TopupFragment fragInfo = new TopupFragment();
        fragInfo.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view_topup, fragInfo).commit();
    }

    public void back(View view){
        finish();
    }

    public void topUp(View view){

        topupfield = findViewById(R.id.topupfield);

        System.out.println("saldo:" + user.getSaldo());

        getTopup = topupfield.getText().toString();

        if(getTopup.isEmpty()){

            Toast.makeText(TopUpActivity.this, "field tidak boleh kosong",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(Double.parseDouble(getTopup)<10000){
            Toast.makeText(TopUpActivity.this, "minimum top up 10000",
                    Toast.LENGTH_LONG).show();
            return;
        }

        double saldo = user.getSaldo()+ Double.parseDouble(getTopup);
        user.setSaldo(saldo);


        PFLockScreenFragment fragment = new PFLockScreenFragment();
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                .setTitle("masukan security code anda")
                .setCodeLength(6);
        fragment.setConfiguration(builder.build());
        fragment.setEncodedPinCode(user.getPin());
        fragment.setLoginListener(mLoginListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view_topup, fragment).commit();

    }

//    private void doTopup(){
//
//
//        FirebaseDatabase.getInstance().getReference("users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    System.out.println("berhasil menambahkan saldo");
//                    Toast.makeText(TopUpActivity.this, "saldo berhasil di tambahkan",
//                            Toast.LENGTH_LONG).show();
//                    finish();
//                }
//            }
//        });
//    }

    private void doTopup() {
        try {

            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("saldo").setValue(user.getSaldo());
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            History history = new History(formatter.format(calendar.getTime()), "+ Rp. "+getTopup, "Topup");
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(formatter.format(calendar.getTime())).setValue(history);
            Toast.makeText(getApplication(), "Topup Berhasil", Toast.LENGTH_SHORT).show();
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
                .setMessage("Topup Sukses!")
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
                    Toast.makeText(TopUpActivity.this, "Pin sama",
                           Toast.LENGTH_LONG).show();
                    doTopup();
                }

                @Override
                public void onFingerprintSuccessful() {

                }

                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(TopUpActivity.this, "Pin tidak sama",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFingerprintLoginFailed() {

                }
            };
}
