package com.example.kwh_wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kwh_wallet.controller.Login;
import com.example.kwh_wallet.controller.MenuActivity;

public class MainActivity extends AppCompatActivity {
    private int loadTime=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.launch_ui);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent home=new Intent(MainActivity.this, MenuActivity.class);
                startActivity(home);
                finish();

            }
        },loadTime);
    }
}
