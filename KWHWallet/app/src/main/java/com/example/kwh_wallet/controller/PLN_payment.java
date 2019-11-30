package com.example.kwh_wallet.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PLN_payment extends AppCompatActivity {
    private Button next;


    private TextView nomorMeter;
    private int getNominal= 0;
    Dialog customDialog;
    int total;
    boolean actionNext = false;
    String pembayaran;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private TextView sisaSaldo;
    private User user;
    private double current_saldo = -1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pln_payment_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        getSaldo();



    }

    public void nextPembayaran(View v){

        System.out.println("oke dongggg");
        System.out.println("pin pln: " + user.getPin());


        final View coba = v;

        Spinner mySpinner = (Spinner) findViewById(R.id.nomimal);
        String text = mySpinner.getSelectedItem().toString();
        System.out.println("spinner: " + text);

        for(int i=0 ; i<text.toString().length() ; i++){
            char c = text.toString().charAt(i);
            if(Character.isDigit(c)){
                getNominal = (getNominal*10)+Integer.parseInt(String.valueOf(c));
            }
        }

        customDialog = new Dialog(v.getContext());
        nomorMeter = findViewById(R.id.nomorMeter);
        customDialog.setContentView(R.layout.confirm_pln_payment);
        TextView nomorMeterCD = customDialog.findViewById(R.id.nomorMeter);
        TextView namaPelangganCD = customDialog.findViewById(R.id.namapelanggan);
        TextView periodeCD = customDialog.findViewById(R.id.periode);
        TextView biayaTagihCD = customDialog.findViewById(R.id.biayatagihan);
        Calendar c = Calendar.getInstance();
        String[] monthName = {"January","February","March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = monthName[c.get(Calendar.MONTH)];

        nomorMeterCD.setText(nomorMeter.getText());
        namaPelangganCD.setText(firebaseUser.getEmail());
        periodeCD.setText(month);
        biayaTagihCD.setText("Rp. "+String.valueOf(getNominal));
        total = getNominal+2000;
        Button batal = customDialog.findViewById(R.id.Batalkan);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                actionNext = false;
            }
        });

        Button bayar = customDialog.findViewById(R.id.BayarPln);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialog.dismiss();
                PFLockScreenFragment fragment = new PFLockScreenFragment();
                PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(coba.getContext())
                        .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                        .setTitle("masukan security code anda")
                        .setCodeLength(6);
                fragment.setConfiguration(builder.build());
                fragment.setEncodedPinCode("F4zJ69ixo2Q+tAKJ/J70p+dW/8OKxocLRjelwbanPZ1NADerRw/5JF1UBPG86Ng9sC0LEm3yDrjhyRgIkp6KFYtnglWC3qcCGFEURZ2gSjQ9plo5vm6K6zXMHteekubUU4CEmc122v+TbUVEy4brB/C9LdxOjLnHymKoR+XCUUKb3IqQU3HOA51/8dWP4kQXI8/7OHITD2+v4U8B/Bup9tOieQkrVuvzLqhLEya5Ws34Bm2+nEUeNme0qDXX4Xmrq44vLwphX9aKsLLbSSbfDFCdD1Wz7kX2fMasO28pZ1JAORKYG2d8WfVZOHQPqWp96VJOOWA0e3lava8dDHj10g==");
                fragment.setLoginListener(mLoginListener);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_view_pln, fragment).commit();


            }
        });

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        System.out.println("tess dong");


    }

    private void checkPin(){

    }

    private void updateSaldo(double saldo, String key, String stats) {
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            History history = new History(formatter.format(calendar.getTime()), stats+" Rp. "+String.valueOf(total), "PLN");
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            mDatabase.child(key).child(formatter.format(calendar.getTime())).setValue(history);
            Toast.makeText(getApplication(), "pembayaran Berhasil", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(View view){
        finish();
    }

//    private void getNominalList(){

//        nominal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorOrangeDark));
//                Object item = parent.getItemAtPosition(position);
//                if(item != null){
//                    Toast.makeText(PLN_payment.this, item.toString(), Toast.LENGTH_SHORT).show();

//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

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


                    System.out.println("saldo user: " + user.getSaldo());
                    current_saldo=user.getSaldo();



                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();

                            Pln_payment_fragment fragInfo = new Pln_payment_fragment();
                            bundle.putDouble("saldo", current_saldo);
                            fragInfo.setArguments(bundle);

                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container_view_pln, fragInfo).commit();
                        }
                    },1000);
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private final  PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {
                @Override
                public void onCodeInputSuccessful() {

                    
                }

                @Override
                public void onFingerprintSuccessful() {

                }

                @Override
                public void onPinLoginFailed() {

                }

                @Override
                public void onFingerprintLoginFailed() {

                }
            };

}
