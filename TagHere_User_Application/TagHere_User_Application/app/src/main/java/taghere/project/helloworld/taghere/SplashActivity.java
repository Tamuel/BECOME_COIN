package taghere.project.helloworld.taghere;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class SplashActivity extends Activity {
    private ImageView icon;

    private int openningTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        ActionBar actionBar = getActionBar();
//        actionBar.hide();

        icon = (ImageView) findViewById(R.id.icon_imageview);

        startSplash();
    }

    public void startSplash() {
        icon.animate().alpha(1.0f)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(openningTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent scanActivity = new Intent(SplashActivity.this, ScanActivity.class);
                        startActivity(scanActivity);
                        SplashActivity.this.finish();
                    }
                })
                .start();
    }
}
