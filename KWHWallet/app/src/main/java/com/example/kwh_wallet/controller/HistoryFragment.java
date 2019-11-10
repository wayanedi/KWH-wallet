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
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<History> listHistory=new ArrayList<History> ();

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

            adapter = new HistoryAdapter(listHistory);
            System.out.println("=============================ada===========================");
            recyclerView.setAdapter(adapter);
            }
        },1500);
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

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    System.out.println("value :"+ snapshot.getValue());
                    String str = snapshot.getValue().toString();
                    String newStr=str.replace("{","");
                    String newStr2=newStr.replace("}","");
//                    System.out.println(newStr2);
                    String[] arrOfStr = newStr2.split("jumlah=");
                    String s="";
                    for (int i=0;i<arrOfStr.length;i++){
                        s+=arrOfStr[i];
                    }
                    String[] fixedStr = s.split(", deskripsi=");
                    System.out.println(fixedStr[1]);
                    System.out.println(fixedStr[0]);
                    System.out.println("key: "+ snapshot.getKey());
                    String[] tgl = snapshot.getKey().split("_");
                    String pattern = "dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


                    String sTgl="";
//                    for (int i=0;i<tgl.length;i++){
                        sTgl+=tgl[0];
//                    }
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(sTgl);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
//                    System.out.println("ini tanggal"+date);
//                    System.out.println(date.toString().substring(0,10));
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(date);
//                    System.out.println("tgl skrng" + cal);
                    History history = new History(date.toString().substring(0,10),fixedStr[0],fixedStr[1]);
                    listHistory.add(history);
                }
            }else{
                System.out.println("tidak ada");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
