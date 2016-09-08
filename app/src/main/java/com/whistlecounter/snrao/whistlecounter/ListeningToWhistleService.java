package com.whistlecounter.snrao.whistlecounter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class ListeningToWhistleService extends Service {

    private final IBinder mIBinder = new LocalBinder();
    private Handler mHandler = null;
    MediaRecorder mRecorder;
    EventBus eventBus;
    WhistleEvent whistleEvent;

    public class LocalBinder extends Binder
    {
        public ListeningToWhistleService getInstance()
        {
            return ListeningToWhistleService.this;
        }
    }

    public void setHandler(Handler handler)
    {
        Toast.makeText(ListeningToWhistleService.this, "In Handler", Toast.LENGTH_SHORT).show();
        mHandler = handler;
        //sendMessageToMainActivity();
    }

    public ListeningToWhistleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String mFileName;
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/whistleSound.3gp";

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("LOGGER", "prepare() failed");
        }

        mRecorder.start();

        int startAmplitude = mRecorder.getMaxAmplitude();
        Log.d("Logger", "starting amplitude: " + startAmplitude);

        boolean clapDetected = false;
        int i = 0;

        do {
            Log.d("Logger", "waiting while recording...");
            waitSome();
            int finishAmplitude = mRecorder.getMaxAmplitude();
//            if (clipListener != null)
//            {
//                clipListener.heard(finishAmplitude);
//            }

            int ampDifference = finishAmplitude - startAmplitude;
            if (ampDifference >= 25000) {
                Log.d("LOGGER", "heard a clap!");
                clapDetected = true;
                i++;
                eventBus.post(whistleEvent);

            }
            Log.d("LOGGER", "finishing amplitude: " + finishAmplitude + " diff: "
                    + ampDifference);
        } while (i < 5);



        Log.d("LOGGER", "stopped recording");
        done();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void done()
    {
        Log.d("LOGGER", "stop recording");
        if (mRecorder != null)
        {
            mRecorder.stop();
            mRecorder.release();
        }
    }


    private void waitSome()
    {
        try
        {
            // wait a while
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            Log.d("LOGGER", "interrupted");
        }
    }

}
