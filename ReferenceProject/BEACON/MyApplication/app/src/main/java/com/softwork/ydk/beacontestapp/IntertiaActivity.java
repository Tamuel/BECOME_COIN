package com.softwork.ydk.beacontestapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.Beacon.BeaconRangingListener;

import java.util.Timer;

public class IntertiaActivity extends AppCompatActivity {
    private RelativeLayout canvasView;
    private TextView sensorView;
    private TextView speedView;

    private SensorManager sensorManager;

    private Sensor gravitySensor;
    private Sensor gyroSensor;
    private Sensor accelSensor;
    private Sensor magneticSensor;

    private final int aX = 0, aY = 1, aZ = 2;

    private float gravity[] = new float[3];
    private float gyro[] = new float[3];
    private float accel[] = new float[3];
    private float magnetic[] = new float[3];
    private float orientation[] = new float[3];

    private float rotationMat[] = new float[9];

    private float correctPos[] = {0, 0};

    private long s_time = 0, e_time;
    private float cur_accel = 0.0f, e_accel = 0.0f;
    private float speed = 0.0f;

    private float accelSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intertia);

        canvasView = (RelativeLayout) findViewById(R.id.MapCanvasView);
        sensorView = (TextView) findViewById(R.id.GetSensorDataTextView);
        speedView = (TextView) findViewById(R.id.GetSpeedTextView);
        canvasView.addView(new CanvasView(this));


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected class CanvasView extends View implements View.OnTouchListener{
        private int directionBarSize = 200;
        private int directionCircleSize = 10;
        private int directionPos[] = {100, 500};
        private int gravityPos[] = {300, 500};
        private int gyroPos[] = {500, 500};
        private int compassPos[] = {100, 800};
        private int compassSize = 50;
        private int compassCircleSize = 7;


        private float points[] = new float[4];

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            correctPos[0] = event.getX();
            correctPos[1] = event.getY();
            return false;
        }

        public CanvasView(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setTextSize(50);
            paint.setStyle(Paint.Style.FILL);

            paint.setStrokeWidth(2);

            // Compass North
            points[0] = compassPos[aX] - (float)Math.sin(orientation[0]) * compassSize;
            points[1] = compassPos[aY] - (float)Math.cos(orientation[0]) * compassSize;

            // Compass South
            points[2] = compassPos[aX] + (float)Math.sin(orientation[0]) * compassSize;
            points[3] = compassPos[aY] + (float)Math.cos(orientation[0]) * compassSize;

            paint.setColor(Color.RED);
            canvas.drawCircle(
                    points[0],
                    points[1],
                    compassCircleSize,
                    paint
            );

            canvas.drawCircle(
                    correctPos[0],
                    correctPos[1],
                    15,
                    paint
            );

            paint.setColor(Color.BLUE);
            canvas.drawCircle(
                    points[2],
                    points[3],
                    compassCircleSize,
                    paint
            );

            paint.setColor(Color.GRAY);
            canvas.drawCircle(
                    compassPos[aX],
                    compassPos[aY],
                    compassCircleSize,
                    paint
            );

            invalidate();
        }
    }


    public SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch(event.sensor.getType())
            {
                case Sensor.TYPE_GRAVITY:
                    gravity = event.values.clone();
                    break;

                case Sensor.TYPE_GYROSCOPE:
                    gyro = event.values.clone();
                    break;

                case Sensor.TYPE_ACCELEROMETER:
                    if(s_time == 0) {
                        s_time = System.currentTimeMillis();
                    }
                    e_time = System.currentTimeMillis();
                    accel = event.values.clone();
                    accelSize = (float) (
                            Math.sqrt(
                                    Math.pow((accel[aX] - gravity[aX]) * Math.abs(9.8035f - gravity[aX]) / 9.8035f, 2) +
                                    Math.pow((accel[aY] - gravity[aY]) * Math.abs(9.8035f - gravity[aY]) / 9.8035f, 2) +
                                    Math.pow((accel[aZ] - gravity[aZ]) * Math.abs(9.8035f - gravity[aZ]) / 9.8035f, 2)
                            )
                    );
                    if(Float.isNaN(accelSize))
                        accelSize = 0.0f;

                    if(e_accel == 0.0f)
                        e_accel = accelSize;

                    cur_accel = accelSize;

                    speed += (e_time - s_time) / 10 * (cur_accel - e_accel);

                    e_accel = cur_accel;
                    s_time = e_time;
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic = event.values.clone();
                    break;
            }


            sensorView.setText(
                    String.format(
                            "%15s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "%15s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "%15s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "%15s : %.5f\n" +
                            "%15s : %8.5f, %8.5f, %8.5f, SUM : %8.5f\n" +
                            "Accel - Gravity X : %8.5f, Y : %8.5f, Z : %8.5f, ACCEL SIZE : %8.5f",
                            "Gravity", gravity[aX], gravity[aY], gravity[aZ],
                            "Gyroscope", gyro[aX], gyro[aY], gyro[aZ],
                            "Accel", accel[aX], accel[aY], accel[aZ],
                            "Orientation", Math.toDegrees(orientation[0]) > 0 ? Math.toDegrees(orientation[0]) : Math.toDegrees(orientation[0]) + 360,
                            "Magnetic", magnetic[aX], magnetic[aY], magnetic[aZ], Math.sqrt(Math.pow(magnetic[aX], 2) + Math.pow(magnetic[aY], 2) + Math.pow(magnetic[aZ], 2)),
                            accel[aX] - gravity[aX], accel[aY] - gravity[aY], accel[aZ] - gravity[aZ], accelSize
                    )
            );

            speedView.setText(
                    String.format(
                            "%15s : %8.5f",
                            "SPEED", speed
                    )
            );



            // Realize compass by magnetic field sensor and accelerometer sensor
            // Get rotation matrix
            SensorManager.getRotationMatrix(rotationMat, null, accel, magnetic);
            // Get orientation
            SensorManager.getOrientation(rotationMat, orientation);
            // Convert Radian to Degree
//            orientation[0] = (float)Math.toDegrees(orientation[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
