package com.softwork.ydk.beacontestapp.FloorPlanActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.BFunc;
import com.softwork.ydk.beacontestapp.Beacon.BeaconRangingListener;
import com.softwork.ydk.beacontestapp.Beacon.BeaconService;
import com.softwork.ydk.beacontestapp.FloorPlan.DrawingObject;
import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;
import com.softwork.ydk.beacontestapp.FloorPlan.IconMode;
import com.softwork.ydk.beacontestapp.FloorPlan.ToolMode;
import com.softwork.ydk.beacontestapp.GoogleMaps.GoogleMapActivity;
import com.softwork.ydk.beacontestapp.R;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

public class FloorPlanActivity extends Activity {
    private RelativeLayout floorPlanView;
    private FloorPlanInfoLayout floorPlanInfoView;
    private RelativeLayout canvasView;
    private TextView bannerTextView;
    private TextView locationTextView;

    public static final int GET_LOCATION = 0;

    // Floor Plan
    private FloorPlan floorPlan;

    // Sensors
    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor magneticSensor;

    private float accel[] = new float[3];
    private float magnetic[] = new float[3];

    private float orientation[] = new float[3];
    private float rotationMat[] = new float[9];

    // For coordinate system
    private final int aX = 0, aY = 1;
    private float magnitude = 1.5f;
    private int criteriaPos[] = {0, 0};

    // For beacon
    private BeaconService beaconService;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION = 10;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private int x1 = 118, x2 = 780, y1 = 43, y2 = 460;


    private String objectData =
            "RECT:7:118:43:780:460:-16777216:null\n" +
            "RECT:3:116:43:290:170:-16777216:null\n" +
            "RECT:3:626:43:270:170:-16777216:null\n" +
            "RECT:3:406:43:220:170:-16777216:null\n" +
            "ICON:COIN_TOILET:230:99:50:50:0.0:null\n" +
            "ICON:COIN_UAAA:740:89:50:50:1.5707963267948966:null\n" +
            "CIRCLE:3:440:260:140:140:-14898960:-1055568\n" +
            "LINE:3:460:380:560:290:-16777216:null\n" +
            "LINE:3:460:280:560:380:-16777216:null\n" +
            "LINE:3:210:310:270:310:-16777216:null\n" +
            "LINE:3:240:310:240:380:-16777216:null\n" +
            "LINE:3:310:310:350:310:-16777216:null\n" +
            "LINE:3:320:310:320:380:-16777216:null\n" +
            "LINE:3:320:380:360:380:-16777216:null\n" +
            "LINE:3:320:350:350:350:-16777216:null\n" +
            "LINE:3:680:290:640:290:-16777216:null\n" +
            "LINE:3:640:290:640:320:-16777216:null\n" +
            "LINE:3:640:320:680:320:-16777216:null\n" +
            "LINE:3:680:320:680:380:-16777216:null\n" +
            "LINE:3:680:380:650:380:-16777216:null\n" +
            "LINE:3:720:290:770:290:-16777216:null\n" +
            "LINE:3:750:290:750:370:-16777216:null\n" +
            "BEACON:CON_BEACON:125:215:50:50:M1:m1\n" +
            "BEACON:CON_BEACON:845:215:50:50:M2:m2\n" +
            "BEACON:CON_BEACON:125:445:50:50:M3:m3\n" +
            "BEACON:CON_BEACON:845:445:50:50:M4:m4\n" +
            "TAG:COIN_TAG:495:105:50:50:tagkey1234:null\n" +
            "LINE:3:200:240:200:270:-16777216:null\n" +
            "LINE:3:810:240:830:240:-16777216:null\n" +
            "LINE:3:830:240:830:260:-16777216:null\n" +
            "LINE:3:830:260:820:260:-16777216:null\n" +
            "LINE:3:820:260:820:280:-16777216:null\n" +
            "LINE:3:820:280:840:280:-16777216:null\n" +
            "LINE:3:190:450:210:450:-16777216:null\n" +
            "LINE:3:210:450:210:490:-16777216:null\n" +
            "LINE:3:210:490:180:490:-16777216:null\n" +
            "LINE:3:190:470:210:470:-16777216:null\n" +
            "LINE:3:810:440:810:470:-16777216:null\n" +
            "LINE:3:810:470:840:470:-16777216:null\n" +
            "LINE:3:830:440:830:480:-16777216:null\n" +
            "TEXT:COIN_TEXT:170:270:0:0:Hello!:The other data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);

        floorPlanView = (RelativeLayout)findViewById(R.id.floor_plan_view);
        floorPlanInfoView = (FloorPlanInfoLayout)findViewById(R.id.floor_plan_info_view);
        canvasView = (RelativeLayout)findViewById(R.id.canvas_layout);
        bannerTextView = (TextView)findViewById(R.id.banner_text_view);
        locationTextView = (TextView)findViewById(R.id.location_text_view);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                BFunc.getDP(this, 200)
        );
        params.setMargins(0, getResources().getDisplayMetrics().heightPixels - BFunc.getDP(this, 200), 0, 0);
        floorPlanInfoView.setLayoutParams(params);

        canvasView.addView(new CanvasView(this));

        DrawingObject.iconSize = new int[2];
        DrawingObject.iconSize[aX] = BFunc.getDP(this, 40);
        DrawingObject.iconSize[aY] = BFunc.getDP(this, 40);

        // Get Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        floorPlan = ServerManager.getInstance().getFloorPlans().get(getIntent().getIntExtra("FLOOR_PLAN", 0));
        setBanner();
        if(floorPlan.getObjects().size() == 0)
            floorPlan.setObjects(ServerManager.getInstance().getObject(objectData));


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
            beaconService = new BeaconService(this);

    }

    private void requestLocationPermission() {
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }
    }

    public SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch(event.sensor.getType())
            {
                case Sensor.TYPE_ACCELEROMETER:
                    accel = event.values.clone();
                    break;

                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic = event.values.clone();
                    break;
            }

            // Get rotation matrix
            SensorManager.getRotationMatrix(rotationMat, null, accel, magnetic);
            // Get orientation
            SensorManager.getOrientation(rotationMat, orientation);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    public void setBanner() {
        bannerTextView.setText(floorPlan.getBuildingName() + " " + floorPlan.getName() + " " + floorPlan.getFloor() + "층");
        locationTextView.setText("(" + String.format("%.6f", floorPlan.getLatitude()) + ", " + String.format("%.6f", floorPlan.getLongitude()) + ")");
    }

    public void showGoogleMaps(View v) {
        Intent googleMaps = new Intent(this, GoogleMapActivity.class);
        googleMaps.putExtra("latitude", floorPlan.getLatitude());
        googleMaps.putExtra("longitude", floorPlan.getLongitude());
        startActivity(googleMaps);
    }

    public void exitFloorPlan(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }


    // Draw Graphics
    protected class CanvasView extends View implements View.OnTouchListener {

        // For beacons
        private int beaconRange[] = new int[BeaconRangingListener.numberOfBeacons];
        private int BEACON_CLAMP_LENGTH = (int)(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) * 1.2);
        private int approximatePos[][] = new int[BeaconRangingListener.numberOfBeacons][2];
        private int beaconPos[][] = new int[BeaconRangingListener.numberOfBeacons][2];
        private int next_pos[] = {0, 0};

        // For dragging
        private int pressedPos[]        = {0, 0};
        private int movePos[]           = {600, 600};
        private int prevPos[]           = {0, 0};
        private int prevPosEnd[]           = {0, 0};

        // For zooming
        private float zoomRate          = 0;
        private boolean zooming         = false;
        private int multitouchPos[][]   = {{0, 0}, {0, 0}};
        private int zoomingPos[]        = {0, 0};
        private int magClamp[]          = {1, 7};

        // For compass
        private int compassPos[] = {
                getResources().getDisplayMetrics().widthPixels / 6,
                getResources().getDisplayMetrics().heightPixels - getResources().getDisplayMetrics().widthPixels / 6
        };
        private int compassSize = 50;
        private int compassCircleSize = 7;
        private Path triangle;
        private float points[] = new float[6];

        // For drawing
        private Paint paint = new Paint();
        private int r, g, b;

        // For long click
        private long downTime = 0;
        private long upTime = 0;

        // For editing
        private int editLineThickness = 5;
        private int selectCircleRadius = BFunc.getDP(getContext(), 10);

        private Bitmap iconText =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_information)).getBitmap();
        private Bitmap iconToilet =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_toilet)).getBitmap();
        private Bitmap iconUaaa =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_uaaa)).getBitmap();
        private Bitmap iconTag =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_tag)).getBitmap();
        private Bitmap iconBeacon =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_beacon)).getBitmap();

        private Bitmap tempBitmap = null;

        public CanvasView(Context context) {
            super(context);
            this.setOnTouchListener(this);
            this.setBackgroundColor(Color.WHITE);

            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);
        }


        public void onDraw(Canvas canvas) {
            for(DrawingObject tempObject : floorPlan.getObjects()) {
                tempBitmap = null;
                switch (tempObject.getToolMode()) {
                    case LINE:
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(tempObject.getThickness() * magnitude);
                        paint.setColor(tempObject.getLineColor());
                        canvas.drawLine(
                                criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                paint
                        );
                        break;

                    case RECT:
                        if(tempObject.isFill()) {
                            paint.setStyle(Paint.Style.FILL);
                            paint.setColor(tempObject.getFillColor());
                            canvas.drawRect(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
                        }

                        if(tempObject.isLine()) {
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(tempObject.getThickness() * magnitude);
                            paint.setColor(tempObject.getLineColor());
                            canvas.drawRect(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
                        }
                        break;

                    case CIRCLE:
                        if (tempObject.isFill()) {
                            paint.setStyle(Paint.Style.FILL);
                            paint.setColor(tempObject.getFillColor());
                            canvas.drawOval(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
                        }

                        if (tempObject.isLine()) {
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(tempObject.getThickness() * magnitude);
                            paint.setColor(tempObject.getLineColor());
                            canvas.drawOval(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
                        }
                        break;

                    case ICON:
                        switch (tempObject.getIconMode()) {
                            case COIN_DOOR:
                                break;

                            case COIN_ELEVATOR:
                                break;

                            case COIN_ESCALATOR:
                                break;

                            case COIN_STAIR:
                                break;

                            case COIN_TOILET:
                                if(tempBitmap == null) tempBitmap = iconToilet;
                                break;

                            case COIN_UAAA:
                                if(tempBitmap == null) tempBitmap = iconUaaa;
                                break;
                        }
                    case TEXT:
                        if(tempBitmap == null) tempBitmap = iconText;
                    case BEACON:
                        if(tempBitmap == null) tempBitmap = iconBeacon;
                    case TAG:
                        if(tempBitmap == null) tempBitmap = iconTag;
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawBitmap(
                                tempBitmap,
                                null,
                                new Rect(
                                        (int)(criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude - DrawingObject.iconSize[aX] / 2),
                                        (int)(criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude - DrawingObject.iconSize[aY] / 2),
                                        (int)(criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude + DrawingObject.iconSize[aX] / 2),
                                        (int)(criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude + DrawingObject.iconSize[aY] / 2)
                                ),
                                paint
                        );
                        break;

                }
            }


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
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int pointerIndex = event.getActionIndex();
            int fingerId = event.getPointerId(pointerIndex);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                case MotionEvent.ACTION_DOWN:
                    if (event.getPointerCount() <= 2) {
                        multitouchPos[fingerId][aX] = (int) event.getX(pointerIndex);
                        multitouchPos[fingerId][aY] = (int) event.getY(pointerIndex);
                    }
                    if (event.getPointerCount() == 2) {
                        zooming = true;
                        zoomRate = (float) (magnitude / Math.sqrt(
                                Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) +
                                        Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)
                        )
                        );
                        pressedPos[aX] = (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2;
                        pressedPos[aY] = (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2;
                        prevPos[aX] = criteriaPos[aX];
                        prevPos[aY] = criteriaPos[aY];
                    }

                    if (!zooming) {
                        pressedPos[aX] = (int) event.getX();
                        pressedPos[aY] = (int) event.getY();

                        prevPos[aX] = criteriaPos[aX];
                        prevPos[aY] = criteriaPos[aY];
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1 && zooming) {
                        zooming = false;
                        pressedPos[aX] = (int) event.getX();
                        pressedPos[aY] = (int) event.getY();
                        break;
                    }

                    if (!zooming) {
                        movePos[aX] = (int) event.getX() - pressedPos[aX];
                        movePos[aY] = (int) event.getY() - pressedPos[aY];

                        criteriaPos[aX] = prevPos[aX] + movePos[aX];
                        criteriaPos[aY] = prevPos[aY] + movePos[aY];
                    } else {
                        if (event.getPointerCount() >= 3)
                            break;

                        for (int i = 0; i < event.getPointerCount(); i++) {
                            multitouchPos[i][aX] = (int) event.getX(i);
                            multitouchPos[i][aY] = (int) event.getY(i);
                        }

                        if (event.getPointerCount() == 2) {
                            int tempX = (int) (magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2);
                            int tempY = (int) (magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2);
                            magnitude = (float) (zoomRate *
                                    Math.sqrt(
                                            Math.pow(multitouchPos[0][aX] - multitouchPos[1][aX], 2) +
                                                    Math.pow(multitouchPos[0][aY] - multitouchPos[1][aY], 2)
                                    )
                            );
                            if (magnitude <= magClamp[0])
                                magnitude = magClamp[0];
                            else if (magnitude > magClamp[1])
                                magnitude = magClamp[1];
                            int tempX2 = (int) (magnitude * (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2);
                            int tempY2 = (int) (magnitude * (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2);
                            zoomingPos[aX] = tempX2 - tempX;
                            zoomingPos[aY] = tempY2 - tempY;
                        }

                        prevPos[aX] -= zoomingPos[aX] / 3;
                        prevPos[aY] -= zoomingPos[aY] / 3;

                        criteriaPos[aX] = prevPos[aX] +
                                (multitouchPos[0][aX] + multitouchPos[1][aX]) / 2 -
                                pressedPos[aX];

                        criteriaPos[aY] = prevPos[aY] +
                                (multitouchPos[0][aY] + multitouchPos[1][aY]) / 2 -
                                pressedPos[aY];

                    }
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    if (event.getPointerCount() == 1 && zooming)
                        zooming = false;
                    if(!zooming && BFunc.getLength(prevPos[aX], criteriaPos[aX], prevPos[aY], criteriaPos[aY]) < 15) {
                        boolean selected = false;
                        for (int i = floorPlan.getObjects().size() - 1; i >= 0; i--) {
                            DrawingObject tempObject = floorPlan.getObjects().get(i);
                            if(tempObject.isIcon()) {
                                selected = tempObject.isSelected(
                                        (int) ((pressedPos[aX] - criteriaPos[aX]) / magnitude),
                                        (int) ((pressedPos[aY] - criteriaPos[aY]) / magnitude)
                                );

                                if (selected) { // When Clicked
                                    selected = true;
                                    floorPlanInfoView.setTitle(tempObject.getMajorKey());
                                    floorPlanInfoView.setContent(tempObject.getMinorKey());
                                    floorPlanInfoView.startUpAnimation();
                                    break;
                                }
                            }
                        }
                        if(!selected) {
                            floorPlanInfoView.startDownAnimation();
                        }
                    }
                    break;
            }
            return true;
        }

        public void calculateCurPos() {
            int range[] = new int[BeaconRangingListener.numberOfBeacons];
            int closestBeacon = 0;

            // 공간보다 크게 잡히는 것을 방지
            for(int i = 0; i < BeaconRangingListener.numberOfBeacons; i++)
                range[i] = BeaconRangingListener.beacons[i] > BEACON_CLAMP_LENGTH ? BEACON_CLAMP_LENGTH : BeaconRangingListener.beacons[i];

            // 가장 근접한 비콘 탐지
            for(int i = 1; i < BeaconRangingListener.numberOfBeacons; i++)
                if(range[i] < range[closestBeacon])
                    closestBeacon = i;

            // 근접한 비콘을 제외한 비콘들의 신호 세기에 비례해 위치 설정
            double gradient;
            double distance;
            double distanceFromClosestBeacon;
            double newPositions[] = {0, 0};
            for (int b_index = 0; b_index < BeaconRangingListener.numberOfBeacons; b_index++) {
                if (b_index != closestBeacon) {
                    try {
                        // 두 비콘 사이의 기울기
                        gradient = (double)(beaconPos[b_index][aY] - beaconPos[closestBeacon][aY]) / (double)(beaconPos[b_index][aX] - beaconPos[closestBeacon][aX]);
                    } catch (ArithmeticException e) {
                        gradient = 10000;
                    }
                    // 두 비콘 사이의 거리
                    distance = Math.sqrt(Math.pow(beaconPos[b_index][aX] - beaconPos[closestBeacon][aX], 2) + Math.pow(beaconPos[b_index][aY] - beaconPos[closestBeacon][aY], 2));
                    distanceFromClosestBeacon = range[b_index] - distance;
                    if(beaconPos[b_index][aX] > beaconPos[closestBeacon][aX])
                        distanceFromClosestBeacon = -distanceFromClosestBeacon;

                    double bias = distanceFromClosestBeacon / (distanceFromClosestBeacon + Math.abs(gradient) * distanceFromClosestBeacon);
                    newPositions[aX] += distanceFromClosestBeacon * bias;
                    newPositions[aY] += distanceFromClosestBeacon * bias * gradient;

                    approximatePos[b_index][aX] = beaconPos[closestBeacon][aX] + (int)(distanceFromClosestBeacon * bias);
                    approximatePos[b_index][aY] = beaconPos[closestBeacon][aY] + (int)(distanceFromClosestBeacon * bias * gradient);
                } else {
                    int _x = (x2 - x1) / 2;
                    int _y = (y2 - y1) / 2;
                    try {
                        // 두 비콘 사이의 기울기
                        gradient = (double)(beaconPos[b_index][aY] - _y) / (double)(beaconPos[b_index][aX] - _x);
                    } catch (ArithmeticException e) {
                        gradient = 10000;
                    }
                    distance = Math.sqrt(Math.pow(beaconPos[b_index][aX] - _x, 2) + Math.pow(beaconPos[b_index][aY] - _y, 2));
                    distanceFromClosestBeacon = range[b_index];
                    if(beaconPos[b_index][aX] > _x)
                        distanceFromClosestBeacon = -distanceFromClosestBeacon;

                    double bias = distanceFromClosestBeacon / (distanceFromClosestBeacon + Math.abs(gradient) * distanceFromClosestBeacon);
                    newPositions[aX] += distanceFromClosestBeacon * bias * 2;
                    newPositions[aY] += distanceFromClosestBeacon * bias * gradient * 2;

                    approximatePos[b_index][aX] = beaconPos[closestBeacon][aX] + (int)(distanceFromClosestBeacon * bias * 2);
                    approximatePos[b_index][aY] = beaconPos[closestBeacon][aY] + (int)(distanceFromClosestBeacon * bias * gradient * 2);
                }
            }
            next_pos[aX] = beaconPos[closestBeacon][aX] + (int) newPositions[aX] / (BeaconRangingListener.numberOfBeacons - 1);
            next_pos[aY] = beaconPos[closestBeacon][aY] + (int) newPositions[aY] / (BeaconRangingListener.numberOfBeacons - 1);


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
