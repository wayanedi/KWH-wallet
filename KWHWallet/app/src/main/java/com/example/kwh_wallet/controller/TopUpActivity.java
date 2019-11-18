package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpActivity extends AppCompatActivity {

    Button topupBtn;
    TextView topupfield;
    private User user;

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

    private void doTopup(){

        System.out.println("saldo:" + user.getSaldo());

        String getTopup = topupfield.getText().toString();

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

        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("berhasil menambahkan saldo");
                    Toast.makeText(TopUpActivity.this, "saldo berhasil di tambahkan",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
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
