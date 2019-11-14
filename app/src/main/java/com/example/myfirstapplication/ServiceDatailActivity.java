package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class ServiceDatailActivity extends AppCompatActivity {
    private TextView tv_date;
    private TextView tv_physicalactivity;
    private TextView tv_everydaylife;
    private TextView tv_individualactivity;
    private TextView tv_feeling;
    private TextView tv_bodyfunction;
    private TextView tv_meal;
    private TextView tv_recognition;
    private TextView tv_excrement;
    private TextView tv_urine;
    private TextView tv_specialnote;


    private String date;
    private String physicalactivity;
    private String everydaylife;
    private String individualactivity;
    private String feeling;
    private String bodyfunction;
    private String meal;
    private String recognition;
    private String excrement;
    private String urine;
    private String specialnote;
    private int id;

    private ResultSet resultSet;
    private Connection connection;

    private AsyncTask<String, String, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_datail);

        mTask = new MySyncTask().execute();

        final Intent intent = getIntent();
        id = intent.getExtras().getInt("id");


        tv_date = findViewById(R.id.tv_date);
        tv_physicalactivity = findViewById(R.id.tv_physicalactivity);
        tv_everydaylife = findViewById(R.id.tv_everydaylife);
        tv_individualactivity = findViewById(R.id.tv_individualactivity);
        tv_feeling = findViewById(R.id.tv_feeling);
        tv_bodyfunction = findViewById(R.id.tv_bodyfunction);
        tv_meal = findViewById(R.id.tv_meal);
        tv_recognition = findViewById(R.id.tv_recognition);
        tv_excrement = findViewById(R.id.tv_excrement);
        tv_urine = findViewById(R.id.tv_urine);
        tv_specialnote = findViewById(R.id.tv_specialnote);

    }

    public class MySyncTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isCancelled())
                return null;
            servicequery();

            return null;

        }

        protected void onPostExecute(String result) {
        }

        protected void onCancelled() {
            super.onCancelled();
        }

    }

    public void servicequery() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();


            resultSet = statement.executeQuery("select * from Su_방문요양급여정보 where id='" + id + "'");
            while (resultSet.next()) {
                date = resultSet.getString(2);
                physicalactivity = resultSet.getString("신체사용시간계");
                everydaylife = resultSet.getString("생활지원사용시간계");
                individualactivity = resultSet.getString("인지사용시간계");
                feeling = resultSet.getString("정서사용시간계");
                bodyfunction = resultSet.getString("신체기능");
                meal = resultSet.getString("식사기능");
                recognition = resultSet.getString("인지기능");
                excrement = resultSet.getString("대변횟수");
                urine = resultSet.getString("소변횟수");
                specialnote = resultSet.getString("특이사항");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_date.setText(date);
                        tv_physicalactivity.setText(physicalactivity);
                        tv_everydaylife.setText(everydaylife);
                        tv_individualactivity.setText(individualactivity);
                        tv_feeling.setText(feeling);
                        tv_bodyfunction.setText(bodyfunction);
                        tv_meal.setText(meal);
                        tv_recognition.setText(recognition);
                        tv_excrement.setText(excrement);
                        tv_urine.setText(urine);
                        tv_specialnote.setText(specialnote);


                    }
                });
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
