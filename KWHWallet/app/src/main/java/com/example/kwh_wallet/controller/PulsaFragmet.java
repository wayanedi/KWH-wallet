package com.example.kwh_wallet.controller;


import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kwh_wallet.R;

import java.util.ArrayList;
import java.util.List;

public class PulsaFragmet extends Fragment {

    private TextView sisaSaldo;
    private Spinner nominal;
    private List<String> nominalPembayaran = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pulsa_fragment, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                double saldo = bundle.getDouble("saldo");
                System.out.println("saldo di plm: " + saldo);

                sisaSaldo = view.findViewById(R.id.sisaSaldoLabel);
                DecimalFormat df = new DecimalFormat("#");
                sisaSaldo.setText("Sisa Saldo Anda Rp. "+String.format("%,.0f", saldo));
                sisaSaldo.setVisibility(View.VISIBLE);
                sisaSaldo.bringToFront();

                nominalPembayaran.add("Pilih Nominal");
                nominalPembayaran.add("Rp 20.000");
                nominalPembayaran.add("Rp 50.000");
                nominalPembayaran.add("Rp 100.000");
                nominalPembayaran.add("Rp 200.000");
                nominalPembayaran.add("Rp 500.000");
                nominalPembayaran.add("Rp 1.000.000");
                nominalPembayaran.add("Rp 2.000.000");

                ArrayAdapter<String> nominalAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, nominalPembayaran);
                nominal = view.findViewById(R.id.nominal);
                nominal.setAdapter(nominalAdapter);
            }
        },1000);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
