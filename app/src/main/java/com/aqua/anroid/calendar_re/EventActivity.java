package com.aqua.anroid.calendar_re;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "10.0.2.2"; // AVD = 서버 같은 컴퓨터 동작
    private static String TAG = "calendartest";

    private EditText event_title;
    private TextView start_date_text;
    private ImageView start_date_btn;

    private TextView end_date_text;
    private ImageView end_date_btn;
    private Button event_save_btn;

    private EditText event_memo;

    private TextView mTextViewResult; //결과 보여줌

    private String startdate, enddate; // db 저장

    Calendar sCalendar = Calendar.getInstance();
    Calendar eCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        start_date_text = (TextView) findViewById(R.id.start_date_text);
        end_date_text = (TextView) findViewById(R.id.end_date_text);

        event_title = (EditText) findViewById(R.id.event_title);
        start_date_btn = (ImageView) findViewById(R.id.start_date_btn);
        end_date_btn = (ImageView) findViewById(R.id.end_date_btn);
        event_save_btn = (Button) findViewById(R.id.event_save_btn);

        event_memo = (EditText) findViewById(R.id.event_memo);

        mTextViewResult = (TextView) findViewById(R.id.result_text);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        makeevent();

    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EventActivity.this,
                    "저장중입니다...", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss(); //progressdialog 종료
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);

        }


        @Override
        protected String doInBackground(String... params) {

            String title = (String) params[1];
            String startdate = (String) params[2];
            String enddate = (String) params[3];
            String memo = (String) params[4];

            String serverURL = (String) params[0];
            String postParameters = "title=" + title + "&startdate=" + startdate + "&enddate=" + enddate + "&memo=" + memo;


            try {

                //HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);  //5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));//전송할 데이터가 저장된 변수를 이곳에 입력

                outputStream.flush();
                outputStream.close();

                //응답 읽기
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    //정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    //에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }


                //StringBUilder 사용하여 수신되는 데이터 저장
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                //저장된 데이터를 String으로 변환하여 리턴
                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

    private void makeevent() {
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

        //저장 버튼 클릭 시
        event_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = event_title.getText().toString();
                String memo = event_memo.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php", title, startdate, enddate, memo);


            }
        });
    }


    private void startLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        startdate = sdf.format(sCalendar.getTime());

        TextView start_date_text = (TextView) findViewById(R.id.start_date_text);
        start_date_text.setText(startdate);

        //startdate = start_date_text.getText().toString();

    }

    private void endLabel() {
        String myFormat = "yyyy-MM-dd";// 출력형식 2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        enddate = sdf.format(eCalendar.getTime());

        TextView end_date_text = (TextView) findViewById(R.id.end_date_text);
        end_date_text.setText(enddate);

        //enddate = end_date_text.getText().toString();

    }

}