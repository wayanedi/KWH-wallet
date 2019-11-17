package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;

public class TransferFromScannerActivity extends AppCompatActivity {
    TextView usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transfer_from_scanner);

        Bundle extras = getIntent().getExtras();
        String email = extras.getString(ScannerActivity.KEY_EMAIL);
        Double saldo = extras.getDouble(ScannerActivity.KEY_SALDO);
        String username = extras.getString(ScannerActivity.KEY_USERNAME);
        String key = extras.getString(ScannerActivity.KEY_UID);
        System.out.println(email);
        System.out.println("test" + username);

        if (extras == null) {
        } else {
            usr = findViewById(R.id.username);
            usr.setText("Trasfer ke: " + username);
        }
//
    }
}
