package com.example.myfirstapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NursingActivity extends AppCompatActivity {

    TextView tv_name;
    TextView tv_room;
    EditText et_blood;
    EditText et_pulse;
    EditText et_heat;
    EditText et_breath;
    EditText et_glucose;
    EditText et_weight;
    EditText et_condition;
    EditText et_meal;
    EditText et_evacuation;
    Button btn_send;

    String name;
    String currentDate;
    String startTime;
    String endTime;
    String room;
    String blood;
    String pulse;
    String heat;
    String breath;
    String glucose;
    String weight;
    String condition;
    String meal;
    String evacuation;
    String usingTime;
    private ResultSet resultSet;
    private Connection connection;
    private Bitmap bitmap;
    Date date;
    Date endDate;
    Date endTimeParse;
    Date startDate;

    private AsyncTask<String, String, String> mTask;
    SimpleDateFormat timeformatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        tv_name = findViewById(R.id.tv_name);
        tv_room = findViewById(R.id.tv_room);
        et_blood = findViewById(R.id.et_blood);
        et_pulse = findViewById(R.id.et_pulse);
        et_heat = findViewById(R.id.et_heat);
        et_breath = findViewById(R.id.et_breath);
        et_glucose = findViewById(R.id.et_glucose);
        et_weight = findViewById(R.id.et_weight);
        et_condition = findViewById(R.id.et_condition);
        et_meal = findViewById(R.id.et_meal);
        et_evacuation = findViewById(R.id.et_evacuation);
        btn_send = findViewById(R.id.btn_send);

        tv_name.setText(name);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        date = new Date();
        currentDate = formatter.format(date);
        timeformatter = new SimpleDateFormat("HH:mm:ss",Locale.KOREA);
        startTime = timeformatter.format(date);
        try {
            startDate = timeformatter.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blood = et_blood.getText().toString();
                pulse = et_pulse.getText().toString();
                heat = et_heat.getText().toString();
                breath = et_breath.getText().toString();
                glucose = et_glucose.getText().toString();
                weight = et_weight.getText().toString();
                condition = et_condition.getText().toString();
                meal = et_meal.getText().toString();
                evacuation = et_evacuation.getText().toString();


                mTask = new NursingActivity.MySyncTask().execute();
                endDate = new Date();
                try {
                    endTime = timeformatter.format(endDate);
                    endTimeParse = timeformatter.parse(timeformatter.format(endDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                d2 = timeformatter.parse(endDate);
                long diff = endTimeParse.getTime()-startDate.getTime();
                long diffSeconds = diff / 1000;
                long diffMinutes = diff / (60 * 1000);
                long diffHours = diff / (60 * 60 * 1000);

                usingTime = diffHours+":"+diffMinutes+":"+diffSeconds;

                Toast.makeText(NursingActivity.this, usingTime, Toast.LENGTH_SHORT).show();
            }
        });



    }

    public class MySyncTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isCancelled())
                return null;
            nursingquery();

            return null;

        }

        protected void onPostExecute(String result) {
        }

        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void nursingquery() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();


            resultSet = statement.executeQuery("insert into Su_간호관리정보(수급자명,일자,혈압1,맥박1,체온,호흡1,혈당1,체중,상태,식사,배설,간호시작시간,간호종료시간,간호처치시간) " +
                    "values('" + name + "','"+currentDate+"','" + blood + "','" + pulse + "','" + heat + "','" + breath + "','" + glucose + "','" + weight + "','" + condition + "','" + meal + "','" + evacuation + "','"+startTime+"','"+endTime+"','"+usingTime+"')");
            while (resultSet.next()) {
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
