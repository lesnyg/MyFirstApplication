package com.example.myfirstapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceContentsActivity extends AppCompatActivity {
    private String name;
    private String date;
    private int serviceId;
    private List<String> serviceList;
    private ServiceAdapter mServiceAdapter;
    private RecyclerView recyclerView;
    private Recipient recipient;
    private AsyncTask<String, String, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_contents);

        mTask = new ServiceSyncTask().execute();
        recipient = new Recipient();
//        name = recipient.getName();
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");

        recyclerView = findViewById(R.id.recyclerview_servicelist);
        mServiceAdapter = new ServiceAdapter();


    }

    public class ServiceSyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }

            servicelistQuery();
            return null;
        }
    }

    public void servicelistQuery() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:jtds:sqlserver://222.122.213.216/mashw08", "mashw08", "msts0850op");
            Statement statement = connection.createStatement();
            ResultSet serviceResultSetlist = statement.executeQuery("select * from Su_방문요양급여정보 where 수급자명='" + name + "'");
            serviceList = new ArrayList<>();
            while (serviceResultSetlist.next()) {
                date = serviceResultSetlist.getString(2);
                serviceId = serviceResultSetlist.getInt(1);

                serviceList.add(date);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mServiceAdapter = new ServiceAdapter(new ServiceAdapter.ServiceClickListener() {
                    @Override
                    public void setServiceClick(String serviceDate) {
                        Intent intent = new Intent(ServiceContentsActivity.this, ServiceDatailActivity.class);
                        intent.putExtra("id", serviceId);
                        startActivity(intent);
                    }
                });


                mServiceAdapter.setItems(serviceList);
                recyclerView.setAdapter(mServiceAdapter);
            }
        });

    }

}

