package com.whistlecounter.snrao.whistlecounter;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.musicg.wave.Wave;

import java.io.IOException;

public class ListeningToWhistleService extends Service {

    MediaRecorder mRecorder;
    private Messenger messageHandler;

    public ListeningToWhistleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
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

                Bundle extras = intent.getExtras();
                messageHandler = (Messenger) extras.get("MESSENGER");
                sendMessage(ProgressBarState.SHOW);

            }
            Log.d("LOGGER", "finishing amplitude: " + finishAmplitude + " diff: "
                    + ampDifference);
        } while (i >= 5);

        

        Log.d("LOGGER", "stopped recording");
        done();
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
