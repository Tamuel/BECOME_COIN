package user.taghere.taghere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import java.util.Timer;
import java.util.TimerTask;

public class StartSceneActivity extends AppCompatActivity {

    private TimerTask mTask;
    private Timer mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scene);
        Log.i("test", "start ac");
        mTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext()
                        , ReadActivity.class);
                startActivity(intent);
                finish();
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 10);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_scene, menu);
        return true;
    }

}
