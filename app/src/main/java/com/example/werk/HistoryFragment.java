package com.example.werk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.werk.HistoryItem;
import com.example.werk.model.User;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<HistoryItem> arrayList = new ArrayList<HistoryItem>();
    private HistoryAdapter adapter;
    private TextView tvStatus;
    Bundle b = getArguments();
    String email;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        //Bundle bundle = getArguments();


        DatabaseAccess db = DatabaseAccess.getInstance(getActivity().getApplicationContext());
        db.open();

//        if(b != null)
//        {
//            email = b.getString("email");
//        }

        SharedPref SP = new SharedPref(getActivity());
        User user = SP.load();

        String s = user.getEmail();

        recyclerView = view.findViewById(R.id.rv_history_job);
        recyclerView.hasFixedSize();

        arrayList = (ArrayList<HistoryItem>) db.getAppHistory(s);

        tvStatus = (TextView) view.findViewById(R.id.tv_apply_status);

//        Command by Hardy (Reason: Ada bug saat memanggil tvStatus)
//        if(db.getStatusHistory(s).equals("Accepted")){
//            System.out.println("1");
//            tvStatus.setBackgroundResource(R.drawable.bg_status_accept);
//        }
//        else if(db.getStatusHistory(s).equals("Rejected")){
//            System.out.println("0");
//            tvStatus.setBackgroundResource(R.drawable.bg_status_reject);
//        }
//        else if(db.getStatusHistory(s).equals("Waiting")){
//            System.out.println("3");
////            tvStatus.setBackgroundResource(getActivity().getResources().getColor());
//        }


        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);


        db.close();

        return view;
    }

}
