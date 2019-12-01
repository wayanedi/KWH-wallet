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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;
import com.example.kwh_wallet.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<History> listHistory=new ArrayList<History> ();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.history_view, container, false);
        initDataset();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_history);
            recyclerView.setHasFixedSize(true);

            layoutManager = new LinearLayoutManager(view.getContext());
            recyclerView.setLayoutManager(layoutManager);
            Collections.sort(listHistory);
            Collections.reverse(listHistory);
            adapter = new HistoryAdapter(listHistory);
            System.out.println("=============================ada===========================");
            recyclerView.setAdapter(adapter);
            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                view.findViewById(R.id.recycle_view_history).bringToFront();
            }
        },1500);

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listHistory.clear();
                initDataset();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Collections.sort(listHistory);
                        Collections.reverse(listHistory);;
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view;

    }

    private void initDataset(){

    getData();
        System.out.println(listHistory.size());
    }

    private void getData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("history")
                .child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener2);
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                History test;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    test = snapshot.getValue(History.class);
                    System.out.println("HISTORY========================================================");
                    System.out.println(test.getDeskripsi());
                    System.out.println(test.getJumlah());
                    System.out.println(test.getTanggal());
                    String[] s = test.getTanggal().split("-");
                    String date = s[0];
                    String month = convertBulan(s[1]);
                    String year = s[2].substring(0,4);
                    String dFinal = date + " " + month + " " + year;
//                    System.out.println(dFinal);
                    test.setTanggal(dFinal);
                    listHistory.add(test);
                }
            }else{
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public String convertBulan(String s){
        if(s.equals("1")){
            return "Januari";
        }else if(s.equals("2")){
            return "Febuari";
        }else if(s.equals("3")){
            return "Maret";
        }else if(s.equals("4")){
            return "April";
        }else if(s.equals("5")){
            return "Mei";
        }else if(s.equals("6")){
            return "Juni";
        }else if(s.equals("7")){
            return "Juli";
        }else if(s.equals("8")){
            return "Agustus";
        }else if(s.equals("9")){
            return "September";
        }else if(s.equals("10")){
            return "Oktober";
        }else if(s.equals("11")){
            return "November";
        }else
            return "Desember";
    }
}
