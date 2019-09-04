package com.android.freelance.javaretrofit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.freelance.javaretrofit.R;
import com.android.freelance.javaretrofit.data.network.RetrofitClient;
import com.android.freelance.javaretrofit.data.network.responses.DefaultResponse;
import com.android.freelance.javaretrofit.data.storage.SharedPrefManager;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String LOG_TAG = MainActivity.class.getName();
    private EditText etEmail, etPassword, etName, etSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreate() is called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etName = findViewById(R.id.editTextName);
        etSchool = findViewById(R.id.editTextSchool);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        Log.i(LOG_TAG, "TEST : onStart() is called...");
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View view) {
        Log.i(LOG_TAG, "TEST : onClick() is called...");

        switch (view.getId()) {
            case R.id.buttonSignUp:
                userSignUp();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
    }

    private void userSignUp() {
        Log.i(LOG_TAG, "TEST : userSignUp() is called...");

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String school = etSchool.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required!");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password should be at least six characters long!");
            etPassword.requestFocus();
            return;
        }

        if (school.isEmpty()) {
            etSchool.setError("School is required!");
            etSchool.requestFocus();
            return;
        }

        //TODO
        callWebServiceForDefaultResponse(email, password, name, school);
    }

    private void callWebServiceForDefaultResponse(String email, String password, String name, String school) {
        Log.i(LOG_TAG, "TEST : callWebServiceForDefaultResponse() is called...");

        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApiServices()
                .createUser(email, password, name, school);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                if (response.code() == 201) {
                    DefaultResponse dr = response.body();
                    Toast.makeText(MainActivity.this, dr.getMsg(), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 422) {
                    Toast.makeText(MainActivity.this, "User Already Exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");

                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
