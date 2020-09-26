package com.example.werk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.werk.model.User;

public class SeekerProfile extends AppCompatActivity {

    //deklarasi variable global
    ImageView profilePicture;
    Button btnUpdateProfile;
    TextView email, phoneNumber, linkedInUrl, address, name, gender, dateOfBirth;
    String oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_profile);

        //inisialisasi
        profilePicture = (ImageView) findViewById(R.id.iv_profile_picture);
        profilePicture.setImageResource(R.drawable.ic_person_black_24dp);
        btnUpdateProfile = (Button) findViewById(R.id.btn_edit_profile);
        email = (TextView) findViewById(R.id.tv_email);
        phoneNumber = (TextView) findViewById(R.id.tv_phone_number);
        linkedInUrl = (TextView) findViewById(R.id.tv_linkedin);
        address = (TextView) findViewById(R.id.tv_address);
        name = (TextView) findViewById(R.id.tv_name);
        dateOfBirth = (TextView) findViewById(R.id.tv_dob);
        gender = (TextView) findViewById(R.id.tv_gender);

        //membuat object dari SharedPref dan melakukan load pada data yang ada
        SharedPref SP = new SharedPref(SeekerProfile.this);
        User user = SP.load();

        //memasukkan data lama ke dalam kolom input profile
        oldEmail = user.getEmail();
        setUserData(oldEmail);

        //jika tombol update profile ditekan, maka akan masuk ke
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"update profile",Toast.LENGTH_SHORT).show();

                Intent mainIntent = new Intent(SeekerProfile.this, EditSeekerProfile.class);
                SeekerProfile.this.startActivity(mainIntent);
                SeekerProfile.this.finish();
            }
        });

    }

    //function untuk menampilkan data user kedalam activity
    private void setUserData(String oldEmail){
        email = (TextView) findViewById(R.id.tv_email);
        phoneNumber = (TextView) findViewById(R.id.tv_phone_number);
        linkedInUrl = (TextView) findViewById(R.id.tv_linkedin);
        address = (TextView) findViewById(R.id.tv_address);
        name = (TextView) findViewById(R.id.tv_name);
        dateOfBirth = (TextView) findViewById(R.id.tv_dob);
        gender = (TextView) findViewById(R.id.tv_gender);

        //mengambil data profile user dan meletakkannya di halaman edit profile
        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
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
    public void onLogoutClicked(View v){
        SharedPref SP = new SharedPref(SeekerProfile.this);
        SP.clearAll(SeekerProfile.this);

        Intent mainIntent = new Intent(SeekerProfile.this, SeekerLogin.class);
        SeekerProfile.this.startActivity(mainIntent);
        SeekerProfile.this.finish();
    }

}
