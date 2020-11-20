package com.example.mapas.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.icu.util.ULocale;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mapas.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener, LocationListener {

    private View rootView;
    private GoogleMap mMap;
    private MapView mapView;

    private Marker marker;
    private CameraPosition camera;
    private Geocoder geocoder;
    private List<Address> addresses;

    private FloatingActionButton fab;

    private LocationManager locationManager;
    private Location currenLocation;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
//        TODO:revisar fab
        fab = rootView.findViewById(R.id.fba);
        fab.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);




        ////        activo el boton
//        mMap.setMyLocationEnabled(true);
////        lo quito de la interface
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//        locationManager.requestLocationUpdates((LocationManager.NETWORK_PROVIDER), 1000, 10, this);
//        locationManager.requestLocationUpdates((LocationManager.GPS_PROVIDER), 1000, 0, this);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);


        geocoder = new Geocoder(getContext(), Locale.getDefault());
    }

    // TODO:REVISAR checkIfGPSIsEnabel
    private boolean checkIfGPSIsEnabel() {
        try {
            int gpsSignal = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
            if (gpsSignal != 0) {
//                el gps no esta activo
                return true;

            } else {
                return true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showInfoAlert() {
        new AlertDialog.Builder(getContext())
                .setTitle("Señal GPS ")
                .setMessage("No tiene la señal  GPS activa, la quiere activar?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;


        //        capturar informacion
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();

        marker.setSnippet("address: " + address + "\n" +
                "address: " + city + "\n" +
                "address: " + state + "\n" +
                "address: " + country + "\n" +
                "address: " + postalCode);
        marker.showInfoWindow();

        Toasty.info(getActivity(), "address: " + address + "\n" +
                        "address: " + city + "\n" +
                        "address: " + state + "\n" +
                        "address: " + country + "\n" +
                        "address: " + postalCode
                , Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onClick(View v) {
        if (!this.checkIfGPSIsEnabel()) {
            showInfoAlert();
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location==null){
                location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            currenLocation=location;
            if (currenLocation!=null){
                createOrUpdateMarkerByLocation(location);
                zoomToLocation(location);



            }
        }
    }

    private void createOrUpdateMarkerByLocation(Location location){

        if (marker == null) {
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).draggable(true));
        } else {
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void zoomToLocation(Location location){
        camera=new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(),location.getLongitude()))
                .zoom(15)
                .bearing(0)
                .tilt(30)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toasty.info(getActivity(), "cambio -> " + location.getProvider(), Toast.LENGTH_SHORT).show();
        createOrUpdateMarkerByLocation(location);
       }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}



