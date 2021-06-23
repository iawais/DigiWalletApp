package com.studios.digiwallet.Activities;

import android.os.Bundle;

import com.studios.digiwallet.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.studios.digiwallet.MyApplication.statusBarColor;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        statusBarColor(this);
    }

    private void init() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar9);
        setSupportActionBar(myToolbar);
        setTitle("");

        rv = findViewById(R.id.rvTransactions_History);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);


    }
}