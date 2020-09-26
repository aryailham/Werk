package com.example.werk;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.ListAdapter;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.werk.model.AvailableJobs;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableJobsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter listAdapter;
    ArrayList<AvailableJobs> arrayList = new ArrayList<>();
    EditText etSearch;
    ImageView btnBack;


    public AvailableJobsFragment() {
        // Required empty public constructor
    }

    //CREATED BY STANLEY 2201744802

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_available_jobs, container, false);
        final Bundle bundle = getArguments();
        String cat = bundle.getString("category");
        TextView tvAvailJobsCat = (TextView) view.findViewById(R.id.avail_jobs_category);
        etSearch  = view.findViewById(R.id.et_search);
        btnBack = view.findViewById(R.id.back_btn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);

                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.container, homeFragment);
                fragmentTransaction.commit();
            }
        });

        DatabaseAccess db = DatabaseAccess.getInstance(getActivity().getApplicationContext());
        db.open();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_avail_jobs);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //untuk mengisi array list sesuai dengan datata yang diminta
        if(cat !=  null) // && cat.equals("all"))
        {
            if (cat.equals("all"))
            {
                //jika yang diklik view all jobs
                arrayList = (ArrayList<AvailableJobs>) db.getJobsByPopular();
            }
            else
            {

                //view by category
                tvAvailJobsCat.setText("Available " + cat + " Jobs");
                arrayList = (ArrayList<AvailableJobs>) db.getJobsByCategory(cat);
            }
            //arrayList = (ArrayList<AvailableJobs>) db.getJobsByPopular();
        }
        else
        {
            //view sesuai yang di search
            String search = bundle.getString("search");
            arrayList = (ArrayList<AvailableJobs>) db.searchJobs(search);
        }

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment currentFragments = fragmentManager.findFragmentById(R.id.frame_layout);
        listAdapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(listAdapter);

        db.close();

        //function untuk search jobs
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String searchInput = etSearch.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("search", searchInput);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    AvailableJobsFragment availableJobsFragment = new AvailableJobsFragment();
                    availableJobsFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.container, availableJobsFragment);
                    fragmentTransaction.commit();

                    handled = true;
                }
                return handled;
            }
        });

        return view;
    }

}
