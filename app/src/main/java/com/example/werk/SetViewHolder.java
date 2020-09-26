package com.example.werk;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SetViewHolder extends RecyclerView.ViewHolder {

    public TextView tvJobTitle;
    public TextView tvCompany;
    public TextView tvDate;
    public TextView tvStatus;

    public SetViewHolder(View itemView){

        super(itemView);
        tvJobTitle = itemView.findViewById(R.id.tv_job_title);
        tvCompany = itemView.findViewById(R.id.tv_company);
        tvDate = itemView.findViewById(R.id.tv_date);
        tvStatus = itemView.findViewById(R.id.tv_apply_status);

    }

}
