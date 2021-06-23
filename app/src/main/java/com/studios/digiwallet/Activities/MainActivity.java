package com.studios.digiwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.studios.digiwallet.R;

import androidx.appcompat.app.AppCompatActivity;

import static com.studios.digiwallet.MyApplication.firebase;
import static com.studios.digiwallet.MyApplication.readAll;
import static com.studios.digiwallet.MyApplication.statusBarColor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusBarColor(this);
        FirebaseApp.initializeApp(this);
        firebase = FirebaseDatabase.getInstance().getReference().child("Users");

        readAll();

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(2*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),LoginActivity.class);
                    Looper.prepare();
                    startActivity(i);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                    Log.d("Welcome Screen Error", e.toString());
                }
            }
        };
        // start thread
        background.start();

    }
}