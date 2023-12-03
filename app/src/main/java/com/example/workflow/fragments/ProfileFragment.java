package com.example.workflow.fragments;

import static com.example.workflow.utils.ConstantUtils.KEY_ADDRESS;
import static com.example.workflow.utils.ConstantUtils.KEY_DEPARTMENT;
import static com.example.workflow.utils.ConstantUtils.KEY_EMAIL_ADDRESS;
import static com.example.workflow.utils.ConstantUtils.KEY_FIRST_NAME;
import static com.example.workflow.utils.ConstantUtils.KEY_LAST_NAME;
import static com.example.workflow.utils.ConstantUtils.KEY_PHONE_NUMBER;
import static com.example.workflow.utils.ConstantUtils.KEY_USER;
import static com.example.workflow.utils.ConstantUtils.KEY_USER_NAME;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.database.offlineModels.EmployeeData;
import com.example.workflow.models.UserDetails;
import com.example.workflow.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ProfileFragment extends Fragment implements View.OnClickListener{

    private View viewFragment = null;

    EditText edt_name, edt_email, edt_dept, edt_address;

    TextView emname;
    TextView empdep;
    TextView email;
    TextView phone;
    TextView empadd;
    TextView empUserName;
    EditText edt_userName;

    LinearLayout buttonLayout;
    String userId;
    FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

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

        emname =viewFragment.findViewById(R.id.profileName);
        empdep = viewFragment.findViewById(R.id.profileDepartment);
        email = viewFragment.findViewById(R.id.profileEmail);
        phone = viewFragment.findViewById(R.id.profilePhoneNumber);
        empadd = viewFragment.findViewById(R.id.profileAddress);
        empUserName = viewFragment.findViewById(R.id.profileUserName);

        edt_name = viewFragment.findViewById(R.id.edit_profileName);
        edt_dept = viewFragment.findViewById(R.id.edit_profileDepartment);
        edt_email = viewFragment.findViewById(R.id.edit_Email);
        edt_address = viewFragment.findViewById(R.id.edit_Address);
        edt_userName = viewFragment.findViewById(R.id.edit_UserName);


        buttonLayout = viewFragment.findViewById(R.id.button_layout);

        final ImageView ivimage = (ImageView) viewFragment.findViewById(R.id.profile_image);

        ((ImageButton) viewFragment.findViewById(R.id.edit_btn)).setOnClickListener(this);
        ((AppCompatButton) viewFragment.findViewById(R.id.profileCancel_btn)).setOnClickListener(this);
        ((AppCompatButton) viewFragment.findViewById(R.id.profileUpdate_btn)).setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        getUserInfo(userId);

        return viewFragment;
    }

    private void onRefresh(){
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        getUserInfo(userId);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.edit_btn){
            showEditViewProfile();
        }

        else if(view.getId() == R.id.profileCancel_btn){
            showTextViewProfile();
        }

        else if(view.getId() == R.id.profileUpdate_btn){
            alertUpdateDialog();
        }

    }

    private String[] splitFullName(String fullName) {
        // Split the full name using space as a delimiter
        String[] names = fullName.split(" ");

        // Check if there are at least two parts (first name and last name)
        if (names.length >= 2) {
            // Extract the first name and last name
            String firstName = names[0];
            String lastName = names[names.length - 1];

            // Log or return the names as needed
            Log.d("SplitFullName", "First Name: " + firstName);
            Log.d("SplitFullName", "Last Name: " + lastName);

            // You can also return the names if needed
            return new String[]{firstName, lastName};
        } else {
            // Handle the case where the full name doesn't contain both first and last names
            Log.w("SplitFullName", "Invalid full name format");
            return null;
        }
    }

    private void processFullName(String fullName) {
        String[] names = splitFullName(fullName);

        if (names != null) {
            // Now you can use names[0] as the first name and names[1] as the last name
            String firstName = names[0];
            String lastName = names[1];
        }
    }

    private void updateUserInfo(String userId, String newfName, String newlName, String newEmail, String newUserName, String newAddress, String newDept) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(KEY_FIRST_NAME, newfName);
        userInfo.put(KEY_LAST_NAME, newlName);
        userInfo.put(KEY_USER_NAME, newUserName);
        userInfo.put(KEY_ADDRESS, newAddress);
        userInfo.put(KEY_DEPARTMENT, newDept);
        userInfo.put(KEY_EMAIL_ADDRESS, newEmail);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if(user.getUserId().equals(userId)) {
                        usersRef.child(dataSnapshot.getKey()).updateChildren(userInfo);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onRefresh();

        Log.d("UpdateUserInfo", "User details updated in the database");
    }


    private void alertUpdateDialog() {
        if(userId == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            userId = firebaseAuth.getCurrentUser().getUid();
        }
        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(getActivity());

        builder1.setMessage("Do you want to update the profile ?");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            String[] names = splitFullName(edt_name.getText().toString().trim());

                            if (names != null) {
                                // Now you can use names[0] as the first name and names[1] as the last name
                                String firstName = names[0];
                                String lastName = names[1];

                                if(edt_email.getText().toString().trim().length()>3 && edt_email.getText().toString().contains("@")) {
                                    if(edt_dept.getText().toString().trim().length()>2) {
                                        if(edt_address.getText().toString().trim().length()>3) {
                                            updateUserInfo(userId, firstName, lastName, edt_email.getText().toString().trim(), edt_userName.getText().toString().trim(),
                                                    edt_address.getText().toString().trim(), edt_dept.getText().toString().trim());
                                        } else {
                                            edt_address.setError("Please enter proper address");
                                        }

                                    } else {
                                        edt_dept.setError("Please enter a valid department");
                                    }
                                } else {
                                    edt_email.setError("Please enter valid email");
                                }
                            }
                            else {
                                edt_name.setError("Please enter name");
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        showTextViewProfile();
                        getUserInfo(userId);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        android.app.AlertDialog alertCheckInAlertDialog = builder1.create();
        alertCheckInAlertDialog.show();
    }

    private void getUserInfo(String userId) {
        usersRef = FirebaseDatabase.getInstance().getReference(KEY_USER);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    if(user.getUserId() != null) {
                        if(user.getUserId().equals(userId)) {
                            emname.setText(user.getFirstName() + " " + user.getLastName());
                            edt_name.setText(user.getFirstName() + " " + user.getLastName());
                            empdep.setText(user.getDept());
                            edt_dept.setText(user.getDept());
                            empadd.setText(user.getAddress());
                            edt_address.setText(user.getAddress());
                            email.setText(user.getEmailAddress());
                            edt_email.setText(user.getEmailAddress());
                            empUserName.setText(user.getUserName());
                            edt_userName.setText(user.getUserName());
                            phone.setText(user.getPhonenumber());
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showTextViewProfile() {
        emname.setVisibility(View.VISIBLE);
        empdep.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        empadd.setVisibility(View.VISIBLE);
        empUserName.setVisibility(View.VISIBLE);

        edt_name.setVisibility(View.GONE);
        edt_email.setVisibility(View.GONE);
        edt_dept.setVisibility(View.GONE);
        edt_address.setVisibility(View.GONE);
        edt_userName.setVisibility(View.GONE);

        buttonLayout.setVisibility(View.GONE);
    }

    private void showEditViewProfile() {
        emname.setVisibility(View.GONE);
        empdep.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        empadd.setVisibility(View.GONE);
        empUserName.setVisibility(View.GONE);

        edt_name.setVisibility(View.VISIBLE);
        edt_email.setVisibility(View.VISIBLE);
        edt_dept.setVisibility(View.VISIBLE);
        edt_address.setVisibility(View.VISIBLE);
        edt_userName.setVisibility(View.VISIBLE);

        buttonLayout.setVisibility(View.VISIBLE);
    }


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}