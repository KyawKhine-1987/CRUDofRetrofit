package com.android.freelance.javaretrofit.ui.activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.freelance.javaretrofit.R;
import com.android.freelance.javaretrofit.data.storage.SharedPrefManager;
import com.android.freelance.javaretrofit.ui.fragments.HomeFragment;
import com.android.freelance.javaretrofit.ui.fragments.SettingsFragment;
import com.android.freelance.javaretrofit.ui.fragments.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static String LOG_TAG = ProfileActivity.class.getName();
//    private TextView tv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreate() is called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*tv = findViewById(R.id.textview);
        User user = SharedPrefManager.getInstance(this).getUser();
        tv.setText("Welcome Back " + user.getName());*/
        BottomNavigationView bnv = findViewById(R.id.bottom_nav);
        bnv.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "TEST : onStart() is called...");
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        androidx.fragment.app.Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_users:
                fragment = new UsersFragment();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                break;
        }

        if (fragment != null) {
            displayFragment(fragment);
        }
        return false;
    }

    public void displayFragment(androidx.fragment.app.Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit();
    }
}
