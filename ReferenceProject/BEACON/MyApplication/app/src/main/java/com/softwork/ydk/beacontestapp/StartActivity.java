package com.softwork.ydk.beacontestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.Room.RoomDataManager;

public class StartActivity extends AppCompatActivity {
    private EditText roomWidthEditText;
    private EditText roomHeightEditText;
    private EditText horizontalSellsEditText;
    private EditText verticalSellsEditText;

    private TextView roomDataTextView;

    private RoomDataManager rm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        roomWidthEditText = (EditText) findViewById(R.id.RoomWidthEditText);
        roomHeightEditText = (EditText) findViewById(R.id.RoomHeightEditText);
        horizontalSellsEditText = (EditText) findViewById(R.id.HorizontalSellsEditText);
        verticalSellsEditText = (EditText) findViewById(R.id.VerticalSellsEditText);
        roomDataTextView = (TextView) findViewById(R.id.RoomDataTextView);

        rm = RoomDataManager.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomDataTextView.setText(
                "Room Data - Wi: " + rm.getRoomWidth() +
                "  He: " + rm.getRoomHeight() +
                "  Ho: " + rm.getHorizontalSells() +
                "  Ve: " + rm.getVerticalSells()
        );
    }

    public void onButtonClick(View v) {
        switch(v.getId()) {
            case R.id.BeaconTestButton:
                Intent beaconActivity = new Intent(this, BeaconActivity.class);
                startActivity(beaconActivity);
                break;

            case R.id.MagnaticTestButton:
                Intent MFActivity = new Intent(this, ConstructMFActivity.class);
                startActivity(MFActivity);
                break;

            case R.id.GetMapButton:
                break;

            case R.id.SetRoomButton:
                rm.setRoomWidth(Integer.parseInt(roomWidthEditText.getText().toString()));
                rm.setRoomHeight(Integer.parseInt(roomHeightEditText.getText().toString()));
                rm.setHorizontalSells(Integer.parseInt(horizontalSellsEditText.getText().toString()));
                rm.setVerticalSells(Integer.parseInt(verticalSellsEditText.getText().toString()));

                roomDataTextView.setText(
                        "Room Data - Wi: " + rm.getRoomWidth() +
                                "  He: " + rm.getRoomHeight() +
                                "  Ho: " + rm.getHorizontalSells() +
                                "  Ve: " + rm.getVerticalSells()
                );
                break;
        }
    }
}
