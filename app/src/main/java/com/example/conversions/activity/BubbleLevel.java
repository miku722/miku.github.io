package com.example.conversions.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BubbleLevel extends AppCompatActivity {
    private ImageButton On_off;
    private ImageView Bubble;
    private boolean is_on;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetic;
    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    //the core values for compass
    float[] OrientationValues = new float[3];
    //the matrix
    float[] R = new float[9];
    private float pitchAngle;
    private float rollAngle;
    MySensorEventListener mMySensorEventListener;
    private float fromXDelta;
    private float fromYDelta;
    private float toXDelta = 0;
    private float toYDelta = 0;
    private final float limitX = 270;
    private final float limitY = 270;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.conversions.R.layout.bubble_level);
        On_off = findViewById(com.example.conversions.R.id.On_off);
        Bubble = findViewById(com.example.conversions.R.id.bubble);
        is_on = false;
        On_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!is_on){
                    On_off.setImageResource(com.example.conversions.R.drawable.tools_level_lock_on);
                    is_on = true;
                }else {
                    On_off.setImageResource(com.example.conversions.R.drawable.tools_level_lock_off);
                    is_on = false;
                }
            }
        }); //instantiate sensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mMySensorEventListener = new MySensorEventListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register two sensor when activity about to start

        sensorManager.registerListener(mMySensorEventListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mMySensorEventListener,magnetic,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(mMySensorEventListener);
    }


    class MySensorEventListener implements SensorEventListener {


        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magnetometerValues = event.values.clone();
            }

            //The getRotationMatrix method needs the Magnetic Field and Accelerometer sensor values to compute the rotation matrix, which is named R;
            SensorManager.getRotationMatrix(R,null,accelerometerValues, magnetometerValues);
            //The core method to get the Azimuth values,which is values[];
            sensorManager.getOrientation(R, OrientationValues);
            pitchAngle = -(float) Math.toDegrees(OrientationValues[1]);
            rollAngle = (float) Math.toDegrees(OrientationValues[2]);
            toXDelta = rollAngle*6;
            toYDelta = pitchAngle*6;
            /*
            inorder to convert the rollAngle and pitchAngle to X and Y properly, first of all set the limitation of X and Y : test on my phone ;

             */
            if (Math.abs(toXDelta) >= limitX){
                toXDelta = limitX;
            }
            if (Math.abs(toYDelta) >= limitY){
                toYDelta = limitY;
            }
            TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta,toXDelta,fromYDelta,toYDelta);
            translateAnimation.setDuration(2000);
            Bubble.startAnimation(translateAnimation);
            fromYDelta = toYDelta;
            fromXDelta =toXDelta;
            Log.d("pitchAngle",String.valueOf(pitchAngle));
            Log.d("rollAngle",String.valueOf(rollAngle));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}

