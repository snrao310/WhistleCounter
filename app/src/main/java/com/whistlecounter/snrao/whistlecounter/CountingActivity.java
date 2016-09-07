package com.whistlecounter.snrao.whistlecounter;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

public class CountingActivity extends AppCompatActivity {

    public static Handler messageHandler = new MessageHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counting);

        Intent intent=new Intent(this,ListeningToWhistleService.class);
        intent.putExtra("MESSENGER", new Messenger(messageHandler));
        startService(intent);
    }


    public static class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            int state = message.arg1;

        }
    }



}
