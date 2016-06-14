package taghere.project.helloworld.taghere.LogIn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import taghere.project.helloworld.taghere.AccountFolder.Account;
import taghere.project.helloworld.taghere.GoogleMap.NearAlarmActivity;
import taghere.project.helloworld.taghere.R;
import taghere.project.helloworld.taghere.Menu.SelectMenuActivity;

public class LogInActivity extends AppCompatActivity {
    private Account account;

    //입력된 아이디와 패스워드
    private EditText editId;
    private EditText editPassword;

    //우리집 좌표
    private double latitude = 35.8613587;
    private double longitude = 128.3964453;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        editId = (EditText)findViewById(R.id.IdEditText);
        editPassword = (EditText)findViewById(R.id.PasswordEditText);
        account = new Account(editId.getText().toString(), editPassword.getText().toString());

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getLatitude() >= latitude - 0.1 && location.getLatitude() <= latitude + 0.1
                        && location.getLongitude() >= longitude - 0.1 && location.getLongitude() <= longitude + 0.1) {
                    endLocationUpdates();
                    Intent nearAlarmIntent = new Intent(getApplicationContext(), NearAlarmActivity.class);
                    startActivityForResult(nearAlarmIntent, 1001);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    public void onClickLoginButton(View v) {
        //디비에서 아이디와 비밀번호가 맞는지 확인 후 로그인 성공
        Intent selectIntent = new Intent(LogInActivity.this, SelectMenuActivity.class);

        registerLocationUpdates();

        startActivity(selectIntent);
        LogInActivity.this.finish();
    }

    public void onClickMemberButton(View v) {
        Intent createAccountIntent = new Intent(LogInActivity.this, CreateAccountActivity.class);
        startActivity(createAccountIntent);
    }

    //위치 업데이트
    public void registerLocationUpdates() {
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, locationListener);
            }
        }
    }

    //위치 업데이트 종료
    public void endLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }
    }
}