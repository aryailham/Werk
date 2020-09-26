package com.example.werk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.werk.model.User;

public class ProviderLogin extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView gotoSeeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_login);

        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        login = (Button)findViewById(R.id.btn_login);
        gotoSeeker = (TextView)findViewById(R.id.tv_seeker_login);

        //membuat shared preferences
        SharedPref SP = new SharedPref(ProviderLogin.this);
        User user = SP.load();
        //clearALl untuk logout nanti
//        SP.clearAll(getApplicationContext());

        //cek apakah user sudah login atau belum
        if(!user.getEmail().isEmpty() && !user.getPassword().isEmpty()){
            //langsung pergi ke landing page
            Intent intent = new Intent(getApplicationContext(), SeekerProfile.class);
            startActivity(intent);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String inputEmail = email.getText().toString();
                String inputPassword = password.getText().toString();

                userValidation(inputEmail, inputPassword);

            }
        });

        gotoSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), SeekerLogin.class);
                ProviderLogin.this.startActivity(mainIntent);
                ProviderLogin.this.finish();
            }
        });
    }

    /*
    fungsi:
    -melakukan pengecekan panjang password
    -melakukan pengecekan format email
    -mengecek apakah email sudah didaftarkan
    -mengecek apakah email dan password sudah benar
    -jika semua persyaratan terpenuhi, maka user dianggap berhasil login
     */
    private void userValidation(String inputEmail, String inputPassword){

        //cek panjang password
        if(inputPassword.length() < 8){
            Toast.makeText(getApplicationContext(),"password should be more than 8 characters",Toast.LENGTH_SHORT).show();
            return;
        }

        //cek format email
        int atIdx = -1;
        int pointIdx = -1;
        int len = inputEmail.length();
        int counter = 0;

        for(int i = 0; i < len; i++){
            if(inputEmail.charAt(i) == '@' && atIdx == -1){
                atIdx = i;
                counter++;
            }
            else if(inputEmail.charAt(i) == '.' && pointIdx == -1){
                pointIdx = i;
//                counter++;
            }
            if(counter > 2) break;
        }

        if(counter > 2 || pointIdx - atIdx <= 1){
            Toast.makeText(getApplicationContext(),"Please input email according to format",Toast.LENGTH_SHORT).show();
            return;
        }

        //mengecek apakah field email dan password sudah diisi
        if(inputEmail.isEmpty() || inputPassword.isEmpty()){
            Toast.makeText(getApplicationContext(),"email or password Cannot be Empty",Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();

        boolean isExist = db.checkProviderExist(inputEmail, inputPassword);

        //mengecek apakah email dan password benar atau salah
        if(!isExist){
            Toast.makeText(getApplicationContext(),"email or password is Incorrect",Toast.LENGTH_SHORT).show();
        }

        //toast jika email dan password benar
        else if(isExist){
            Toast.makeText(getApplicationContext(),"you've been logged in",Toast.LENGTH_SHORT).show();

            //login dan masuk ke landing page
            User User = new User();
            User.setEmail(inputEmail);
            User.setPassword(inputPassword);

            //simpan di shared preferences
            SharedPref SP = new SharedPref(getApplicationContext());
            SP.save(User);

            //code supaya membuka activity landing page
//            Intent mainIntent = new Intent(getApplicationContext(), SeekerProfile.class);
//            SeekerLogin.this.startActivity(mainIntent);
//            SeekerLogin.this.finish();
        }

        db.close();

    }
}
