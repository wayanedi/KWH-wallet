package com.example.kwh_wallet.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwh_wallet.R;
import com.example.kwh_wallet.model.User;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<User> historyList = new ArrayList<User>();
    public static final String KEY_USERNAME="USERNAME";
    public static final String KEY_EMAIL="EMAIL";

    public ListAdapter(ArrayList<User> historyList){
        this.historyList = historyList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        System.out.println(historyList.size());
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final String name = historyList.get(position).getUsername();
        final String email =historyList.get(position).getEmail();


        holder.email.setText(email);
        holder.name.setText(name);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(email);

                final Intent mIntent = new Intent(view.getContext(), TransferFromScannerActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString(KEY_EMAIL, email);
                bundle.putString(KEY_USERNAME, name);

                mIntent.putExtras(bundle);
                view.getContext().startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView email;

        public ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.name);
            email = v.findViewById(R.id.email);
        }
    }
}
