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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NursingActivity extends AppCompatActivity {

    TextView tv_name;
    EditText et_room;
    EditText et_blood;
    EditText et_blood2;
    EditText et_pulse;
    EditText et_pulse2;
    EditText et_heat;
    EditText et_breath;
    EditText et_breath2;
    EditText et_glucose;
    EditText et_glucose2;
    EditText et_weight;
    EditText et_condition;
    EditText et_meal;
    EditText et_evacuation;
    EditText et_behavior;
    EditText et_fecesUrine;
    EditText et_delirium;
    EditText et_fall;
    EditText et_bedsore;
    EditText et_bodyarea;
    EditText et_pain;
    EditText et_dehydrated;
    EditText et_content;
    Button btn_send;

    String name;        //이름
    String currentDate;//오늘날짜
    String startTime;   //간호시작시간
    String endTime;     //간호종료시간
    String room;        //병실
    String blood;       //혈압1
    String blood2;      //혈압2
    String pulse;       //맥박1
    String pulse2;      //맥박2
    String heat;        //체온
    String breath;      //호흡1
    String breath2;     //호흡2
    String glucose;     //혈당1
    String glucose2;    //혈당2
    String weight;      //체중
    String condition;      //상태
    String meal;            //식사
    String evacuation;      //배설(횟수?)
    String behavior;        //문제행동
    String fecesUrine;      //소대변(상태?)
    String delirium;        //섬망
    String fall;            //낙상
    String bedsore;         //욕창
    String bodyarea;        //부위
    String pain;            //통증
    String dehydrated;      //탈수
    String content;         //내용
    String usingTime;       //간호처치시간
    private ResultSet resultSet;
    private Connection connection;
    private Bitmap bitmap;
    Date date;
    Date endDate;
    Date endTimeParse;
    Date startDate;
    Date dateUsingTime;

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
        et_room = findViewById(R.id.et_room);
        et_blood = findViewById(R.id.et_blood);
        et_blood2 = findViewById(R.id.et_blood2);
        et_pulse = findViewById(R.id.et_pulse);
        et_pulse2 = findViewById(R.id.et_pulse2);
        et_heat = findViewById(R.id.et_heat);
        et_breath = findViewById(R.id.et_breath);
        et_breath2 = findViewById(R.id.et_breath2);
        et_glucose = findViewById(R.id.et_glucose);
        et_glucose2 = findViewById(R.id.et_glucose2);
        et_weight = findViewById(R.id.et_weight);
        et_condition = findViewById(R.id.et_condition);
        et_meal = findViewById(R.id.et_meal);
        et_evacuation = findViewById(R.id.et_evacuation);
        et_behavior = findViewById(R.id.et_evacuation);
        et_fecesUrine = findViewById(R.id.et_evacuation);
        et_delirium = findViewById(R.id.et_evacuation);
        et_fall = findViewById(R.id.et_evacuation);
        et_bedsore = findViewById(R.id.et_evacuation);
        et_bodyarea = findViewById(R.id.et_evacuation);
        et_pain = findViewById(R.id.et_evacuation);
        et_dehydrated = findViewById(R.id.et_evacuation);
        et_content = findViewById(R.id.et_evacuation);
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
                room = et_room.getText().toString();
                blood = et_blood.getText().toString();
                blood2 = et_blood2.getText().toString();
                pulse = et_pulse.getText().toString();
                pulse2 = et_pulse2.getText().toString();
                heat = et_heat.getText().toString();
                breath = et_breath.getText().toString();
                breath2 = et_breath2.getText().toString();
                glucose = et_glucose.getText().toString();
                glucose2 = et_glucose2.getText().toString();
                weight = et_weight.getText().toString();
                condition = et_condition.getText().toString();
                meal = et_meal.getText().toString();
                evacuation = et_evacuation.getText().toString();
                fecesUrine = et_fecesUrine.getText().toString();
                delirium = et_delirium.getText().toString();
                fall = et_fall.getText().toString();
                bedsore = et_bedsore.getText().toString();
                bodyarea = et_bodyarea.getText().toString();
                pain = et_pain.getText().toString();
                dehydrated = et_dehydrated.getText().toString();
                content = et_content.getText().toString();


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
                usingTime = timeformatter.format(diff);

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

            resultSet = statement.executeQuery("insert into Su_간호관리정보(수급자명,일자,병실,혈압1,혈압2,맥박1,맥박2,체온,호흡1,호흡2,혈당1,혈당2,체중,상태,식사,배설,간호시작시간,간호종료시간,간호처치시간," +
                    "문제행동,소대변,섬망,낙상,욕창,부위,통증,탈수,내용) " +
                    "values('" + name + "','"+currentDate+"','"+room+"','" + blood + "','" + blood2 + "','" + pulse + "','" + pulse2 + "','" + heat + "','" + breath + "','" + breath2 + "','" + glucose + "','" + glucose2 + "'," +
                    "'" + weight + "','" + condition + "','" + meal + "','" + evacuation + "','"+startTime+"','"+endTime+"','"+usingTime+"'," +
                    "'"+behavior+"','"+fecesUrine+"','"+delirium+"','"+fall+"','"+bedsore+"','"+bodyarea+"','"+pain+"','"+dehydrated+"','"+content+"',)");
            while (resultSet.next()) {
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
