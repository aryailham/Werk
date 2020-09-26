package com.example.werk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.werk.model.User;

import java.util.Calendar;

public class EditSeekerProfile extends AppCompatActivity {

    EditText email, password, confirmPassword, phoneNumber, linkedInUrl, address, name;
    ImageView profilePicture;
    Button updateProfile;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView displayDate;
    String dateOfBirth;
    String gender = null;
    String oldEmail = null;
    DatePickerDialog.OnDateSetListener dateListener;

    //declare variable temporary, untuk menyimpan data lama sebelum diupdate
    String oldPassword;
    String oldPhoneNo;
    String oldAddress;
    String oldLinkedIn;
    String oldName;
    String oldDob;
    String oldGender;

    //declare variable untuk menyimpan data yang diinput
    String inputEmail;
    String inputPassword;
    String passwordConfirm;
    String inputPhoneNo;
    String inputAddress;
    String inputLinkedIn;
    String inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_seeker_profile);

        //inisialisasi variable
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        confirmPassword = (EditText)findViewById(R.id.et_password_confirm);
        phoneNumber = (EditText) findViewById(R.id.et_phone_number);
        linkedInUrl = (EditText) findViewById(R.id.et_linkedin_url);
        address = (EditText) findViewById(R.id.et_address);
        name = (EditText) findViewById(R.id.et_username);
        updateProfile = (Button)findViewById(R.id.btn_update);
        displayDate = (TextView) findViewById(R.id.tv_dob);
        radioGroup = (RadioGroup) findViewById(R.id.radio_gender);
        profilePicture = (ImageView) findViewById(R.id.iv_update_picture);
        profilePicture.setImageResource(R.drawable.ic_person_black_24dp);

        displayDate.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditSeekerProfile.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth , dateListener, year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });


        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;

                dateOfBirth = oldDob;
                dateOfBirth = year+ "/"+ month +"/" +day;
                displayDate.setText(dateOfBirth);
            }
        };

        //load data dari shared preferences
        SharedPref SP = new SharedPref(EditSeekerProfile.this);
        User user = SP.load();

        //memasukkan data lama ke dalam kolom input profile
        oldEmail = user.getEmail();
        setUserData(oldEmail);

        //mengedit data

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mengecek apakah field diisi atau tidak, jika tidak diisi maka dianggap data lama tidak diganti dan tetap akan menggunakan data lama
                inputEmail = email.getText().toString();
                if(inputEmail.isEmpty()) inputEmail = oldEmail;
                inputPassword = password.getText().toString();
                if(inputPassword.isEmpty()) inputPassword = oldPassword;
                passwordConfirm = confirmPassword.getText().toString();
                inputPhoneNo = phoneNumber.getText().toString();
                if(inputPhoneNo.isEmpty()) inputPhoneNo = oldPhoneNo;
                inputAddress = address.getText().toString();
                if(inputAddress.isEmpty()) inputAddress = oldAddress;
                inputLinkedIn = linkedInUrl.getText().toString();
                if(inputLinkedIn.isEmpty()) inputLinkedIn = oldLinkedIn;
                inputName = name.getText().toString();
                if(inputName.isEmpty()) inputName = oldName;
                if(gender == null) gender = oldGender;
                if(dateOfBirth == null) dateOfBirth = oldDob;

                //mengecek apakah format email dan password sama
                boolean isFormatCorrect = checkFormat(inputEmail, inputPassword);
                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();

                //cek apakah password dan retype psasword sama
                if(!inputPassword.equals(passwordConfirm) || passwordConfirm.isEmpty() || inputPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"please re type your password",Toast.LENGTH_SHORT).show();
                }

                //jika semua persyaratan terpenuhi, maka data user akan diganti
                if(isFormatCorrect && inputPassword.equals(passwordConfirm) && !passwordConfirm.isEmpty()){
                    boolean isUpdated = db.updateSeeker(oldEmail, inputEmail, inputPassword, gender, dateOfBirth, inputAddress, "test", inputLinkedIn, inputName, inputPhoneNo);

                    Toast.makeText(getApplicationContext(),"update success",Toast.LENGTH_SHORT).show();
                    SharedPref SP = new SharedPref(EditSeekerProfile.this);
                    SP.clearAll(getApplicationContext());

                    Intent mainIntent = new Intent(EditSeekerProfile.this, SeekerLogin.class);
                    EditSeekerProfile.this.startActivity(mainIntent);
                    EditSeekerProfile.this.finish();

                }
                db.close();
            }
        });

    }
    /*
    fungsi: untuk mengambil data user lama yang berada di database dan menampilkannya
     */
    private void setUserData(String oldEmail){

        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        phoneNumber = (EditText) findViewById(R.id.et_phone_number);
        linkedInUrl = (EditText) findViewById(R.id.et_linkedin_url);
        address = (EditText) findViewById(R.id.et_address);
        name = (EditText) findViewById(R.id.et_username);
        displayDate = (TextView) findViewById(R.id.tv_dob);
        radioGroup = (RadioGroup) findViewById(R.id.radio_gender);

        //mengambil data profile user dan meletakkannya di halaman edit profile
        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();

        //mengambil data user di database dan memasukkannya ke cursor
        Cursor cursor = db.getUserData(oldEmail);

        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            //memasukkan data tersebut kedalam string
            oldPassword = cursor.getString(10);
            oldPhoneNo = cursor.getString(5);
            oldAddress = cursor.getString(4);
            oldLinkedIn = cursor.getString(8);
            oldName = cursor.getString(1);
            oldDob = cursor.getString(3);
            oldGender = cursor.getString(2);

            email.setText(oldEmail);
            phoneNumber.setText(oldPhoneNo);
            address.setText(oldAddress);
            linkedInUrl.setText(oldLinkedIn);
            name.setText(oldName);
            displayDate.setText(oldDob);

            if(oldGender.equalsIgnoreCase("male")){
                radioGroup.check(R.id.radio_male);
            }
            else{
                radioGroup.check(R.id.radio_female);
            }
        }
        db.close();
    }

    private boolean checkFormat(String email, String password){
        //validasi gender harus diiisi
        if(gender == null){
            Toast.makeText(getApplicationContext(),"input your gender",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi tanggal lahir harus diisi
        if(dateOfBirth == null){
            Toast.makeText(getApplicationContext(),"input your date of birth",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi nama: harus diisi, dan tidak boleh ada angka, panjangnya 3-30
        if(inputName.length() < 3 || inputName.length() > 30 ||inputName.isEmpty() || !(inputName.matches("^[\\p{L} .'-]+$"))){
            Toast.makeText(getApplicationContext(),"please input your name correctly",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi nomor telepon: harus diisi, tidak boleh ada huruf, 10-14 karakter
        if(inputPhoneNo.length() < 10 || inputPhoneNo.length() > 14 || inputPhoneNo.isEmpty() || !(inputPhoneNo.matches("\\d+"))){
            Toast.makeText(getApplicationContext(),"please input your phone number",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi address : tidak boleh kosong
        if(inputAddress.isEmpty()){
            Toast.makeText(getApplicationContext(),"please input your address",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi linkedIn: format harus benar, harus ada "." dan "/"
        if(inputLinkedIn.length() < 12 || !(inputLinkedIn.substring(0,13).equalsIgnoreCase("linkedIn.com/"))){
            Toast.makeText(getApplicationContext(),"input your linkedIn correctly",Toast.LENGTH_SHORT).show();
            return false;
        }

        //validasi panjang password
        if(password.isEmpty() || password.length() < 8){
            Toast.makeText(getApplicationContext(),"password should be more than 8 characters",Toast.LENGTH_SHORT).show();
            return false;
        }

        //cek format email
        int atIdx = -1;
        int pointIdx = -1;
        int len = email.length();
        int counter = 0;

        for(int i = 0; i < len; i++){
            if(email.charAt(i) == '@' && atIdx == -1){
                atIdx = i;
                counter++;
            }
            else if(email.charAt(i) == '.' && pointIdx == -1){
                pointIdx = i;
//                counter++;
            }
            if(counter > 2) break;
        }

        if(email.isEmpty() || counter > 2 || pointIdx - atIdx <= 1){
            Toast.makeText(getApplicationContext(),"Please input email according to format",Toast.LENGTH_SHORT).show();
            return false;
        }

        //mengecek apakah field email dan password sudah diisi
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(),"email or password Cannot be Empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //fungsi yang dijalankan jika radio button di click
    public void onRadioButtonClicked(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton)findViewById(radioId);

        gender = oldGender;
        gender = radioButton.getText().toString();
    }

}
