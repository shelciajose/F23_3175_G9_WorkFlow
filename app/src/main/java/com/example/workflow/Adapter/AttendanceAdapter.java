package com.example.workflow.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workflow.R;
import com.example.workflow.models.AttendanceModel;
import com.example.workflow.utils.CommonFunc;

/*
Created by Varsha Dhoni on 07-09-2020.
*/
public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context=null;
    private ArrayList<AttendanceModel> attendanceModels=null;
    private AppCompatActivity appCompatActivity=null;
    private View viewTop =null;
    ///////////////////////////
    private int VIEW_TYPE_TITLE=1;
    private int VIEW_TYPE_DATA=2;

    public AttendanceAdapter(Context context, ArrayList<AttendanceModel> attendanceModels, AppCompatActivity appCompatActivity, View viewTop) {
        this.context = context;
        this.attendanceModels = attendanceModels;
        this.appCompatActivity = appCompatActivity;
        this.viewTop = viewTop;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType==VIEW_TYPE_TITLE)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.attendance_details_top,parent,false);
            return new AttendanceAdapter.HolderMeetingTopRow(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.attendance_details_row,parent,false);
            return new AttendanceAdapter.HolderMeetingDataRow(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof AttendanceAdapter.HolderMeetingDataRow)
        {
            ((HolderMeetingDataRow) holder).textViewDate.setText(""+attendanceModels.get(holder.getAdapterPosition()).getAttendanceDate());
            if(!attendanceModels.get(holder.getAdapterPosition()).getCheckintime().equals("NA")) {
                ((HolderMeetingDataRow) holder).textViewCheckinTime.setText(CommonFunc.convertTimestampToTime(attendanceModels.get(holder.getAdapterPosition()).getCheckintime()));
            }
            else{
                ((HolderMeetingDataRow) holder).textViewCheckinTime.setText(attendanceModels.get(holder.getAdapterPosition()).getCheckintime());

            }
            ((HolderMeetingDataRow) holder).textViewCheckinLoc.setText(""+attendanceModels.get(holder.getAdapterPosition()).getCheckinlocation());
            if(!attendanceModels.get(holder.getAdapterPosition()).getCheckouttime().equals("NA")) {
                ((HolderMeetingDataRow) holder).textViewCheckoutTime.setText(CommonFunc.convertTimestampToTime(attendanceModels.get(holder.getAdapterPosition()).getCheckouttime()));
            }
            else {
                ((HolderMeetingDataRow) holder).textViewCheckoutTime.setText(attendanceModels.get(holder.getAdapterPosition()).getCheckouttime());
            }
            ((HolderMeetingDataRow) holder).textViewCheckoutLoc.setText(""+attendanceModels.get(holder.getAdapterPosition()).getCheckoutlocation());
        }
    }

    @Override
    public int getItemCount() {
        return attendanceModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (attendanceModels.get(position)==null)
        {
            return VIEW_TYPE_TITLE;
        }else {
            return VIEW_TYPE_DATA;
        }
    }

    /////////////////////////////////////////////////////////////////////////////// meeting to row layout

    private class HolderMeetingTopRow extends RecyclerView.ViewHolder
    {

        HolderMeetingTopRow(View itemView) {
            super(itemView);
        }
    }

    private class HolderMeetingDataRow extends RecyclerView.ViewHolder
    {
        AppCompatTextView textViewDate=null,textViewCheckinTime=null,textViewCheckinLoc=null,textViewCheckoutTime=null,textViewCheckoutLoc=null;
        LinearLayoutCompat layoutCompatClick=null;
        public HolderMeetingDataRow(View itemView) {
            super(itemView);
            this.textViewDate=itemView.findViewById(R.id.attendance_date);
            this.textViewCheckinTime=itemView.findViewById(R.id.checkin_time);
            this.textViewCheckoutTime=itemView.findViewById(R.id.checkout_time);
            this.textViewCheckinLoc=itemView.findViewById(R.id.checkin_location);
            this.textViewCheckoutLoc=itemView.findViewById(R.id.checkout_location);
        }
    }
}

