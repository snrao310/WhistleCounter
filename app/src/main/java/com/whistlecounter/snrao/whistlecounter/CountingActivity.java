package com.whistlecounter.snrao.whistlecounter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class CountingActivity extends AppCompatActivity {

    public Handler messageHandler = new MessageHandler();
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        MediaRecorder mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        textView=(TextView) findViewById(R.id.number);
        Intent intent=new Intent(this,ListeningToWhistleService.class);
        intent.putExtra("MESSENGER", new Messenger(messageHandler));
        startService(intent);
    }


    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            int i = message.arg1;
            textView.setText(i);
        }
    }



}
