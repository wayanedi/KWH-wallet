package com.example.kwh_wallet.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {
    private double current_saldo = 0;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference database;
    private String FCM_API = "https;//fcm.googleapis.com/fcm/send";
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final EditText email = findViewById(R.id.email);
        checkSaldo(firebaseUser.getEmail());

        mRequestQueue = Volley.newRequestQueue(this);


        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button transfer = findViewById(R.id.transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(email.getText().toString().equals(firebaseUser.getEmail()))
                    Toast.makeText(getApplication(), "Tidak bisa transfer ke rekening sendiri!", Toast.LENGTH_SHORT).show();
                else
                    selectData(email.getText().toString());
            }
        });
    }

    private void selectData(String email) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    User user = snapshot.getValue(User.class);
                    System.out.println("email user " + user.getEmail() +"saldo user: " + user.getSaldo());
                    EditText value = findViewById(R.id.value);
                    if(value.getText().toString().isEmpty())
                        Toast.makeText(getApplication(), "Saldo tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    else if(value.getText().toString().matches("\\d+")) {
                        System.out.println(current_saldo);
                        if(Double.parseDouble(value.getText().toString())<10000)
                            Toast.makeText(getApplication(), "Minimal transfer Rp 10.000!", Toast.LENGTH_SHORT).show();
                        else if(current_saldo>=Double.parseDouble(value.getText().toString())){
                            updateSaldo(Double.parseDouble(value.getText().toString()) + user.getSaldo(), key, "+");
                            updateSaldo(current_saldo-Double.parseDouble(value.getText().toString()), firebaseUser.getUid(), "-");
                        }
                        else
                            Toast.makeText(getApplication(), "Saldo anda tidak mencukupi!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    Toast.makeText(getApplication(), "Saldo hanya boleh angka!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplication(), "User tidak terdaftar!", Toast.LENGTH_SHORT).show();
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void checkSaldo(String email) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(valueEventListener2);
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                System.out.println("ada data nya");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    current_saldo=user.getSaldo();
                    TextView saldo = findViewById(R.id.saldo);
                    saldo.setText(String.format("%,.0f", current_saldo));
                    System.out.println("saldo user: " + user.getSaldo());
                }
            }else{
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void updateSaldo(double saldo, String key, String stats) {
        EditText email = findViewById(R.id.email);
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            EditText value = findViewById(R.id.value);
            mDatabase.child(key).child(formatter.format(calendar.getTime())).child("jumlah").setValue(stats+" "+value.getText().toString());
            mDatabase.child(key).child(formatter.format(calendar.getTime())).child("deskripsi").setValue("Transfer");
//            mDatabase.child(key).child(formatter.format(calendar.getTime())).child("from").setValue(firebaseUser.getDisplayName());
//            mDatabase.child(key).child(formatter.format(calendar.getTime())).child("to").setValue(firebaseUser.getDisplayName());
            Toast.makeText(getApplication(), "Transfer Berhasil", Toast.LENGTH_SHORT).show();
            showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(){
        final EditText value = findViewById(R.id.value);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle(null);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Transfer Sukses!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini & Mengirimkan Notifdication
                        JSONObject notification = new JSONObject();
                        JSONObject notificationBody = new JSONObject();
                        try{
                           notificationBody.put("title","KWH-Wallet");
                           notificationBody.put("body","Anda Telah menerima Transfer sebesar Rp."+Double.parseDouble(value.getText().toString()));
                            notification.put("to","topic");
                            notification.put("data", notificationBody);
                            sendNotification(notification);
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        finish();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void sendNotification(JSONObject notification){
        Log.e("TAG", "sendNotification");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG", "onErrorResponse: Didn't Work");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("content-type", "application/json");
                header.put("authorization", "key=AIzaSyCnulFnxDZXCyicvKI69DTa-Jxc5c9e2VI");
                return header;

            }
        };
        mRequestQueue.add(request);
    }

}
