package com.example.myfirstapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText et_name;
    Button btn_check;
    Button btn_ratingPay;
    String name;
    String indiviPay;
    String rating;
    private RecipientAdapter mAdapter;
    List<Recipient> list;
    private AsyncTask<String, String, String> mTask;
    RecyclerView recyclerView;
    String personId;
    Bitmap bitmap;
    Connection connection;
    ResultSet resultSetlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTask = new MySyncTask().execute();

        et_name = findViewById(R.id.et_name);
        btn_check = findViewById(R.id.btn_check);
        recyclerView = findViewById(R.id.recyclerview);
//        btn_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = et_name.getText().toString();
//                Intent intent = new Intent(MainActivity.this, RecipientDetailActivity.class);
//                intent.putExtra("name", name);
//                startActivity(intent);
//
//
//                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
//            }
//        });

        btn_ratingPay = findViewById(R.id.btn_ratingPay);
        btn_ratingPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RatingPayActivity.class);
                startActivity(intent);
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
            listQuery();
            return null;

        }

        protected void onPostExecute(String result) {
        }

        protected void onCancelled() {
            super.onCancelled();
        }

    }


    public void listQuery() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();
            resultSetlist = statement.executeQuery("select * from Su_수급자기본정보 left join Su_사진 on Su_수급자기본정보.id=Su_사진.Idno");
            byte bt[];
            list = new ArrayList<>();
            while (resultSetlist.next()) {
                personId = resultSetlist.getString("Idno");
                name = resultSetlist.getString(2);
                indiviPay = resultSetlist.getString(39);
                rating = resultSetlist.getString(4);

                Blob bloblist = resultSetlist.getBlob("BLOBData");
                if(bloblist != null) {
                    bt = bloblist.getBytes(1, (int) bloblist.length());
                    bitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                }else {
                    bitmap = null;
                }
                list.add(new Recipient(bitmap, name, indiviPay, rating));

            }
//            ResultSet resultSetsumnail = statement.executeQuery("select * from Su_사진 where Idno = '"+personId+"'");
//            byte b[];
//            while (resultSetsumnail.next()) {
//                Blob blob = resultSetsumnail.getBlob(4);
//                b = blob.getBytes(1, (int) blob.length());
//                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
//                String photoId = resultSet.getString(1);
//
//            }
        connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new RecipientAdapter(new RecipientAdapter.OnRecipientClickListener() {
                    @Override
                    public void onRecipientClick(Recipient recipient) {
                        Intent intent = new Intent(MainActivity.this, RecipientDetailActivity.class);
                        intent.putExtra("name", recipient.getName());
                        startActivity(intent);
                    }


                });
                mAdapter.setitems(list);
                recyclerView.setAdapter(mAdapter);
            }
        });
    }
}