package com.example.werk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.werk.model.AvailableJobs;
import com.example.werk.model.Job;

import java.text.NumberFormat;
import java.util.ArrayList;

public class JobDetailFragment extends Fragment{

    public static JobDetailFragment newInstance() {
        return new JobDetailFragment();
    }

    Button applyButton;
    ImageView backButton;
    TextView showAll;
    TextView jobTitle, companyName, jobSalaryRange, jobCategory, jobEmploymentType, jobLocation;
    TextView jobDesc, jobQualifications, jobEndPeriod;//, jobStartPeriod;
    LinearLayout jobDescBox, jobQualificationBox;
    DatabaseAccess databaseAccess;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter listAdapter;
    ArrayList<AvailableJobs> arrayList = new ArrayList<>();
    String[] benefitList;
    Bundle bundle2;
    int jobId;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.job_detail_fragment, container, false);
        bundle2 = getArguments();
        if(bundle2 != null)
        {
            jobId = bundle2.getInt("jobId");
        }
//        Bundle bundle = getArguments();
//        String cat = bundle.getString("category");
//        String jobTitleValue = bundle.getString("jobTitle");
//        String companyNameValue = bundle.getString("companyName");


        databaseAccess = DatabaseAccess.getInstance(getContext());
        applyButton = root.findViewById(R.id.apply_btn);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bun = new Bundle();
                bun.putInt("jobId", jobId);
                ApplicationFormBottomSheetDialog applicationForm = new ApplicationFormBottomSheetDialog();
                applicationForm.setArguments(bun);
                applicationForm.show(getActivity().getSupportFragmentManager(), "applicationForm");
            }
        });

        backButton = root.findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Harusnya ke Fragment sebelom ini
                JobDetailFragment frag = new JobDetailFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.frame_layout, frag);
                ft.commit();
            }
        });

        jobDescBox = (LinearLayout) root.findViewById(R.id.job_desc_box);

        //How to get idJobProvider?
        //Input data dari page sebelumnya
        //Start
        jobTitle = (TextView) root.findViewById(R.id.job_title);
        companyName = (TextView) root.findViewById(R.id.company_name);
        jobSalaryRange = (TextView) root.findViewById(R.id.job_salary_range);
        jobCategory = (TextView) root.findViewById(R.id.job_category);
        jobEmploymentType = (TextView) root.findViewById(R.id.job_employment_type);
        jobLocation = (TextView) root.findViewById(R.id.job_location);

        jobDesc = (TextView) root.findViewById(R.id.job_desc);
        jobQualifications = (TextView) root.findViewById(R.id.job_qualifications);
        jobEndPeriod = (TextView) root.findViewById(R.id.job_end_period);

        databaseAccess.open();
        Job job = databaseAccess.getJobData("UX Researcher", "PT. Karya Anak Bangsa");
        databaseAccess.close();

        NumberFormat format = NumberFormat.getInstance();
        String salaryLower = String.valueOf(format.format(job.getSalaryLower())).replaceAll(",", ".");
        String salaryUpper = String.valueOf(format.format(job.getSalaryUpper())).replaceAll(",",".");
        jobTitle.setText("UX Researcher ");
        companyName.setText("PT. Karya Anak Bangsa");
        jobSalaryRange.setText("Rp. " + salaryLower + " - " + salaryUpper);
        jobCategory.setText(job.getJobCategory());
        jobEmploymentType.setText(job.getEmployentType());
        jobLocation.setText(job.getCompanyAddress());

        jobDesc.setText(job.getJobDescription());
        jobQualifications.setText(job.getQualification());

        String endPeriodData[] = job.getEndPeriod().split("/");
        String month[] = {"January", "February", "March", "April", "May", "June",
                          "July", "August", "September", "October", "November", "December"};
        String endPeriod = "Last apply " + Integer.toString(Integer.parseInt(endPeriodData[2])) + " " + month[Integer.parseInt(endPeriodData[1])] + " " + endPeriodData[0];
        jobEndPeriod.setText(endPeriod);
        //End

        String jobCategoryValue = jobCategory.getText().toString();

//        Bundle bundle = new Bundle();
//        bundle.putString("category", jobCategoryValue);

        showAll = (TextView) root.findViewById(R.id.show_all);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("category", jobCategory.getText().toString());

                AvailableJobsFragment frag = new AvailableJobsFragment();
                frag.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.frame_layout, frag);
                ft.commit();
            }
        });
        databaseAccess.open();

        benefitList = (job.getBenefits().split(", "));
        initRecyclerViewBenefit(root);

        arrayList = (ArrayList<AvailableJobs>) databaseAccess.getFewJobsByCategory(jobCategoryValue);
        initRecyclerView(root);
        initJobsByCategory(jobCategoryValue);

        databaseAccess.close();
        return root;
    }

//    @Override
    public void onButtonClicked(int desiredSalary, String pitch) {
        Bundle bun = new Bundle();
        bun.putInt("jobId", jobId);
        databaseAccess.open();
        databaseAccess.applyJob(1, jobId, desiredSalary, pitch);
        databaseAccess.close();
    }

    private void initJobsByCategory(String category){
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();

        databaseAccess.getJobsByCategory(category);
        databaseAccess.close();
    }

    private void initRecyclerView(View root){
        Bundle bun = new Bundle();
        bun.putInt("jobId", jobId);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.rv_available_jobs);
        RecyclerAdapterJobDetail adapter = new RecyclerAdapterJobDetail(this, arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initRecyclerViewBenefit(View root){
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.rv_benefits);
        BenefitAdapter adapter = new BenefitAdapter(this, benefitList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));
    }
}
