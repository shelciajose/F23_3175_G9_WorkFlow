package com.example.workflow.Adapter;

import static com.example.workflow.utils.ConstantUtils.KEY_LEAVE_REQUEST_TABLE;
import static com.example.workflow.utils.ConstantUtils.KEY_STATUS;
import static com.example.workflow.utils.ConstantUtils.KEY_USER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workflow.R;
import com.example.workflow.models.LeaveRequestModel;
import com.example.workflow.models.UserModel;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.Myholder> {
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbref;
    List<LeaveRequestModel> leaveRequestList;

    public LeaveRequestAdapter(){}

    public LeaveRequestAdapter(Context context, List<LeaveRequestModel> leaveRequestList) {
        this.context = context;
        this.leaveRequestList = leaveRequestList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leave_request, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        String userId = leaveRequestList.get(position).getUserId();
        int status = leaveRequestList.get(position).getStatus();

        if(status != 0) {
            dbref = FirebaseDatabase.getInstance().getReference(KEY_USER);
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        if(user.getUserId().equals(userId)) {
                            String name = user.getFirstName() + " " + user.getLastName();
                            holder.txtViewRequestName.setText(name);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        holder.btnRequestApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref = FirebaseDatabase.getInstance().getReference(KEY_LEAVE_REQUEST_TABLE).child(userId);
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            LeaveRequestModel leaveRequest = dataSnapshot.getValue(LeaveRequestModel.class);
                            if(userId.equals(leaveRequest.getUserId())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put(KEY_STATUS, 2);
                                dbref.child(dataSnapshot.getKey().toString()).updateChildren(map);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.btnRequestDeny.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dbref = FirebaseDatabase.getInstance().getReference(KEY_LEAVE_REQUEST_TABLE).child(userId);
                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            LeaveRequestModel leaveRequest = dataSnapshot.getValue(LeaveRequestModel.class);
                            if(userId.equals(leaveRequest.getUserId())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put(KEY_STATUS, 3);
                                dbref.child(dataSnapshot.getKey().toString()).updateChildren(map);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class Myholder extends RecyclerView.ViewHolder {
        TextView txtViewRequestName;
        Button btnRequestApprove;
        Button btnRequestDeny;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            txtViewRequestName = itemView.findViewById(R.id.txtViewRequestName);
            btnRequestApprove = itemView.findViewById(R.id.btnRequestApprove);
            btnRequestDeny = itemView.findViewById(R.id.btnRequestDeny);
        }
    }

}
