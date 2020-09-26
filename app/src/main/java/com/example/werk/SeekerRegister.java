package com.example.werk;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class SeekerRegister extends AppCompatActivity {

    //deklarasi object dan variable
    EditText email, password, confirmPassword, phoneNumber, linkedInUrl, address, name;
    Button register;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView displayDate;
    String dateOfBirth = null;
    String gender = null;
    DatePickerDialog.OnDateSetListener dateListener;
    private final int WaitLength = 2000;

    //deklarasi varibel global penampung input
    String inputEmail;
    String inputPassword;
    String PasswordConfirm;
    String inputPhoneNo;
    String inputAddress;
    String inputLinkedIn;
    String inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_register);

        //inisialisasi object dan variable
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        confirmPassword = (EditText)findViewById(R.id.et_password_confirm);
        phoneNumber = (EditText) findViewById(R.id.et_phone_number);
        linkedInUrl = (EditText) findViewById(R.id.et_linkedin_url);
        address = (EditText) findViewById(R.id.et_address);
        name = (EditText) findViewById(R.id.et_username);
        register = (Button)findViewById(R.id.btn_register);
        displayDate = (TextView) findViewById(R.id.tv_dob);
        radioGroup = (RadioGroup) findViewById(R.id.radio_gender);

        //jika text tanggal lahir ditekan, maka akan menampilkan date picker untuk tanggal lahir
        displayDate.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SeekerRegister.this,android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth , dateListener, year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        //menampilkan tanggal lahir yang dipilih ke Textview displayDate
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month + 1;

                dateOfBirth = year+ "/"+ month +"/" +day;
                displayDate.setText(dateOfBirth);
            }
        };

        //mendaftarkan user jika semua field sudah diisi dan tombol sign up ditekan
        register.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                inputEmail = email.getText().toString();
                inputPassword = password.getText().toString();
                PasswordConfirm = confirmPassword.getText().toString();
                inputPhoneNo = phoneNumber.getText().toString();
                inputAddress = address.getText().toString();
                inputLinkedIn = linkedInUrl.getText().toString();
                inputName = name.getText().toString();

                DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
                db.open();

                boolean isFormatCorrect = checkFormat(inputEmail, inputPassword);
                boolean isExist = db.checkExist(inputEmail);

                //cek apakah email sudah didaftarkan didalam database
                if(isExist == true){
                    Toast.makeText(getApplicationContext(),"User Already Exist", Toast.LENGTH_SHORT).show();
                }

                //cek apakah password dan konfirmasi password sama
                else if(!inputPassword.equals(PasswordConfirm)){
                    Toast.makeText(getApplicationContext(),"Please Retype Your password Correctly", Toast.LENGTH_SHORT).show();
                }

                //jika belum ada, daftarkan
                else if(!isExist && isFormatCorrect){
                    boolean isInserted = db.registerSeeker(inputEmail, inputPassword, gender, dateOfBirth, inputAddress , "test", inputLinkedIn, inputName , inputPhoneNo);
                    if(isInserted) Toast.makeText(getApplicationContext(),"register Success", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(getApplicationContext(), SeekerLogin.class);
                    SeekerRegister.this.startActivity(mainIntent);
                    SeekerRegister.this.finish();

                    /*
                    Commented by Arya
                    alasan: ada bug
                     */

//                    new Handler().postDelayed(new Runnable(){
//                        @Override
//                        public void run() {
//                            /* Create an Intent that will start the Menu-Activity. */
//                            Intent mainIntent = new Intent(getApplicationContext(), SeekerLogin.class);
//                            SeekerRegister.this.startActivity(mainIntent);
//                            SeekerRegister.this.finish();
//                        }
//                    }, WaitLength);
                }

                db.close();
            }
        });
    }


    //menentukan jenis kelamin berdasarkan input yang diberikan
    public void onRadioButtonClicked(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = (RadioButton)findViewById(radioId);

        gender = radioButton.getText().toString();

    }


    //fungsi ini berfungsi untuk mengecek format email dan password
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

}