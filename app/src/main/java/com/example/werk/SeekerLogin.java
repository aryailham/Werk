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

public class SeekerLogin extends AppCompatActivity {

    //deklarasi variabel global
    EditText email, password;
    TextView tvRegister;
    Button login;
    TextView gotoProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_login);
        email = (EditText)findViewById(R.id.et_email);
        password = (EditText)findViewById(R.id.et_password);
        login = (Button)findViewById(R.id.btn_login);
        tvRegister = (TextView)findViewById(R.id.tv_register);
        gotoProvider = (TextView) findViewById(R.id.tv_provider_login);


        //membuat shared preferences
        SharedPref SP = new SharedPref(SeekerLogin.this);
        User user = SP.load();

        /*
        Commented by Arya
        alasan: sudah ada tombol login, tidak perlu panggil fungsi clearAll langsung
         */
//        SP.clearAll(getApplicationContext());

        //cek apakah user sudah login atau belum, jika  sudah lansung ke activity selanjutnya (auto login)
        if(!user.getEmail().isEmpty() && !user.getPassword().isEmpty()){
            //langsung pergi ke landing page
            Intent intent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
            startActivity(intent);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                String inputEmail = email.getText().toString();
                String inputPassword = password.getText().toString();


                /*
                 Modified By Arya
                 Tanggal: Sabtu, 1 februari 2019
                 perubahan, membuat function untuk mengecek email dan password
                 */

                //function untuk cek format dan apakah user sudah daftar atau belum
                userValidation(inputEmail, inputPassword);
            }
        });

        //pergi ke activity seeker register
        tvRegister.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SeekerRegister.class);
                startActivity(intent);
                finish();
            }
        });

        //pergi ke activity provider login
        gotoProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), ProviderLogin.class);
                SeekerLogin.this.startActivity(mainIntent);
                SeekerLogin.this.finish();
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

        //fungsi untuk mengecek apakah email dan password ada di database
        boolean isExist = db.checkExist(inputEmail, inputPassword);

        //mengecek apakah email dan password benar atau salah
        if(!isExist){
            Toast.makeText(getApplicationContext(),"email or password is Incorrect",Toast.LENGTH_SHORT).show();
        }

        //toast jika email dan password benar, akan masuk ke activity selanjutnya
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
            Intent mainIntent = new Intent(getApplicationContext(), BottomNavigationActivity.class);
            SeekerLogin.this.startActivity(mainIntent);
            SeekerLogin.this.finish();
        }

        db.close();

    }

}
