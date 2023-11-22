package com.example.workflow.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.workflow.R;
import com.example.workflow.database.offlineModels.EmployeeData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ProfileFragment extends Fragment implements View.OnClickListener{

    private View viewFragment = null;

    EmployeeData empData;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        viewFragment = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView emname =viewFragment.findViewById(R.id.settingCustomerName);
        TextView emid = viewFragment.findViewById(R.id.profileUserID);
        TextView empdep = viewFragment.findViewById(R.id.settingDepartment);
        TextView email = viewFragment.findViewById(R.id.settingEmail);
        TextView phone = viewFragment.findViewById(R.id.settingPhoneNumber);
        TextView empadd = viewFragment.findViewById(R.id.settingAddress);

        final ImageView ivimage = (ImageView) viewFragment.findViewById(R.id.profile_image);

        empData = new EmployeeData();
        return viewFragment;
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}