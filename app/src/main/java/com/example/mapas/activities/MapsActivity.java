package com.example.mapas.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.mapas.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(15);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
//        LatLng bogota = new LatLng(4.60971, -74.08175);
//        mMap.addMarker(new MarkerOptions().position(bogota).title("saludos desde bogota"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));

        LatLng bogota = new LatLng(4.60971, -74.08175);
        mMap.addMarker(new MarkerOptions().position(bogota).title("saludos desde bogota").draggable(true));

        CameraPosition cameraPosition=new CameraPosition.Builder()
                .target(bogota)
                .zoom(10)       //limit->21
                .bearing(90)    //0 - 365° angulo de la camara
                .tilt(30)       //efecto 3d limit 90
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Click on: \n" +
                        "Lat: "+latLng.latitude+"\n"+
                        "Lon: "+latLng.longitude+"\n",
                        Toast.LENGTH_SHORT).show();

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Long Click on: \n" +
                                "Lat: "+latLng.latitude+"\n"+
                                "Lon: "+latLng.longitude+"\n",
                        Toast.LENGTH_SHORT).show();

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MapsActivity.this, "Market dragged to: \n" +
                                "Lat: "+marker.getPosition().latitude+"\n"+
                                "Lon: "+marker.getPosition().longitude+"\n",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }
}