package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.ArrayList;
import java.util.Collections;

public class ListFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> listHistory=new ArrayList<User> ();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_view, container, false);

        initDataset();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_list);
                recyclerView.setHasFixedSize(true);

                layoutManager = new LinearLayoutManager(view.getContext());
                recyclerView.setLayoutManager(layoutManager);
                adapter = new ListAdapter(listHistory);
                System.out.println("=============================ada===========================");
                recyclerView.setAdapter(adapter);
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                view.findViewById(R.id.recycle_view_list).bringToFront();
            }
        },1500);

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


        return view;
    }



    private void initDataset() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("list")
                .child(firebaseUser.getUid());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                User test;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    test = snapshot.getValue(User.class);
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
}
