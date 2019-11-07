package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RatingPayActivity extends AppCompatActivity {



    TextView tv_ratingPrice;
    TextView tv_sum;
    TextView tv_nonPay;
    TextView tv_rating_check;
    TextView tv_ratingPer_check;


    String rating;
    String ratingPer;
    int sumRatingPay;
    String ratingPay;
    int ratingPayInt;
//    int ratingPay;

    Connection connection;
    ResultSet resultRatingPay;

    private AsyncTask<String, String, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_pay);



        tv_ratingPrice = findViewById(R.id.tv_ratingPrice);
        tv_nonPay = findViewById(R.id.tv_nonpay);
        Spinner spin = findViewById(R.id.spin_rating);
        Spinner spin_per = findViewById(R.id.spin_per);
        tv_sum = findViewById(R.id.tv_sum);
        tv_rating_check = findViewById(R.id.tv_rating_check);
        tv_ratingPer_check = findViewById(R.id.tv_ratingper_check);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rating = (String) parent.getItemAtPosition(position);
                Toast.makeText(RatingPayActivity.this, rating, Toast.LENGTH_SHORT).show();
                mTask = new RatingPayActivity.MySyncTask().execute();
                tv_rating_check.setText(rating+"등급");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_per.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ratingPer = (String) parent.getItemAtPosition(position);
                int ratingPerInt = Integer.parseInt(ratingPer);
                tv_ratingPer_check.setText(ratingPerInt+"%");


                int sum = sumRatingPay/100*ratingPerInt;
                tv_nonPay.setText(sum+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            ratingQuary();
            return null;

        }

        protected void onPostExecute(String result) {
        }

        protected void onCancelled() {
            super.onCancelled();
        }

    }

    private void ratingQuary() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();
            resultRatingPay = statement.executeQuery("select * from Su_년도별금액 where 등급='" + rating + "'");



            while (resultRatingPay.next()) {

                ratingPay = resultRatingPay.getString(4);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_ratingPrice.setText(ratingPay + "");
                        ratingPayInt = Integer.parseInt(ratingPay);
                        sumRatingPay = ratingPayInt*30;
                        tv_sum.setText(sumRatingPay+"");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
