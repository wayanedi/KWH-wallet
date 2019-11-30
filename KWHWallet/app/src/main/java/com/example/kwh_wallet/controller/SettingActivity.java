package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;

import org.w3c.dom.Text;

public class SettingActivity extends AppCompatActivity {
    TextView ubahPw;
    TextView editPrfl;

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

        findViewById(R.id.tentangKWH).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, TentangActivity.class));
            }
        });

        findViewById(R.id.kebijakanPrivasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, KebijakanPrivasi.class));
            }
        });

        findViewById(R.id.pusatBantuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, PusatBantuan.class));
            }
        });
    }


}
