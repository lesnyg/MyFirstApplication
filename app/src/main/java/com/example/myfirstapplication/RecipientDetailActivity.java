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

public class RecipientDetailActivity extends AppCompatActivity {
    private EditText et_name;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_date;
    private TextView tv_rating;
    private TextView tv_kounggam;
    private TextView tv_number;
    private TextView tv_gongdanper;
    private TextView tv_gongdanprice;
    private TextView tv_individualper;
    private TextView tv_individualprice;
    private TextView tv_sum;
    private ImageView img_person;

    private int personId;
    private String name;
    private String date;
    private String rating;
    private String kounggam;
    private String number;
    private String gongdanper;
    private String gongdanprice;
    private String individualper;
    private String individualprice;
    private String formattedGongPrice;
    private String formattedindividualPrice;
    private String formattedsum;
    private String gender;
    private String birth;
    private String pastdisease;
    private String responsibility;

    private String phoneNumber;

    private ResultSet resultSet;
    private ResultSet resultSetPhoto;
    private Connection connection;
    private Bitmap bitmap;

    private AsyncTask<String, String, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_detail);

        mTask = new RecipientDetailActivity.MySyncTask().execute();

        final Intent intent = getIntent();
        name = intent.getExtras().getString("name");

        et_name = findViewById(R.id.et_name);
        tv_name = findViewById(R.id.tv_name_result);
        tv_phone = findViewById(R.id.tv_phone_result);
        tv_date = findViewById(R.id.tv_date_result);
        tv_rating = findViewById(R.id.tv_rating_result);
        tv_kounggam = findViewById(R.id.tv_kounggam_result);
        tv_number = findViewById(R.id.tv_number_result);
        tv_gongdanper = findViewById(R.id.tv_gongdanper_result);
        tv_gongdanprice = findViewById(R.id.tv_gongdanprice_result);
        tv_individualper = findViewById(R.id.tv_individualper_result);
        tv_individualprice = findViewById(R.id.tv_individualprice_result);
        tv_sum = findViewById(R.id.tv_sum_result);
        img_person = findViewById(R.id.img_person);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipientDetailActivity.this, phoneNumber, Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + phoneNumber));
                if (intent1.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent1);
                }
            }
        });

        Button btn_nursing = findViewById(R.id.btn_nursing);
        btn_nursing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nursingIntent = new Intent(RecipientDetailActivity.this,ServiceContentsActivity.class);
                nursingIntent.putExtra("name", name);
                nursingIntent.putExtra("gender", gender);
                nursingIntent.putExtra("rating", rating);
                nursingIntent.putExtra("birth", birth);
                nursingIntent.putExtra("pastdisease", pastdisease);
                nursingIntent.putExtra("responsibility", responsibility);

                startActivity(nursingIntent);
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
            query();

            return null;

        }

        protected void onPostExecute(String result) {
        }

        protected void onCancelled() {
            super.onCancelled();
        }

    }

    public void query() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();


            resultSet = statement.executeQuery("select * from Su_수급자기본정보 where 수급자명='" + name + "'");
            while (resultSet.next()) {

                personId = Integer.parseInt(resultSet.getString(1));
                date = resultSet.getString(3);
                rating = resultSet.getString(4);
                kounggam = resultSet.getString(5);
                number = resultSet.getString(6);
                gongdanper = resultSet.getString(36);
                gongdanprice = resultSet.getString(37);
                individualper = resultSet.getString(38);
                individualprice = resultSet.getString(39);
                phoneNumber = resultSet.getString(20);
                gender = resultSet.getString(11);
                birth = resultSet.getString(22);
                pastdisease = resultSet.getString("과거병력");
                responsibility = resultSet.getString("담당");



                final int gongPrice = Integer.parseInt(gongdanprice);
                final int indiviPrice = Integer.parseInt(individualprice);
                final int sum = gongPrice + indiviPrice;


                DecimalFormat myFormatter = new DecimalFormat("###,###");
                formattedGongPrice = myFormatter.format(gongPrice);
                formattedindividualPrice = myFormatter.format(indiviPrice);
                formattedsum = myFormatter.format(sum);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_name.setText(name);
                        tv_phone.setText(phoneNumber);
                        tv_date.setText(date);
                        tv_rating.setText(rating);
                        tv_kounggam.setText(kounggam);
                        tv_number.setText(number);
                        tv_gongdanper.setText(gongdanper + "%");
                        tv_gongdanprice.setText(formattedGongPrice + " 원");
                        tv_individualper.setText(individualper + "%");
                        tv_individualprice.setText(formattedindividualPrice + " 원");
                        tv_sum.setText(formattedsum + " 원");

                    }
                });
            }

            resultSetPhoto = statement.executeQuery("select * from Su_사진 where Idno='" + personId + "'");
            byte b[];
            while (resultSetPhoto.next()) {
                Blob blob = resultSetPhoto.getBlob(4);
                b = blob.getBytes(1, (int) blob.length());
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_person.setImageBitmap(bitmap);

                    }
                });
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
