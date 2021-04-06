package com.example.conversions.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class Compass extends AppCompatActivity{

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetic;
    private TextView valueOfDegree;
    private ImageView pointer;
    private float[] accelerometerValues = new float[3];
    private float[] magnetometerValues = new float[3];
    //the core values for compass
    float[] OrientationValues = new float[3];
    //the matrix
    float[] R = new float[9];
    float rotateDegree = 0.1f;
    float lastRotateDegree = 0;
    private  MySensorEventListener mMySensorEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.conversions.R.layout.compass);

        pointer = findViewById(com.example.conversions.R.id.compassPointer);
        valueOfDegree = findViewById(com.example.conversions.R.id.azimuthAngle);

        //instantiate sensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //init two sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //set rotation center
        pointer.setRotationX(pointer.getHeight()/2);
        pointer.setRotationY(pointer.getWidth()/2);
        mMySensorEventListener = new MySensorEventListener();
        //

    }
    //onResume is the last step to running a activity
    @Override
    protected void onResume() {
        super.onResume();
        // register two sensor when activity about to start
        sensorManager.registerListener(mMySensorEventListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mMySensorEventListener,magnetic,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(mMySensorEventListener);
        super.onPause();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
            sensorManager.getRotationMatrix(R,null,accelerometerValues, magnetometerValues);
            //The core method to get the Azimuth values,which is values[];
            sensorManager.getOrientation(R, OrientationValues);
            Log.d("Main","values[0] :"+Math.toDegrees(OrientationValues[0]));
            rotateDegree = -(float) Math.toDegrees(OrientationValues[0]);
            valueOfDegree.setText(String.valueOf(rotateDegree));
            if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
                Log.d("Main","lastRotateDegree= "+lastRotateDegree +"rotatedegree = "+rotateDegree);
                RotateAnimation animation = new RotateAnimation(lastRotateDegree,rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);
                animation.setFillEnabled(true);
                pointer.startAnimation(animation);
                animation.setDuration(1000);
                lastRotateDegree = (float) rotateDegree;
                Log.d("xuanzhaun","rotating!");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


}
