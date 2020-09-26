package com.example.werk;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.werk.model.AvailableJobs;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    GridView gridView;
    TextView tvShowAll;
    EditText etSearch;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter listAdapter;
    ArrayList<AvailableJobs> arrayList = new ArrayList<>();

    //data tampilan category menu
    String[] catTitle = {"Software Development", "UI / UX Design", "Computer & IT", "Web Development", "Graphic Design", "Marketing", "Accounting", "Engineering"};
    int[] catImage = {R.drawable.ic_software_development, R.drawable.ic_uiux_design, R.drawable.ic_computer, R.drawable.ic_web_development, R.drawable.ic_graphicdesign, R.drawable.ic_marketing, R.drawable.ic_accounting, R.drawable.ic_engineering};

    public HomeFragment() {
        // Required empty public constructor
    }

    //CREATED BY STANLEY - 2201744802

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gridView = view.findViewById(R.id.gv_categories);
        gridView.setAdapter(new MainAdapter(getActivity(), catTitle, catImage));

        tvShowAll = view.findViewById(R.id.tv_show_all);
        etSearch  = view.findViewById(R.id.et_search);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "Category"+ catTitle[+position], Toast.LENGTH_SHORT).show();

                //untuk melempar data category apa yang diklik
                Bundle bundle = new Bundle();
                bundle.putString("category", catTitle[+position]);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //set tujuan pelemparan data
                AvailableJobsFragment availableJobsFragment = new AvailableJobsFragment();
                availableJobsFragment.setArguments(bundle);

                //pergi ke fragment tempat view data data jobs sesuai category
                fragmentTransaction.replace(R.id.container, availableJobsFragment);
                fragmentTransaction.commit();


            }
        });

        tvShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //untuk melempar data category apa yang diklik
                Bundle bundle = new Bundle();
                bundle.putString("category", "all");

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //set tujuan pelemparan data
                AvailableJobsFragment availableJobsFragment = new AvailableJobsFragment();
                availableJobsFragment.setArguments(bundle);

                //pergi ke fragment tempat view data data jobs sesuai popularity
                fragmentTransaction.replace(R.id.container, availableJobsFragment);
                fragmentTransaction.commit();
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String searchInput = etSearch.getText().toString();

                    //untuk melempar data apa yang di search
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

        DatabaseAccess db = DatabaseAccess.getInstance(getActivity().getApplicationContext());
        db.open();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_popular_jobs);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        arrayList = (ArrayList<AvailableJobs>) db.getTopJobs();

        listAdapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(listAdapter);

        db.close();


        return view;
    }


}
