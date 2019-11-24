package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kwh_wallet.R;

public class TransferFragment extends Fragment {
    TextView price;
    View v;
    public EditText value;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.transfer_fragment, container, false);
        v = view;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                double saldo = bundle.getDouble("saldo");
                TextView price = view.findViewById(R.id.saldo);
                price.setText(String.format("%,.0f", saldo));
            }
        },2500);

        return view;
    }

    public void getSaldo(){
        value = v.findViewById(R.id.value);
        System.out.println(value.toString());
    }
}
