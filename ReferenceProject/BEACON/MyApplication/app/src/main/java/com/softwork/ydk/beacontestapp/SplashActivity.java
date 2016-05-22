package com.softwork.ydk.beacontestapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    private ImageView markImageView;
    private int openningTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        markImageView = (ImageView) findViewById(R.id.markImageView);
        startSplsh();
    }

    private void startSplsh() {
        markImageView.animate().alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(openningTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent scanActivity = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(scanActivity);
                        SplashActivity.this.finish();
                    }
                })
                .start();
    }
}
