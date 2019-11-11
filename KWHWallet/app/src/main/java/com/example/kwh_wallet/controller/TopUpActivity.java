package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;

public class TopUpActivity extends AppCompatActivity {
    TextView price;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_activity);
        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        User user = getIntent().getParcelableExtra("USER");

        price = findViewById(R.id.price);
        price.setText(Double.toString(user.getSaldo()));

        System.out.println("saldo:" + user.getSaldo());
    }
}
