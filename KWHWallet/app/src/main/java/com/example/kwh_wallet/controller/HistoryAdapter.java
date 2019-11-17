package com.example.kwh_wallet.controller;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> historyList = new ArrayList<History>();
    public HistoryAdapter(List<History> inputData){
        this.historyList=inputData;
        System.out.println("==========================================" + historyList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_view_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        System.out.println(historyList.size());
        return vh;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tanggal.setText(historyList.get(position).getTanggal());
        System.out.println("=========================================================");
        System.out.println(historyList.get(position).getTanggal());
        holder.total.setText(historyList.get(position).getJumlah());
        System.out.println(historyList.get(position).getTanggal());
        holder.status.setText(historyList.get(position).getDeskripsi());
        if(historyList.get(position).getJumlah().toString().substring(0,1).equals("-"))
            holder.total.setTextColor(R.color.redText);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tanggal;
        public TextView total;
        public TextView status;
        public ViewHolder(View v) {
            super(v);
            tanggal = v.findViewById(R.id.tanggal);
            total = v.findViewById(R.id.total2);
            status = v.findViewById(R.id.status);
        }
    }
}
