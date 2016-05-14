package com.softwork.ydk.beacontestapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.Beacon.BeaconRangingListener;
import com.softwork.ydk.beacontestapp.Beacon.BeaconService;
import com.softwork.ydk.beacontestapp.Beacon.RefreshBeaconService;
import com.softwork.ydk.beacontestapp.Room.RoomDataManager;

import java.util.List;

public class BeaconActivity extends AppCompatActivity {
    private BeaconService beaconService;
    private SensorManager sensorManager;
    private List<Sensor> deviceSensors;

    private Sensor gravitySensor;
    private Sensor gyroSensor;
    private Sensor accelSensor;
    private Sensor magneticSensor;

    private float gravity[] = new float[3];
    private float gyro[] = new float[3];
    private float accel[] = new float[3];
    private float magnetic[] = new float[3];
    private float orientation[] = new float[3];

    private float rotationMat[] = new float[9];

    private final int aX = 0;
    private final int aY = 1;
    private final int aZ = 2;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION = 10;

    static public TextView dataView;
    static public TextView curPosView;

    private RelativeLayout drawGrphicLayout;
    private EditText setSizeEditText;
    private EditText setBeaconLocationEditText;
    private TextView sensorValueTextView;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    private int x1 = 20;
    private int y1 = 20;
    private int x2 = 20 + RoomDataManager.getInstance().getRoomWidth();
    private int y2 = 20 + RoomDataManager.getInstance().getRoomHeight();

    private int magnitude = 3;

    private int b1_coor[] = {(x2 - x1) / 2, y1};
    private int b2_coor[] = {x1, y2};
    private int b3_coor[] = {x2, y2};

    private float BEACON_CLAMP_LENGTH = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

    private int next_pos[] = {0, 0};
    private int cur_pos[] = {(x2 - x1) / 2, (y2 - y1) / 2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataView = (TextView) findViewById(R.id.BeaconDataTextView);
        curPosView = (TextView) findViewById(R.id.CurPos);
        setSizeEditText = (EditText) findViewById(R.id.SetSizeEditText);
        setBeaconLocationEditText = (EditText) findViewById(R.id.SetBeaconLocationEditText);
        drawGrphicLayout = (RelativeLayout) findViewById(R.id.DrawGrphicLayout);
        sensorValueTextView = (TextView) findViewById(R.id.SensorValueTextView);

        //If a user device turns off bluetooth, request to turn it on.
        //사용자가 블루투스를 켜도록 요청합니다.
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("BA", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is not granted.");
                this.requestLocationPermission();
            } else {
                Log.i("BA", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is already granted.");
            }
        }

        if(beaconService == null)
            beaconService = new BeaconService(this);

        drawGrphicLayout.addView(new CanvasView(this));
//        new RefreshBeaconService(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void requestLocationPermission() {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }
    }

    public void resetBeacon(View v) {
        String regionSizeStr = setSizeEditText.getText().toString();
        int regionSize[] = {0, 0, 0};
        for(int i = 0; i < 3; i++)
            regionSize[i] = Integer.parseInt(regionSizeStr.split(" ")[i]);
        x1 = 20;
        y1 = 20;
        x2 = regionSize[0] + 20;
        y2 = regionSize[1] + 20;
        magnitude = regionSize[2];

        String beaconLocationStr = setBeaconLocationEditText.getText().toString();
        int beaconLocation[] = {0, 0, 0, 0, 0, 0};
        for(int i = 0; i < 6; i++)
            beaconLocation[i] = Integer.parseInt(beaconLocationStr.split(" ")[i]);
        b1_coor[0] = beaconLocation[0];
        b1_coor[1] = beaconLocation[1];
        b2_coor[0] = beaconLocation[2];
        b2_coor[1] = beaconLocation[3];
        b3_coor[0] = beaconLocation[4];
        b3_coor[1] = beaconLocation[5];

        BEACON_CLAMP_LENGTH = (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void resetBeaconService() {
        if(beaconService != null) {
            beaconService.unBindService();
            beaconService = null;
            beaconService = new BeaconService(this);
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
                    accel = event.values.clone();
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic = event.values.clone();
                    break;
            }
            sensorValueTextView.setText(
                    String.format(
                            "%10s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "%10s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "%10s X : %8.5f, Y : %8.5f, Z : %8.5f\n" +
                            "Orientation : %.5f\n" +
                            "Magnetic : %8.5f, %8.5f, %8.5f, SUM : %8.5f\n" +
                            "Accel - Gravity X : %8.5f, Y : %8.5f, Z : %8.5f",
                            "Gravity", gravity[aX], gravity[aY], gravity[aZ],
                            "Gyroscope", gyro[aX], gyro[aY], gyro[aZ],
                            "Accel", accel[aX], accel[aY], accel[aZ],
                            Math.toDegrees(orientation[0]) > 0 ? Math.toDegrees(orientation[0]) : Math.toDegrees(orientation[0]) + 360,
                            magnetic[aX], magnetic[aY], magnetic[aZ], Math.sqrt(Math.pow(magnetic[aX], 2) + Math.pow(magnetic[aY], 2) + Math.pow(magnetic[aZ], 2)),
                            accel[aX] - gravity[aX], accel[aY] - gravity[aY], accel[aZ] - gravity[aZ])
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

    protected class CanvasView extends View {
        private int directionBarSize = 200;
        private int directionCircleSize = 10;
        private int directionPos[] = {100, 500};
        private int gravityPos[] = {300, 500};
        private int gyroPos[] = {500, 500};
        private int compassPos[] = {100, 800};
        private int compassSize = 50;
        private int compassCircleSize = 7;

        private float points[] = new float[4];

        public CanvasView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            calculateCurPos();
            curPosView.setText("x1 : " + next_pos[0] + " " + "y1 : " + next_pos[1]);

            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setTextSize(50);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x1, y1, x2 * magnitude, y2 * magnitude, paint);

            canvas.drawText(String.format("b100(%d, %d) %.2fm", b1_coor[0], b1_coor[1], BeaconRangingListener.beacon1 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH / 100 : (float)BeaconRangingListener.beacon1),
                    b1_coor[0] * magnitude - 100, b1_coor[1] * magnitude + 50, paint);
            canvas.drawText(String.format("b200(%d, %d) %.2fm", b2_coor[0], b2_coor[1], BeaconRangingListener.beacon2 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH / 100 : (float)BeaconRangingListener.beacon2),
                    b2_coor[0] * magnitude, b2_coor[1] * magnitude, paint);
            canvas.drawText(String.format("b300(%d, %d) %.2fm", b3_coor[0], b3_coor[1], BeaconRangingListener.beacon3 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH / 100 : (float)BeaconRangingListener.beacon3),
                    b3_coor[0] * magnitude - 400, b3_coor[1] * magnitude, paint);

            canvas.drawCircle(
                    b1_coor[0] * magnitude, b1_coor[1] * magnitude,
                    BeaconRangingListener.beacon1 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : (float)BeaconRangingListener.beacon1 * 100 * magnitude,
                    paint
            );
            canvas.drawCircle(
                    b2_coor[0] * magnitude, b2_coor[1] * magnitude,
                    BeaconRangingListener.beacon2 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : (float)BeaconRangingListener.beacon2 * 100 * magnitude,
                    paint
            );
            canvas.drawCircle(
                    b3_coor[0] * magnitude, b3_coor[1] * magnitude,
                    BeaconRangingListener.beacon3 * 100 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : (float)BeaconRangingListener.beacon3 * 100 * magnitude,
                    paint
            );


            paint.setStrokeWidth(2);
            canvas.drawLine(
                    directionPos[aX] - directionBarSize / 2 * (accel[aX] / 9.8f),
                    directionPos[aY] - directionBarSize / 2 * (accel[aY] / 9.8f),
                    directionPos[aX] + directionBarSize / 2 * (accel[aX] / 9.8f),
                    directionPos[aY] + directionBarSize / 2 * (accel[aY] / 9.8f),
                    paint
            );

            canvas.drawLine(
                    gravityPos[aX] - directionBarSize / 2 * (gravity[aX] / 9.8f),
                    gravityPos[aY] - directionBarSize / 2 * (gravity[aY] / 9.8f),
                    gravityPos[aX] + directionBarSize / 2 * (gravity[aX] / 9.8f),
                    gravityPos[aY] + directionBarSize / 2 * (gravity[aY] / 9.8f),
                    paint
            );

            canvas.drawLine(
                    gyroPos[aX] - directionBarSize / 2 * (gyro[aX] / 9.8f),
                    gyroPos[aY] - directionBarSize / 2 * (gyro[aY] / 9.8f),
                    gyroPos[aX] + directionBarSize / 2 * (gyro[aX] / 9.8f),
                    gyroPos[aY] + directionBarSize / 2 * (gyro[aY] / 9.8f),
                    paint
            );

            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(
                    directionPos[aX] - directionBarSize / 2 * (accel[aX] / 9.8f),
                    directionPos[aY] - directionBarSize / 2 * (accel[aY] / 9.8f),
                    directionCircleSize * (accel[aZ] / 9.8f + 1.5f),
                    paint
            );

            canvas.drawCircle(
                    gyroPos[aX] - directionBarSize / 2 * (gyro[aX] / 9.8f),
                    gyroPos[aY] - directionBarSize / 2 * (gyro[aY] / 9.8f),
                    directionCircleSize * (gyro[aZ] / 9.8f + 1.5f),
                    paint
            );

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

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(20);
            canvas.drawPoint(b1_coor[0] * magnitude, b1_coor[1] * magnitude, paint);
            canvas.drawPoint(b2_coor[0] * magnitude, b2_coor[1] * magnitude, paint);
            canvas.drawPoint(b3_coor[0] * magnitude, b3_coor[1] * magnitude, paint);

            paint.setStrokeWidth(50);
            canvas.drawPoint(cur_pos[0] * magnitude, cur_pos[1] * magnitude, paint);
            cur_pos[0] += (next_pos[0] - cur_pos[0]) / 10;
            cur_pos[1] += (next_pos[1] - cur_pos[1]) / 10;
            invalidate();
        }

        public void calculateCurPos() {
            int magPow = 100;
            int mag = (b2_coor[0] - b3_coor[0]) / (b1_coor[0] - b2_coor[0]);
            double  distance[] = {
                    Math.sqrt(Math.pow(b1_coor[0] - b2_coor[0], 2) + Math.pow(b1_coor[1] - b2_coor[1], 2)),
                    Math.sqrt(Math.pow(b1_coor[0] - b3_coor[0], 2) + Math.pow(b1_coor[1] - b3_coor[1], 2)),
                    Math.sqrt(Math.pow(b3_coor[0] - b2_coor[0], 2) + Math.pow(b3_coor[1] - b2_coor[1], 2)),
            };
            double range1 = BeaconRangingListener.beacon1 * magPow > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon1 * magPow;
            double range2 = BeaconRangingListener.beacon2 * magPow > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon2 * magPow;
            double range3 = BeaconRangingListener.beacon3 * magPow > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon3 * magPow;

            if(range1 + range2 > distance[0]) {
                double bias = (range1 - ((range1 + range2) - distance[0]) / 2) / distance[0];
                double _x = b1_coor[aX] + (b2_coor[0] - b1_coor[0]) * bias;
                double _y = b1_coor[aY] + (b2_coor[1] - b1_coor[1]) * bias;
                next_pos[aX] = b3_coor[aX] + (int)((_x - b3_coor[aX]) * (range3 / distance[2]));
                next_pos[aY] = b3_coor[aY] + (int)((_y - b3_coor[aY]) * (range3 / distance[2]));
            } else if(range1 + range3 > distance[1]) {
                double bias = (range1 - ((range1 + range3) - distance[1]) / 2) / distance[1];
                double _x = b1_coor[aX] + (b3_coor[0] - b1_coor[0]) * bias;
                double _y = b1_coor[aY] + (b3_coor[1] - b1_coor[1]) * bias;
                next_pos[aX] = b2_coor[aX] + (int)((_x - b2_coor[aX]) * (range2 / distance[1]));
                next_pos[aY] = b2_coor[aY] + (int)((_y - b2_coor[aY]) * (range2 / distance[1]));
            } else if(range2 + range3 > distance[2]) {
                double bias = (range2 - ((range2 + range3) - distance[2]) / 2) / distance[2];
                double _x = b2_coor[aX] + (b3_coor[0] - b2_coor[0]) * bias;
                double _y = b2_coor[aY] + (b3_coor[1] - b2_coor[1]) * bias;
                next_pos[aX] = b1_coor[aX] + (int)((_x - b1_coor[aX]) * (range1 / distance[0]));
                next_pos[aY] = b1_coor[aY] + (int)((_y - b1_coor[aY]) * (range1 / distance[0]));
            }
        }
    }
}

