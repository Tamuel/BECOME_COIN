package taghere.project.helloworld.taghere.GoogleMap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import taghere.project.helloworld.taghere.R;

//건물에 대한 간단한 정보들을 알려주는 액티비티
public class GoogleBuildingInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_building_info);
    }

    //건물 내부 보기 버튼 누를 시
    public void onClickBuildingInfoButton(View v) {
        Intent CoinPositionIntent = new Intent(this, BuildingInfoActivity.class);
        startActivity(CoinPositionIntent);
    }

    //나가기 버튼 누를 시
    public void onClickBuildingInfoExitButton(View v) {
        Intent resultIntent = new Intent();

        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
