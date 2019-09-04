package com.android.freelance.javaretrofit.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.freelance.javaretrofit.R;
import com.android.freelance.javaretrofit.data.storage.SharedPrefManager;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private static String LOG_TAG = HomeFragment.class.getName();
    private TextView tvEmail, tvName, tvSchool;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onCreateView() is called...");

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST : onViewCreated() is called...");

        super.onViewCreated(view, savedInstanceState);
        tvEmail = view.findViewById(R.id.fragmentTVEmail);
        tvName = view.findViewById(R.id.fragmentTVName);
        tvSchool = view.findViewById(R.id.fragmentTVSchool);

        tvEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        tvName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());
        tvSchool.setText(SharedPrefManager.getInstance(getActivity()).getUser().getSchool());
    }
}
