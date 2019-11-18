package com.example.kwh_wallet.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Code_activity extends AppCompatActivity {

    private String code;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_activity);

        user = getIntent().getParcelableExtra("USER");

        PFLockScreenFragment fragment = new PFLockScreenFragment();
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setMode(PFFLockScreenConfiguration.MODE_CREATE).setCodeLength(6)
                .setTitle("set security code");
        fragment.setConfiguration(builder.build());

        fragment.setCodeCreateListener(mCodeCreateListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    private void check(){

        PFLockScreenFragment fragment = new PFLockScreenFragment();
        PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(this)
                .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                .setCodeLength(6)
                .setTitle("confirm security code");
        fragment.setConfiguration(builder.build());
        fragment.setEncodedPinCode(code);
        fragment.setLoginListener(mLoginListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_view, fragment).commit();
    }

    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {
                @Override
                public void onCodeInputSuccessful() {

                    user.setPin(code);
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(Code_activity.this, "pin berhasil di set",
                                        Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Code_activity.this, MenuActivity.class);
                                startActivity(i);
                            }
                        }
                    });

                }

                @Override
                public void onFingerprintSuccessful() {

                }

                @Override
                public void onPinLoginFailed() {

                    Toast.makeText(Code_activity.this, "pin tidak sama",
                            Toast.LENGTH_LONG).show();

                }

                @Override
                public void onFingerprintLoginFailed() {

                }
            };

    private final PFLockScreenFragment.OnPFLockScreenCodeCreateListener mCodeCreateListener =
            new PFLockScreenFragment.OnPFLockScreenCodeCreateListener() {
                @Override
                public void onCodeCreated(String encodedCode) {

                    code = encodedCode;

                    check();
                }
            };
}
