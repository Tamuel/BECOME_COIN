package taghere.project.helloworld.taghere.GoogleMap;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import taghere.project.helloworld.taghere.R;

public class GoogleMapActivity extends FragmentActivity {
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        init();

        //coin이 등록되어 있는 위치에 마크를 그림
        MarkerOptions coinMarker[] = {
                new MarkerOptions().position(
                        new LatLng(35.886869, 128.608408)).title("coin1"),
                new MarkerOptions().position(
                        new LatLng(34.886839, 127.608378)).title("coin2")
        };

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent coinActivity = new Intent(GoogleMapActivity.this, CoinPositionActivity.class);
                startActivity(coinActivity);

                return true;
            }
        });

        for(MarkerOptions temp : coinMarker) {
            temp.icon(BitmapDescriptorFactory.fromResource(R.drawable.coin_position_mark));
            googleMap.addMarker(temp);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    //지도를 띄울 시 시작 지점
    void init() {
        if(googleMap == null) {
            googleMap = ((SupportMapFragment)getSupportFragmentManager()
            .findFragmentById(R.id.fragment1)).getMap();

            if(googleMap != null) {
                addMarker();
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.886869, 128.608408), 17));
    }

    private void addMarker() {

    }
}
