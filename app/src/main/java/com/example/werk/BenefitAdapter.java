package com.example.werk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class BenefitAdapter extends RecyclerView.Adapter<BenefitAdapter.RecyclerViewHolder> {
    public String[] benefitList;
    public Fragment fragment;
    public BenefitAdapter(Fragment fragment, String[] benefitList)
    {
        this.fragment = fragment;
        this.benefitList = benefitList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_benefits, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String benefit = benefitList[position];

        holder.tvBenefit.setText(benefit);
    }

    @Override
    public int getItemCount() {
        return benefitList.length;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvBenefit;
        RecyclerViewHolder(View view)
        {
            super(view);
            tvBenefit = (TextView) view.findViewById(R.id.job_benefits);
        }
    }
}
