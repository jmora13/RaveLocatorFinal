package com.example.ravelocator.Workers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ravelocator.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetLocationWorker extends Worker {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences sharedPref;
    private String sharedPrefFile = "sharedPrefFile";
    Location mLastLocation;

    public GetLocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        return null;
    }

//    private void getLocation() {
//        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getApplicationContext(), new String[]
//                            {Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION_PERMISSION);
//        } else {
//            mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                    new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                mLastLocation = location;
//                                sharedPref = getApplicationContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPref.edit();
//                                editor.putString("lat", String.valueOf(mLastLocation.getLatitude()));
//                                editor.putString("lon", String.valueOf(mLastLocation.getLongitude()));
//                                editor.apply();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "No Location", Toast.LENGTH_LONG).show();
//                                Log.d("Location", "No Location");
//                            }
//                        }
//                    });
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
//        onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_LOCATION_PERMISSION:
//                // If the permission is granted, get the location,
//                // otherwise, show a Toast
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "permission denied",
//                            Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
}
