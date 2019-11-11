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

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class TopUpActivity extends AppCompatActivity {
    TextView price;
    Button topupBtn;
    TextView topupfield;

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
        final User user = getIntent().getParcelableExtra("USER");
        topupfield = findViewById(R.id.topupfield);
        price = findViewById(R.id.price);
        price.setText(Double.toString(user.getSaldo()));




        System.out.println("saldo:" + user.getSaldo());

        topupBtn = findViewById(R.id.topupBtn);

        topupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


    }
}
