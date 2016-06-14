package com.softwork.ydk.beacontestapp.FloorPlanActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softwork.ydk.beacontestapp.R;

/**
 * Created by DongKyu on 2016-06-12.
 */
public class FloorPlanInfoLayout extends LinearLayout {

    private Animation upAni, downAni;
    private boolean isAnimated = false;
    private boolean isUpping = false;
    private boolean isDowning = false;
    private TextView titleTextView;
    private TextView contentTextView;


    public FloorPlanInfoLayout(Context context) {
        super(context);
        initiate(context);
    }

    public FloorPlanInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initiate(context);
    }

    public FloorPlanInfoLayout(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
        initiate(context);
    }

    public void initiate(Context context) {
        this.setVisibility(View.INVISIBLE);
        upAni = AnimationUtils.loadAnimation(context, R.anim.up_animation);
        upAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimated = true;
                FloorPlanInfoLayout.this.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimated = false;
                isUpping = false;
                isDowning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        downAni = AnimationUtils.loadAnimation(context, R.anim.down_animation);
        downAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimated = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimated = false;
                isUpping = false;
                isDowning = false;
                FloorPlanInfoLayout.this.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startSlideAnimation() {
        if(this.getVisibility() == View.INVISIBLE && !this.isAnimated()) {
            this.startAnimation(upAni);
        } else if(this.getVisibility() == View.VISIBLE && !this.isAnimated()) {
            this.startAnimation(downAni);
        }
    }

    public void startUpAnimation() {
        if(this.getVisibility() == View.INVISIBLE && !this.isAnimated ||
            this.isDowning && !isUpping) {
            isUpping = true;
            isDowning = false;
            this.startAnimation(upAni);
        }
    }

    public void startDownAnimation() {
        if(this.getVisibility() == View.VISIBLE && !this.isAnimated ||
                isUpping && !isDowning) {
            isDowning = true;
            isUpping = false;
            this.startAnimation(downAni);
        }
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public void setIsAnimated(boolean isAnimated) {
        this.isAnimated = isAnimated;
    }

    public boolean isUpping() {
        return isUpping;
    }

    public void setIsUpping(boolean isUpping) {
        this.isUpping = isUpping;
    }

    public boolean isDowning() {
        return isDowning;
    }

    public void setIsDowning(boolean isDowning) {
        this.isDowning = isDowning;
    }

    public void setTitle(String title) {
        if(titleTextView == null)
            titleTextView = (TextView) findViewById(R.id.InfoTitleTextView);
        titleTextView.setText(title);
    }

    public void setContent(String content) {
        if(contentTextView == null)
            contentTextView = (TextView) findViewById(R.id.InfoContentTextView);
        contentTextView.setText(content);
    }
}
