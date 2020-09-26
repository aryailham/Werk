package com.example.werk;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CandidatesFormBottomSheetDialog extends BottomSheetDialogFragment {

    TextView acceptButton, rejectButton;
    EditText desiredSalary, pitch;
    Integer desiredSalaryValue;
    String pitchValue;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        final DatabaseAccess databaseAccess;
        databaseAccess = DatabaseAccess.getInstance(getContext());

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.candidates_form, null);
        dialog.setContentView(view);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        desiredSalary = (EditText) view.findViewById(R.id.desired_salary);
        pitch = (EditText) view.findViewById(R.id.pitch);

        acceptButton = (TextView) view.findViewById(R.id.accept_btn);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess.open();
//                databaseAccess.applyJob(1, 2, desiredSalaryValue, pitchValue);
                databaseAccess.close();
                dismiss();
            }
        });
        rejectButton = (TextView) view.findViewById(R.id.reject_btn);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseAccess.open();
//              databaseAccess.respondCandidates();
                databaseAccess.close();
                dismiss();
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
