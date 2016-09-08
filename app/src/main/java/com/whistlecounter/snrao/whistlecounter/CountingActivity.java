package com.whistlecounter.snrao.whistlecounter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class CountingActivity extends AppCompatActivity {

    int aResponse;
    ListeningToWhistleService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);


        final Handler handler;

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //msg.arg1
                aResponse = msg.getData().getInt("message");
                TextView t=(TextView)findViewById(R.id.numberCount);
                t.setText(Integer.toString(aResponse));

            }
        };


        Intent serviceIntent;
        serviceIntent = new Intent(this, ListeningToWhistleService.class);
        startService(serviceIntent);
        ServiceConnection serve = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Toast.makeText(getApplicationContext(), "In Service Connect", Toast.LENGTH_LONG).show();
                mService = ((ListeningToWhistleService.LocalBinder) service).getInstance();
                mService.setHandler(handler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(serviceIntent,serve, Context.BIND_AUTO_CREATE);
    }
}
