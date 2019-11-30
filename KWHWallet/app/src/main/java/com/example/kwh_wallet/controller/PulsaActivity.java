package com.example.kwh_wallet.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.R;

public class PulsaActivity extends AppCompatActivity {
    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fa = this;
         super.onCreate(savedInstanceState);
        setContentView(R.layout.pulsa_activity);
        PulsaFragmet fragInfo = new PulsaFragmet();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_pulsa_activity, fragInfo).commit();
    }

    public void back(View view){
        finish();
    }

}
