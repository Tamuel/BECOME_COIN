package com.softwork.ydk.beacontestapp.FloorPlanActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.BFunc;
import com.softwork.ydk.beacontestapp.FloorPlan.DrawingObject;
import com.softwork.ydk.beacontestapp.FloorPlan.ToolMode;
import com.softwork.ydk.beacontestapp.R;

import java.util.ArrayList;

public class FloorPlanActivity extends Activity {
    private RelativeLayout floorPlanView;
    private FloorPlanInfoLayout floorPlanInfoView;
    private RelativeLayout canvasView;
    private TextView bannerTextView;

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
            "RECT:7:100:50:800:450:-14898960:null\n" +
            "TEXT:0:200:300:0:0:0:null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);

        floorPlanView = (RelativeLayout)findViewById(R.id.floor_plan_view);
        floorPlanInfoView = (FloorPlanInfoLayout)findViewById(R.id.floor_plan_info_view);
        canvasView = (RelativeLayout)findViewById(R.id.canvas_layout);
        bannerTextView = (TextView)findViewById(R.id.banner_text_view);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                BFunc.getDP(this, 200)
        );
        Log.i("HEIGHT", getResources().getDisplayMetrics().heightPixels + "");
        params.setMargins(0, getResources().getDisplayMetrics().heightPixels - BFunc.getDP(this, 200), 0, 0);
        floorPlanInfoView.setLayoutParams(params);

        canvasView.addView(new CanvasView(this));

        String line[] = objectData.split("\n");

        int lineColor, fillColor;
        for(String temp : line) {
            String data[] = temp.split(":");
            DrawingObject newObject = new DrawingObject();
            switch (data[0]) {
                case "RECT":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.RECT);
                case "CIRCLE":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.CIRCLE);
                case "LINE":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.LINE);

                    newObject.setThickness(Integer.parseInt(data[1]));
                    newObject.setBeginPoint(
                            new Point(
                                    Integer.parseInt(data[2]),
                                    Integer.parseInt(data[3])
                            )
                    );
                    newObject.setEndPoint(
                            new Point(
                                    Integer.parseInt(data[4]),
                                    Integer.parseInt(data[5])
                            )
                    );
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

                case "TEXT":
                    newObject.setToolMode(ToolMode.TEXT);
                    newObject.setBeginPoint(
                            new Point(
                                    Integer.parseInt(data[2]),
                                    Integer.parseInt(data[3])
                            )
                    );
                    newObject.setLine(true);
                    break;
            }
            objects.add(newObject);
        }
    }


    // Draw Graphics
    protected class CanvasView extends View implements View.OnTouchListener {
        // For coordinate system
        private final int aX = 0, aY = 1;

        // For dragging
        private int pressedPos[]        = {0, 0};
        private int movePos[]           = {600, 600};
        private int criteriaPos[]       = {0, 0};
        private int prevPos[]           = {0, 0};

        // For zooming
        private float magnitude         = 1.5f;
        private float zoomRate          = 0;
        private boolean zooming         = false;
        private int multitouchPos[][]   = {{0, 0}, {0, 0}};
        private int zoomingPos[]        = {0, 0};
        private int magClamp[]          = {1, 7};

        // For drawing
        private Paint paint = new Paint();
        private int r, g, b;

        // For icon
        private int iconSize[] = {
                BFunc.getDP(this.getContext(), 40),
                BFunc.getDP(this.getContext(), 40)
        };
        private Bitmap textIcon =
                ((BitmapDrawable)getResources().getDrawable(R.drawable.icon_information)).getBitmap();

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
            for(DrawingObject tempObject : objects) {
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
                                    criteriaPos[aX] + (tempObject.getBeginPoint().x + tempObject.getEndPoint().x) * magnitude,
                                    criteriaPos[aY] + (tempObject.getBeginPoint().y + tempObject.getEndPoint().y) * magnitude,
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
                            paint.setStrokeWidth(tempObject.getThickness() * magnitude);
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

                    case TEXT:
                        paint.setStyle(Paint.Style.FILL_AND_STROKE);
                        canvas.drawBitmap(
                                textIcon,
                                null,
                                new Rect(
                                        (int)(criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude - iconSize[aX] / 2),
                                        (int)(criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude - iconSize[aY]),
                                        (int)(criteriaPos[aX] + tempObject.getBeginPoint().x * magnitude + iconSize[aX] / 2),
                                        (int)(criteriaPos[aY] + tempObject.getBeginPoint().y * magnitude)
                                ),
                                paint
                        );
                        break;
                }
            }
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
                    if(!zooming && getLength(prevPos[aX], criteriaPos[aX], prevPos[aY], criteriaPos[aY]) < 15) {
                        for(DrawingObject tempObject : objects)
                            if (tempObject.getToolMode() == ToolMode.TEXT) {
                                int length = getLength(
                                        criteriaPos[aX] + (int) (tempObject.getBeginPoint().x * magnitude),
                                        pressedPos[aX],
                                        criteriaPos[aY] + (int) (tempObject.getBeginPoint().y * magnitude),
                                        pressedPos[aY]
                                );
                                if (length < iconSize[aX]) { // When Clicked!
                                    floorPlanInfoView.startSlideAnimation();
                                    break;
                                }
                            }
                    }
                    break;
            }
            return true;
        }

        public int getLength(int x1, int x2, int y1, int y2) {
            return (int)Math.sqrt(
                    Math.pow(x1 - x2, 2) +
                    Math.pow(y1 - y2, 2)
            );
        }
    }
}
