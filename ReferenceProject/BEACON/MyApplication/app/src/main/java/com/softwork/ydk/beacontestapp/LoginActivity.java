package com.softwork.ydk.beacontestapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.softwork.ydk.beacontestapp.Server.ServerManager;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v) {
        // TODO - Need to request login to server
        ServerManager.getInstance();
        Intent floorPlanListActivity = new Intent(this, FloorPlanListActivity.class);
        startActivity(floorPlanListActivity);
        finish();
    }

    public void join(View v) {
        // TODO - Need to request join to server
        ServerManager.getInstance();

    }
}
