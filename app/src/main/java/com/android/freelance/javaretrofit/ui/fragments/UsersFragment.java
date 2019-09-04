package com.android.freelance.javaretrofit.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.freelance.javaretrofit.R;
import com.android.freelance.javaretrofit.data.network.RetrofitClient;
import com.android.freelance.javaretrofit.data.network.responses.UsersResponse;
import com.android.freelance.javaretrofit.data.network.responses.model.User;
import com.android.freelance.javaretrofit.ui.activities.LogInActivity;
import com.android.freelance.javaretrofit.ui.adapters.UsersAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private static final String LOG_TAG = UsersFragment.class.getName();
    private RecyclerView mRV;
    private UsersAdapter mA;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreateView() is called...");

        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onViewCreated() is called...");

        super.onViewCreated(view, savedInstanceState);

        mRV = view.findViewById(R.id.recyclerView);
        mRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        callWebServiceForUsersResponse();
    }

    private void callWebServiceForUsersResponse() {

    Call<UsersResponse> call = RetrofitClient.getInstance().getApiServices().fetchAllUsers();
    call.enqueue(new Callback<UsersResponse>() {
        @Override
        public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
            Log.i(LOG_TAG, "TEST : onResponse() is called...");

            userList =response.body().getUsers();
            mA = new UsersAdapter(getActivity(), userList);
            mRV.setAdapter(mA);
        }

        @Override
        public void onFailure(Call<UsersResponse> call, Throwable t) {
            Log.i(LOG_TAG, "TEST : onFailure() is called...");
            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
    }
}
