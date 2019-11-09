package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;

public class SettingActivity extends AppCompatActivity {
    Button ubahPw;
    Button editPrfl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        ubahPw = findViewById(R.id.ubahPassword);
        ubahPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubahPassword = new Intent(SettingActivity.this, PasswordChangeActivity.class);
                startActivity(ubahPassword);
            }
        });
        editPrfl = findViewById(R.id.editProfil);
        editPrfl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ubahProfile = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(ubahProfile);
            }
        });
    }
}
