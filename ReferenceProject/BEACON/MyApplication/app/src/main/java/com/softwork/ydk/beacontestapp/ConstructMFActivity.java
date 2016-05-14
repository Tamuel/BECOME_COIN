package com.softwork.ydk.beacontestapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.Room.RoomDataManager;

import java.util.List;

public class ConstructMFActivity extends AppCompatActivity {
    private TextView roomDataTextView;
    private TextView MFValueTextView;
    private LinearLayout MFLinearLayout;

    private RoomDataManager rm;
    private Button MFbuttons[][];

    private int position[] = {0, 0};
    private int accumulateRate = 30;
    private int dataIndex = 0;
    private float accumulateSensorData = 0;
    private float avgSensorData = 0;

    private SensorManager sensorManager;
    private Sensor magneticSensor;
    private Sensor gravitySensor;

    private float getMFData[] = new float[3];
    private float magnetic[] = new float[3];
    private float gravity[] =  new float[3];
    private float gravity_alpha = 0.8f;
    private float sumMagnetic;

    private boolean trackingPosition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construct_mf);

        roomDataTextView = (TextView)findViewById(R.id.CMFRoomDataTextView);
        MFValueTextView = (TextView) findViewById(R.id.MFvalueTextView);
        MFLinearLayout = (LinearLayout)findViewById(R.id.MFButtonsLinearLayout);

        rm = RoomDataManager.getInstance();
        MFbuttons = new Button[rm.getHorizontalSells()][rm.getVerticalSells()];

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        roomDataTextView.setText(
                "Room Data - Wi: " + rm.getRoomWidth() +
                        "  He: " + rm.getRoomHeight() +
                        "  Ho: " + rm.getHorizontalSells() +
                        "  Ve: " + rm.getVerticalSells()
        );

        createSellButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
    }


    public SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                    gravity[0] = gravity_alpha * gravity[0] + (1 - gravity_alpha) * event.values[0];
                    gravity[1] = gravity_alpha * gravity[1] + (1 - gravity_alpha) * event.values[1];
                    gravity[2] = gravity_alpha * gravity[2] + (1 - gravity_alpha) * event.values[2];
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magnetic = event.values.clone();
                    break;
            }
            float R[] = new float[9];
            float l[] = new float[9];
            SensorManager.getRotationMatrix(R, l, gravity, magnetic);
            float A_D[] = event.values.clone();
            float A_W[] = new float[3];
            A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
            A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
            A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];
            sumMagnetic = (float)Math.sqrt(Math.pow(A_W[0], 2) + Math.pow(A_W[1], 2) + Math.pow(A_W[2], 2));
            if(trackingPosition) {
                accumulateSensorData += sumMagnetic;
                dataIndex++;
                if(dataIndex == accumulateRate) {
                    avgSensorData = accumulateSensorData / accumulateRate;
                    dataIndex = 0;
                    accumulateSensorData = 0;
                    MFbuttons[position[0]][position[1]].setBackgroundColor(Color.LTGRAY);
                    int nextOffset = 0;
                    int offset[][] = {{-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {+1, +1}, {+1, 0}, {+1, -1}, {0, -1}};
                    float min = 100000;
                    float temp[] = new float[8];
                    for(int i = 0; i < 8; i++) {
                        temp[i] = position[0] + offset[i][0] >= 0 && position[1] + offset[i][1] >= 0 &&
                                position[0] + offset[i][0] < rm.getHorizontalSells() && position[1] + offset[i][1] < rm.getVerticalSells()?
                                rm.getMFAvgValue()[position[0] + offset[i][0]][position[1] + offset[i][1]] : 100000;
                    };
                    for(int i = 0; i < 8; i++) {
                        if(Math.abs(temp[i] - avgSensorData) < min) {
                            min = Math.abs(temp[i] - avgSensorData);
                            nextOffset = i;
                        }
                    }

                    position[0] = position[0] + offset[nextOffset][0];
                    position[1] = position[1] + offset[nextOffset][1];
                    MFbuttons[position[0]][position[1]].setBackgroundColor(Color.RED);
                }
            }
            MFValueTextView.setText(
                    String.format(
                            "MF : (%8.5f,%8.5f,%8.5f) Y + Z (%8.5f) AVG : %8.5f",
                            A_W[0], A_W[1], A_W[2], A_W[1] + A_W[2], avgSensorData
                    )
            );
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void createSellButtons() {
        for (int i = 0; i < rm.getVerticalSells(); i++) {
            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tempLayout.setLayoutParams(lp);

            LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            blp.weight = 1;

            for (int j = 0; j < rm.getHorizontalSells(); j++) {
                Button tempButton = new Button(this);
                tempButton.setLayoutParams(blp);
                tempButton.setText("0");
                tempButton.setBackgroundColor(Color.LTGRAY);
                final int _x = j;
                final int _y = i;
                tempButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (Integer.parseInt(((Button) v).getText().toString()) < 3) {
                                Log.i("SUM", _x + " " + _y + " " + sumMagnetic);
                                getMFData[Integer.parseInt(((Button) v).getText().toString())] = sumMagnetic;
                                ((Button) v).setText("" + (Integer.parseInt(((Button) v).getText().toString()) + 1));
                            }
                            if (Integer.parseInt(((Button) v).getText().toString()) == 3) {
                                float sum = 0;
                                for (float temp : getMFData)
                                    sum += temp;

                                ((Button) v).setText(String.format("%3.3f", sum / getMFData.length));
                                rm.setMFAvgValue(_x, _y, (sum / getMFData.length));
                            }
                        } catch (NumberFormatException e) {
                        }
                    }
                });

                tempLayout.addView(tempButton);
                MFbuttons[j][i] = tempButton;
            }
            MFLinearLayout.addView(tempLayout);
        }
    }

    public void makeMFMap(View v) {
        if(trackingPosition)
            trackingPosition = false;
        else
            trackingPosition = true;
    }
}
