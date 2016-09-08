package com.whistlecounter.snrao.whistlecounter;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

public class ListeningToWhistleService extends Service {

    MediaRecorder mRecorder;
    private Messenger messageHandler;
    Intent intent;

    public ListeningToWhistleService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent=intent;
        startRecording();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startRecording() {
        super.onCreate();
        String mFileName;
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/whistleSound.3gp";

        mRecorder = new MediaRecorder();
        if(checkPermission("android.permission.RECORD_AUDIO",1,0)== PackageManager.PERMISSION_GRANTED) {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }

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
                sendMessage(i);

            }
            Log.d("LOGGER", "finishing amplitude: " + finishAmplitude + " diff: "
                    + ampDifference);
        } while (i < 5);

        

        Log.d("LOGGER", "stopped recording");
        done();
        stopSelf();
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


    public void sendMessage(int i) {
        Message message = Message.obtain();
        message.arg1=i;

        try {
            messageHandler.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
