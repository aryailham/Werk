package com.example.werk;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.werk.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //deklarasi variable global
    ImageView profilePicture;
    Button btnUpdateProfile;
    TextView email, phoneNumber, linkedInUrl, address, name, gender, dateOfBirth, tvLogout;
    String oldEmail;
    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.seeker_profile, container, false);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.seeker_profile);

        //inisialisasi
        profilePicture = (ImageView) view.findViewById(R.id.iv_profile_picture);
        profilePicture.setImageResource(R.drawable.ic_person_black_24dp);
        btnUpdateProfile = (Button) view.findViewById(R.id.btn_edit_profile);
        email = (TextView) view.findViewById(R.id.tv_email);
        phoneNumber = (TextView) view.findViewById(R.id.tv_phone_number);
        linkedInUrl = (TextView) view.findViewById(R.id.tv_linkedin);
        address = (TextView) view.findViewById(R.id.tv_address);
        name = (TextView) view.findViewById(R.id.tv_name);
        dateOfBirth = (TextView) view.findViewById(R.id.tv_dob);
        gender = (TextView) view.findViewById(R.id.tv_gender);
        tvLogout = (TextView) view.findViewById(R.id.tv_logout);

        //membuat object dari SharedPref dan melakukan load pada data yang ada
        SharedPref SP = new SharedPref(getActivity());
        User user = SP.load();

        //memasukkan data lama ke dalam kolom input profile
        oldEmail = user.getEmail();
        setUserData(oldEmail);

        //jika tombol update profile ditekan, maka akan masuk ke
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(),"update profile",Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(getActivity(), EditSeekerProfile.class);
                getActivity().startActivity(mainIntent);
//                getActivity().finish();
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref SP = new SharedPref(getActivity());
                SP.clearAll(getActivity());

                Intent mainIntent = new Intent(getActivity(), SeekerLogin.class);
                getActivity().startActivity(mainIntent);
                getActivity().finish();
            }
        });


        return view;
    }

    //function untuk menampilkan data user kedalam activity
    private void setUserData(String oldEmail){
        email = (TextView) view.findViewById(R.id.tv_email);
        phoneNumber = (TextView) view.findViewById(R.id.tv_phone_number);
        linkedInUrl = (TextView) view.findViewById(R.id.tv_linkedin);
        address = (TextView) view.findViewById(R.id.tv_address);
        name = (TextView) view.findViewById(R.id.tv_name);
        dateOfBirth = (TextView) view.findViewById(R.id.tv_dob);
        gender = (TextView) view.findViewById(R.id.tv_gender);

        //mengambil data profile user dan meletakkannya di halaman edit profile
        DatabaseAccess db = DatabaseAccess.getInstance(getActivity().getApplicationContext());
        db.open();

        //mengambil data user di database dan memasukkannya ke cursor
        Cursor cursor = db.getUserData(oldEmail);

        //mengecek apakah cursor kosong atau tidak
        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            //memasukkan data tersebut kedalam string
            String oldPhoneNo = cursor.getString(5);
            String oldAddress = cursor.getString(4);
            String oldLinkedIn = cursor.getString(8);
            String oldName = cursor.getString(1);
            String oldDob = cursor.getString(3);
            String oldGender = cursor.getString(2);

            email.setText(oldEmail);
            phoneNumber.setText(oldPhoneNo);
            address.setText(oldAddress);
            linkedInUrl.setText(oldLinkedIn);
            name.setText(oldName);
            gender.setText(oldGender);
            dateOfBirth.setText(oldDob);
        }
        db.close();
    }

    //jika tombol logout ditekan, maka user akan logout dan akan diarahkan ke halaman login
//    public void onLogoutClicked(View v){
//        SharedPref SP = new SharedPref(getActivity());
//        SP.clearAll(getActivity());
//
//        Intent mainIntent = new Intent(getActivity(), SeekerLogin.class);
//        getActivity().startActivity(mainIntent);
//        getActivity().finish();
//    }

}
