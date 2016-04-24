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

public class MainActivity extends AppCompatActivity {
    private BeaconService beaconService;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION = 10;

    static public TextView dataView;
    static public TextView curPosView;

    private RelativeLayout drawGrphicLayout;
    private EditText setSizeEditText;
    private EditText setBeaconLocationEditText;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;


    private int x1 = 20;
    private int y1 = 20;
    private int x2 = 430;
    private int y2 = 320;

    private int magnitude = 3;

    private int b1_coor[] = {(x2 - x1) / 2, y1};
    private int b2_coor[] = {x1, y2};
    private int b3_coor[] = {x2, y2};

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

        //If a user device turns off bluetooth, request to turn it on.
        //사용자가 블루투스를 켜도록 요청합니다.
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if(mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBTIntent, REQUEST_ENABLE_BT);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is not granted.");
                this.requestLocationPermission();
            } else {
                Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is already granted.");
            }
        }

        if(beaconService == null)
            beaconService = new BeaconService(this);

        drawGrphicLayout.addView(new CanvasView(this));
        new RefreshBeaconService(this).start();
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
        x2 = regionSize[0];
        y2 = regionSize[1];
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
    }

    public void resetBeaconService() {
        if(beaconService != null) {
            beaconService.unBindService();
            beaconService = null;
            beaconService = new BeaconService(this);
        }
    }


    protected class CanvasView extends View {

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

            canvas.drawText(String.format("b1(%d, %d) %.2fm", b1_coor[0], b1_coor[1], BeaconRangingListener.beacon1),
                    b1_coor[0] * magnitude - 100, b1_coor[1] * magnitude + 50, paint);
            canvas.drawText(String.format("b2(%d, %d) %.2fm", b2_coor[0], b2_coor[1], BeaconRangingListener.beacon2),
                    b2_coor[0] * magnitude, b2_coor[1] * magnitude, paint);
            canvas.drawText(String.format("b3(%d, %d) %.2fm", b3_coor[0], b3_coor[1], BeaconRangingListener.beacon3),
                    b3_coor[0] * magnitude - 400, b3_coor[1] * magnitude, paint);

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
            int an1 = (int)(Math.pow(b1_coor[0], 2) + Math.pow(b1_coor[1], 2) - Math.pow(BeaconRangingListener.beacon1 * magPow, 2) -
                    (Math.pow(b2_coor[0], 2) + Math.pow(b2_coor[1], 2) - Math.pow(BeaconRangingListener.beacon2 * magPow, 2)));

            int an2 = (int)(Math.pow(b2_coor[0], 2) + Math.pow(b2_coor[1], 2) - Math.pow(BeaconRangingListener.beacon2 * magPow, 2) -
                    (Math.pow(b3_coor[0], 2) + Math.pow(b3_coor[1], 2) - Math.pow(BeaconRangingListener.beacon3 * magPow, 2)));

            int yy1 = 2 * (b1_coor[1] - b2_coor[1]);
            int yy2 = 2 * (b2_coor[1] - b3_coor[1]);

            next_pos[1] = (an2 - an1 * mag) / (yy2 - yy1 * mag);

            mag = (b2_coor[1] - b3_coor[1]) / (b1_coor[1] - b2_coor[1]);

            int xx1 = 2 * (b1_coor[0] - b2_coor[0]);
            int xx2 = 2 * (b2_coor[0] - b3_coor[0]);

            next_pos[0] = (an2 - an1 * mag) / (xx2 - xx1 * mag);
        }
    }
}

