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
import com.android.freelance.javaretrofit.data.network.responses.LogInResponse;
import com.android.freelance.javaretrofit.data.storage.SharedPrefManager;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private static String LOG_TAG = LogInActivity.class.getName();
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreate() is called...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.tvRegister).setOnClickListener(this);
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
            case R.id.btnLogin:
                userLogIn();
                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void userLogIn() {
        Log.i(LOG_TAG, "TEST : userLogIn() is called...");

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

        callWebServiceForLogInResponse(email, password);
    }

    private void callWebServiceForLogInResponse(String email, String password) {
        Log.i(LOG_TAG, "TEST : callWebServiceForLogInResponse() is called...");

        Call<LogInResponse> call = RetrofitClient
                .getInstance()
                .getApiServices()
                .userLogIn(email, password);
        call.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                Log.i(LOG_TAG, "TEST : onResponse() is called...");

                LogInResponse lir = response.body();
                if (!lir.isError()) {

                    SharedPrefManager.getInstance(LogInActivity.this)
                            .saveUser(lir.getUser());

                    Intent i = new Intent(LogInActivity.this, ProfileActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(LogInActivity.this, "User doesn't Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : onFailure() is called...");

                Toast.makeText(LogInActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
