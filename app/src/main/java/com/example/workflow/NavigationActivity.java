package com.example.workflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {
    ActionBar actionBar;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Check in / Check out");
        }
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnItemSelectedListener(selectedListener);

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment,"");
        fragmentTransaction.commit();
    }

    private final NavigationBarView.OnItemSelectedListener selectedListener = new NavigationBarView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(item.getItemId() == R.id.menu_home) {
                actionBar.setTitle("Home");

                return true;
            } else if(item.getItemId() == R.id.menu_profile) {
                actionBar.setTitle("Profile");

                return true;
            } else if(item.getItemId() == R.id.menu_calendar) {
                actionBar.setTitle("Calendar");
                CalendarFragment calendarFragment = new CalendarFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, calendarFragment);
                fragmentTransaction.commit();

                return true;
            } else if(item.getItemId() == R.id.menu_leaveRequest) {
                actionBar.setTitle("Leave Request");

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
}