package com.example.workflow.fragments;

import static com.example.workflow.utils.ConstantUtils.FORMAT_CALENDAR_EVENT;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.workflow.Adapter.AttendanceAdapter;
import com.example.workflow.R;
import com.example.workflow.database.OfflineDatabase;
import com.example.workflow.models.AttendanceModel;
import com.example.workflow.utils.CommonFunc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment{
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userId;
    List<Calendar> days;
    List<EventDay> events;
    CalendarView calendarView;
    TextView txtViewWorkSchedule;
    Locale locale = new Locale("en", "CA");

    private ArrayList<AttendanceModel> attendanceModelArrayList = new ArrayList<>();
    // meeting adapter
    private AttendanceAdapter attendanceAdapter = null;
    private AttendanceModel attendanceModelGlobal = null;
    private OfflineDatabase offlineDatabase = null;

    String strDate;
    View view;
    public CalendarFragment() {
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
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        //TEST
        calendar.set(2023, 10,16);
        days = new ArrayList<>();
        days.add(calendar);
        EventDay event = new EventDay(calendar, null, Color.parseColor("#228B22"));
        events = new ArrayList<>();
        events.add(event);
        offlineDatabase = new OfflineDatabase(getActivity());
        calendarView.setHighlightedDays(days);
        calendarView.setEvents(events);
        //End test
        strDate = CommonFunc.getTodayDate();
        displayAttendanceList(offlineDatabase.getAttendanceDetails(strDate));

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                String event = String.valueOf(calendar.getTimeInMillis());
                Date date = new Date(calendar.getTimeInMillis());
                DateFormat df = new SimpleDateFormat(FORMAT_CALENDAR_EVENT);
                strDate = df.format(date);


                displayAttendanceList(offlineDatabase.getAttendanceDetails(strDate));
            }
        });

        ((SwipeRefreshLayout) view.findViewById(R.id.activity_attendance_swipe_referesh_id)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((SwipeRefreshLayout) view.findViewById(R.id.activity_attendance_swipe_referesh_id)).setRefreshing(false);
                displayAttendanceList(offlineDatabase.getAttendanceDetails(strDate));

            }
        });

        return view;
    }


    public void displayAttendanceList(ArrayList<AttendanceModel> attendanceModels) {

        attendanceModelArrayList.clear();
        attendanceModelArrayList.add(null);
        if(attendanceModels.size()>0) {
            for (int i = 0; i < attendanceModels.size(); i++) {

                attendanceModelArrayList.add(attendanceModels.get(i));
            }
            attendanceAdapter = new AttendanceAdapter(getActivity(), attendanceModelArrayList,
                    (AppCompatActivity) getActivity(), view.findViewById(R.id.attendance_list_scrollview));
            ((RecyclerView) view.findViewById(R.id.activity_attendance_recycleview_id)).setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false));
            ((RecyclerView) view.findViewById(R.id.activity_attendance_recycleview_id)).setAdapter(attendanceAdapter);
            ////////////////////////////////////////////////
            if (attendanceModels.isEmpty()) {
                view.findViewById(R.id.activity_attendance_no_data_found).setVisibility(View.VISIBLE);
                ((RecyclerView) view.findViewById(R.id.activity_attendance_recycleview_id)).setVisibility(View.GONE);

            } else {
                view.findViewById(R.id.activity_attendance_no_data_found).setVisibility(View.GONE);
                ((RecyclerView) view.findViewById(R.id.activity_attendance_recycleview_id)).setVisibility(View.VISIBLE);

            }
        }
        else{
            attendanceModelArrayList.clear();
            ((RecyclerView) view.findViewById(R.id.activity_attendance_recycleview_id)).setVisibility(View.GONE);
            view.findViewById(R.id.activity_attendance_no_data_found).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        displayAttendanceList(offlineDatabase.getAttendanceDetails(strDate));

    }


}