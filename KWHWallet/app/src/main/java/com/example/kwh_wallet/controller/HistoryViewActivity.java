package com.example.kwh_wallet.controller;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryViewActivity extends Activity {
    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<History> listHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_view);
        initDataset();

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view_history);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HistoryAdapter(listHistory);
        System.out.println("=============================ada===========================");
        recyclerView.setAdapter(adapter);
    }

    private void initDataset(){
        listHistory = new ArrayList<History>(Arrays.asList(
                new History("15 Oktober 2019", "400000"),
                new History("13 Oktober 2019", "200000"),
                new History("14 Oktober 2019", "200000")
        ));
    }
}
