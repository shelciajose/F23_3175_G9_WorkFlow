package com.example.workflow.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.workflow.R;
import com.example.workflow.chat.ChatListFragment;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.fragments.CalendarFragment;
import com.example.workflow.fragments.HomeFragment;
import com.example.workflow.fragments.LeaveRequestFragment;
import com.example.workflow.fragments.OTPFragment;
import com.example.workflow.fragments.ProfileFragment;
import com.example.workflow.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class NavigationActivity extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView navigationView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Home");
        }

        ((ImageButton) toolbar.findViewById(R.id.logout_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertLogoutDialog();
            }
        });

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(selectedListener);

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment,"");
        fragmentTransaction.commit();
    }

    private void alertLogoutDialog() {

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(NavigationActivity.this);

        builder1.setMessage("Do you want to logout ?");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {

                            new OfflineDatabase(NavigationActivity.this).deleteAllAtTheTimeOfLogout();
                            PreferenceUtils.clearUserPreference(NavigationActivity.this);
                            startActivity(new Intent(NavigationActivity.this, RegistrationActivity.class));
                            finish();
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

    private final NavigationBarView.OnItemSelectedListener selectedListener = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.menu_home) {
                actionBar.setTitle("Home");
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                fragmentTransactionHome.replace(R.id.content, homeFragment);
                fragmentTransactionHome.commit();

                return true;
            } else if(item.getItemId() == R.id.menu_profile) {
                actionBar.setTitle("Profile");
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction fragmentTransactionProfile = getSupportFragmentManager().beginTransaction();
                fragmentTransactionProfile.replace(R.id.content, profileFragment);
                fragmentTransactionProfile.commit();

                return true;
            } else if(item.getItemId() == R.id.menu_calendar) {
                actionBar.setTitle("Calendar");
                CalendarFragment calendarFragment = new CalendarFragment();
                FragmentTransaction fragmentTransactionCalendar = getSupportFragmentManager().beginTransaction();
                fragmentTransactionCalendar.replace(R.id.content, calendarFragment);
                fragmentTransactionCalendar.commit();

                return true;
            } else if(item.getItemId() == R.id.menu_leaveRequest) {
                actionBar.setTitle("Leave Request");
                LeaveRequestFragment leaveRequestFragment = new LeaveRequestFragment();
                FragmentTransaction fragmentTransactionLeaveRequest = getSupportFragmentManager().beginTransaction();
                fragmentTransactionLeaveRequest.replace(R.id.content, leaveRequestFragment, "");
                fragmentTransactionLeaveRequest.commit();

                return true;
            } else if(item.getItemId() == R.id.menu_chat) {
                actionBar.setTitle("Chat");
                ChatListFragment listFragment = new ChatListFragment();
                FragmentTransaction fragmentTransactionChat = getSupportFragmentManager().beginTransaction();
                fragmentTransactionChat.replace(R.id.content, listFragment, "");
                fragmentTransactionChat.commit();

                return true;
            } else {
                return false;
            }
//            else if(item.getItemId() == R.id.menu_singout) {
//                actionBar.setTitle("Sign out");
//                return true;
//            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (navigationView.getSelectedItemId() == R.id.menu_home) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert!!")
                    .setMessage("Do you want to close " + getString(R.string.app_name) + " ?")
                    .setCancelable(true)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            try {
                                finishAffinity();
                            } catch (Exception e) {
                                finish();
                                e.printStackTrace();
                            }

                        }

                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else{
            finish();
        }
    }
}