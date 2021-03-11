package com.example.ravelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SplashActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Location mLastLocation;
    private RaveLocatorViewModel mRaveLocatorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // method to get the location
        mRaveLocatorViewModel = new ViewModelProvider(this).get(RaveLocatorViewModel.class);
        //mRaveLocatorViewModel.getLocation();
        mRaveLocatorViewModel.updateDatabase();
        getLocation();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLastLocation = location;
                                SharedPreferences sharedPref = PreferenceManager
                                        .getDefaultSharedPreferences(getApplication().getApplicationContext());
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("lat", String.valueOf(mLastLocation.getLatitude()));

                                editor.putString("lon", String.valueOf(mLastLocation.getLongitude()));
                                editor.putBoolean("settings_changed", false);
                                editor.apply();
                                mRaveLocatorViewModel.reverseGeocoding();
                                startActivity(new Intent(getApplication(), MainActivity.class));
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_LONG).show();
                                Log.d("Location", "No Location");
                            }
                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}