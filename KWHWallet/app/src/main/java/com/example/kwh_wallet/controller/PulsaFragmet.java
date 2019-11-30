package com.example.kwh_wallet.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration;
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment;
import com.example.kwh_wallet.NotificationService.Data;
import com.example.kwh_wallet.NotificationService.Sender;
import com.example.kwh_wallet.NotificationService.Token;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PulsaFragmet extends Fragment {
    private String myUid;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    TextView price;
    private Button next;
    private String key_;
    private EditText nomorMeter;
    Dialog customDialog;
    private List<String> nominalPembayaran = new ArrayList<String>();
    Spinner nominal;
    private int total=0;
    private int getNominal= 0;
    private double current_saldo = 0;
    private String pin;
    private TextView sisaSaldo;
    private User user;
    boolean notify = false;

    private RequestQueue requestQueue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pulsa_fragment, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myUid = firebaseUser.getUid();
        nominal = view.findViewById(R.id.nominal);
        getNominalList();
        requestQueue = Volley.newRequestQueue(getContext());
        getSaldo();
        customDialog = new Dialog(getContext());
        next =  view.findViewById(R.id.BayarPulsa);
        sisaSaldo = view.findViewById(R.id.sisaSaldoLabel);
        nomorMeter = view.findViewById(R.id.nomorMeter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPembayaran(view);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ArrayAdapter<String> nominalAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nominalPembayaran);

        nominal.setAdapter(nominalAdapter);
        nominal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorOrangeDark));
                Object item = parent.getItemAtPosition(position);
                if(item != null){
                    Toast.makeText(getContext(), item.toString(), Toast.LENGTH_SHORT).show();
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
                    key_ = snapshot.getKey();
                    user = snapshot.getValue(User.class);
                    pin = user.getPin();
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

    public void nextPembayaran(View v){
        customDialog.setContentView(R.layout.confirm_pulsa_payment);
        TextView nomorMeterCD = customDialog.findViewById(R.id.nomorMeter);
        TextView namaPelangganCD = customDialog.findViewById(R.id.namapelanggan);
        TextView biayaTagihCD = customDialog.findViewById(R.id.biayatagihan);

        nomorMeterCD.setText(nomorMeter.getText());
        namaPelangganCD.setText(firebaseUser.getEmail());
//        String a = String.format("%,.0f", String.valueOf(getNominal));
        biayaTagihCD.setText("Rp. "+getNominal);
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
                PFLockScreenFragment fragment = new PFLockScreenFragment();
                PFFLockScreenConfiguration.Builder builder = new PFFLockScreenConfiguration.Builder(getContext())
                        .setMode(PFFLockScreenConfiguration.MODE_AUTH)
                        .setTitle("Masukan security code anda")
                        .setCodeLength(6);
                fragment.setConfiguration(builder.build());
                fragment.setEncodedPinCode(pin);
                fragment.setLoginListener(mLoginListener);

                getChildFragmentManager().beginTransaction()
                        .add(R.id.container_pulsa_activity, fragment).commit();
//                updateSaldo(current_saldo-Double.parseDouble(String.valueOf(total)), firebaseUser.getUid(), "-");
//                PulsaActivity.fa.finish();
            }
        });

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();

    }


    private final PFLockScreenFragment.OnPFLockScreenLoginListener mLoginListener =
            new PFLockScreenFragment.OnPFLockScreenLoginListener() {
                @Override
                public void onCodeInputSuccessful() {
                    Toast.makeText(getContext(), "Berhasil",
                            Toast.LENGTH_LONG).show();
                    notify = true;
                    final String message = "Anda Telah Melakukan pembelian pulsa sebesar Rp,"+ Double.parseDouble(String.valueOf(total));
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("users").child(key_);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if(notify){
                                sendNotification(key_, user.getEmail(), message);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    System.out.println("masuk");
                    updateSaldo(current_saldo-Double.parseDouble(String.valueOf(total)), firebaseUser.getUid(), "-");
                    PulsaActivity.fa.finish();
                    showDialog();

//                    finish();
                }

                @Override
                public void onFingerprintSuccessful() {

                }


                @Override
                public void onPinLoginFailed() {
                    Toast.makeText(getContext(), "Pin salah",
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFingerprintLoginFailed() {

                }
            };
    private void updateSaldo(double saldo, String key, String stats) {
        try {
            FirebaseDatabase.getInstance().getReference("users").child(key).child("saldo").setValue(saldo);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'_'HH:mm:ss");
            System.out.println(formatter.format(calendar.getTime()));
            DatabaseReference mDatabase;
            History history = new History(formatter.format(calendar.getTime()), "+ Rp. "+String.valueOf(total), "Beli Pulsa");
            mDatabase = FirebaseDatabase.getInstance().getReference("history");
            mDatabase.child(key).child(formatter.format(calendar.getTime())).setValue(history);
            Toast.makeText(getContext(), "Transfer Berhasil", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialog(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        // set title dialog
        alertDialogBuilder.setTitle(null);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Transfer Sukses!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        PulsaActivity.fa.finish();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.dismiss();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void sendNotification(final String recieverUid, final String name, final String message){
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = allTokens.orderByKey().equalTo(recieverUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(myUid, message, "KWH_Wallet Transfer", recieverUid, R.drawable.kwh_wallet_logo);
                    Sender sender = new Sender(data, token.getToken());

                    //fcm json
                    try{
                        JSONObject senderJsonObject = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest request = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("JSON_RESPONSE", "onResponse: "+response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: "+error.toString());
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers =  new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAA_Gth1k4:APA91bEoQj3rrz3QtJvMBxUVGF2qYHCuQFwtzmyCh1p4Vdfv9xWwXZ0J9sXTzhbYzMWbAPiRIODW5YhmRvvUPSRu2UyQ0FOgO77lSXCoBEHeZ4_R6NgjT4nRC0uFqz-KonKQd4lIDb3o");

                                return super.getHeaders();
                            }
                        };
                        requestQueue.add(request);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
