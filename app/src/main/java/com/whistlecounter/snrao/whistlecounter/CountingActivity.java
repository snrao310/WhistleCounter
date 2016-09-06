package com.whistlecounter.snrao.whistlecounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CountingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        Intent intent=new Intent(this,ListeningToWhistleService.class);
        startService(intent);
    }



}
