package com.softwork.ydk.beacontestapp.FloorPlanActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.BFunc;
import com.softwork.ydk.beacontestapp.FloorPlan.DrawingObject;
import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;
import com.softwork.ydk.beacontestapp.FloorPlan.IconMode;
import com.softwork.ydk.beacontestapp.FloorPlan.ToolMode;
import com.softwork.ydk.beacontestapp.GoogleMaps.GoogleMapActivity;
import com.softwork.ydk.beacontestapp.R;
import com.softwork.ydk.beacontestapp.Server.ServerManager;

public class FloorPlanEditActivity extends Activity {
    private RelativeLayout floorPlanView;
    private FloorPlanInfoLayout floorPlanInfoView;
    private RelativeLayout canvasView;
    private EditText bannerEditText;
    private TextView locationTextView;

    public static final int GET_LOCATION = 0;

    // Floor Plan
    private FloorPlan floorPlan;
    private DrawingObject selectedObject = null;

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
    private float magnitude         = 1.5f;

    private boolean objectSelected = false;
    private boolean objectDragged = false;

    private int criteriaPos[] = {0, 0};

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
            "BEACON:CON_BEACON:125:215:50:50:1000:100\n" +
            "BEACON:CON_BEACON:845:215:50:50:1000:200\n" +
            "BEACON:CON_BEACON:125:445:50:50:1000:300\n" +
            "BEACON:CON_BEACON:845:445:50:50:1000:400\n" +
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
        setContentView(R.layout.activity_floor_plan_edit);

        floorPlanView = (RelativeLayout)findViewById(R.id.floor_plan_view);
        floorPlanInfoView = (FloorPlanInfoLayout)findViewById(R.id.floor_plan_info_view);
        canvasView = (RelativeLayout)findViewById(R.id.canvas_layout);
        bannerEditText = (EditText)findViewById(R.id.banner_edit_text);
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(sensorEventListener, accelSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
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

    public void addNewInfo(View v) {
        switch (v.getId()) {
            case R.id.add_info_button:
                showNewInfoDialog(
                        NewObjectType.TEXT,
                        getString(R.string.add_information),
                        getString(R.string.title),
                        getString(R.string.content)
                );
                break;

            case R.id.add_beacon_button:
                showNewInfoDialog(
                        NewObjectType.BEACON,
                        getString(R.string.add_beacon),
                        getString(R.string.major_key),
                        getString(R.string.minor_key)
                );
                break;


            case R.id.add_tag_button:
                showNewInfoDialog(
                        NewObjectType.TAG,
                        getString(R.string.add_tag),
                        getString(R.string.tag_key),
                        getString(R.string.tag_data)
                );
                break;
        }
    }

    public void setBanner() {
        bannerEditText.setText(floorPlan.getBuildingName() + " " + floorPlan.getName() + " " + floorPlan.getFloor() + "층");
        locationTextView.setText("(" + String.format("%.6f", floorPlan.getLatitude()) + ", " + String.format("%.6f", floorPlan.getLongitude()) + ")");
    }

    public void showGoogleMaps(View v) {
        Intent googleMaps = new Intent(this, GoogleMapActivity.class);
        startActivityForResult(googleMaps, GET_LOCATION);
    }

    public void saveFloorPlan(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case GET_LOCATION:
                try {
                    floorPlan.setLatitude(data.getDoubleExtra("latitude", 0.0));
                    floorPlan.setLongitude(data.getDoubleExtra("longitude", 0.0));
                    setBanner();
                } catch (Exception e) {

                }
                break;


        }
    }

    public void exitFloorPlan(View v) {
        finish();
    }

    public void deleteObject(View v) {
        objectSelected = false;
        objectDragged = false;
        floorPlan.getObjects().remove(selectedObject);
        floorPlanInfoView.startDownAnimation();
    }

    public void cancelObject(View v) {
        objectSelected = false;
        objectDragged = false;
        selectedObject = null;
        floorPlanInfoView.startDownAnimation();
    }

    public void showNewInfoDialog(final NewObjectType type, String title, String titleHint, String contentHint) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.new_info_alert_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText titleEditText = (EditText) dialogView.findViewById(R.id.title_alert_edit_text);
        final EditText contentEditText = (EditText) dialogView.findViewById(R.id.content_alert_edit_text);

        titleEditText.setHint(titleHint);
        contentEditText.setHint(contentHint);

        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DrawingObject newObject = new DrawingObject();
                switch (type) {
                    case ICON:
                        break;

                    case BEACON:
                        newObject.setToolMode(ToolMode.BEACON);
                        newObject.setBeginPoint(
                                new Point(
                                        (int) (getResources().getDisplayMetrics().widthPixels / 2 / magnitude - criteriaPos[aX] / magnitude),
                                        (int) (getResources().getDisplayMetrics().heightPixels / 2 / magnitude - criteriaPos[aY] / magnitude)
                                )
                        );
                        newObject.setEndPoint(new Point(DrawingObject.iconSize[0], DrawingObject.iconSize[1]));
                        newObject.setMajorKey(titleEditText.getText().toString());
                        newObject.setMinorKey(contentEditText.getText().toString());
                        newObject.setLine(true);
                        newObject.setIsIcon(true);
                        floorPlan.addObject(newObject);
                        break;

                    case TEXT:
                        newObject.setToolMode(ToolMode.TEXT);
                        newObject.setBeginPoint(
                                new Point(
                                        (int)(getResources().getDisplayMetrics().widthPixels / 2 / magnitude - criteriaPos[aX] / magnitude),
                                        (int)(getResources().getDisplayMetrics().heightPixels / 2 / magnitude - criteriaPos[aY] / magnitude)
                                )
                        );
                        newObject.setEndPoint(new Point(DrawingObject.iconSize[0], DrawingObject.iconSize[1]));
                        newObject.setMajorKey(titleEditText.getText().toString());
                        newObject.setMinorKey(contentEditText.getText().toString());
                        newObject.setLine(true);
                        newObject.setIsIcon(true);
                        floorPlan.addObject(newObject);
                        break;


                    case TAG:
                        newObject.setToolMode(ToolMode.TAG);
                        newObject.setBeginPoint(
                                new Point(
                                        (int)(getResources().getDisplayMetrics().widthPixels / 2 / magnitude - criteriaPos[aX] / magnitude),
                                        (int)(getResources().getDisplayMetrics().heightPixels / 2 / magnitude - criteriaPos[aY] / magnitude)
                                )
                        );
                        newObject.setEndPoint(new Point(DrawingObject.iconSize[0], DrawingObject.iconSize[1]));
                        newObject.setMajorKey(titleEditText.getText().toString());
                        newObject.setMinorKey(contentEditText.getText().toString());
                        newObject.setLine(true);
                        newObject.setIsIcon(true);
                        floorPlan.addObject(newObject);
                        break;
                }
            }
        });
        dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog newInfoDialog = dialogBuilder.create();
        newInfoDialog.show();
    }


    // Draw Graphics
    protected class CanvasView extends View implements View.OnTouchListener {

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
                if(tempObject.equals(selectedObject)) {
                    switch (tempObject.getToolMode()) {
                        case LINE:
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(editLineThickness * magnitude);
                            paint.setColor(Color.RED);
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

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(editLineThickness * magnitude);
                            paint.setColor(Color.RED);
                            canvas.drawRect(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
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

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(editLineThickness * magnitude);
                            paint.setColor(Color.RED);
                            canvas.drawOval(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + tempObject.getEndPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getEndPoint().y * magnitude,
                                    paint
                            );
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

                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(editLineThickness * magnitude);
                            paint.setColor(Color.RED);
                            canvas.drawRect(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude - DrawingObject.iconSize[aX] / 2,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude - DrawingObject.iconSize[aY] / 2,
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude + DrawingObject.iconSize[aX] / 2,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude + DrawingObject.iconSize[aY] / 2,
                                    paint
                            );
                            break;

                    }
                } else {
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
                    downTime = System.currentTimeMillis();
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
                        if(objectSelected) {
                            objectDragged = selectedObject.isSelected(
                                    (int)((pressedPos[aX] - criteriaPos[aX]) / magnitude),
                                    (int)((pressedPos[aY] - criteriaPos[aY]) / magnitude)
                            );

                            if(objectDragged) {
                                prevPos[aX] = selectedObject.getBeginPoint().x;
                                prevPos[aY] = selectedObject.getBeginPoint().y;
                                prevPosEnd[aX] = selectedObject.getEndPoint().x;
                                prevPosEnd[aY] = selectedObject.getEndPoint().y;
                            }
                        }
                        if(!objectDragged){
                            prevPos[aX] = criteriaPos[aX];
                            prevPos[aY] = criteriaPos[aY];
                        }
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1 && zooming) {
                        zooming = false;
                        pressedPos[aX] = (int) event.getX();
                        pressedPos[aY] = (int) event.getY();
                        break;
                    }

                    if(objectDragged && !zooming) {
                        movePos[aX] = (int) event.getX() - pressedPos[aX];
                        movePos[aY] = (int) event.getY() - pressedPos[aY];

                        selectedObject.setBeginPoint(
                                new Point(
                                        prevPos[aX] + (int) (movePos[aX] / magnitude),
                                        prevPos[aY] + (int) (movePos[aY] / magnitude)
                                )
                        );

                        selectedObject.setEndPoint(
                                new Point(
                                        prevPosEnd[aX] + (int)(movePos[aX] / magnitude),
                                        prevPosEnd[aY] + (int)(movePos[aY] / magnitude)
                                )
                        );

                        floorPlanInfoView.setContent(selectedObject.toString());


                    } else if (!zooming) {
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
                    Log.i("PRESS", (int)((pressedPos[aX] - criteriaPos[aX])/magnitude) + " " +
                            (int)((pressedPos[aY] - criteriaPos[aY])/magnitude));
                    upTime = System.currentTimeMillis();
                    if (event.getPointerCount() == 1 && zooming)
                        zooming = false;
                    if(!zooming && BFunc.getLength(prevPos[aX], criteriaPos[aX], prevPos[aY], criteriaPos[aY]) < 15) { // When on click
//                        if(upTime - downTime > 1000) { // When long click
                        boolean selected = false;
                        for (int i = floorPlan.getObjects().size() - 1; i >= 0; i--) {
                            DrawingObject tempObject = floorPlan.getObjects().get(i);
                            selected = tempObject.isSelected(
                                    (int)((pressedPos[aX] - criteriaPos[aX]) / magnitude),
                                    (int)((pressedPos[aY] - criteriaPos[aY]) / magnitude)
                            );

                            if (selected) { // When Clicked
                                selectedObject = tempObject;
                                selected = true;
                                objectSelected = true;
                                floorPlanInfoView.startUpAnimation();
                                floorPlanInfoView.setTitle(selectedObject.getToolMode().name());
                                floorPlanInfoView.setContent(selectedObject.toString());
                                break;
                            }
                        }
                        if(!selected) {
                            selectedObject = null;
                            objectSelected = false;
                            floorPlanInfoView.startDownAnimation();
                        }
                    }
                    objectDragged = false;
                    break;
            }
            return true;
        }

    }
}
