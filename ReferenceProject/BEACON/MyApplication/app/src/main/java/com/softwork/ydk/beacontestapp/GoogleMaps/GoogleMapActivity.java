package com.softwork.ydk.beacontestapp.GoogleMaps;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softwork.ydk.beacontestapp.BFunc;
import com.softwork.ydk.beacontestapp.BeaconActivity;
import com.softwork.ydk.beacontestapp.FloorPlanActivity.FloorPlanEditActivity;
import com.softwork.ydk.beacontestapp.R;

import java.util.List;
import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private Bitmap coinMarkerIcon;
    private MarkerOptions locationMarker;

    private LinearLayout googleMapButtonsLinearLayout;
    private TextView bannerTextView;
    private Button setPositionButton;
    private Button cancelButton;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        googleMapButtonsLinearLayout = (LinearLayout)findViewById(R.id.google_map_buttons_linear_layout);

        bannerTextView = (TextView)findViewById(R.id.banner_text_view);
        setPositionButton = (Button)findViewById(R.id.save_floor_plan_location_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);

        coinMarkerIcon = Bitmap.createScaledBitmap(
                        ((BitmapDrawable) getResources().getDrawable(R.drawable.icon_map_mark)).getBitmap(),
                        BFunc.getDP(this, 30),
                        BFunc.getDP(this, 50),
                        true
                );

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_fragment);
        mapFragment.getMapAsync(this);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);
        if(latitude == 0.0 && longitude == 0.0) { // for edit

            mapFragment.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent coinActivity = new Intent(GoogleMapActivity.this, BeaconActivity.class);
                    startActivity(coinActivity);

                    return true;
                }
            });

            mapFragment.getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                }
            });

            mapFragment.getMap().setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mapFragment.getMap().clear();
                    locationMarker = new MarkerOptions().
                            position(latLng).
                            title("IN").
                            icon(BitmapDescriptorFactory.fromBitmap(coinMarkerIcon));
                    mapFragment.getMap().addMarker(locationMarker);
                }
            });
        } else { // for view
            try {
                Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);

                bannerTextView.setText(getString(R.string.location_is) + " " + addresses.get(0).getAddressLine(0) + " 입니다");
            } catch (Exception e) {
                bannerTextView.setText(getString(R.string.location_is) + " (" + latitude + ", " + longitude + ")");
            }
            cancelButton.setText(getString(R.string.exit));
            googleMapButtonsLinearLayout.removeView(setPositionButton);
            locationMarker = new MarkerOptions().
                    position(new LatLng(latitude, longitude)).
                    title("IN").
                    icon(BitmapDescriptorFactory.fromBitmap(coinMarkerIcon));
            mapFragment.getMap().addMarker(locationMarker);
        }
    }

    public void saveLocation(View v) {
        Intent locationData = new Intent();
        locationData.putExtra("latitude", locationMarker.getPosition().latitude);
        locationData.putExtra("longitude", locationMarker.getPosition().longitude);
        setResult(FloorPlanEditActivity.GET_LOCATION, locationData);
        finish();
    }

    public void cancelLocation(View v) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(latitude == 0.0 && longitude == 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(35.886869, 128.608408), 17));
        else
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

}
