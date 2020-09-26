package com.example.werk;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.werk.model.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ApplicationFormBottomSheetDialog extends BottomSheetDialogFragment {
//    private BottomSheetListener mListener;


    //@Nullable
    //@Override
    //public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //    View v = inflater.inflate(R.layout.application_form, container, false);

//        desiredSalary = (EditText) v.findViewById(R.id.desired_salary);
//        pitch = (EditText) v.findViewById(R.id.pitch);
//        submitButton = (Button) v.findViewById(R.id.submit_btn);

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isValid = false;
//                try{
//                    Integer desiredSalaryValue = Integer.parseInt(desiredSalary.getText().toString());
//                    isValid = true;
//                }catch(Exception e){
//                    isValid = false;
//                }
//
//                if(isValid) {
//                    mListener.onButtonClicked(Integer.parseInt(desiredSalary.getText().toString()), pitch.getText().toString());
//                    dismiss();
//                }else{
//                    Toast.makeText(getActivity(), "Please enter a valid value", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



     //   return v;
     //}
    TextView submitButton;
    EditText desiredSalary, pitch;
    Integer desiredSalaryValue;
    String pitchValue;
    Bundle bundle;
    int jobId ;


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        final DatabaseAccess db;
        db = DatabaseAccess.getInstance(getContext());

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.application_form, null);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        desiredSalary = (EditText) view.findViewById(R.id.desired_salary);
        pitch = (EditText) view.findViewById(R.id.pitch);
        submitButton = (TextView) view.findViewById(R.id.submit_btn);
        bundle = getArguments();
        if(bundle != null)
        {
            jobId = bundle.getInt("jobId");
            //Toast.makeText(getActivity(), "id = " + jobId, Toast.LENGTH_SHORT).show();
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regex = "[0-9]+";
                boolean isValid = false;

                SharedPref SP = new SharedPref(getActivity());
                User user = SP.load();

                String email = user.getEmail();

                String desiredSalaryText = desiredSalary.getText().toString();
                pitchValue = pitch.getText().toString();

                if(desiredSalaryText.matches(regex)){
                    isValid = true;
                }

                if(isValid) {
                    db.open();
                    int id = db.checkIdJobSeeker(email);
                    desiredSalaryValue = Integer.parseInt(desiredSalaryText);
                    db.applyJob(id, jobId , desiredSalaryValue, pitchValue);
                    db.close();
                    dismiss();
                }else{
                    if(desiredSalaryText.isEmpty()){
                        Toast.makeText(getContext(), "Please input your desired salary", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "The input can only be numeric", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        if (behavior != null && behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//                @Override
//                public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//                }
//
//                @Override
//                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                }
//            });
//        }
    }
}
