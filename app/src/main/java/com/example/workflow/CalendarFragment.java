package com.example.workflow;

import static com.example.workflow.utils.ConstantUtils.FORMAT_CALENDAR_EVENT;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String userId;
    List<Calendar> days;
    List<EventDay> events;
    CalendarView calendarView;
    TextView txtViewWorkSchedule;
    Locale locale = new Locale("en", "CA");

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
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        txtViewWorkSchedule = view.findViewById(R.id.txtViewWorkSchedule);
        Calendar calendar = Calendar.getInstance();
        //TEST
        calendar.set(2023, 10,16);
        days = new ArrayList<>();
        days.add(calendar);
        EventDay event = new EventDay(calendar, null, Color.parseColor("#228B22"));
        events = new ArrayList<>();
        events.add(event);

        calendarView.setHighlightedDays(days);
        calendarView.setEvents(events);
        //End test

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                String event = String.valueOf(calendar.getTimeInMillis());
                Date date = new Date(calendar.getTimeInMillis());
                DateFormat df = new SimpleDateFormat(FORMAT_CALENDAR_EVENT);
                String strDate = df.format(date);

                txtViewWorkSchedule.setText(strDate);
            }
        });

        return view;
    }
}