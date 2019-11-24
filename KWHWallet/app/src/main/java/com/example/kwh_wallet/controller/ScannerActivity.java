package com.example.kwh_wallet.controller;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, PermissionCallback, ErrorCallback {
    private ZXingScannerView mScannerView;
    String username="";
    double saldo;
    String email;
    String pin;
    private static final int REQUEST_PERMISSIONS=20;
    public static final String KEY_USERNAME="USERNAME";
    public static final String KEY_EMAIL="EMAIL";
    public static final String KEY_SALDO="SALDO";
    public static final String KEY_UID="UID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reqPermission();
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        Log.v("TAG", rawResult.getText()); // Prints scan results
        Log.v("TAG", rawResult.getBarcodeFormat().toString());
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("Please Wait . . .");

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        final AlertDialog alert1 = builder.create();
        alert1.show();
        onPause();
        System.out.println("rawResult" + rawResult.getText());

        try {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(rawResult.getText()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count = 0;
                for(DataSnapshot datas: dataSnapshot.getChildren()) {

                    if (count == 0)
                        email = datas.getValue().toString();

                    if (count == 1) {
                        pin = datas.getValue().toString();
                    }

                    if (count == 2) {
                        saldo = Double.parseDouble(datas.getValue().toString());
                    }

                    if (count == 3)
                        username = datas.getValue().toString();
                    count++;
                    System.out.println(count);
                    System.out.println(datas.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

            final Intent mIntent = new Intent(this, TransferFromScannerActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!username.equals("")){
                        User usr=new User(username,email,pin);
                        usr.setSaldo(saldo);
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_EMAIL, usr.getEmail());
                        bundle.putString(KEY_USERNAME, usr.getUsername());
                        bundle.putString(KEY_UID, rawResult.getText());
                        bundle.putDouble(KEY_SALDO, usr.getSaldo());
                        User user = new User(username, email);
                        FirebaseDatabase.getInstance().getReference("list").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                        mIntent.putExtras(bundle);
                        System.out.println(usr.getEmail());
                        System.out.println(usr.getUsername());
                        System.out.println(usr.getSaldo());
                        startActivity(mIntent);
                        finish();
                    }else{
                        alert1.hide();
                        AlertDialog alert2 = builder2.create();
                        builder2.setTitle("Message");
                        builder2.setMessage("QR code not match, please try again...");
                        alert2=builder2.create();
                        alert2.show();
                        onResume();
                    }
                }
            },3000);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("====================================================");
            alert1.hide();
            AlertDialog alert2 = builder2.create();
            builder2.setTitle("Message");
            builder2.setMessage("QR code not match, please try again...");
            alert2=builder2.create();
            alert2.show();
            onResume();
        }
        mScannerView.resumeCameraPreview(this);

    }

    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions(
                Manifest.permission.CAMERA)
                .setCallback(this)
                .setErrorCallback((ErrorCallback) this)
                .request(REQUEST_PERMISSIONS);
    }

    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("cancel", null);
        builder.show();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permissions Denied.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("We need permissions for this app.");
            builder.setPositiveButton("oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    permissionInterface.onDialogShown();
                }
            });
            builder.setNegativeButton("cancel", null);
            builder.show();
    }
}
