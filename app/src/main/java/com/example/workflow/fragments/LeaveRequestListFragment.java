package com.example.workflow.fragments;

import static com.example.workflow.utils.ConstantUtils.KEY_LEAVE_REQUEST_TABLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workflow.Adapter.LeaveRequestAdapter;
import com.example.workflow.R;
import com.example.workflow.chat.ChatListModel;
import com.example.workflow.models.LeaveRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaveRequestListFragment extends Fragment {
    DatabaseReference dbref;
    List<LeaveRequestModel> leaveRequestList;
    List<ChatListModel> users;
    LeaveRequestAdapter leaveRequestAdapter;
    RecyclerView leaveRequestRecycler;


    public LeaveRequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_request_list, container, false);
        leaveRequestRecycler = view.findViewById(R.id.leaveRequestRecycle);
        leaveRequestList = new ArrayList<>();

        dbref = FirebaseDatabase.getInstance().getReference(KEY_LEAVE_REQUEST_TABLE);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaveRequestList.clear();
                users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatListModel user = dataSnapshot.getValue(ChatListModel.class);
                    users.add(user);
                }
                loadLeaveRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void loadLeaveRequest() {
        dbref = FirebaseDatabase.getInstance().getReference(KEY_LEAVE_REQUEST_TABLE);
        for(int i = 0; i < users.size(); i++) {
            dbref = dbref.child(users.get(i).getUserId());
            dbref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        LeaveRequestModel leaveRequest = dataSnapshot.getValue(LeaveRequestModel.class);
                        if(leaveRequest.getStatus() != 0) {
                            leaveRequestList.add(leaveRequest);
                        }
                    }

                    leaveRequestAdapter = new LeaveRequestAdapter(getActivity(), leaveRequestList);
                    leaveRequestRecycler.setAdapter(leaveRequestAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}