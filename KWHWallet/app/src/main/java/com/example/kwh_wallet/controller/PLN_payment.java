package com.example.kwh_wallet.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
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
    private List<String> nominalPembayaran = new ArrayList<String>();
    private Spinner nominal;
    private RadioGroup rg;
    private TextView nomorMeter;
    private int getNominal= 0;
    Dialog customDialog;
    int total;
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
        getNominalList();

        rg = findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                if(rb != null){
                    pembayaran = String.valueOf(rb.getText());
                }
            }
        });

        nomorMeter = findViewById(R.id.nomorMeter);
        customDialog = new Dialog(this);
        next =  findViewById(R.id.BayarPln);
    }

    public void nextPembayaran(View v){
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
            }
        });

        Button bayar = customDialog.findViewById(R.id.BayarPln);
        bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSaldo(current_saldo-Double.parseDouble(String.valueOf(total)), firebaseUser.getUid(), "-");
                finish();
            }
        });

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();

    }

    private void updateSaldo(double saldo, String key, String stats) {
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            History history = new History(formatter.format(calendar.getTime()), "+ Rp. "+String.valueOf(total), "Transfer");
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            mDatabase.child(key).child(formatter.format(calendar.getTime())).setValue(history);
            Toast.makeText(getApplication(), "Transfer Berhasil", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(View view){
        finish();
    }

    private void getNominalList(){
        nominalPembayaran.add("Pilih Nominal");
        nominalPembayaran.add("Rp 20.000");
        nominalPembayaran.add("Rp 50.000");
        nominalPembayaran.add("Rp 100.000");
        nominalPembayaran.add("Rp 200.000");
        nominalPembayaran.add("Rp 500.000");
        nominalPembayaran.add("Rp 1.000.000");
        nominalPembayaran.add("Rp 2.000.000");

        ArrayAdapter<String> nominalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nominalPembayaran);
        nominal = findViewById(R.id.nomimal);
        nominal.setAdapter(nominalAdapter);
        nominal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorOrangeDark));
                Object item = parent.getItemAtPosition(position);
                if(item != null){
                    Toast.makeText(PLN_payment.this, item.toString(), Toast.LENGTH_SHORT).show();
                    for(int i=0 ; i<item.toString().length() ; i++){
                        char c = item.toString().charAt(i);
                        if(Character.isDigit(c)){
                            getNominal = (getNominal*10)+Integer.parseInt(String.valueOf(c));
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    sisaSaldo = findViewById(R.id.sisaSaldoLabel);
                    DecimalFormat df = new DecimalFormat("#");
//                    System.out.print();

                    sisaSaldo.setText("Sisa Saldo Anda Rp. "+String.format("%,.0f", user.getSaldo()));
                    sisaSaldo.setVisibility(View.VISIBLE);
                    sisaSaldo.bringToFront();
                    System.out.println("saldo user: " + user.getSaldo());
                    current_saldo=user.getSaldo();
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
