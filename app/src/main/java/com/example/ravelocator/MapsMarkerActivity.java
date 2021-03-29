package com.example.ravelocator;
// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.ravelocator.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private String lat;
    private String lon;
    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getApplication().getApplicationContext());
        lat = sharedPref.getString("lat", "");
        lon = sharedPref.getString("lon", "");
        state = sharedPref.getString("state" ,"State not found");
        LatLng location = new LatLng(Double.valueOf(lat), Double.valueOf(lon));

       // googleMap.setLatLngBoundsForCameraTarget(unitedStatesBounds);
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Searching for events near " + location))
                .setDraggable(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 7.0f));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Searching for events near " + latLng))
                        .setDraggable(true);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lat", String.valueOf(latLng.latitude));
                editor.putString("lon", String.valueOf(latLng.longitude));
                editor.putBoolean("settings_changed", true);
                editor.apply();
                Toast.makeText(MapsMarkerActivity.this, "Location Saved", Toast.LENGTH_SHORT).show();

            }
        });
    }

}

