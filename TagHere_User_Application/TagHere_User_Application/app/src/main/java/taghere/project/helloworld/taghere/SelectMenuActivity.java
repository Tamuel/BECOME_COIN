package taghere.project.helloworld.taghere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import taghere.project.helloworld.taghere.GoogleMap.GoogleMapActivity;

public class SelectMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);
    }

    //구글 지도 실행
    public void onClickGoogleMapButton(View v) {
        Intent GoogleMapIntent = new Intent(SelectMenuActivity.this, GoogleMapActivity.class);
        startActivity(GoogleMapIntent);
    }

    //QR코드 스캔 실행
    public void onClickQRCodeButton(View v) {
        Intent qrIntent = new Intent(SelectMenuActivity.this, ScanActivity.class);
        startActivity(qrIntent);
    }
}
