package taghere.project.helloworld.taghere.GoogleMap;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import taghere.project.helloworld.taghere.R;

public class GoogleMapActivity extends FragmentActivity {
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    void init() {
        if(googleMap == null) {
            googleMap = ((SupportMapFragment)getSupportFragmentManager()
            .findFragmentById(R.id.fragment1)).getMap();

            if(googleMap != null) {
                addMarker();
            }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.886869, 128.608408), 16));
    }

    private void addMarker() {

    }
}
