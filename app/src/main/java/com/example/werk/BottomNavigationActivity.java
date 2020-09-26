package com.example.werk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    //CREATED BY STANLEY - 2201744802

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        //untuk set default awalnya menjadi HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment fragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;

                case R.id.history:
                    fragment = new HistoryFragment();
                    break;

                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;
            }

            //untuk merubah halaman fragment menjadi halaman fragment yang dituju
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();


            return true;
        }
    };
}
