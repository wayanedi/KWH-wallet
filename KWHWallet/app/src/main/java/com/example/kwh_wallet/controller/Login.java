package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kwh_wallet.MainActivity;
import com.example.kwh_wallet.R;

public class Login extends AppCompatActivity {
    private TextView hyperlink;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        hyperlink = findViewById(R.id.HyperlinkDaftar);
        hyperlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.signup_activity);
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
            }
        });
    }


}
