package com.example.werk;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werk.model.AvailableJobs;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    ArrayList<AvailableJobs> arrayList = new ArrayList<AvailableJobs>();
    Fragment c;
    BottomNavigationActivity bottomNavigationActivity;
    View view;

    public RecyclerAdapter(ArrayList<AvailableJobs> arrayList, BottomNavigationActivity bottomNav, Fragment c) {
        this.arrayList = arrayList;
        this.bottomNavigationActivity = bottomNav;
        this.c = c;

    }

    RecyclerAdapter(ArrayList<AvailableJobs> arrayList)
    {
        this.arrayList = arrayList;
    }

    //CREATED BY STANLEY - 2201744802

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_avail_jobs, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {

        //set text menjadi text yang diambil dari database
        final AvailableJobs availableJobs = arrayList.get(position);
        holder.tvJobTitle.setText(availableJobs.getJobTitle());
        holder.tvCompanyName.setText(availableJobs.getCompanyName());
        holder.tvCompanyAdress.setText(availableJobs.getCompanyAddress());
        holder.tvJobSalary.setText("Rp"+String.valueOf(availableJobs.getSalaryLower())+" - Rp"+String.valueOf(availableJobs.getSalaryUpper()));
        holder.tvApplicantCount.setText(String.valueOf(availableJobs.getApplicantCount()) + " Applicant"); //Integer.toString(availableJobs.getApplicantCount()));

        //funtion jalan jika ada job diklik dan menampilkan page jobdetail
        holder.conJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int jobVacancyId = availableJobs.getJobVacancyId();

                Bundle bundle2 = new Bundle();
                bundle2.putInt("jobId", jobVacancyId);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new JobDetailFragment();
                myFragment.setArguments(bundle2);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();


            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvJobTitle, tvCompanyAdress, tvCompanyName, tvJobSalary, tvApplicantCount;
        LinearLayout conJobList;

        RecyclerViewHolder(View view)
        {
            super(view);

            //inisialisasi
            tvJobTitle = (TextView)view.findViewById(R.id.job_title);
            tvJobSalary = (TextView)view.findViewById(R.id.job_salary);
            tvCompanyName = (TextView)view.findViewById(R.id.company_name);
            tvCompanyAdress = (TextView)view.findViewById(R.id.company_address);
            tvApplicantCount = (TextView) view.findViewById(R.id.applicant_count);
            conJobList = (LinearLayout) view.findViewById(R.id.con_job_list);

        }


    }
}
