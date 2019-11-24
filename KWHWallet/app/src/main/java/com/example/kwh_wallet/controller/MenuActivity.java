package com.example.kwh_wallet.controller;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.icu.text.DecimalFormat;


import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kwh_wallet.NotificationService.TransferNotificationService;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    ImageView setting;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView price;
    private User user;

    private Double oldSaldo;
    private Double newSaldo;

    private BroadcastReceiver minuteUpdateReceiver;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        System.out.println(firebaseUser.getEmail());
        getSaldo();
        loadFragment(new HomeFragment());

        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Button topUp = (Button) findViewById(R.id.topUp);
        topUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent topUp=new Intent(v.getContext(), TopUpActivity.class);
                topUp.putExtra("USER", user);
                startActivity(topUp);
            }
        });
        setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, SettingActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                price.setVisibility(View.GONE);
                getSaldo();
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void friendList(View view){
        loadFragment(new ListFragment());
    }

    private void getSaldo(){
                Query query = FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("email").equalTo(firebaseUser.getEmail());
                query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);
                    price = findViewById(R.id.price);

                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    DecimalFormat df = new DecimalFormat("#");
//                    System.out.print();

                    price.setText(String.format("%,.0f", user.getSaldo()));
                    price.setVisibility(View.VISIBLE);
                    price.bringToFront();
                    if(oldSaldo == null){
                        oldSaldo = user.getSaldo();
                    }else{
                        newSaldo = user.getSaldo();
                        startService();
                    }
                    System.out.println("saldo user: " + user.getSaldo());
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.search_menu:
                Intent i = new Intent(MenuActivity.this, ScannerActivity.class);
                startActivity(i);
                break;
            case R.id.list_history:
                fragment = new HistoryFragment();
                break;

        }
        return loadFragment(fragment);
    }

    //Notification

    public void startService(){
        if(newSaldo > oldSaldo){
            Intent serviceIntent = new Intent(this, TransferNotificationService.class);
            serviceIntent.putExtra("inputExtra", "Anda telah menerima Transfer sebesar Rp "+String.valueOf((newSaldo-oldSaldo)));

            ContextCompat.startForegroundService(this, serviceIntent);
            oldSaldo = newSaldo;
            newSaldo = null;
        }
        else if(newSaldo == oldSaldo){
            newSaldo = null;
        }
        else if(oldSaldo > newSaldo){
            Intent serviceIntent = new Intent(this, TransferNotificationService.class);
            serviceIntent.putExtra("inputExtra", "Anda telah melakukan Transfer sebesar Rp "+String.valueOf((oldSaldo-newSaldo)));

            ContextCompat.startForegroundService(this, serviceIntent);
            oldSaldo = newSaldo;
            newSaldo = null;
        }
    }

    //AutoRefresh

    public void startMinuteUpdater() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        minuteUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                counter++;
            }
        };

        registerReceiver(minuteUpdateReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMinuteUpdater();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(minuteUpdateReceiver);
    }
}
