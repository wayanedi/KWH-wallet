package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kwh_wallet.MainActivity;
import com.example.kwh_wallet.R;

public class HomeFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    Button pln;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Button transfer = (Button) view.findViewById(R.id.transfer);
        transfer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent trans=new Intent(v.getContext(), TransferActivity.class);
                startActivity(trans);
            }
        });

        Button kwh_id = (Button) view.findViewById(R.id.KWH_id);
        kwh_id.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent qrId=new Intent(v.getContext(), QRCodeActivity.class);
                startActivity(qrId);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        pln =  view.findViewById(R.id.pln);
        pln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PLN_payment.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
