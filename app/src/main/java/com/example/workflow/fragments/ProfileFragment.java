package com.example.workflow.fragments;

import static com.example.workflow.utils.ConstantUtils.KEY_ADDRESS;
import static com.example.workflow.utils.ConstantUtils.KEY_DEPARTMENT;
import static com.example.workflow.utils.ConstantUtils.KEY_EMAIL_ADDRESS;
import static com.example.workflow.utils.ConstantUtils.KEY_FIRST_NAME;
import static com.example.workflow.utils.ConstantUtils.KEY_LAST_NAME;
import static com.example.workflow.utils.ConstantUtils.KEY_PHONE_NUMBER;
import static com.example.workflow.utils.ConstantUtils.KEY_USER;

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

    UserDetails userDetails;

    EditText edt_name, edt_phone, edt_email, edt_dept, edt_address;

    TextView emname;
    TextView emid;
    TextView empdep;
    TextView email;
    TextView phone;
    TextView empadd;

    LinearLayout buttonLayout;
    //Start change by Takumi
    String userId;
    FirebaseAuth firebaseAuth;
    //End

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
        emid = viewFragment.findViewById(R.id.profileUserID);
        empdep = viewFragment.findViewById(R.id.profileDepartment);
        email = viewFragment.findViewById(R.id.profileEmail);
        phone = viewFragment.findViewById(R.id.profilePhoneNumber);
        empadd = viewFragment.findViewById(R.id.profileAddress);


        edt_name = viewFragment.findViewById(R.id.edit_profileName);
        edt_dept = viewFragment.findViewById(R.id.edit_profileDepartment);
        edt_phone = viewFragment.findViewById(R.id.edit_PhoneNumber);
        edt_email = viewFragment.findViewById(R.id.edit_Email);
        edt_address = viewFragment.findViewById(R.id.edit_Address);

        buttonLayout = viewFragment.findViewById(R.id.button_layout);

        final ImageView ivimage = (ImageView) viewFragment.findViewById(R.id.profile_image);

        ((ImageButton) viewFragment.findViewById(R.id.edit_btn)).setOnClickListener(this);
        ((AppCompatButton) viewFragment.findViewById(R.id.profileCancel_btn)).setOnClickListener(this);
        ((AppCompatButton) viewFragment.findViewById(R.id.profileUpdate_btn)).setOnClickListener(this);

        //Start change by Takumi
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        //End

        try {
            userDetails = new OfflineDatabase(getActivity()).getUser().get(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //Start Changed by Takumi
        //usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef = FirebaseDatabase.getInstance().getReference(KEY_USER);
        //End

        emname.setText(userDetails.getfName() + " " + userDetails.getlName());
        edt_name.setText(userDetails.getfName() + " " + userDetails.getlName());
        emid.setText(userDetails.getUserID());
        empdep.setText(userDetails.getDepartment());
        edt_dept.setText(userDetails.getDepartment());
        empadd.setText(userDetails.getAddress());
        edt_address.setText(userDetails.getAddress());
        email.setText(userDetails.getEmail());
        edt_email.setText(userDetails.getEmail());
        phone.setText(userDetails.getPhone());
        edt_phone.setText(userDetails.getPhone());


        return viewFragment;
    }

    private void onRefresh(){
        try {
            userDetails = new OfflineDatabase(getActivity()).getUser().get(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        emname.setText(userDetails.getfName() + " " + userDetails.getlName());
        edt_name.setText(userDetails.getfName() + " " + userDetails.getlName());
        emid.setText(userDetails.getUserID());
        empdep.setText(userDetails.getDepartment());
        edt_dept.setText(userDetails.getDepartment());
        empadd.setText(userDetails.getAddress());
        edt_address.setText(userDetails.getAddress());
        email.setText(userDetails.getEmail());
        edt_email.setText(userDetails.getEmail());
        phone.setText(userDetails.getPhone());
        edt_phone.setText(userDetails.getPhone());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.edit_btn){
            emname.setVisibility(View.GONE);
            empdep.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
            empadd.setVisibility(View.GONE);

            edt_name.setVisibility(View.VISIBLE);
            edt_email.setVisibility(View.VISIBLE);
            edt_phone.setVisibility(View.VISIBLE);
            edt_dept.setVisibility(View.VISIBLE);
            edt_address.setVisibility(View.VISIBLE);

            buttonLayout.setVisibility(View.VISIBLE);
        }

        else if(view.getId() == R.id.profileCancel_btn){
            emname.setVisibility(View.VISIBLE);
            empdep.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            empadd.setVisibility(View.VISIBLE);

            edt_name.setVisibility(View.GONE);
            edt_email.setVisibility(View.GONE);
            edt_phone.setVisibility(View.GONE);
            edt_dept.setVisibility(View.GONE);
            edt_address.setVisibility(View.GONE);

            buttonLayout.setVisibility(View.GONE);
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

    private void updateUserInfo(String userId, String newfName, String newlName, String newEmail, String newPhoneNumber, String newAddress, String newDept) {
        //Start Changed by Takumi
        //DatabaseReference userRef = usersRef.child(userId);
        //DatabaseReference userRef = usersRef.child(userId);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put(KEY_FIRST_NAME, newfName);
        userInfo.put(KEY_LAST_NAME, newlName);
        userInfo.put(KEY_PHONE_NUMBER, newPhoneNumber);
        userInfo.put(KEY_ADDRESS, newAddress);
        userInfo.put(KEY_DEPARTMENT, newDept);
        userInfo.put(KEY_EMAIL_ADDRESS, newEmail);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    System.out.println(userId);
                    System.out.println(user.getUserId());
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

        // Update the user details in the Realtime Database
        //userRef.child("firstName").setValue(newfName);
        //userRef.child("lastName").setValue(newlName);
        //userRef.child("emailAddress").setValue(newEmail);
        //userRef.child("phoneNumber").setValue(newPhoneNumber);
        //userRef.child("Address").setValue(newAddress);
        //userRef.child("Dept").setValue(newDept);
        //End

        userDetails.setAddress(newAddress);
        userDetails.setfName(newfName);
        userDetails.setlName(newlName);
        userDetails.setEmail(newEmail);
        userDetails.setPhone(newPhoneNumber);
        userDetails.setDepartment(newDept);
        new OfflineDatabase(getActivity()).updateUserDetailsToDatabase(userId, userDetails);

        onRefresh();

        Log.d("UpdateUserInfo", "User details updated in the database");
    }


    private void alertUpdateDialog() {

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

                                if(edt_phone.getText().toString().trim().length()==10){
                                    if(edt_email.getText().toString().trim().length()>3 && edt_email.getText().toString().contains("@")){
                                        if(edt_dept.getText().toString().trim().length()>2){
                                            if(edt_address.getText().toString().trim().length()>3){
                                                //Start change by Takumi
                                                //updateUserInfo(userDetails.getUserID(), firstName,lastName,edt_email.getText().toString().trim(), edt_phone.getText().toString().trim(),
                                                        //edt_address.getText().toString().trim(), edt_dept.getText().toString().trim());
                                                updateUserInfo(userId, firstName, lastName, edt_email.getText().toString().trim(), edt_phone.getText().toString().trim(),
                                                        edt_address.getText().toString().trim(), edt_dept.getText().toString().trim());
                                                //End
                                            }
                                            else {
                                                edt_address.setError("Please enter proper address");
                                            }
                                        }
                                        else {
                                            edt_dept.setError("Please enter a valid department");
                                        }
                                    }
                                    else {
                                        edt_email.setError("Please enter valid email");
                                    }
                                }
                                else {
                                    edt_phone.setError("Please enter valid phone number");
                                }
                            }
                            else {
                                edt_name.setError("Please enter name");
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

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


    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}