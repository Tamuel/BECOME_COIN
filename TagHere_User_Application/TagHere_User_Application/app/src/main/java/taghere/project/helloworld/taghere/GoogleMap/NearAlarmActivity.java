package taghere.project.helloworld.taghere.GoogleMap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import taghere.project.helloworld.taghere.R;

public class NearAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_alarm);
    }

    public void onClickNearAlarmYesButton(View v) {
        Intent googleBuildingInfoIntent = new Intent(this, GoogleBuildingInfoActivity.class);
        startActivity(googleBuildingInfoIntent);
        finish();
    }

    public void onClickNearAlarmNoButton(View v) {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
