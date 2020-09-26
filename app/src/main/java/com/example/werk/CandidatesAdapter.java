package com.example.werk;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.werk.model.Candidates;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CandidatesAdapter extends RecyclerView.Adapter<CandidatesAdapter.RecyclerViewHolder> {
    public List<Candidates> candidatesList = new ArrayList<>();
    public Fragment fragment;
    Candidates candidate;

    public CandidatesAdapter(Fragment fragment, List<Candidates> candidatesList)
    {
        this.fragment = fragment;
        this.candidatesList = candidatesList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_candidates, parent, false);

        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        candidate = candidatesList.get(position);

        holder.tvName.setText(candidate.getName());
        holder.tvEmail.setText(candidate.getEmail());
        holder.tvLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(candidate.getLinkedIn());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                fragment.getActivity().startActivity(intent);
            }
        });
        NumberFormat format = NumberFormat.getInstance();
        holder.tvDesiredSalary.setText(String.valueOf(format.format(candidate.getDesiredSalary())).replaceAll(",", "."));
        holder.tvPitch.setText(candidate.getPitch());
    }

    @Override
    public int getItemCount() {
        return candidatesList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvEmail, tvDesiredSalary, tvPitch;
        ImageView tvLinkedIn;
        RecyclerViewHolder(View view)
        {
            super(view);
            tvName = (TextView) view.findViewById(R.id.candidates_name);
            tvEmail = (TextView) view.findViewById(R.id.candidates_email);
            tvLinkedIn = (ImageView) view.findViewById(R.id.linkedin_profile);
            tvDesiredSalary = (TextView) view.findViewById(R.id.desired_salary);
            tvPitch= (TextView) view.findViewById(R.id.pitch);
        }
    }
}
