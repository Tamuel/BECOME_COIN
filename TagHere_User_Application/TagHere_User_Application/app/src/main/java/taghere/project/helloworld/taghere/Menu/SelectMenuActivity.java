package taghere.project.helloworld.taghere.Menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import taghere.project.helloworld.taghere.GoogleMap.GoogleMapActivity;
import taghere.project.helloworld.taghere.R;
import taghere.project.helloworld.taghere.ScanActivity;

//메뉴 액티비티
public class SelectMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_menu);
    }

    //회원 정보 실행
    public void onClickMemberInfoButton(View v) {
        Intent MemberInfoIntent = new Intent(SelectMenuActivity.this, MemberInfoActivity.class);
        startActivity(MemberInfoIntent);
    }

    //Q&A 정보 실행
    public void onClickQAButton(View v) {
        Intent QAIntent = new Intent(SelectMenuActivity.this, QAActivity.class);
        startActivity(QAIntent);
    }

    //건물 평가 실행
    public void onClickBuildingButton(View v) {
        Intent BuildingIntent = new Intent(SelectMenuActivity.this, BuildingRankActivity.class);
        startActivity(BuildingIntent);
    }

    //설정 실행
    public void onClickOptionButton(View v) {
        Intent OptionActivity = new Intent(SelectMenuActivity.this, OptionActivity.class);
        startActivity(OptionActivity);
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
