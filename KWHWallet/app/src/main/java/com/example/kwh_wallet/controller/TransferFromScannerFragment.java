package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kwh_wallet.R;

public class TransferFromScannerFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.transfer_from_scanner_fragment, container, false);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                Bundle bundle = getArguments();
                double saldo = bundle.getDouble("saldo");
                String user = bundle.getString("username");
                TextView price = view.findViewById(R.id.saldo);
                price.setText(String.format("%,.0f", saldo));

                TextView usr = view.findViewById(R.id.saldo);
                usr = view.findViewById(R.id.username);
                usr.setText("Trasfer ke: " + user);
//            }
//        },2500);

        return view;
    }
}
