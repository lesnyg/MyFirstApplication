package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    String dbname;
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
    private ResultSet resultSet;
    private Connection connection;
    private Bitmap bitmap;

    private AsyncTask<String, String, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing);

        mTask = new NursingActivity.MySyncTask().execute();

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
        blood = et_blood.getText().toString();
        pulse = et_pulse.getText().toString();
        heat = et_heat.getText().toString();
        breath = et_breath.getText().toString();
        glucose = et_glucose.getText().toString();
        weight = et_weight.getText().toString();
        condition = et_condition.getText().toString();
        meal = et_meal.getText().toString();
        evacuation = et_evacuation.getText().toString();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


            resultSet = statement.executeQuery("insert into Su_간호관리정보(수급자명,혈압1,맥박1,체온,호흡1,혈당1,체중,상태,식사,배설) values(name,blood,pulse,heat,breath,glucose,weight,condition,meal,evacuation)");
            while (resultSet.next()) {
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
