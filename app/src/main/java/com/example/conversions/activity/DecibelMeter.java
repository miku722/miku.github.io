package com.example.conversions.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conversions.R;

import java.io.IOException;

public class DecibelMeter extends AppCompatActivity {
    private static final String TAG = "DecibelMeter";
    private MediaRecorder mMediaRecorder = new MediaRecorder();
    private String filePath = "/sdcard/SoundMeter/new1";
    float decibel;
    public int WaitTime = 50;
    // when set degree to -130; the pointer points 0;
    float originalDegree = -130;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decibel_meter);
        startRecording();
        TextView numOfDecibel = findViewById(R.id.TV_num);
        ImageView pointer = findViewById(R.id.IV_pointer);
        pointer.setRotationX(pointer.getHeight()/2);
        pointer.setRotationY(pointer.getWidth()/2);
        getMaxAmplitude.start();


        Handler mTimeHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    numOfDecibel.setText(String.valueOf(decibel));
                    pointer.setRotation(originalDegree+decibel/10*15);
                    sendEmptyMessageDelayed(0, WaitTime);
                }
            }
        };
        mTimeHandler.sendEmptyMessageDelayed(0, WaitTime);



    }
    
    public DecibelMeter(){
//
    }

//
//    public int getCurrentDecibel(){
//        return decibel;
//    }

    public void startRecording(){
        if (mMediaRecorder == null){
            mMediaRecorder = new MediaRecorder();
        }
        try {
            //set some necessary things
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(filePath);
            //actions
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IllegalStateException | IOException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        }
    }


    Thread getMaxAmplitude = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (mMediaRecorder != null) {
                    double maxAmplitude = mMediaRecorder.getMaxAmplitude();
                    Log.d("amplitude", String.valueOf(maxAmplitude));
                    if (maxAmplitude > 1) {
                        decibel = (int) (20 * Math.log10(maxAmplitude));
                    }

                    Log.d("decibel", String.valueOf(decibel));
                    try {
                        Thread.sleep(WaitTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });
    
    
  
    public void stopRecording(){
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }
}