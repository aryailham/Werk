package com.example.werk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.werk.model.AvailableJobs;
import com.example.werk.model.Job;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ProviderDetailFragment extends Fragment{

    Button viewCandidatesButton;
    TextView jobTitle, companyName, jobSalaryRange, jobCategory, jobEmploymentType, jobLocation;
    TextView jobDesc, jobQualifications, jobEndPeriod;//, jobStartPeriod;
    LinearLayout jobDescBox, jobQualificationBox;
    DatabaseAccess databaseAccess;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter listAdapter;
    ArrayList<AvailableJobs> arrayList = new ArrayList<>();
    String[] benefitList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.job_detail_fragment, container, false);
//        Bundle bundle = getArguments();
//        String cat = bundle.getString("category");
//        String jobTitleValue = bundle.getString("jobTitle");
//        String companyNameValue = bundle.getString("companyName");

        databaseAccess = DatabaseAccess.getInstance(getContext());
        viewCandidatesButton = root.findViewById(R.id.view_candidates_btn);
        viewCandidatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationFormBottomSheetDialog applicationForm = new ApplicationFormBottomSheetDialog();
                applicationForm.show(getActivity().getSupportFragmentManager(), "applicationForm");
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
        jobTitle.setText("UX Researcher");
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

        Bundle bundle = new Bundle();
        bundle.putString("category", jobCategoryValue);

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
        databaseAccess.open();
        databaseAccess.applyJob(1, 2, desiredSalary, pitch);
        databaseAccess.close();
    }

    private void initJobsByCategory(String category){
        databaseAccess = DatabaseAccess.getInstance(getContext());
        databaseAccess.open();
        databaseAccess.getJobsByCategory(category);
        databaseAccess.close();
    }

    private void initRecyclerView(View root){
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
