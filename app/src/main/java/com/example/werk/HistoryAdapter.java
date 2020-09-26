package com.example.werk;

import android.app.Activity;
import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werk.HistoryItem;
import com.example.werk.SetViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<SetViewHolder> {

    private Activity activity;
    ArrayList<HistoryItem> historyItems = new ArrayList<HistoryItem>();

    public HistoryAdapter (Activity activity, ArrayList<HistoryItem> historyItems) {
        this.activity = activity;
        this.historyItems = historyItems;
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history_job,parent,false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        holder.tvJobTitle.setText(historyItems.get(position).getJobTitle());
        holder.tvCompany.setText(historyItems.get(position).getNamaPerusahahaan());
        holder.tvDate.setText(historyItems.get(position).getApplyDate());
        holder.tvStatus.setText(historyItems.get(position).getStatus());

        if(historyItems.get(position).getStatus().equalsIgnoreCase("Accepted")){
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_accept);
        }
        else if(historyItems.get(position).getStatus().equalsIgnoreCase("Rejected")){
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_reject);
        }
        else{
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_waiting);
        }
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
