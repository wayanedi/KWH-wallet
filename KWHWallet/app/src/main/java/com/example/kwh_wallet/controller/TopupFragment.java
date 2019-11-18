package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kwh_wallet.R;

public class TopupFragment extends Fragment {

    TextView price;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.topup_fragment, container, false);

        Bundle bundle = this.getArguments();
        double saldo = bundle.getDouble("saldo");

        price = view.findViewById(R.id.price);
        price.setText(Double.toString(saldo));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
