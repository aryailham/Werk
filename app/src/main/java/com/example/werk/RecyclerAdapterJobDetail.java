package com.example.werk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.werk.model.AvailableJobs;

import java.text.NumberFormat;
import java.util.ArrayList;

public class RecyclerAdapterJobDetail extends RecyclerView.Adapter<RecyclerAdapterJobDetail.RecyclerViewHolder> {
    public ArrayList<AvailableJobs> arrayList = new ArrayList<AvailableJobs>();
    public Fragment fragment;
    public RecyclerAdapterJobDetail(Fragment fragment, ArrayList<AvailableJobs> arrayList)
    {
        this.fragment = fragment;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_available_jobs, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        final AvailableJobs availableJobs = arrayList.get(position);
        NumberFormat format = NumberFormat.getInstance();

        String salaryLower = String.valueOf(format.format(availableJobs.getSalaryLower())).replaceAll(",", ".");
        String salaryUpper = String.valueOf(format.format(availableJobs.getSalaryUpper())).replaceAll(",",".");
        holder.tvJobTitle.setText(availableJobs.getJobTitle());
        holder.tvCompanyName.setText(availableJobs.getCompanyName());
        holder.tvCompanyAddress.setText(availableJobs.getCompanyAddress());
        holder.tvJobSalary.setText("Rp. " + salaryLower + " - " + salaryUpper);

//        String jobTitle = holder.tvJobTitle.getText().toString();
//        String companyName = holder.tvJobTitle.getText().toString();
//        String companyAddress = holder.tvJobTitle.getText().toString();
//        String jobSalary = holder.tvJobTitle.getText().toString();
        holder.tvContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("jobTitle", availableJobs.getJobTitle());
                bundle.putString("companyName", availableJobs.getCompanyName());
                bundle.putString("category", availableJobs.getJobCategory());

                JobDetailFragment frag = new JobDetailFragment();
                FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout, frag);
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvJobTitle, tvCompanyAddress, tvCompanyName, tvJobSalary, tvCategory;
        GridLayout tvContainer;
        RecyclerViewHolder(View view)
        {
            super(view);
            tvJobTitle = (TextView)view.findViewById(R.id.job_title);
            tvJobSalary = (TextView)view.findViewById(R.id.job_salary);
            tvCompanyName = (TextView)view.findViewById(R.id.company_name);
            tvCompanyAddress = (TextView)view.findViewById(R.id.company_address);
            tvCategory = (TextView)view.findViewById(R.id.job_category);
            tvContainer = (GridLayout)view.findViewById(R.id.rv_container);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//
//                    if(pos != RecyclerView.NO_POSITION){
//                        AvailableJobs clickedJob = arrayList.get(pos);
//                    }
//                }
//            });
        }
    }
}
