package com.aqua.anroid.calendar_re;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final String TAG = "calendar test";
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    private SimpleDateFormat dateFormatForMonth2 = new SimpleDateFormat("yyyy-MM-d일", Locale.KOREA);

    private TextView textView_month;
    private ImageButton button_add_event;
    private ImageButton event_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.calendar_view);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY); // 첫 번째 요일을 월요일로 설정
        textView_month = (TextView) findViewById(R.id.textView_month); //월 표시 - 2022년 1월
        textView_month.setText(dateFormatForDisplaying.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        button_add_event = (ImageButton) findViewById(R.id.button_add_event);
        event_loading = (ImageButton) findViewById(R.id.event_loading);

        // 일정 추가 버튼 클릭
        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
            }
        });

        event_loading.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = getIntent();
                String start = j.getStringExtra("start");
                String end = j.getStringExtra("end");

                Date trans_date1 = null;
                Date trans_date2 = null;

                try {
                    trans_date1 = dateFormatForDisplaying.parse(start);
                    trans_date2 = dateFormatForDisplaying.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long time1 = trans_date1.getTime();
                long time2 = trans_date2.getTime();

                // Add event
                Event event1 = new Event(Color.GREEN, time1, "이벤트1");
                Event event2 = new Event(Color.GREEN, time2, "이벤트2");

                compactCalendarView.addEvent(event1);
                compactCalendarView.addEvent(event2);

            }
        });
    }

}