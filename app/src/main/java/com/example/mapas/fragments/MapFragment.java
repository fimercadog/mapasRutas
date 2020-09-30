package com.example.mapas.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mapas.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private View view;
    private MapView mapView;
    private GoogleMap gMap;

    private List<Address> addresses;
    private Geocoder geocoder;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng place = new LatLng(4.60971, -74.08175);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        gMap.addMarker(new MarkerOptions().position(place).title("Marker in bogota").draggable(true));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        gMap.animateCamera(zoom);

        gMap.setOnMarkerDragListener(this);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

//
//        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                Toast.makeText(getActivity().getApplicationContext(), "Click on: \n"+
//                                "Lat: "+latLng.latitude+"\n"+
//                                "Lon: "+latLng.longitude+"\n",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                Toast.makeText(getActivity().getApplicationContext(), "Click on: \n"+
//                                "Lat: "+marker.getPosition().latitude+"\n"+
//                                "Lon: "+marker.getPosition().longitude+"\n",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        double latitude = marker.getPosition().latitude;
        double longitude = marker.getPosition().longitude;

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


        Toast.makeText(getContext(),
                "address: " + address + "\n" +
                        "city: " + city + "\n" +
                        "state: " + state + "\n" +
                        "country: " + country + "\n" +
                        "postalCode: " + postalCode
                , Toast.LENGTH_SHORT).show();


    }
}