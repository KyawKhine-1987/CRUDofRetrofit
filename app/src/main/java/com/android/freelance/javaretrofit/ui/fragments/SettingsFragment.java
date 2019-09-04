package com.android.freelance.javaretrofit.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.freelance.javaretrofit.R;
import com.android.freelance.javaretrofit.data.network.RetrofitClient;
import com.android.freelance.javaretrofit.data.network.responses.DefaultResponse;
import com.android.freelance.javaretrofit.data.network.responses.LogInResponse;
import com.android.freelance.javaretrofit.data.network.responses.model.User;
import com.android.freelance.javaretrofit.data.storage.SharedPrefManager;
import com.android.freelance.javaretrofit.ui.activities.LogInActivity;
import com.android.freelance.javaretrofit.ui.activities.MainActivity;
import com.android.freelance.javaretrofit.ui.activities.ProfileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static String LOG_TAG = SettingsFragment.class.getName();
    private EditText etEmail, etName, etCurrentPwd, etNewPwd, etSchool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(LOG_TAG, "TEST : onCreateView() is called...");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Log.i(LOG_TAG, "TEST : onViewCreated() is called...");
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.editTextEmail);
        etName = view.findViewById(R.id.editTextName);
        etCurrentPwd = view.findViewById(R.id.editTextCurrentPassword);
        etNewPwd = view.findViewById(R.id.editTextNewPassword);
        etSchool = view.findViewById(R.id.editTextSchool);

        view.findViewById(R.id.buttonSave).setOnClickListener(this);
        view.findViewById(R.id.buttonChangePassword).setOnClickListener(this);
        view.findViewById(R.id.buttonLogOut).setOnClickListener(this);
        view.findViewById(R.id.buttonDelete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Log.i(LOG_TAG, "TEST : onClick() is called...");
        switch (view.getId()) {
            case R.id.buttonSave:
                updateProfile();
                break;
            case R.id.buttonChangePassword:
                updatePassword();
                break;
            case R.id.buttonLogOut:
                logOut();
                break;
            case R.id.buttonDelete:
                deleteUser();
                break;

        }
    }

    private void updateProfile() {
        Log.i(LOG_TAG, "TEST : updateProfile() is called...");

        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String school = etSchool.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid Email!");
            etEmail.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            etName.setError("Name is required!");
            etName.requestFocus();
            return;
        }

        if (school.isEmpty()) {
            etSchool.setError("School is required!");
            etSchool.requestFocus();
            return;
        }

        callWebServiceForLogInResponse(email, name, school);
    }

    private void callWebServiceForLogInResponse(String email, String name, String school) {

        Log.i(LOG_TAG, "TEST : callWebServiceForLogInResponse() is called...");
        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Call<LogInResponse> call = RetrofitClient.getInstance().getApiServices().updateUser(
                user.getId(),
                email,
                name,
                school
        );

        call.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {

                Log.i(LOG_TAG, "TEST : onResponse() is called...");
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                if (!response.body().isError()) {
                    SharedPrefManager.getInstance(getActivity()).saveUser(response.body().getUser());
                }
            }

            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {

                Log.i(LOG_TAG, "TEST : onFailure() is called...");
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updatePassword() {
        Log.i(LOG_TAG, "TEST : updatePassword() is called...");

        String cPwd = etCurrentPwd.getText().toString().trim();
        String nPwd = etNewPwd.getText().toString().trim();

        if (cPwd.isEmpty()) {
            etCurrentPwd.setError("Password is required!");
            etCurrentPwd.requestFocus();
            return;
        }

        if (nPwd.isEmpty()) {
            etNewPwd.setError("Enter new password!");
            etNewPwd.requestFocus();
            return;
        }

        callWebServiceForDefaultResponse(cPwd, nPwd);
    }

    private void callWebServiceForDefaultResponse(String cPwd, String nPwd) {
        Log.i(LOG_TAG, "TEST : callWebServiceForDefaultResponse() is called...");

        User user = SharedPrefManager.getInstance(getContext()).getUser();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApiServices().updatePassword(cPwd, nPwd, user.getEmail());
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Log.i(LOG_TAG, "TEST : updateProfile() is called...");

                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.i(LOG_TAG, "TEST : updateProfile() is called...");
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void logOut() {
        Log.i(LOG_TAG, "TEST : logOut() is called...");

        SharedPrefManager.getInstance(getActivity()).clear();
        Intent i = new Intent(getActivity(), LogInActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void deleteUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("This action is irreversible...");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                User user = SharedPrefManager.getInstance(getActivity()).getUser();
                Call<DefaultResponse> call = RetrofitClient.getInstance().getApiServices().deleteUser(user.getId());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                        if (!response.body().isErr()){
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), dialogInterface.toString(), Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
}
