package com.softwork.ydk.beacontestapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.softwork.ydk.beacontestapp.Beacon.BeaconRangingListener;
import com.softwork.ydk.beacontestapp.FloorPlan.DrawingObject;
import com.softwork.ydk.beacontestapp.FloorPlan.ToolMode;

import java.util.ArrayList;

public class FloorPlanActivity extends Activity {

    private float magnitude = 3;

    private int next_pos[] = {0, 0};
    private int cur_pos[] = {0, 0};

    private final int aX = 0, aY = 1;

    private boolean setBeacon = false;

    private ArrayList<DrawingObject> objects = new ArrayList<>();
    private String objectData =
            "RECT:3:150:150:100:100:-16777216:-14898960\n" +
            "LINE:3:250:200:300:200:-16777216:-14898960\n" +
            "LINE:3:300:150:300:250:-16777216:-14898960\n" +
            "CIRCLE:3:200:300:100:100:-16777216:-1055568\n" +
            "LINE:3:400:150:450:150:-4144960:null\n" +
            "LINE:3:350:200:500:200:-1055568:-1055568\n" +
            "LINE:3:400:200:400:300:-16777216:null\n" +
            "LINE:3:400:250:500:300:-1055568:-1055568\n" +
            "LINE:3:500:250:550:250:-1055568:-1055568\n" +
            "LINE:3:550:150:550:300:-1055568:-1055568\n" +
            "CIRCLE:3:450:350:100:100:-14898960:null\n" +
            "CIRCLE:7:650:200:150:150:-4144960:null\n" +
            "LINE:7:850:150:850:450:-16777216:null\n" +
            "RECT:7:100:50:800:450:-14898960:null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CanvasView(this));

        String line[] = objectData.split("\n");

        int lineColor, fillColor;
        for(String temp : line) {
            String data[] = temp.split(":");
            DrawingObject newObject = new DrawingObject();
            switch (data[0]) {
                case "RECT":
                    newObject.setToolMode(ToolMode.RECT);
                    newObject.setThickness(Integer.parseInt(data[1]));
                    newObject.setBeginPoint(new Point(Integer.parseInt(data[2]), Integer.parseInt(data[3])));
                    newObject.setEndPoint(new Point(Integer.parseInt(data[4]), Integer.parseInt(data[5])));
                    if(!data[6].equals("null")) {
                        lineColor = Integer.parseInt(data[6]);
                        newObject.setLineColor(lineColor);
                        newObject.setLine(true);
                    }
                    if(!data[7].equals("null")) {
                        fillColor = Integer.parseInt(data[7]);
                        newObject.setFillColor(fillColor);
                        newObject.setFill(true);
                    }
                    break;

                case "LINE":
                    newObject.setToolMode(ToolMode.LINE);
                    newObject.setThickness(Integer.parseInt(data[1]));
                    newObject.setBeginPoint(new Point(Integer.parseInt(data[2]), Integer.parseInt(data[3])));
                    newObject.setEndPoint(new Point(Integer.parseInt(data[4]), Integer.parseInt(data[5])));
                    if(!data[6].equals("null")) {
                        lineColor = Integer.parseInt(data[6]);
                        newObject.setLineColor(lineColor);
                        newObject.setLine(true);
                    }
                    break;

                case "CIRCLE":
                    newObject.setToolMode(ToolMode.CIRCLE);
                    newObject.setThickness(Integer.parseInt(data[1]));
                    newObject.setBeginPoint(new Point(Integer.parseInt(data[2]), Integer.parseInt(data[3])));
                    newObject.setEndPoint(new Point(Integer.parseInt(data[4]), Integer.parseInt(data[5])));
                    if(!data[6].equals("null")) {
                        lineColor = Integer.parseInt(data[6]);
                        newObject.setLineColor(lineColor);
                        newObject.setLine(true);
                    }
                    if(!data[7].equals("null")) {
                        fillColor = Integer.parseInt(data[7]);
                        newObject.setFillColor(fillColor);
                        newObject.setFill(true);
                    }
                    break;
            }
            objects.add(newObject);
        }
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

        private int approximatePos[][] = new int[BeaconRangingListener.numberOfBeacons][2];

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (setBeacon) {
//                beaconPos[numOfArrangedBeacon][0] = (int)((event.getX() - criteriaPos[aX]) / magnitude);
//                beaconPos[numOfArrangedBeacon][1] = (int)((event.getY() - criteriaPos[aY]) / magnitude);
//
//                numOfArrangedBeacon++;
//
//                if (numOfArrangedBeacon == BeaconRangingListener.numberOfBeacons) {
//                    numOfArrangedBeacon = 0;
//                    setBeacon = false;
//                }
//                return false;
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
            return true;
        }

        public CanvasView(Context context) {
            super(context);
            this.setOnTouchListener(this);
        }

        public void onDraw(Canvas canvas) {
//            curPosView.setText("x1 : " + next_pos[0] + " " + "y1 : " + next_pos[1]);

            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setTextSize(50);
            paint.setStyle(Paint.Style.STROKE);

            for(DrawingObject tempObject : objects) {
                switch (tempObject.getToolMode()) {
                    case LINE:
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(tempObject.getThickness());
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
                                    criteriaPos[aX] + (tempObject.getBeginPoint().x + tempObject.getEndPoint().x) * magnitude,
                                    criteriaPos[aY] + (tempObject.getBeginPoint().y + tempObject.getEndPoint().y) * magnitude,
                                    paint
                            );
                        }

                        if(tempObject.isLine()) {
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(tempObject.getThickness());
                            paint.setColor(tempObject.getLineColor());
                            canvas.drawRect(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + (tempObject.getBeginPoint().x + tempObject.getEndPoint().x) * magnitude,
                                    criteriaPos[aY] + (tempObject.getBeginPoint().y + tempObject.getEndPoint().y) * magnitude,
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
                                    criteriaPos[aX] + (tempObject.getBeginPoint().x + tempObject.getEndPoint().x) * magnitude,
                                    criteriaPos[aY] + (tempObject.getBeginPoint().y + tempObject.getEndPoint().y) * magnitude,
                                    paint
                            );
                        }

                        if (tempObject.isLine()) {
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth(tempObject.getThickness());
                            paint.setColor(tempObject.getLineColor());
                            canvas.drawOval(
                                    criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude,
                                    criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude,
                                    criteriaPos[aX] + (tempObject.getBeginPoint().x + tempObject.getEndPoint().x) * magnitude,
                                    criteriaPos[aY] + (tempObject.getBeginPoint().y + tempObject.getEndPoint().y) * magnitude,
                                    paint
                            );
                        }
                        break;
                }
            }
            invalidate();
        }
    }
}
