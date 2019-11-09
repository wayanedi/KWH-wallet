package com.example.kwh_wallet.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView ;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<History> listHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_view, container, false);
        initDataset();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_history);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HistoryAdapter(listHistory);
        System.out.println("=============================ada===========================");
        recyclerView.setAdapter(adapter);
        return view;

    }

    private void initDataset(){
        listHistory = new ArrayList<History>(Arrays.asList(
                new History("15 Oktober 2019", "400000"),
                new History("13 Oktober 2019", "200000"),
                new History("14 Oktober 2019", "200000")
        ));
    }
}
