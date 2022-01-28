package com.aqua.anroid.calendar_re;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {

    private TextView start_date;
    private ImageView start_date_btn;

    private TextView end_date;
    private ImageView end_date_btn;

    Calendar sCalendar = Calendar.getInstance();
    Calendar eCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ImageView start_date_btn = (ImageView) findViewById(R.id.start_date_btn);
        ImageView end_date_btn = (ImageView) findViewById(R.id.end_date_btn);

        //시작 날짜 표시
        DatePickerDialog.OnDateSetListener myDatePicker1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                sCalendar.set(Calendar.YEAR, year);
                sCalendar.set(Calendar.MONTH, month);
                sCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startLabel();
            }
        };
        //종료 날짜 표시
        DatePickerDialog.OnDateSetListener myDatePicker2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                eCalendar.set(Calendar.YEAR, year);
                eCalendar.set(Calendar.MONTH, month);
                eCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endLabel();
            }
        };

        //시작 달력 버튼 클릭 시
        start_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventActivity.this,
                        myDatePicker1,
                        sCalendar.get(Calendar.YEAR),
                        sCalendar.get(Calendar.MONTH),
                        sCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //종료 달력 버튼 클릭 시
        end_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventActivity.this,
                        myDatePicker2,
                        eCalendar.get(Calendar.YEAR),
                        eCalendar.get(Calendar.MONTH),
                        eCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void startLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView start_date_text = (TextView) findViewById(R.id.start_date);
        start_date_text.setText(sdf.format(sCalendar.getTime()));
    }
    private void endLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        TextView end_date_text = (TextView) findViewById(R.id.end_date);
        end_date_text.setText(sdf.format(eCalendar.getTime())) ;
    }

}
