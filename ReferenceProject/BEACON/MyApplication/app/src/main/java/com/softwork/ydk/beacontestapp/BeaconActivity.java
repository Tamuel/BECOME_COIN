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
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.Beacon.BeaconRangingListener;
import com.softwork.ydk.beacontestapp.Beacon.BeaconService;
import com.softwork.ydk.beacontestapp.Room.RoomDataManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconActivity extends AppCompatActivity {
    private BeaconService beaconService = null;
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
    private int x2 = 20 + 500;
    private int y2 = 20 + 300;

    private float magnitude = 3;

    private Map<Integer, int[]> beaconPos = new HashMap<>();

    private int BEACON_CLAMP_LENGTH = (int)(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) * 1.2);

    private int next_pos[] = {0, 0};
    private int cur_pos[] = {(x2 - x1) / 2, (y2 - y1) / 2};

    private boolean setBeacon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        dataView = (TextView) findViewById(R.id.BeaconDataTextView);
        curPosView = (TextView) findViewById(R.id.CurPos);
//        setSizeEditText = (EditText) findViewById(R.id.SetSizeEditText);
//        setBeaconLocationEditText = (EditText) findViewById(R.id.SetBeaconLocationEditText);
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("BA", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is not granted.");
                this.requestLocationPermission();
            } else {
                Log.i("BA", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is already granted.");
            }
        }

        if(beaconService == null)
            resetBeaconService();

        drawGrphicLayout.addView(new CanvasView(this));
//        new RefreshBeaconService(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
//        sensorManager.registerListener(sensorEventListener, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
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
        if(!setBeacon)
            setBeacon = true;
        /*
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
        for(int i = 0; i < 8; i++)
            beaconLocation[i] = Integer.parseInt(beaconLocationStr.split(" ")[i]);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 2; j++) {
                beaconPos[i][j] = beaconLocation[i * 2 + j];
            }
        }
        BEACON_CLAMP_LENGTH = (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        */
    }

    public void resetBeaconService() {
        if(beaconService != null)
            beaconService.unBindService();
        beaconService = null;
        beaconService = new BeaconService(this);
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

    @Override
    public void finish() {
        if(beaconService != null)
            beaconService.unBindService();
        super.finish();
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
        private Path triangle;

        private int beaconRange[] = new int[BeaconRangingListener.numberOfBeacons];

        private int correctPos[] = {0, 0};

        // For dragging
        private int pressedPos[] = {0, 0};
        private int movePos[] = {0,0};
        private int criteriaPos[] = {0, 0};
        private int prevPos[] = {0, 0};

        // For zooming
        private boolean zooming = false;
        private int multitouchPos[][] = {{0, 0}, {0, 0}};
        private float zoomRate = 0;
        private int zoomingPos[] = {0, 0};

        private float points[] = new float[6];

        private int numOfArrangedBeacon = 0;

        private Map<Integer, int[]> approximatePos = new HashMap<>();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (setBeacon) {
                int pos[] = {(int)((event.getX() - criteriaPos[aX]) / magnitude), (int)((event.getY() - criteriaPos[aY]) / magnitude)};
                beaconPos.put((numOfArrangedBeacon + 1) * 100, pos);

                numOfArrangedBeacon++;

                if (numOfArrangedBeacon == 5) {
                    numOfArrangedBeacon = 0;
                    setBeacon = false;
                }
                return false;
            } else {
                int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int fingerId = event.getPointerId(pointerIndex);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_DOWN:
                        multitouchPos[fingerId][aX] = (int)event.getX(pointerIndex);
                        multitouchPos[fingerId][aY] = (int)event.getY(pointerIndex);
                        if(event.getPointerCount() == 2) {
                            zooming = true;
                            zoomRate = (float)(magnitude / Math.sqrt(Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) + Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)));
                            zoomingPos[aX] = (int)(magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2);
                            zoomingPos[aY] = (int)(magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2);
                            prevPos[aX] = criteriaPos[aX];
                            prevPos[aY] = criteriaPos[aY];
                        }

                        if(!zooming) {
                            pressedPos[aX] = (int) event.getX();
                            pressedPos[aY] = (int) event.getY();

                            prevPos[aX] = criteriaPos[aX];
                            prevPos[aY] = criteriaPos[aY];
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(!zooming) {
                            movePos[aX] = (int) event.getX() - pressedPos[aX];
                            movePos[aY] = (int) event.getY() - pressedPos[aY];

                            criteriaPos[aX] = prevPos[aX] + movePos[aX];
                            criteriaPos[aY] = prevPos[aY] + movePos[aY];
                        } else {
                            multitouchPos[fingerId][aX] = (int)event.getX(pointerIndex);
                            multitouchPos[fingerId][aY] = (int)event.getY(pointerIndex);
                            criteriaPos[aX] = prevPos[aX] - (int)(magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2 - zoomingPos[aX]) / 2;
                            criteriaPos[aY] = prevPos[aY] - (int)(magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2 - zoomingPos[aY]) / 2;
                            if(event.getPointerCount() == 2) {
                                magnitude = (float)(zoomRate * Math.sqrt(Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) + Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)));
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        if(event.getPointerCount() == 1 && zooming)
                            zooming = false;
                        break;
                }

                return true;
            }
        }

        public CanvasView(Context context) {
            super(context);
            this.setOnTouchListener(this);

            int pos[] = {0, 0};
            beaconPos.put(100, pos);
            beaconPos.put(200, pos);
            beaconPos.put(300, pos);
            beaconPos.put(400, pos);
            beaconPos.put(500, pos);
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
            canvas.drawRect(
                    criteriaPos[aX] + x1,
                    criteriaPos[aY] + y1,
                    criteriaPos[aX] + x2 * magnitude,
                    criteriaPos[aY] + y2 * magnitude,
                    paint
            );


            // Draw beacons
            for(int i = 0; i < 5; i++) {
                int length = -1;
                try {
                    length = BeaconRangingListener.beacons.get((i + 1) * 100) > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacons.get((i + 1) * 100);
                } catch (Exception e) {

                }
                canvas.drawText(
                        String.format(
                                "b%d(%d, %d)\n%5dcm",
                                (i + 1) * 100,
                                beaconPos.get((i + 1) * 100)[aX], beaconPos.get((i + 1) * 100)[aY],
                                length
                        ),
                        criteriaPos[aX] + beaconPos.get((i + 1) * 100)[aX] * magnitude - 100,
                        criteriaPos[aY] + beaconPos.get((i + 1) * 100)[aY] * magnitude + 50,
                        paint
                );

                canvas.drawCircle(
                        criteriaPos[aX] + beaconPos.get((i + 1) * 100)[aX] * magnitude,
                        criteriaPos[aY] + beaconPos.get((i + 1) * 100)[aY] * magnitude,
                        beaconRange[i] * magnitude,
                        paint
                );
                beaconRange[i] = length;
            }


            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);

            for(int key : approximatePos.keySet()) {
                paint.setColor(Color.GRAY);
                canvas.drawText(
                        String.format(
                                "%d",
                                key
                        ),
                        criteriaPos[aX] + approximatePos.get(key)[aX] * magnitude,
                        criteriaPos[aY] + approximatePos.get(key)[aY] * magnitude,
                        paint
                );
                paint.setColor(Color.RED);
                canvas.drawCircle(
                        criteriaPos[aX] + approximatePos.get(key)[aX] * magnitude,
                        criteriaPos[aY] + approximatePos.get(key)[aY] * magnitude,
                        compassCircleSize,
                        paint
                );
            }

            paint.setStrokeWidth(20);
            paint.setColor(Color.BLACK);
            for(int i = 0; i < 5; i++) {
                canvas.drawPoint(
                        criteriaPos[aX] + beaconPos.get((i + 1) * 100)[aX] * magnitude,
                        criteriaPos[aY] + beaconPos.get((i + 1) * 100)[aY] * magnitude,
                        paint
                );
            }

            paint.setColor(Color.rgb(0, 175, 255));
            canvas.drawCircle(
                    criteriaPos[aX] + cur_pos[0] * magnitude,
                    criteriaPos[aY] + cur_pos[1] * magnitude,
                    20,
                    paint
            );
            cur_pos[0] += (next_pos[0] - cur_pos[0]) / 30;
            cur_pos[1] += (next_pos[1] - cur_pos[1]) / 30;


            /*
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
            */

            paint.setStyle(Paint.Style.FILL);

            // Compass North
            points[0] = compassPos[aX] - (float)Math.sin(orientation[0]) * compassSize;
            points[1] = compassPos[aY] - (float)Math.cos(orientation[0]) * compassSize;

            // Compass South
            points[2] = compassPos[aX] + (float)Math.sin(orientation[0]) * compassSize;
            points[3] = compassPos[aY] + (float)Math.cos(orientation[0]) * compassSize;

            // Compass Front
            points[4] = compassPos[aX] - (float)Math.cos(orientation[0] - Math.toRadians(90)) * compassSize;
            points[5] = compassPos[aY] - (float)Math.sin(orientation[0] - Math.toRadians(90)) * compassSize;


            paint.setColor(Color.GRAY);
            canvas.drawCircle(
                    compassPos[aX],
                    compassPos[aY],
                    compassCircleSize,
                    paint
            );

            paint.setColor(Color.BLUE);

            triangle = new Path();

            triangle.moveTo(
                    points[4],
                    points[5]
            );

            triangle.lineTo(
                    points[4] - (float) Math.cos(orientation[0] - Math.toRadians(180)) * compassSize / 3,
                    points[5] - (float) Math.sin(orientation[0] - Math.toRadians(180)) * compassSize / 3
            );

            triangle.lineTo(
                    compassPos[aX] + (points[4] - compassPos[aX]) * 2,
                    compassPos[aY] + (points[5] - compassPos[aY]) * 2
            );

            triangle.lineTo(
                    points[4] - (float) Math.cos(orientation[0]) * compassSize / 3,
                    points[5] - (float) Math.sin(orientation[0]) * compassSize / 3
            );

            triangle.close();

            canvas.drawPath(triangle, paint);

            invalidate();
//            paint.setColor(Color.RED);
//
//            canvas.drawCircle(
//                    points[0],
//                    points[1],
//                    compassCircleSize,
//                    paint
//            );
//
//            canvas.drawCircle(
//                    correctPos[0],
//                    correctPos[1],
//                    15,
//                    paint
//            );

//            canvas.drawCircle(
//                    points[2],
//                    points[3],
//                    compassCircleSize,
//                    paint
//            );
        }

        public void calculateCurPos() {
            Map<Integer, Integer> range = new HashMap<>();
            int closestBeacon = 0; // key

            // 공간보다 크게 잡히는 것을 방지
            for(int key : BeaconRangingListener.beacons.keySet())
                range.put(key, BeaconRangingListener.beacons.get(key) > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacons.get(key));

            if(range.size() >= 1) {
                // 가장 근접한 비콘 탐지
                closestBeacon = (int) range.keySet().toArray()[0];
                for (int key : range.keySet())
                    if (range.get(key) < range.get(closestBeacon))
                        closestBeacon = key;

                // 근접한 비콘을 제외한 비콘들의 신호 세기에 비례해 위치 설정
                double gradient;
                double distance;
                double distanceFromClosestBeacon;
                double newPositions[] = {0, 0};
                for (int key : range.keySet()) {
//                if (b_index != closestBeacon) {
                    try {
                        // 두 비콘 사이의 기울기
                        gradient = (double) (beaconPos.get(key)[aY] - beaconPos.get(closestBeacon)[aY]) / (double) (beaconPos.get(key)[aX] - beaconPos.get(closestBeacon)[aX]);
                    } catch (ArithmeticException e) {
                        gradient = 10000;
                    }
                    // 두 비콘 사이의 거리
                    distance = Math.sqrt(Math.pow(beaconPos.get(key)[aX] - beaconPos.get(closestBeacon)[aX], 2) + Math.pow(beaconPos.get(key)[aY] - beaconPos.get(closestBeacon)[aY], 2));
                    distanceFromClosestBeacon = range.get(key) - distance;
                    if (beaconPos.get(key)[aX] > beaconPos.get(closestBeacon)[aX])
                        distanceFromClosestBeacon = -distanceFromClosestBeacon;

                    double bias = distanceFromClosestBeacon / (distanceFromClosestBeacon + Math.abs(gradient) * distanceFromClosestBeacon);
                    newPositions[aX] += distanceFromClosestBeacon * bias;
                    newPositions[aY] += distanceFromClosestBeacon * bias * gradient;

                    int pos[] = {
                            beaconPos.get(closestBeacon)[aX] + (int) (distanceFromClosestBeacon * bias),
                            beaconPos.get(closestBeacon)[aY] + (int) (distanceFromClosestBeacon * bias * gradient)
                    };
                    approximatePos.put(key, pos);
//                } else {
//                    int _x = (x2 - x1) / 2;
//                    int _y = (y2 - y1) / 2;
//                    try {
//                        // 두 비콘 사이의 기울기
//                        gradient = (double)(beaconPos[b_index][aY] - _y) / (double)(beaconPos[b_index][aX] - _x);
//                    } catch (ArithmeticException e) {
//                        gradient = 10000;
//                    }
//                    distance = Math.sqrt(Math.pow(beaconPos[b_index][aX] - _x, 2) + Math.pow(beaconPos[b_index][aY] - _y, 2));
//                    distanceFromClosestBeacon = range[b_index];
//                    if(beaconPos[b_index][aX] > _x)
//                        distanceFromClosestBeacon = -distanceFromClosestBeacon;
//
//                    double bias = distanceFromClosestBeacon / (distanceFromClosestBeacon + Math.abs(gradient) * distanceFromClosestBeacon);
//                    newPositions[aX] += distanceFromClosestBeacon * bias * 2;
//                    newPositions[aY] += distanceFromClosestBeacon * bias * gradient * 2;
//
//                    approximatePos[b_index][aX] = beaconPos[closestBeacon][aX] + (int)(distanceFromClosestBeacon * bias * 2);
//                    approximatePos[b_index][aY] = beaconPos[closestBeacon][aY] + (int)(distanceFromClosestBeacon * bias * gradient * 2);
//                }
                }
                next_pos[aX] = beaconPos.get(closestBeacon)[aX] + (int) newPositions[aX] / range.size();
                next_pos[aY] = beaconPos.get(closestBeacon)[aY] + (int) newPositions[aY] / range.size();
            }
            /*
            double  distance[] = {
                    Math.sqrt(Math.pow(b1_coor[0] - b2_coor[0], 2) + Math.pow(b1_coor[1] - b2_coor[1], 2)),
                    Math.sqrt(Math.pow(b1_coor[0] - b3_coor[0], 2) + Math.pow(b1_coor[1] - b3_coor[1], 2)),
                    Math.sqrt(Math.pow(b3_coor[0] - b2_coor[0], 2) + Math.pow(b3_coor[1] - b2_coor[1], 2)),
            };
            double range1 = BeaconRangingListener.beacon1 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon1;
            double range2 = BeaconRangingListener.beacon2 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon2;
            double range3 = BeaconRangingListener.beacon3 > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacon3;

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
            */

        }
    }
}

