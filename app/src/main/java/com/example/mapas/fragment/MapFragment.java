package com.example.mapas.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mapas.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private View rootView;
    private GoogleMap gMap;
    private MapView mapView;



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        rootView= inflater.inflate(R.layout.fragment_map, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView=rootView.findViewById(R.id.map);
        if (mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
        this.checkIfGPSIsEnabel();
    }
    @Override
    public void onMapReady(GoogleMap googleMap){

        gMap=googleMap;

        LatLng bogota = new LatLng(4.60971, -74.08175);
        gMap.addMarker(new MarkerOptions().position(bogota).title("saludos desde bogota"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
    }

    private void checkIfGPSIsEnabel(){
        //        preguntar por gps
        try {
            int gpsSignal=Settings.Secure.getInt(getActivity().getContentResolver(),
                    Settings.Secure.LOCATION_MODE);
            if (gpsSignal==0){


                //El gps no esta activado
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

}
